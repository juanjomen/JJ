package Db;

/**
 * Created by juanjomen on 1/11/2016.
 */
public class DbTruth {
  private Long     id;
  private Long     set_Id;
  private String   value;
  private String   contextValue;

  public DbTruth(
    Long id, Long set_Id, String value, String contextValue) {
    this.id = id;
    this.set_Id = set_Id;
    this.value = value;
    this.contextValue= contextValue;
  }

  public Long getId() {
    return id;
  }

  public Long getSet_Id() {
    return set_Id;
  }
  public String getValue() {
    return value;
  }
  public String getContextValue() {
    return contextValue;
  }
}
