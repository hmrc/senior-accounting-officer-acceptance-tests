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
import uk.gov.hmrc.test.ui.pages.submission.SubmissionTemplateGuidancePage
import uk.gov.hmrc.test.ui.pages.{AccountHomePage, AuthorityWizardPage}
import uk.gov.hmrc.test.ui.specs.tags.*
import uk.gov.hmrc.test.ui.support.PageSupport.*
import uk.gov.hmrc.test.ui.support.TestData
import uk.gov.hmrc.test.ui.support.TestData.Companies

class HomepageSpec extends BaseSpec {
  override def beforeEach(): Unit = {
    super.beforeEach()
    AuthorityWizardPage.withAffinityGroup(Organisation).withDsaoEnrolment("123").redirectToHomePage()
  }

  Feature("Account Homepage") {

    Scenario(
      "The 'Account Homepage' shows the correct company details for a registered user",
      SubmissionUITests,
      ZapTests
    ) {
      Given("a registered user lands on the 'Account Homepage'")
      assertOnPage(AccountHomePage)

      Then("a caption containing the 'Company Name' is displayed")
      And("a paragraph containing the 'Registration ID' number is displayed")
      AccountHomePage.assertCompanyDetailsCorrect(Companies("DummyCompany"))
    }

    Scenario(
      "View the submission template guidance from the 'Account Homepage",
      SubmissionUITests,
      ZapTests
    ) {
      Given("a registered user lands on the 'Account Homepage'")
      assertOnPage(AccountHomePage)

      When("the 'Get a Submission Template 'link is clicked")
      AccountHomePage.clickGetSubmissionTemplateLink()

      Then("the user is taken to the 'Submission Template Guidance 'page")
      assertOnPage(SubmissionTemplateGuidancePage)
    }

  }
}
