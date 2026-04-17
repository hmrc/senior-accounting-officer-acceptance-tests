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
import uk.gov.hmrc.test.ui.pages.*
import uk.gov.hmrc.test.ui.pages.registration.*
import uk.gov.hmrc.test.ui.pages.registration.GrsHost.GrsStubOnRegistrationFrontEnd
import uk.gov.hmrc.test.ui.specs.tags.*
import uk.gov.hmrc.test.ui.support.PageSupport.{assertOnPage, assertTextOnPage, sendKeys}
import uk.gov.hmrc.test.ui.support.TestData

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
      sendKeys(FirstContactNamePage.nameInput, TestData.firstContactName)
      FirstContactNamePage.clickSubmissionButton()
      assertOnPage(FirstContactEmailPage)
      sendKeys(FirstContactEmailPage.emailInput, TestData.firstContactEmail)
      FirstContactEmailPage.clickSubmissionButton()
      assertOnPage(AddAnotherContactPage)
      AddAnotherContactPage.clickYesRadioButton()
      AddAnotherContactPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.firstContactName)
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.firstContactEmail)

      When("the user amends the first contact name using the 'Change' link")
      CheckYourAnswersPage.clickFirstContactNameChangeLink()
      assertOnPage(FirstContactNamePage.changePageUrl)
      sendKeys(FirstContactNamePage.nameInput, TestData.secondContactName)
      FirstContactNamePage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      And("amends the first contact email using the 'Change' link")
      CheckYourAnswersPage.clickFirstContactEmailChangeLink()
      assertOnPage(FirstContactEmailPage.changePageUrl)
      sendKeys(FirstContactEmailPage.emailInput, TestData.secondContactEmail)
      FirstContactEmailPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("the amended name and email are correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.secondContactName)
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.secondContactEmail)

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
      sendKeys(FirstContactNamePage.nameInput, TestData.firstContactName)
      FirstContactNamePage.clickSubmissionButton()
      assertOnPage(FirstContactEmailPage)
      sendKeys(FirstContactEmailPage.emailInput, TestData.firstContactEmail)
      FirstContactEmailPage.clickSubmissionButton()
      assertOnPage(AddAnotherContactPage)
      AddAnotherContactPage.clickNoRadioButton()
      AddAnotherContactPage.clickSubmissionButton()
      assertOnPage(SecondContactNamePage)
      sendKeys(SecondContactNamePage.nameInput, TestData.secondContactName)
      SecondContactNamePage.clickSubmissionButton()
      assertOnPage(SecondContactEmailPage)
      sendKeys(SecondContactEmailPage.emailInput, TestData.secondContactEmail)
      SecondContactEmailPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("both contacts details are correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.firstContactName)
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.firstContactEmail)
      assertTextOnPage(CheckYourAnswersPage.secondContactNameValue, TestData.secondContactName)
      assertTextOnPage(CheckYourAnswersPage.secondContactEmailValue, TestData.secondContactEmail)

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
      "Validate that contact details are required during registration",
      RegistrationUITests,
      ZapTests
    ) {
      Given(
        "an authenticated user lands on the first contact details page showing question 'What is the name of the person or team to keep on record?'"
      )
      RegistrationPage.clickEnterYourContactDetailsLink()
      ContactDetailsPage.clickSubmissionButton()
      assertOnPage(FirstContactNamePage)

      When("the user clicks 'Continue' without entering a name")
      FirstContactNamePage.clickSubmissionButton()

      Then("an error is shown")
      assertTextOnPage(FirstContactNamePage.errorTitle, "There is a problem")

      When("the user enters a valid name and clicks 'Continue'")
      sendKeys(FirstContactNamePage.nameInput, TestData.firstContactName)
      FirstContactNamePage.clickSubmissionButton()

      Then("the user is taken to the first contact details page showing question 'What is their email address?'")
      assertOnPage(FirstContactEmailPage)

      When("the user clicks 'Continue' without entering an email address")
      FirstContactEmailPage.clickSubmissionButton()

      Then("an error is shown")
      assertTextOnPage(FirstContactEmailPage.errorTitle, "There is a problem")

      When("the user enters a valid email and clicks 'Continue'")
      sendKeys(FirstContactEmailPage.emailInput, TestData.firstContactEmail)
      FirstContactEmailPage.clickSubmissionButton()

      Then("the user is taken to the 'Have you added all the contacts you need?' question page")
      assertOnPage(AddAnotherContactPage)

      When("the selects the 'Yes' radio button and clicks 'Continue'")
      AddAnotherContactPage.clickYesRadioButton()
      AddAnotherContactPage.clickSubmissionButton()

      Then("the given contact details are correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.firstContactName)
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.firstContactEmail)
    }

    Scenario(
      "When a user selects any 'Change' link and does not commit changes the resultant values on the 'Check Your Answers' page remain unchanged",
      RegistrationUITests,
      ZapTests
    ) {
      Given("an authenticated user completes company details and adds first and second contacts")
      RegistrationPage.clickEnterYourContactDetailsLink()
      assertOnPage(ContactDetailsPage)
      ContactDetailsPage.clickSubmissionButton()
      assertOnPage(FirstContactNamePage)
      sendKeys(FirstContactNamePage.nameInput, TestData.firstContactName)
      FirstContactNamePage.clickSubmissionButton()
      assertOnPage(FirstContactEmailPage)
      sendKeys(FirstContactEmailPage.emailInput, TestData.firstContactEmail)
      FirstContactEmailPage.clickSubmissionButton()
      assertOnPage(AddAnotherContactPage)
      AddAnotherContactPage.clickNoRadioButton()
      AddAnotherContactPage.clickSubmissionButton()
      assertOnPage(SecondContactNamePage)
      sendKeys(SecondContactNamePage.nameInput, TestData.secondContactName)
      SecondContactNamePage.clickSubmissionButton()
      assertOnPage(SecondContactEmailPage)
      sendKeys(SecondContactEmailPage.emailInput, TestData.secondContactEmail)
      SecondContactEmailPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      When("the user navigates to the first contact 'change name' page using the 'Change' link")
      CheckYourAnswersPage.clickFirstContactNameChangeLink()
      assertOnPage(FirstContactNamePage.changePageUrl)

      And("submits without changing the name")
      FirstContactNamePage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("the original first contact name is correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.firstContactName)

      When("the user navigates to the first contact 'change email address' page using the 'Change' link")
      CheckYourAnswersPage.clickFirstContactEmailChangeLink()
      assertOnPage(FirstContactEmailPage.changePageUrl)

      And("submits without changing the email")
      FirstContactEmailPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("the original first contact email address is correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.firstContactEmail)

      When("the user navigates to the second contact 'change name' page using the 'Change' link")
      CheckYourAnswersPage.clickSecondContactNameChangeLink()
      assertOnPage(SecondContactNamePage.changePageUrl)

      And("submits without changing the name")
      SecondContactNamePage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("the original second contact name is correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.secondContactNameValue, TestData.secondContactName)

      When("the user navigates to the second contact 'change email address' page using the 'Change' link")
      CheckYourAnswersPage.clickSecondContactEmailChangeLink()
      assertOnPage(SecondContactEmailPage.changePageUrl)

      And("submits without changing the email")
      SecondContactEmailPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("the original second contact email address is correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.secondContactEmailValue, TestData.secondContactEmail)
    }
  }
}
