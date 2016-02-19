package org.familysearch.standards.services;

import org.apache.commons.dbcp2.BasicDataSource;
import org.familysearch.standards.model.exception.PnR_SystemException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by juanjomen on 2/8/2016.
 */
@WebListener
public class DeployContextListener implements ServletContextListener {



  @Override
  public void contextInitialized(ServletContextEvent sce) {
    setupServices(sce);
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    ReadableMetricsService PnRService = (ReadableMetricsService) sce.getServletContext().getAttribute("readableService");
    if (PnRService != null) {
      PnRService.shutdown();
    }
  }

  private void setupServices(ServletContextEvent sce) {
    BasicDataSource dataSource = null;
    ReadableMetricsService readService = null;
    WriteableMetricsService writeService = null;

    try {

      ApplicationContext appCtx = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
      //logger.info(null, MODULE_NAME, "Checking Application Context for beans.", "context", String.valueOf(appCtx));
      if (appCtx != null) {

        // Look for a DataSource bean
        try {
          dataSource = appCtx.getBean(BasicDataSource.class);
        //  logger.info(null, MODULE_NAME, "DataSource bean found.", "dataSource", String.valueOf(dataSource));
        //  logger.info("Database URL: " + dataSource.getUrl());
        //  logger.info("Database user: " + dataSource.getUsername());
        //  logger.info("Database password: " + dataSource.getPassword());
        }
        catch (BeansException ex) {
         // logger.info(null, MODULE_NAME, "No pre-existing DataSource bean defined.");
        }

        // Look for a read service bean
        try {
          readService = appCtx.getBean(ReadableMetricsService.class);
          //logger.info(null, MODULE_NAME, "ReadableNameService bean found.", "readService", String.valueOf(readService));
        }
        catch (BeansException ex) {
          //logger.info(null, MODULE_NAME, "No pre-existing readable database service bean is defined.");
        }

        // Look for a write service bean
        try {
          writeService = appCtx.getBean(WriteableMetricsService.class);
         // logger.info(null, MODULE_NAME, "WriteableNameService bean found.", "writeService", String.valueOf(writeService));
        }
        catch (BeansException ex) {
         // logger.info(null, MODULE_NAME, "No pre-existing writable database service bean is defined.");
        }
      }

      // Use an existing ReadableService or an existing DbDataSource bean, or we are dead
      if (readService != null) {
        // logger.info(null, MODULE_NAME, "Using existing readable database service.", "service", String.valueOf(readService));
      }
      else if (dataSource != null) {
       // logger.info(null, MODULE_NAME, "Using existing DataSource.", "dataSource", String.valueOf(dataSource));
        readService = new ReadableMetricsService(dataSource);
      }
      else {
        //logger.error(null, MODULE_NAME, "Unable to create database service!");
      }
      // Use an existing WritableService or an existing DbDataSource bean, or we are dead
      if (writeService != null) {
        //logger.info(null, MODULE_NAME, "Using existing writeable database service.", "service", String.valueOf(writeService));
      }
      else if (dataSource != null) {
       // logger.info(null, MODULE_NAME, "Using existing DataSource.", "dataSource", String.valueOf(dataSource));
        writeService = new WriteableMetricsService(dataSource);
      }
      else {
       // logger.error(null, MODULE_NAME, "Unable to create database service!");
      }
    }
    catch (PnR_SystemException e) {
      //logger.error(e, MODULE_NAME, "Error creating database service!");
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
