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

class CompanyDetailsSpec extends BaseSpec {

  Feature("Company Details page") {
    Scenario(
      "Successfully navigated to the Company Details page",
      RegistrationTests,
      ZapTests
    ) {
      Given("An authenticated organisation user successfully navigated to the Register Your Company page")
      AuthLoginPage.selectRedirectedUrlAndAffinityGroup(Organisation)
      Then("Click on 'Enter your company details' link")
      RegisterYourCompanyPage.clickEnterYourCompanyDetailsLink()

      And("They view the 'Company Details' page")
      CompanyDetailsPage.verifyCompanyDetailsPageURL()
      And("The 'Stub response' button should be clearly visible")
      CompanyDetailsPage.verifyStubResponseButton()
      And("The 'Stub response' button should be clickable")
      CompanyDetailsPage.elementStubResponseButtonClickable()
      When("They click on the 'Stub response' button")
      CompanyDetailsPage.clickStubResponseButton()
      Then("They should be redirected to the 'Register Your Company' page")
      RegisterYourCompanyPage.verifyRegisterYourCompanyPageURL()
      And("The status of the Company Details must be displayed")
      RegisterYourCompanyPage.verifyCompanyDetailsStatusCompleted()
    }
  }
}
