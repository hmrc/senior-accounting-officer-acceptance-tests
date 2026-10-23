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
      Given("an authenticated user starts adding contact details")
      addFirstContactDetails()
      Then("they click the no radio button")
      AddAnotherContactPage.clickNoRadioButton()
      And("click the continue button")
      AddAnotherContactPage.clickSubmissionButton()
      Then("the user is on the check your answers page")
      assertOnPage(CheckYourAnswersPage)
      testCheckYourAnswerContentFirstContact()

      When("the user amends the first contact name using the 'Change' link")
      Then("they click the first contact name change link")
      CheckYourAnswersPage.clickFirstContactNameChangeLink()
      Then("they are back on the enter first contact name page")
      assertUrl(FirstContactNamePage.changePageUrl)
      Then("they change the old name")
      FirstContactNamePage.addName(TestData.secondPersonName)
      Then("they click the continue button")
      FirstContactNamePage.clickSubmissionButton()
      Then("they are back on the check your answers page")
      assertOnPage(CheckYourAnswersPage)

      And("the user clicks the change email contact link")
      CheckYourAnswersPage.clickFirstContactEmailChangeLink()
      Then("the user is back on the enter first contact email page")
      assertUrl(FirstContactEmailPage.changePageUrl)
      Then("they change the old email")
      FirstContactEmailPage.addEmail(TestData.secondPersonEmail)
      Then("they click the continue button")
      FirstContactEmailPage.clickSubmissionButton()
      Then("they are back on the check your answers page")
      assertOnPage(CheckYourAnswersPage)
      Then("the amended name and email are correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.secondPersonName)
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.secondPersonEmail)

      When("the user wants to go back to the add another client page through the change link")
      Then("the user clicks the change link for 'do you want to add another contact'")
      CheckYourAnswersPage.clickDoYouWantToAddAnotherContactChangeLink()
      Then("they are on the 'add another contact' page")
      assertUrl(AddAnotherContactPage.transactionPageUrl)
      Then("they click the continue button")
      AddAnotherContactPage.clickSubmissionButton()
      Then("they are back on the check your answers page")
      assertOnPage(CheckYourAnswersPage)
      Then("the original value No is correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.addAnotherContactValue, "No")

      When("the user submits the contact details by clicking the continue button")
      CheckYourAnswersPage.clickSubmissionButton()
      Then("the 'Enter your contact details' section status is 'Completed'")
      RegistrationPage.assertRegistrationPageSectionStatus(ContactDetails, Completed)
      When("the user submits the registration by clicking the submission button")
      RegistrationPage.clickSubmissionButton()
      Then("the user is sent to the registration complete page")
      assertOnPage(RegistrationCompletePage)
      And("a unique reference number is displayed")
      RegistrationCompletePage.assertReferenceNumberReturned()
    }

    Scenario(
      "Complete a registration with second contact details",
      RegistrationUITests,
      ZapTests
    ) {
      Given("an authenticated user completes company details and adds first and second contacts")
      addFirstContactDetails()
      addSecondContactDetails()
      testCheckYourAnswerContentFirstContact()
      testCheckYourAnswerContentSecondContact()

      When("the user submits the contact details by clicking the continue button")
      CheckYourAnswersPage.clickSubmissionButton()
      Then("the user is back on the registration page")
      assertOnPage(RegistrationPage)

      Then("the 'Enter your contact details' section status is 'Completed'")
      RegistrationPage.assertRegistrationPageSectionStatus(ContactDetails, Completed)

      When("the user submits the registration")
      RegistrationPage.clickSubmissionButton()

      Then("the user is sent to the registration complete page")
      assertOnPage(RegistrationCompletePage)

      And("a unique reference number is displayed")
      RegistrationCompletePage.assertReferenceNumberReturned()
    }

    Scenario(
      "Navigate to 'Account Homepage' after single contact registration",
      RegistrationUITests,
      ZapTests
    ) {
      Given("a user has completed registration with a single contact")
      addFirstContactDetails()

      Then("they click the no radio button")
      AddAnotherContactPage.clickNoRadioButton()

      Then("they click the continue button")
      AddAnotherContactPage.clickSubmissionButton()

      Then("they are on the check your answers page")
      assertOnPage(CheckYourAnswersPage)

      testCheckYourAnswerContentFirstContact()

      Then("the user clicks the continue button")
      CheckYourAnswersPage.clickSubmissionButton()

      Then("they are on the registration page")
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

      Then("the user clicks the continue button")
      FirstContactNamePage.clickSubmissionButton()

      Then("an error is shown")
      FirstContactNamePage.assertValidationErrorDisplayed(InvalidNameCharactersError)

      When("the user enter a first contact name containing more than 105 characters")
      FirstContactNamePage.addName(TestData.nameCharacterLimitExceeded)

      Then("the user clicks the continue button")
      FirstContactNamePage.clickSubmissionButton()

      Then("an error is shown")
      FirstContactNamePage.assertValidationErrorDisplayed(NameTooLongError)

      When("the user enters a valid name")
      FirstContactNamePage.addName(TestData.firstPersonName)

      Then("the user clicks the continue button")
      FirstContactNamePage.clickSubmissionButton()

      Then("the user is taken to the first contact email page")
      assertOnPage(FirstContactEmailPage)

      When("the user clicks 'Continue' without entering an email address")
      FirstContactEmailPage.clickSubmissionButton()

      Then("the validation error for 'missing email' is shown")
      FirstContactEmailPage.assertValidationErrorDisplayed(ValidationError.MissingEmailError)

      When("the user enters an invalid email address")
      FirstContactEmailPage.addEmail(TestData.invalidEmail)

      Then("the user clicks the continue button")
      FirstContactEmailPage.clickSubmissionButton()

      Then("the validation error for 'invalid email' is shown")
      FirstContactEmailPage.assertValidationErrorDisplayed(ValidationError.InvalidEmailError)

      When("the user enters an email address exceeding the maximum allowed '254' character limit")
      FirstContactEmailPage.addEmail(TestData.emailCharacterLimitExceeded)

      Then("the user clicks the continue button")
      FirstContactEmailPage.clickSubmissionButton()

      Then("the validation error for 'email characters limit exceed' is shown")
      FirstContactEmailPage.assertValidationErrorDisplayed(ValidationError.emailCharacterLimitExceededError)

      When("the user enters a valid email with 'allowed special characters'")
      FirstContactEmailPage.addEmail(TestData.firstPersonEmail)

      Then("the user clicks the continue button")
      FirstContactEmailPage.clickSubmissionButton()

      Then("the user lands on the 'Have you added all the contacts you need?' question page")
      assertOnPage(AddAnotherContactPage)

      When("the user doesn't select an option and clicks 'Continue'")
      AddAnotherContactPage.clickSubmissionButton()

      Then("the 'no element selected validation error' is shown")
      AddAnotherContactPage.assertValidationErrorDisplayed(ValidationError.NoElementChosenContactError)

      When("the selects the 'No' radio button")
      AddAnotherContactPage.clickYesRadioButton()

      Then("the user clicks the continue button")
      AddAnotherContactPage.clickSubmissionButton()

      Then("the user lands on the second contact name page")
      assertOnPage(SecondContactNamePage)

      When("the user clicks 'Continue' without entering a name")
      SecondContactNamePage.clickSubmissionButton()

      Then("the validation error for 'missing name' is shown")
      SecondContactNamePage.assertValidationErrorDisplayed(ValidationError.MissingNameError)

      When("the user enter a second contact name with invalid characters")
      SecondContactNamePage.addName(TestData.nameWithInvalidCharacters)

      Then("the user clicks the continue button")
      SecondContactNamePage.clickSubmissionButton()

      Then("an error is shown")
      SecondContactNamePage.assertValidationErrorDisplayed(InvalidNameCharactersError)

      When("the user enter a second contact name containing more than 105 characters")
      SecondContactNamePage.addName(TestData.nameCharacterLimitExceeded)

      Then("the user clicks the continue button")
      SecondContactNamePage.clickSubmissionButton()

      Then("an error is shown")
      SecondContactNamePage.assertValidationErrorDisplayed(NameTooLongError)

      When("the user enters a valid name and clicks 'Continue'")
      SecondContactNamePage.addName(TestData.secondPersonName)

      Then("the user clicks the continue button")
      SecondContactNamePage.clickSubmissionButton()

      Then("the user is taken to the second contact email page")
      assertOnPage(SecondContactEmailPage)

      When("the user clicks 'Continue' without entering an email address")
      SecondContactEmailPage.clickSubmissionButton()

      Then("the validation error for 'invalid email' is shown")
      SecondContactEmailPage.assertValidationErrorDisplayed(ValidationError.MissingEmailError)

      When("the user enters an invalid email address with consecutive dots in the domain")
      SecondContactEmailPage.addEmail(TestData.invalidEmail)

      Then("the user clicks the continue button")
      SecondContactEmailPage.clickSubmissionButton()

      Then("the invalid email address validation error is shown")
      SecondContactEmailPage.assertValidationErrorDisplayed(ValidationError.InvalidEmailError)

      When("the user enters an email address exceeding the maximum allowed '254' character limit")
      SecondContactEmailPage.addEmail(TestData.emailCharacterLimitExceeded)

      Then("the user clicks the continue button")
      SecondContactEmailPage.clickSubmissionButton()

      Then("the validation error for 'email characters limit exceed' is shown")
      SecondContactEmailPage.assertValidationErrorDisplayed(ValidationError.emailCharacterLimitExceededError)

      When("the user enters a valid email")
      SecondContactEmailPage.addEmail(TestData.secondPersonEmail)

      Then("the user clicks the continue button")
      SecondContactEmailPage.clickSubmissionButton()

      Then("the user lands on the 'Check Your Answers' page showing the correct details")
      assertOnPage(CheckYourAnswersPage)
      testCheckYourAnswerContentFirstContact()
      testCheckYourAnswerContentSecondContact()
    }

    Scenario(
      "When a user selects any 'Change' link and does not commit changes the resultant values on the 'Check Your Answers' page remain unchanged",
      RegistrationUITests,
      ZapTests
    ) {
      Given("an authenticated user completes company details and adds first and second contacts")
      addFirstContactDetails()
      addSecondContactDetails()

      testCheckYourAnswerContentFirstContact()
      testCheckYourAnswerContentSecondContact()

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

      And("the user removes name")
      FirstContactNamePage.addName("")

      And("the user clicks 'Continue' without entering a name")
      FirstContactNamePage.clickSubmissionButton()

      Then("the validation error for 'missing name' is shown")
      FirstContactNamePage.assertValidationErrorDisplayed(MissingNameError)

      Then("the user resubmits with the original name")
      FirstContactNamePage.addName(TestData.firstPersonName)

      And("the user clicks 'Continue'")
      FirstContactNamePage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("the original first contact name is correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.firstPersonName)

      When("the user navigates to the first contact 'change email address' page using the 'Change' link")
      CheckYourAnswersPage.clickFirstContactEmailChangeLink()
      assertUrl(FirstContactEmailPage.changePageUrl)

      And("the user removes email")
      FirstContactEmailPage.addEmail("")

      When("the user clicks 'Continue' without entering an email address")
      FirstContactEmailPage.clickSubmissionButton()

      Then("the validation error for 'missing email' is shown")
      FirstContactEmailPage.assertValidationErrorDisplayed(ValidationError.MissingEmailError)

      And("the user resubmits with the original email")
      FirstContactEmailPage.addEmail(TestData.firstPersonEmail)

      And("the user clicks 'Continue'")
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

      And("the user removes name")
      SecondContactNamePage.addName("")

      And("the user clicks 'Continue' without entering an email address")
      SecondContactNamePage.clickSubmissionButton()

      And("the validation error for 'missing name' is shown")
      SecondContactNamePage.assertValidationErrorDisplayed(ValidationError.MissingNameError)

      And("the user resubmits with the original name")
      SecondContactNamePage.addName(TestData.secondPersonName)

      And("the user clicks 'Continue'")
      SecondContactNamePage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("the original second contact name is correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.secondContactNameValue, TestData.secondPersonName)

      When("the user navigates to the second contact 'change email address' page using the 'Change' link")
      CheckYourAnswersPage.clickSecondContactEmailChangeLink()
      assertUrl(SecondContactEmailPage.changePageUrl)

      And("the user leaves email empty")
      SecondContactEmailPage.addEmail("")

      When("the user clicks 'Continue' without entering a name")
      SecondContactEmailPage.clickSubmissionButton()

      Then("the validation error for 'missing name' is shown")
      SecondContactEmailPage.assertValidationErrorDisplayed(ValidationError.MissingEmailError)

      And("the user resubmits with the original email")
      SecondContactEmailPage.addEmail(TestData.secondPersonEmail)

      And("the user clicks 'Continue'")
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
      addFirstContactDetails()
      Then("the user clicks the no radio button")
      AddAnotherContactPage.clickNoRadioButton()
      Then("the user clicks the continue button")
      AddAnotherContactPage.clickSubmissionButton()
      Then("the user is on the check your answers page")
      assertOnPage(CheckYourAnswersPage)

      testCheckYourAnswerContentFirstContact()

      When("the user selects the 'Change' link for 'Do you want to add another contact'")
      CheckYourAnswersPage.clickDoYouWantToAddAnotherContactChangeLink()
      Then("the user is on the 'add another contact page'")
      assertUrl(AddAnotherContactPage.transactionPageUrl)

      And("the user changes the selection from 'No' to 'Yes' by clicking the yes radio button")
      AddAnotherContactPage.clickYesRadioButton()
      Then("the user clicks the continue button")
      AddAnotherContactPage.clickSubmissionButton()
      Then("the user is on the enter second contact name page")
      assertUrl(SecondContactNamePage.transactionPageUrl)

      Then("the user can enter the second contact details by inputting a name")
      SecondContactNamePage.addName(TestData.secondPersonName)
      Then("the user clicks the continue button")
      SecondContactNamePage.clickSubmissionButton()
      Then("the user is on the enter second contact email page")
      assertUrl(SecondContactEmailPage.transactionPageUrl)
      Then("they input a second contact email")
      SecondContactEmailPage.addEmail(TestData.secondPersonEmail)
      Then("they click the continue button")
      SecondContactEmailPage.clickSubmissionButton()
      Then("the user is on the check your answers page")
      assertOnPage(CheckYourAnswersPage)

      testCheckYourAnswerContentFirstContact()
      testCheckYourAnswerContentSecondContact()

      When("the user selects the 'Change' link for 'Do you want to add another contact?'")
      CheckYourAnswersPage.clickDoYouWantToAddAnotherContactChangeLink()
      Then("The user is on the 'add another contact page'")
      assertUrl(AddAnotherContactPage.transactionPageUrl)

      And("the user changes the selection from 'Yes' to 'No'")
      AddAnotherContactPage.clickNoRadioButton()
      Then("the user clicks the continue button")
      AddAnotherContactPage.clickSubmissionButton()

      Then("the user can view only the first contact details")
      assertOnPage(CheckYourAnswersPage)
      testCheckYourAnswerContentFirstContact()

      When("the user selects the 'Change' link for 'Do you want to add another contact?'")
      CheckYourAnswersPage.clickDoYouWantToAddAnotherContactChangeLink()
      Then("the user is on the 'add another contact' page")
      assertUrl(AddAnotherContactPage.transactionPageUrl)

      And("the user changes the selection from 'No' to 'Yes' for incomplete journey")
      AddAnotherContactPage.clickYesRadioButton()
      Then("the user clicks the back link")
      clickOnBackLink()

      Then("the user can view only the original selection 'No'")
      assertOnPage(CheckYourAnswersPage)
      And("the 'add another contact' value is No")
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
      Then("the user clicks the continue button")
      FirstContactNamePage.clickSubmissionButton()
      Then("the user is on the enter first contact email page")
      assertOnPage(FirstContactEmailPage)
    }

    Scenario(
      "Accept first contact name containing 105 characters",
      RegistrationUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the first contact details page")
      goToFirstContactNamePage()

      When("the user enter a first contact name containing 105 characters")
      FirstContactNamePage.addName(TestData.maximumNameCharacterLimit)
      Then("the user clicks the continue button")
      FirstContactNamePage.clickSubmissionButton()
      Then("the user is on the first contact email page")
      assertOnPage(FirstContactEmailPage)
    }

    Scenario(
      "Accept second contact name containing 1 character",
      RegistrationUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the second contact details page")
      goToSecondContactNamePage()

      When("the user enter a second contact name containing 1 character")
      SecondContactNamePage.addName(TestData.minimumNameCharacterLimit)
      Then("the user clicks the continue button")
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
      Then("the user clicks the continue button")
      SecondContactNamePage.clickSubmissionButton()
      assertOnPage(SecondContactEmailPage)
    }
  }

  private def goToFirstContactNamePage(): Unit = {
    Then("the user clicks the enter contact details link")
    RegistrationPage.clickEnterYourContactDetailsLink()
    Then("the user clicks the continue button")
    ContactDetailsPage.clickSubmissionButton()
    Then("the user is on the first contact name page")
    assertOnPage(FirstContactNamePage)
  }

  private def goToSecondContactNamePage(): Unit = {
    addFirstContactDetails()
    Then("the user clicks the yes radio button")
    AddAnotherContactPage.clickYesRadioButton()
    Then("the user clicks the continue button")
    AddAnotherContactPage.clickSubmissionButton()
  }

  private def addFirstContactDetails(): Unit = {
    Then("the user clicks the enter your contact details link")
    RegistrationPage.clickEnterYourContactDetailsLink()
    Then("the user is on the contact details page")
    assertOnPage(ContactDetailsPage)
    Then("the user clicks the continue button")
    ContactDetailsPage.clickSubmissionButton()
    Then("the user is on the enter first contact name page")
    assertOnPage(FirstContactNamePage)
    Then("the user inputs their first name")
    FirstContactNamePage.addName(TestData.firstPersonName)
    Then("the user clicks the continue button")
    FirstContactNamePage.clickSubmissionButton()
    Then("the user on the enter first email page")
    assertOnPage(FirstContactEmailPage)
    Then("the user inputs their first email")
    FirstContactEmailPage.addEmail(TestData.firstPersonEmail)
    Then("the user clicks the continue button")
    FirstContactEmailPage.clickSubmissionButton()
    Then("the user is on the 'add another contact' page")
    assertOnPage(AddAnotherContactPage)
  }

  private def addSecondContactDetails(): Unit = {
    Then("the user clicks the yes radio button")
    AddAnotherContactPage.clickYesRadioButton()
    And("the user clicks the continue button")
    AddAnotherContactPage.clickSubmissionButton()
    Then("the user is on the enter second contact name page")
    assertOnPage(SecondContactNamePage)
    Then("the user inputs a second contact name")
    SecondContactNamePage.addName(TestData.secondPersonName)
    Then("the user clicks the continue button")
    SecondContactNamePage.clickSubmissionButton()
    Then("the user is on the enter second contact email page")
    assertOnPage(SecondContactEmailPage)
    Then("the user inputs a second contact email")
    SecondContactEmailPage.addEmail(TestData.secondPersonEmail)
    Then("the user clicks the continue button")
    SecondContactEmailPage.clickSubmissionButton()
    Then("the user is on the check your answers page")
    assertOnPage(CheckYourAnswersPage)
  }

  private def testCheckYourAnswerContentFirstContact() = {
    And("the first contact name is correctly displayed")
    assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.firstPersonName)
    And("the first contact email is correctly displayed")
    assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.firstPersonEmail)
    And(s"the add another contact value is correctly displayed with value Yes")
    assertTextOnPage(CheckYourAnswersPage.addAnotherContactValue, "Yes")
  }

  private def testCheckYourAnswerContentSecondContact() = {
    And("the second contact name is correctly displayed")
    assertTextOnPage(CheckYourAnswersPage.secondContactNameValue, TestData.secondPersonName)
    And("the second contact email is correctly displayed")
    assertTextOnPage(CheckYourAnswersPage.secondContactEmailValue, TestData.secondPersonEmail)
  }
}
