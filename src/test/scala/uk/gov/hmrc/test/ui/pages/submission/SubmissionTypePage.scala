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

package uk.gov.hmrc.test.ui.pages.submission

import org.openqa.selenium.By
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.CommonPage
import uk.gov.hmrc.test.ui.support.PageSupport.clickRadioButton
import uk.gov.hmrc.test.ui.support.{ErrorMessageSupport, SubmissionButtonSupport}

object SubmissionTypePage extends CommonPage with SubmissionButtonSupport with ErrorMessageSupport {
  override val pageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/submission-type"

  override val pageTitle: String =
    "What would you like to submit? - Senior Accounting Officer notification and certificate - GOV.UK"

  val notification: By = By.cssSelector("""input[value="notification"]""")
  val aCertificate: By  = By.cssSelector("""input[value="certificate"]""")

  def clickNotificationRadioButton(): Unit = clickRadioButton(aNotification)
  def clickCertificateRadioButton(): Unit  = clickRadioButton(aCertificate)
}
