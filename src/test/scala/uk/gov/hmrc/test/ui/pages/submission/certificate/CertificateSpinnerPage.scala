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

import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait}
import org.openqa.selenium.{By, WebDriver}
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.submission.certificate.UploadSubmissionTemplatePage.fluentWait
import uk.gov.hmrc.test.ui.pages.{BasePage, DynamicUrlWithKnownParam, StaticTitle}
import uk.gov.hmrc.test.ui.support.GrsJourneyIdSupport

import scala.concurrent.duration.*

import java.time.{Duration, Instant}

object CertificateSpinnerPage
    extends BasePage
    with DynamicUrlWithKnownParam[String]
    with StaticTitle
    with GrsJourneyIdSupport {
  private val baseUrl: String = TestConfiguration.url("senior-accounting-officer-submission-frontend")

  override def pageUrl(param: String): String =
    s"$baseUrl/certificate/submitting?idempotencyKey=$param"

  override def pageTitle: String =
    "Submitting your certificate - Senior Accounting Officer notification and certificate - GOV.UK"

  val pageHeadingElement: By = By.tagName("h1")

  def assertOnPage(): Unit = {
    fluentWait.until(ExpectedConditions.urlContains(pageUrl(journeyId)))
    fluentWait.until(ExpectedConditions.titleContains(pageTitle))

    fluentWait.until(
      ExpectedConditions.textToBePresentInElementLocated(pageHeadingElement, "Submitting your certificate")
    )

    val startTime = Instant.now()

    new WebDriverWait(driver, Duration.ofSeconds(3))
      .until(_ => Duration.between(startTime, Instant.now()).toMillis >= 2000)
  }
}
