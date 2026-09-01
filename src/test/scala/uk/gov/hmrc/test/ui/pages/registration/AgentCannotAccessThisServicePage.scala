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

package uk.gov.hmrc.test.ui.pages.registration

import org.openqa.selenium.By
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.CommonPage
import uk.gov.hmrc.test.ui.support.PageSupport.*

object AgentCannotAccessThisServicePage extends CommonPage {
  val pageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-registration-frontend")}/agent-cannot-access-service"

  val pageTitle: String =
    "You cannot access this service - Senior Accounting Officer notification and certificate - GOV.UK"

  val whoCanUseThisServiceLink: By = By.linkText("Find out more about who can use this service")

  def assertLinkHasTextOnPage(link: By, expectedText: String): Unit = {
    assertLinkHasText(link, expectedText)
  }
}
