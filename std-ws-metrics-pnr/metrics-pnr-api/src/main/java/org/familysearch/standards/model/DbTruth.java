package org.familysearch.standards.model;

/**
 * Created by juanjomen on 2/4/2016.
 */
public class DbTruth {
  private Long     id;
  private Long     setId;
  private String   val;
  private String   contextVal;

  public DbTruth(
    Long id, Long setId, String val, String contextVal) {
    this.id = id;
    this.setId = setId;
    this.val = val;
    this.contextVal=contextVal;
  }

  /**
   * @return the truth identifier
   */
  public Long getId() {
    return id;
  }

  /**
   * @return the setId to where the truth is contained
   */
  public Long getSetId() {
    return setId;
  }

  /**
   * @return the value (search string)
   */
  public String getValue() {
    return val;
  }

  /**
   * @return the context value (search string optional parameters)
   */
  public String getContextValue() {
    return contextVal;
  }

}
