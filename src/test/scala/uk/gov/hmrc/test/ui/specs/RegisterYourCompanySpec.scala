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

import uk.gov.hmrc.test.ui.pages.*
import uk.gov.hmrc.test.ui.pages.AuthLoginPage.clickOnBackLink
import uk.gov.hmrc.test.ui.specs.tags.*

class RegisterYourCompanySpec extends BaseSpec {

  Feature("Register Your company page") {

    Scenario(
      "Select Company details and navigate back",
      RegistrationTests
    ) {
      Given("The user enters the localhost URL and selects the redirect URL and affinity group")
      AuthLoginPage.loginAsNonAutomatchedUser("Organisation")
      Then("The user click on the Company details")
      RegisterYourCompanyPage.clickCompanyDetails()
      And("The user navigate back")
      clickOnBackLink()
    }

    Scenario(
      "Select Contact details",
      RegistrationTests
    ) {
      Given("The user enters the localhost URL and selects the redirect URL and affinity group")
      AuthLoginPage.loginAsNonAutomatchedUser("Individual")
      When("The user clicks on the Contact Details link")
      RegisterYourCompanyPage.clickContactDetails()
      Then("The user sees a service problem message")
      RegisterYourCompanyPage.displayedServiceProblemMessage()
      And("The user navigate back")
      clickOnBackLink()
      Then("The user clicks on the Company Details link")
      RegisterYourCompanyPage.clickCompanyDetails()
      Then("The user is navigated back to the Register your company page")
      CompanyDetailsPage.clickStubResponseButton()
      And("The user click on the Contact details link again")
      RegisterYourCompanyPage.clickContactDetails()
    }

    Scenario(
      "Select Review and submit",
      RegistrationTests
    ) {
      Given("The user enters the localhost URL and selects the redirect URL and affinity group")
      AuthLoginPage.loginAsNonAutomatchedUser("Organisation")
      When("The user click on the Check your answers before submitting your registration")
      RegisterYourCompanyPage.clickCheckYourAnswersBeforeSubmittingYourRegistration()
      Then("The user see a service problem message")
      RegisterYourCompanyPage.displayedServiceProblemMessage()
    }
  }
}
