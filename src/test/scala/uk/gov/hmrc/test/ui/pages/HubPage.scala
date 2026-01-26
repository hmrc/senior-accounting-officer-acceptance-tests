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

package uk.gov.hmrc.test.ui.pages

import org.openqa.selenium.By
import uk.gov.hmrc.test.ui.conf.TestConfiguration

object HubPage extends BasePage {
  override val pageUrl: String   = TestConfiguration.url("senior-accounting-officer-hub-frontend")
  override val pageTitle: String =
    "Senior Accounting Officer notification and certificate - Senior Accounting Officer notification and certificate - site.govuk"
  val submitNotificationLink: By = By.cssSelector("#section-submit-notification-link a")
  val submitCertificateLink: By  = By.cssSelector("#section-submit-certificate-link a")
}
