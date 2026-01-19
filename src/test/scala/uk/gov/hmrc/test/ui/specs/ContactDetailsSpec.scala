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
import uk.gov.hmrc.test.ui.adt.RegistrationPageSection.ContactDetails
import uk.gov.hmrc.test.ui.adt.PageSectionStatus.Completed
import uk.gov.hmrc.test.ui.adt.{FirstContact, SecondContact}
import uk.gov.hmrc.test.ui.pages.*
import uk.gov.hmrc.test.ui.pages.registration.GrsHost.GrsStubOnRegistrationFrontEnd
import uk.gov.hmrc.test.ui.pages.registration.{ContactDetailsPage, FeatureTogglePage, GrsStubPage, RegistrationPage}
import uk.gov.hmrc.test.ui.specs.tags.*
import uk.gov.hmrc.test.ui.support.AffinityGroup.Organisation
import uk.gov.hmrc.test.ui.support.PageSupport.assertOnPage

class ContactDetailsSpec extends BaseSpec {

  Feature("Contact Details") {

    Scenario("Complete first contact details", RegistrationTests, ZapTests) {
      Given("a user successfully adds company details from the registration page")
      FeatureTogglePage.setGrsHost(GrsStubOnRegistrationFrontEnd)
      AuthorityWizardPage.selectValidRedirectUrlAndAffinityGroup(Organisation)
      RegistrationPage.clickEnterYourCompanyDetailsLink()
      GrsStubPage.clickStubResponseButton()

      And("the user successfully adds a single contact from the registration page")
      RegistrationPage.clickEnterYourContactDetailsLink()
      ContactDetailsPage.clickContinue()
      ContactDetailsPage.enterContactNameAndClickContinue(ContactDetailsPage.firstContactName)
      ContactDetailsPage.enterEmailAddressAndClickContinue(ContactDetailsPage.firstContactEmail)
      ContactDetailsPage.selectYesRadioAndClickContinue()
      ContactDetailsPage.assertContactDetailsMatch(FirstContact)

      When("the user selects to save and continue from the 'Check your answers' page")
      ContactDetailsPage.clickContinue()

      Then("the status on the registration page for the 'Enter your contact details' section is set to 'Completed'")
      RegistrationPage.assertSectionStatus(ContactDetails, Completed)
    }

    Scenario("Complete second contact details", RegistrationTests, ZapTests) {
      Given("a user successfully adds company details from the registration page")
      FeatureTogglePage.setGrsHost(GrsStubOnRegistrationFrontEnd)
      AuthorityWizardPage.selectValidRedirectUrlAndAffinityGroup(Organisation)
      RegistrationPage.clickEnterYourCompanyDetailsLink()
      GrsStubPage.clickStubResponseButton()

      And("the user successfully adds a single contact from the registration page")
      RegistrationPage.clickEnterYourContactDetailsLink()
      ContactDetailsPage.clickContinue()
      ContactDetailsPage.enterContactNameAndClickContinue(ContactDetailsPage.firstContactName)
      ContactDetailsPage.enterEmailAddressAndClickContinue(ContactDetailsPage.firstContactEmail)

      And("the user successfully adds a second contact when prompted if all contacts needed have been added")
      ContactDetailsPage.selectNoRadioAndClickContinue()
      ContactDetailsPage.enterContactNameAndClickContinue(ContactDetailsPage.secondContactName)
      ContactDetailsPage.enterEmailAddressAndClickContinue(ContactDetailsPage.secondContactEmail)
      ContactDetailsPage.assertContactDetailsMatch(FirstContact)
      ContactDetailsPage.assertContactDetailsMatch(SecondContact)

      When("the user selects to save and continue from the 'Check your answers' page")
      ContactDetailsPage.clickContinue()

      Then("the status on the registration page for the 'Enter your contact details' section is set to 'Completed'")
      RegistrationPage.assertSectionStatus(ContactDetails, Completed)
    }

    Scenario("Attempting to add a contact with no name produces the expected error", RegistrationTests, ZapTests) {
      Given("a user successfully adds company details from the registration page")
      FeatureTogglePage.setGrsHost(GrsStubOnRegistrationFrontEnd)
      AuthorityWizardPage.selectValidRedirectUrlAndAffinityGroup(Organisation)
      RegistrationPage.clickEnterYourCompanyDetailsLink()
      GrsStubPage.clickStubResponseButton()

      When("the user selects to add contact details but attempts to continue with no contact name added")
      RegistrationPage.clickEnterYourContactDetailsLink()
      ContactDetailsPage.clickContinue()
      ContactDetailsPage.clickContinue()

      Then("an error page is shown noting the contact name is missing")
      ContactDetailsPage.assertErrorMessageMatches(MissingContactDetails)

      And("on the user selecting to again continue without adding a contact name the same error page is shown")
      ContactDetailsPage.clickContinue()
      ContactDetailsPage.assertErrorMessageMatches(MissingContactDetails)

      And("on the user adding a valid contact name and selecting to continue")
      ContactDetailsPage.enterContactNameAndClickContinue(ContactDetailsPage.firstContactName)

      Then("the user progresses to the 'Enter email address' screen successfully")
      assertOnPage(ContactDetailsPage.enterFirstContactEmailAddressPage)
    }

    Scenario(
      "Attempting to add a contact with no email address produces the expected error",
      RegistrationTests,
      ZapTests
    ) {
      Given("a user successfully adds company details from the registration page")
      FeatureTogglePage.setGrsHost(GrsStubOnRegistrationFrontEnd)
      AuthorityWizardPage.selectValidRedirectUrlAndAffinityGroup(Organisation)
      RegistrationPage.clickEnterYourCompanyDetailsLink()
      GrsStubPage.clickStubResponseButton()

      When("the user selects to add contact details but attempts to continue with no email address added")
      RegistrationPage.clickEnterYourContactDetailsLink()
      ContactDetailsPage.clickContinue()
      ContactDetailsPage.enterContactNameAndClickContinue(ContactDetailsPage.firstContactName)
      ContactDetailsPage.clickContinue()

      Then("an error page is shown noting the contact email is missing")
      ContactDetailsPage.assertErrorMessageMatches(MissingEmailAddress)

      And("on the user selecting to again continue without adding a contact email the same error page is shown")
      ContactDetailsPage.clickContinue()
      ContactDetailsPage.assertErrorMessageMatches(MissingEmailAddress)

      And("on the user adding a valid contact email and selecting to continue")
      ContactDetailsPage.enterEmailAddressAndClickContinue(ContactDetailsPage.firstContactEmail)

      Then("the user progresses to the 'Add another contact' screen successfully")
      assertOnPage(ContactDetailsPage.addAnotherContactPage)
    }

    Scenario(
      "Change contact details from Check Your Answers page",
      RegistrationTests,
      ZapTests
    ) {
      Given("a user successfully adds company details from the registration page")
      FeatureTogglePage.setGrsHost(GrsStubOnRegistrationFrontEnd)
      AuthorityWizardPage.selectValidRedirectUrlAndAffinityGroup(Organisation)
      RegistrationPage.clickEnterYourCompanyDetailsLink()
      GrsStubPage.clickStubResponseButton()

      And("the user successfully adds a single contact from the registration page")
      RegistrationPage.clickEnterYourContactDetailsLink()
      ContactDetailsPage.clickContinue()
      ContactDetailsPage.enterContactNameAndClickContinue(ContactDetailsPage.firstContactName)
      ContactDetailsPage.enterEmailAddressAndClickContinue(ContactDetailsPage.firstContactEmail)

      And("the user successfully adds a second contact when prompted if all contacts needed have been added")
      ContactDetailsPage.selectNoRadioAndClickContinue()
      ContactDetailsPage.enterContactNameAndClickContinue(ContactDetailsPage.secondContactName)
      ContactDetailsPage.enterEmailAddressAndClickContinue(ContactDetailsPage.secondContactEmail)
      ContactDetailsPage.assertContactDetailsMatch(FirstContact)
      ContactDetailsPage.assertContactDetailsMatch(SecondContact)

      And("the user changes all contact details")
      ContactDetailsPage.changeContactDetail(
        ContactDetailsPage.firstContactNameField,
        ContactDetailsPage.changeFirstContactNameLink,
        ContactDetailsPage.newFirstContactName,
        ContactDetailsPage.enterContactNameAndClickContinue
      )

      ContactDetailsPage.changeContactDetail(
        ContactDetailsPage.firstContactEmailField,
        ContactDetailsPage.changeFirstContactEmailLink,
        ContactDetailsPage.newFirstContactEmail,
        ContactDetailsPage.enterEmailAddressAndClickContinue
      )

      ContactDetailsPage.changeContactDetail(
        ContactDetailsPage.secondContactNameField,
        ContactDetailsPage.changeSecondContactNameLink,
        ContactDetailsPage.newSecondContactName,
        ContactDetailsPage.enterContactNameAndClickContinue
      )

      ContactDetailsPage.changeContactDetail(
        ContactDetailsPage.secondContactEmailField,
        ContactDetailsPage.changeSecondContactEmailLink,
        ContactDetailsPage.newSecondContactEmail,
        ContactDetailsPage.enterEmailAddressAndClickContinue
      )

      Then("the new contact detail amendments are shown on screen correctly")
      ContactDetailsPage.verifyChangedContactDetails(
        ContactDetailsPage.firstContactNameField,
        ContactDetailsPage.firstContactName
      )

      ContactDetailsPage.verifyChangedContactDetails(
        ContactDetailsPage.firstContactEmailField,
        ContactDetailsPage.firstContactEmail
      )

      ContactDetailsPage.verifyChangedContactDetails(
        ContactDetailsPage.secondContactNameField,
        ContactDetailsPage.secondContactName
      )

      ContactDetailsPage.verifyChangedContactDetails(
        ContactDetailsPage.secondContactEmailField,
        ContactDetailsPage.secondContactEmail
      )
    }
  }
}
