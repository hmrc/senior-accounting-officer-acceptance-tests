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
      "Successfully view the Register your company page with Company and Contact Details",
      RegistrationTests,
      ZapTests
    ) {
      Given(s"An authenticated user with the Organisation affinity group has started SAO registration")
      AuthLoginPage.selectRedirectedUrlAndAffinityGroup(Organisation)
      Then("They must be on the 'Register your company' page")
      RegisterYourCompanyPage.verifyRegisterYourCompanyPageURL()
      Then(
        "The page title must be 'Register your company - Senior Accounting Officer notification and certificate - GOV.UK'"
      )
      RegisterYourCompanyPage.verifyRegisterYourCompanyPageTitle()
      Then("The title 'Enter your company details' must be visible")
      RegisterYourCompanyPage.verifyCompanyDetailsField()
      Then("The status of the Company Details must be displayed")
      RegisterYourCompanyPage.verifyCompanyDetailsStatus()
      Then("The title 'Enter your contact details' must be visible")
      RegisterYourCompanyPage.verifyContactDetailsField()
      Then("The status of the Contact Details must be displayed")
      RegisterYourCompanyPage.verifyContactDetailsStatus()
    }
  }
}
