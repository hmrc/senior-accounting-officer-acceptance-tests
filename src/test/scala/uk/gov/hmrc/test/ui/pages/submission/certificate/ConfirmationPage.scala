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

package uk.gov.hmrc.test.ui.pages.submission.certificate

import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.{BasePage, DynamicUrlWithUnknownParam, StaticTitle}
import uk.gov.hmrc.test.ui.support.SubmissionButtonSupport

import java.util.regex.Pattern

object ConfirmationPage
    extends BasePage
    with DynamicUrlWithUnknownParam[String]
    with StaticTitle
    with SubmissionButtonSupport {

  private val host: String = TestConfiguration.url("senior-accounting-officer-submission-frontend")

  override val pageTitle: String =
    "Certificate submitted - Senior Accounting Officer notification and certificate - GOV.UK"

  override protected val urlRegex: String =
    s"^${Pattern.quote(host)}/certificate-confirmation\\?certificateIdReferenceNumber=(CRT[0-9]{10})$$"

  override def extractParams: String = {
    val UrlPattern      = urlRegex.r
    val UrlPattern(ref) = driver.getCurrentUrl: @unchecked
    ref
  }

  private val certificateReferenceNumber: By = testId("notification-reference-number")

  def certificateReference: String = {
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(certificateReferenceNumber))
    driver.findElement(certificateReferenceNumber).getText
  }

  def assertReferenceNumberMatchesUrl(): Unit = {
    certificateReference mustBe extractParams
  }
}
