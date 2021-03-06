package org.familysearch.standards.place.ws.model;

import org.assertj.core.api.AbstractAssert;
// Assertions is needed if an assertion for Iterable is generated


/**
 * {@link CountsModel} specific assertions - Generated by CustomAssertionGenerator.
 */
public class CountsModelAssert extends AbstractAssert<CountsModelAssert, CountsModel> {

  /**
   * Creates a new </code>{@link CountsModelAssert}</code> to make assertions on actual CountsModel.
   * @param actual the CountsModel we want to make assertions on.
   */
  public CountsModelAssert(CountsModel actual) {
    super(actual, CountsModelAssert.class);
  }

  /**
   * An entry point for CountsModelAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one's can write directly : <code>assertThat(myCountsModel)</code> and get specific assertion with code completion.
   * @param actual the CountsModel we want to make assertions on.
   * @return a new </code>{@link CountsModelAssert}</code>
   */
  public static CountsModelAssert assertThat(CountsModel actual) {
    return new CountsModelAssert(actual);
  }

  /**
   * Verifies that the actual CountsModel's finalParsedInputTextCount is equal to the given one.
   * @param finalParsedInputTextCount the given finalParsedInputTextCount to compare the actual CountsModel's finalParsedInputTextCount to.
   * @return this assertion object.
   * @throws AssertionError - if the actual CountsModel's finalParsedInputTextCount is not equal to the given one.
   */
  public CountsModelAssert hasFinalParsedInputTextCount(Integer finalParsedInputTextCount) {
    // check that actual CountsModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected finalParsedInputTextCount of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    Integer actualFinalParsedInputTextCount = actual.getFinalParsedInputTextCount();
    if (!org.assertj.core.util.Objects.areEqual(actualFinalParsedInputTextCount, finalParsedInputTextCount)) {
      failWithMessage(assertjErrorMessage, actual, finalParsedInputTextCount, actualFinalParsedInputTextCount);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual CountsModel's initialParsedInputTextCount is equal to the given one.
   * @param initialParsedInputTextCount the given initialParsedInputTextCount to compare the actual CountsModel's initialParsedInputTextCount to.
   * @return this assertion object.
   * @throws AssertionError - if the actual CountsModel's initialParsedInputTextCount is not equal to the given one.
   */
  public CountsModelAssert hasInitialParsedInputTextCount(Integer initialParsedInputTextCount) {
    // check that actual CountsModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected initialParsedInputTextCount of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    Integer actualInitialParsedInputTextCount = actual.getInitialParsedInputTextCount();
    if (!org.assertj.core.util.Objects.areEqual(actualInitialParsedInputTextCount, initialParsedInputTextCount)) {
      failWithMessage(assertjErrorMessage, actual, initialParsedInputTextCount, actualInitialParsedInputTextCount);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual CountsModel's preScoringCandidateCount is equal to the given one.
   * @param preScoringCandidateCount the given preScoringCandidateCount to compare the actual CountsModel's preScoringCandidateCount to.
   * @return this assertion object.
   * @throws AssertionError - if the actual CountsModel's preScoringCandidateCount is not equal to the given one.
   */
  public CountsModelAssert hasPreScoringCandidateCount(Integer preScoringCandidateCount) {
    // check that actual CountsModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected preScoringCandidateCount of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    Integer actualPreScoringCandidateCount = actual.getPreScoringCandidateCount();
    if (!org.assertj.core.util.Objects.areEqual(actualPreScoringCandidateCount, preScoringCandidateCount)) {
      failWithMessage(assertjErrorMessage, actual, preScoringCandidateCount, actualPreScoringCandidateCount);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual CountsModel's rawCandidateCount is equal to the given one.
   * @param rawCandidateCount the given rawCandidateCount to compare the actual CountsModel's rawCandidateCount to.
   * @return this assertion object.
   * @throws AssertionError - if the actual CountsModel's rawCandidateCount is not equal to the given one.
   */
  public CountsModelAssert hasRawCandidateCount(Integer rawCandidateCount) {
    // check that actual CountsModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected rawCandidateCount of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    Integer actualRawCandidateCount = actual.getRawCandidateCount();
    if (!org.assertj.core.util.Objects.areEqual(actualRawCandidateCount, rawCandidateCount)) {
      failWithMessage(assertjErrorMessage, actual, rawCandidateCount, actualRawCandidateCount);
    }

    // return the current assertion for method chaining
    return this;
  }

}
