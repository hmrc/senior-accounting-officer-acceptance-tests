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
import uk.gov.hmrc.test.ui.adt.NotificationTaskListSection.{
  ProvideSaoDetails,
  SubmitNotification,
  UploadSubmissionTemplate
}
import uk.gov.hmrc.test.ui.adt.{NotificationTaskListSection, PageSectionStatus}
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.CommonPage
import uk.gov.hmrc.test.ui.support.PageSupport
import uk.gov.hmrc.test.ui.support.PageSupport.*


object SubmitNotificationStartPage extends CommonPage {

  case class TaskListSection(
      name: String,
      nameLocator: By,
      statusLocator: By
  )

  override val pageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/notification/start"

  override val pageTitle: String =
    "Submit a notification - Senior Accounting Officer notification and certificate - GOV.UK"

  val provideSaoDetailsLocator: By              = By.cssSelector("""[data-test-id="provide-sao-details"]""")
  val uploadSubmissionTemplateLocator: By       = By.cssSelector("""[data-test-id="upload-submission-template"]""")
  val submitNotificationLocator: By             = By.cssSelector("""[data-test-id="submit-notification"]""")
  val provideSaoDetailsStatusLocator: By        = By.cssSelector("#provide-sao-details-status strong")
  val uploadSubmissionTemplateStatusLocator: By = By.cssSelector("#upload-template-details-status strong")
  val submitNotificationStatusLocator: By       = By.cssSelector("#submit-notification-details-status strong")

  private val taskListSections: Map[NotificationTaskListSection, TaskListSection] = Map(
    ProvideSaoDetails ->
      TaskListSection(
        name = "Provide the SAO’s details",
        nameLocator = provideSaoDetailsLocator,
        statusLocator = provideSaoDetailsStatusLocator
      ),
    UploadSubmissionTemplate ->
      TaskListSection(
        name = "Upload a submission template",
        nameLocator = uploadSubmissionTemplateLocator,
        statusLocator = uploadSubmissionTemplateStatusLocator
      ),
    SubmitNotification ->
      TaskListSection(
        name = "Submit a notification",
        nameLocator = submitNotificationLocator,
        statusLocator = submitNotificationStatusLocator
      )
  )

  def clickTaskListSectionLink(section: NotificationTaskListSection): Unit = {
    clickElement(taskListSections(section).nameLocator)
  }

  def assertTaskListSectionNameIsHyperlink(section: NotificationTaskListSection): Unit = {
    val givenSection = taskListSections(section)
    assertTextIsHyperlink(givenSection.nameLocator, givenSection.name)
  }

  def assertTaskListSectionNameIsNotHyperlink(section: NotificationTaskListSection): Unit = {
    val givenSection = taskListSections(section)
    assertTextIsNotHyperlink(givenSection.nameLocator, givenSection.name)
  }

  def assertStatusHighlightedForSection(section: NotificationTaskListSection): Unit = {
    assertAttributeMatches(
      locator = taskListSections(section).statusLocator,
      attribute = "class",
      expectedText = "govuk-tag govuk-tag--blue"
    )
  }

  def assertTaskListSectionStatus(section: NotificationTaskListSection, expectedStatus: PageSectionStatus): Unit = {
    val statusElement = new FluentWait(driver)
      .until(ExpectedConditions.visibilityOfElementLocated(taskListSections(section).statusLocator))
    statusElement.getText.trim mustBe expectedStatus.toString withClue
      s"Expected a status of '$expectedStatus' for the '$section' section, but found '${statusElement.getText}'"
  }
}
