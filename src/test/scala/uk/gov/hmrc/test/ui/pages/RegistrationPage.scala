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

import org.openqa.selenium.support.ui.{ExpectedConditions, FluentWait}
import org.openqa.selenium.{By, WebElement}
import org.scalatest.AppendedClues.convertToClueful
import uk.gov.hmrc.test.ui.adt.RegistrationPageLink.EnterYourCompanyDetailsLink
import uk.gov.hmrc.test.ui.adt.{PageLink, RegistrationPageSection, RegistrationPageSectionStatus}
import uk.gov.hmrc.test.ui.pages.RegistrationPage.sectionLocators
import uk.gov.hmrc.test.ui.support.PageSupport.fluentWait

import scala.jdk.CollectionConverters.*

class RegistrationPage extends BasePage {
  override val pageUrl: String   = baseRegUrl
  override val pageTitle: String =
    "Register your company - Senior Accounting Officer notification and certificate - GOV.UK"

  val submitButton: By         = By.cssSelector("#submit")
  val contactDetailsPage       = new ContactDetailsPage
  val companyDetailsField: By  =
    By.cssSelector("ul.govuk-task-list > li.govuk-task-list__item--with-link:nth-of-type(1)")
  val contactDetailsField: By  =
    By.cssSelector("ul.govuk-task-list > li.govuk-task-list__item--with-link:nth-of-type(2)")
  val contactDetailsFieldText  = "Enter your contact details"
  val contactDetailsStatus: By = By.cssSelector("#contacts-details-status")
  val statusNotStarted         = "Not started"
  val statusCompleted          = "Completed"

  private val pageLinks: Map[PageLink, (By, String)] = Map(
    EnterYourCompanyDetailsLink -> (
      By.cssSelector("ul.govuk-task-list > li.govuk-task-list__item--with-link:nth-of-type(1)"),
      s"${pageUrl.stripSuffix("/")}/business-match"
    )
  )

  def companyDetailsElement: WebElement =
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(companyDetailsField))

  def assertLinkIsVisibleWithText(link: PageLink): Unit = {
    val (elementWithLink: By, expectedLinkText: String) = pageLinks(link)
    val element                                         = fluentWait.until(ExpectedConditions.visibilityOfElementLocated(elementWithLink))
    val linkText                                        = element.findElement(By.tagName("a")).getAttribute("href").trim
    element.isDisplayed mustBe true
    linkText mustBe expectedLinkText
  }

  def verifyEnterYourCompanyDetailsLinkIsEmpty(): Unit = {
    val enterYourCompanyDetailsLink = companyDetailsElement.findElements(By.tagName("a")).asScala
    enterYourCompanyDetailsLink mustBe empty
  }

  def clickEnterYourCompanyDetailsLink(): Unit =
    companyDetailsElement.click()

  def assertSectionStatus(section: RegistrationPageSection, expectedStatus: RegistrationPageSectionStatus): Unit = {
    val statusElement = new FluentWait(driver)
      .until(ExpectedConditions.visibilityOfElementLocated(sectionLocators(section)))
    statusElement.getText.trim mustBe expectedStatus.toString withClue
      s"Expected a status of '$expectedStatus' for the '$section' section, but found '${statusElement.getText}'"
  }

  def contactDetailsElement: WebElement =
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(contactDetailsField))

  def assertContactDetailsFieldIsNotALink(): Unit = {
    val field = fluentWait.until(ExpectedConditions.visibilityOfElementLocated(contactDetailsField))
    field.findElements(By.tagName("a")).asScala mustBe empty
    field.getText.trim must include(contactDetailsFieldText)
  }

  def verifyEnterYourContactDetailsLink(): Unit =
    contactDetailsElement
      .findElement(By.tagName("a"))
      .getAttribute("href")
      .trim mustBe contactDetailsPage.enterYourContactDetailsLink

  def verifyContactDetailsStatus(expectedStatus: String): Unit = {
    val contactDetailsStatusText = fluentWait.until(ExpectedConditions.visibilityOfElementLocated(contactDetailsStatus))
    contactDetailsStatusText.isDisplayed mustBe true
    contactDetailsStatusText.isEnabled mustBe true
    contactDetailsStatusText.getText.trim mustBe expectedStatus
  }

  def verifyContactDetailsStatusNotStarted(): Unit =
    verifyContactDetailsStatus(statusNotStarted)

  def verifyContactDetailsStatusCompleted(): Unit =
    verifyContactDetailsStatus(statusCompleted)

  def clickEnterYourContactDetailsLink(): Unit =
    contactDetailsElement.click()

  def assertSubmitButtonDoesNotExist(): Unit = {
    val submitButtonElements = driver.findElements(submitButton).asScala
    submitButtonElements mustBe empty
  }
}

object RegistrationPage {

  private val sectionLocators: Map[RegistrationPageSection, By] = Map(
    RegistrationPageSection.CompanyDetails -> By.cssSelector("#company-details-status"),
    RegistrationPageSection.ContactDetails -> By.cssSelector("#contacts-details-status")
  )
}
