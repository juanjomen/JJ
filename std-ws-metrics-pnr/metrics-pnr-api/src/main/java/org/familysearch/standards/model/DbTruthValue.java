package org.familysearch.standards.model;

/**
 * Created by juanjomen on 2/4/2016.
 */
public class DbTruthValue {
  private Long     id;
  private Long     truthId;
  private String   val;
  private String   contextVal;
  private Long     score;
  private Boolean  isPositive;


  public DbTruthValue(Long id, Long truthId, String val, String contextVal, Long score, Boolean isPositive) {
    this.isPositive = isPositive;
    this.score = score;
    this.contextVal = contextVal;
    this.val = val;
    this.truthId = truthId;
    this.id = id;
  }

  /**
   * @return the truthValue identifier
   */
  public Long getId() {
    return id;
  }

  /**
   * @return the truth associated with this truth Value
   */
  public Long getTruthId() {
    return truthId;
  }

  /**
   * @return truth if this value should be retrieve (true-positive)
   */
  public Boolean getIsPositive() {
    return isPositive;
  }

  /**
   * @return the score for the truth value (for now all same " 1")
   */
  public Long getScore() {
    return score;
  }

  /**
   * @return additional data associated with the TRUTH )
   */
  public String getContextVal() {
    return contextVal;
  }

  /**
   * @return value expected from the results)
   */
  public String getVal() {
    return val;
  }
}


