import Db.DbTruthSet;
import models.TruthSetModel;

/**
 * Created by juanjomen on 1/6/2016.
 */

public class TruthSetMapper extends BaseMapper{

  public static final String TRUTH_SET_PATH="truthSet";
  public static final String TRUTH_SET_ID_PATH="truthSet/{id}";
  public static final String TRUTH_SET_TRUTH_PATH="truthSet/{id}/truths";
  public static final String TRUTH_SET_TRUTH_ID_PATH="truthSet/{id}/truth/{truth_id}";
  public static final String TRUTH_SET_NAME_PATH="truthSet/{name}";

  public TruthSetMapper() { }

  /**
   * Create a {@link TruthSetModel} from a {@link DbTruthSet} instance.
   *
   * @param dbTruthSet a DbModel instance
   * @param path URL path to the application root
   * @return a NameModel instance
   */
  public TruthSetModel mapFrom(DbTruthSet dbTruthSet, String path) {
    TruthSetModel truthSetModel = new TruthSetModel();

    truthSetModel.setId(dbTruthSet.getId());
    truthSetModel.setName(dbTruthSet.getName());
    truthSetModel.setVersion(dbTruthSet.getVersion());
    truthSetModel.setType(dbTruthSet.getType());

    return truthSetModel;
  }

  /**
   * Create a {@link DbTruthSet} from a {@link TruthSetModel} instance.
   *
   * @param truthSetModel a NameModel instance
   * @return a DbModel instance
   */
  public DbTruthSet mapFrom(TruthSetModel truthSetModel) {
    return new DbTruthSet(truthSetModel.getId(), truthSetModel.getType(),truthSetModel.getName(),truthSetModel.getVersion());
  }

}
