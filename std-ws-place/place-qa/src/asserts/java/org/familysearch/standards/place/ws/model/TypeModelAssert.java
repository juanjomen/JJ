package org.familysearch.standards.place.ws.model;

import org.assertj.core.api.AbstractAssert;
// Assertions is needed if an assertion for Iterable is generated
import org.assertj.core.api.Assertions;


/**
 * {@link TypeModel} specific assertions - Generated by CustomAssertionGenerator.
 */
public class TypeModelAssert extends AbstractAssert<TypeModelAssert, TypeModel> {

  /**
   * Creates a new </code>{@link TypeModelAssert}</code> to make assertions on actual TypeModel.
   * @param actual the TypeModel we want to make assertions on.
   */
  public TypeModelAssert(TypeModel actual) {
    super(actual, TypeModelAssert.class);
  }

  /**
   * An entry point for TypeModelAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one's can write directly : <code>assertThat(myTypeModel)</code> and get specific assertion with code completion.
   * @param actual the TypeModel we want to make assertions on.
   * @return a new </code>{@link TypeModelAssert}</code>
   */
  public static TypeModelAssert assertThat(TypeModel actual) {
    return new TypeModelAssert(actual);
  }

  /**
   * Verifies that the actual TypeModel's code is equal to the given one.
   * @param code the given code to compare the actual TypeModel's code to.
   * @return this assertion object.
   * @throws AssertionError - if the actual TypeModel's code is not equal to the given one.
   */
  public TypeModelAssert hasCode(String code) {
    // check that actual TypeModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected code of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    String actualCode = actual.getCode();
    if (!org.assertj.core.util.Objects.areEqual(actualCode, code)) {
      failWithMessage(assertjErrorMessage, actual, code, actualCode);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual TypeModel's groups contains the given LinkModel elements.
   * @param groups the given elements that should be contained in actual TypeModel's groups.
   * @return this assertion object.
   * @throws AssertionError if the actual TypeModel's groups does not contain all given LinkModel elements.
   */
  public TypeModelAssert hasGroups(LinkModel... groups) {
    // check that actual TypeModel we want to make assertions on is not null.
    isNotNull();

    // check that given LinkModel varargs is not null.
    if (groups == null) throw new AssertionError("Expecting groups parameter not to be null.");
    
    // check with standard error message (see commented below to set your own message).
    Assertions.assertThat(actual.getGroups()).contains(groups);

    // To override the standard error message :
    // - remove the previous call to Assertions.assertThat(actual.getGroups().contains(groups)
    // - uncomment the line below and set your error message:
    // Assertions.assertThat(actual.getGroups()).overridingErrorMessage("\nmy error message %s", "arg1").contains(groups);

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual TypeModel has no groups.
   * @return this assertion object.
   * @throws AssertionError if the actual TypeModel's groups is not empty.
   */
  public TypeModelAssert hasNoGroups() {
    // check that actual TypeModel we want to make assertions on is not null.
    isNotNull();

    // we override the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected :\n  <%s>\nnot to have groups but had :\n  <%s>";
    
    // check
    if (!actual.getGroups().isEmpty()) {
      failWithMessage(assertjErrorMessage, actual, actual.getGroups());
    }
    
    // return the current assertion for method chaining
    return this;
  }
  

  /**
   * Verifies that the actual TypeModel's id is equal to the given one.
   * @param id the given id to compare the actual TypeModel's id to.
   * @return this assertion object.
   * @throws AssertionError - if the actual TypeModel's id is not equal to the given one.
   */
  public TypeModelAssert hasId(Integer id) {
    // check that actual TypeModel we want to make assertions on is not null.
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
   * Verifies that the actual TypeModel's localizedName contains the given LocalizedNameDescModel elements.
   * @param localizedName the given elements that should be contained in actual TypeModel's localizedName.
   * @return this assertion object.
   * @throws AssertionError if the actual TypeModel's localizedName does not contain all given LocalizedNameDescModel elements.
   */
  public TypeModelAssert hasLocalizedName(LocalizedNameDescModel... localizedName) {
    // check that actual TypeModel we want to make assertions on is not null.
    isNotNull();

    // check that given LocalizedNameDescModel varargs is not null.
    if (localizedName == null) throw new AssertionError("Expecting localizedName parameter not to be null.");
    
    // check with standard error message (see commented below to set your own message).
    Assertions.assertThat(actual.getLocalizedName()).contains(localizedName);

    // To override the standard error message :
    // - remove the previous call to Assertions.assertThat(actual.getLocalizedName().contains(localizedName)
    // - uncomment the line below and set your error message:
    // Assertions.assertThat(actual.getLocalizedName()).overridingErrorMessage("\nmy error message %s", "arg1").contains(localizedName);

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual TypeModel has no localizedName.
   * @return this assertion object.
   * @throws AssertionError if the actual TypeModel's localizedName is not empty.
   */
  public TypeModelAssert hasNoLocalizedName() {
    // check that actual TypeModel we want to make assertions on is not null.
    isNotNull();

    // we override the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected :\n  <%s>\nnot to have localizedName but had :\n  <%s>";
    
    // check
    if (!actual.getLocalizedName().isEmpty()) {
      failWithMessage(assertjErrorMessage, actual, actual.getLocalizedName());
    }
    
    // return the current assertion for method chaining
    return this;
  }
  

  /**
   * Verifies that the actual TypeModel's selfLink is equal to the given one.
   * @param selfLink the given selfLink to compare the actual TypeModel's selfLink to.
   * @return this assertion object.
   * @throws AssertionError - if the actual TypeModel's selfLink is not equal to the given one.
   */
  public TypeModelAssert hasSelfLink(LinkModel selfLink) {
    // check that actual TypeModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected selfLink of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    LinkModel actualSelfLink = actual.getSelfLink();
    if (!org.assertj.core.util.Objects.areEqual(actualSelfLink, selfLink)) {
      failWithMessage(assertjErrorMessage, actual, selfLink, actualSelfLink);
    }

    // return the current assertion for method chaining
    return this;
  }

}