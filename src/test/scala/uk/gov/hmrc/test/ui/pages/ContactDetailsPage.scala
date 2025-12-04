/*
 * Copyright 2025 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.test.ui.pages

import com.github.javafaker.Faker
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import org.scalactic.Prettifier.default
import uk.gov.hmrc.test.ui.adt.*
import uk.gov.hmrc.test.ui.support.TestDataGenerator
import uk.gov.hmrc.test.ui.support.PageSupport.fluentWait

object ContactDetailsPage extends BasePage with TestDataGenerator {
  override val pageUrl: String   = baseRegUrl
  override val pageTitle: String = ""
  private val faker              = new Faker(new java.util.Locale("en-GB"))

  val enterFirstContactEmailAddressPage: String = s"${pageUrl.stripSuffix("/")}/contact-details/first/email"
  val addAnotherContactPage: String             = s"${pageUrl.stripSuffix("/")}/contact-details/first/add-another"
  val enterYourContactDetailsLinkUrl: String    = s"${pageUrl.stripSuffix("/")}/contact-details"

  private val continueButton: By               = By.cssSelector("button[type='submit']")
  private val contactNameInput: By             = By.cssSelector("#value")
  private val emailAddressInput: By            = By.cssSelector("input[type=email]")
  private val yesOptionRadioButton: By         = By.cssSelector("#value_0")
  private val noOptionLabel: By                = By.cssSelector("label[for='value_1']")
  private val noOptionRadioButton: By          = By.cssSelector("#value_1")
  private val addAnotherContactPageHeading: By = By.cssSelector(".govuk-fieldset__heading")

  private val firstContactSectionHeader: By = By.cssSelector(".govuk-heading-s")
  val firstContactNameField: By             = contactNameValueLocator(1)
  val firstContactEmailField: By            = contactEmailValueLocator(1)
  val secondContactSectionHeader: By        = By.cssSelector(".govuk-heading-s:nth-of-type(2)")
  val secondContactNameField: By            = contactNameValueLocator(2)
  val secondContactEmailField: By           = contactEmailValueLocator(2)
  val changeFirstContactNameLink: By        = By.cssSelector("a[href*='first/change-name']")
  val changeFirstContactEmailLink: By       = By.cssSelector("a[href*='first/change-email']")
  val changeSecondContactNameLink: By       = By.cssSelector("a[href*='second/change-name']")
  val changeSecondContactEmailLink: By      = By.cssSelector("a[href*='second/change-email']")

  val firstContactName: String     = faker.name().fullName()
  val newFirstContactName: String  = faker.name().fullName()
  val secondContactName: String    = faker.name().fullName()
  val newSecondContactName: String = faker.name().fullName()

  val firstContactEmail: String     = emailForUser(firstContactName)
  val newFirstContactEmail: String  = emailForUser(newFirstContactName)
  val secondContactEmail: String    = emailForUser(secondContactName)
  val newSecondContactEmail: String = emailForUser(newSecondContactName)

  val contactMap: Map[Contact, ContactDetails] = Map(
    FirstContact  -> ContactDetails(
      firstContactSectionHeader,
      "First contact details",
      firstContactNameField,
      firstContactName,
      firstContactEmailField,
      firstContactEmail
    ),
    SecondContact -> ContactDetails(
      secondContactSectionHeader,
      "Second contact details",
      secondContactNameField,
      secondContactName,
      secondContactEmailField,
      secondContactEmail
    )
  )

  private val pageErrors: Map[PageError, (By, String)] = Map(
    ContactDetailsPageError.MissingContactDetails -> (By.cssSelector("a[href='#value']"), "Enter contactName"),
    ContactDetailsPageError.MissingEmailAddress   -> (By.cssSelector("a[href='#value']"), "Enter contactEmail")
  )

  def contactNameValueLocator(index: Int): By =
    By.cssSelector(
      s"dl.govuk-summary-list:nth-of-type($index) > .govuk-summary-list__row:nth-of-type(1) > dd.govuk-summary-list__value"
    )

  def contactEmailValueLocator(index: Int): By =
    By.cssSelector(
      s"dl.govuk-summary-list:nth-of-type($index) > .govuk-summary-list__row:nth-of-type(2) > dd.govuk-summary-list__value"
    )

  def clickContinue(): Unit =
    fluentWait.until(ExpectedConditions.elementToBeClickable(continueButton)).click()

  def enterContactNameAndClickContinue(fullName: String): Unit = {
    val enterFullNameField = fluentWait.until(ExpectedConditions.elementToBeClickable(contactNameInput))
    enterFullNameField.clear()
    enterFullNameField.sendKeys(fullName)
    clickContinue()
  }

  def enterEmailAddressAndClickContinue(emailAddress: String): Unit = {
    val enterEmailAddressField = fluentWait.until(ExpectedConditions.elementToBeClickable(emailAddressInput))
    enterEmailAddressField.clear()
    enterEmailAddressField.sendKeys(emailAddress)
    clickContinue()
  }

  def selectYesRadioAndClickContinue(): Unit = {
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(addAnotherContactPageHeading))
    driver.findElement(yesOptionRadioButton).click()
    clickContinue()
  }

  def selectNoRadioAndClickContinue(): Unit = {
    fluentWait.until(ExpectedConditions.elementToBeClickable(noOptionLabel)).click()
    fluentWait.until(ExpectedConditions.elementSelectionStateToBe(noOptionRadioButton, true))
    clickContinue()
  }

  def assertContactDetailsMatch(contact: Contact): Unit = {
    val details = contactMap(contact)
    assertFieldValueMatches(details.headingField, details.headingText)
    assertFieldValueMatches(details.fullNameField, details.fullName)
    assertFieldValueMatches(details.emailField, details.email)
  }

  def assertFieldValueMatches(field: By, expectedValue: String): Unit = {
    val fieldValue = fluentWait.until(ExpectedConditions.visibilityOfElementLocated(field))
    fieldValue.getText.trim mustBe expectedValue
  }

  def assertErrorMessageMatches(error: PageError): Unit = {
    val (elementWithError: By, expectedErrorMessage: String) = pageErrors(error)

    val element = fluentWait.until(ExpectedConditions.visibilityOfElementLocated(elementWithError))
    element.isDisplayed mustBe true
    element.getText.trim mustBe expectedErrorMessage
  }

  def changeContactDetail(
    valueLocator: By,
    changeLinkLocator: By,
    updatedValue: String,
    inputFunction: String => Unit
  ): Unit = {
    fluentWait.until(ExpectedConditions.elementToBeClickable(changeLinkLocator)).click()
    inputFunction(updatedValue)
  }

  def verifyChangedContactDetails(
    valueLocator: By,
    originalValue: String
  ): Unit = {
    val updatedValue = fluentWait.until(ExpectedConditions.visibilityOfElementLocated(valueLocator))

    // TODO: It's better to validate the new values based on edits (i.e. known values)
    updatedValue.getText must not be originalValue
  }
}
