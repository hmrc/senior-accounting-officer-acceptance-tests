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

import uk.gov.hmrc.test.ui.adt.PageSectionStatus.Completed
import uk.gov.hmrc.test.ui.adt.RegistrationPageSection.ContactDetails
import uk.gov.hmrc.test.ui.adt.{FirstContact, SecondContact}
import uk.gov.hmrc.test.ui.pages.*
import uk.gov.hmrc.test.ui.pages.registration.*
import uk.gov.hmrc.test.ui.pages.registration.GrsHost.GrsStubOnRegistrationFrontEnd
import uk.gov.hmrc.test.ui.specs.tags.*
import uk.gov.hmrc.test.ui.support.AffinityGroup.Organisation
import uk.gov.hmrc.test.ui.support.PageSupport.{assertOnPage, assertTextOnPage, sendKeys}

class ContactDetailsSpec extends BaseSpec {

  override def beforeEach(): Unit = {
    super.beforeEach()
    FeatureTogglePage.setGrsHost(GrsStubOnRegistrationFrontEnd)
    AuthorityWizardPage.withAffinityGroup(Organisation).redirectToRegistration()
    RegistrationPage.clickEnterYourNominatedCompanyDetailsLink()
    GrsStubPage.clickStubResponseButton()
    assertOnPage(RegistrationPage)
  }

  Feature("Add Contact Details For Registration") {

    // TODO:
    // * Determine tests to have
    // * Decide if we should have error scenarios is one test - are they covered elsewhere?
    // * New pages to add - see UI journey {
    //          contact-details/first/name
    //          contact-details/first/email
    //          first/add-another
    //          contact-details/second/name
    //          contact-details/second/email
    //          contact-details/check-your-answers
    //          registration-complete
    //   }
    // * Add assertOnPage() for each step requiring it

    // * See if we can remove references to .clickContinue() if submission button is the same
    //   - remove clickContinue() from page object and use submissionButton

    // * Dismantle and remove the compound steps
    //   - Add sendKeys directly in spec
    //   - Remove references to .clickContinue() in compound methods (e.g., enterContactNameAndClickContinue)

    // * Consider whether 'contactMap' is the best approach for holding contact details
    // * Consider a better way to test 'ContactDetailsPage.changeContactDetails()' - do we check this already elsewhere?

    // * On the page object:
    //   - review locators on page object (e.g. contactNameValueLocator)
    //   - review and correct the verifyChangedContactDetails function

    // SCENARIOS:
    //   - Complete adding first contact details with amendments > Assert registration ID
    //   - Complete adding second contact details without amendments > Assert registration ID
    //   - Missing name shows error > missing email shows error
    //   - Select to change all values without changing anything > assert CYA does not change.

    Scenario(
      "Complete a registration adding first contact details with amendments",
      RegistrationUITests,
      ZapTests
    ) {
      Given("an authenticated user has added a first contact with name 'Amanda Test' and email 'Amanda_Test@mail.com'")
      RegistrationPage.clickEnterYourContactDetailsLink()
      assertOnPage(ContactDetailsPage)
      ContactDetailsPage.clickContinue()
      assertOnPage(FirstContactNamePage)
      sendKeys(FirstContactNamePage.nameInput, "Amanda Test")
      FirstContactNamePage.clickSubmissionButton()
      assertOnPage(FirstContactEmailPage)
      sendKeys(FirstContactEmailPage.emailInput, "Amanda_Test@mail.com")
      FirstContactEmailPage.clickSubmissionButton()
      assertOnPage(AddAnotherContactPage)
      AddAnotherContactPage.clickYesRadioButton()
      AddAnotherContactPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, "Amanda Test")
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, "Amanda_Test@mail.com")

      When("the user amends the first contact name to 'Test-Amendment Of'name'")
      CheckYourAnswersPage.clickFirstContactNameChangeLink()
      assertOnPage(FirstContactNamePage.changePageUrl)
      sendKeys(FirstContactNamePage.nameInput, "Test-Amendment Of'name")
      FirstContactNamePage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      And(
        "navigates to the 'change email address' page using the 'change' link but selects to submit without making a change"
      )
      CheckYourAnswersPage.clickFirstContactEmailChangeLink()
      assertOnPage(FirstContactEmailPage.changePageUrl)
      FirstContactEmailPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("the amended name and original email are correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, "Test-Amendment Of'name")
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, "Amanda_Test@mail.com")

      When("the user submits the contact details")
      CheckYourAnswersPage.clickSubmissionButton()
      assertOnPage(RegistrationPage)

      Then("the state of the 'Enter your contact details' section is correctly set as 'Completed'")
      RegistrationPage.assertSectionStatus(ContactDetails, Completed)

      When("the user finally submits the registration")
      RegistrationPage.clickSubmissionButton()

      Then("the registration is completed and a reference Id is displayed")
      assertOnPage(RegistrationCompletePage)
      RegistrationCompletePage.assertReferenceIdReturned()
    }

    Scenario("Complete second contact details", RegistrationUITests, ZapTests) {
      Given("an authenticated user has added a first contact")
      RegistrationPage.clickEnterYourContactDetailsLink()
      assertOnPage(ContactDetailsPage)
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

    Scenario("Attempting to add a contact with no name produces the expected error", RegistrationUITests, ZapTests) {
      Given("an authenticated user attempts to add a contact with no name")
      RegistrationPage.clickEnterYourContactDetailsLink()
      ContactDetailsPage.clickContinue()
      ContactDetailsPage.clickContinue()

      Then("an error page is shown")
      assertTextOnPage(ContactDetailsPage.errorTitle, "There is a problem")

      And("on the user selecting to again continue without adding a contact name the error page is shown again")
      ContactDetailsPage.clickContinue()
      assertTextOnPage(ContactDetailsPage.errorTitle, "There is a problem")

      And("on the user adding a valid contact name and selecting to continue")
      ContactDetailsPage.enterContactNameAndClickContinue(ContactDetailsPage.firstContactName)

      Then("the user progresses to the 'Enter email address' screen successfully")
      assertOnPage(ContactDetailsPage.enterFirstContactEmailAddressPage)
    }

    Scenario(
      "Attempting to add a contact with no email address causes an error to be displayed",
      RegistrationUITests,
      ZapTests
    ) {
      Given("an authenticated user attempts to add a contact with no email address")
      RegistrationPage.clickEnterYourContactDetailsLink()
      ContactDetailsPage.clickContinue()
      ContactDetailsPage.enterContactNameAndClickContinue(ContactDetailsPage.firstContactName)
      ContactDetailsPage.clickContinue()

      Then("an error page is shown")
      assertTextOnPage(ContactDetailsPage.errorTitle, "There is a problem")

      And("on the user selecting to again continue without adding a contact email the error page is shown again")
      ContactDetailsPage.clickContinue()
      assertTextOnPage(ContactDetailsPage.errorTitle, "There is a problem")

      And("on the user adding a valid contact email and selecting to continue")
      ContactDetailsPage.enterEmailAddressAndClickContinue(ContactDetailsPage.firstContactEmail)

      Then("the user progresses to the 'Add another contact' screen successfully")
      assertOnPage(ContactDetailsPage.addAnotherContactPage)
    }

    Scenario(
      "Change contact details from Check Your Answers page",
      RegistrationUITests,
      ZapTests
    ) {
      Given("an authenticated user successfully adds a single contact from the registration page")
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
