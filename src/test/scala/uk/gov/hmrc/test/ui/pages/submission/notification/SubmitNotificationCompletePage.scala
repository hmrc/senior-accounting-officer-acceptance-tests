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
import org.openqa.selenium.support.ui.{ExpectedConditions, FluentWait}
import org.scalatest.AppendedClues.convertToClueful
import uk.gov.hmrc.test.ui.adt.NotificationTaskListSection.*
import uk.gov.hmrc.test.ui.adt.{NotificationTaskListSection, PageSectionStatus}
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.CommonPage
import uk.gov.hmrc.test.ui.support.PageSupport.*
import uk.gov.hmrc.test.ui.support.SubmissionButtonSupport

object SubmitNotificationCompletePage extends CommonPage with SubmissionButtonSupport {

  case class TaskListSection(
      name: String,
      nameLocator: By,
      statusLocator: By,
      statusHighlightLocator: By
  )

  override val pageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/notification/complete"

  override val pageTitle: String =
    "Submit a notification - Senior Accounting Officer notification and certificate - GOV.UK"

  private def statusLocator(id: String): By          = By.cssSelector(s"#$id-status")
  private def statusHighlightLocator(id: String): By = By.cssSelector(s"#$id-status .govuk-tag.govuk-tag--blue")

  val provideSaoDetailsLocator: By                       = testId("provide-sao-details")
  val uploadSubmissionTemplateLocator: By                = testId("upload-submission-template")
  val submitNotificationLocator: By                      = testId("submit-notification")
  val provideSaoDetailsStatusLocator: By                 = statusLocator("provide-sao-details")
  val uploadSubmissionTemplateStatusLocator: By          = statusLocator("upload-template-details")
  val submitNotificationStatusLocator: By                = statusLocator("submit-notification-details")
  val provideSaoDetailsStatusHighlightLocator: By        = statusHighlightLocator("provide-sao-details")
  val uploadSubmissionTemplateStatusHighlightLocator: By = statusHighlightLocator("upload-template-details")
  val submitNotificationStatusHighlightLocator: By       = statusHighlightLocator("submit-notification-details")

  private val taskListSections: Map[NotificationTaskListSection, TaskListSection] = Map(
    ProvideSaoDetails ->
      TaskListSection(
        name = "Provide the SAO’s details",
        nameLocator = provideSaoDetailsLocator,
        statusLocator = provideSaoDetailsStatusLocator,
        statusHighlightLocator = provideSaoDetailsStatusHighlightLocator
      ),
    UploadSubmissionTemplate ->
      TaskListSection(
        name = "Upload the submission template",
        nameLocator = uploadSubmissionTemplateLocator,
        statusLocator = uploadSubmissionTemplateStatusLocator,
        statusHighlightLocator = uploadSubmissionTemplateStatusHighlightLocator
      ),
    SubmitNotification ->
      TaskListSection(
        name = "Submit the notification",
        nameLocator = submitNotificationLocator,
        statusLocator = submitNotificationStatusLocator,
        statusHighlightLocator = submitNotificationStatusHighlightLocator
      )
  )

  def assertTaskListSectionNameIsNotHyperlink(section: NotificationTaskListSection): Unit = {
    val givenSection = taskListSections(section)
    assertTextIsNotHyperlink(givenSection.nameLocator, givenSection.name)
  }

  def assertStatusNotHighlighted(section: NotificationTaskListSection): Unit = {
    assertElementNotVisible(
      taskListSections(section).statusHighlightLocator
    )
  }

  def assertTaskListSectionStatus(section: NotificationTaskListSection, expectedStatus: PageSectionStatus): Unit = {
    val statusElement = new FluentWait(driver)
      .until(ExpectedConditions.visibilityOfElementLocated(taskListSections(section).statusLocator))
    statusElement.getText.trim mustBe expectedStatus.toString withClue
      s"Expected a status of '$expectedStatus' for the '$section' section, but found '${statusElement.getText}'"
  }
}
