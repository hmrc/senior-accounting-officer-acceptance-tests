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
import uk.gov.hmrc.test.ui.adt.NotificationPageLink.*
import uk.gov.hmrc.test.ui.adt.{NotificationPageSection, PageLink, PageSection}
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.BasePage

object SubmitNotificationStartPage extends BasePage {
  override val pageUrl: String   = TestConfiguration.url("senior-accounting-officer-submission-notification-frontend")
  override val pageTitle: String =
    "Submit a notification - Senior Accounting Officer notification and certificate - GOV.UK"

  private val pageLinks: Map[PageLink, (By, String)] = Map(
    TemplateGuidanceLink   -> (
      By.cssSelector("p.govuk-body a"),
      s"${pageUrl.stripSuffix("/")}/business-match"
    ),
    UploadTemplateLink     -> (
      By.cssSelector("ul.govuk-task-list > li.govuk-task-list__item--with-link:nth-of-type(1)"),
      s"${pageUrl.stripSuffix("/")}/business-match"
    ),
    SubmitNotificationLink -> (
      By.cssSelector("ul.govuk-task-list > li.govuk-task-list__item--with-link:nth-of-type(2)"),
      s"${pageUrl.stripSuffix("/")}/business-match"
    )
  )

  private val sectionLocators: Map[PageSection, By] = Map(
    NotificationPageSection.UploadTemplate     -> By.cssSelector("#upload-template-details-status"),
    NotificationPageSection.SubmitNotification -> By.cssSelector("#submit-notification-details-status")
  )
}
