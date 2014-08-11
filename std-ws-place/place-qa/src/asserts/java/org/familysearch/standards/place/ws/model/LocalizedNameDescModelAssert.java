package org.familysearch.standards.place.ws.model;

import org.assertj.core.api.AbstractAssert;
// Assertions is needed if an assertion for Iterable is generated


/**
 * {@link LocalizedNameDescModel} specific assertions - Generated by CustomAssertionGenerator.
 */
public class LocalizedNameDescModelAssert extends AbstractAssert<LocalizedNameDescModelAssert, LocalizedNameDescModel> {

  /**
   * Creates a new </code>{@link LocalizedNameDescModelAssert}</code> to make assertions on actual LocalizedNameDescModel.
   * @param actual the LocalizedNameDescModel we want to make assertions on.
   */
  public LocalizedNameDescModelAssert(LocalizedNameDescModel actual) {
    super(actual, LocalizedNameDescModelAssert.class);
  }

  /**
   * An entry point for LocalizedNameDescModelAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one's can write directly : <code>assertThat(myLocalizedNameDescModel)</code> and get specific assertion with code completion.
   * @param actual the LocalizedNameDescModel we want to make assertions on.
   * @return a new </code>{@link LocalizedNameDescModelAssert}</code>
   */
  public static LocalizedNameDescModelAssert assertThat(LocalizedNameDescModel actual) {
    return new LocalizedNameDescModelAssert(actual);
  }

  /**
   * Verifies that the actual LocalizedNameDescModel's description is equal to the given one.
   * @param description the given description to compare the actual LocalizedNameDescModel's description to.
   * @return this assertion object.
   * @throws AssertionError - if the actual LocalizedNameDescModel's description is not equal to the given one.
   */
  public LocalizedNameDescModelAssert hasDescription(String description) {
    // check that actual LocalizedNameDescModel we want to make assertions on is not null.
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
   * Verifies that the actual LocalizedNameDescModel's locale is equal to the given one.
   * @param locale the given locale to compare the actual LocalizedNameDescModel's locale to.
   * @return this assertion object.
   * @throws AssertionError - if the actual LocalizedNameDescModel's locale is not equal to the given one.
   */
  public LocalizedNameDescModelAssert hasLocale(String locale) {
    // check that actual LocalizedNameDescModel we want to make assertions on is not null.
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
   * Verifies that the actual LocalizedNameDescModel's name is equal to the given one.
   * @param name the given name to compare the actual LocalizedNameDescModel's name to.
   * @return this assertion object.
   * @throws AssertionError - if the actual LocalizedNameDescModel's name is not equal to the given one.
   */
  public LocalizedNameDescModelAssert hasName(String name) {
    // check that actual LocalizedNameDescModel we want to make assertions on is not null.
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

}