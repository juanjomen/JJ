package services;

import api.TruthDAO;
import api.TruthDAOImpl;
import api.TruthSetDAO;
import api.TruthSetDAOImpl;
import exception.NameSystemException;
import org.apache.commons.dbcp2.BasicDataSource;

import java.util.Objects;


public class BaseMetricsService {

    protected static final String MODULE_NAME = "METRICS_SERVICE";

    private TruthDAO truthDAO;
    private TruthSetDAO truthSetDAO;



    public BaseMetricsService(BasicDataSource dataSource) throws NameSystemException {
        Objects.requireNonNull(dataSource, "DataSource can not be null");
        truthDAO = new TruthDAOImpl(dataSource);
        truthSetDAO = new TruthSetDAOImpl(dataSource);
       // typeDAO = new TypeDAOImpl(dataSource);
    }

    protected TruthDAO getTruthDAO() {
        return truthDAO;
    }

    protected TruthSetDAO getTruthSetDAO() {
        return truthSetDAO;
    }

//    protected TypeDAO getTypeDAO() {
//        return typeDAO;
//    }
}
