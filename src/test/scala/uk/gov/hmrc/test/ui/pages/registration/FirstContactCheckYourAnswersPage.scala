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
import uk.gov.hmrc.test.ui.support.PageSupport.clickElement
import uk.gov.hmrc.test.ui.support.SubmissionButtonSupport

object FirstContactCheckYourAnswersPage extends CommonPage with SubmissionButtonSupport {
  override val pageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-registration-frontend")}/contact-details/first/check-your-answers"
  override val pageTitle: String =
    "First contact details - Senior Accounting Officer notification and certificate - GOV.UK"

  private val FIRST_CONTACT     = "first-contact"
  private val NAME_VALUE        = "name-value"
  private val EMAIL_VALUE       = "email-value"
  private val NAME_CHANGE_LINK  = "name-change-link"
  private val EMAIL_KEY         = "email-key"
  private val EMAIL_CHANGE_LINK = "email-change-link"

  val firstContactNameValue: By       = contactLocator(FIRST_CONTACT, NAME_VALUE)
  val firstContactEmailValue: By      = contactLocator(FIRST_CONTACT, EMAIL_VALUE)
  val firstContactNameChangeLink: By  = contactLocator(FIRST_CONTACT, NAME_CHANGE_LINK)
  val firstContactEmailKey: By        = contactLocator(FIRST_CONTACT, EMAIL_KEY)
  val firstContactEmailChangeLink: By = contactLocator(FIRST_CONTACT, EMAIL_CHANGE_LINK)

  def clickFirstContactNameChangeLink(): Unit  = clickElement(firstContactNameChangeLink)
  def clickFirstContactEmailChangeLink(): Unit = clickElement(firstContactEmailChangeLink)

  private def contactLocator(contactType: String, elementName: String): By = {
    By.cssSelector(s"""[data-test-id="$contactType-$elementName"]""")
  }
}
