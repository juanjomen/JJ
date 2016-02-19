package org.familysearch.standards.services;

import org.apache.commons.dbcp2.BasicDataSource;
import org.familysearch.standards.TruthDAO;
import org.familysearch.standards.TruthSetDAO;
import org.familysearch.standards.TruthValueDAO;
import org.familysearch.standards.impl.TruthDAOImpl;
import org.familysearch.standards.impl.TruthSetDAOImpl;
import org.familysearch.standards.impl.TruthValueDAOImpl;
import org.familysearch.standards.model.exception.PnR_SystemException;

import java.util.Objects;

/**
 * Created by juanjomen on 2/8/2016.
 */
public class BaseMetricsService {
  protected  static final String MODULE_NAME= "PNR_SERVICE";

  private TruthDAO truthDAO;
  private TruthSetDAO truthSetDAO;
  private TruthValueDAO truthValueDAO;

  public BaseMetricsService(BasicDataSource dataSource)throws PnR_SystemException{
    Objects.requireNonNull(dataSource,"DataSource can not be null");
    truthDAO = new TruthDAOImpl(dataSource);
    truthSetDAO = new TruthSetDAOImpl(dataSource);
    truthValueDAO = new TruthValueDAOImpl(dataSource);

  }


  public TruthDAO getTruthDAO() {
    return truthDAO;
  }

  public TruthSetDAO getTruthSetDAO() {
    return truthSetDAO;
  }

  public TruthValueDAO getTruthValueDAO() {
    return truthValueDAO;
  }
}
