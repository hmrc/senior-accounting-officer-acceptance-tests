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
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.RegisterYourCompanyPage.{baseRegUrl, fluentWait}

object GrsCompanyDetailsPages extends BasePage {
  override val pageUrl: String = baseRegUrl

  val grsCompanyDetailsPageUrl: String = TestConfiguration.url("incorporated-entity-identification-frontend")

  private val companyRegistrationNumberTextBox: By = By.id("companyNumber")
  private val companyUtrNumberTextBox: By          = By.id("ctutr")
  private val selectYesRadioButton: By             = By.id("confirmBusinessName")

  private val checkYourAnswersCrnValue: By =
    By.cssSelector(
      "body > div:nth-child(4) > main:nth-child(3) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > dl:nth-child(2) > div:nth-child(1) > dd:nth-child(2)"
    )

  private val checkYourAnswersUtrValue: By =
    By.cssSelector(
      "body > div:nth-child(4) > main:nth-child(3) > div:nth-child(1) > div:nth-child(1) > div:nth-child(1) > dl:nth-child(2) > div:nth-child(2) > dd:nth-child(2)"
    )

  def verifyGrsCompanyDetailsPageURL(): Unit = waitFor.until(ExpectedConditions.urlContains(grsCompanyDetailsPageUrl))

  def enterCompanyRegistrationNumber(): Unit = {
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(companyRegistrationNumberTextBox))
    driver.findElement(companyRegistrationNumberTextBox).sendKeys("AB123456")
    clickContinueButton()
  }

  def selectYesForIsThisYourBusiness(): Unit = {
    fluentWait.until(ExpectedConditions.presenceOfElementLocated(selectYesRadioButton))
    click(selectYesRadioButton)
    clickContinueButton()
  }

  def enterUTRNumber(): Unit = {
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(companyUtrNumberTextBox))
    driver.findElement(companyUtrNumberTextBox).sendKeys("1234567890")
    clickContinueButton()
  }

  def verifyCheckYourAnswers(): Unit = {
    verifyCheckYourAnswersCrnValue()
    verifyCheckYourAnswersUtrValue()
    clickContinueButton()
  }

  def verifyCheckYourAnswersCrnValue(): Unit = {
    val crnValue = fluentWait
      .until(ExpectedConditions.visibilityOfElementLocated(checkYourAnswersCrnValue))
      .getText

    crnValue mustBe "AB123456"
  }

  def verifyCheckYourAnswersUtrValue(): Unit = {
    val utrValue = fluentWait
      .until(ExpectedConditions.visibilityOfElementLocated(checkYourAnswersUtrValue))
      .getText

    utrValue mustBe "1234567890"
  }
}
