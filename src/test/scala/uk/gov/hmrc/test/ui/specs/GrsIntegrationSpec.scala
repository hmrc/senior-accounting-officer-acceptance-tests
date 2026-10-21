/*
 * Copyright 2023 HM Revenue & Customs
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

import uk.gov.hmrc.test.ui.adt.AffinityGroup.Organisation
import uk.gov.hmrc.test.ui.adt.PageSectionStatus.Completed
import uk.gov.hmrc.test.ui.adt.RegistrationPageSection.CompanyDetails
import uk.gov.hmrc.test.ui.pages.AuthorityWizardPage
import uk.gov.hmrc.test.ui.pages.grs.*
import uk.gov.hmrc.test.ui.pages.registration.GrsHost.GrsMicroservice
import uk.gov.hmrc.test.ui.pages.registration.{FeatureTogglePage, RegistrationPage}
import uk.gov.hmrc.test.ui.specs.tags.*
import uk.gov.hmrc.test.ui.support.PageSupport.*

class GrsIntegrationSpec extends BaseSpec {

  Feature("Generic Registration Service (GRS) integration") {
    Scenario(
      "Integrate successfully with GRS",
      SoloTests,
      ZapTests
    ) {
      Given("an authenticated user accesses the Generic Registration Service")
      LimitedCompanyStubConfigurationPage.setStubbedDependencies()
      FeatureTogglePage.setGrsHost(GrsMicroservice)
      FeatureTogglePage.setReshuffledContactFlow(isEnabled = true)
      FeatureTogglePage.setGrsAllowsRelativeUrl(isEnabled = true)
      AuthorityWizardPage.withAffinityGroup(Organisation).redirectToRegistration()

      Then("the user clicks the enter your nominated company details link")
      RegistrationPage.clickEnterYourNominatedCompanyDetailsLink()
      Then("the user is on the nominated company details guidance page")
      assertOnPage(NominatedCompanyDetailsGuidancePage)
      Then("the user clicks the continue button")
      NominatedCompanyDetailsGuidancePage.clickSubmissionButton()

      When("the user completes a business match successfully")
      CompanyRegistrationNumberPage.assertUrl()
      val grsJourneyId = CompanyRegistrationNumberPage.extractParams
      Then("the user enters a valid crn number and clicks continue")
      CompanyRegistrationNumberPage.enterCrnAndSubmit()
      Then("the user is on the 'is this your business' page")
      assertUrl(IsThisYourBusinessPage.pageUrl(grsJourneyId))
      And("the page title is 'is this your business?'")
      assertPageTitle(IsThisYourBusinessPage.pageTitle)
      Then("the user selects yes and clicks the continue button")
      IsThisYourBusinessPage.selectYesRadioAndSubmit()
      Then("the user is on the UTR page")
      assertUrl(UniqueTaxpayerReferencePage.pageUrl(grsJourneyId))
      And("Your Corporation Tax Unique Taxpayer Reference (UTR)")
      assertPageTitle(UniqueTaxpayerReferencePage.pageTitle)
      Then("the user enters a valid utr and clicks continue")
      UniqueTaxpayerReferencePage.enterUtrAndSubmit()
      Then("the user is on the check your answers page")
      assertUrl(GrsCheckYourAnswersPage.pageUrl(grsJourneyId))
      And("the page title is 'Check your answers'")
      assertPageTitle(GrsCheckYourAnswersPage.pageTitle)
      Then("the user clicks the continue button")
      GrsCheckYourAnswersPage.assertAnswersAndSubmit()

      Then("the company details have a status of 'Completed' on the registration dashboard")
      assertOnPage(RegistrationPage)
      RegistrationPage.assertRegistrationPageSectionStatus(CompanyDetails, Completed)

      And("the 'Enter your company details' link is not present")
      RegistrationPage.assertEnterYourNominatedCompanyDetailsLinkNotFound()
    }
  }
}
