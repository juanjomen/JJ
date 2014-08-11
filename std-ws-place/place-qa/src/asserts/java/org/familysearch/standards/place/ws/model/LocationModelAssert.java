package org.familysearch.standards.place.ws.model;

import org.assertj.core.api.AbstractAssert;
// Assertions is needed if an assertion for Iterable is generated


/**
 * {@link LocationModel} specific assertions - Generated by CustomAssertionGenerator.
 */
public class LocationModelAssert extends AbstractAssert<LocationModelAssert, LocationModel> {

  /**
   * Creates a new </code>{@link LocationModelAssert}</code> to make assertions on actual LocationModel.
   * @param actual the LocationModel we want to make assertions on.
   */
  public LocationModelAssert(LocationModel actual) {
    super(actual, LocationModelAssert.class);
  }

  /**
   * An entry point for LocationModelAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one's can write directly : <code>assertThat(myLocationModel)</code> and get specific assertion with code completion.
   * @param actual the LocationModel we want to make assertions on.
   * @return a new </code>{@link LocationModelAssert}</code>
   */
  public static LocationModelAssert assertThat(LocationModel actual) {
    return new LocationModelAssert(actual);
  }

  /**
   * Verifies that the actual LocationModel's centroid is equal to the given one.
   * @param centroid the given centroid to compare the actual LocationModel's centroid to.
   * @return this assertion object.
   * @throws AssertionError - if the actual LocationModel's centroid is not equal to the given one.
   */
  public LocationModelAssert hasCentroid(CentroidModel centroid) {
    // check that actual LocationModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected centroid of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    CentroidModel actualCentroid = actual.getCentroid();
    if (!org.assertj.core.util.Objects.areEqual(actualCentroid, centroid)) {
      failWithMessage(assertjErrorMessage, actual, centroid, actualCentroid);
    }

    // return the current assertion for method chaining
    return this;
  }

}
