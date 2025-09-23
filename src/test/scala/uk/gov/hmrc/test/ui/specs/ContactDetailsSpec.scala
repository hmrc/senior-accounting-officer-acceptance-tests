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

class ContactDetailsSpec extends BaseSpec {

  Feature("Contact Details page") {
    Scenario(
      "Successfully navigated to the Contact Details page with GRS microservice",
      RegistrationTests,
      ZapTests
    ) {
      Given("An authenticated organisation user successfully navigated to the Register Your Company page")
      AuthLoginPage.enableGrsStubAndServiceHomePage(Organisation)

      And("They click on 'Enter your company details' link")
      RegisterYourCompanyPage.clickEnterYourCompanyDetailsLink()

      And("They click on Stub Response button")
      GrsStubPage.clickStubResponseButton()

      When("They are back to the 'Register Your Company' page")
      Then("The heading 'Enter your Contact details' must be link")
      RegisterYourCompanyPage.verifyEnterYourContactDetailsLink()
      And("The status of the Contact Details must be Not Started")
      RegisterYourCompanyPage.verifyContactDetailsStatusNotStarted()
    }
  }
}
