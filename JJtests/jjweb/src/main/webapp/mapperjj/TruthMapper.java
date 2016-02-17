import Db.DbTruth;
import models.TruthModel;

/**
 * Utility class to map between {@link DbTruth} and {@link TruthModel} types.
 * It is thread-safe since it doesn't use any instance variables.
 *
 * @author wjohnson000
 *
 */
public class TruthMapper extends BaseMapper {

  public static final String TRUTH_PATH="";

  public TruthMapper() { }

  /**
   * Create a {@link TruthModel} from a {@link DbTruth} instance.
   *
   * @param dbTruth a DbModel instance
   * @param path URL path to the application root
   * @return a AttributeModel instance
   */
  public TruthModel mapFrom(DbTruth dbTruth, String path) {
    TruthModel tModel = new TruthModel();

    tModel.setId(dbTruth.getId());
    tModel.setSet_Id(dbTruth.getSet_Id());
    tModel.setValue(dbTruth.getValue());
    tModel.setContextValue(dbTruth.getContextValue());

    return tModel;
  }

  /**
   * Create a {@link DbTruth} from a {@link TruthModel} instance.
   *
   * @param tModel a AttributeModel instance
   * @return a DbModel instance
   */
  public DbTruth mapFrom(TruthModel tModel) {
    return new DbTruth(
      tModel.getId(),
      tModel.getSet_Id(),
      tModel.getValue(),
      tModel.getContextValue());
  }
}
