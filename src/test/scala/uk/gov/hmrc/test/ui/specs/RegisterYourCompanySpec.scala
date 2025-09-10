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
import uk.gov.hmrc.test.ui.specs.tags.*
import uk.gov.hmrc.test.ui.utils.AffinityGroupEnum.{Agent, Individual, Organisation}

class RegisterYourCompanySpec extends BaseSpec {

  Feature("Register Your company page") {
    Scenario(
      "Verify Register Your Company Page URL and Page details",
      RegistrationTests,
      ZapTests
    ) {
      Given(s"Given an $Organisation user has initiated SAO registration")
      AuthLoginPage.selectRedirectedURLAndAffinityGroup(Organisation)
      When("The Register your company page URL is verified")
      RegisterYourCompanyPage.verifyRegisterYourCompanyPageURL()
      Then("The Register your company page Title is verified")
      RegisterYourCompanyPage.verifyRegisterYourCompanyPageTitle()
    }

    Scenario(
      "View the Company details field and its status on the registration page",
      RegistrationTests,
      ZapTests
    ) {
      Given(s"Given an $Agent user has initiated SAO registration")
      AuthLoginPage.selectRedirectedURLAndAffinityGroup(Agent)
      When("The Company Details field is verified")
      RegisterYourCompanyPage.verifyCompanyDetailsField()
      Then("The Company Details Status is verified")
      RegisterYourCompanyPage.verifyCompanyDetailsStatus()
    }

    Scenario(
      "View the Contacts details field and its status on the registration page",
      RegistrationTests,
      ZapTests
    ) {
      Given(s"Given an $Individual user has initiated SAO registration")
      AuthLoginPage.selectRedirectedURLAndAffinityGroup(Individual)
      When("The Contact Details field is verified")
      RegisterYourCompanyPage.verifyContactDetailsField()
      Then("The Contact Details Status field is verified")
      RegisterYourCompanyPage.verifyContactDetailsStatus()
    }
  }
}
