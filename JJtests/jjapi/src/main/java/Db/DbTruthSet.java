package Db;

/**
 * Created by juanjomen on 1/11/2016.
 */
public class DbTruthSet {
  private Long id;
  private String name;
  private String type;
  private String version;


  public DbTruthSet(Long id, String name, String type,String version) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.version=version;
  }

  /**
   * @return the name identifier
   */
  public Long getId() {
    return id;
  }

  public String getType() {
    return type;
  }
  public String getVersion() {
    return version;
  }
  public String getName() {
    return name;
  }
}
