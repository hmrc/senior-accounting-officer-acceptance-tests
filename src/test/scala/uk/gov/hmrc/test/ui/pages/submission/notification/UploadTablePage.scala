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

import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.{By, WebDriver}
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.CommonPage
import uk.gov.hmrc.test.ui.support.PageSupport.clickElement
import uk.gov.hmrc.test.ui.support.SubmissionButtonSupport

object UploadTablePage extends CommonPage with SubmissionButtonSupport {

  override val pageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/notification/upload/table"

  override val pageTitle: String = {
    "Review the companies in your notification - Senior Accounting Officer notification and certificate - GOV.UK"
  }

  val pageHeadingText: String       = "Review the companies in your notification"
  val pageContentElement: By        = By.cssSelector(".govuk-grid-column-two-thirds")
  val paragraph: By                 = By.cssSelector(".govuk-body")
  val uploadUpdatedTemplateLink: By = By.cssSelector(".govuk-body a")
  val tableLocator: By              = By.cssSelector("table")
  val tableEmptyMessageLocator: By  = By.cssSelector("tbody tr td")
  val tableRowLocator: By           = By.cssSelector("tbody tr")

  private def textInParagraphTwo: String = fluentWait
    .until(ExpectedConditions.visibilityOfElementLocated(pageContentElement))
    .findElements(paragraph)
    .get(1)
    .getText

  def assertCompanyCountAndSaoMatch(companyCount: Int, expectedSao: String): Unit = {
    val expectedText = s"There were $companyCount companies $expectedSao was responsible for during the financial year."
    textInParagraphTwo mustBe expectedText
  }

  def assertCompanyTableEmpty(): Unit = {
    val messageInEmptyTable: String = fluentWait
      .until(ExpectedConditions.visibilityOfElementLocated(tableEmptyMessageLocator))
      .getText

    messageInEmptyTable mustBe "No parsed rows"
  }

  def assertCompanyCountInTableEquals(expectedCount: Int): Unit = {
    val rowCount: Int = fluentWait
      .until(ExpectedConditions.visibilityOfElementLocated(tableLocator))
      .findElements(tableRowLocator)
      .size()

    rowCount mustBe expectedCount
  }

  def clickUploadUpdatedTemplateLink(): Unit = {
    clickElement(uploadUpdatedTemplateLink)
  }
}
