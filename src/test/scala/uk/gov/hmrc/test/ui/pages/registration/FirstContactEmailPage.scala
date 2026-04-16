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
import uk.gov.hmrc.test.ui.support.SubmissionButtonSupport

object FirstContactEmailPage extends CommonPage with SubmissionButtonSupport {
  override val pageUrl: String   = s"${RegistrationPage.pageUrl}/contact-details/first/email"
  override val pageTitle: String =
    "First contact details - Senior Accounting Officer notification and certificate - GOV.UK"

  val changePageUrl: String = s"${RegistrationPage.pageUrl}/contact-details/first/change-email"

  val emailInput: By = By.cssSelector("#value")
  val errorTitle: By = By.cssSelector(".govuk-error-summary__title")
}
