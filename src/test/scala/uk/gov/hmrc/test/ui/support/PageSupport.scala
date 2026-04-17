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

package uk.gov.hmrc.test.ui.support

import org.openqa.selenium.support.ui.*
import org.openqa.selenium.{By, WebDriver}
import org.scalatest.matchers.must.Matchers
import uk.gov.hmrc.selenium.component.PageObject
import uk.gov.hmrc.test.ui.driver.BrowserDriver
import uk.gov.hmrc.test.ui.pages.CommonPage

import java.lang
import java.time.Duration

object PageSupport extends BrowserDriver with Matchers with PageObject {

  val backLink: By       = By.cssSelector(".govuk-back-link")
  val continueButton: By = By.id("continue")

  def fluentWait: Wait[WebDriver] = new FluentWait[WebDriver](driver)
    .withTimeout(Duration.ofSeconds(5))
    .pollingEvery(Duration.ofMillis(200))

  def selectDropdownById(id: By): Select = new Select(driver.findElement(id: By))

  def clickOnBackLink(): Unit = fluentWait.until(ExpectedConditions.elementToBeClickable(backLink)).click()

  def clickContinueButton(): Unit =
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(continueButton)).click()

  def assertElementIsClickable(locator: By): Unit = fluentWait.until(ExpectedConditions.elementToBeClickable(locator))

  def clickElement(locator: By): Unit = fluentWait.until(ExpectedConditions.visibilityOfElementLocated(locator)).click()

  def clickRadioButton(locator: By): Unit =
    fluentWait.until(ExpectedConditions.presenceOfElementLocated(locator)).click()

  def assertTextOnPage(locator: By, text: String): Unit = {
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(locator))
    text mustBe driver.findElement(locator).getText
  }

  def assertOnPage(url: String): Unit = fluentWait.until(ExpectedConditions.urlToBe(url))

  def assertPageWithError(page: CommonPage): Unit = assertOnPage(page, Some(page.pageErrorTitle))

  def assertElementNotVisible(locator: By): Unit = {
    val elementNotVisible = fluentWait.until(ExpectedConditions.invisibilityOfElementLocated(locator))
    elementNotVisible mustBe true
  }

  def assertOnPage(page: CommonPage, expectedTitle: String): Unit = assertOnPage(page, Some(expectedTitle))

  def assertOnPage(page: CommonPage): Unit = assertOnPage(page, None)

  private def assertOnPage(page: CommonPage, titleOverride: Option[String]): Unit = {
    val expectedTitle = titleOverride.getOrElse(page.pageTitle)
    fluentWait.until(_ => getCurrentUrl == page.pageUrl && getTitle == expectedTitle)
    getCurrentUrl mustBe page.pageUrl
    getTitle mustBe expectedTitle
  }

  override def sendKeys(locator: By, value: String): Unit = {
    fluentWait.until(ExpectedConditions.elementToBeClickable(locator)).clear()
    driver.findElement(locator).sendKeys(value)
  }
}
