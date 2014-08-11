package org.familysearch.standards.place.ws.model;

import org.assertj.core.api.AbstractAssert;
// Assertions is needed if an assertion for Iterable is generated
import org.assertj.core.api.Assertions;


/**
 * {@link ScorersModel} specific assertions - Generated by CustomAssertionGenerator.
 */
public class ScorersModelAssert extends AbstractAssert<ScorersModelAssert, ScorersModel> {

  /**
   * Creates a new </code>{@link ScorersModelAssert}</code> to make assertions on actual ScorersModel.
   * @param actual the ScorersModel we want to make assertions on.
   */
  public ScorersModelAssert(ScorersModel actual) {
    super(actual, ScorersModelAssert.class);
  }

  /**
   * An entry point for ScorersModelAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one's can write directly : <code>assertThat(myScorersModel)</code> and get specific assertion with code completion.
   * @param actual the ScorersModel we want to make assertions on.
   * @return a new </code>{@link ScorersModelAssert}</code>
   */
  public static ScorersModelAssert assertThat(ScorersModel actual) {
    return new ScorersModelAssert(actual);
  }

  /**
   * Verifies that the actual ScorersModel's scorers contains the given ScorerModel elements.
   * @param scorers the given elements that should be contained in actual ScorersModel's scorers.
   * @return this assertion object.
   * @throws AssertionError if the actual ScorersModel's scorers does not contain all given ScorerModel elements.
   */
  public ScorersModelAssert hasScorers(ScorerModel... scorers) {
    // check that actual ScorersModel we want to make assertions on is not null.
    isNotNull();

    // check that given ScorerModel varargs is not null.
    if (scorers == null) throw new AssertionError("Expecting scorers parameter not to be null.");
    
    // check with standard error message (see commented below to set your own message).
    Assertions.assertThat(actual.getScorers()).contains(scorers);

    // To override the standard error message :
    // - remove the previous call to Assertions.assertThat(actual.getScorers().contains(scorers)
    // - uncomment the line below and set your error message:
    // Assertions.assertThat(actual.getScorers()).overridingErrorMessage("\nmy error message %s", "arg1").contains(scorers);

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual ScorersModel has no scorers.
   * @return this assertion object.
   * @throws AssertionError if the actual ScorersModel's scorers is not empty.
   */
  public ScorersModelAssert hasNoScorers() {
    // check that actual ScorersModel we want to make assertions on is not null.
    isNotNull();

    // we override the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected :\n  <%s>\nnot to have scorers but had :\n  <%s>";
    
    // check
    if (!actual.getScorers().isEmpty()) {
      failWithMessage(assertjErrorMessage, actual, actual.getScorers());
    }
    
    // return the current assertion for method chaining
    return this;
  }
  

}
