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
import uk.gov.hmrc.test.ui.pages.grs.NominatedCompanyDetailsGuidancePage
import uk.gov.hmrc.test.ui.pages.registration.*
import uk.gov.hmrc.test.ui.pages.registration.GrsHost.GrsStubOnRegistrationFrontEnd
import uk.gov.hmrc.test.ui.specs.tags.*
import uk.gov.hmrc.test.ui.support.PageSupport.{assertOnPage, assertTextOnPage}
import uk.gov.hmrc.test.ui.support.TestData

class ContactDetailsSpec extends BaseSpec {

  override def beforeEach(): Unit = {
    super.beforeEach()
    FeatureTogglePage.setGrsHost(GrsStubOnRegistrationFrontEnd)
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
      HaveYouAddedAllContactsPage.clickYesRadioButton()
      HaveYouAddedAllContactsPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.firstPersonName)
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.firstPersonEmail)

      When("the user amends the first contact name using the 'Change' link")
      CheckYourAnswersPage.clickFirstContactNameChangeLink()
      assertOnPage(FirstContactNamePage.changePageUrl)
      FirstContactNamePage.addName(TestData.secondPersonName)
      FirstContactNamePage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      And("amends the first contact email using the 'Change' link")
      CheckYourAnswersPage.clickFirstContactEmailChangeLink()
      assertOnPage(FirstContactEmailPage.changePageUrl)
      FirstContactEmailPage.addEmail(TestData.secondPersonEmail)
      FirstContactEmailPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("the amended name and email are correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.secondPersonName)
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.secondPersonEmail)

      When("the user submits the contact details")
      CheckYourAnswersPage.clickSubmissionButton()
      assertOnPage(RegistrationPage)

      Then("the 'Enter your contact details' section status is 'Completed'")
      RegistrationPage.assertSectionStatus(ContactDetails, Completed)

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
      HaveYouAddedAllContactsPage.clickNoRadioButton()
      HaveYouAddedAllContactsPage.clickSubmissionButton()
      assertOnPage(SecondContactNamePage)
      AddSecondContactDetails()

      Then("both contacts details are correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.firstPersonName)
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.firstPersonEmail)
      assertTextOnPage(CheckYourAnswersPage.secondContactNameValue, TestData.secondPersonName)
      assertTextOnPage(CheckYourAnswersPage.secondContactEmailValue, TestData.secondPersonEmail)

      When("the user submits the contact details")
      CheckYourAnswersPage.clickSubmissionButton()
      assertOnPage(RegistrationPage)

      Then("the 'Enter your contact details' section status is 'Completed'")
      RegistrationPage.assertSectionStatus(ContactDetails, Completed)

      When("the user submits the registration")
      RegistrationPage.clickSubmissionButton()

      Then("a unique reference number is displayed on the 'Registration Complete' page")
      assertOnPage(RegistrationCompletePage)
      RegistrationCompletePage.assertReferenceNumberReturned()
    }

    Scenario(
      "Navigate to 'Account Homepage' after registration",
      RegistrationUITests,
      ZapTests
    ) {
      Given("a user has completed registration with a single contact")
      AddFirstContactDetails()
      HaveYouAddedAllContactsPage.clickYesRadioButton()
      HaveYouAddedAllContactsPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      CheckYourAnswersPage.clickSubmissionButton()
      assertOnPage(RegistrationPage)
      RegistrationPage.clickSubmissionButton()
      assertOnPage(RegistrationCompletePage)

      When("the user clicks the 'Continue' button on the 'Registration Complete' page")
      RegistrationCompletePage.clickSubmissionButton()

      Then("the user lands on the 'Account Homepage'")
      assertOnPage(AccountHomePage)
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
      FirstContactNamePage.assertErrorShownOnPage()

      When("the user enters a valid name and clicks 'Continue'")
      FirstContactNamePage.addName(TestData.firstPersonName)
      FirstContactNamePage.clickSubmissionButton()

      Then("the user is taken to the first contact details page showing question 'What is their email address?'")
      assertOnPage(FirstContactEmailPage)

      When("the user clicks 'Continue' without entering an email address")
      FirstContactEmailPage.clickSubmissionButton()

      Then("an error is shown")
      FirstContactEmailPage.assertErrorShownOnPage()

      When("the user enters a valid email and clicks 'Continue'")
      FirstContactEmailPage.addEmail(TestData.firstPersonEmail)
      FirstContactEmailPage.clickSubmissionButton()

      Then("the user is taken to the 'Have you added all the contacts you need?' question page")
      assertOnPage(HaveYouAddedAllContactsPage)

      When("the selects the 'No' radio button and clicks 'Continue'")
      HaveYouAddedAllContactsPage.clickNoRadioButton()
      HaveYouAddedAllContactsPage.clickSubmissionButton()

      And(
        "the user lands on the second contact details page showing question 'What is the name of the person or team to keep on record?'"
      )
      assertOnPage(SecondContactNamePage)

      When("the user clicks 'Continue' without entering a name")
      SecondContactNamePage.clickSubmissionButton()

      Then("an error is shown")
      SecondContactNamePage.assertErrorShownOnPage()

      When("the user enters a valid name and clicks 'Continue'")
      SecondContactNamePage.addName(TestData.secondPersonName)
      SecondContactNamePage.clickSubmissionButton()

      Then("the user is taken to the second contact details page showing question 'What is their email address?'")
      assertOnPage(SecondContactEmailPage)

      When("the user clicks 'Continue' without entering an email address")
      SecondContactEmailPage.clickSubmissionButton()

      Then("an error is shown")
      SecondContactEmailPage.assertErrorShownOnPage()

      When("the user enters a valid email and clicks 'Continue'")
      SecondContactEmailPage.addEmail(TestData.secondPersonEmail)
      SecondContactEmailPage.clickSubmissionButton()

      Then("both contacts details are correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.firstPersonName)
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.firstPersonEmail)
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
      HaveYouAddedAllContactsPage.clickNoRadioButton()
      HaveYouAddedAllContactsPage.clickSubmissionButton()
      assertOnPage(SecondContactNamePage)
      AddSecondContactDetails()

      When("the user navigates to the first contact 'change name' page using the 'Change' link")
      CheckYourAnswersPage.clickFirstContactNameChangeLink()
      assertOnPage(FirstContactNamePage.changePageUrl)

      And("submits without changing the name")
      FirstContactNamePage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("the original first contact name is correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.firstContactNameValue, TestData.firstPersonName)

      When("the user navigates to the first contact 'change email address' page using the 'Change' link")
      CheckYourAnswersPage.clickFirstContactEmailChangeLink()
      assertOnPage(FirstContactEmailPage.changePageUrl)

      And("submits without changing the email")
      FirstContactEmailPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("the original first contact email address is correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.firstContactEmailValue, TestData.firstPersonEmail)

      When("the user navigates to the second contact 'change name' page using the 'Change' link")
      CheckYourAnswersPage.clickSecondContactNameChangeLink()
      assertOnPage(SecondContactNamePage.changePageUrl)

      And("submits without changing the name")
      SecondContactNamePage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("the original second contact name is correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.secondContactNameValue, TestData.secondPersonName)

      When("the user navigates to the second contact 'change email address' page using the 'Change' link")
      CheckYourAnswersPage.clickSecondContactEmailChangeLink()
      assertOnPage(SecondContactEmailPage.changePageUrl)

      And("submits without changing the email")
      SecondContactEmailPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      Then("the original second contact email address is correctly displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.secondContactEmailValue, TestData.secondPersonEmail)
    }
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
    assertOnPage(HaveYouAddedAllContactsPage)
  }

  private def AddSecondContactDetails(): Unit = {
    SecondContactNamePage.addName(TestData.secondPersonName)
    SecondContactNamePage.clickSubmissionButton()
    assertOnPage(SecondContactEmailPage)
    SecondContactEmailPage.addEmail(TestData.secondPersonEmail)
    SecondContactEmailPage.clickSubmissionButton()
    assertOnPage(CheckYourAnswersPage)
  }
}
