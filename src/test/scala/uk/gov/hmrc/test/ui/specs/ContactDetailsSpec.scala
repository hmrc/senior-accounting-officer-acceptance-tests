/*
 * Copyright 2025 HM Revenue & Customs
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
      "Completion of First Contact Details",
      RegistrationTests,
      ZapTests
    ) {
      Given("An authenticated organisation user who has just completed Business matching")
      AuthLoginPage.enableGrsStubAndServiceHomePage(Organisation)

      And("They click on the 'Enter your contact details' link and Stub response button")
      RegisterYourCompanyPage.clickEnterYourCompanyDetailsLink()
      GrsStubPage.clickStubResponseButton()

      And("They must see the 'Contact Details' page")
      RegisterYourCompanyPage.clickEnterYourContactDetailsLink()

      When("They click the 'Continue' button on the Contact Details information page")
      ContactDetailsPage.clickContinueButtonElement()

      And("They must see the 'First Contact Details' page")
      And("They Enter full name")
      And("They click the 'Continue' button")
      ContactDetailsPage.enterFullName()

      And("They enter role")
      And("They click the 'Continue' button")
      ContactDetailsPage.enterRoll()

      And("They must see the 'Contact Email Address' page")
      And("They enter email address")
      And("They click the 'Continue' button")
      ContactDetailsPage.enterEmailAddress()

      And("They enter phone number")
      And("They click the 'Continue' button")
      ContactDetailsPage.enterPhoneNumber()

      And("They select 'Yes' for the question Have you added all the contacts you need")
      And("They click the 'Continue' button")
      ContactDetailsPage.selectYes()

      And("They must see the 'Check Your Answers' page")
      And("They must see 'Change' links next to each entered details")
      And("They update previously entered details")
      And("They must verify all the answers for the first contact details")
      ContactDetailsPage.verifyFirstContactDetailsInCheckYourAnswersPage()

      And("They click the 'Save and Continue' button")
      ContactDetailsPage.clickContinueButtonElement()

      Then("They must be redirected back to the 'Register Your Company' page")
      And("The 'Enter your contact details' status must be marked as 'Completed'")
      RegisterYourCompanyPage.verifyContactDetailsStatusCompleted()
    }

    Scenario(
      "Completion of Second Contact Details",
      RegistrationTests,
      ZapTests
    ) {
      Given("An authenticated organisation user who has just completed Business matching")
      AuthLoginPage.enableGrsStubAndServiceHomePage(Organisation)

      And("They click on the 'Enter your contact details' link and Stub response button")
      RegisterYourCompanyPage.clickEnterYourCompanyDetailsLink()
      GrsStubPage.clickStubResponseButton()

      And("They must see the 'Contact Details' page")
      RegisterYourCompanyPage.clickEnterYourContactDetailsLink()

      When("They click the 'Continue' button on the Contact Details information page")
      ContactDetailsPage.clickContinueButtonElement()

      And("They must see the 'First Contact Details' page")
      And("They Enter full name")
      And("They click the 'Continue' button")
      ContactDetailsPage.enterFullName()

      And("They enter role")
      And("They click the 'Continue' button")
      ContactDetailsPage.enterRoll()

      And("They must see the 'Contact Email Address' page")
      And("They enter email address")
      And("They click the 'Continue' button")
      ContactDetailsPage.enterEmailAddress()

      And("They enter phone number")
      And("They click the 'Continue' button")
      ContactDetailsPage.enterPhoneNumber()

      And("They select 'No' for the question Have you added all the contacts you need")
      And("They click the 'Continue' button")
      ContactDetailsPage.selectNo()

      And("They must see the 'Second Contact Details' page")
      And("They Enter second full name")
      And("They click the 'Continue' button")
      ContactDetailsPage.enterFullName()

      And("They enter second role")
      And("They click the 'Continue' button")
      ContactDetailsPage.enterRoll()

      And("They must see the 'Contact Email Address' page")
      And("They enter second email address")
      And("They click the 'Continue' button")
      ContactDetailsPage.enterEmailAddress()

      And("They enter second phone number")
      And("They click the 'Continue' button")
      ContactDetailsPage.enterPhoneNumber()

      And("They select 'Yes' for the question Have you added all the contacts you need")
      And("They click the 'Continue' button")
      ContactDetailsPage.selectYes()

      And("They must see the 'Check Your Answers' page")
      And("They must see 'Change' links next to each entered details")
      And("They update previously entered details")
      And("They must verify all the answers for the first contact details")
      ContactDetailsPage.verifySecondContactDetailsInCheckYourAnswersPage()

      And("They click the 'Save and Continue' button")
      ContactDetailsPage.clickContinueButtonElement()

      Then("They must be redirected back to the 'Register Your Company' page")
      And("The 'Enter your contact details' status must be marked as 'Completed'")
      RegisterYourCompanyPage.verifyContactDetailsStatusCompleted()
    }
  }
}
