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
import org.scalactic.Prettifier.default
import uk.gov.hmrc.selenium.webdriver.Driver
import uk.gov.hmrc.test.ui.pages.AuthLoginPage.getText
import uk.gov.hmrc.test.ui.pages.CompanyDetailsPage.{companyDetailsHref, companyDetailsText}
import uk.gov.hmrc.test.ui.pages.ContactDetailsPage.{contactDetailsHref, contactDetailsText}
import uk.gov.hmrc.test.ui.pages.ReviewAndSubmitPage.{checkYourAnswersBeforeSubmittingYourRegistrationHref, checkYourAnswersBeforeSubmittingYourRegistrationText}

import scala.language.postfixOps

object RegisterYourCompanyPage extends BasePage {
  override val pageUrl: String = baseRegUrl

  private val companyDetails                                   = By.xpath("//a[normalize-space()='Company details']")
  private val contactDetails                                   = By.xpath("//a[normalize-space()='Contact details']")
  private val checkYourAnswersBeforeSubmittingYourRegistration =
    By.xpath("//a[contains(text(),'Check your answers before submitting your registra')]")

  private val serviceProblemMessage = By.xpath("//h1[normalize-space()='Sorry, there is a problem with the service']")

  def verifyCompanyDetailsLink(): Unit = {
    val companyDetailsElement = fluentWait
      .until(ExpectedConditions.visibilityOfElementLocated(companyDetails))

    val isVisible = companyDetailsElement.isDisplayed
    assert(isVisible)

    val isEnabled = companyDetailsElement.isEnabled
    assert(isEnabled)

    val actualHref = companyDetailsElement.getAttribute("href")
    actualHref.trim should be(companyDetailsHref)

    val actualText = companyDetailsElement.getText
    actualText.trim should be(companyDetailsText)
  }

  def clickCompanyDetails(): Unit = click(companyDetails)

  def verifyContactDetailsLink(): Unit = {
    val contactDetailsElement = fluentWait
      .until(ExpectedConditions.visibilityOfElementLocated(contactDetails))

    val isVisible = contactDetailsElement.isDisplayed
    assert(isVisible)

    val isEnabled = contactDetailsElement.isEnabled
    assert(isEnabled)

    val actualHref = contactDetailsElement.getAttribute("href")
    actualHref.trim should be(contactDetailsHref)

    val actualText = contactDetailsElement.getText
    actualText.trim should be(contactDetailsText)
  }

  def clickContactDetails(): Unit =
    fluentWait.until(ExpectedConditions.elementToBeClickable(contactDetails)).click()

  def verifyCheckYourAnswersBeforeSubmittingYourRegistrationLink(): Unit = {
    val element = fluentWait
      .until(ExpectedConditions.visibilityOfElementLocated(checkYourAnswersBeforeSubmittingYourRegistration))

    val isVisible = element.isDisplayed
    assert(isVisible)

    val isEnabled = element.isEnabled
    assert(isEnabled)

    val actualHref = element.getAttribute("href")
    actualHref.trim should be(checkYourAnswersBeforeSubmittingYourRegistrationHref)

    val actualText = element.getText
    actualText.trim should equal(checkYourAnswersBeforeSubmittingYourRegistrationText)
  }

  def clickCheckYourAnswersBeforeSubmittingYourRegistration(): Unit =
    click(checkYourAnswersBeforeSubmittingYourRegistration)

  def displayedServiceProblemMessage(): Unit = {
    val displayServiceProblemMessage = getText(serviceProblemMessage)
    displayServiceProblemMessage should be("Sorry, there is a problem with the service")
  }

  def verifyRegisterYourCompanyPageURL(): Unit = {
    waitFor.until(ExpectedConditions.urlToBe(pageUrl))
    assert(Driver.instance.getCurrentUrl == pageUrl)
  }

  def verifyRegisterYourCompanyPageTitle(): Unit = {
    val registerYourCompanyPageTitle = Driver.instance.getTitle
    registerYourCompanyPageTitle should be(
      "Register your company - Senior Accounting Officer notification and certificate - GOV.UK"
    )
  }
}
