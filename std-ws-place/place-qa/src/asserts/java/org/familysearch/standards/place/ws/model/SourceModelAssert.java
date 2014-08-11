package org.familysearch.standards.place.ws.model;

import org.assertj.core.api.AbstractAssert;
// Assertions is needed if an assertion for Iterable is generated


/**
 * {@link SourceModel} specific assertions - Generated by CustomAssertionGenerator.
 */
public class SourceModelAssert extends AbstractAssert<SourceModelAssert, SourceModel> {

  /**
   * Creates a new </code>{@link SourceModelAssert}</code> to make assertions on actual SourceModel.
   * @param actual the SourceModel we want to make assertions on.
   */
  public SourceModelAssert(SourceModel actual) {
    super(actual, SourceModelAssert.class);
  }

  /**
   * An entry point for SourceModelAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one's can write directly : <code>assertThat(mySourceModel)</code> and get specific assertion with code completion.
   * @param actual the SourceModel we want to make assertions on.
   * @return a new </code>{@link SourceModelAssert}</code>
   */
  public static SourceModelAssert assertThat(SourceModel actual) {
    return new SourceModelAssert(actual);
  }

  /**
   * Verifies that the actual SourceModel's description is equal to the given one.
   * @param description the given description to compare the actual SourceModel's description to.
   * @return this assertion object.
   * @throws AssertionError - if the actual SourceModel's description is not equal to the given one.
   */
  public SourceModelAssert hasDescription(String description) {
    // check that actual SourceModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected description of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    String actualDescription = actual.getDescription();
    if (!org.assertj.core.util.Objects.areEqual(actualDescription, description)) {
      failWithMessage(assertjErrorMessage, actual, description, actualDescription);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual SourceModel's id is equal to the given one.
   * @param id the given id to compare the actual SourceModel's id to.
   * @return this assertion object.
   * @throws AssertionError - if the actual SourceModel's id is not equal to the given one.
   */
  public SourceModelAssert hasId(Integer id) {
    // check that actual SourceModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected id of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    Integer actualId = actual.getId();
    if (!org.assertj.core.util.Objects.areEqual(actualId, id)) {
      failWithMessage(assertjErrorMessage, actual, id, actualId);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual SourceModel's title is equal to the given one.
   * @param title the given title to compare the actual SourceModel's title to.
   * @return this assertion object.
   * @throws AssertionError - if the actual SourceModel's title is not equal to the given one.
   */
  public SourceModelAssert hasTitle(String title) {
    // check that actual SourceModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected title of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    String actualTitle = actual.getTitle();
    if (!org.assertj.core.util.Objects.areEqual(actualTitle, title)) {
      failWithMessage(assertjErrorMessage, actual, title, actualTitle);
    }

    // return the current assertion for method chaining
    return this;
  }

}