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
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.CommonPage
import uk.gov.hmrc.test.ui.support.PageSupport.assertTextOnPage
import uk.gov.hmrc.test.ui.support.PageSupport.sendKeys
import uk.gov.hmrc.test.ui.support.{ErrorMessageSupport, PageSupport, SubmissionButtonSupport}

object CertificateSaoEmailPage extends CommonPage with SubmissionButtonSupport with ErrorMessageSupport {
  override val pageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/certificate-only/sao-email"

  override val pageTitle: String =
    "What is the email address for the SAO? - Senior Accounting Officer notification and certificate - GOV.UK"

  val pageHeadingElement: By = By.cssSelector(".govuk-label-wrapper")

  override protected def submissionButtonLocator: By = By.cssSelector(".govuk-button")

  val saoEmailInput: By = By.cssSelector("#value")

  def addEmail(emailAddress: String): Unit = {
    sendKeys(saoEmailInput, emailAddress)
  }

  def assertHeadingMatches(text: String): Unit = {
    assertTextOnPage(pageHeadingElement, text)
  }
}
