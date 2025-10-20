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
import uk.gov.hmrc.test.ui.pages.ContactDetailsPage.*
import uk.gov.hmrc.test.ui.specs.tags.*
import uk.gov.hmrc.test.ui.utils.AffinityGroup.Organisation

class ContactDetailsSpec extends BaseSpec {

  Feature("Contact Details page") {

    def authenticateAndCompleteBusinessMatching(): Unit = {
      Given("An authenticated organisation user successfully navigates to the Register Your Company page")
      AuthLoginPage.enableGrsStubAndServiceHomePage(Organisation)

      When("They click on the 'Enter your contact details' link and complete Business matching")
      RegisterYourCompanyPage.clickEnterYourCompanyDetailsLink()
      GrsStubPage.clickStubResponseButton()

      And(
        "They click on the 'Enter Contact Details' link on dashboard and click 'Continue' button on the Contact Details information page"
      )
      RegisterYourCompanyPage.clickEnterYourContactDetailsLink()
      ContactDetailsPage.clickContinueButtonElement()
    }

    Scenario(
      "Completion of First Contact Details",
      RegistrationTests,
      ZapTests
    ) {
      authenticateAndCompleteBusinessMatching()

      And("They Enter the full name details and click on continue button")
      ContactDetailsPage.enterFullName(firstContactFullName)

      And("They enter the email address details and click on continue button")
      ContactDetailsPage.enterEmailAddress(firstContactEmailAddress)

      And("They select 'Yes' for the question 'Have you added all the contacts you need' and click on continue button")
      ContactDetailsPage.selectYes()

      Then("They can verify all the answers for the first contact details")
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

      And("They Enter the first full name details and click on continue button")
      ContactDetailsPage.enterFullName(firstContactFullName)

      And("They enter the first email address details and click on continue button")
      ContactDetailsPage.enterEmailAddress(firstContactEmailAddress)

      And("They select 'No' for the question 'Have you added all the contacts you need' and click on 'Continue' button")
      ContactDetailsPage.selectNo()

      And("They Enter the Second full name details and click on continue button")
      ContactDetailsPage.enterFullName(secondContactFullName)

      And("They enter the Second email address details and click on continue button")
      ContactDetailsPage.enterEmailAddress(secondContactEmailAddress)

      Then("They can verify all the answers for the second contact details")
      ContactDetailsPage.verifySecondContactDetailsInCheckYourAnswersPage()

      When("They click the 'Save and Continue' button")
      ContactDetailsPage.clickContinueButtonElement()

      And("The 'Enter your contact details' status must be marked as 'Completed' in Register your company page")
      RegisterYourCompanyPage.verifyContactDetailsStatusCompleted()
    }

    Scenario(
      "Contact Details error message for all fields",
      RegistrationTests,
      ZapTests
    ) {
      authenticateAndCompleteBusinessMatching()

      Then("They must see the error message in contact details")
      ContactDetailsPage.verifyFullNameErrorSummaryOnContactDetailsPage()
      ContactDetailsPage.enterFullName(firstContactFullName)

      ContactDetailsPage.verifyEmailAddressErrorSummaryOnContactDetailsPage()
      ContactDetailsPage.enterEmailAddress(firstContactEmailAddress)
    }

    Scenario(
      "Change second contact details from Check Your Answers page",
      RegistrationTests,
      ZapTests
    ) {
      authenticateAndCompleteBusinessMatching()

      And("They Edit Name and Email fields for both contact details")
      // Enter First Contact Details
      ContactDetailsPage.enterFullName(firstContactFullName)
      ContactDetailsPage.enterEmailAddress(firstContactEmailAddress)

      ContactDetailsPage.selectNo()

      // Enter Second Contact Details
      ContactDetailsPage.enterFullName(secondContactFullName)
      ContactDetailsPage.enterEmailAddress(secondContactEmailAddress)

      ContactDetailsPage.changeContactFullName(
        firstContactDetailsFullName,
        changeLinkForFirstContactFullName,
        randomFirstContactFullName
      )
      ContactDetailsPage.changeContactEmailAddress(
        firstContactDetailsEmailAddress,
        changeLinkForFirstContactEmailAddress,
        randomFirstContactEmailAddress
      )

      ContactDetailsPage.changeContactFullName(
        secondContactDetailsFullName,
        changeLinkForSecondContactFullName,
        randomSecondContactFullName
      )
      ContactDetailsPage.changeContactEmailAddress(
        secondContactDetailsEmailAddress,
        changeLinkForSecondContactEmailAddress,
        randomSecondContactEmailAddress
      )
    }
  }
}
