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

import org.openqa.selenium.support.ui.{ExpectedConditions, FluentWait, Select, Wait}
import org.openqa.selenium.{By, WebDriver}
import uk.gov.hmrc.test.ui.pages.BasePage

import java.time.Duration

object PageSupport extends BasePage {
  def pageUrl: String    = ""
  def pageTitle: String  = ""
  val backLink: By       = By.cssSelector(".govuk-back-link")
  val submitButton: By   = By.id("submit")
  val continueButton: By = By.id("continue")

  def fluentWait: Wait[WebDriver] = new FluentWait[WebDriver](driver)
    .withTimeout(Duration.ofSeconds(5))
    .pollingEvery(Duration.ofMillis(200))

  def selectDropdownById(id: By): Select = new Select(driver.findElement(id: By))

  def clickOnBackLink(): Unit =
    click(backLink)

  def clickSubmitButton(): Unit = {
    fluentWait.until(ExpectedConditions.elementToBeClickable(submitButton))
    click(submitButton)
  }

  def clickContinueButton(): Unit = {
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(continueButton))
    click(continueButton)
  }

  def clickElement(locator: By): Unit =
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(locator)).click()

  def assertOnPage(url: String = this.pageUrl): Unit = fluentWait.until(ExpectedConditions.urlToBe(url))

  def assertOnPage(page: BasePage): Unit = {
    fluentWait.until(_ => getCurrentUrl == page.pageUrl && getTitle == page.pageTitle)
    getCurrentUrl mustBe page.pageUrl
    getTitle mustBe page.pageTitle
  }

  override def sendKeys(locator: By, value: String): Unit = {
    driver.findElement(locator).clear()
    driver.findElement(locator).sendKeys(value)
  }
}
