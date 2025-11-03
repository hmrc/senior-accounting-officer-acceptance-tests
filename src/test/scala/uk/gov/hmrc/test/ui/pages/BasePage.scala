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
import org.scalatest.matchers.must.Matchers
import uk.gov.hmrc.selenium.component.PageObject
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.driver.BrowserDriver
import uk.gov.hmrc.test.ui.support.IdGenerators

import java.time.Duration

trait BasePage extends BrowserDriver with Matchers with IdGenerators with PageObject {
  case class PageNotFoundException(message: String) extends Exception(message)

  def pageUrl: String
  def pageTitle: String
  def baseRegUrl: String = TestConfiguration.url("senior-accounting-officer-registration-frontend")

  def navigateTo(url: String): Unit = driver.navigate().to(url)

  protected def waitFor = new WebDriverWait(driver, Duration.ofSeconds(2))

}
