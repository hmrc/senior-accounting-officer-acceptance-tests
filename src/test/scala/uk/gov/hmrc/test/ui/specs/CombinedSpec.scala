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

package uk.gov.hmrc.test.ui.specs

import org.scalatest.*
import uk.gov.hmrc.test.ui.adt.AffinityGroup.Organisation
import uk.gov.hmrc.test.ui.pages.submission.*
import uk.gov.hmrc.test.ui.pages.submission.SubmissionFeatureTogglePage.{combinedJourneyCheckbox, setToggle}
import uk.gov.hmrc.test.ui.pages.{AccountHomePage, AuthorityWizardPage}
import uk.gov.hmrc.test.ui.specs.tags.*
import uk.gov.hmrc.test.ui.support.InternalAuthorisationSupport.setupInternalAuthorisation
import uk.gov.hmrc.test.ui.support.PageSupport.*
import uk.gov.hmrc.test.ui.support.{PageSupport, TestData}

class CombinedSpec extends BaseSpec {
  override def beforeEach(): Unit = {
    super.beforeEach()
    setToggle(isEnabled = true, combinedJourneyCheckbox)
    AuthorityWizardPage.withAffinityGroup(Organisation).withDsaoEnrolment(TestData.subscriptionId).redirectToHomePage()
    setupInternalAuthorisation
  }

  override def afterEach(): Unit = {
    setToggle(isEnabled = false, combinedJourneyCheckbox)
    super.afterEach()
  }

  Feature("Submit Combined Journey") {

    Scenario(
      "Combined journey submission",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the combined journey start page at the start of a new submission")
      navigateToCombinedJourneyStartPage()

    }
  }

  private def navigateToCombinedJourneyStartPage(): Unit = {
    assertOnPage(AccountHomePage)
    AccountHomePage.clickMakeSubmissionLink()
    assertOnPage(SubmissionTypePage)
    SubmissionTypePage.clickCombinedRadioButton()
    SubmissionTypePage.clickSubmissionButton()
  }
}
