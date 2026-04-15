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

  val firstContactNameKey: By          = getContactLocatorForElement("first", "name-key")
  val firstContactNameValue: By        = getContactLocatorForElement("first", "name-value")
  val firstContactNameChangeLink: By   = getContactLocatorForElement("first", "name-change-link")
  val firstContactEmailKey: By         = getContactLocatorForElement("first", "email-key")
  val firstContactEmailValue: By       = getContactLocatorForElement("first", "email-value")
  val firstContactEmailChangeLink: By  = getContactLocatorForElement("first", "email-change-link")
  val secondContactNameKey: By         = getContactLocatorForElement("second", "name-key")
  val secondContactNameValue: By       = getContactLocatorForElement("second", "name-value")
  val secondContactNameChangeLink: By  = getContactLocatorForElement("second", "name-change-link")
  val secondContactEmailKey: By        = getContactLocatorForElement("second", "email-key")
  val secondContactEmailValue: By      = getContactLocatorForElement("second", "email-value")
  val secondContactEmailChangeLink: By = getContactLocatorForElement("second", "email-change-link")

  def clickFirstContactNameChangeLink(): Unit  = clickElement(firstContactNameChangeLink)
  def clickFirstContactEmailChangeLink(): Unit = clickElement(firstContactEmailChangeLink)

  private def getContactLocatorForElement(contactType: String, elementName: String): By = {
    By.cssSelector(s"""[data-test-id="$contactType-contact-$elementName"]""")
  }
}
