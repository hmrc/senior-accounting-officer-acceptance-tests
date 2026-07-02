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
import org.openqa.selenium.support.ui.ExpectedConditions
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.CommonPage
import uk.gov.hmrc.test.ui.support.{PageSupport, SubmissionButtonSupport}

object UploadTableErrorPage extends CommonPage with SubmissionButtonSupport {
  override val pageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/notification/upload/table/error"

  override val pageTitle: String =
    "There is a problem with your submission template file - Senior Accounting Officer notification and certificate - GOV.UK"

  val pageContentElement: By = By.cssSelector(".govuk-grid-column-two-thirds")
  val paragraph: By          = By.cssSelector(".govuk-body")

  def assertParagraphWithErrorCountMatches(errorCount: Int): Unit = {
    val expectedText = s"Your file has $errorCount errors. At least one company has missing information or is not in " +
      s"the correct format."
    getParagraph(paragraphIndex = 1) mustBe expectedText
  }

  private def getParagraph(paragraphIndex: Int): String = {
    fluentWait
      .until(ExpectedConditions.visibilityOfElementLocated(pageContentElement))
      .findElements(paragraph)
      .get(paragraphIndex)
      .getText
  }
}
