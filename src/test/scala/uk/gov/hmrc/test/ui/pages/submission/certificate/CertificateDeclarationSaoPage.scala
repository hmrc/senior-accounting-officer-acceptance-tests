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
import uk.gov.hmrc.test.ui.support.PageSupport.{assertElementIsClickable, assertTextOnPage, sendKeys}
import uk.gov.hmrc.test.ui.support.{BackLinkSupport, ErrorMessageSupport, SubmissionButtonSupport}

object CertificateDeclarationSaoPage
    extends CommonPage
    with SubmissionButtonSupport
    with BackLinkSupport
    with ErrorMessageSupport {
  override val pageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/certificate/submit-certificate-confirm-sao"

  override val pageTitle: String =
    "Confirm the certificate - Senior Accounting Officer notification and certificate - GOV.UK"

  val changePageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/certificate/change-submit-certificate-confirm-sao"

  override protected def submissionButtonLocator: By = By.cssSelector(".govuk-button")

<<<<<<< HEAD
  val saoNameInput: By = By.cssSelector("#value")

  def addSaoName(text: String): Unit = {
    sendKeys(saoNameInput, text)
=======
  val heading: By          = By.cssSelector("h1")
  val declarationInput: By = By.cssSelector("#value")

  def assertCorePageContent(): Unit = {
    assertTextOnPage(heading, "Confirm the certificate")
    assertElementIsClickable(declarationInput)
    assertTextOnPage(submissionButtonLocator, "Confirm")
  }

  def addDeclaration(text: String): Unit = {
    sendKeys(declarationInput, text)
>>>>>>> 57d7d00 (confirm certificate updates)
  }
}
