package org.familysearch.standards.place.ws.dbload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.familysearch.standards.core.logging.Logger;
import org.familysearch.standards.place.data.PlaceDataException;
import org.familysearch.standards.place.data.solr.SolrConnection;
import org.familysearch.standards.place.solr.load.LoadSolrProcessor;


/**
 * In order to fit into the GIT+EC+AWS box, an empty SOLR master repository is deployed;
 * the responsibility of ensuring that the repository gets loaded falls to the web
 * service.  This class primarily does two things:
 * <ul>
 *   <li><strong>Check status</strong> - query the SOLR master to determine whether
 *       the documents have been loaded</li>
 *   <li><strong>Start load</strong> - take control of loading the SOLR master from
 *       the database</li>
 * </ul>
 * 
 * Since multiple web servers will talk to a single SOLR master, each one will ask the
 * master if is ready for replication.  If not, it'll take control and start the load.
 * Since the master could go down -- or the server which initiated the load could go
 * down -- we periodically check to see if the load needs to be re-started.
 * 
 * @author wjohnson000
 *
 */
public class DbLoadManager {

    private static final Logger LOGGER = new Logger(DbLoadManager.class);

    /** Time-out values*/
    private static int readTimeOut    = 12000;
    private static int connectTimeOut = 5000;

    /** Response values that we care about ... */
    private static Set<String> responseKeys = new HashSet<>(Arrays.asList(new String[] { "status", "procId" }));

    /** Singleton instance */
    private static DbLoadManager onlyInstance = null;

    private int         loadCount = 0; 
    private String      processId = String.valueOf(System.nanoTime());
    private DataSource  dbSource = null;
    private String      solrURL  = "";
    private ScheduledExecutorService fScheduler;

    /**
     * Private constructor ... enforced singleton
     */
    private DbLoadManager() { }

    /**
     * Setup and start the DbLoadManager, if not already running ...
     *  
     * @param solrURL SOLR master URL
     * @param dbSource data-source for DB connections
     */
    public static void startDbLoadManager(String solrURL, DataSource dbSource) {
        LOGGER.info("Testing whether to start the DbLoadManager thread ... only-instance: " + onlyInstance);
        if (onlyInstance == null) {
            onlyInstance = new DbLoadManager();
            onlyInstance.solrURL = solrURL;
            onlyInstance.dbSource = dbSource;
            onlyInstance.fScheduler = Executors.newScheduledThreadPool(3);
            onlyInstance.startReplicationCheck();
        }
    }

    /**
     * Shutdown the load manager by stopping all running threads ...
     */
    public static void shutdown() {
        if (onlyInstance != null) {
            onlyInstance.fScheduler.shutdown();
        }
    }

    /**
     * Start the replication check, which will ask the master for its status to determine
     * if the load process needs to be started.
     */
    private void startReplicationCheck() {
        LOGGER.info("Start the DbLoadCheck thread ...");

        fScheduler.scheduleWithFixedDelay(
            new Runnable() {
                @Override
                public void run() {
                    replicationCheck();
                }
            },
            3,    // Initial delay = 3 seconds
            60,   // Delay between execution = 60 seconds
            TimeUnit.SECONDS);

        LOGGER.info("DbLoadCheck thread started ...");
    }

    /**
     * Run the replication check, to determine if we need to start the DB load
     * process.
     */
    private void replicationCheck() {
        // Retrieve the current status; if "new", then request that this server be
        // allowed to do the population.  If granted, start the load.
        Map<String,String> response = pingSolrServer("status");
        LOGGER.info("DbLoadCheck.status=" + response.get("status"));

        if ("new".equals(response.get("status"))  ||  "stalled".equals(response.get("status"))) {
            response = pingSolrServer("start");
            if ("in-progress".equals(response.get("status"))  &&  processId.equals(response.get("procId"))) {
                startDbLoad();
            }
        }
    }

    /**
     * Start the DB load process.  Keep running until either: 1) all documents are loaded,
     * or 2) this stalls out and another process picks it up ...
     */
    private void startDbLoad() {
        // Define the temporary directory where files are to be stored, and the
        // database connectivity parameter defaults
        final String tempDir = System.getProperty("java.io.tmpdir", "/tmp/db-flat-file");
        LOGGER.info("Starting the DB-LOAD ... tempDir: " + tempDir);

        // Ensure that the directory exists or can be created
        File file = new File(tempDir);
        if (! file.exists()) {
            if (! file.mkdirs()) {
                LOGGER.error("Unable to create the temporary directory: " + tempDir);
                return;
            }
        } else if (! file.isDirectory()) {
            LOGGER.error("Temporary 'directory' exists but isn't a directory: " + tempDir);
            return;
        }

        // Start the load process in a separate thread
        loadCount = 0;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                SolrConnection solrConn;
                try {
                    solrConn = SolrConnection.connectToRemoteInstance(solrURL);
                    LoadSolrProcessor processor = new LoadSolrProcessor(getUsableConnection(), solrConn);
                    loadCount = processor.runLoad(tempDir, true, true, true);
                } catch (PlaceDataException | SQLException ex) {
                    LOGGER.error("Data load failed ... ", ex);
                }
            }
        };
        ScheduledFuture<?> future = fScheduler.schedule(runnable, 1, TimeUnit.SECONDS);

        // Wait for the load thread to finish, doing period "commit" calls to let the
        // server know that we're still alive.  Make sure that the remote master still
        // thinks this server is doing the load.
        while (! (future.isDone()  ||  future.isCancelled())) {
            Map<String,String> response = pingSolrServer("commit");
            if ("in-progress".equals(response.get("status"))  &&  processId.equals(response.get("procId"))) {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException ex) {
                    LOGGER.debug("Couldn't sleep ... " +  ex.getMessage());
                }
            } else {
                future.cancel(true);
            }
        }

        LOGGER.info("Load Count: " + loadCount);
        if (loadCount > 0) {
            Map<String,String> response = pingSolrServer("finish");
            LOGGER.info("Database Load finished -- SOLR status: " + response.get("status"));
        }
    }

    /**
     * Issue a GET request against the replication-status endpoint, returning the
     * current status, process-id and last-update delta.
     * 
     * @param action action to perform
     * @return map w/ "status", "procId" and "lastUpdate" keys
     */
    private Map<String,String> pingSolrServer(String action) {
        LOGGER.debug("Checking replication status ... action=" + action);

        Map<String,String> results = new HashMap<>();
        try {
            URL serviceURL = new URL(solrURL + "/replicationStatus?action=" + action + "&procId=" + processId + "&wt=json");
            URLConnection urlConn = serviceURL.openConnection();

            urlConn.setConnectTimeout(connectTimeOut);
            urlConn.setReadTimeout(readTimeOut);
            urlConn.setRequestProperty("Accept-Language", "en");
            urlConn.setRequestProperty("Accept-Charset", "utf-8");
            urlConn.setRequestProperty("Accept", "application/xml");

            if (urlConn instanceof HttpURLConnection) {
                HttpURLConnection httpUrlConn = (HttpURLConnection)urlConn;
                httpUrlConn.setRequestMethod("GET");
                httpUrlConn.setConnectTimeout(30000);
                httpUrlConn.connect();
                results = parseResponse(httpUrlConn);
            }
        } catch (MalformedURLException ex) {
            LOGGER.error("Unable to check replication-status", ex);
        } catch (IOException ex) {
            LOGGER.error("Unable to check replication-status", ex);
        }

        LOGGER.debug("  replication status results: " + results);
        return results;
    }

    /**
     * Handle the response of the HttpURLConnection.
     * 
     * @param httpUrlConn HttpURLConnection
     * 
     * @return Map of Key/Value pairs
     * @throws IOException 
     */
    private static Map<String,String> parseResponse(HttpURLConnection httpUrlConn) throws IOException {
        Map<String,String> results = new HashMap<>();

        int status = httpUrlConn.getResponseCode();
        if (status == 200  ||  status == 201) {
            int len = 0;
            byte[] buffer = new byte[1024*16];
            ByteArrayOutputStream bais = new ByteArrayOutputStream();
            InputStream inStr = httpUrlConn.getInputStream();
            while ((len = inStr.read(buffer)) > 0) {
                bais.write(buffer, 0, len);
            }

            String respText = bais.toString();
            String[] chunks = respText.replace('"',  ' ').replace('}', ' ').split(",");
            for (String chunk : chunks) {
                String[] keyValue = chunk.split(":");
                String key = keyValue[0].trim();
                String val = keyValue[1].trim();
                if (responseKeys.contains(key)) {
                    results.put(key, val);
                }
            }
        }

        return results;
    }

    /**
     * Unwrap the connection from the data-source looking for a PostgreSQL connection.
     * 
     * @return PostgreSQL connection
     */
    private Connection getUsableConnection() throws SQLException {
        Connection dbConn = dbSource.getConnection().getMetaData().getConnection();

        if (dbConn != null  &&  dbConn instanceof org.apache.commons.dbcp.PoolableConnection) {
            org.apache.commons.dbcp.PoolableConnection poolConn = (org.apache.commons.dbcp.PoolableConnection)dbConn;
            Connection delegateConn = poolConn.getDelegate();
            if (delegateConn != null  &&  delegateConn instanceof org.postgresql.core.BaseConnection) {
                return delegateConn;
            }

            Connection innerConn = poolConn.getInnermostDelegate();
            if (innerConn != null  &&  innerConn instanceof org.postgresql.core.BaseConnection) {
                return innerConn;
            }
        }

        return dbSource.getConnection();
    }
}
