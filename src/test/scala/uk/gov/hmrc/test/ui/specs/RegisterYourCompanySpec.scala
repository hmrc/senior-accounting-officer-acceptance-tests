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

import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.*
import uk.gov.hmrc.test.ui.pages.AuthLoginPage.clickOnBackLink
import uk.gov.hmrc.test.ui.specs.tags.*

class RegisterYourCompanySpec extends BaseSpec {

  Feature("Register Your company page") {

    val envType = if (TestConfiguration.env == "local") "localhost" else "staging"

    Scenario(
      "Verify Register Your Company Page URL and Page Details",
      RegistrationTests,
      SoloTests
    ) {
      Given(s"Accesses the $envType url and selects the redirect URL and affinity group")
      AuthLoginPage.selectRedirectedURLAndAffinityGroup("Organisation")
      When("The page URL is verified")
      RegisterYourCompanyPage.verifyRegisterYourCompanyPageURL()
      Then("The page Title is verified")
      RegisterYourCompanyPage.verifyRegisterYourCompanyPageTitle()
    }

    Scenario(
      "Select Company details and Navigate back",
      RegistrationTests,
      SoloTests
    ) {
      Given(s"Accesses the $envType URL and selects the redirect URL and affinity group")
      AuthLoginPage.selectRedirectedURLAndAffinityGroup("Organisation")
      When("The Company Details link is verified to be enabled")
      RegisterYourCompanyPage.verifyCompanyDetailsLink()
      Then("The Company details link is clicked")
      RegisterYourCompanyPage.clickCompanyDetails()
      And("Navigation returns to the Register your company page")
      clickOnBackLink()
    }

    Scenario(
      "Select Contact details",
      RegistrationTests,
      SoloTests
    ) {
      Given(s"Accesses the $envType URL and selects the redirect URL and affinity group")
      AuthLoginPage.selectRedirectedURLAndAffinityGroup("Individual")
      When("The Contact Details link is verified to be enabled")
      RegisterYourCompanyPage.verifyContactDetailsLink()
      Then("The Contact Details link is clicked")
      RegisterYourCompanyPage.clickContactDetails()
      Then("A service problem message is displayed")
      RegisterYourCompanyPage.displayedServiceProblemMessage()
      And("The back link is clicked")
      clickOnBackLink()
      Then("The Company Details link is clicked")
      RegisterYourCompanyPage.clickCompanyDetails()
      Then("Navigation returns to the Register your company page")
      CompanyDetailsPage.clickStubResponseButton()
      And("The Contact details link is clicked again")
      RegisterYourCompanyPage.clickContactDetails()
    }

    Scenario(
      "Select Review and submit",
      RegistrationTests,
      SoloTests
    ) {
      Given(s"Accesses the $envType URL and selects the redirect URL and affinity group")
      AuthLoginPage.selectRedirectedURLAndAffinityGroup("Organisation")
      When("The Check Your Answers Before Submitting Your Registration link is enabled")
      RegisterYourCompanyPage.verifyCheckYourAnswersBeforeSubmittingYourRegistrationLink()
      Then("The Check your answers before submitting your registration link is clicked")
      RegisterYourCompanyPage.clickCheckYourAnswersBeforeSubmittingYourRegistration()
      Then("A service problem message is displayed")
      RegisterYourCompanyPage.displayedServiceProblemMessage()
    }
  }
}
