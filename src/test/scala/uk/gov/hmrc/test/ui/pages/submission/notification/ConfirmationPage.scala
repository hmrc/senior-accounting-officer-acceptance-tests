/*
 * Copyright 2026 HM Revenue & Customs
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

package uk.gov.hmrc.test.ui.pages.submission.notification

import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.BasePage
import uk.gov.hmrc.test.ui.support.PageSupport.fluentWait

object ConfirmationPage extends BasePage {
  override val pageUrl: String   =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/notification/confirmation"
  override val pageTitle: String =
    "Confirmation page - Senior Accounting Officer notification and certificate - GOV.UK"

  def assertReferenceNumberEquals(number: String): Unit = {
    val referenceNumberElement: By = By.cssSelector("""[data-test-id="notification-reference-number"]""")
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(referenceNumberElement))
    driver.findElement(referenceNumberElement).getText mustBe number
  }
}
