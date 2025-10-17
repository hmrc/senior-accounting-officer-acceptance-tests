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

import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import uk.gov.hmrc.test.ui.pages.RegisterYourCompanyPage.{baseRegUrl, fluentWait}
import uk.gov.hmrc.test.ui.utils.TestDataGenerator

object ContactDetailsPage extends BasePage with TestDataGenerator {
  override val pageUrl: String = baseRegUrl

  val enterYourContactDetailsLink: String = pageUrl + "/contact-details"

  private val continueButton: By             = By.id("submit")
  private val contactDetailsTextField: By    = By.id("value")
  private val contactDetailsErrorSummary: By = By.cssSelector("a[href='#value']")
  private val yesRadioButton: By             = By.id("value_0")
  private val noRadioButton: By              = By.id("value_1")

  private val addedAllTheContactsYouNeedPageTitle: By = By.xpath("//h1[@class='govuk-fieldset__heading']")

  private val firstContactDetailsHeading: By     = By.xpath("//*[@id=\"main-content\"]//h2[1]")
  val firstContactDetailsFullName: By            = By.xpath("(//dl[@class='govuk-summary-list'])[1]/div[1]/dd[1]")
  private val firstContactDetailsRole: By        = By.xpath("(//dl[@class='govuk-summary-list'])[1]/div[2]/dd[1]")
  val firstContactDetailsEmailAddress: By        = By.xpath("(//dl[@class='govuk-summary-list'])[1]/div[3]/dd[1]")
  private val firstContactDetailsPhoneNumber: By = By.xpath("(//dl[@class='govuk-summary-list'])[1]/div[4]/dd[1]")

  private val secondContactDetailsHeading: By     = By.xpath("//*[@id=\"main-content\"]//h2[2]")
  val secondContactDetailsFullName: By            = By.xpath("(//dl[@class='govuk-summary-list'])[2]/div[1]/dd[1]")
  private val secondContactDetailsRole: By        = By.xpath("(//dl[@class='govuk-summary-list'])[2]/div[2]/dd[1]")
  val secondContactDetailsEmailAddress: By        = By.xpath("(//dl[@class='govuk-summary-list'])[2]/div[3]/dd[1]")
  private val secondContactDetailsPhoneNumber: By = By.xpath("(//dl[@class='govuk-summary-list'])[2]/div[4]/dd[1]")

  // Random Contact Details
  var firstContactFullName: String        = randomFullName()
  var randomFirstContactFullName: String  = randomFullName()
  var secondContactFullName: String       = randomFullName()
  var randomSecondContactFullName: String = randomFullName()

  val rndEmailAddress: String = randomEmail()

  val firstContactEmailAddress: String       = randomEmail()
  val randomFirstContactEmailAddress: String = randomEmail()

  val secondContactEmailAddress: String       = randomEmail()
  val randomSecondContactEmailAddress: String = randomEmail()

  val rndRole: String        = randomRole()
  val rndPhoneNumber: String = randomUkPhoneNumber()

  // Error Summary
  private val errorMessageForEnterFullName: String     = "Enter contactName"
  private val errorMessageForEnterEmailAddress: String = "Enter contactEmail"
  private val errorMessageForEnterRole: String         = "Enter contactRole"
  private val errorMessageForEnterPhoneNumber: String  = "Enter contactPhone"

  // Contact details Change link
  val changeLinkForFirstContactFullName: By     =
    By.cssSelector("a[href='/senior-accounting-officer/registration/contact-details/first/change-name']")
  val changeLinkForFirstContactEmailAddress: By =
    By.cssSelector("a[href='/senior-accounting-officer/registration/contact-details/first/change-email']")

  val changeLinkForSecondContactFullName: By     =
    By.cssSelector("a[href='/senior-accounting-officer/registration/contact-details/second/change-name']")
  val changeLinkForSecondContactEmailAddress: By =
    By.cssSelector("a[href='/senior-accounting-officer/registration/contact-details/second/change-email']")

  def clickContinueButtonElement(): Unit = {
    fluentWait.until(ExpectedConditions.elementToBeClickable(continueButton))
    click(continueButton)
  }

  def enterFullName(fullName: String): Unit = {
    val enterFullNameField = fluentWait.until(ExpectedConditions.elementToBeClickable(contactDetailsTextField))
    enterFullNameField.clear()
    enterFullNameField.sendKeys(fullName)

    clickContinueButtonElement()
  }

  def enterEmailAddress(emailAddress: String): Unit = {
    val enterEmailAddressField =
      fluentWait.until(ExpectedConditions.elementToBeClickable(contactDetailsTextField))
    enterEmailAddressField.clear()
    enterEmailAddressField.sendKeys(emailAddress)

    clickContinueButtonElement()
  }

  def enterRole(): Unit = {
    val enterRoleField = fluentWait.until(ExpectedConditions.elementToBeClickable(contactDetailsTextField))
    enterRoleField.clear()
    enterRoleField.sendKeys(rndRole)

    clickContinueButtonElement()
  }

  def enterPhoneNumber(): Unit = {
    val enterPhoneNumberField = fluentWait.until(ExpectedConditions.elementToBeClickable(contactDetailsTextField))
    enterPhoneNumberField.clear()
    enterPhoneNumberField.sendKeys(rndPhoneNumber)

    clickContinueButtonElement()
  }

  def selectYes(): Unit = {
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(addedAllTheContactsYouNeedPageTitle))

    driver.findElement(yesRadioButton).click()
    clickContinueButtonElement()
  }

  def selectNo(): Unit = {
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(addedAllTheContactsYouNeedPageTitle))

    driver.findElement(noRadioButton).click()
    clickContinueButtonElement()
  }

  def verifyContactDetailsFieldValue(elementLocator: By, expectedValue: String): Unit = {
    val contactDetailsElementValue =
      fluentWait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator)).getText

    contactDetailsElementValue.trim mustBe expectedValue
  }

  def verifyFirstContactDetailsInCheckYourAnswersPage(): Unit = {
    verifyContactDetailsFieldValue(firstContactDetailsHeading, "First contact details")
    verifyContactDetailsFieldValue(firstContactDetailsFullName, firstContactFullName)
    verifyContactDetailsFieldValue(firstContactDetailsRole, rndRole)
    verifyContactDetailsFieldValue(firstContactDetailsEmailAddress, rndEmailAddress)
    verifyContactDetailsFieldValue(firstContactDetailsPhoneNumber, rndPhoneNumber)
  }

  def verifySecondContactDetailsInCheckYourAnswersPage(): Unit = {
    verifyContactDetailsFieldValue(secondContactDetailsHeading, "Second contact details")
    verifyContactDetailsFieldValue(secondContactDetailsFullName, secondContactFullName)
    verifyContactDetailsFieldValue(secondContactDetailsRole, rndRole)
    verifyContactDetailsFieldValue(secondContactDetailsEmailAddress, rndEmailAddress)
    verifyContactDetailsFieldValue(secondContactDetailsPhoneNumber, rndPhoneNumber)
  }

  def verifyFullNameErrorSummaryOnContactDetailsPage(): Unit = {
    clickContinueButtonElement()
    errorSummary(errorMessageForEnterFullName)
  }

  def verifyEmailAddressErrorSummaryOnContactDetailsPage(): Unit = {
    clickContinueButtonElement()
    errorSummary(errorMessageForEnterEmailAddress)
  }

  def verifyRoleErrorSummaryOnContactDetailsPage(): Unit = {
    clickContinueButtonElement()
    errorSummary(errorMessageForEnterRole)
  }

  def verifyPhoneNumberErrorSummaryOnContactDetailsPage(): Unit = {
    clickContinueButtonElement()
    errorSummary(errorMessageForEnterPhoneNumber)
  }

  def errorSummary(expectedErrorSummary: String): Unit = {
    val contactDetailsErrorSummaryElement =
      fluentWait.until(ExpectedConditions.visibilityOfElementLocated(contactDetailsErrorSummary))

    val actualTextValue = contactDetailsErrorSummaryElement.getText

    actualTextValue.trim mustBe expectedErrorSummary
  }

  def changeContactFullName(fullNameLocator: By, changeLinkLocator: By): Unit = {
    val originalFullName =
      fluentWait.until(ExpectedConditions.visibilityOfElementLocated(fullNameLocator)).getText

    val changeLink =
      fluentWait.until(ExpectedConditions.elementToBeClickable(changeLinkLocator))
    changeLink.click()

    enterFullName(randomSecondContactFullName)

    val updatedFullName =
      fluentWait.until(ExpectedConditions.visibilityOfElementLocated(changeLinkLocator)).getText

    updatedFullName must not be originalFullName
  }

  def changeContactEmailAddress(emailAddressLocator: By, changeLinkLocator: By): Unit = {
    val originalFullName =
      fluentWait.until(ExpectedConditions.visibilityOfElementLocated(emailAddressLocator)).getText

    val changeLink =
      fluentWait.until(ExpectedConditions.elementToBeClickable(changeLinkLocator))
    changeLink.click()

    enterFullName(randomSecondContactEmailAddress)

    val updatedFullName =
      fluentWait.until(ExpectedConditions.visibilityOfElementLocated(changeLinkLocator)).getText

    updatedFullName must not be originalFullName
  }
}
