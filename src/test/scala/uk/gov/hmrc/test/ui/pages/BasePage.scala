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

import org.openqa.selenium.support.ui.{ExpectedConditions, FluentWait, Select, Wait}
import org.openqa.selenium.{By, WebDriver}
import org.scalatest.matchers.should.Matchers
import uk.gov.hmrc.selenium.component.PageObject
import uk.gov.hmrc.selenium.webdriver.Driver
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.driver.BrowserDriver

import java.time.Duration

trait BasePage extends BrowserDriver with Matchers with PageObject {
  case class PageNotFoundException(message: String) extends Exception(message)

  val pageUrl: String
  val baseRegUrl: String =
    TestConfiguration.url("senior-accounting-officer-registration-frontend")
  val backLinkText: By   = By.linkText("Back")

  def navigateTo(url: String): Unit = driver.navigate().to(url)

  def fluentWait: Wait[WebDriver] = new FluentWait[WebDriver](Driver.instance)
    .withTimeout(Duration.ofSeconds(2))
    .pollingEvery(Duration.ofMillis(200))

  def onPage(url: String = this.pageUrl): Unit = fluentWait.until(ExpectedConditions.urlToBe(url))

  def selectDropdownById(id: By): Select = new Select(driver.findElement(id: By))

  def clickOnBackLink(): Unit =
    click(backLinkText)

  def clickOnByPartialLinkText(partialLinkText: By): Unit = {
    onPage()
    click(partialLinkText)
  }
}
