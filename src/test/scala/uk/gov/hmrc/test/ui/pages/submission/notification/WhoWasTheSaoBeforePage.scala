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
import uk.gov.hmrc.test.ui.pages.CommonPage
import uk.gov.hmrc.test.ui.support.*
import uk.gov.hmrc.test.ui.support.PageSupport.assertTextOnPage

object WhoWasTheSaoBeforePage
    extends CommonPage
    with SubmissionButtonSupport
    with ErrorMessageSupport
    with BackLinkSupport
    with NameInputSupport {

  override val pageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/notification/who-was-the-sao-before"

  override val pageTitle: String =
    "Senior Accounting Officer full name - Senior Accounting Officer notification and certificate - GOV.UK"

  val pageHeadingElement: By = By.cssSelector(".govuk-label.govuk-label--l")
  val pageHintElement: By    = By.id("value-hint")

  val changePageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/notification/who-was-the-sao-before?saoIndex=1"
  val saoEmailInput: By = By.cssSelector("#value")

  def assertHeadingMatches(text: String): Unit = {
    assertTextOnPage(pageHeadingElement, text)
  }

  def assertHintMatches(text: String): Unit = {
    assertTextOnPage(pageHintElement, text)
  }
}
