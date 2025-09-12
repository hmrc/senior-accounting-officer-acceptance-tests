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

import org.openqa.selenium.{By, WebElement}
import org.openqa.selenium.support.ui.ExpectedConditions
import org.scalactic.Prettifier.default
import uk.gov.hmrc.test.ui.pages.CompanyDetailsPage.companyDetailsHref

import scala.jdk.CollectionConverters.*
import scala.language.postfixOps

object RegisterYourCompanyPage extends BasePage {
  override val pageUrl: String  = baseRegUrl
  private val pageTitle: String =
    "Register your company - Senior Accounting Officer notification and certificate - GOV.UK"

  // Company Details Section
  private val companyDetailsField             = By.xpath(s"(//div[@class='govuk-task-list__name-and-hint'])[1]")
  private val companyDetailsFieldText: String = "Enter your company details"
  private val companyDetailsStatus            = By.id("company-details-status")

  // Contact Details Section
  private val contactDetailsField             = By.xpath("(//div[@class='govuk-task-list__name-and-hint'])[2]")
  private val contactDetailsFieldText: String = "Enter your contact details"
  private val contactDetailsStatus            = By.id("contacts-details-status")

  // Submit Button section
  private val submitButton = By.id("submit")

  // Dashboard Content
  private val statusNotStarted: String       = "Not started"
  private val statusCannotStartedYet: String = "Cannot start yet"

  def companyDetailsElement: WebElement =
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(companyDetailsField))

  def verifyCompanyDetailsField(): Unit = {
    val isVisible = companyDetailsElement.isDisplayed
    isVisible mustBe true

    val isEnabled = companyDetailsElement.isEnabled
    isEnabled mustBe true

    val actualText = companyDetailsElement.getText
    actualText.trim mustBe companyDetailsFieldText
  }

  def verifyEnterYourCompanyDetailsLink(): Unit = {
    val actualHref = companyDetailsElement.findElement(By.tagName("a")).getAttribute("href")
    actualHref.trim mustBe companyDetailsHref
  }

  def verifyCompanyDetailsStatus(): Unit = {
    val companyDetailsStatusText = fluentWait
      .until(ExpectedConditions.visibilityOfElementLocated(companyDetailsStatus))

    val isVisible = companyDetailsStatusText.isDisplayed
    isVisible mustBe true

    val isEnabled = companyDetailsStatusText.isEnabled
    isEnabled mustBe true

    val actualText = companyDetailsStatusText.getText
    actualText.trim mustBe statusNotStarted
  }

  def clickCompanyDetails(): Unit = click(companyDetailsField)

  def verifyContactDetailsField(): Unit = {
    val contactDetailsElement = fluentWait
      .until(ExpectedConditions.visibilityOfElementLocated(contactDetailsField))

    val isVisible = contactDetailsElement.isDisplayed
    isVisible mustBe true

    val isEnabled = contactDetailsElement.isEnabled
    isEnabled mustBe true

    val actualHrefs = contactDetailsElement.findElements(By.tagName("a")).asScala
    actualHrefs mustBe empty

    val actualText = contactDetailsElement.getText
    actualText.trim mustBe contactDetailsFieldText
  }

  def verifyContactDetailsStatus(): Unit = {
    val contactDetailsStatusText = fluentWait
      .until(ExpectedConditions.visibilityOfElementLocated(contactDetailsStatus))

    val isVisible = contactDetailsStatusText.isDisplayed
    isVisible mustBe true

    val isEnabled = contactDetailsStatusText.isEnabled
    isEnabled mustBe true

    val actualText = contactDetailsStatusText.getText
    actualText.trim mustBe statusCannotStartedYet
  }

  def verifyRegisterYourCompanyPageURL(): Unit = {
    waitFor.until(ExpectedConditions.urlToBe(pageUrl))
    assert(driver.getCurrentUrl == pageUrl)
  }

  def verifyRegisterYourCompanyPageTitle(): Unit = {
    val registerYourCompanyPageTitle = driver.getTitle
    registerYourCompanyPageTitle mustBe pageTitle

  }

  def verifySubmitButtonDoestNotExist(): Unit = {
    val submitButtonElements = driver.findElements(submitButton).asScala
    submitButtonElements mustBe empty
  }
}
