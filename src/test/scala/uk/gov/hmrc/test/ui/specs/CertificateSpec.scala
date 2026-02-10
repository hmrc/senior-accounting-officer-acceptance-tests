/*
 * Copyright 2026 HM Revenue & Customs
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

import uk.gov.hmrc.test.ui.pages.submission.certificate.{CheckYourAnswersPage as CertificateCheckYourAnswersPage, *}
import uk.gov.hmrc.test.ui.pages.submission.notification.{CheckYourAnswersPage as NotificationCheckYourAnswersPage, *}
import uk.gov.hmrc.test.ui.pages.{AuthorityWizardPage, HubPage}
import uk.gov.hmrc.test.ui.specs.tags.{SoloTests, SubmissionUITests, ZapTests}
import uk.gov.hmrc.test.ui.support.AffinityGroup.Organisation
import uk.gov.hmrc.test.ui.support.PageSupport
import uk.gov.hmrc.test.ui.support.PageSupport.*

class CertificateSpec extends BaseSpec {

  Feature("Submit Certificate") {

    // this scenario to be used as part of the notification + certificate e2e journey; change no radio button to yes
    Scenario(
      "A user can submit a certificate successfully from the 'Hub' page",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user initiates a certificate submission from the 'Hub' page")
      navigateToCertificateStartPage()

      When("the user clicks 'Continue' on the 'Submit Certificate' start page")
      clickElement(submitButton)

      Then("the user is taken to the 'Is the given person the named SAO on the Certificate' question page")
      assertOnPage(IsThisTheSaoPage)

      When("the user selects the 'No' radio option and clicks 'Continue'")
      clickRadioElement(IsThisTheSaoPage.noRadioButton)
      clickElement(submitButton)

      Then("the user is taken to the 'SAO Name' page")
      assertOnPage(SaoNamePage)

      When("the user enters a valid SAO name and clicks 'Continue'")
      sendKeys(SaoNamePage.saoNameInput, "John Wick")
      clickElement(submitButton)

      Then("the user is taken to the 'SAO Email' page")
      assertOnPage(SaoEmailPage)

      When("the user enters a valid email and clicks 'Continue'")
      sendKeys(SaoEmailPage.saoEmailInput, "JohnWick@test.com")
      clickElement(submitButton)

      Then("the user is taken to the 'SAO Email Communication Choice' page")
      assertOnPage(SaoEmailCommunicationChoicePage)

      When("the user selects the 'No' radio option and clicks 'Continue'")
      clickRadioElement(SaoEmailCommunicationChoicePage.noRadioButton)
      clickElement(submitButton)

      Then("the user is taken to the 'Check Your Answers' page")
      assertOnPage(CertificateCheckYourAnswersPage)

      When("the user clicks 'Continue'")
      clickElement(submitButton)

      Then("the user is taken to the 'Who is submitting the certificate' question page")
      assertOnPage(SubmitCertificateSubmitterPage)

      When("the user selects 'I am authorised to submit the certificate on behalf of the Senior Accounting Officer'")
      clickRadioElement(SubmitCertificateSubmitterPage.saoProxySubmitterRadio)

      And("clicks 'Continue'")
      clickElement(submitButton)

      Then("the user is taken to the 'Companies with a qualified certificate' page")
      assertOnPage(QualifiedCompaniesPage)

      // Then("the given certificate reference number is successfully returned")
    }

    // TODO: (MA - 28/01) Update test to click the 'upload another submission template' link when it's provided
    Scenario(
      "The 'upload another submission template' link is displayed on the 'Submit Certificate Guidance' page",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user initiates submitting a certificate from the hub page")
      navigateToCertificateStartPage()

      Then("the 'upload another submission template' link is displayed on the 'Submit Certificate' start page")
      assertElementIsClickable(SubmitCertificateStartPage.uploadSubmissionTemplateLink)
    }

    Scenario(
      "Selecting the provided SAO as the submitter hides the 'Full name' row on the 'Check Your Answers' page, and changing this selection via the 'Change' link causes the row to appear",
      SubmissionUITests,
      ZapTests
    ) {
      Given("a user has chosen 'Yes' to select the provided SAO as the named person on the certificate")
      navigateToCertificateStartPage()
      clickElement(submitButton)
      assertOnPage(IsThisTheSaoPage)
      clickRadioElement(IsThisTheSaoPage.yesRadioButton)
      clickElement(submitButton)
      assertOnPage(SaoEmailPage)
      sendKeys(SaoEmailPage.saoEmailInput, "JohnWick@test.com")
      clickElement(submitButton)
      assertOnPage(SaoEmailCommunicationChoicePage)
      clickRadioElement(SaoEmailCommunicationChoicePage.noRadioButton)
      clickElement(submitButton)
      assertOnPage(CertificateCheckYourAnswersPage)

      And("the 'Full name' row is not shown on the 'Check Your Answers' page")
      assertElementNotVisible(CertificateCheckYourAnswersPage.fullNameKey)

      When("the user clicks the 'Change' link on the 'Is given person the named SAO on the certificate' row")
      clickElement(CertificateCheckYourAnswersPage.isThisTheSaoChangeLink)
      assertOnPage(IsThisTheSaoPage.changePageUrl)

      And("a new name is provided after changing the radio option to 'No'")
      clickRadioElement(IsThisTheSaoPage.noRadioButton)
      clickElement(submitButton)
      assertOnPage(SaoNamePage.changePageUrl)
      sendKeys(SaoNamePage.saoNameInput, "Bobby Brown")
      clickElement(submitButton)

      Then("the 'Check Your Answers' page is displayed with the 'Full name' row displayed with the newly added name")
      assertOnPage(CertificateCheckYourAnswersPage)
      assertTextOnPage(CertificateCheckYourAnswersPage.fullNameKey, "Full name")
      assertTextOnPage(CertificateCheckYourAnswersPage.fullNameValue, "Bobby Brown")
    }

    Scenario(
      "Selecting a new SAO name as the submitter reveals the 'Full name' row on the 'Check Your Answers' page, and changing this selection via the 'Change' link causes the row to be removed",
      SubmissionUITests,
      ZapTests
    ) {
      Given("a user has chosen 'No' and selects a new SAO as the named person on the certificate")
      navigateToCertificateStartPage()
      clickElement(submitButton)
      assertOnPage(IsThisTheSaoPage)
      clickRadioElement(IsThisTheSaoPage.noRadioButton)
      clickElement(submitButton)
      assertOnPage(SaoNamePage)
      sendKeys(SaoNamePage.saoNameInput, "Richer")
      clickElement(submitButton)
      assertOnPage(SaoEmailPage)
      sendKeys(SaoEmailPage.saoEmailInput, "Richer@test.com")
      clickElement(submitButton)
      assertOnPage(SaoEmailCommunicationChoicePage)
      clickRadioElement(SaoEmailCommunicationChoicePage.noRadioButton)
      clickElement(submitButton)
      assertOnPage(CertificateCheckYourAnswersPage)

      And("the 'Full name' row is shown on the 'Check Your Answers' page")
      assertTextOnPage(CertificateCheckYourAnswersPage.fullNameKey, "Full name")

      When("the user clicks the 'Change' link on the 'Is given person the named SAO on the certificate' row")
      clickElement(CertificateCheckYourAnswersPage.isThisTheSaoChangeLink)
      assertOnPage(IsThisTheSaoPage.changePageUrl)

      And("changes the radio option to 'Yes'")
      clickRadioElement(IsThisTheSaoPage.yesRadioButton)
      clickElement(submitButton)

      Then("the 'Check Your Answers' page is displayed with the 'Full name' row removed")
      assertOnPage(CertificateCheckYourAnswersPage)
      assertElementNotVisible(CertificateCheckYourAnswersPage.fullNameKey)
    }

    Scenario(
      "Selecting the 'Change' link to update an Email Address is successful",
      SubmissionUITests,
      ZapTests
    ) {
      Given("a user has landed on the 'Check your answers' page")
      navigateToCertificateStartPage()
      clickElement(submitButton)
      assertOnPage(IsThisTheSaoPage)
      clickRadioElement(IsThisTheSaoPage.yesRadioButton)
      clickElement(submitButton)
      assertOnPage(SaoEmailPage)
      sendKeys(SaoEmailPage.saoEmailInput, "Richer@test.com")
      clickElement(submitButton)
      assertOnPage(SaoEmailCommunicationChoicePage)
      clickRadioElement(SaoEmailCommunicationChoicePage.yesRadioButton)
      clickElement(submitButton)
      assertOnPage(CertificateCheckYourAnswersPage)

      When("the user clicks the 'Change' link on the 'Email address' row")
      clickElement(CertificateCheckYourAnswersPage.emailAddressChangeLink)
      assertOnPage(SaoEmailPage.changePageUrl)

      And("changes the email address to another valid value")
      sendKeys(SaoEmailPage.saoEmailInput, "John@test.com")
      clickElement(submitButton)

      Then("the 'Check Your Answers' page is displayed with the new email address")
      assertOnPage(CertificateCheckYourAnswersPage)
      assertTextOnPage(CertificateCheckYourAnswersPage.emailAddressValue, "John@test.com")
    }

    Scenario(
      "When a user selects any 'Change' link and does not commit changes the resultant values on the 'Check Your Answers' page remain unchanged",
      SubmissionUITests,
      ZapTests
    ) {
      Given("a user has landed on the 'Check your answers' page")
      navigateToCertificateStartPage()
      clickElement(submitButton)
      assertOnPage(IsThisTheSaoPage)
      clickRadioElement(IsThisTheSaoPage.noRadioButton)
      clickElement(submitButton)
      assertOnPage(SaoNamePage)
      sendKeys(SaoNamePage.saoNameInput, "Richer")
      clickElement(submitButton)
      assertOnPage(SaoEmailPage)
      sendKeys(SaoEmailPage.saoEmailInput, "Richer@test.com")
      clickElement(submitButton)
      assertOnPage(SaoEmailCommunicationChoicePage)
      clickRadioElement(SaoEmailCommunicationChoicePage.yesRadioButton)
      clickElement(submitButton)
      assertOnPage(CertificateCheckYourAnswersPage)

      When("the user clicks the 'Change' link on the 'Is given person the named SAO on the certificate' row")
      clickElement(CertificateCheckYourAnswersPage.isThisTheSaoChangeLink)
      assertOnPage(IsThisTheSaoPage.changePageUrl)

      And("the user makes no changes and clicks 'Continue'")
      clickElement(submitButton)

      When("the user lands on the 'sao name' page, makes no changes and clicks 'Continue'")
      assertOnPage(SaoNamePage.changePageUrl)
      clickElement(submitButton)

      Then(
        "the 'Check Your Answers' page is displayed with the value in the 'Is given person the named SAO on the certificate' row unchanged"
      )
      assertOnPage(CertificateCheckYourAnswersPage)
      assertTextOnPage(CertificateCheckYourAnswersPage.isThisTheSaoValue, "No")

      When("the user clicks the 'Change' link on the 'Full name' row")
      clickElement(CertificateCheckYourAnswersPage.fullNameChangeLink)
      assertOnPage(SaoNamePage.changePageUrl)

      And("the user makes no changes and clicks 'Continue'")
      clickElement(submitButton)

      Then(
        "the 'Check Your Answers' page is displayed with the value in the 'Full name' row unchanged"
      )
      assertOnPage(CertificateCheckYourAnswersPage)
      assertTextOnPage(CertificateCheckYourAnswersPage.fullNameValue, "Richer")

      When("the user clicks the 'Change' link on the 'Email address' row")
      clickElement(CertificateCheckYourAnswersPage.emailAddressChangeLink)
      assertOnPage(SaoEmailPage.changePageUrl)

      And("the user makes no changes and clicks 'Continue'")
      clickElement(submitButton)

      Then(
        "the 'Check Your Answers' page is displayed with the value in the 'Email address' row unchanged"
      )
      assertOnPage(CertificateCheckYourAnswersPage)
      assertTextOnPage(CertificateCheckYourAnswersPage.emailAddressValue, "Richer@test.com")

      When("the user clicks the 'Change' link on the 'Email communications' row")
      clickElement(CertificateCheckYourAnswersPage.emailCommunicationChoiceChangeLink)
      assertOnPage(SaoEmailCommunicationChoicePage.changePageUrl)

      And("the user makes no changes and clicks 'Continue'")
      clickElement(submitButton)

      Then(
        "the 'Check Your Answers' page is displayed with the value in the 'Email communications' row unchanged"
      )
      assertOnPage(CertificateCheckYourAnswersPage)
      assertTextOnPage(CertificateCheckYourAnswersPage.emailCommunicationChoiceValue, "Yes")
    }

    Scenario(
      "The user changes the answers when on the 'Check Your Answers' page",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'Check Your Answers' page and has entered 'SAO Name'")
      navigateToCertificateStartPage()
      clickElement(submitButton)
      assertOnPage(IsThisTheSaoPage)
      clickRadioElement(IsThisTheSaoPage.noRadioButton)
      clickElement(submitButton)
      assertOnPage(SaoNamePage)
      sendKeys(SaoNamePage.saoNameInput, "John Wick")
      clickElement(submitButton)
      assertOnPage(SaoEmailPage)
      sendKeys(SaoEmailPage.saoEmailInput, "JohnWick@test.com")
      clickElement(submitButton)
      assertOnPage(SaoEmailCommunicationChoicePage)
      clickRadioElement(SaoEmailCommunicationChoicePage.noRadioButton)
      clickElement(submitButton)
      assertOnPage(CertificateCheckYourAnswersPage)

      When("The user click change on the 'Full Name' row and are redirected to 'change SAO Name' page")
      clickElement(CertificateCheckYourAnswersPage.fullNameChangeLink)
      assertOnPage(SaoNamePage.changePageUrl)

      Then("The user enters a different name and can continue to 'Check Yours Answers' page")
      sendKeys(SaoNamePage.saoNameInput, "Test Name")
      clickElement(submitButton)
      assertOnPage(CertificateCheckYourAnswersPage)

      When("The user click change on the 'Is This The SAO' row and are redirected to 'change Is This The SAO' page")
      clickElement(CertificateCheckYourAnswersPage.isThisTheSaoChangeLink)
      assertOnPage(IsThisTheSaoPage.changePageUrl)

      Then(
        "The user select 'Yes' radio button and can continue to 'Check Yours Answers' page where the Full Name row is not displayed"
      )
      clickRadioElement(IsThisTheSaoPage.yesRadioButton)
      clickElement(submitButton)
      assertOnPage(CertificateCheckYourAnswersPage)
      assertElementNotVisible(CertificateCheckYourAnswersPage.fullNameChangeLink)

      When("The user click change on the 'Email Address' row and are redirected to 'change Email Address' page")
      clickElement(CertificateCheckYourAnswersPage.emailAddressChangeLink)
      assertOnPage(SaoEmailPage.changePageUrl)

      Then("The user enters a different email address and can continue to 'Check Yours Answers' page")
      sendKeys(SaoNamePage.saoNameInput, "TestName@test.com")
      clickElement(submitButton)
      assertOnPage(CertificateCheckYourAnswersPage)

      When(
        "The user click change on the 'Sao Email Communication Choice' row and are redirected to 'change Sao Email Communication Choice' page"
      )
      clickElement(CertificateCheckYourAnswersPage.emailCommunicationChoiceChangeLink)
      assertOnPage(SaoEmailCommunicationChoicePage.changePageUrl)

      Then("The user select 'Yes' radio button and can continue to 'Check Yours Answers' page")
      clickRadioElement(SaoEmailCommunicationChoicePage.yesRadioButton)
      clickElement(submitButton)
      assertOnPage(CertificateCheckYourAnswersPage)

      // press continue we are on submitter page
      Then("the user is taken to the 'submitter' page")
      // assertOnPage(CertificateSubmitterPage)

      // Then("the given certificate reference number is successfully returned")
    }

    // TODO: (MA - 28/01) This scenario will be absorbed into the notification and certificate happy path e-2-e journey
    Scenario(
      "On selecting 'Yes' when asked whether the given SAO is the correct SAO, the user is taken directly to the 'SAO Email' page",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'Is This The SAO' page")
      AuthorityWizardPage.withAffinityGroup(Organisation).redirectToHub()
      addNotificationFromHub()
      clickElement(submitButton)
      assertOnPage(SubmitCertificateStartPage)
      clickElement(submitButton)
      assertOnPage(IsThisTheSaoPage)

      When("the user selects the 'Yes' radio button and clicks 'Continue'")
      clickRadioElement(IsThisTheSaoPage.yesRadioButton)
      clickElement(submitButton)

      Then("the user lands on the 'SAO Email' page")
      assertOnPage(SaoEmailPage)
    }

    Scenario(
      "During a certificate submission errors are displayed when the user attempts to progress with invalid input",
      SubmissionUITests,
      ZapTests,
      SoloTests
    ) {
      Given("an authenticated user lands on the 'Is the given person the named SAO on the Certificate' question page")
      AuthorityWizardPage.withAffinityGroup(Organisation).redirectToHub()
      addNotificationFromHub()
      clickElement(submitButton)
      assertOnPage(SubmitCertificateStartPage)
      clickElement(submitButton)
      assertOnPage(IsThisTheSaoPage)

      When("the user clicks 'Continue' without selecting a radio option")
      clickElement(submitButton)

      Then("an error is shown")
      assertTextOnPage(IsThisTheSaoPage.errorTitle, "There is a problem")

      When("the user selects the 'No' radio option and clicks 'Continue'")
      clickRadioElement(IsThisTheSaoPage.noRadioButton)
      clickElement(submitButton)

      Then("the user is taken to the 'SAO Name' page")
      assertOnPage(SaoNamePage)

      When("the user clicks 'Continue' without entering an SAO name")
      clickElement(submitButton)

      Then("an error is shown")
      assertTextOnPage(SaoNamePage.errorTitle, "There is a problem")

      When("the user enters a valid name and clicks 'Continue'")
      sendKeys(SaoNamePage.saoNameInput, "John Wick")
      clickElement(submitButton)

      Then("the user is taken to the 'SAO Email' page")
      assertOnPage(SaoEmailPage)

      When("the user clicks 'Continue' without entering an SAO email")
      clickElement(submitButton)

      Then("an error is shown")
      assertTextOnPage(SaoEmailPage.errorTitle, "There is a problem")

      When("the user enters a valid email and clicks 'Continue'")
      sendKeys(SaoEmailPage.saoEmailInput, "JohnWick@test.com")
      clickElement(submitButton)

      Then("the user is taken to the 'SAO Email Communication Choice' page")
      assertOnPage(SaoEmailCommunicationChoicePage)

      When("the user clicks 'Continue' without selecting a radio option")
      clickElement(submitButton)

      Then("an error is shown")
      assertTextOnPage(SaoEmailCommunicationChoicePage.errorTitle, "There is a problem")

      When("the user selects the 'No' radio option and clicks 'Continue'")
      clickRadioElement(SaoEmailCommunicationChoicePage.noRadioButton)
      clickElement(submitButton)

      Then("the user is taken to the 'Check Your Answers' page")
      assertOnPage(CertificateCheckYourAnswersPage)

      When("the user clicks the 'Change' link on the 'Email address' row")
      clickElement(CertificateCheckYourAnswersPage.emailAddressChangeLink)
      assertOnPage(SaoEmailPage.changePageUrl)

      Then("the user removes the existing email and clicks continue, an error is shown")
      sendKeys(SaoEmailPage.saoEmailInput, "")
      clickElement(submitButton)
      assertTextOnPage(SaoEmailPage.errorTitle, "There is a problem")

      //TODO start from here
      
      When("the user clicks 'Continue'")
      clickElement(submitButton)

      Then("the user is taken to the 'Who is submitting the certificate' question page")
      assertOnPage(SubmitCertificateSubmitterPage)

      When("the user clicks 'Continue' without selecting a radio option")
      clickElement(submitButton)

      Then("an error is shown")
      assertTextOnPage(SubmitCertificateSubmitterPage.errorTitle, "There is a problem")

      When("the user selects 'I am authorised to submit the certificate on behalf of the Senior Accounting Officer'")
      clickRadioElement(SubmitCertificateSubmitterPage.saoProxySubmitterRadio)

      And("clicks 'Continue'")
      clickElement(submitButton)

      Then("the user is taken to the 'Companies with a qualified certificate' page")
      assertOnPage(QualifiedCompaniesPage)
    }
  }

  private def navigateToCertificateStartPage(): Unit = {
    AuthorityWizardPage.withAffinityGroup(Organisation).redirectToHub()
    assertOnPage(HubPage)

    // TODO: (MA - 26/01) Temporary workaround until data is available at this point in the journey.
    clickElement(HubPage.submitNotificationLink)
    assertOnPage(SubmitNotificationStartPage)
    driver.navigate().back()
    assertOnPage(HubPage)

    clickElement(HubPage.submitCertificateLink)
    assertOnPage(SubmitCertificateStartPage)
  }

  private def addNotificationFromHub(): Unit = {
    assertOnPage(HubPage)
    clickElement(HubPage.submitNotificationLink)
    assertOnPage(SubmitNotificationStartPage)
    clickElement(SubmitNotificationStartPage.submitNotificationLink)
    assertOnPage(GuidancePage)
    clickElement(submitButton)
    assertOnPage(AdditionalInformationPage)
    clickElement(AdditionalInformationPage.skipButton)
    assertOnPage(NotificationCheckYourAnswersPage)
    clickElement(submitButton)
    assertOnPage(SubmitPage)
    clickElement(SubmitPage.confirmAndSubmitButton)
    assertOnPage(ConfirmationPage)
    ConfirmationPage.assertReferenceNumberEquals("SAONOT0123456789")
  }
}
