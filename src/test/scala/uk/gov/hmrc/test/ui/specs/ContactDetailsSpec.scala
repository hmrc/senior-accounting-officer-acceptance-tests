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
import uk.gov.hmrc.test.ui.support.PageSupport.authenticateAndCompleteBusinessMatching

class ContactDetailsSpec extends BaseSpec {

  Feature("Contact Details page") {

    Scenario(
      "Completion of First Contact Details",
      RegistrationTests,
      ZapTests
    ) {
      val registrationPage   = authenticateAndCompleteBusinessMatching()
      val contactDetailsPage = new ContactDetailsPage

      And("They Enter the full name details and click on continue button")
      contactDetailsPage.enterFullName(contactDetailsPage.firstContactFullName)

      And("They enter the email address details and click on continue button")
      contactDetailsPage.enterEmailAddress(contactDetailsPage.firstContactEmailAddress)

      And("They select 'Yes' for the question 'Have you added all the contacts you need' and click on continue button")
      contactDetailsPage.selectYes()

      Then("They can verify all the answers for the first contact details")
      contactDetailsPage.verifyFirstContactDetailsInCheckYourAnswersPage()

      When("They click the 'Save and Continue' button")
      contactDetailsPage.clickContinueButtonElement()

      Then("The 'Enter your contact details' status must be marked as 'Completed' in Register your company page")
      registrationPage.verifyContactDetailsStatusCompleted()
    }

    Scenario(
      "Completion of Second Contact Details",
      RegistrationTests,
      ZapTests
    ) {
      val registrationPage   = authenticateAndCompleteBusinessMatching()
      val contactDetailsPage = new ContactDetailsPage

      And("They Enter the first full name details and click on continue button")
      contactDetailsPage.enterFullName(contactDetailsPage.firstContactFullName)

      And("They enter the first email address details and click on continue button")
      contactDetailsPage.enterEmailAddress(contactDetailsPage.firstContactEmailAddress)

      And("They select 'No' for the question 'Have you added all the contacts you need' and click on 'Continue' button")
      contactDetailsPage.selectNo()

      And("They Enter the Second full name details and click on continue button")
      contactDetailsPage.enterFullName(contactDetailsPage.secondContactFullName)

      And("They enter the Second email address details and click on continue button")
      contactDetailsPage.enterEmailAddress(contactDetailsPage.secondContactEmailAddress)

      Then("They can verify all the answers for the second contact details")
      contactDetailsPage.verifySecondContactDetailsInCheckYourAnswersPage()

      When("They click the 'Save and Continue' button")
      contactDetailsPage.clickContinueButtonElement()

      And("The 'Enter your contact details' status must be marked as 'Completed' in Register your company page")
      registrationPage.verifyContactDetailsStatusCompleted()
    }

    Scenario(
      "Contact Details error message for all fields",
      RegistrationTests,
      ZapTests
    ) {
      authenticateAndCompleteBusinessMatching()
      val contactDetailsPage = new ContactDetailsPage

      Then("They must see the error message in contact details")
      contactDetailsPage.verifyFullNameErrorSummaryOnContactDetailsPage()
      contactDetailsPage.enterFullName(contactDetailsPage.firstContactFullName)

      contactDetailsPage.verifyEmailAddressErrorSummaryOnContactDetailsPage()
      contactDetailsPage.enterEmailAddress(contactDetailsPage.firstContactEmailAddress)
    }

    Scenario(
      "Change contact details from Check Your Answers page",
      RegistrationTests,
      ZapTests
    ) {
      authenticateAndCompleteBusinessMatching()
      val contactDetailsPage = new ContactDetailsPage

      And("They Edit Name and Email fields for both contact details")
      // Enter First Contact Details
      contactDetailsPage.enterFullName(contactDetailsPage.firstContactFullName)
      contactDetailsPage.enterEmailAddress(contactDetailsPage.firstContactEmailAddress)

      contactDetailsPage.selectNo()

      // Enter Second Contact Details
      contactDetailsPage.enterFullName(contactDetailsPage.secondContactFullName)
      contactDetailsPage.enterEmailAddress(contactDetailsPage.secondContactEmailAddress)

      contactDetailsPage.changeContactDetail(
        contactDetailsPage.firstContactDetailsFullNameLocator,
        contactDetailsPage.changeLinkForFirstContactFullNameLocator,
        contactDetailsPage.randomFirstContactFullName,
        contactDetailsPage.enterFullName
      )

      contactDetailsPage.changeContactDetail(
        contactDetailsPage.firstContactDetailsEmailAddressLocator,
        contactDetailsPage.changeLinkForFirstContactEmailAddressLocator,
        contactDetailsPage.randomFirstContactEmailAddress,
        contactDetailsPage.enterEmailAddress
      )

      contactDetailsPage.changeContactDetail(
        contactDetailsPage.secondContactDetailsFullNameLocator,
        contactDetailsPage.changeLinkForSecondContactFullName,
        contactDetailsPage.randomSecondContactFullName,
        contactDetailsPage.enterFullName
      )

      contactDetailsPage.changeContactDetail(
        contactDetailsPage.secondContactDetailsEmailAddressLocator,
        contactDetailsPage.changeLinkForSecondContactEmailAddress,
        contactDetailsPage.randomSecondContactEmailAddress,
        contactDetailsPage.enterEmailAddress
      )

      Then("They must verify all updated contact details")
      contactDetailsPage.verifyChangedContactDetails(
        contactDetailsPage.firstContactDetailsFullNameLocator,
        contactDetailsPage.firstContactFullName
      )

      contactDetailsPage.verifyChangedContactDetails(
        contactDetailsPage.firstContactDetailsEmailAddressLocator,
        contactDetailsPage.firstContactEmailAddress
      )

      contactDetailsPage.verifyChangedContactDetails(
        contactDetailsPage.secondContactDetailsFullNameLocator,
        contactDetailsPage.secondContactFullName
      )

      contactDetailsPage.verifyChangedContactDetails(
        contactDetailsPage.secondContactDetailsEmailAddressLocator,
        contactDetailsPage.secondContactEmailAddress
      )
    }
  }
}
