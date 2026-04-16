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
import uk.gov.hmrc.test.ui.pages.CommonPage
import uk.gov.hmrc.test.ui.support.PageSupport.clickElement
import uk.gov.hmrc.test.ui.support.SubmissionButtonSupport

object CheckYourAnswersPage extends CommonPage with SubmissionButtonSupport {
  override val pageUrl: String   = s"${RegistrationPage.pageUrl}/contact-details/check-your-answers"
  override val pageTitle: String =
    "Check your answers - Senior Accounting Officer notification and certificate - GOV.UK"

  private val FIRST_CONTACT     = "first-contact"
  private val SECOND_CONTACT    = "second-contact"
  private val NAME_KEY          = "name-key"
  private val NAME_VALUE        = "name-value"
  private val NAME_CHANGE_LINK  = "name-change-link"
  private val EMAIL_KEY         = "email-key"
  private val EMAIL_VALUE       = "email-value"
  private val EMAIL_CHANGE_LINK = "email-change-link"

  val firstContactNameKey: By          = contactLocator(FIRST_CONTACT, NAME_KEY)
  val firstContactNameValue: By        = contactLocator(FIRST_CONTACT, NAME_VALUE)
  val firstContactNameChangeLink: By   = contactLocator(FIRST_CONTACT, NAME_CHANGE_LINK)
  val firstContactEmailKey: By         = contactLocator(FIRST_CONTACT, EMAIL_KEY)
  val firstContactEmailValue: By       = contactLocator(FIRST_CONTACT, EMAIL_VALUE)
  val firstContactEmailChangeLink: By  = contactLocator(FIRST_CONTACT, EMAIL_CHANGE_LINK)
  val secondContactNameKey: By         = contactLocator(SECOND_CONTACT, NAME_KEY)
  val secondContactNameValue: By       = contactLocator(SECOND_CONTACT, NAME_VALUE)
  val secondContactNameChangeLink: By  = contactLocator(SECOND_CONTACT, NAME_CHANGE_LINK)
  val secondContactEmailKey: By        = contactLocator(SECOND_CONTACT, EMAIL_KEY)
  val secondContactEmailValue: By      = contactLocator(SECOND_CONTACT, EMAIL_VALUE)
  val secondContactEmailChangeLink: By = contactLocator(SECOND_CONTACT, EMAIL_CHANGE_LINK)

  def clickFirstContactNameChangeLink(): Unit   = clickElement(firstContactNameChangeLink)
  def clickFirstContactEmailChangeLink(): Unit  = clickElement(firstContactEmailChangeLink)
  def clickSecondContactNameChangeLink(): Unit  = clickElement(secondContactNameChangeLink)
  def clickSecondContactEmailChangeLink(): Unit = clickElement(secondContactEmailChangeLink)

  private def contactLocator(contactType: String, elementName: String): By = {
    By.cssSelector(s"""[data-test-id="$contactType-$elementName"]""")
  }
}
