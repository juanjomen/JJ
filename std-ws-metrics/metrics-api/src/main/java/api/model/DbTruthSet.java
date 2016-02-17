package api.model;

/**
 * A "Name" consists of a text and StdLocale, bound to a unique identifier.  It
 * is non-modifiable.
 *
 * @author wjohnson000
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
