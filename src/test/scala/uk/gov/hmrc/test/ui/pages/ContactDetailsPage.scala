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

  private val continueButton: By                  = By.id("submit")
  private val contactDetailsTextField: By         = By.id("value")
  private val contactDetailsErrorSummary: By      = By.className("a[href='#value']")
  private val yesRadioButton: By                  = By.id("value_0")
  private val noRadioButton: By                   = By.id("value_1")
  private val contactDetailsPageTitle: By         = By.cssSelector("label[for='value']")
  private val firstContactDetailsHeading: By      = By.xpath("//*[@id=\"main-content\"]//h2[1]")
  private val firstContactDetailsFullName: By     = By.xpath("//form[@method='POST']//div[1]//dd[1]")
  private val firstContactDetailsRole: By         = By.xpath("//form[@method='POST']//div[2]//dd[1]")
  private val firstContactDetailsEmailAddress: By = By.xpath("//form[@method='POST']//div[3]//dd[1]")
  private val firstContactDetailsPhoneNumber: By  = By.xpath("//form[@method='POST']//div[4]//dd[1]")

  private val secondContactDetailsHeading: By = By.xpath("//*[@id=\"main-content\"]//h2[2]")

  // Error Summary
  private val errorMessageForEnterFullName: String     = "Enter contactName"
  private val errorMessageForEnterEmailAddress: String = "Enter contactEmail"
  private val errorMessageForEnterRole: String         = "Enter contactRole"
  private val errorMessageForEnterPhoneNumber: String  = "Enter contactPhone"

  // Random Test Data
  val rndFullName: String     = randomName()
  val rndRole: String         = randomRole()
  val rndEmailAddress: String = randomEmail()
  val rndPhoneNumber: String  = randomUkPhoneNumber()

  def verifyContactDetailsPageURL(): Unit = waitFor.until(ExpectedConditions.urlToBe(enterYourContactDetailsLink))

  def clickContinueButtonElement(): Unit = {
    val continueButtonElement = fluentWait.until(ExpectedConditions.visibilityOfElementLocated(continueButton))
    continueButtonElement.click()
  }

  def enterFullName(): Unit = {
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(contactDetailsPageTitle))

    val enterFullNameField = fluentWait.until(ExpectedConditions.visibilityOfElementLocated(contactDetailsTextField))
    enterFullNameField.clear()
    enterFullNameField.sendKeys(rndFullName)
    clickContinueButtonElement()
  }

  def enterEmailAddress(): Unit = {
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(contactDetailsPageTitle))

    val enterEmailAddressField =
      fluentWait.until(ExpectedConditions.visibilityOfElementLocated(contactDetailsTextField))
    enterEmailAddressField.clear()
    enterEmailAddressField.sendKeys(rndEmailAddress)
    clickContinueButtonElement()
  }

  def enterRoll(): Unit = {
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(contactDetailsPageTitle))

    val enterRoleField = fluentWait.until(ExpectedConditions.visibilityOfElementLocated(contactDetailsTextField))
    enterRoleField.clear()
    enterRoleField.sendKeys(rndRole)
    clickContinueButtonElement()
  }

  def enterPhoneNumber(): Unit = {
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(contactDetailsPageTitle))

    val enterPhoneNumberField = fluentWait.until(ExpectedConditions.visibilityOfElementLocated(contactDetailsTextField))
    enterPhoneNumberField.clear()
    enterPhoneNumberField.sendKeys(rndPhoneNumber)
    clickContinueButtonElement()
  }

  def selectYes(): Unit = {
    driver.findElement(yesRadioButton).click()
    clickContinueButtonElement()
  }

  def selectNo(): Unit = {
    val noRadioButtonElement = fluentWait.until(ExpectedConditions.visibilityOfElementLocated(noRadioButton))
    noRadioButtonElement.click()
    clickContinueButtonElement()
  }

  def verifyFirstContactDetailsTitleValue(): Unit = {
    val firstContactDetailsHeadingElement =
      fluentWait.until(ExpectedConditions.visibilityOfElementLocated(firstContactDetailsHeading)).getText

    firstContactDetailsHeadingElement.trim mustBe "First contact details"
  }

  def verifyFirstContactDetailsFullNameValue(): Unit = {
    val firstContactDetailsHeadingElement =
      fluentWait.until(ExpectedConditions.visibilityOfElementLocated(firstContactDetailsFullName)).getText

    firstContactDetailsHeadingElement.trim mustBe rndFullName
  }

  def verifyFirstContactDetailsRoleValue(): Unit = {
    val firstContactDetailsHeadingElement =
      fluentWait.until(ExpectedConditions.visibilityOfElementLocated(firstContactDetailsRole)).getText

    firstContactDetailsHeadingElement.trim mustBe rndRole
  }

  def verifyFirstContactDetailsEmailAddressValue(): Unit = {
    val firstContactDetailsHeadingElement =
      fluentWait.until(ExpectedConditions.visibilityOfElementLocated(firstContactDetailsEmailAddress)).getText

    firstContactDetailsHeadingElement.trim mustBe rndEmailAddress
  }

  def verifyFirstContactDetailsPhoneNumberValue(): Unit = {
    val firstContactDetailsHeadingElement =
      fluentWait.until(ExpectedConditions.visibilityOfElementLocated(firstContactDetailsPhoneNumber)).getText

    firstContactDetailsHeadingElement.trim mustBe rndPhoneNumber
  }

  def verifySecondContactInCheckYourAnswersPage(): Unit = {
    val secondContactDetailsHeadingElement =
      fluentWait.until(ExpectedConditions.visibilityOfElementLocated(secondContactDetailsHeading)).getText

    secondContactDetailsHeadingElement.trim mustBe "Second contact details"
  }

  def verifyFirstContactDetailsInCheckYourAnswersPage(): Unit = {
    verifyFirstContactDetailsTitleValue()
    verifyFirstContactDetailsFullNameValue()
    verifyFirstContactDetailsRoleValue()
    verifyFirstContactDetailsEmailAddressValue()
    verifyFirstContactDetailsPhoneNumberValue()
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
