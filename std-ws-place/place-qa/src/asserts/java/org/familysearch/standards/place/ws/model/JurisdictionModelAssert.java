package org.familysearch.standards.place.ws.model;

import org.assertj.core.api.AbstractAssert;
// Assertions is needed if an assertion for Iterable is generated


/**
 * {@link JurisdictionModel} specific assertions - Generated by CustomAssertionGenerator.
 */
public class JurisdictionModelAssert extends AbstractAssert<JurisdictionModelAssert, JurisdictionModel> {

  /**
   * Creates a new </code>{@link JurisdictionModelAssert}</code> to make assertions on actual JurisdictionModel.
   * @param actual the JurisdictionModel we want to make assertions on.
   */
  public JurisdictionModelAssert(JurisdictionModel actual) {
    super(actual, JurisdictionModelAssert.class);
  }

  /**
   * An entry point for JurisdictionModelAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one's can write directly : <code>assertThat(myJurisdictionModel)</code> and get specific assertion with code completion.
   * @param actual the JurisdictionModel we want to make assertions on.
   * @return a new </code>{@link JurisdictionModelAssert}</code>
   */
  public static JurisdictionModelAssert assertThat(JurisdictionModel actual) {
    return new JurisdictionModelAssert(actual);
  }

  /**
   * Verifies that the actual JurisdictionModel's id is equal to the given one.
   * @param id the given id to compare the actual JurisdictionModel's id to.
   * @return this assertion object.
   * @throws AssertionError - if the actual JurisdictionModel's id is not equal to the given one.
   */
  public JurisdictionModelAssert hasId(int id) {
    // check that actual JurisdictionModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected id of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // check
    int actualId = actual.getId();
    if (actualId != id) {
      failWithMessage(assertjErrorMessage, actual, id, actualId);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual JurisdictionModel's locale is equal to the given one.
   * @param locale the given locale to compare the actual JurisdictionModel's locale to.
   * @return this assertion object.
   * @throws AssertionError - if the actual JurisdictionModel's locale is not equal to the given one.
   */
  public JurisdictionModelAssert hasLocale(String locale) {
    // check that actual JurisdictionModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected locale of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    String actualLocale = actual.getLocale();
    if (!org.assertj.core.util.Objects.areEqual(actualLocale, locale)) {
      failWithMessage(assertjErrorMessage, actual, locale, actualLocale);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual JurisdictionModel's name is equal to the given one.
   * @param name the given name to compare the actual JurisdictionModel's name to.
   * @return this assertion object.
   * @throws AssertionError - if the actual JurisdictionModel's name is not equal to the given one.
   */
  public JurisdictionModelAssert hasName(String name) {
    // check that actual JurisdictionModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected name of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    String actualName = actual.getName();
    if (!org.assertj.core.util.Objects.areEqual(actualName, name)) {
      failWithMessage(assertjErrorMessage, actual, name, actualName);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual JurisdictionModel's parent is equal to the given one.
   * @param parent the given parent to compare the actual JurisdictionModel's parent to.
   * @return this assertion object.
   * @throws AssertionError - if the actual JurisdictionModel's parent is not equal to the given one.
   */
  public JurisdictionModelAssert hasParent(JurisdictionModel parent) {
    // check that actual JurisdictionModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected parent of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    JurisdictionModel actualParent = actual.getParent();
    if (!org.assertj.core.util.Objects.areEqual(actualParent, parent)) {
      failWithMessage(assertjErrorMessage, actual, parent, actualParent);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual JurisdictionModel's place is equal to the given one.
   * @param place the given place to compare the actual JurisdictionModel's place to.
   * @return this assertion object.
   * @throws AssertionError - if the actual JurisdictionModel's place is not equal to the given one.
   */
  public JurisdictionModelAssert hasPlace(Integer place) {
    // check that actual JurisdictionModel we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpected place of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";
    
    // null safe check
    Integer actualPlace = actual.getPlace();
    if (!org.assertj.core.util.Objects.areEqual(actualPlace, place)) {
      failWithMessage(assertjErrorMessage, actual, place, actualPlace);
    }

    // return the current assertion for method chaining
    return this;
  }

  /**
   * Verifies that the actual JurisdictionModel's selfLink is equal to the given one.
   * @param selfLink the given selfLink to compare the actual JurisdictionModel's selfLink to.
   * @return this assertion object.
   * @throws AssertionError - if the actual JurisdictionModel's selfLink is not equal to the given one.
   */
  public JurisdictionModelAssert hasSelfLink(LinkModel selfLink) {
    // check that actual JurisdictionModel we want to make assertions on is not null.
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
