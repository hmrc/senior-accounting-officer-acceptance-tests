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
import org.openqa.selenium.support.ui.{ExpectedConditions, FluentWait}
import org.scalatest.AppendedClues.convertToClueful
import uk.gov.hmrc.test.ui.adt.CertificateTaskListSection.*
import uk.gov.hmrc.test.ui.adt.{CertificateTaskListSection, PageSectionStatus}
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.{BasePage, StaticTitle}
import uk.gov.hmrc.test.ui.support.PageSupport.*
import uk.gov.hmrc.test.ui.support.SubmissionButtonSupport

import java.util.regex.Pattern

object CertificateTaskListPage extends BasePage with StaticTitle with SubmissionButtonSupport {

  case class TaskListSection(
      name: String,
      nameLocator: By,
      statusLocator: By,
      statusHighlightLocator: By
  )
  val host: String = TestConfiguration.url("senior-accounting-officer-submission-frontend")

  val taskListOnePageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/certificate/task-list/1"

  override val pageTitle: String =
    "Submit a certificate - Senior Accounting Officer notification and certificate - GOV.UK"

  val taskListTwoPageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/certificate/task-list/2"

  val taskListThreePageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/certificate/task-list/3"

  val taskListCompletePageUrlRegEx: String =
    s"^${Pattern.quote(host)}/certificate/task-list/complete\\?certificateReference=(CRT[0-9]{10})$$"

  private def statusLocator(id: String): By          = By.cssSelector(s"#$id-status")
  private def statusHighlightLocator(id: String): By = By.cssSelector(s"#$id-status .govuk-tag.govuk-tag--blue")

  val provideSaoDetailsLocator: By                       = testId("provide-sao-details")
  val uploadSubmissionTemplateLocator: By                = testId("upload-submission-template")
  val submitCertificateLocator: By                       = testId("submit-certificate")
  val provideSaoDetailsStatusLocator: By                 = statusLocator("provide-sao-details")
  val uploadSubmissionTemplateStatusLocator: By          = statusLocator("upload-submission-template")
  val submitCertificateStatusLocator: By                 = statusLocator("submit-certificate")
  val provideSaoDetailsStatusHighlightLocator: By        = statusHighlightLocator("provide-sao-details")
  val uploadSubmissionTemplateStatusHighlightLocator: By = statusHighlightLocator("upload-submission-template")
  val submitCertificateStatusHighlightLocator: By        = statusHighlightLocator("submit-certificate")

  private val taskListSections: Map[CertificateTaskListSection, TaskListSection] = Map(
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
    SubmitCertificate ->
      TaskListSection(
        name = "Submit the certificate",
        nameLocator = submitCertificateLocator,
        statusLocator = submitCertificateStatusLocator,
        statusHighlightLocator = submitCertificateStatusHighlightLocator
      )
  )

  def clickTaskListSectionLink(section: CertificateTaskListSection): Unit = {
    clickElement(taskListSections(section).nameLocator)
  }

  def assertTaskListSectionNameIsHyperlink(section: CertificateTaskListSection): Unit = {
    val givenSection = taskListSections(section)
    assertTextIsHyperlink(givenSection.nameLocator, givenSection.name)
  }

  def assertTaskListSectionNameIsNotHyperlink(section: CertificateTaskListSection): Unit = {
    val givenSection = taskListSections(section)
    assertTextIsNotHyperlink(givenSection.nameLocator, givenSection.name)
  }

  def assertStatusNotHighlighted(section: CertificateTaskListSection): Unit = {
    assertElementNotVisible(
      taskListSections(section).statusHighlightLocator
    )
  }

  def assertStatusHighlightedBlue(section: CertificateTaskListSection): Unit = {
    assertAttributeMatches(
      locator = taskListSections(section).statusHighlightLocator,
      attribute = "class",
      expectedText = "govuk-tag govuk-tag--blue"
    )
  }

  def assertCompleteUrl(): Unit = {
    fluentWait.until(ExpectedConditions.urlMatches(taskListCompletePageUrlRegEx))
  }

  def assertTaskListSectionStatus(section: CertificateTaskListSection, expectedStatus: PageSectionStatus): Unit = {
    val statusElement = new FluentWait(driver)
      .until(ExpectedConditions.visibilityOfElementLocated(taskListSections(section).statusLocator))
    statusElement.getText.trim mustBe expectedStatus.toString withClue
      s"Expected a status of '$expectedStatus' for the '$section' section, but found '${statusElement.getText}'"
  }

}
