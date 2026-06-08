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
import uk.gov.hmrc.test.ui.pages.CommonPage
import uk.gov.hmrc.test.ui.support.PageSupport.*
import uk.gov.hmrc.test.ui.support.SubmissionButtonSupport

object CertificateTaskListPage extends CommonPage with SubmissionButtonSupport {

  case class TaskListSection(
      name: String,
      nameLocator: By,
      statusLocator: By,
      statusHighlightLocator: By
  )

  override val pageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/certificate/task-list/1"

  override val pageTitle: String =
    "Submit a certificate - Senior Accounting Officer notification and certificate - GOV.UK"

  val changeTask2PageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/certificate/task-list/2"

  val changeTask3PageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/certificate/task-list/3"

  val changeTaskCompletePageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/certificate/task-list/complete"

  // TODO better selectors
  private def task1Link =
    By.cssSelector("""a[href="/senior-accounting-officer/submission/certificate/submit-certificate-sao-full-name"]""")
  private def task2Link = By.cssSelector("""a[href="/senior-accounting-officer/submission/certificateUploadForm"]""")
  private def task3Link =
    By.cssSelector("""a[href="/senior-accounting-officer/submission/certificateAdditionalInformation"]""")

  def clickTask1(): Unit = clickElement(task1Link)
  def clickTask2(): Unit = clickElement(task2Link)
  def clickTask3(): Unit = clickElement(task3Link)

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

  def assertTaskListSectionStatus(section: CertificateTaskListSection, expectedStatus: PageSectionStatus): Unit = {
    val statusElement = new FluentWait(driver)
      .until(ExpectedConditions.visibilityOfElementLocated(taskListSections(section).statusLocator))
    statusElement.getText.trim mustBe expectedStatus.toString withClue
      s"Expected a status of '$expectedStatus' for the '$section' section, but found '${statusElement.getText}'"
  }
}
