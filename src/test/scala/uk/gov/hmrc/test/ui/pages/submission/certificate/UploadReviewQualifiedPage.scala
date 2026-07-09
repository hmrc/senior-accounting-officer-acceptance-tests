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
import org.openqa.selenium.support.ui.ExpectedConditions
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.CommonPage
import uk.gov.hmrc.test.ui.support.PageSupport.clickElement
import uk.gov.hmrc.test.ui.support.SubmissionButtonSupport

object UploadReviewQualifiedPage extends CommonPage with SubmissionButtonSupport {

  override val pageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/certificateReviewQualified"

  override val pageTitle: String = {
    "Review the companies with a qualified certificate - Senior Accounting Officer notification and certificate - GOV.UK"
  }

  val pageContentElement: By        = By.cssSelector(".govuk-grid-column-two-thirds")
  val paragraph: By                 = By.cssSelector(".govuk-body")
  val uploadUpdatedTemplateLink: By = By.cssSelector(".govuk-body a")
  val pageHeadingText: String       = "Review the companies with a qualified certificate"

  override def clickSubmissionButton(): Unit = {
    clickElement(submissionButtonLocator)
  }

  def clickUploadUpdatedTemplateLink(): Unit = {
    clickElement(uploadUpdatedTemplateLink)
  }

  def assertFirstParagraphMatches(totalCompanyCount: Int): Unit = {
    val expectedText = s"This list is from the certificate details in your submission template. " +
      s"There were $totalCompanyCount companies your SAO was responsible for in a previous financial year."

    getParagraph(paragraphIndex = 0) mustBe expectedText
  }

  def assertDeclarationParagraphMatches(
      qualifiedCompanyCount: Int,
      expectedSao: String
  ): Unit = {
    val expectedText = s"In accordance with paragraph 2, Schedule 46 of the Finance Act 2009, I $expectedSao, " +
      s"the Senior Accounting Officer, hereby certify that $qualifiedCompanyCount companies did not have appropriate " +
      "tax accounting arrangements."

    getParagraph(paragraphIndex = 2) mustBe expectedText
  }

  private def getParagraph(paragraphIndex: Int): String = {
    fluentWait
      .until(ExpectedConditions.visibilityOfElementLocated(pageContentElement))
      .findElements(paragraph)
      .get(paragraphIndex)
      .getText
  }
}
