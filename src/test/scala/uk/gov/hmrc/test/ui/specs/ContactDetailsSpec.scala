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

import uk.gov.hmrc.test.ui.adt.AffinityGroup.Organisation
import uk.gov.hmrc.test.ui.adt.PageSectionStatus.Completed
import uk.gov.hmrc.test.ui.adt.RegistrationPageSection.ContactDetails
import uk.gov.hmrc.test.ui.adt.{FirstContact, SecondContact}
import uk.gov.hmrc.test.ui.pages.*
import uk.gov.hmrc.test.ui.pages.registration.*
import uk.gov.hmrc.test.ui.pages.registration.GrsHost.GrsStubOnRegistrationFrontEnd
import uk.gov.hmrc.test.ui.specs.tags.*
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
    // * Dismantle and remove the compound steps
    //   - Add sendKeys directly in spec

    // * Consider whether 'contactMap' is the best approach for holding contact details
    // * Consider a better way to test 'ContactDetailsPage.changeContactDetails()' - do we check this already elsewhere?

    // * On the page object:
    //   - review locators on page object (e.g. contactNameValueLocator)
    //   - review and correct the verifyChangedContactDetails function

    Scenario(
      "Complete a registration with amended first contact details",
      RegistrationUITests,
      ZapTests
    ) {
      Given("an authenticated user adds company details and a first contact")
      RegistrationPage.clickEnterYourContactDetailsLink()
      assertOnPage(ContactDetailsPage)
      ContactDetailsPage.clickSubmissionButton()
      assertOnPage(FirstContactNamePage)
      sendKeys(FirstContactNamePage.nameInput, ContactDetailsPage.firstContactName)
      FirstContactNamePage.clickSubmissionButton()
      assertOnPage(FirstContactEmailPage)
      sendKeys(FirstContactEmailPage.emailInput, ContactDetailsPage.firstContactEmail)
      FirstContactEmailPage.clickSubmissionButton()
      assertOnPage(AddAnotherContactPage)
      AddAnotherContactPage.clickYesRadioButton()
      AddAnotherContactPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, ContactDetailsPage.firstContactName)
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, ContactDetailsPage.firstContactEmail)

      When("the user amends the first contact name using the 'Change' link")
      CheckYourAnswersPage.clickFirstContactNameChangeLink()
      assertOnPage(FirstContactNamePage.changePageUrl)
      sendKeys(FirstContactNamePage.nameInput, ContactDetailsPage.secondContactName)
      FirstContactNamePage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      And("navigates to the 'change email address' page using the 'Change' link")
      CheckYourAnswersPage.clickFirstContactEmailChangeLink()
      assertOnPage(FirstContactEmailPage.changePageUrl)

      And("submits without changing the email")
      FirstContactEmailPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("the amended name and original email are correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, ContactDetailsPage.secondContactName)
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, ContactDetailsPage.firstContactEmail)

      When("the user submits the contact details")
      CheckYourAnswersPage.clickSubmissionButton()
      assertOnPage(RegistrationPage)

      Then("the 'Enter your contact details' section status is 'Completed'")
      RegistrationPage.assertSectionStatus(ContactDetails, Completed)

      When("the user submits the registration")
      RegistrationPage.clickSubmissionButton()

      Then("a unique reference Id is displayed on the 'Registration Complete' page")
      assertOnPage(RegistrationCompletePage)
      RegistrationCompletePage.assertReferenceIdReturned()
    }

    Scenario(
      "Complete a registration with second contact details",
      RegistrationUITests,
      ZapTests
    ) {
      Given("an authenticated user completes company details and adds first and second contacts")
      RegistrationPage.clickEnterYourContactDetailsLink()
      assertOnPage(ContactDetailsPage)
      ContactDetailsPage.clickSubmissionButton()
      assertOnPage(FirstContactNamePage)
      sendKeys(FirstContactNamePage.nameInput, ContactDetailsPage.firstContactName)
      FirstContactNamePage.clickSubmissionButton()
      assertOnPage(FirstContactEmailPage)
      sendKeys(FirstContactEmailPage.emailInput, ContactDetailsPage.firstContactEmail)
      FirstContactEmailPage.clickSubmissionButton()
      assertOnPage(AddAnotherContactPage)
      AddAnotherContactPage.clickNoRadioButton()
      AddAnotherContactPage.clickSubmissionButton()
      assertOnPage(SecondContactNamePage)
      sendKeys(SecondContactNamePage.nameInput, ContactDetailsPage.secondContactName)
      SecondContactNamePage.clickSubmissionButton()
      assertOnPage(SecondContactEmailPage)
      sendKeys(SecondContactEmailPage.emailInput, ContactDetailsPage.secondContactEmail)
      SecondContactEmailPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("both contacts details are correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, ContactDetailsPage.firstContactName)
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, ContactDetailsPage.firstContactEmail)
      assertTextOnPage(CheckYourAnswersPage.secondContactNameValue, ContactDetailsPage.secondContactName)
      assertTextOnPage(CheckYourAnswersPage.secondContactEmailValue, ContactDetailsPage.secondContactEmail)

      When("the user submits the contact details")
      CheckYourAnswersPage.clickSubmissionButton()
      assertOnPage(RegistrationPage)

      Then("the 'Enter your contact details' section status is 'Completed'")
      RegistrationPage.assertSectionStatus(ContactDetails, Completed)

      When("the user submits the registration")
      RegistrationPage.clickSubmissionButton()

      Then("a unique reference Id is displayed on the 'Registration Complete' page")
      assertOnPage(RegistrationCompletePage)
      RegistrationCompletePage.assertReferenceIdReturned()
    }

    // SCENARIOS:
    //   - Missing name shows error > missing email shows error
    //   - Select to change all values without changing anything > assert CYA does not change.

    Scenario(
      "Attempting to add a contact with no name produces the expected error",
      RegistrationUITests,
      ZapTests,
      SoloTests
    ) {
      Given("an authenticated user attempts to add a contact with no name")
      RegistrationPage.clickEnterYourContactDetailsLink()
      ContactDetailsPage.clickSubmissionButton() // why does this test only pass when this is clicked twice
      ContactDetailsPage.clickSubmissionButton()

      Then("an error page is shown")
      assertTextOnPage(ContactDetailsPage.errorTitle, "There is a problem")

      And("on the user selecting to again continue without adding a contact name the error page is shown again")
      ContactDetailsPage.clickSubmissionButton()
      assertTextOnPage(ContactDetailsPage.errorTitle, "There is a problem")

      And("on the user adding a valid contact name and selecting to continue")
      ContactDetailsPage.enterContactNameAndClickContinue(ContactDetailsPage.firstContactName)

      Then("the user progresses to the 'Enter email address' screen successfully")
      assertOnPage(ContactDetailsPage.enterFirstContactEmailAddressPage)
    }

    Scenario(
      "Attempting to add a contact with no email address causes an error to be displayed",
      RegistrationUITests,
      ZapTests,
      SoloTests
    ) {
      Given("an authenticated user attempts to add a contact with no email address")
      RegistrationPage.clickEnterYourContactDetailsLink()
      ContactDetailsPage.clickSubmissionButton()
      ContactDetailsPage.enterContactNameAndClickContinue(ContactDetailsPage.firstContactName)
      ContactDetailsPage.clickSubmissionButton()

      Then("an error page is shown")
      assertTextOnPage(ContactDetailsPage.errorTitle, "There is a problem")

      And("on the user selecting to again continue without adding a contact email the error page is shown again")
      ContactDetailsPage.clickSubmissionButton()
      assertTextOnPage(ContactDetailsPage.errorTitle, "There is a problem")

      And("on the user adding a valid contact email and selecting to continue")
      ContactDetailsPage.enterEmailAddressAndClickContinue(ContactDetailsPage.firstContactEmail)

      Then("the user progresses to the 'Add another contact' screen successfully")
      assertOnPage(ContactDetailsPage.addAnotherContactPage)
    }

    Scenario(
      "Change contact details from Check Your Answers page",
      RegistrationUITests,
      ZapTests,
      SoloTests
    ) {
      Given("an authenticated user successfully adds a single contact from the registration page")
      RegistrationPage.clickEnterYourContactDetailsLink()
      ContactDetailsPage.clickSubmissionButton()
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
