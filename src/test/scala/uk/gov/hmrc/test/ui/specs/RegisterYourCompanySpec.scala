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
import uk.gov.hmrc.test.ui.utils.AffinityGroup.Organisation

class RegisterYourCompanySpec extends BaseSpec {

  Feature("Register Your company page") {
    Scenario(
      "Successfully navigated to the Register your company page",
      RegistrationTests,
      ZapTests
    ) {
      Given("An authenticated organisation user successfully navigated to the Register Your Company page")
      AuthLoginPage.selectRedirectedUrlAndAffinityGroup(Organisation)
      RegisterYourCompanyPage.verifyRegisterYourCompanyPageURL()
      When("They view the 'Register your company' page")
      Then(
        "The page title must be 'Register your company - Senior Accounting Officer notification and certificate - GOV.UK'"
      )
      RegisterYourCompanyPage.verifyRegisterYourCompanyPageTitle()
      And("The title 'Enter your company details' must be visible")
      RegisterYourCompanyPage.verifyCompanyDetailsField()
    }

    Scenario(
      "Successfully view Company and Contact Details in the Register your company page",
      RegistrationTests,
      ZapTests
    ) {
      Given("An authenticated organisation user successfully navigated to the Register Your Company page")
      AuthLoginPage.selectRedirectedUrlAndAffinityGroup(Organisation)
      RegisterYourCompanyPage.verifyRegisterYourCompanyPageURL()
      And("They view the 'Register your company' page")
      Then("The title 'Enter your company details' must be link")
      RegisterYourCompanyPage.verifyEnterYourCompanyDetailsLink()
      And("The status of the Company Details must be Not Started")
      RegisterYourCompanyPage.verifyCompanyDetailsStatusNotStarted()
      And("The title 'Enter your contact details' is not a link")
      RegisterYourCompanyPage.verifyContactDetailsField()
      And("The status of the Contact Details must be Cannot start yet")
      RegisterYourCompanyPage.verifyContactDetailsStatusCannotStartYet()
      And("The Submit button does not exist")
      RegisterYourCompanyPage.verifySubmitButtonDoestNotExist()
    }

    Scenario(
      "Can navigate to the Contact Details page after GRS",
      RegistrationTests,
      ZapTests
    ) {
      Given("An authenticated organisation user who has just completed Business matching")
      AuthLoginPage.enableGrsStubAndServiceHomePage(Organisation)

      When("They click on 'Enter your company details' link and Stub Response button")
      RegisterYourCompanyPage.clickEnterYourCompanyDetailsLink()
      GrsStubPage.clickStubResponseButton()

      Then("The heading 'Enter your Contact details' must be link")
      RegisterYourCompanyPage.verifyEnterYourContactDetailsLink()

      And("The status of the Contact Details must be Not Started")
      RegisterYourCompanyPage.verifyContactDetailsStatusNotStarted()
    }
  }
}
