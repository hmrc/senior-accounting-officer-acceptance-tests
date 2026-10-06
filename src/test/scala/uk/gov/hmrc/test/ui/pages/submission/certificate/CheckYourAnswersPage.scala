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
import uk.gov.hmrc.test.ui.support.PageSupport.clickElement
import uk.gov.hmrc.test.ui.support.{BackLinkSupport, SubmissionButtonSupport}

object CheckYourAnswersPage extends CommonPage with SubmissionButtonSupport with BackLinkSupport {
  override val pageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/certificateCheckYourAnswers"

  override val pageTitle: String =
    "Check your answers – Submit a certificate - Senior Accounting Officer notification and certificate - GOV.UK"

  val certificateSaoFullNameValue: By           = testId("sao-full-name-value")
  val certificateSaoEmailValue: By              = testId("sao-email-value")
  val certificateWhoIsSubmittingValue: By       = testId("who-is-submitting-value")
  val certificateDeclarationSaoValue: By        = testId("declaration-sao-value")
  val certificateAdditionalInformationValue: By = testId("additional-information-value")

  val certificateSaoNameChangeLink: By            = testId("change-sao-full-name-link")
  val certificateSaoEmailChangeLink: By           = testId("change-sao-email-link")
  val certificateSubmitterChangeLink: By          = testId("change-who-is-submitting-link")
  val certificateDeclarationSaoNameChangeLink: By = testId("change-declaration-sao-link")
  val certificateAdditionalInformationLink: By    = testId("change-additional-information-link")

  def clickCertificateSaoNameChangeLink(): Unit            = clickElement(certificateSaoNameChangeLink)
  def clickCertificateSaoEmailChangeLink(): Unit           = clickElement(certificateSaoEmailChangeLink)
  def clickCertificateSubmitterChangeLink(): Unit          = clickElement(certificateSubmitterChangeLink)
  def clickCertificateDeclarationSaoNameChangeLink(): Unit = clickElement(certificateDeclarationSaoNameChangeLink)
  def clickCertificateAdditionalInformationLink(): Unit    = clickElement(certificateAdditionalInformationLink)
}
