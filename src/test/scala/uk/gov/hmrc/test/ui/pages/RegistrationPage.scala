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
import org.openqa.selenium.By
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

  private val contactDetailsPage      = new ContactDetailsPage
  private val submitButton: By        = By.cssSelector("#submit")
  private val companyDetailsField: By = actionListItem(1)
  private val contactDetailsField: By = actionListItem(2)

  private val pageLinks: Map[PageLink, (By, String)] = Map(
    EnterYourCompanyDetailsLink -> (
      By.cssSelector("ul.govuk-task-list > li.govuk-task-list__item--with-link:nth-of-type(1)"),
      s"${pageUrl.stripSuffix("/")}/business-match"
    )
  )

  private def actionListItem(index: Int): By =
    By.cssSelector(s"ul.govuk-task-list > li.govuk-task-list__item--with-link:nth-of-type($index)")

  def assertLinkIsVisibleWithText(link: PageLink): Unit = {
    val (elementWithLink: By, expectedLinkText: String) = pageLinks(link)

    val element  = fluentWait.until(ExpectedConditions.visibilityOfElementLocated(elementWithLink))
    val linkText = element.findElement(By.tagName("a")).getAttribute("href").trim
    element.isDisplayed mustBe true
    linkText mustBe expectedLinkText
  }

  def assertEnterYourCompanyDetailsLinkNotFound(): Unit =
    fluentWait
      .until(ExpectedConditions.visibilityOfElementLocated(companyDetailsField))
      .findElements(By.tagName("a"))
      .asScala mustBe empty

  def clickEnterYourCompanyDetailsLink(): Unit =
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(companyDetailsField)).click()

  def assertSectionStatus(section: RegistrationPageSection, expectedStatus: RegistrationPageSectionStatus): Unit = {
    val statusElement = new FluentWait(driver)
      .until(ExpectedConditions.visibilityOfElementLocated(sectionLocators(section)))
    statusElement.getText.trim mustBe expectedStatus.toString withClue
      s"Expected a status of '$expectedStatus' for the '$section' section, but found '${statusElement.getText}'"
  }

  def assertEnterYourContactDetailsLinkNotFound(): Unit =
    fluentWait
      .until(ExpectedConditions.visibilityOfElementLocated(contactDetailsField))
      .findElements(By.tagName("a"))
      .asScala mustBe empty

  def assertEnterYourContactDetailsLinkFound(): Unit =
    fluentWait
      .until(ExpectedConditions.visibilityOfElementLocated(contactDetailsField))
      .findElement(By.tagName("a"))
      .getAttribute("href")
      .trim mustBe contactDetailsPage.enterYourContactDetailsLinkUrl

  def clickEnterYourContactDetailsLink(): Unit =
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(contactDetailsField)).click()

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
