package org.familysearch.standards.model;

/**
 * Created by juanjomen on 2/4/2016.
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
   * @return the truthSet identifier
   */
  public Long getId() {
    return id;
  }

  /**
   * @return the truth Set name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the truth Set type
   */
  public String getType() {
    return type;
  }

  /**
   * @return the truth Set version
   */
  public String getVersion() {
    return version;
  }
}
