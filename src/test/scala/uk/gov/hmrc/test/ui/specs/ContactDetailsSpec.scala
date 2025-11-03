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

import uk.gov.hmrc.test.ui.adt.ContactDetailsPageError.{MissingContactDetails, MissingEmailAddress}
import uk.gov.hmrc.test.ui.pages.*
import uk.gov.hmrc.test.ui.specs.tags.*
import uk.gov.hmrc.test.ui.support.AffinityGroup.Organisation
import uk.gov.hmrc.test.ui.support.PageSupport.assertOnPage

class ContactDetailsSpec extends BaseSpec {

  Feature("Contact Details") {

    Scenario("Complete first contact details", RegistrationTests, ZapTests) {
      val authLoginPage      = new AuthLoginPage
      val grsStubPage        = new GrsStubPage
      val contactDetailsPage = new ContactDetailsPage
      val registrationPage   = new RegistrationPage

      Given("a user successfully adds company details from the registration page")
      authLoginPage.enableGrsStubAndServiceHomePage(Organisation)
      registrationPage.clickEnterYourCompanyDetailsLink()
      grsStubPage.clickStubResponseButton()

      And("the user successfully adds a single contact from the registration page")
      registrationPage.clickEnterYourContactDetailsLink()
      contactDetailsPage.clickContinue()
      contactDetailsPage.enterContactNameAndClickContinue(contactDetailsPage.firstContactFullName)
      contactDetailsPage.enterEmailAddressAndClickContinue(contactDetailsPage.firstContactEmailAddress)
      contactDetailsPage.selectYes()
      contactDetailsPage.verifyFirstContactDetailsInCheckYourAnswersPage()

      When("the user selects to save and continue from the 'Check your answers' page")
      contactDetailsPage.clickContinue()

      Then("the status on the registration page for the 'Enter your contact details' section is set to 'Completed'")
      registrationPage.verifyContactDetailsStatusCompleted()
    }

    Scenario("Complete second contact details", RegistrationTests, ZapTests) {
      val authLoginPage      = new AuthLoginPage
      val grsStubPage        = new GrsStubPage
      val contactDetailsPage = new ContactDetailsPage
      val registrationPage   = new RegistrationPage

      Given("a user successfully adds company details from the registration page")
      authLoginPage.enableGrsStubAndServiceHomePage(Organisation)
      registrationPage.clickEnterYourCompanyDetailsLink()
      grsStubPage.clickStubResponseButton()

      And("the user successfully adds a single contact from the registration page")
      registrationPage.clickEnterYourContactDetailsLink()
      contactDetailsPage.clickContinue()
      contactDetailsPage.enterContactNameAndClickContinue(contactDetailsPage.firstContactFullName)
      contactDetailsPage.enterEmailAddressAndClickContinue(contactDetailsPage.firstContactEmailAddress)

      And("the user successfully adds a second contact when prompted if all contacts needed have been added")
      contactDetailsPage.selectNoRadioAndClickContinue()
      contactDetailsPage.verifyFirstContactDetailsInCheckYourAnswersPage()
      contactDetailsPage.enterContactNameAndClickContinue(contactDetailsPage.secondContactFullName)
      contactDetailsPage.enterEmailAddressAndClickContinue(contactDetailsPage.secondContactEmailAddress)
      contactDetailsPage.verifySecondContactDetailsInCheckYourAnswersPage()

      When("the user selects to save and continue from the 'Check your answers' page")
      contactDetailsPage.clickContinue()

      Then("the status on the registration page for the 'Enter your contact details' section is set to 'Completed'")
      registrationPage.verifyContactDetailsStatusCompleted()
    }

    Scenario("Attempting to add a contact with no name produces the expected error", RegistrationTests, ZapTests) {
      val authLoginPage      = new AuthLoginPage
      val grsStubPage        = new GrsStubPage
      val contactDetailsPage = new ContactDetailsPage
      val registrationPage   = new RegistrationPage

      Given("a user successfully adds company details from the registration page")
      authLoginPage.enableGrsStubAndServiceHomePage(Organisation)
      registrationPage.clickEnterYourCompanyDetailsLink()
      grsStubPage.clickStubResponseButton()

      When("the user selects to add contact details but attempts to continue with no contact name added")
      registrationPage.clickEnterYourContactDetailsLink()
      contactDetailsPage.clickContinue()

      Then("an error page is shown noting the contact name is missing")
      contactDetailsPage.assertErrorMessageMatches(MissingContactDetails)

      And("on the user selecting to again continue without adding a contact name the same error page is shown")
      contactDetailsPage.clickContinue()
      contactDetailsPage.assertErrorMessageMatches(MissingContactDetails)

      And("on the user adding a valid contact name and selecting to continue")
      contactDetailsPage.enterContactNameAndClickContinue(contactDetailsPage.firstContactFullName)

      Then("the user progresses to the 'Enter email address' screen successfully")
      assertOnPage(contactDetailsPage.enterFirstContactEmailAddressPage)
    }

    Scenario(
      "Attempting to add a contact with no email address produces the expected error",
      RegistrationTests,
      ZapTests
    ) {
      val authLoginPage      = new AuthLoginPage
      val grsStubPage        = new GrsStubPage
      val contactDetailsPage = new ContactDetailsPage
      val registrationPage   = new RegistrationPage

      Given("a user successfully adds company details from the registration page")
      authLoginPage.enableGrsStubAndServiceHomePage(Organisation)
      registrationPage.clickEnterYourCompanyDetailsLink()
      grsStubPage.clickStubResponseButton()

      When("the user selects to add contact details but attempts to continue with no email address added")
      registrationPage.clickEnterYourContactDetailsLink()
      contactDetailsPage.clickContinue()
      contactDetailsPage.enterContactNameAndClickContinue(contactDetailsPage.firstContactFullName)
      contactDetailsPage.clickContinue()

      Then("an error page is shown noting the contact email is missing")
      contactDetailsPage.assertErrorMessageMatches(MissingEmailAddress)

      And("on the user selecting to again continue without adding a contact email the same error page is shown")
      contactDetailsPage.clickContinue()
      contactDetailsPage.assertErrorMessageMatches(MissingEmailAddress)

      And("on the user adding a valid contact email and selecting to continue")
      contactDetailsPage.enterEmailAddressAndClickContinue(contactDetailsPage.firstContactEmailAddress)

      Then("the user progresses to the 'Add another contact' screen successfully")
      assertOnPage(contactDetailsPage.addAnotherContactPage)
    }

    Scenario(
      "Change contact details from Check Your Answers page",
      RegistrationTests,
      ZapTests
    ) {
      val authLoginPage      = new AuthLoginPage
      val grsStubPage        = new GrsStubPage
      val contactDetailsPage = new ContactDetailsPage
      val registrationPage   = new RegistrationPage

      Given("a user successfully adds company details from the registration page")
      authLoginPage.enableGrsStubAndServiceHomePage(Organisation)
      registrationPage.clickEnterYourCompanyDetailsLink()
      grsStubPage.clickStubResponseButton()

      And("the user successfully adds a single contact from the registration page")
      registrationPage.clickEnterYourContactDetailsLink()
      contactDetailsPage.clickContinue()
      contactDetailsPage.enterContactNameAndClickContinue(contactDetailsPage.firstContactFullName)
      contactDetailsPage.enterEmailAddressAndClickContinue(contactDetailsPage.firstContactEmailAddress)

      And("the user successfully adds a second contact when prompted if all contacts needed have been added")
      contactDetailsPage.selectNoRadioAndClickContinue()
      contactDetailsPage.verifyFirstContactDetailsInCheckYourAnswersPage()
      contactDetailsPage.enterContactNameAndClickContinue(contactDetailsPage.secondContactFullName)
      contactDetailsPage.enterEmailAddressAndClickContinue(contactDetailsPage.secondContactEmailAddress)
      contactDetailsPage.verifySecondContactDetailsInCheckYourAnswersPage()

      And("the user changes all contact details") // fix with ADT
      contactDetailsPage.changeContactDetail(
        contactDetailsPage.firstContactDetailsFullNameLocator,
        contactDetailsPage.changeLinkForFirstContactFullNameLocator,
        contactDetailsPage.randomFirstContactFullName,
        contactDetailsPage.enterContactNameAndClickContinue
      )

      contactDetailsPage.changeContactDetail(
        contactDetailsPage.firstContactDetailsEmailAddressLocator,
        contactDetailsPage.changeLinkForFirstContactEmailAddressLocator,
        contactDetailsPage.randomFirstContactEmailAddress,
        contactDetailsPage.enterEmailAddressAndClickContinue
      )

      contactDetailsPage.changeContactDetail(
        contactDetailsPage.secondContactDetailsFullNameLocator,
        contactDetailsPage.changeLinkForSecondContactFullName,
        contactDetailsPage.randomSecondContactFullName,
        contactDetailsPage.enterContactNameAndClickContinue
      )

      contactDetailsPage.changeContactDetail(
        contactDetailsPage.secondContactDetailsEmailAddressLocator,
        contactDetailsPage.changeLinkForSecondContactEmailAddress,
        contactDetailsPage.randomSecondContactEmailAddress,
        contactDetailsPage.enterEmailAddressAndClickContinue
      )

      Then("the new contact detail amendments are shown on screen correctly")
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
