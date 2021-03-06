package org.familysearch.standards.place.ws.model;

import org.assertj.core.api.AbstractAssert;
// Assertions is needed if an assertion for Iterable is generated
import org.assertj.core.api.Assertions;


/**
 * {@link PlaceSearchResultsModel} specific assertions - Generated by CustomAssertionGenerator.
 */
public class PlaceSearchResultsModelAssert extends AbstractAssert<PlaceSearchResultsModelAssert, PlaceSearchResultsModel> {

  /**
   * Creates a new </code>{@link PlaceSearchResultsModelAssert}</code> to make assertions on actual PlaceSearchResultsModel.
   * @param actual the PlaceSearchResultsModel we want to make assertions on.
   */
  public PlaceSearchResultsModelAssert(PlaceSearchResultsModel actual) {
    super(actual, PlaceSearchResultsModelAssert.class);
  }

  /**
   * An entry point for PlaceSearchResultsModelAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one's can write directly : <code>assertThat(myPlaceSearchResultsModel)</code> and get specific assertion with code completion.
   * @param actual the PlaceSearchResultsModel we want to make assertions on.
   * @return a new </code>{@link PlaceSearchResultsModelAssert}</code>
   */
  public static PlaceSearchResultsModelAssert assertThat(PlaceSearchResultsModel actual) {
    return new PlaceSearchResultsModelAssert(actual);
  }

  /**
   * Verifies that the actual PlaceSearchResultsModel's count is equal to the given one.
   * @param count the given count to compare the actual PlaceSearchResultsModel's count to.
   * @return this assertion object.
   * @throws AssertionError - if the actual PlaceSearchResultsModel's count is not equal to the given one.
   */
  public PlaceSearchResultsModelAssert hasCount(int count) {
    // check that actual PlaceSearchResultsModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected count of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // check
    int actualCount = actual.getCount();
    if (actualCount != count) {
      failWithMessage(assertjErrorMessage, actual, count, actualCount);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual PlaceSearchResultsModel's metrics is equal to the given one.
   * @param metrics the given metrics to compare the actual PlaceSearchResultsModel's metrics to.
   * @return this assertion object.
   * @throws AssertionError - if the actual PlaceSearchResultsModel's metrics is not equal to the given one.
   */
  public PlaceSearchResultsModelAssert hasMetrics(MetricsModel metrics) {
    // check that actual PlaceSearchResultsModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected metrics of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    MetricsModel actualMetrics = actual.getMetrics();
    if (!org.assertj.core.util.Objects.areEqual(actualMetrics, metrics)) {
      failWithMessage(assertjErrorMessage, actual, metrics, actualMetrics);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual PlaceSearchResultsModel's refId is equal to the given one.
   * @param refId the given refId to compare the actual PlaceSearchResultsModel's refId to.
   * @return this assertion object.
   * @throws AssertionError - if the actual PlaceSearchResultsModel's refId is not equal to the given one.
   */
  public PlaceSearchResultsModelAssert hasRefId(int refId) {
    // check that actual PlaceSearchResultsModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected refId of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // check
    int actualRefId = actual.getRefId();
    if (actualRefId != refId) {
      failWithMessage(assertjErrorMessage, actual, refId, actualRefId);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual PlaceSearchResultsModel's results contains the given PlaceSearchResultModel elements.
   * @param results the given elements that should be contained in actual PlaceSearchResultsModel's results.
   * @return this assertion object.
   * @throws AssertionError if the actual PlaceSearchResultsModel's results does not contain all given PlaceSearchResultModel elements.
   */
  public PlaceSearchResultsModelAssert hasResults(PlaceSearchResultModel... results) {
    // check that actual PlaceSearchResultsModel we want to make assertions on is not null.
    isNotNull();

    // check that given PlaceSearchResultModel varargs is not null.
    if (results == null) throw new AssertionError("Expecting results parameter not to be null.");
    
    // check with standard error message (see commented below to set your own message).
    Assertions.assertThat(actual.getResults()).contains(results);

    // To override the standard error message :
    // - remove the previous call to Assertions.assertThat(actual.getResults().contains(results)
    // - uncomment the line below and set your error message:
    // Assertions.assertThat(actual.getResults()).overridingErrorMessage("\nmy error message %s", "arg1").contains(results);

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual PlaceSearchResultsModel has no results.
   * @return this assertion object.
   * @throws AssertionError if the actual PlaceSearchResultsModel's results is not empty.
   */
  public PlaceSearchResultsModelAssert hasNoResults() {
    // check that actual PlaceSearchResultsModel we want to make assertions on is not null.
    isNotNull();

    // we override the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected :\n  <%s>\nnot to have results but had :\n  <%s>";
    
    // check
    if (!actual.getResults().isEmpty()) {
      failWithMessage(assertjErrorMessage, actual, actual.getResults());
    }
    
    // return the current assertion for method chaining
    return this;
  }
  

}
