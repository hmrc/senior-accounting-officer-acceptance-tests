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

object SubmitCertificateStartPage extends CommonPage with SubmissionButtonSupport {
  override val pageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/certificate/start"

  override val pageTitle: String =
    "certificateTaskList - Senior Accounting Officer notification and certificate - GOV.UK"

  // TODO better selectors
  private def task1Link =
    By.cssSelector("""a[href="/senior-accounting-officer/submission/certificate/submit-certificate-sao-full-name"]""")
  private def task2Link = By.cssSelector("""a[href=/senior-accounting-officer/submission/certificateUploadForm"]""")
  private def task3Link =
    By.cssSelector("""a[href="/senior-accounting-officer/submission/certificateAdditionalInformation"]""")

  def clickTask1(): Unit = clickElement(task1Link)
  def clickTask2(): Unit = clickElement(task2Link)
  def clickTask3(): Unit = clickElement(task3Link)

}
