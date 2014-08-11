package org.familysearch.standards.place.ws.model;

import org.assertj.core.api.AbstractAssert;
// Assertions is needed if an assertion for Iterable is generated


/**
 * {@link VariantModel} specific assertions - Generated by CustomAssertionGenerator.
 */
public class VariantModelAssert extends AbstractAssert<VariantModelAssert, VariantModel> {

  /**
   * Creates a new </code>{@link VariantModelAssert}</code> to make assertions on actual VariantModel.
   * @param actual the VariantModel we want to make assertions on.
   */
  public VariantModelAssert(VariantModel actual) {
    super(actual, VariantModelAssert.class);
  }

  /**
   * An entry point for VariantModelAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one's can write directly : <code>assertThat(myVariantModel)</code> and get specific assertion with code completion.
   * @param actual the VariantModel we want to make assertions on.
   * @return a new </code>{@link VariantModelAssert}</code>
   */
  public static VariantModelAssert assertThat(VariantModel actual) {
    return new VariantModelAssert(actual);
  }

  /**
   * Verifies that the actual VariantModel's id is equal to the given one.
   * @param id the given id to compare the actual VariantModel's id to.
   * @return this assertion object.
   * @throws AssertionError - if the actual VariantModel's id is not equal to the given one.
   */
  public VariantModelAssert hasId(Integer id) {
    // check that actual VariantModel we want to make assertions on is not null.
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
   * Verifies that the actual VariantModel's name is equal to the given one.
   * @param name the given name to compare the actual VariantModel's name to.
   * @return this assertion object.
   * @throws AssertionError - if the actual VariantModel's name is not equal to the given one.
   */
  public VariantModelAssert hasName(NameModel name) {
    // check that actual VariantModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected name of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    NameModel actualName = actual.getName();
    if (!org.assertj.core.util.Objects.areEqual(actualName, name)) {
      failWithMessage(assertjErrorMessage, actual, name, actualName);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual VariantModel's type is equal to the given one.
   * @param type the given type to compare the actual VariantModel's type to.
   * @return this assertion object.
   * @throws AssertionError - if the actual VariantModel's type is not equal to the given one.
   */
  public VariantModelAssert hasType(TypeModel type) {
    // check that actual VariantModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected type of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    TypeModel actualType = actual.getType();
    if (!org.assertj.core.util.Objects.areEqual(actualType, type)) {
      failWithMessage(assertjErrorMessage, actual, type, actualType);
    }

    // return the current assertion for method chaining
    return this;
  }

}