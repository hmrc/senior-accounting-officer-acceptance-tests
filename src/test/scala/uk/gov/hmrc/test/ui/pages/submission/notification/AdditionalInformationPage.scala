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

package uk.gov.hmrc.test.ui.pages.submission.notification

import org.openqa.selenium.By
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.BasePage

object AdditionalInformationPage extends BasePage {
  override val pageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/notification/additional-information"

  override val pageTitle: String =
    "Notification details - Senior Accounting Officer notification and certificate - GOV.UK"

  val changePageUrl: String            =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/notification/change-additional-information"
  val additionalInformationTextBox: By = By.cssSelector(".govuk-textarea")
  val errorTitle: By                   = By.cssSelector(".govuk-error-summary__title")
  val skipButton: By                   = By.id("skip")
}
