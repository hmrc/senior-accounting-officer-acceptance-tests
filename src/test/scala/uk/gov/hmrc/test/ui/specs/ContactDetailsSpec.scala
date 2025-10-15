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

    def authenticateAndCompleteBusinessMatching(): Unit = {
      Given("An authenticated organisation user successfully navigates to the Register Your Company page")
      AuthLoginPage.enableGrsStubAndServiceHomePage(Organisation)

      And("They click on the 'Enter your contact details' link and complete Business matching")
      RegisterYourCompanyPage.clickEnterYourCompanyDetailsLink()
      GrsStubPage.clickStubResponseButton()
    }

    Scenario(
      "Completion of First Contact Details",
      RegistrationTests,
      ZapTests
    ) {
      authenticateAndCompleteBusinessMatching()

      When(
        "They click on the 'Enter Contact Details' link on dashboard and click 'Continue' button on the Contact Details information page"
      )
      RegisterYourCompanyPage.clickEnterYourContactDetailsLink()
      ContactDetailsPage.clickContinueButtonElement()

      Then("They must see the 'Enter full name' Title")
      ContactDetailsPage.waitForContactDetailsFieldTitleToBeVisible()

      When("They Enter the full name details and click on continue button")
      ContactDetailsPage.enterFullName()
      ContactDetailsPage.clickContinueButtonElement()

      And("They enter the role details and click on continue button")
      ContactDetailsPage.enterRole()
      ContactDetailsPage.clickContinueButtonElement()

      Then("They must see the 'Enter email address' Title")
      ContactDetailsPage.waitForContactDetailsFieldTitleToBeVisible()

      When("They enter the email address details and click on continue button")
      ContactDetailsPage.enterEmailAddress()
      ContactDetailsPage.clickContinueButtonElement()

      And("They enter the phone number details and click on continue button")
      ContactDetailsPage.enterPhoneNumber()
      ContactDetailsPage.clickContinueButtonElement()

      And("They select 'Yes' for the question 'Have you added all the contacts you need' and click on continue button")
      ContactDetailsPage.selectYes()
      ContactDetailsPage.clickContinueButtonElement()

      Then("They must see the 'Check Your Answers' Title")
      ContactDetailsPage.waitForCheckYourAnswersTitleToBeVisible()

      And("They can verify all the answers for the first contact details")
      ContactDetailsPage.verifyFirstContactDetailsInCheckYourAnswersPage()

      When("They click the 'Save and Continue' button")
      ContactDetailsPage.clickContinueButtonElement()

      Then("The 'Enter your contact details' status must be marked as 'Completed' in Register your company page")
      RegisterYourCompanyPage.verifyContactDetailsStatusCompleted()
    }

    Scenario(
      "Completion of Second Contact Details",
      RegistrationTests,
      ZapTests
    ) {
      authenticateAndCompleteBusinessMatching()

      When(
        "They click on the 'Enter Contact Details' link on Register your company page and click 'Continue' button on the Contact Details information page"
      )
      RegisterYourCompanyPage.clickEnterYourContactDetailsLink()
      ContactDetailsPage.clickContinueButtonElement()

      Then("They must see the 'Enter full name' Title")
      ContactDetailsPage.waitForContactDetailsFieldTitleToBeVisible()

      When("They Enter the first full name details and click on continue button")
      ContactDetailsPage.enterFullName()
      ContactDetailsPage.clickContinueButtonElement()

      And("They enter the first role details and click on continue button")
      ContactDetailsPage.enterRole()
      ContactDetailsPage.clickContinueButtonElement()

      Then("They must see the 'Enter email address' Title")
      ContactDetailsPage.waitForContactDetailsFieldTitleToBeVisible()

      When("They enter the first email address details and click on continue button")
      ContactDetailsPage.enterEmailAddress()
      ContactDetailsPage.clickContinueButtonElement()

      And("They enter the first phone number details and click on continue button")
      ContactDetailsPage.enterPhoneNumber()
      ContactDetailsPage.clickContinueButtonElement()

      And("They select 'No' for the question 'Have you added all the contacts you need' and click on 'Continue' button")
      ContactDetailsPage.selectNo()
      ContactDetailsPage.clickContinueButtonElement()

      Then("They must see the 'Enter full name' Title")
      ContactDetailsPage.waitForContactDetailsFieldTitleToBeVisible()

      When("They Enter the Second full name details and click on continue button")
      ContactDetailsPage.enterFullName()
      ContactDetailsPage.clickContinueButtonElement()

      And("They enter the Second role details and click on continue button")
      ContactDetailsPage.enterRole()
      ContactDetailsPage.clickContinueButtonElement()

      Then("They must see the 'Enter email address' Title")
      ContactDetailsPage.waitForContactDetailsFieldTitleToBeVisible()

      When("They enter the Second email address details and click on continue button")
      ContactDetailsPage.enterEmailAddress()
      ContactDetailsPage.clickContinueButtonElement()

      And("They enter the Second phone number details and click on continue button")
      ContactDetailsPage.enterPhoneNumber()
      ContactDetailsPage.clickContinueButtonElement()

      And("They select 'Yes' for the question 'Have you added all the contacts you need' and click on continue button")
      ContactDetailsPage.selectYes()
      ContactDetailsPage.clickContinueButtonElement()

      Then("They must see the 'Check Your Answers' Title")
      ContactDetailsPage.waitForCheckYourAnswersTitleToBeVisible()

      And("They can verify all the answers for the second contact details")
      ContactDetailsPage.verifySecondContactDetailsInCheckYourAnswersPage()

      When("They click the 'Save and Continue' button")
      ContactDetailsPage.clickContinueButtonElement()

      Then("The 'Enter your contact details' status must be marked as 'Completed' in Register your company page")
      RegisterYourCompanyPage.verifyContactDetailsStatusCompleted()
    }
  }
}
