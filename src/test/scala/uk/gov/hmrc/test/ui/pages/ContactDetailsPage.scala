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

  private val firstContactDetailsHeading: By      = By.xpath("//*[@id=\"main-content\"]//h2[1]")
  private val firstContactDetailsFullName: By     = By.xpath("(//dl[@class='govuk-summary-list'])[1]/div[1]/dd[1]")
  private val firstContactDetailsRole: By         = By.xpath("(//dl[@class='govuk-summary-list'])[1]/div[2]/dd[1]")
  private val firstContactDetailsEmailAddress: By = By.xpath("(//dl[@class='govuk-summary-list'])[1]/div[3]/dd[1]")
  private val firstContactDetailsPhoneNumber: By  = By.xpath("(//dl[@class='govuk-summary-list'])[1]/div[4]/dd[1]")

  private val secondContactDetailsHeading: By      = By.xpath("//*[@id=\"main-content\"]//h2[2]")
  private val secondContactDetailsFullName: By     = By.xpath("(//dl[@class='govuk-summary-list'])[2]/div[1]/dd[1]")
  private val secondContactDetailsRole: By         = By.xpath("(//dl[@class='govuk-summary-list'])[2]/div[2]/dd[1]")
  private val secondContactDetailsEmailAddress: By = By.xpath("(//dl[@class='govuk-summary-list'])[2]/div[3]/dd[1]")
  private val secondContactDetailsPhoneNumber: By  = By.xpath("(//dl[@class='govuk-summary-list'])[2]/div[4]/dd[1]")

  // Random Test Data
  val rndFullName: String     = randomFullName()
  val rndRole: String         = randomRole()
  val rndEmailAddress: String = randomEmail()
  val rndPhoneNumber: String  = randomUkPhoneNumber()

  // Error Summary
  private val errorMessageForEnterFullName: String     = "Enter contactName"
  private val errorMessageForEnterEmailAddress: String = "Enter contactEmail"
  private val errorMessageForEnterRole: String         = "Enter contactRole"
  private val errorMessageForEnterPhoneNumber: String  = "Enter contactPhone"

  def verifyContactDetailsPageURL(): Unit = waitFor.until(ExpectedConditions.urlToBe(enterYourContactDetailsLink))

  def clickContinueButtonElement(): Unit = {
    val continueButtonElement = fluentWait.until(ExpectedConditions.visibilityOfElementLocated(continueButton))
    continueButtonElement.click()
  }

  def enterFullName(): Unit = {
    val enterFullNameField = fluentWait.until(ExpectedConditions.elementToBeClickable(contactDetailsTextField))
    enterFullNameField.clear()
    enterFullNameField.sendKeys(rndFullName)
  }

  def enterEmailAddress(): Unit = {
    val enterEmailAddressField =
      fluentWait.until(ExpectedConditions.elementToBeClickable(contactDetailsTextField))
    enterEmailAddressField.clear()
    enterEmailAddressField.sendKeys(rndEmailAddress)
  }

  def enterRole(): Unit = {
    val enterRoleField = fluentWait.until(ExpectedConditions.elementToBeClickable(contactDetailsTextField))
    enterRoleField.clear()
    enterRoleField.sendKeys(rndRole)
  }

  def enterPhoneNumber(): Unit = {
    val enterPhoneNumberField = fluentWait.until(ExpectedConditions.elementToBeClickable(contactDetailsTextField))
    enterPhoneNumberField.clear()
    enterPhoneNumberField.sendKeys(rndPhoneNumber)
  }

  def selectYes(): Unit = {
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(addedAllTheContactsYouNeedPageTitle))

    driver.findElement(yesRadioButton).click()
  }

  def selectNo(): Unit = {
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(addedAllTheContactsYouNeedPageTitle))

    driver.findElement(noRadioButton).click()
  }

  def verifyContactDetailsFieldValue(elementLocator: By, expectedValue: String): Unit = {
    val contactDetailsElementValue =
      fluentWait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator)).getText

    contactDetailsElementValue.trim mustBe expectedValue
  }

  def verifyFirstContactDetailsInCheckYourAnswersPage(): Unit = {
    verifyContactDetailsFieldValue(firstContactDetailsHeading, "First contact details")
    verifyContactDetailsFieldValue(firstContactDetailsFullName, rndFullName)
    verifyContactDetailsFieldValue(firstContactDetailsRole, rndRole)
    verifyContactDetailsFieldValue(firstContactDetailsEmailAddress, rndEmailAddress)
    verifyContactDetailsFieldValue(firstContactDetailsPhoneNumber, rndPhoneNumber)
  }

  def verifySecondContactDetailsInCheckYourAnswersPage(): Unit = {
    verifyContactDetailsFieldValue(secondContactDetailsHeading, "Second contact details")
    verifyContactDetailsFieldValue(secondContactDetailsFullName, rndFullName)
    verifyContactDetailsFieldValue(secondContactDetailsRole, rndRole)
    verifyContactDetailsFieldValue(secondContactDetailsEmailAddress, rndEmailAddress)
    verifyContactDetailsFieldValue(secondContactDetailsPhoneNumber, rndPhoneNumber)
  }

  def verifyContactDetailsEnterFullNameErrorSummary(): Unit =
    errorSummary(errorMessageForEnterFullName)

  def verifyContactDetailsEnterEmailAddressErrorSummary(): Unit =
    errorSummary(errorMessageForEnterEmailAddress)

  def verifyContactDetailsEnterRoleErrorSummary(): Unit =
    errorSummary(errorMessageForEnterRole)

  def verifyContactDetailsEnterPhoneNumberErrorSummary(): Unit =
    errorSummary(errorMessageForEnterPhoneNumber)

  def errorSummary(expectedErrorSummary: String): Unit = {
    val contactDetailsErrorSummaryElement =
      fluentWait.until(ExpectedConditions.visibilityOfElementLocated(contactDetailsErrorSummary))

    val actualTextValue = contactDetailsErrorSummaryElement.getText

    actualTextValue.trim mustBe expectedErrorSummary
  }
}
