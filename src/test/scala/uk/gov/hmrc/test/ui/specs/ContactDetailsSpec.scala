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
import uk.gov.hmrc.test.ui.adt.ValidationError
import uk.gov.hmrc.test.ui.adt.ValidationError.*
import uk.gov.hmrc.test.ui.pages.*
import uk.gov.hmrc.test.ui.pages.grs.NominatedCompanyDetailsGuidancePage
import uk.gov.hmrc.test.ui.pages.registration.*
import uk.gov.hmrc.test.ui.pages.registration.GrsHost.GrsStubOnRegistrationFrontEnd
import uk.gov.hmrc.test.ui.specs.tags.*
import uk.gov.hmrc.test.ui.support.PageSupport.*
import uk.gov.hmrc.test.ui.support.TestData

class ContactDetailsSpec extends BaseSpec {

  override def beforeEach(): Unit = {
    super.beforeEach()
    FeatureTogglePage.setGrsHost(GrsStubOnRegistrationFrontEnd)
    FeatureTogglePage.setReshuffledContactFlow(isEnabled = true)
    AuthorityWizardPage.withAffinityGroup(Organisation).redirectToRegistration()
    RegistrationPage.clickEnterYourNominatedCompanyDetailsLink()
    assertOnPage(NominatedCompanyDetailsGuidancePage)
    NominatedCompanyDetailsGuidancePage.clickSubmissionButton()
    GrsStubPage.clickStubResponseButton()
    assertOnPage(RegistrationPage)
  }

  Feature("Add Contact Details For Registration") {

    Scenario(
      "Complete a registration with amended first contact details",
      RegistrationUITests,
      ZapTests
    ) {
      Given("an authenticated user adds company details and a first contact")
      AddFirstContactDetails()
      AddAnotherContactPage.clickNoRadioButton()
      AddAnotherContactPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.firstPersonName)
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.firstPersonEmail)
      assertTextOnPage(CheckYourAnswersPage.addAnotherContactValue, "No")

      When("the user amends the first contact name using the 'Change' link")
      CheckYourAnswersPage.clickFirstContactNameChangeLink()
      assertUrl(FirstContactNamePage.changePageUrl)
      FirstContactNamePage.addName(TestData.secondPersonName)
      FirstContactNamePage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      And("amends the first contact email using the 'Change' link")
      CheckYourAnswersPage.clickFirstContactEmailChangeLink()
      assertUrl(FirstContactEmailPage.changePageUrl)
      FirstContactEmailPage.addEmail(TestData.secondPersonEmail)
      FirstContactEmailPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("the amended name and email are correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.secondPersonName)
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.secondPersonEmail)

      And(
        "the user navigates to the 'Add another contact' page using 'Do you want to add another contact' change link"
      )
      CheckYourAnswersPage.clickDoYouWantToAddAnotherContactChangeLink()
      assertUrl(AddAnotherContactPage.transactionPageUrl)
      AddAnotherContactPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("the original value No is correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.addAnotherContactValue, "No")

      When("the user submits the contact details")
      CheckYourAnswersPage.clickSubmissionButton()

      Then("the 'Enter your contact details' section status is 'Completed'")
      RegistrationPage.assertRegistrationPageSectionStatus(ContactDetails, Completed)

      When("the user submits the registration")
      RegistrationPage.clickSubmissionButton()

      Then("a unique reference number is displayed on the 'Registration Complete' page")
      assertOnPage(RegistrationCompletePage)
      RegistrationCompletePage.assertReferenceNumberReturned()
    }

    Scenario(
      "Complete a registration with second contact details",
      RegistrationUITests,
      ZapTests
    ) {
      Given("an authenticated user completes company details and adds first and second contacts")
      AddFirstContactDetails()
      AddSecondContactDetails()

      assertOnPage(CheckYourAnswersPage)

      Then("the both contacts details are correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.firstPersonName)
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.firstPersonEmail)
      assertTextOnPage(CheckYourAnswersPage.addAnotherContactValue, "Yes")
      assertTextOnPage(CheckYourAnswersPage.secondContactNameValue, TestData.secondPersonName)
      assertTextOnPage(CheckYourAnswersPage.secondContactEmailValue, TestData.secondPersonEmail)

      When("the user submits the contact details")
      CheckYourAnswersPage.clickSubmissionButton()
      assertOnPage(RegistrationPage)

      Then("the 'Enter your contact details' section status is 'Completed'")
      RegistrationPage.assertRegistrationPageSectionStatus(ContactDetails, Completed)

      When("the user submits the registration")
      RegistrationPage.clickSubmissionButton()

      Then("a unique reference number is displayed on the 'Registration Complete' page")
      assertOnPage(RegistrationCompletePage)
      RegistrationCompletePage.assertReferenceNumberReturned()
    }

    Scenario(
      "Navigate to 'Account Homepage' after single contact registration",
      RegistrationUITests,
      ZapTests
    ) {
      Given("a user has completed registration with a single contact")
      AddFirstContactDetails()
      AddAnotherContactPage.clickNoRadioButton()
      AddAnotherContactPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.firstPersonName)
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.firstPersonEmail)
      assertTextOnPage(CheckYourAnswersPage.addAnotherContactValue, "No")
      CheckYourAnswersPage.clickSubmissionButton()
      assertOnPage(RegistrationPage)

      When("the user clicks the 'Continue' button on the 'Registration Complete' page")
      RegistrationCompletePage.clickSubmissionButton()

      Then("the user lands on the 'Account Homepage'")
      assertOnPage(RegistrationCompletePage)
    }

    Scenario(
      "Validate that valid contact details are required during registration",
      RegistrationUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the first contact name page")
      goToFirstContactNamePage()

      When("the user clicks 'Continue' without entering a name")
      FirstContactNamePage.clickSubmissionButton()

      Then("the validation error for 'missing name' is shown")
      FirstContactNamePage.assertValidationErrorDisplayed(MissingNameError)

      When("the user enter a first contact name with invalid characters")
      FirstContactNamePage.addName(TestData.nameWithInvalidCharacters)
      FirstContactNamePage.clickSubmissionButton()

      Then("an error is shown")
      FirstContactNamePage.assertValidationErrorDisplayed(InvalidNameCharactersError)

      When("the user enter a first contact name containing more than 105 characters")
      FirstContactNamePage.addName(TestData.nameCharacterLimitExceeded)
      FirstContactNamePage.clickSubmissionButton()

      Then("an error is shown")
      FirstContactNamePage.assertValidationErrorDisplayed(NameTooLongError)

      When("the user enters a valid name and clicks 'Continue'")
      FirstContactNamePage.addName(TestData.firstPersonName)
      FirstContactNamePage.clickSubmissionButton()

      Then("the user is taken to the first contact email page")
      assertOnPage(FirstContactEmailPage)

      When("the user clicks 'Continue' without entering an email address")
      FirstContactEmailPage.clickSubmissionButton()

      Then("the validation error for 'missing email' is shown")
      FirstContactEmailPage.assertValidationErrorDisplayed(ValidationError.MissingEmailError)

      When("the user enters an invalid email address and clicks 'Continue'")
      FirstContactEmailPage.addEmail(TestData.invalidEmail)
      FirstContactEmailPage.clickSubmissionButton()

      Then("the validation error for 'invalid email' is shown")
      FirstContactEmailPage.assertValidationErrorDisplayed(ValidationError.InvalidEmailError)

      When("the user enters an email address exceeding the maximum allowed '254' character limit and clicks 'Continue'")
      FirstContactEmailPage.addEmail(TestData.emailCharacterLimitExceeded)
      FirstContactEmailPage.clickSubmissionButton()

      Then("the validation error for 'email characters limit exceed' is shown")
      FirstContactEmailPage.assertValidationErrorDisplayed(ValidationError.emailCharacterLimitExceededError)

      When("the user enters a valid email with 'allowed special characters' and clicks 'Continue'")
      FirstContactEmailPage.addEmail(TestData.firstPersonEmail)
      FirstContactEmailPage.clickSubmissionButton()

      Then("the user lands on the 'Have you added all the contacts you need?' question page")
      assertOnPage(AddAnotherContactPage)

      When("the user doesn't select an option and clicks 'Continue'")
      AddAnotherContactPage.clickSubmissionButton()

      Then("the 'no element selected validation error' is shown")
      AddAnotherContactPage.assertValidationErrorDisplayed(ValidationError.NoElementChosenContactError)

      When("the selects the 'No' radio button and clicks 'Continue'")
      AddAnotherContactPage.clickYesRadioButton()
      AddAnotherContactPage.clickSubmissionButton()

      Then("the user lands on the second contact name page")
      assertOnPage(SecondContactNamePage)

      When("the user clicks 'Continue' without entering a name")
      SecondContactNamePage.clickSubmissionButton()

      Then("the validation error for 'missing name' is shown")
      SecondContactNamePage.assertValidationErrorDisplayed(ValidationError.MissingNameError)

      When("the user enter a second contact name with invalid characters")
      SecondContactNamePage.addName(TestData.nameWithInvalidCharacters)
      SecondContactNamePage.clickSubmissionButton()

      Then("an error is shown")
      SecondContactNamePage.assertValidationErrorDisplayed(InvalidNameCharactersError)

      When("the user enter a second contact name containing more than 105 characters")
      SecondContactNamePage.addName(TestData.nameCharacterLimitExceeded)
      SecondContactNamePage.clickSubmissionButton()

      Then("an error is shown")
      SecondContactNamePage.assertValidationErrorDisplayed(NameTooLongError)

      When("the user enters a valid name and clicks 'Continue'")
      SecondContactNamePage.addName(TestData.secondPersonName)
      SecondContactNamePage.clickSubmissionButton()

      Then("the user is taken to the second contact email page")
      assertOnPage(SecondContactEmailPage)

      When("the user clicks 'Continue' without entering an email address")
      SecondContactEmailPage.clickSubmissionButton()

      Then("the validation error for 'invalid email' is shown")
      SecondContactEmailPage.assertValidationErrorDisplayed(ValidationError.MissingEmailError)

      When("the user enters an invalid email address with consecutive dots in the domain and clicks 'Continue'")
      SecondContactEmailPage.addEmail(TestData.invalidEmail)
      SecondContactEmailPage.clickSubmissionButton()

      Then("the invalid email address validation error is shown")
      SecondContactEmailPage.assertValidationErrorDisplayed(ValidationError.InvalidEmailError)

      When("the user enters an email address exceeding the maximum allowed '254' character limit and clicks 'Continue'")
      SecondContactEmailPage.addEmail(TestData.emailCharacterLimitExceeded)
      SecondContactEmailPage.clickSubmissionButton()

      Then("the validation error for 'email characters limit exceed' is shown")
      SecondContactEmailPage.assertValidationErrorDisplayed(ValidationError.emailCharacterLimitExceededError)

      When("the user enters a valid email and clicks 'Continue'")
      SecondContactEmailPage.addEmail(TestData.secondPersonEmail)
      SecondContactEmailPage.clickSubmissionButton()

      Then("the user lands on the 'Check Your Answers' page showing the correct details")
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.firstPersonName)
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.firstPersonEmail)
      assertTextOnPage(CheckYourAnswersPage.addAnotherContactValue, "Yes")
      assertTextOnPage(CheckYourAnswersPage.secondContactNameValue, TestData.secondPersonName)
      assertTextOnPage(CheckYourAnswersPage.secondContactEmailValue, TestData.secondPersonEmail)
    }

    Scenario(
      "When a user selects any 'Change' link and does not commit changes the resultant values on the 'Check Your Answers' page remain unchanged",
      RegistrationUITests,
      ZapTests
    ) {
      Given("an authenticated user completes company details and adds first and second contacts")
      AddFirstContactDetails()
      AddSecondContactDetails()

      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.firstPersonName)
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.firstPersonEmail)
      assertTextOnPage(CheckYourAnswersPage.addAnotherContactValue, "Yes")
      assertTextOnPage(CheckYourAnswersPage.secondContactNameValue, TestData.secondPersonName)
      assertTextOnPage(CheckYourAnswersPage.secondContactEmailValue, TestData.secondPersonEmail)

      When("the user navigates to the first contact 'change name' page using the 'Change' link")
      CheckYourAnswersPage.clickFirstContactNameChangeLink()
      assertUrl(FirstContactNamePage.changePageUrl)

      And("the user changes the name but not completed the journey")
      FirstContactNamePage.addName(TestData.name)
      clickOnBackLink()
      assertOnPage(CheckYourAnswersPage)

      Then("the original first contact name is correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.firstPersonName)

      When("the user navigates to the first contact 'change name' page using the 'Change' link")
      CheckYourAnswersPage.clickFirstContactNameChangeLink()
      assertUrl(FirstContactNamePage.changePageUrl)

      And("submits without changing the name")
      FirstContactNamePage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("the original first contact name is correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.firstPersonName)

      When("the user navigates to the first contact 'change email address' page using the 'Change' link")
      CheckYourAnswersPage.clickFirstContactEmailChangeLink()
      assertUrl(FirstContactEmailPage.changePageUrl)

      And("submits without changing the email")
      FirstContactEmailPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("the original first contact email address is correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.firstPersonEmail)

      When(
        "the user navigates to the 'Add another contact' page using 'Do you want to add another contact' change link"
      )
      CheckYourAnswersPage.clickDoYouWantToAddAnotherContactChangeLink()
      assertUrl(AddAnotherContactPage.transactionPageUrl)

      And("the user not changed the original selection")
      AddAnotherContactPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("the original value Yes is correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.addAnotherContactValue, "Yes")

      When("the user navigates to the second contact 'change name' page using the 'Change' link")
      CheckYourAnswersPage.clickSecondContactNameChangeLink()
      assertUrl(SecondContactNamePage.changePageUrl)

      And("submits without changing the name")
      SecondContactNamePage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("the original second contact name is correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.secondContactNameValue, TestData.secondPersonName)

      When("the user navigates to the second contact 'change email address' page using the 'Change' link")
      CheckYourAnswersPage.clickSecondContactEmailChangeLink()
      assertUrl(SecondContactEmailPage.changePageUrl)

      And("submits without changing the email")
      SecondContactEmailPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("the original second contact email address is correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.secondContactEmailValue, TestData.secondPersonEmail)
    }

    Scenario(
      "Allow users to update their “add another contact” selection via the Change link",
      RegistrationUITests,
      ZapTests
    ) {
      Given("an authenticated user has added company details and a first contact")
      AddFirstContactDetails()
      AddAnotherContactPage.clickNoRadioButton()
      AddAnotherContactPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.firstPersonName)
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.firstPersonEmail)
      assertTextOnPage(CheckYourAnswersPage.addAnotherContactValue, "No")

      When("the user selects the 'Change' link for 'Do you want to add another contact'")
      CheckYourAnswersPage.clickDoYouWantToAddAnotherContactChangeLink()
      assertUrl(AddAnotherContactPage.transactionPageUrl)

      And("the user changes the selection from 'No' to 'Yes'")
      AddAnotherContactPage.clickYesRadioButton()
      AddAnotherContactPage.clickSubmissionButton()
      assertUrl(SecondContactNamePage.transactionPageUrl)

      Then("the user can enter the second contact details")
      SecondContactNamePage.addName(TestData.secondPersonName)
      SecondContactNamePage.clickSubmissionButton()
      assertUrl(SecondContactEmailPage.transactionPageUrl)
      SecondContactEmailPage.addEmail(TestData.secondPersonEmail)
      SecondContactEmailPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      And("the user can view the details of both contacts")
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.firstPersonName)
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.firstPersonEmail)
      assertTextOnPage(CheckYourAnswersPage.addAnotherContactValue, "Yes")
      assertTextOnPage(CheckYourAnswersPage.secondContactNameValue, TestData.secondPersonName)
      assertTextOnPage(CheckYourAnswersPage.secondContactEmailValue, TestData.secondPersonEmail)

      When("the user selects the 'Change' link for 'Do you want to add another contact?'")
      CheckYourAnswersPage.clickDoYouWantToAddAnotherContactChangeLink()
      assertUrl(AddAnotherContactPage.transactionPageUrl)

      And("the user changes the selection from 'Yes' to 'No'")
      AddAnotherContactPage.clickNoRadioButton()
      AddAnotherContactPage.clickSubmissionButton()

      Then("the user can view only the first contact details")
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.firstPersonName)
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.firstPersonEmail)
      assertTextOnPage(CheckYourAnswersPage.addAnotherContactValue, "No")

      When("the user selects the 'Change' link for 'Do you want to add another contact?'")
      CheckYourAnswersPage.clickDoYouWantToAddAnotherContactChangeLink()
      assertUrl(AddAnotherContactPage.transactionPageUrl)

      And("the user changes the selection from 'No' to 'Yes' for incomplete journey")
      AddAnotherContactPage.clickYesRadioButton()
      clickOnBackLink()

      Then("the user can view only the original selection 'No'")
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.addAnotherContactValue, "No")
    }

    Scenario(
      "Accept first contact name containing 1 character",
      RegistrationUITests,
      ZapTests
    ) {
      Given(
        "an authenticated user lands on the first contact details page"
      )
      goToFirstContactNamePage()

      When("the user enter a first contact name containing 1 character")
      FirstContactNamePage.addName(TestData.minimumNameCharacterLimit)
      FirstContactNamePage.clickSubmissionButton()
      assertOnPage(FirstContactEmailPage)
    }

    Scenario(
      "Accept first contact name containing 105 characters",
      RegistrationUITests,
      ZapTests
    ) {
      Given(
        "an authenticated user lands on the first contact details page"
      )
      goToFirstContactNamePage()

      When("the user enter a first contact name containing 105 characters")
      FirstContactNamePage.addName(TestData.maximumNameCharacterLimit)
      FirstContactNamePage.clickSubmissionButton()
      assertOnPage(FirstContactEmailPage)
    }

    Scenario(
      "Accept second contact name containing 1 character",
      RegistrationUITests,
      ZapTests
    ) {
      Given(
        "an authenticated user lands on the second contact details page"
      )
      goToSecondContactNamePage()

      When("the user enter a second contact name containing 1 character")
      SecondContactNamePage.addName(TestData.minimumNameCharacterLimit)
      SecondContactNamePage.clickSubmissionButton()
      assertOnPage(SecondContactEmailPage)
    }

    Scenario(
      "Accept second contact name containing 105 characters",
      RegistrationUITests,
      ZapTests
    ) {
      Given(
        "an authenticated user lands on the second contact details page"
      )
      goToSecondContactNamePage()

      When("the user enter a second contact name containing 105 characters")
      SecondContactNamePage.addName(TestData.maximumNameCharacterLimit)
      SecondContactNamePage.clickSubmissionButton()
      assertOnPage(SecondContactEmailPage)
    }
  }

  private def goToFirstContactNamePage(): Unit = {
    RegistrationPage.clickEnterYourContactDetailsLink()
    ContactDetailsPage.clickSubmissionButton()
    assertOnPage(FirstContactNamePage)
  }

  private def goToSecondContactNamePage(): Unit = {
    AddFirstContactDetails()
    AddAnotherContactPage.clickYesRadioButton()
    AddAnotherContactPage.clickSubmissionButton()
  }

  private def AddFirstContactDetails(): Unit = {
    RegistrationPage.clickEnterYourContactDetailsLink()
    assertOnPage(ContactDetailsPage)
    ContactDetailsPage.clickSubmissionButton()
    assertOnPage(FirstContactNamePage)
    FirstContactNamePage.addName(TestData.firstPersonName)
    FirstContactNamePage.clickSubmissionButton()
    assertOnPage(FirstContactEmailPage)
    FirstContactEmailPage.addEmail(TestData.firstPersonEmail)
    FirstContactEmailPage.clickSubmissionButton()
    assertOnPage(AddAnotherContactPage)
  }

  private def AddSecondContactDetails(): Unit = {
    AddAnotherContactPage.clickYesRadioButton()
    AddAnotherContactPage.clickSubmissionButton()
    assertOnPage(SecondContactNamePage)
    SecondContactNamePage.addName(TestData.secondPersonName)
    SecondContactNamePage.clickSubmissionButton()
    assertOnPage(SecondContactEmailPage)
    SecondContactEmailPage.addEmail(TestData.secondPersonEmail)
    SecondContactEmailPage.clickSubmissionButton()
    assertOnPage(CheckYourAnswersPage)
  }
}
