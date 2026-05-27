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

package uk.gov.hmrc.test.ui.pages.registration
import org.openqa.selenium.By
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.CommonPage
import uk.gov.hmrc.test.ui.pages.registration.FirstContactCheckYourAnswersPage.testId
import uk.gov.hmrc.test.ui.support.PageSupport.clickElement
import uk.gov.hmrc.test.ui.support.SubmissionButtonSupport

object SecondContactCheckYourAnswersPage extends CommonPage with SubmissionButtonSupport {
  override val pageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-registration-frontend")}/contact-details/second/check-your-answers"
  override val pageTitle: String =
    "Second contact details - Senior Accounting Officer notification and certificate - GOV.UK"

  private val SECOND_CONTACT    = "second-contact"
  private val NAME_VALUE        = "name-value"
  private val EMAIL_VALUE       = "email-value"
  private val NAME_CHANGE_LINK  = "name-change-link"
  private val EMAIL_KEY         = "email-key"
  private val EMAIL_CHANGE_LINK = "email-change-link"

  val secondContactNameValue: By       = contactLocator(SECOND_CONTACT, NAME_VALUE)
  val secondContactEmailValue: By      = contactLocator(SECOND_CONTACT, EMAIL_VALUE)
  val secondContactNameChangeLink: By  = contactLocator(SECOND_CONTACT, NAME_CHANGE_LINK)
  val secondContactEmailKey: By        = contactLocator(SECOND_CONTACT, EMAIL_KEY)
  val secondContactEmailChangeLink: By = contactLocator(SECOND_CONTACT, EMAIL_CHANGE_LINK)

  def clickSecondContactNameChangeLink(): Unit  = clickElement(secondContactNameChangeLink)
  def clickSecondContactEmailChangeLink(): Unit = clickElement(secondContactEmailChangeLink)

  private def contactLocator(contactType: String, elementName: String): By = {
    testId(s"$contactType-$elementName")
  }
}
