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

package uk.gov.hmrc.test.ui.pages

import org.openqa.selenium.By
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.support.PageSupport.assertLinkHasText

object NotEnrolledPage extends CommonPage {
  override val pageUrl: String = s"${TestConfiguration.url("senior-accounting-officer-hub-frontend")}/not-enrolled"

  override val pageTitle =
    "You do not have access to this service - Senior Accounting Officer notification and certificate - GOV.UK"

  val registerLink: By =
    By.cssSelector(".govuk-link[href='http://localhost:10057/senior-accounting-officer/registration']")

  def assertLinkHasTextOnPage(link: By, expectedText: String): Unit = {
    assertLinkHasText(link = link, expectedText = expectedText)
  }
}
