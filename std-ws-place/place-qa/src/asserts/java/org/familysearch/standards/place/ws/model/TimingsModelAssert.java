package org.familysearch.standards.place.ws.model;

import org.assertj.core.api.AbstractAssert;
// Assertions is needed if an assertion for Iterable is generated


/**
 * {@link TimingsModel} specific assertions - Generated by CustomAssertionGenerator.
 */
public class TimingsModelAssert extends AbstractAssert<TimingsModelAssert, TimingsModel> {

  /**
   * Creates a new </code>{@link TimingsModelAssert}</code> to make assertions on actual TimingsModel.
   * @param actual the TimingsModel we want to make assertions on.
   */
  public TimingsModelAssert(TimingsModel actual) {
    super(actual, TimingsModelAssert.class);
  }

  /**
   * An entry point for TimingsModelAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one's can write directly : <code>assertThat(myTimingsModel)</code> and get specific assertion with code completion.
   * @param actual the TimingsModel we want to make assertions on.
   * @return a new </code>{@link TimingsModelAssert}</code>
   */
  public static TimingsModelAssert assertThat(TimingsModel actual) {
    return new TimingsModelAssert(actual);
  }

  /**
   * Verifies that the actual TimingsModel's assemblyTime is equal to the given one.
   * @param assemblyTime the given assemblyTime to compare the actual TimingsModel's assemblyTime to.
   * @return this assertion object.
   * @throws AssertionError - if the actual TimingsModel's assemblyTime is not equal to the given one.
   */
  public TimingsModelAssert hasAssemblyTime(Long assemblyTime) {
    // check that actual TimingsModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected assemblyTime of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    Long actualAssemblyTime = actual.getAssemblyTime();
    if (!org.assertj.core.util.Objects.areEqual(actualAssemblyTime, assemblyTime)) {
      failWithMessage(assertjErrorMessage, actual, assemblyTime, actualAssemblyTime);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual TimingsModel's identifyCandidatesLookupTime is equal to the given one.
   * @param identifyCandidatesLookupTime the given identifyCandidatesLookupTime to compare the actual TimingsModel's identifyCandidatesLookupTime to.
   * @return this assertion object.
   * @throws AssertionError - if the actual TimingsModel's identifyCandidatesLookupTime is not equal to the given one.
   */
  public TimingsModelAssert hasIdentifyCandidatesLookupTime(Long identifyCandidatesLookupTime) {
    // check that actual TimingsModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected identifyCandidatesLookupTime of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    Long actualIdentifyCandidatesLookupTime = actual.getIdentifyCandidatesLookupTime();
    if (!org.assertj.core.util.Objects.areEqual(actualIdentifyCandidatesLookupTime, identifyCandidatesLookupTime)) {
      failWithMessage(assertjErrorMessage, actual, identifyCandidatesLookupTime, actualIdentifyCandidatesLookupTime);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual TimingsModel's identifyCandidatesMaxHitFilterTime is equal to the given one.
   * @param identifyCandidatesMaxHitFilterTime the given identifyCandidatesMaxHitFilterTime to compare the actual TimingsModel's identifyCandidatesMaxHitFilterTime to.
   * @return this assertion object.
   * @throws AssertionError - if the actual TimingsModel's identifyCandidatesMaxHitFilterTime is not equal to the given one.
   */
  public TimingsModelAssert hasIdentifyCandidatesMaxHitFilterTime(Long identifyCandidatesMaxHitFilterTime) {
    // check that actual TimingsModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected identifyCandidatesMaxHitFilterTime of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    Long actualIdentifyCandidatesMaxHitFilterTime = actual.getIdentifyCandidatesMaxHitFilterTime();
    if (!org.assertj.core.util.Objects.areEqual(actualIdentifyCandidatesMaxHitFilterTime, identifyCandidatesMaxHitFilterTime)) {
      failWithMessage(assertjErrorMessage, actual, identifyCandidatesMaxHitFilterTime, actualIdentifyCandidatesMaxHitFilterTime);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual TimingsModel's identifyCandidatesTailMatchTime is equal to the given one.
   * @param identifyCandidatesTailMatchTime the given identifyCandidatesTailMatchTime to compare the actual TimingsModel's identifyCandidatesTailMatchTime to.
   * @return this assertion object.
   * @throws AssertionError - if the actual TimingsModel's identifyCandidatesTailMatchTime is not equal to the given one.
   */
  public TimingsModelAssert hasIdentifyCandidatesTailMatchTime(Long identifyCandidatesTailMatchTime) {
    // check that actual TimingsModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected identifyCandidatesTailMatchTime of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    Long actualIdentifyCandidatesTailMatchTime = actual.getIdentifyCandidatesTailMatchTime();
    if (!org.assertj.core.util.Objects.areEqual(actualIdentifyCandidatesTailMatchTime, identifyCandidatesTailMatchTime)) {
      failWithMessage(assertjErrorMessage, actual, identifyCandidatesTailMatchTime, actualIdentifyCandidatesTailMatchTime);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual TimingsModel's identifyCandidatesTime is equal to the given one.
   * @param identifyCandidatesTime the given identifyCandidatesTime to compare the actual TimingsModel's identifyCandidatesTime to.
   * @return this assertion object.
   * @throws AssertionError - if the actual TimingsModel's identifyCandidatesTime is not equal to the given one.
   */
  public TimingsModelAssert hasIdentifyCandidatesTime(Long identifyCandidatesTime) {
    // check that actual TimingsModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected identifyCandidatesTime of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    Long actualIdentifyCandidatesTime = actual.getIdentifyCandidatesTime();
    if (!org.assertj.core.util.Objects.areEqual(actualIdentifyCandidatesTime, identifyCandidatesTime)) {
      failWithMessage(assertjErrorMessage, actual, identifyCandidatesTime, actualIdentifyCandidatesTime);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual TimingsModel's parseTime is equal to the given one.
   * @param parseTime the given parseTime to compare the actual TimingsModel's parseTime to.
   * @return this assertion object.
   * @throws AssertionError - if the actual TimingsModel's parseTime is not equal to the given one.
   */
  public TimingsModelAssert hasParseTime(Long parseTime) {
    // check that actual TimingsModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected parseTime of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    Long actualParseTime = actual.getParseTime();
    if (!org.assertj.core.util.Objects.areEqual(actualParseTime, parseTime)) {
      failWithMessage(assertjErrorMessage, actual, parseTime, actualParseTime);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual TimingsModel's scoringTime is equal to the given one.
   * @param scoringTime the given scoringTime to compare the actual TimingsModel's scoringTime to.
   * @return this assertion object.
   * @throws AssertionError - if the actual TimingsModel's scoringTime is not equal to the given one.
   */
  public TimingsModelAssert hasScoringTime(Long scoringTime) {
    // check that actual TimingsModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected scoringTime of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    Long actualScoringTime = actual.getScoringTime();
    if (!org.assertj.core.util.Objects.areEqual(actualScoringTime, scoringTime)) {
      failWithMessage(assertjErrorMessage, actual, scoringTime, actualScoringTime);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual TimingsModel's totalTime is equal to the given one.
   * @param totalTime the given totalTime to compare the actual TimingsModel's totalTime to.
   * @return this assertion object.
   * @throws AssertionError - if the actual TimingsModel's totalTime is not equal to the given one.
   */
  public TimingsModelAssert hasTotalTime(Long totalTime) {
    // check that actual TimingsModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected totalTime of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    Long actualTotalTime = actual.getTotalTime();
    if (!org.assertj.core.util.Objects.areEqual(actualTotalTime, totalTime)) {
      failWithMessage(assertjErrorMessage, actual, totalTime, actualTotalTime);
    }

    // return the current assertion for method chaining
    return this;
  }

}
