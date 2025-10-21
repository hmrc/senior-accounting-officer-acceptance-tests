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
import uk.gov.hmrc.test.ui.pages.RegisterYourCompanyPage.{baseRegUrl, fluentWait}
import uk.gov.hmrc.test.ui.utils.TestDataGenerator

object ContactDetailsPage extends BasePage with TestDataGenerator {
  override val pageUrl: String = baseRegUrl

  val enterYourContactDetailsLink: String = pageUrl + "/contact-details"

  private val continueButtonLocator: By             = By.cssSelector("#submit")
  private val contactDetailsTextFieldLocator: By    = By.cssSelector("#value")
  private val contactDetailsErrorSummaryLocator: By = By.cssSelector("a[href='#value']")
  private val yesRadioButtonLocator: By             = By.cssSelector("#value_0")
  private val noRadioButtonLocator: By              = By.cssSelector("#value_1")

  private val addedAllTheContactsYouNeedPageTitle: By = By.cssSelector(".govuk-fieldset__heading")

  private val firstContactDetailsHeadingLocator: By =
    By.cssSelector("#main-content form h2.govuk-heading-s:first-of-type")
  val firstContactDetailsFullNameLocator: By        = By.cssSelector(
    "dl.govuk-summary-list:nth-of-type(1) > .govuk-summary-list__row:nth-of-type(1) > dd.govuk-summary-list__value"
  )
  val firstContactDetailsEmailAddressLocator: By    = By.cssSelector(
    "dl.govuk-summary-list:nth-of-type(1) > .govuk-summary-list__row:nth-of-type(2) > dd.govuk-summary-list__value"
  )

  private val secondContactDetailsHeadingLocator: By =
    By.cssSelector("#main-content form h2.govuk-heading-s:nth-of-type(2)")
  val secondContactDetailsFullNameLocator: By        = By.cssSelector(
    "dl.govuk-summary-list:nth-of-type(2) > .govuk-summary-list__row:nth-of-type(1) > dd.govuk-summary-list__value"
  )
  val secondContactDetailsEmailAddressLocator: By    = By.cssSelector(
    "dl.govuk-summary-list:nth-of-type(2) > .govuk-summary-list__row:nth-of-type(2) > dd.govuk-summary-list__value"
  )

  // Random Contact Details
  private val faker = new Faker(new java.util.Locale("en-GB"))

  var firstContactFullName: String        = faker.name().fullName()
  var randomFirstContactFullName: String  = faker.name().fullName()
  var secondContactFullName: String       = faker.name().fullName()
  var randomSecondContactFullName: String = faker.name().fullName()

  val firstContactEmailAddress: String        = randomEmail(firstContactFullName)
  val randomFirstContactEmailAddress: String  = randomEmail(randomFirstContactFullName)
  val secondContactEmailAddress: String       = randomEmail(secondContactFullName)
  val randomSecondContactEmailAddress: String = randomEmail(randomSecondContactFullName)

  // Error Summary
  private val errorMessageForEnterFullName: String     = "Enter contactName"
  private val errorMessageForEnterEmailAddress: String = "Enter contactEmail"

  // Contact details Change link
  val changeLinkForFirstContactFullNameLocator: By     =
    By.cssSelector("a[href='/senior-accounting-officer/registration/contact-details/first/change-name']")
  val changeLinkForFirstContactEmailAddressLocator: By =
    By.cssSelector("a[href='/senior-accounting-officer/registration/contact-details/first/change-email']")

  val changeLinkForSecondContactFullName: By     =
    By.cssSelector("a[href='/senior-accounting-officer/registration/contact-details/second/change-name']")
  val changeLinkForSecondContactEmailAddress: By =
    By.cssSelector("a[href='/senior-accounting-officer/registration/contact-details/second/change-email']")

  def clickContinueButtonElement(): Unit = {
    fluentWait.until(ExpectedConditions.elementToBeClickable(continueButtonLocator))
    click(continueButtonLocator)
  }

  def enterFullName(fullName: String): Unit = {
    val enterFullNameField =
      fluentWait.until(ExpectedConditions.elementToBeClickable(contactDetailsTextFieldLocator))
    enterFullNameField.clear()
    enterFullNameField.sendKeys(fullName)

    clickContinueButtonElement()
  }

  def enterEmailAddress(emailAddress: String): Unit = {
    val enterEmailAddressField =
      fluentWait.until(ExpectedConditions.elementToBeClickable(contactDetailsTextFieldLocator))
    enterEmailAddressField.clear()
    enterEmailAddressField.sendKeys(emailAddress)

    clickContinueButtonElement()
  }

  def selectYes(): Unit = {
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(addedAllTheContactsYouNeedPageTitle))

    driver.findElement(yesRadioButtonLocator).click()
    clickContinueButtonElement()
  }

  def selectNo(): Unit = {
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(addedAllTheContactsYouNeedPageTitle))

    driver.findElement(noRadioButtonLocator).click()
    clickContinueButtonElement()
  }

  def verifyContactDetailsFieldValue(elementLocator: By, expectedValue: String): Unit = {
    val contactDetailsElementValue =
      fluentWait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator)).getText

    contactDetailsElementValue.trim mustBe expectedValue
  }

  def verifyFirstContactDetailsInCheckYourAnswersPage(): Unit = {
    verifyContactDetailsFieldValue(firstContactDetailsHeadingLocator, "First contact details")
    verifyContactDetailsFieldValue(firstContactDetailsFullNameLocator, firstContactFullName)
    verifyContactDetailsFieldValue(firstContactDetailsEmailAddressLocator, firstContactEmailAddress)
  }

  def verifySecondContactDetailsInCheckYourAnswersPage(): Unit = {
    verifyContactDetailsFieldValue(secondContactDetailsHeadingLocator, "Second contact details")
    verifyContactDetailsFieldValue(secondContactDetailsFullNameLocator, secondContactFullName)
    verifyContactDetailsFieldValue(secondContactDetailsEmailAddressLocator, secondContactEmailAddress)
  }

  def verifyFullNameErrorSummaryOnContactDetailsPage(): Unit = {
    clickContinueButtonElement()
    errorSummary(errorMessageForEnterFullName)
  }

  def verifyEmailAddressErrorSummaryOnContactDetailsPage(): Unit = {
    clickContinueButtonElement()
    errorSummary(errorMessageForEnterEmailAddress)
  }

  def errorSummary(expectedErrorSummary: String): Unit = {
    val contactDetailsErrorSummaryElement =
      fluentWait.until(ExpectedConditions.visibilityOfElementLocated(contactDetailsErrorSummaryLocator))

    val actualTextValue = contactDetailsErrorSummaryElement.getText

    actualTextValue.trim mustBe expectedErrorSummary
  }

  def changeContactDetail(
    valueLocator: By,
    changeLinkLocator: By,
    updatedValue: String,
    inputFunction: String => Unit
  ): Unit = {
    val changeLink =
      fluentWait.until(ExpectedConditions.elementToBeClickable(changeLinkLocator))
    changeLink.click()

    inputFunction(updatedValue)
  }

  def verifyChangedContactDetails(
    valueLocator: By,
    originalValue: String
  ): Unit = {
    val updatedValue =
      fluentWait.until(ExpectedConditions.visibilityOfElementLocated(valueLocator)).getText

    updatedValue must not be originalValue
  }
}
