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

import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.{By, WebElement}
import uk.gov.hmrc.test.ui.pages.RegisterYourCompanyPage.{baseRegUrl, fluentWait}

object CompanyDetailsPage extends BasePage {
  override val pageUrl: String = baseRegUrl

  val enterYourCompanyDetailsLink: String = pageUrl + "/business-match"
  val companyDetailsPageUrl: String       = pageUrl + "/test-only/grs-stub"

  private val stubResponseButton = By.id("submit")

  def stubResponseButtonElement: WebElement =
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(stubResponseButton))

  def verifyCompanyDetailsPageURL(): Unit = waitFor.until(ExpectedConditions.urlContains(companyDetailsPageUrl))

  def verifyStubResponseButton(): Unit = {
    val isVisible = stubResponseButtonElement.isDisplayed
    isVisible mustBe true

    val isEnabled = stubResponseButtonElement.isEnabled
    isEnabled mustBe true
  }

  def elementStubResponseButtonClickable(): Unit =
    fluentWait
      .until(ExpectedConditions.elementToBeClickable(stubResponseButton))

  def clickStubResponseButton(): Unit = click(stubResponseButton)
}
