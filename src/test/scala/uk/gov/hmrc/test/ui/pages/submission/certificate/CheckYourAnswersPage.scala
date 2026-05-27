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
import uk.gov.hmrc.test.ui.support.SubmissionButtonSupport

object CheckYourAnswersPage extends CommonPage with SubmissionButtonSupport {
  override val pageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/certificate/check-your-answers"

  override val pageTitle: String =
    "Submit a certificate - Senior Accounting Officer notification and certificate - GOV.UK"

  val isThisTheSaoKey: By                    = testId("is-this-the-sao-key")
  val isThisTheSaoValue: By                  = testId("is-this-the-sao-value")
  val isThisTheSaoChangeLink: By             = testId("is-this-the-sao-change-link")
  val fullNameChangeLink: By                 = testId("full-name-change-link")
  val fullNameKey: By                        = testId("full-name-key")
  val fullNameValue: By                      = testId("full-name-value")
  val emailAddressChangeLink: By             = testId("email-address-change-link")
  val emailAddressValue: By                  = testId("email-address-value")
  val emailAddressKey: By                    = testId("email-address-key")
  val emailCommunicationChoiceKey: By        = testId("email-communication-key")
  val emailCommunicationChoiceValue: By      = testId("email-communication-value")
  val emailCommunicationChoiceChangeLink: By = testId("email-communication-change-link")

  def clickIsThisTheSaoChangeLink(): Unit             = clickElement(isThisTheSaoChangeLink)
  def clickFullNameChangeLink(): Unit                 = clickElement(fullNameChangeLink)
  def clickEmailAddressChangeLink(): Unit             = clickElement(emailAddressChangeLink)
  def clickEmailCommunicationChoiceChangeLink(): Unit = clickElement(emailCommunicationChoiceChangeLink)
}
