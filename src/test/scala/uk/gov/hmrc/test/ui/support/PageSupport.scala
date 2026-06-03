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
import org.openqa.selenium.{By, WebDriver, WebElement}
import org.scalatest.matchers.must.Matchers
import uk.gov.hmrc.selenium.component.PageObject
import uk.gov.hmrc.test.ui.driver.BrowserDriver
import uk.gov.hmrc.test.ui.pages.*

import scala.jdk.CollectionConverters.*

import java.lang
import java.time.Duration

object PageSupport extends BrowserDriver with Matchers with PageObject {

  val backLink: By       = By.cssSelector(".govuk-back-link")
  val continueButton: By = By.id("continue")

  def fluentWait: Wait[WebDriver] = new FluentWait[WebDriver](driver)
    .withTimeout(Duration.ofSeconds(7))
    .pollingEvery(Duration.ofMillis(250))

  def clickElement(locator: By): Unit = {
    getElementIfClickable(locator).click()
  }

  def clickContinueButton(): Unit = {
    getElementIfClickable(continueButton).click()
  }

  def clickRadioButton(locator: By): Unit =
    fluentWait.until(ExpectedConditions.presenceOfElementLocated(locator)).click()

  def clickOnBackLink(): Unit = {
    getElementIfClickable(backLink).click()
  }

  override def sendKeys(locator: By, value: String): Unit = {
    val element = getElementIfVisible(locator)
    element.clear()
    element.sendKeys(value)
  }

  def selectDropdownById(id: By): Select = {
    new Select(driver.findElement(id: By))
  }

  def assertElementIsClickable(locator: By): Unit = {
    fluentWait.until(ExpectedConditions.elementToBeClickable(locator))
  }

  def assertElementNotVisible(locator: By): Unit = {
    val elementNotVisible = fluentWait.until(ExpectedConditions.invisibilityOfElementLocated(locator))
    elementNotVisible mustBe true
  }

  def assertTextIsHyperlink(locator: By, text: String): Unit = {
    val element = getElementIfVisible(locator)
    assertTextOnPage(element, text)
    element.findElement(By.tagName("a")).getAttribute("href") mustNot be(empty)
  }

  def assertTextIsNotHyperlink(locator: By, text: String): Unit = {
    val element = getElementIfVisible(locator)
    assertTextOnPage(element, text)
    element.findElements(By.tagName("a")).asScala mustBe empty
  }

  def assertLinkHasText(link: By, expectedText: String): Unit = {
    assertElementIsClickable(link)
    assertTextOnPage(link, expectedText)
  }

  def assertTextOnPage(locator: By, text: String): Unit = {
    getElementIfVisible(locator).getText mustBe text
  }

  def assertTextOnPage(element: WebElement, text: String): Unit = {
    element.getText mustBe text
  }

  def assertAttributeMatches(locator: By, attribute: String, expectedText: String): Unit = {
    getElementIfVisible(locator).getAttribute(attribute) mustBe expectedText
  }

  private def assertOnPage(expectedPageUrl: String, expectedTitle: String): Unit = {
    assertUrl(expectedPageUrl)
    assertPageTitle(expectedTitle)
  }

  def assertUrl(url: String): Unit = {
    fluentWait.until(ExpectedConditions.urlToBe(url))
  }

  @deprecated(
    "replaced by PageSupport.assertUrl since this doesn't assert the page title like the other assertOnPage variants"
  )
  def assertOnPage(url: String): Unit = {
    assertUrl(url)
  }

  def assertOnPage(page: BasePage with StaticUrl, expectedTitle: String): Unit = {
    assertOnPage(page.pageUrl, expectedTitle)
  }

  def assertOnPage(page: BasePage with StaticUrl with StaticTitle): Unit = {
    assertOnPage(page, None)
  }

  def assertPageWithError(page: BasePage with StaticUrl with StaticTitle): Unit = {
    assertOnPage(page, Some(page.pageErrorTitle))
  }

  private def assertOnPage(page: BasePage with StaticUrl with StaticTitle, titleOverride: Option[String]): Unit = {
    val expectedTitle = titleOverride.getOrElse(page.pageTitle)
    assertOnPage(page, expectedTitle)
  }

  def assertPageTitle(expectedTitle: String): Unit = {
    fluentWait.until(ExpectedConditions.titleIs(expectedTitle))
  }

  private def getElementIfVisible(locator: By): WebElement = {
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(locator))
  }

  private def getElementIfClickable(locator: By): WebElement =
    fluentWait.until(ExpectedConditions.elementToBeClickable(locator))
}
