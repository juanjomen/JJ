import exception.NameSystemException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import services.ReadableMetricsService;
import services.WriteableMetricsService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Look in the Spring context for either a pre-made "ReadableNameService" or a pre-made
 * "DataSource".  If neither are found, then the service can't be started.
 *
 * @author wjohnson000
 */
public class DeployContextListener implements ServletContextListener {

  //private static final Logger logger = new Logger(DeployContextListener.class);

  private static final String MODULE_NAME = "ContextListener";

  private static final String myDbService = "name-db";

  /* (non-Javadoc)
   * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
   */
  @Override
  public void contextInitialized(ServletContextEvent sce) {
//    logger.debug("In DeployContextListener ... contextInitialized");
    try {
      setupServices(sce);
    } catch (NameSystemException e) {
      e.printStackTrace();
    }
  }

  /* (non-Javadoc)
   * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
   */
  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    ReadableMetricsService nameService = (ReadableMetricsService) sce.getServletContext().getAttribute("readableService");
    if (nameService != null) {
      nameService.shutdown();
    }
  }

  /**
   * Look for appropriate beans in the Spring web context.
   *
   * @param sce servlet context
   */
  private void setupServices(ServletContextEvent sce) throws NameSystemException {
    BasicDataSource dataSource = null;
    ReadableMetricsService readService = null;
    WriteableMetricsService writeService = null;

    try {
      ApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
  //    logger.info(null, MODULE_NAME, "Checking Application Context for beans.", "context", String.valueOf(appCtx));
      if (appCtx != null) {

        // Look for a DataSource bean
        try {
//          dataSource = appCtx.getBean(BasicDataSource.class);
//          logger.info(null, MODULE_NAME, "DataSource bean found.", "dataSource", String.valueOf(dataSource));
//          logger.info("Database URL: " + dataSource.getUrl());
//          logger.info("Database user: " + dataSource.getUsername());
//          logger.info("Database password: " + dataSource.getPassword());
        }
        catch (BeansException ex) {
       //   logger.info(null, MODULE_NAME, "No pre-existing DataSource bean defined.");
        }

        // Look for a read service bean
        try {
    //      readService = appCtx.getBean(ReadableMetricsService.class);
    //      logger.info(null, MODULE_NAME, "ReadableNameService bean found.", "readService", String.valueOf(readService));
        }
        catch (BeansException ex) {
   //       logger.info(null, MODULE_NAME, "No pre-existing readable database service bean is defined.");
        }

        // Look for a write service bean
        try {
     //     writeService = appCtx.getBean(WriteableMetricsService.class);
     //     logger.info(null, MODULE_NAME, "WriteableNameService bean found.", "writeService", String.valueOf(writeService));
        }
        catch (BeansException ex) {
       //   logger.info(null, MODULE_NAME, "No pre-existing writable database service bean is defined.");
        }
      }

      // Use an existing ReadableService or an existing DbDataSource bean, or we are dead
      if (readService != null) {
    //    logger.info(null, MODULE_NAME, "Using existing readable database service.", "service", String.valueOf(readService));
      }
      else if (dataSource != null) {
    //    logger.info(null, MODULE_NAME, "Using existing DataSource.", "dataSource", String.valueOf(dataSource));
        readService = new ReadableMetricsService(dataSource);
      }
      else {
     //   logger.error(null, MODULE_NAME, "Unable to create database service!");
      }

      // Use an existing WritableService or an existing DbDataSource bean, or we are dead
      if (writeService != null) {
   //     logger.info(null, MODULE_NAME, "Using existing writeable database service.", "service", String.valueOf(writeService));
      }
      else if (dataSource != null) {
     //   logger.info(null, MODULE_NAME, "Using existing DataSource.", "dataSource", String.valueOf(dataSource));
        writeService = new WriteableMetricsService(dataSource);
      }
      else {
   //     logger.error(null, MODULE_NAME, "Unable to create database service!");
      }
    }
    catch (NameSystemException e) {
  //    logger.error(e, MODULE_NAME, "Error creating database service!");
    }

    // Store the readable and writable services, if available
    if (readService != null) {
      sce.getServletContext().setAttribute("readableService", readService);
    }
    if (writeService != null) {
      sce.getServletContext().setAttribute("writeableService", writeService);
    }
  }

}
