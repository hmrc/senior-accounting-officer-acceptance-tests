/*
 * Copyright 2023 HM Revenue & Customs
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

import org.openqa.selenium.support.ui.*
import org.openqa.selenium.{By, WebDriver, WebElement}
//import org.scalatest.AppendedClues.convertToClueful
import org.scalatest.matchers.must.Matchers
import uk.gov.hmrc.selenium.component.PageObject
//import uk.gov.hmrc.test.ui.adt.PageLink
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.driver.BrowserDriver
import uk.gov.hmrc.test.ui.support.IdGenerators

import java.time.Duration

trait BasePage extends BrowserDriver with Matchers with IdGenerators with PageObject {
  case class PageNotFoundException(message: String) extends Exception(message)

  def pageUrl: String
  def pageTitle: String
  def baseRegUrl: String = TestConfiguration.url("senior-accounting-officer-registration-frontend")

  protected val backLink: By       = By.cssSelector(".govuk-back-link")
  protected val submitButton: By   = By.id("submit")
  protected val continueButton: By = By.id("continue")

  // def pageLinks: Map[PageLink, By]

  def navigateTo(url: String): Unit = driver.navigate().to(url)

  protected def waitFor = new WebDriverWait(driver, Duration.ofSeconds(2))

//  def assertOnPage(url: String = this.pageUrl): Unit = fluentWait.until(ExpectedConditions.urlToBe(url))
//
//  def assertOnPage(page: BasePage): Unit = {
//    fluentWait.until(_ => getCurrentUrl == page.pageUrl && getTitle == page.pageTitle)
//    getCurrentUrl mustBe page.pageUrl
//    getTitle mustBe page.pageTitle
//
//    println(s"CURRENT URL: $getCurrentUrl \nCURRENT TITLE: $getTitle")
//    println(s"URL: $page.pageUrl \nTITLE: $page.pageTitle")
//
//  }

//  def assertLinkIsVisibleWithText(page: BasePage, fieldName: By, expectedUrl: String): Unit = {
//    val field: WebElement = fluentWait.until(ExpectedConditions.visibilityOfElementLocated(fieldName))
//    val link: String = field.findElement(By.tagName("a")).getAttribute("href").trim
//    link mustBe expectedUrl
//    link mustBe field.getText
//  }

//  def selectDropdownById(id: By): Select = new Select(driver.findElement(id: By))
//
//  def clickOnBackLink(): Unit =
//    click(backLink)
//
//  def clickSubmitButton(): Unit = {
//    fluentWait.until(ExpectedConditions.elementToBeClickable(submitButton))
//    click(submitButton)
//  }
//
//  def clickContinueButton(): Unit = {
//    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(continueButton))
//    click(continueButton)
//  }
}
