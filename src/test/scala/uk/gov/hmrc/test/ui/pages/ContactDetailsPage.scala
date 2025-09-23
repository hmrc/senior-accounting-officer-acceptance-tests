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

object ContactDetailsPage extends BasePage {
  override val pageUrl: String = baseRegUrl

  val enterYourContactDetailsLink: String = pageUrl + "/contact-details"

  private val continueButton: By = By.id("submit")

  def verifyContactDetailsPageURL(): Unit = waitFor.until(ExpectedConditions.urlToBe(enterYourContactDetailsLink))

  def continueButtonElement: WebElement =
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(continueButton))
}
