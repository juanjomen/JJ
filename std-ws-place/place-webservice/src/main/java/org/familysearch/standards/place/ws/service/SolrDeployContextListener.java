package org.familysearch.standards.place.ws.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

import org.familysearch.standards.core.logging.Logger;
import org.familysearch.standards.place.data.solr.SolrDataService;
import org.familysearch.standards.place.service.DbDataService;
import org.familysearch.standards.place.ws.dbload.DbLoadManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


/**
 * Application Lifecycle Listener implementation class SolrDeployContextListener
 *
 * @author dshellman
 */
public class SolrDeployContextListener implements ServletContextListener {

    private static final Logger   logger = new Logger( SolrDeployContextListener.class );

    /** Location of empty solr-places.zip file */
    public static final String    SOLR_ZIP = "/std-ws-solr-common.zip";


    /**
     * Default constructor. 
     */
    public SolrDeployContextListener() { }


    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized( ServletContextEvent sce ) {
        logger.info("In SolrHomeContextListener ... contextInitialized");
        String solrHome = System.getProperty("solr.solr.home");
        logger.info("Solr home is '" + solrHome + "'");
        if (solrHome == null  ||  solrHome.trim().length() == 0) {
            logger.error("'solr.solr.home' is not defined ... exiting");
        }

        File solrDir = new File(solrHome);
        if (! solrDir.exists()) {
            // Create the directory and expand the ZIP file
            if (solrDir.mkdirs()) {
                logger.info("Solr home '" + solrHome + "' has been created.");
                installSolr(solrDir);
            } else {
                logger.error("Solr home '" + solrHome + "' could not be created!!");
            }
        } else if (! solrDir.isDirectory()) {
            // Error message and exit
            logger.error("Solr home '" + solrHome + "' exists but is not a directory!!");
        } else if (solrDir.list().length == 0) {
            // The directory is empty so expand the ZIP file
            installSolr(solrDir);
        } else {
            logger.info("Solr home '" + solrHome + "' exists, so no further processing need be done");
        }

        // Create and start the DB and Solr services, saving them into the servlet context
        this.setupServices(sce);
    }


    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed( ServletContextEvent sce ) {
        SolrDataService solrService = (SolrDataService)sce.getServletContext().getAttribute("solrService");
        DbDataService   dbService   = (DbDataService)sce.getServletContext().getAttribute("dbService");

        if (solrService != null) {
            solrService.shutdown();
        }

        if (dbService != null) {
            dbService.shutdown();
        }

        DbLoadManager.shutdown();
    }

    private boolean installSolr( File installDir ) {
        ZipInputStream			in = null;
        ZipEntry				entry;
        byte[]					buf = new byte[ 1024 ]; //arbitrary length

        try {
            in = new ZipInputStream( getClass().getResourceAsStream( SOLR_ZIP ) );
            entry = in.getNextEntry();
            while ( entry != null ) {
                String				fileName;
                File				file;
                FileOutputStream	fileOut;
                int					bufLength;

                fileName = entry.getName();
                file = new File( installDir.getAbsolutePath() + File.separator + fileName );
                if ( entry.isDirectory() ) {
                    logger.debug( "Solr Installation:  Creating new directory: " + file.getAbsolutePath() );
                    file.mkdirs();
                    entry = in.getNextEntry();
                    continue;
                }
                else if ( !file.getParentFile().exists() ) {
                    logger.debug( "Solr Installation:  Creating new directory: " + file.getParent() );
                    file.getParentFile().mkdirs();
                }
                logger.debug( "Solr Installation:  Installing solr file: " + file.getAbsolutePath() );

                fileOut = new FileOutputStream( file );
                bufLength = in.read( buf );
                while ( bufLength > 0 ) {
                    fileOut.write( buf, 0, bufLength );
                    bufLength = in.read( buf );
                }

                fileOut.close();
                entry = in.getNextEntry();
            }
        }
        catch ( IOException e ) {
            logger.error( "Solr Installation:  Error unzipping solr installation file.", e );
            return false;
        }
        finally {
            try {
                if ( in != null ) {
                    in.closeEntry();
                    in.close();
                }
            }
            catch ( IOException e ) {
                logger.error( "Solr installation:  Error closing solr installation file.", e );
            }
        }

        return true;
    }

    /**
     * Start-up the two main services [DB and Solr] and save them in the servlet context.
     * Both of these services are self-configuring, based on various properties files.
     * 
     * @param sce servlet context event, which contains the servlet context
     */
    private void setupServices(ServletContextEvent sce) {
        SolrDataService solrService = null;
        DbDataService   dbService   = null;
        DataSource      dataSource  = null;
        try {
            solrService = new SolrDataService();
            logger.info("SolrService created: " + solrService);
            logger.info("       Read & Write: " + solrService.isReadReady() + " & " + solrService.isWriteReady());
        } catch (Exception e) {
            logger.error("Error creating solr interface!", e);
        }

        try {
            ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
            logger.info("Checking Application-Context for beans, ctx=" + ctx);
            if (ctx != null) {
                // Look for a DbDataService bean
                try {
                    dbService = (DbDataService)ctx.getBean(DbDataService.class);
                } catch (BeansException ex) {
                    logger.info("No pre-existing DbDataService bean is defined.");
                }

                // Look for a DataSource bean
                try {
                    dataSource = (DataSource)ctx.getBean(DataSource.class);
                    logger.info("Existing DataSource bean? " + dataSource);
                } catch (BeansException ex) {
                    logger.info("No pre-existing DataSource bean is defined.");
                }
            }

            // Use an existing DbDataSource bean, or create a new one
            if (dbService != null) {
                logger.info("Using existing 'DbDataService' bean -- " + dbService);
            } else if (dataSource != null) {
                logger.info("Using existing 'DataSource' bean -- " + dataSource);
                dbService = new DbDataService(dataSource);
            } else {
                logger.info("Creating a new 'DbDataService' bean");
                dbService = new DbDataService();
            }
            logger.info("     Read & Write: " + dbService.isReadReady() + " & " + dbService.isWriteReady());
        } catch (Exception e) {
            logger.error("Error creating database interface!", e);
        }

        sce.getServletContext().setAttribute("solrService", solrService);
        sce.getServletContext().setAttribute("dbService", dbService);

        // Start the SOLR Master load if so directed ...
        String solrURL = System.getProperty("solr.master.url");
        String runLoad = System.getProperty("solr.master.load", "false");
        if (dataSource != null  &&  solrURL != null  && "true".equalsIgnoreCase(runLoad)) {
            logger.info("Starting the SOLR Master load daemon ... " + solrURL);
            DbLoadManager.startDbLoadManager(solrURL, dataSource);
        } else {
            logger.info("The SOLR Master load daemon will NOT be started... ");
        }
        
    }
}
