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

object SubmitNotificationStartPage extends BasePage {
  override val pageUrl: String   =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/notification/start"
  override val pageTitle: String =
    "Submit a notification - Senior Accounting Officer notification and certificate - GOV.UK"

  val uploadTemplateLink: By     = actionListItem(1)
  val submitNotificationLink: By = actionListItem(2)

//  private val sectionLocators: Map[PageSection, By] = Map(
//    NotificationPageSection.UploadTemplate     -> By.cssSelector("#upload-template-details-status"),
//    NotificationPageSection.SubmitNotification -> By.cssSelector("#submit-notification-details-status")
//  )

  def actionListItem(index: Int): By =
    By.cssSelector(s".govuk-task-list li:nth-child($index) a")
}
