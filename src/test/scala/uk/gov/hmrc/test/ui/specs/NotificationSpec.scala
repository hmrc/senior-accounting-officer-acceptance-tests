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

import uk.gov.hmrc.test.ui.adt.AffinityGroup.Organisation
import uk.gov.hmrc.test.ui.adt.NotificationTaskListSection.*
import uk.gov.hmrc.test.ui.adt.PageSectionStatus.*
import uk.gov.hmrc.test.ui.adt.UploadFile.*
import uk.gov.hmrc.test.ui.adt.ValidationError.*
import uk.gov.hmrc.test.ui.pages.submission.*
import uk.gov.hmrc.test.ui.pages.submission.notification.*
import uk.gov.hmrc.test.ui.pages.{AccountHomePage, AuthorityWizardPage}
import uk.gov.hmrc.test.ui.specs.tags.*
import uk.gov.hmrc.test.ui.support.InternalAuthorisationSupport.setupInternalAuthorisation
import uk.gov.hmrc.test.ui.support.PageSupport.*
import uk.gov.hmrc.test.ui.support.{PageSupport, TestData}

import java.time.LocalDate

class NotificationSpec extends BaseSpec {

  override def beforeEach(): Unit = {
    super.beforeEach()
    AuthorityWizardPage.withAffinityGroup(Organisation).withDsaoEnrolment(TestData.subscriptionId).redirectToHomePage()
    setupInternalAuthorisation
  }

  Feature("Submit Notification") {

    Scenario(
      "Notification task list shows the correct initial state",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the notification start page at the start of a new submission")
      assertOnPage(AccountHomePage)
      When("the user clicks the make a submission link")
      AccountHomePage.clickMakeSubmissionLink()
      Then("the user is on the submission type page")
      assertOnPage(SubmissionTypePage)
      When("the user clicks the notification radio button")
      SubmissionTypePage.clickNotificationRadioButton()
      And("the user clicks the continue button")
      SubmissionTypePage.clickSubmissionButton()
      Then("the user is on the submit a notification start page")
      assertOnPage(SubmitNotificationStartPage)

      And("the provide sao details status is 'Not Started'")
      SubmitNotificationStartPage.assertTaskListSectionStatus(ProvideSaoDetails, NotStarted)
      And("the provide sao details status is highlighted blue")
      SubmitNotificationStartPage.assertStatusHighlightedBlue(ProvideSaoDetails)
      And("the provide sao details link is active")
      SubmitNotificationStartPage.assertTaskListSectionNameIsHyperlink(ProvideSaoDetails)
      And("the upload submission template status is 'Cannot Start Yet'")
      SubmitNotificationStartPage.assertTaskListSectionStatus(UploadSubmissionTemplate, CannotStartYet)
      And("the upload submission template status is not highlighted")
      SubmitNotificationStartPage.assertStatusNotHighlighted(UploadSubmissionTemplate)
      And("the upload submission template link is not active")
      SubmitNotificationStartPage.assertTaskListSectionNameIsNotHyperlink(UploadSubmissionTemplate)
      And("the submit notification status is 'Cannot Start Yet'")
      SubmitNotificationStartPage.assertTaskListSectionStatus(SubmitNotification, CannotStartYet)
      And("the submit notification status is not highlighted")
      SubmitNotificationStartPage.assertStatusNotHighlighted(SubmitNotification)
      And("the submit notification link is not active")
      SubmitNotificationStartPage.assertTaskListSectionNameIsNotHyperlink(SubmitNotification)
    }

    Scenario(
      "Invalid certificate data in an upload file will not prevent a successful notification submission",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user provides details for a single SAO in a notification submission")
      assertOnPage(AccountHomePage)
      AccountHomePage.clickMakeSubmissionLink()
      Then("the user is on the submission type page")
      assertOnPage(SubmissionTypePage)
      When("the user clicks the notification radio button")
      SubmissionTypePage.clickNotificationRadioButton()
      And("the user clicks the continue button")
      SubmissionTypePage.clickSubmissionButton()
      Then("the user is on the submit a notification start page")
      assertOnPage(SubmitNotificationStartPage)

      provideSingleSaoDetailsFromStartPage(TestData.firstPersonName)

      When("the user clicks the upload submission template link")
      SubmitNotificationStartPage.clickTaskListSectionLink(UploadSubmissionTemplate)
      Then("the user is on the upload submission template page")
      assertOnPage(UploadSubmissionTemplatePage)
      When("the user uploads valid notification data but invalid certificate data")
      UploadSubmissionTemplatePage.upload(ValidNotificationDataWithInvalidCertificateDataFile)

      Then("the user lands on the 'Review the companies in your notification' page")
      assertOnPage(UploadTablePage)

      When("the 'Continue' button is clicked")
      UploadTablePage.clickSubmissionButton()

      Then("the user lands on the 'Submit a notification' start page")
      assertOnPage(SubmitNotificationStartPage)

      When("the 'submit a notification' task is completed with default selections")
      And("the user lands on the 'Submit a notification' complete page")
      completeSubmitANotificationFromTaskListWithDefaultSelections()

      And("the 'Go back to the homepage' button is clicked")
      SubmitNotificationCompletePage.clickSubmissionButton()

      Then("the user lands on the 'Account Homepage'")
      assertOnPage(AccountHomePage)
    }

    Scenario(
      "An upload notification data file exceeding the size limit is rejected",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user provides details for a single SAO in a notification submission")
      assertOnPage(AccountHomePage)
      When("the user clicks the continue button")
      AccountHomePage.clickMakeSubmissionLink()
      Then("the user is on the submission type page")
      assertOnPage(SubmissionTypePage)
      When("the user clicks the notification radio button")
      SubmissionTypePage.clickNotificationRadioButton()
      And("the user clicks the continue button")
      SubmissionTypePage.clickSubmissionButton()
      Then("the user is on the submit notification start page")
      assertOnPage(SubmitNotificationStartPage)

      provideSingleSaoDetailsFromStartPage(TestData.firstPersonName)

      When("The user clicks on the upload submission template link")
      SubmitNotificationStartPage.clickTaskListSectionLink(UploadSubmissionTemplate)
      Then("the user is on the upload submission template page")
      assertOnPage(UploadSubmissionTemplatePage)

      When("the user uploads a file that is too large")
      UploadSubmissionTemplatePage.upload(TooLargeDataFile)
      And("the file size exceeded error is displayed")
      UploadSubmissionTemplatePage.assertValidationErrorDisplayed(FileSizeExceededError)
    }

    Scenario(
      "A user can submit a notification successfully when additional information is added and not changed",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user initiates adding a notification from the 'Hub' page")
      goToAdditionalInformationPageFromHomePage()

      When("additional information is added")
      AdditionalInformationPage.addInformation("Test")
      And("the user clicks the continue button")
      AdditionalInformationPage.clickSubmissionButton()
      Then("the user is on the conform notification page")
      assertOnPage(ConfirmNotificationPage)

      When("the user clicks the continue button")
      ConfirmNotificationPage.clickSubmissionButton()
      Then("the user is on the check your answers page")
      assertOnPage(CheckYourAnswersPage)
      And("there is a value of the additional information the usr provided")
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Test")

      And("the user confirms their answers by clicking 'Continue'")
      CheckYourAnswersPage.clickSubmissionButton()

      Then("the user lands on the 'Confirmation' page")
      assertOnPage(ConfirmationPage)

      And("a unique reference number is displayed on screen")
      ConfirmationPage.assertReferenceNumberEquals(ConfirmationPage.notificationReference)

      And("the expected 'download a pdf' link is present")
      ConfirmationPage.assertLinkHasTextOnPage(ConfirmationPage.downloadPdfLink, "download a PDF")
      And("the expected 'print this page' link is present")
      ConfirmationPage.assertLinkHasTextOnPage(ConfirmationPage.printPageLink, "print this page")

      And("the 'download a pdf' link targets the notification pdf download route")
      ConfirmationPage.assertDownloadPdfLinkTargetsPdf()
    }

    Scenario(
      "Error is displayed when additional information exceeds the character limit",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user is on the Additional Information page during a notification submission")
      goToAdditionalInformationPageFromHomePage()

      When("the user adds additional information with an exceeded character limit")
      AdditionalInformationPage.addInformation(TestData.additionalInformationLimitExceeded)
      And("the user clicks continue")
      AdditionalInformationPage.clickSubmissionButton()
      Then("the use stays on the additional information page")
      assertPageWithError(AdditionalInformationPage)
      And("an error message is displayed")
      AdditionalInformationPage.assertValidationErrorDisplayed(NotificationAdditionalInformationTooLongError)
    }

    Scenario(
      "When continuing with no additional information an error presents and is cleared on populating additional information and pressing continue",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user arrives on the additional information page during a notification submission")
      goToAdditionalInformationPageFromHomePage()

      When("the user presses continue without providing additional information")
      AdditionalInformationPage.clickSubmissionButton()
      Then("the user is kept on the additional information page")
      assertPageWithError(AdditionalInformationPage)

      And("an error appears on screen")
      AdditionalInformationPage.assertValidationErrorDisplayed(MissingNotificationAdditionalInformationError)

      When("the user adds additional information")
      AdditionalInformationPage.addInformation("Test")
      And("the user clicks the continue button")
      AdditionalInformationPage.clickSubmissionButton()
      Then("the user is on the 'Confirm Notification' page")
      assertOnPage(ConfirmNotificationPage)
      When("the user clicks the continue button")
      ConfirmNotificationPage.clickSubmissionButton()
      Then("the user is on the 'Check Your Answers' page")
      assertOnPage(CheckYourAnswersPage)
      And("the additional information section contains the text the user provided")
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Test")
    }

    Scenario(
      "When continuing with no additional information an error presents and is cleared on pressing 'Skip'",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user arrives on the additional information page during a notification submission")
      goToAdditionalInformationPageFromHomePage()

      When("the user clicks 'Continue' without providing additional information")
      AdditionalInformationPage.clickSubmissionButton()
      Then("the user is kept on the additional information page")
      assertPageWithError(AdditionalInformationPage)

      And("an error appears on screen")
      AdditionalInformationPage.assertValidationErrorDisplayed(MissingNotificationAdditionalInformationError)

      When("the user clicks the skip button")
      AdditionalInformationPage.clickSkipButton()
      Then("the user is on the 'Confirm Notification' page")
      assertOnPage(ConfirmNotificationPage)
      When("the user clicks the continue button")
      ConfirmNotificationPage.clickSubmissionButton()
      Then("the user is on the 'Check Your Answers' page")
      assertOnPage(CheckYourAnswersPage)
      And("there is no provided text on the additional information section")
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Not provided")
    }

    Scenario(
      "When pressing 'Skip' with no additional information, no text is displayed on the 'Check Your Answers' page",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user arrives on the 'Additional Information' page during a notification submission")
      goToAdditionalInformationPageFromHomePage()

      When("the user clicks the skip button")
      AdditionalInformationPage.clickSkipButton()
      Then("the user is on the 'Confirm Notification' page")
      assertOnPage(ConfirmNotificationPage)

      When("the user clicks the continue button")
      ConfirmNotificationPage.clickSubmissionButton()
      Then("the user is on the 'Check Your Answers' page")
      assertOnPage(CheckYourAnswersPage)
      And("the page doesn't provide additional information")
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Not provided")
    }

    Scenario(
      "When pressing 'Skip' with additional information added, no text is displayed on the 'Check Your Answers' page",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user arrives on the 'Additional Information' page during a notification submission")
      goToAdditionalInformationPageFromHomePage()

      When("the user adds additional information")
      AdditionalInformationPage.addInformation("Test")

      And("the user clicks the skip button")
      AdditionalInformationPage.clickSkipButton()

      Then("the user is on the 'Confirm Notification' page")
      assertOnPage(ConfirmNotificationPage)

      When("the user clicks the continue button")
      ConfirmNotificationPage.clickSubmissionButton()

      Then("the user is on the 'Check Your Answers' page")
      assertOnPage(CheckYourAnswersPage)

      And("'Not provided' is displayed for additional information")
      assertTextOnPage(
        CheckYourAnswersPage.additionalInformationValueElement,
        "Not provided"
      )
    }

    Scenario(
      "When selecting to change additional information from the 'Check Your Answers' page the changes are persisted",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user is on the additional information page during a notification submission")
      goToAdditionalInformationPageFromHomePage()

      When("the user adds additional information")
      AdditionalInformationPage.addInformation("Test")

      And("the user clicks the continue button")
      AdditionalInformationPage.clickSubmissionButton()

      Then("the user is on the 'Confirm Notification' page")
      assertOnPage(ConfirmNotificationPage)

      When("the user clicks the continue button")
      ConfirmNotificationPage.clickSubmissionButton()

      Then("the user is on the 'Check Your Answers' page")
      assertOnPage(CheckYourAnswersPage)

      And("the provided additional information is displayed")
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Test")

      When("the user clicks the change link for additional information")
      CheckYourAnswersPage.clickAdditionalInformationChangeLink()

      Then("the user is on the additional information change page")
      assertUrl(AdditionalInformationPage.changePageUrl)

      When("the user updates the additional information")
      AdditionalInformationPage.addInformation("New Test For Changed Text")

      And("the user clicks the continue button")
      AdditionalInformationPage.clickSubmissionButton()

      Then("the user is returned to the 'Check Your Answers' page")
      assertOnPage(CheckYourAnswersPage)

      And("the updated additional information is displayed")
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "New Test For Changed Text")
    }

    Scenario(
      "When selecting to change additional information from the 'Check Your Answers' page and when 'Skip' is pressed then the changes are not persisted",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user is on the additional information page during a notification submission")
      goToAdditionalInformationPageFromHomePage()

      When("the user adds additional information")
      AdditionalInformationPage.addInformation("Test")

      And("the user clicks the continue button")
      AdditionalInformationPage.clickSubmissionButton()

      Then("the user is on the 'Confirm Notification' page")
      assertOnPage(ConfirmNotificationPage)

      When("the user clicks the continue button")
      ConfirmNotificationPage.clickSubmissionButton()

      Then("the user is on the 'Check Your Answers' page")
      assertOnPage(CheckYourAnswersPage)

      And("the provided additional information is displayed")
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Test")

      When("the user clicks the change link for additional information")
      CheckYourAnswersPage.clickAdditionalInformationChangeLink()

      Then("the user is on the additional information change page")
      assertUrl(AdditionalInformationPage.changePageUrl)

      When("the user updates the additional information")
      AdditionalInformationPage.addInformation("New Test For Changed Text")

      And("the user clicks the skip button")
      AdditionalInformationPage.clickSkipButton()

      Then("the user is returned to the 'Check Your Answers' page")
      assertOnPage(CheckYourAnswersPage)

      And("the additional information is shown as 'Not provided'")
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Not provided")
    }

    Scenario(
      "When the existing additional information text is removed, post clicking the change link from 'CYA' page, then the error message is displayed",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user is on the additional information page during a notification submission")
      goToAdditionalInformationPageFromHomePage()

      When("the user adds additional information")
      AdditionalInformationPage.addInformation("Test")

      And("the user clicks the continue button")
      AdditionalInformationPage.clickSubmissionButton()

      Then("the user is on the 'Confirm Notification' page")
      assertOnPage(ConfirmNotificationPage)

      When("the user clicks the continue button")
      ConfirmNotificationPage.clickSubmissionButton()

      Then("the user is on the 'Check Your Answers' page")
      assertOnPage(CheckYourAnswersPage)

      And("the provided additional information is displayed")
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Test")

      When("the user clicks the change link for additional information")
      CheckYourAnswersPage.clickAdditionalInformationChangeLink()

      Then("the user is on the additional information change page")
      assertUrl(AdditionalInformationPage.changePageUrl)

      When("the user clears the additional information")
      AdditionalInformationPage.addInformation("")

      And("the user clicks the continue button")
      AdditionalInformationPage.clickSubmissionButton()

      Then("an error is displayed")
      AdditionalInformationPage.assertValidationErrorDisplayed(MissingNotificationAdditionalInformationError)
    }

    Scenario(
      "When selecting to change SAO name from the 'Check Your Answers' page during a notification submission",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user is on the additional information page during a notification submission")
      goToAdditionalInformationPageFromHomePage()

      When("the user clicks the skip button")
      AdditionalInformationPage.clickSkipButton()

      Then("the user is on the 'Confirm Notification' page")
      assertOnPage(ConfirmNotificationPage)

      When("the user clicks the continue button")
      ConfirmNotificationPage.clickSubmissionButton()

      Then("the user is on the 'Check Your Answers' page")
      assertOnPage(CheckYourAnswersPage)

      When("the user clicks the change link for the SAO name")
      CheckYourAnswersPage.clickSaoNameChangeLink()

      Then("the user is on the SAO name change page")
      assertUrl(SingleSaoNamePage.changePageUrl)

      When("the user updates the SAO name")
      SingleSaoNamePage.addName("Jade Dancing")

      And("the user clicks the continue button")
      SingleSaoNamePage.clickSubmissionButton()

      Then("the user is returned to the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.saoNameValueElement, "Jade Dancing")

      When("the user clicks the continue button")
      CheckYourAnswersPage.clickSubmissionButton()

      Then("the user lands on the 'Confirmation' page")
      assertOnPage(ConfirmationPage)
    }

    Scenario(
      "When changing the SAO name from the 'Check Your Answers' page during a notification submission, without making any changes to the name",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user is on the additional information page during a notification submission")
      goToAdditionalInformationPageFromHomePage()

      When("the user clicks the skip button")
      AdditionalInformationPage.clickSkipButton()

      Then("the user is on the 'Confirm Notification' page")
      assertOnPage(ConfirmNotificationPage)

      When("the user clicks the continue button")
      ConfirmNotificationPage.clickSubmissionButton()

      Then("the user is on the 'Check Your Answers' page")
      assertOnPage(CheckYourAnswersPage)

      When("the user clicks the change link for the SAO name")
      CheckYourAnswersPage.clickSaoNameChangeLink()

      Then("the user is on the SAO name change page")
      assertUrl(SingleSaoNamePage.changePageUrl)

      When("the user clicks the continue button")
      SingleSaoNamePage.clickSubmissionButton()

      Then("the original SAO name is displayed on the 'Check Your Answers' page")
      assertTextOnPage(
        CheckYourAnswersPage.saoNameValueElement,
        "Cat Noir"
      )

      When("the user clicks the continue button")
      CheckYourAnswersPage.clickSubmissionButton()

      Then("the user lands on the 'Confirmation' page")
      assertOnPage(ConfirmationPage)
    }

    Scenario(
      "Complete a notification providing details for a single SAO in the financial year",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'More than one SAO' page")
      goToMoreThanOneSaoPageFromHomePage()

      When("the 'Continue' button is clicked after the 'No' radio button is selected")
      MoreThanOneSaoPage.clickNoRadioButton()

      And("the user clicks the continue button")
      MoreThanOneSaoPage.clickSubmissionButton()

      Then("the user lands on the 'What is the name of the SAO' page")
      assertOnPage(SingleSaoNamePage)

      When("the 'Continue' button is clicked after an SAO name is entered")
      SingleSaoNamePage.addName("Jane Doe")
      And("the user clicks the continue button")
      SingleSaoNamePage.clickSubmissionButton()

      Then("the user lands on the 'Submit a notification' start page")
      assertOnPage(SubmitNotificationStartPage)

      And("the 'Provide SAO Details' status is Completed")
      SubmitNotificationStartPage.assertTaskListSectionStatus(ProvideSaoDetails, Completed)
      And("the 'Provide SAO Details' status is not highlighted")
      SubmitNotificationStartPage.assertStatusNotHighlighted(ProvideSaoDetails)
      And("the 'Provide SAO Details' link is a hyperlink")
      SubmitNotificationStartPage.assertTaskListSectionNameIsHyperlink(ProvideSaoDetails)
      And("the 'Upload Submission Template' status is Not Started")
      SubmitNotificationStartPage.assertTaskListSectionStatus(UploadSubmissionTemplate, NotStarted)
      And("the 'Upload Submission Template' status is highlighted blue")
      SubmitNotificationStartPage.assertStatusHighlightedBlue(UploadSubmissionTemplate)
      And("the 'Upload Submission Template' link is a hyperlink")
      SubmitNotificationStartPage.assertTaskListSectionNameIsHyperlink(UploadSubmissionTemplate)
      And("the 'Submit Notification' status is Cannot Start Yet")
      SubmitNotificationStartPage.assertTaskListSectionStatus(SubmitNotification, CannotStartYet)
      And("the 'Submit Notification' status is not highlighted")
      SubmitNotificationStartPage.assertStatusNotHighlighted(SubmitNotification)
      And("the 'Submit Notification' section name is not a hyperlink")
      SubmitNotificationStartPage.assertTaskListSectionNameIsNotHyperlink(SubmitNotification)

      When("the 'Upload the submission template' link is clicked")
      SubmitNotificationStartPage.clickTaskListSectionLink(UploadSubmissionTemplate)

      Then("the user lands on the 'Upload a submission template' page")
      assertOnPage(UploadSubmissionTemplatePage)

      When("the 'Continue' button is clicked after choosing a valid file for upload")
      UploadSubmissionTemplatePage.upload(FourCompaniesFile)

      Then("the user lands on the 'Review the companies in your notification' page")
      assertOnPage(UploadTablePage)

      When("the 'Continue' button is clicked")
      UploadTablePage.clickSubmissionButton()

      Then("the user lands on the 'Submit a notification' start page")
      assertOnPage(SubmitNotificationStartPage)

      And("the 'Provide SAO Details' status is Completed")
      SubmitNotificationStartPage.assertTaskListSectionStatus(ProvideSaoDetails, Completed)
      And("the 'Provide SAO Details' status is not highlighted")
      SubmitNotificationStartPage.assertStatusNotHighlighted(ProvideSaoDetails)
      And("the 'Provide SAO Details' link is a hyperlink")
      SubmitNotificationStartPage.assertTaskListSectionNameIsHyperlink(ProvideSaoDetails)
      And("the 'Upload Submission Template' status is Completed")
      SubmitNotificationStartPage.assertTaskListSectionStatus(UploadSubmissionTemplate, Completed)
      And("the 'Upload Submission Template' status is not highlighted")
      SubmitNotificationStartPage.assertStatusNotHighlighted(UploadSubmissionTemplate)
      And("the 'Upload Submission Template' link is a hyperlink")
      SubmitNotificationStartPage.assertTaskListSectionNameIsHyperlink(UploadSubmissionTemplate)
      And("the 'Submit Notification' status is Not Started")
      SubmitNotificationStartPage.assertTaskListSectionStatus(SubmitNotification, NotStarted)
      And("the 'Submit Notification' status is highlighted blue")
      SubmitNotificationStartPage.assertStatusHighlightedBlue(SubmitNotification)
      And("the 'Submit Notification' section name is a hyperlink")
      SubmitNotificationStartPage.assertTaskListSectionNameIsHyperlink(SubmitNotification)

      When("the 'submit a notification' task is completed with default selections")
      And("the user lands on the 'Submit a notification' complete page")
      completeSubmitANotificationFromTaskListWithDefaultSelections()

      And("the 'Provide SAO Details' status is Completed")
      SubmitNotificationCompletePage.assertTaskListSectionStatus(ProvideSaoDetails, Completed)
      And("the 'Provide SAO Details' status is not highlighted")
      SubmitNotificationCompletePage.assertStatusNotHighlighted(ProvideSaoDetails)
      And("the 'Provide SAO Details' link is not a hyperlink")
      SubmitNotificationCompletePage.assertTaskListSectionNameIsNotHyperlink(ProvideSaoDetails)
      And("the 'Upload Submission Template' status is Completed")
      SubmitNotificationCompletePage.assertTaskListSectionStatus(UploadSubmissionTemplate, Completed)
      And("the 'Upload Submission Template' status is not highlighted")
      SubmitNotificationCompletePage.assertStatusNotHighlighted(UploadSubmissionTemplate)
      And("the 'Upload Submission Template' link is not a hyperlink")
      SubmitNotificationCompletePage.assertTaskListSectionNameIsNotHyperlink(UploadSubmissionTemplate)
      And("the 'Submit Notification' status is Completed")
      SubmitNotificationCompletePage.assertTaskListSectionStatus(SubmitNotification, Completed)
      And("the 'Submit Notification' status is not highlighted")
      SubmitNotificationCompletePage.assertStatusNotHighlighted(SubmitNotification)
      And("the 'Submit Notification' section name is not a hyperlink")
      SubmitNotificationCompletePage.assertTaskListSectionNameIsNotHyperlink(SubmitNotification)

      When("the 'Go back to the homepage' button is clicked")
      SubmitNotificationCompletePage.clickSubmissionButton()

      Then("the user lands on the 'Account Homepage'")
      assertOnPage(AccountHomePage)
    }

    Scenario(
      "Access template guidance and the 'Download a submission template' link in the notification submission journey",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'Upload a submission template' page during a notification submission")
      goToMoreThanOneSaoPageFromHomePage()

      When("the user clicks the no radio button")
      MoreThanOneSaoPage.clickNoRadioButton()
      And("the user clicks the continue button")
      MoreThanOneSaoPage.clickSubmissionButton()
      Then("the user is on the 'Single SAO name' page")
      assertOnPage(SingleSaoNamePage)
      When("the user adds a name")
      SingleSaoNamePage.addName(TestData.firstPersonName)
      And("the user clicks the continue button")
      SingleSaoNamePage.clickSubmissionButton()
      Then("the user is on the 'Submit Notification Start' page")
      assertOnPage(SubmitNotificationStartPage)
      When("the user clicks the upload submission template link")
      SubmitNotificationStartPage.clickTaskListSectionLink(UploadSubmissionTemplate)
      Then("the user is on the upload submission template page")
      assertOnPage(UploadSubmissionTemplatePage)
      And("the guidance link found has the correct attributes")
      UploadSubmissionTemplatePage.assertTemplateGuidanceLinkFoundWithCorrectAttributes()

      When("the 'Read guidance on how to complete the submission template (opens in new tab)' link is clicked")
      UploadSubmissionTemplatePage.clickReadGuidanceLink()

      Then("the user switches tab to the 'How to complete and submit your submission template' page")
      switchTab(1)
      And("the user is on the 'How to complete and submit your submission template' page")
      assertUrl(SubmissionTemplateGuidancePage.pageUrlForNewTab)
      And("the page title for the page is correct")
      assertPageTitle(SubmissionTemplateGuidancePage.pageTitle)

      And("the 'Download a submission template' link is present with the correct path")
      SubmissionTemplateGuidancePage.assertDownloadSubmissionTemplateLinkFound()

      When("the user closes the active tab")
      closeCurrentTab()
      Then("the user is back on the 'Upload a submission template' page")
      assertOnPage(UploadSubmissionTemplatePage)

      And("the 'Continue' button is clicked after choosing a file with 'no information' in the upload file")
      UploadSubmissionTemplatePage.upload(NoInformationFile)

      Then("the user lands on the 'There is a problem with your submission template file' with no information error page")
      assertOnPage(UploadTemplateErrorPage)
      UploadTemplateErrorPage.assertParagraphDescribesNoInformationTemplateError()

      When("the 'Download a submission template and read guidance on how to complete it (opens in new tab)' link is clicked")
      UploadTemplateErrorPage.clickDownloadTemplateAndReadGuidanceLink()

      Then("the user switches tab to the 'How to complete and submit your submission template' page")
      switchTab(1)
      And("the user is on the 'How to complete and submit your submission template' page")
      assertUrl(SubmissionTemplateGuidancePage.pageUrlForNewTab)
      And("the page title for the page is correct")
      assertPageTitle(SubmissionTemplateGuidancePage.pageTitle)

      And("the 'Download a submission template' link is present with the correct path")
      SubmissionTemplateGuidancePage.assertDownloadSubmissionTemplateLinkFound()

      When("the user closes the active tab")
      closeCurrentTab()
      Then("the user is back on the 'Upload a submission template' page")
      assertOnPage(UploadTemplateErrorPage)
      And("there is a no information template error")
      UploadTemplateErrorPage.assertParagraphDescribesNoInformationTemplateError()

      When("the 'Upload a submission template' button is clicked")
      UploadTemplateErrorPage.clickSubmissionButton()
      Then("the user lands on the 'Upload a submission template' page")
      assertOnPage(UploadSubmissionTemplatePage)

      When("the 'Continue' button is clicked after choosing a file with 'invalid notification data' in the upload file")
      UploadSubmissionTemplatePage.upload(InvalidNotificationDataFile)

      Then("the user lands on the 'There is a problem with your submission template file' page")
      assertOnPage(UploadTemplateErrorPage)
      And("the page describes template data errors relating to 'invalid notification data'")
      UploadTemplateErrorPage.assertParagraphDescribesTemplateDataErrors()

      When("the 'Download a submission template and read guidance on how to complete it (opens in new tab)' link is clicked")
      UploadTemplateErrorPage.clickDownloadTemplateAndReadGuidanceLink()

      Then("the user switches tab")
      switchTab(1)
      And("the user is on a new tab for the submission template guidance page")
      assertUrl(SubmissionTemplateGuidancePage.pageUrlForNewTab)
      And("the page title is correct for the submission template guidance page")
      assertPageTitle(SubmissionTemplateGuidancePage.pageTitle)
      And("the 'Download a submission template' link is present with the correct path")
      SubmissionTemplateGuidancePage.assertDownloadSubmissionTemplateLinkFound()
    }

    Scenario(
      "Review uploaded company details and upload an updated notification submission",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("a random example value of 'Emi A Ward' is chosen as the last SAO during a notification submission")
      goToMoreThanOneSaoPageFromHomePage()
      When("the user clicks the Yes radio button")
      MoreThanOneSaoPage.clickYesRadioButton()
      And("the user clicks the continue button")
      MoreThanOneSaoPage.clickSubmissionButton()
      Then("the user is on the 'Last SAO Name' page")
      assertOnPage(LastSaoNamePage)

      When("the user adds a name")
      LastSaoNamePage.addName("Emi A Ward")
      And("the user clicks the continue button")
      LastSaoNamePage.clickSubmissionButton()
      Then("the user is on the 'Last SAO start date' page")
      assertOnPage(LastSaoStartDatePage)

      When("the user adds a start date")
      LastSaoStartDatePage.addDate(LocalDate.now().minusDays(40))
      And("the user clicks the continue button")
      LastSaoStartDatePage.clickSubmissionButton()
      Then("the user is on the 'Previous SAO Name' page")
      assertOnPage(PreviousSaoNamePage)

      When("the user adds a previous SAO name")
      PreviousSaoNamePage.addName("Piet Van De Merwe")
      And("the user clicks the continue button")
      PreviousSaoNamePage.clickSubmissionButton()
      Then("the user is on the 'Previous SAO start page")
      assertOnPage(PreviousSaoStartDatePage)

      When("the user adds a previous SAO start date")
      PreviousSaoStartDatePage.addDate(LocalDate.now().minusDays(100))
      And("the user clicks the continue button")
      PreviousSaoStartDatePage.clickSubmissionButton()
      Then("the user is on the 'Previous SAO end date' page")
      assertOnPage(PreviousSaoEndDatePage)

      When("the user adds a previous end date")
      PreviousSaoEndDatePage.addDate(LocalDate.now().minusDays(47))
      And("the user clicks the continue button")
      PreviousSaoEndDatePage.clickSubmissionButton()
      Then("the user is on the 'Multiple SAO all added' page")
      assertOnPage(MultiSaoAreAllAddedPage)

      When("the user clicks the Yes radio button")
      MultiSaoAreAllAddedPage.clickYesRadioButton()
      And("the user clicks the continue button")
      MultiSaoAreAllAddedPage.clickSubmissionButton()
      Then("The user is on the 'Submit Notification Start' page")
      assertOnPage(SubmitNotificationStartPage)

      When("the user clicks the 'Upload Submission Template' link")
      SubmitNotificationStartPage.clickTaskListSectionLink(UploadSubmissionTemplate)
      Then("the user is on the 'Upload Submission Template' page")
      assertOnPage(UploadSubmissionTemplatePage)

      When("the user uploads a file with no information")
      UploadSubmissionTemplatePage.upload(NoInformationFile)
      Then("the user lands on the 'There is a problem with your submission template file' with no information error page")
      assertOnPage(UploadTemplateErrorPage)
      And("there is a no information template error on the page")
      UploadTemplateErrorPage.assertParagraphDescribesNoInformationTemplateError()

      When("the 'Upload a submission template' button is clicked")
      UploadTemplateErrorPage.clickSubmissionButton()
      Then("the user lands on the 'Upload a submission template' page")
      assertOnPage(UploadSubmissionTemplatePage)

      When("the 'Continue' button is clicked after choosing a file with '4' companies in the upload file")
      UploadSubmissionTemplatePage.upload(FourCompaniesFile)
      Then("the user lands on the 'Review the companies in your notification' page")
      assertOnPage(UploadTablePage)
      And("The page displays the correct company count of '4' and 'Emi A Ward' as the responsible SAO")
      UploadTablePage.assertCompanyCountAndSaoMatch(companyCount = 4, expectedSao = "Emi A Ward")
      And("The table contains '4' rows of company details")
      UploadTablePage.assertCompanyCountInTableEquals(4)

      When("the 'Continue' button is clicked")
      ConfirmationPage.clickSubmissionButton()
      Then("the user lands on the 'Submit a notification' start page")
      assertOnPage(SubmitNotificationStartPage)
    }

    Scenario(
      "Attempting to upload unacceptable submission template files to Upscan returns the appropriate error",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user provides details for a single SAO in a notification submission")
      assertOnPage(AccountHomePage)
      When("the user clicks the continue button")
      AccountHomePage.clickMakeSubmissionLink()
      Then("the user is on the 'Submission Type' page")
      assertOnPage(SubmissionTypePage)
      When("the user clicks the Notification radio button")
      SubmissionTypePage.clickNotificationRadioButton()
      And("the user clicks the continue button")
      SubmissionTypePage.clickSubmissionButton()
      Then("the user is on the 'Submission Notification Start' page")
      assertOnPage(SubmitNotificationStartPage)

      provideSingleSaoDetailsFromStartPage(TestData.firstPersonName)

      When("the user clicks the 'Upload Submission Template' link")
      SubmitNotificationStartPage.clickTaskListSectionLink(UploadSubmissionTemplate)
      Then("the user is on the 'Upload Submission Template' page")
      assertOnPage(UploadSubmissionTemplatePage)

      When("the user uploads a template which has invalid data for a company")
      UploadSubmissionTemplatePage.upload(InvalidQualificationFile)
      Then("the expected error page displays")
      assertOnPage(UploadTableErrorPage)
      And("the page displays 2 errors associated with notification")
      UploadTableErrorPage.assertParagraphWithErrorCountMatches(errorCount = 2)

      When("the 'Return to file upload' button is clicked")
      UploadTableErrorPage.clickSubmissionButton()
      Then("the user is returned to the 'Upload a submission template' page")
      assertOnPage(UploadSubmissionTemplatePage)

      When("attempting to upload a submission template which is named 'invalid.REASON.csv'")
      UploadSubmissionTemplatePage.upload(InvalidTypeFile)
      Then("the appropriate error is shown on screen")
      UploadSubmissionTemplatePage.assertValidationErrorDisplayed(InvalidFileTypeError)

      When("attempting to upload a submission template which is named 'infected.VIRUS_NAME.csv'")
      UploadSubmissionTemplatePage.upload(InfectedFile)
      Then("the appropriate error is shown on screen")
      UploadSubmissionTemplatePage.assertValidationErrorDisplayed(InfectedFileError)

      When("attempting to upload a submission template which is named 'unknown.REASON.csv'")
      UploadSubmissionTemplatePage.upload(UnknownErrorFile)
      Then("the appropriate error is shown on screen")
      UploadSubmissionTemplatePage.assertValidationErrorDisplayed(UnknownUploadError)
    }

    Scenario(
      "Complete a notification providing multiple SAO's for the financial year",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'More than one SAO' page")
      goToMoreThanOneSaoPageFromHomePage()

      When("the 'Continue' button is clicked without selecting any options")
      MoreThanOneSaoPage.clickSubmissionButton()

      Then("error message 'Select yes, if the SAO changed during the financial year you are submitting for' is displayed")
      MoreThanOneSaoPage.assertValidationErrorDisplayed(NoOptionChosenIfSaoChangedError)

      When("the user clicks the Yes radio button")
      MoreThanOneSaoPage.clickYesRadioButton()
      And("the user clicks the continue button")
      MoreThanOneSaoPage.clickSubmissionButton()

      Then("the user lands on the 'What is the name of the last SAO' page")
      assertOnPage(LastSaoNamePage)

      When("the 'Continue' button is clicked after entering a name of 'Jack Sparrow'")
      LastSaoNamePage.addName("Jack Sparrow")
      LastSaoNamePage.clickSubmissionButton()

      Then("the user lands on the 'What date did Jack Sparrow become the SAO' page")
      assertOnPage(LastSaoStartDatePage)
      LastSaoStartDatePage.assertHeadingMatches("What date did Jack Sparrow become the SAO?")

      When("the 'Continue' button is clicked after adding a date 30 days in the past")
      LastSaoStartDatePage.addDate(LocalDate.now().minusDays(30))
      LastSaoStartDatePage.clickSubmissionButton()

      Then("the user lands on the 'Who was the SAO before Jack Sparrow' page")
      assertOnPage(PreviousSaoNamePage)

      And("the page displays the correct content")
      PreviousSaoNamePage.assertHeadingMatches("Who was the SAO before Jack Sparrow?")

      When("the 'Continue' button is clicked after entering a name of 'Gert Bo'")
      PreviousSaoNamePage.addName("Gert Bo")
      PreviousSaoNamePage.clickSubmissionButton()

      Then("the user lands on the 'When did Gert Bo’s responsibility as the SAO start' page")
      assertOnPage(PreviousSaoStartDatePage)
      PreviousSaoStartDatePage.assertHeadingMatches("When did Gert Bo become the SAO?")

      When("the 'Continue' button is clicked after adding a date 90 days in the past")
      PreviousSaoStartDatePage.addDate(LocalDate.now().minusDays(90))
      PreviousSaoStartDatePage.clickSubmissionButton()

      Then("the user lands on the 'When did Gert Bo stop being the SAO' page")
      assertOnPage(PreviousSaoEndDatePage)
      PreviousSaoEndDatePage.assertHeadingMatches("When did Gert Bo stop being the SAO?")

      When("the user enters an end date that is one day before the start date")
      PreviousSaoEndDatePage.addDate(LocalDate.now().minusDays(91))
      PreviousSaoEndDatePage.clickSubmissionButton()

      Then("an error message is displayed")
      PreviousSaoEndDatePage.assertValidationErrorDisplayed(PreviousSaoEndDateBeforeStartDateError)

      When("the 'Continue' button is clicked after adding a date 65 days in the past")
      PreviousSaoEndDatePage.addDate(LocalDate.now().minusDays(65))
      PreviousSaoEndDatePage.clickSubmissionButton()

      Then(
        "the user lands on the 'Have you added all the SAO for the financial year this notification relates to' page"
      )
      assertOnPage(MultiSaoAreAllAddedPage)

      When("the 'Continue' button is clicked after the 'No' radio button is selected")
      MultiSaoAreAllAddedPage.clickNoRadioButton()
      MultiSaoAreAllAddedPage.clickSubmissionButton()

      Then("the user lands on the 'Who was the SAO before Gert Bo' page")
      assertUrl(PreviousSaoNamePage.changePageUrl)
      PreviousSaoNamePage.assertHeadingMatches("Who was the SAO before Gert Bo?")

      When("the 'Continue' button is clicked after entering a name of 'Alex Rhodes'")
      PreviousSaoNamePage.addName("Alex Rhodes")
      PreviousSaoNamePage.clickSubmissionButton()

      Then("the user lands on the 'When did Alex Rhodes’s responsibility as the SAO start' page")
      assertUrl(PreviousSaoStartDatePage.changePageUrl)

      When("the 'Continue' button is clicked after adding a start date 90 days in the past for the new SAO")
      PreviousSaoStartDatePage.addDate(LocalDate.now().minusDays(90))
      PreviousSaoStartDatePage.clickSubmissionButton()

      Then("the user lands on the 'When did Alex Rhodes stop being the SAO' page")
      assertUrl(PreviousSaoEndDatePage.pageUrlWithSaoIndexOne)

      When("the 'Continue' button is clicked after adding an end date 70 days in the past for the new SAO")
      PreviousSaoEndDatePage.addDate(LocalDate.now().minusDays(70))
      PreviousSaoEndDatePage.clickSubmissionButton()

      Then(
        "the user lands on the 'Have you added all the SAO for the financial year this notification relates to' page"
      )
      assertUrl(MultiSaoAreAllAddedPage.changePageUrl)

      When("the 'Continue' button is clicked after the 'Yes' radio button is selected")
      MultiSaoAreAllAddedPage.clickYesRadioButton()
      MultiSaoAreAllAddedPage.clickSubmissionButton()

      Then("the user lands on the 'Submit a notification' start page")
      assertOnPage(SubmitNotificationStartPage)

      And("the 'Provide the SAO's details' option status is set as 'Completed'")
      SubmitNotificationStartPage.assertTaskListSectionStatus(ProvideSaoDetails, Completed)
    }

    Scenario(
      "Validate that SAO details are required for a notification submission given a single SAO in the financial year",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'More than one SAO' page")
      goToMoreThanOneSaoPageFromHomePage()

      When("the 'Continue' button is clicked without selecting any options")
      MoreThanOneSaoPage.clickSubmissionButton()

      Then(
        "an error message 'Select yes, if the SAO changed during the financial year you are submitting for' is displayed"
      )
      MoreThanOneSaoPage.assertValidationErrorDisplayed(NoOptionChosenIfSaoChangedError)

      When("the 'Continue' button is clicked after the 'No' radio button is selected")
      MoreThanOneSaoPage.clickNoRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()

      Then("the user lands on the 'What is the name of the SAO' page")
      assertOnPage(SingleSaoNamePage)

      When("the 'Continue' button is clicked after no SAO name was entered")
      SingleSaoNamePage.clickSubmissionButton()

      Then("the validation error for 'missing SAO name' is shown")
      SingleSaoNamePage.assertValidationErrorDisplayed(MissingSAONameError)

      When("the 'Continue' button is clicked after a SAO name is entered")
      SingleSaoNamePage.addName("Jane Doe")
      SingleSaoNamePage.clickSubmissionButton()

      Then("the user lands on the 'Submit a notification' start page")
      assertOnPage(SubmitNotificationStartPage)
    }

    Scenario(
      "Validate that SAO details are required for a notification submission given there are multiple SAO's in the financial year",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'More than one SAO' page")
      goToMoreThanOneSaoPageFromHomePage()

      When("the 'Continue' button is clicked after selecting 'Yes'")
      MoreThanOneSaoPage.clickYesRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()

      Then("the user lands on the 'What is the name of the SAO at the end of the financial year?' question page")
      assertOnPage(LastSaoNamePage)

      When("the 'Continue' button is clicked after no name is entered")
      LastSaoNamePage.clickSubmissionButton()

      Then("Then an error is shown ")
      LastSaoNamePage.assertValidationErrorDisplayed(MissingYearEndLastSAONameError)

      When("the 'Continue' button is clicked after entering a name of 'Jerry Hatrix'")
      LastSaoNamePage.addName("Jerry Hatrix")
      LastSaoNamePage.clickSubmissionButton()

      Then("the user lands on the 'What date did Jerry Hatrix become the SAO' page")
      assertOnPage(LastSaoStartDatePage)

      When("the 'Continue' button is clicked after no date is entered")
      LastSaoStartDatePage.clickSubmissionButton()

      Then("an error message is displayed")
      LastSaoStartDatePage.assertValidationErrorDisplayed(MissingLastSAOStartDateError)

      When("the 'Continue' button is clicked after adding a date 30 days in the past")
      LastSaoStartDatePage.addDate(LocalDate.now().minusDays(30))
      LastSaoStartDatePage.clickSubmissionButton()

      Then("the user lands on the 'Who was the SAO before Jerry Hatrix' question page")
      assertOnPage(PreviousSaoNamePage)

      When("the 'Continue' button is clicked after no name is entered")
      PreviousSaoNamePage.clickSubmissionButton()

      Then("an error message is displayed")
      PreviousSaoNamePage.assertValidationErrorDisplayed(MissingPreviousSAONameError)

      When("the 'Continue' button is clicked after entering a name for a preceding SAO of 'Jock B'")
      PreviousSaoNamePage.addName("Jock B")
      PreviousSaoNamePage.clickSubmissionButton()

      Then("the user lands on the 'When did Jock B’s responsibility as the SAO start' page")
      assertOnPage(PreviousSaoStartDatePage)

      When("the 'Continue' button is clicked after no date is entered")
      PreviousSaoStartDatePage.clickSubmissionButton()

      Then("an error message is displayed")
      PreviousSaoStartDatePage.assertValidationErrorDisplayed(MissingPreviousSAOStartDateError)

      When("the 'Continue' button is clicked after adding a date 45 days in the past")
      PreviousSaoStartDatePage.addDate(LocalDate.now().minusDays(45))
      PreviousSaoStartDatePage.clickSubmissionButton()

      Then("the user lands on the 'When did Jock B stop being the SAO' page")
      assertOnPage(PreviousSaoEndDatePage)
      PreviousSaoEndDatePage.assertHeadingMatches("When did Jock B stop being the SAO?")

      When("the 'Continue' button is clicked after no date is entered")
      PreviousSaoEndDatePage.clickSubmissionButton()

      Then("an error message is displayed")
      PreviousSaoEndDatePage.assertValidationErrorDisplayed(MissingPreviousSAOEndDateError)

      When("the 'Continue' button is clicked after adding a date 35 days in the past")
      LastSaoStartDatePage.addDate(LocalDate.now().minusDays(35))
      PreviousSaoEndDatePage.clickSubmissionButton()

      Then(
        "the user lands on the 'Have you added all the SAO for the financial year this notification relates to' page"
      )
      assertOnPage(MultiSaoAreAllAddedPage)

      When("the 'Continue' button is clicked without a radio button being selected")
      MultiSaoAreAllAddedPage.clickSubmissionButton()

      Then("an error appears on screen")
      MultiSaoAreAllAddedPage.assertValidationErrorDisplayed(NoElementChosenMultiSaoAreAllAddedError)

      When("the 'Continue' button is clicked after selecting 'Yes'")
      MultiSaoAreAllAddedPage.clickYesRadioButton()
      MultiSaoAreAllAddedPage.clickSubmissionButton()

      Then("the user lands on the 'Submit a notification' start page")
      assertOnPage(SubmitNotificationStartPage)
    }

    Scenario(
      "Validate that 'dynamic' SAO details are retained on selecting the 'back link' during a notification submission",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user has added 'Shane Warne' as the last SAO")
      goToWhoWasTheSAOBeforePage()

      And("has added 'Jonty Rhodes' as a prior SAO")
      PreviousSaoNamePage.addName("Jonty Rhodes")
      PreviousSaoNamePage.clickSubmissionButton()
      assertOnPage(PreviousSaoStartDatePage)
      PreviousSaoStartDatePage.addDate(LocalDate.now().minusDays(65))
      PreviousSaoStartDatePage.clickSubmissionButton()
      assertOnPage(PreviousSaoEndDatePage)
      PreviousSaoEndDatePage.addDate(LocalDate.now().minusDays(35))
      PreviousSaoEndDatePage.clickSubmissionButton()

      And("the user lands on the 'Have you added all the SAO for the financial year this notification relates to' page")
      assertOnPage(MultiSaoAreAllAddedPage)

      When("the 'Back' link is clicked")
      MultiSaoAreAllAddedPage.clickBackLink()

      Then("the user lands on the 'When did Jonty Rhodes stop being the SAO' page")
      assertOnPage(PreviousSaoEndDatePage)

      And("the page displays the correct content")
      PreviousSaoEndDatePage.assertHeadingMatches("When did Jonty Rhodes stop being the SAO?")

      When("the 'Back' link is clicked")
      PreviousSaoEndDatePage.clickBackLink()

      Then("the user lands on the 'When did Jonty Rhodes’s responsibility as the SAO start' page")
      assertOnPage(PreviousSaoStartDatePage)

      And("the page displays the correct content")
      PreviousSaoStartDatePage.assertHeadingMatches(
        "When did Jonty Rhodes become the SAO?"
      )

      When("the 'Back' link is clicked")
      PreviousSaoStartDatePage.clickBackLink()

      Then("the user lands on the 'Who was the SAO before Shane Warne' page")
      assertOnPage(PreviousSaoNamePage)

      And("the page displays the correct content")
      PreviousSaoNamePage.assertHeadingMatches("Who was the SAO before Shane Warne?")
      PreviousSaoNamePage.assertNameMatches("Jonty Rhodes")

      When("the 'Back' link is clicked")
      PreviousSaoNamePage.clickBackLink()

      Then("the user lands on the 'What date did Shane Warne become the SAO' page")
      assertOnPage(LastSaoStartDatePage)

      And("the page displays the correct content")
      LastSaoStartDatePage.assertHeadingMatches("What date did Shane Warne become the SAO?")
    }

    Scenario(
      "Validate error when SAO name with invalid characters",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given(
        "an authenticated user lands on the submit notification full name page"
      )
      goToMoreThanOneSaoPageFromHomePage()
      MoreThanOneSaoPage.clickNoRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()
      assertOnPage(SingleSaoNamePage)

      When("the user enter a SAO name with invalid characters")
      SingleSaoNamePage.addName(TestData.nameWithInvalidCharacters)
      SingleSaoNamePage.clickSubmissionButton()

      Then("an error is shown")
      SingleSaoNamePage.assertValidationErrorDisplayed(InvalidSAONameCharactersError)
    }

    Scenario(
      "Validate error when SAO name exceeds 105 characters",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given(
        "an authenticated user lands on the submit notification full name page"
      )
      goToMoreThanOneSaoPageFromHomePage()
      MoreThanOneSaoPage.clickNoRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()
      assertOnPage(SingleSaoNamePage)

      When("the user enter a SAO name containing more than 105 characters")
      SingleSaoNamePage.addName(TestData.nameCharacterLimitExceeded)
      SingleSaoNamePage.clickSubmissionButton()

      Then("an error is shown")
      SingleSaoNamePage.assertValidationErrorDisplayed(SAONameTooLongError)
    }

    Scenario(
      "Validate error when 'end of the financial year SAO name' with invalid characters",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given(
        "an authenticated user lands on the 'more sao' submit notification full name page"
      )
      goToMoreThanOneSaoPageFromHomePage()
      MoreThanOneSaoPage.clickYesRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()
      assertOnPage(LastSaoNamePage)

      When("the user enter a 'end of the financial year SAO name' with invalid characters")
      LastSaoNamePage.addName(TestData.nameWithInvalidCharacters)
      LastSaoNamePage.clickSubmissionButton()

      Then("an error is shown")
      LastSaoNamePage.assertValidationErrorDisplayed(InvalidSAONameCharactersError)
    }

    Scenario(
      "Validate error when 'end of the financial year SAO name' exceeds 105 characters",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given(
        "an authenticated user lands on the 'more sao' submit notification full name page"
      )
      goToMoreThanOneSaoPageFromHomePage()
      MoreThanOneSaoPage.clickYesRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()
      assertOnPage(LastSaoNamePage)

      When("the user enter a 'end of the financial year SAO name' containing more than 105 characters")
      LastSaoNamePage.addName(TestData.nameCharacterLimitExceeded)
      LastSaoNamePage.clickSubmissionButton()

      Then("an error is shown")
      LastSaoNamePage.assertValidationErrorDisplayed(SAONameTooLongError)
    }

    Scenario(
      "Validate error when 'Previous SAO name' contains invalid characters",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given(
        "an authenticated user lands on the 'Who was the sao before' page"
      )
      goToWhoWasTheSAOBeforePage()

      When("the user enters a 'Previous SAO name' with invalid characters")
      PreviousSaoNamePage.addName(TestData.nameWithInvalidCharacters)
      PreviousSaoNamePage.clickSubmissionButton()

      Then("an error is shown")
      PreviousSaoNamePage.assertValidationErrorDisplayed(InvalidSAONameCharactersError)
    }

    Scenario(
      "Validate error when 'Previous SAO name' exceeds 105 characters",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given(
        "an authenticated user lands on the 'Who was the sao before' page"
      )
      goToWhoWasTheSAOBeforePage()

      When("the user enters a 'Previous SAO name' containing more than 105 characters")
      PreviousSaoNamePage.addName(TestData.nameCharacterLimitExceeded)
      PreviousSaoNamePage.clickSubmissionButton()

      Then("an error is shown")
      PreviousSaoNamePage.assertValidationErrorDisplayed(SAONameTooLongError)
    }

    Scenario(
      "Validate error when 'Last SAO start date' contains invalid characters",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'Last SAO start date' page")
      goToMoreThanOneSaoPageFromHomePage()
      MoreThanOneSaoPage.clickYesRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()
      assertOnPage(LastSaoNamePage)
      LastSaoNamePage.addName("Jerry Hatrix")
      LastSaoNamePage.clickSubmissionButton()
      assertOnPage(LastSaoStartDatePage)

      When("the user enters a 'Last SAO start date' with invalid characters")
      LastSaoStartDatePage.addDateWithInvalidCharacter()
      LastSaoStartDatePage.clickSubmissionButton()

      Then("an error is shown")
      LastSaoStartDatePage.assertValidationErrorDisplayed(InvalidSAOStartDateError)
    }

    Scenario(
      "Validate error when 'Last SAO start date' contains today's or future date",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'Last SAO start date' page")
      goToMoreThanOneSaoPageFromHomePage()
      MoreThanOneSaoPage.clickYesRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()
      assertOnPage(LastSaoNamePage)
      LastSaoNamePage.addName("Jerry Hatrix")
      LastSaoNamePage.clickSubmissionButton()
      assertOnPage(LastSaoStartDatePage)

      When("the user enters today's date in the 'Last SAO start date' field ")
      LastSaoStartDatePage.addDate(LocalDate.now())
      LastSaoStartDatePage.clickSubmissionButton()

      Then("an error is shown")
      LastSaoStartDatePage.assertValidationErrorDisplayed(SAOStartDateMustBeInThePastError)

      When("the user enters future date in the 'Last SAO start date' field ")
      LastSaoStartDatePage.addDate(LocalDate.now().plusDays(25))
      LastSaoStartDatePage.clickSubmissionButton()

      Then("an error is shown")
      LastSaoStartDatePage.assertValidationErrorDisplayed(SAOStartDateMustBeInThePastError)
    }

    Scenario(
      "Validate error when 'Previous SAO start date' contains invalid characters",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'Previous SAO start date' page")
      goToMoreThanOneSaoPageFromHomePage()
      MoreThanOneSaoPage.clickYesRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()
      LastSaoNamePage.addName(TestData.firstPersonName)
      LastSaoNamePage.clickSubmissionButton()
      LastSaoStartDatePage.addDate(LocalDate.now().minusDays(90))
      LastSaoStartDatePage.clickSubmissionButton()
      PreviousSaoNamePage.addName(TestData.secondPersonName)
      PreviousSaoNamePage.clickSubmissionButton()
      assertOnPage(PreviousSaoStartDatePage)

      When("the user enters a 'Previous SAO start date' containing invalid characters")
      PreviousSaoStartDatePage.addDateWithInvalidCharacter()
      PreviousSaoStartDatePage.clickSubmissionButton()

      Then("an error is shown")
      PreviousSaoStartDatePage.assertValidationErrorDisplayed(InvalidSAOStartDateError)
    }

    Scenario(
      "Validate error when 'Previous SAO start date' contains today's or future date",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'Previous SAO start date' page")
      goToMoreThanOneSaoPageFromHomePage()
      MoreThanOneSaoPage.clickYesRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()
      LastSaoNamePage.addName(TestData.firstPersonName)
      LastSaoNamePage.clickSubmissionButton()
      LastSaoStartDatePage.addDate(LocalDate.now().minusDays(90))
      LastSaoStartDatePage.clickSubmissionButton()
      PreviousSaoNamePage.addName(TestData.secondPersonName)
      PreviousSaoNamePage.clickSubmissionButton()
      assertOnPage(PreviousSaoStartDatePage)

      When("the user enters today's date in the 'Previous SAO start date' field")
      PreviousSaoStartDatePage.addDate(LocalDate.now())
      PreviousSaoStartDatePage.clickSubmissionButton()

      Then("an error is shown")
      PreviousSaoStartDatePage.assertValidationErrorDisplayed(SAOStartDateMustBeInThePastError)

      When("the user enters a future date in the 'Previous SAO start date' field")
      PreviousSaoStartDatePage.addDate(LocalDate.now().plusDays(25))
      PreviousSaoStartDatePage.clickSubmissionButton()

      Then("an error is shown")
      PreviousSaoStartDatePage.assertValidationErrorDisplayed(SAOStartDateMustBeInThePastError)
    }

    Scenario(
      "Validate error when 'Previous SAO end date' contains invalid characters",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'Previous SAO end date' page")
      goToMoreThanOneSaoPageFromHomePage()
      MoreThanOneSaoPage.clickYesRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()
      LastSaoNamePage.addName(TestData.firstPersonName)
      LastSaoNamePage.clickSubmissionButton()
      LastSaoStartDatePage.addDate(LocalDate.now().minusDays(1))
      LastSaoStartDatePage.clickSubmissionButton()
      PreviousSaoNamePage.addName(TestData.secondPersonName)
      PreviousSaoNamePage.clickSubmissionButton()
      PreviousSaoStartDatePage.addDate(LocalDate.now().minusDays(100))
      PreviousSaoStartDatePage.clickSubmissionButton()
      assertOnPage(PreviousSaoEndDatePage)

      When("the user enters a 'Previous SAO end date' containing invalid characters")
      PreviousSaoEndDatePage.addDateWithInvalidCharacter()
      PreviousSaoEndDatePage.clickSubmissionButton()

      Then("an error is shown")
      PreviousSaoEndDatePage.assertValidationErrorDisplayed(InvalidPreviousSAOEndDateError)
    }

    Scenario(
      "Validate error when 'Previous SAO end date' contains today's or future date",
      NotificationUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'Previous SAO end date' page")
      goToMoreThanOneSaoPageFromHomePage()
      MoreThanOneSaoPage.clickYesRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()
      LastSaoNamePage.addName(TestData.firstPersonName)
      LastSaoNamePage.clickSubmissionButton()
      LastSaoStartDatePage.addDate(LocalDate.now().minusDays(1))
      LastSaoStartDatePage.clickSubmissionButton()
      PreviousSaoNamePage.addName(TestData.secondPersonName)
      PreviousSaoNamePage.clickSubmissionButton()
      PreviousSaoStartDatePage.addDate(LocalDate.now().minusDays(100))
      PreviousSaoStartDatePage.clickSubmissionButton()
      assertOnPage(PreviousSaoEndDatePage)

      When("the user enters today's date in the 'Previous SAO end date' field")
      PreviousSaoEndDatePage.addDate(LocalDate.now())
      PreviousSaoEndDatePage.clickSubmissionButton()

      Then("an error is shown")
      PreviousSaoEndDatePage.assertValidationErrorDisplayed(PreviousSAOEndDateMustBeInThePastError)

      When("the user enters a future date in the 'Previous SAO end date' field")
      PreviousSaoEndDatePage.addDate(LocalDate.now().plusDays(25))
      PreviousSaoEndDatePage.clickSubmissionButton()

      Then("an error is shown")
      PreviousSaoEndDatePage.assertValidationErrorDisplayed(PreviousSAOEndDateMustBeInThePastError)
    }
  }

  private def goToWhoWasTheSAOBeforePage(): Unit = {
    goToMoreThanOneSaoPageFromHomePage()
    When("the user clicks the Yes radio button")
    MoreThanOneSaoPage.clickYesRadioButton()
    When("the user clicks the continue button")
    MoreThanOneSaoPage.clickSubmissionButton()
    Then("the user is on the last SAO name page")
    assertOnPage(LastSaoNamePage)
    When("the user adds a name")
    LastSaoNamePage.addName("Shane Warne")
    When("the user clicks the continue button")
    LastSaoNamePage.clickSubmissionButton()
    Then("the user is on the last SAO start date page")
    assertOnPage(LastSaoStartDatePage)
    When("the user adds a date")
    LastSaoStartDatePage.addDate(LocalDate.now().minusDays(20))
    When("the user clicks the continue button")
    LastSaoStartDatePage.clickSubmissionButton()
    Then("the user is on the previous SAO name page")
    assertOnPage(PreviousSaoNamePage)
  }

  private def goToAdditionalInformationPageFromHomePage(): Unit = {
    Then("the user is on the account home page")
    assertOnPage(AccountHomePage)
    When("the user clicks the continue button")
    AccountHomePage.clickMakeSubmissionLink()
    Then("the user is on the submission type page")
    assertOnPage(SubmissionTypePage)
    When("the user clicks the notification radio button")
    SubmissionTypePage.clickNotificationRadioButton()
    When("the user clicks the continue button")
    SubmissionTypePage.clickSubmissionButton()
    Then("the user is on the submit notification start page")
    assertOnPage(SubmitNotificationStartPage)
    provideSingleSaoDetailsFromStartPage()
    uploadSimpleSubmissionTemplateFromStartPage()
    When("the user clicks the submit notification link")
    SubmitNotificationStartPage.clickTaskListSectionLink(SubmitNotification)
    Then("the user is on the additional information page")
    assertOnPage(AdditionalInformationPage)
  }

  private def provideSingleSaoDetailsFromStartPage(name: String = "Cat Noir"): Unit = {
    When("the user clicks the provide sao details link")
    SubmitNotificationStartPage.clickTaskListSectionLink(ProvideSaoDetails)
    Then("the user is on the 'More than one SAO' page")
    assertOnPage(MoreThanOneSaoPage)
    When("the user clicks the no radio button")
    MoreThanOneSaoPage.clickNoRadioButton()
    When("the user clicks the continue button")
    MoreThanOneSaoPage.clickSubmissionButton()
    Then("the user is on the single sao name page")
    assertOnPage(SingleSaoNamePage)
    When("the user adds a name")
    SingleSaoNamePage.addName(name)
    When("the user clicks the continue button")
    SingleSaoNamePage.clickSubmissionButton()
    Then("the user back on the submit notification start page")
    assertOnPage(SubmitNotificationStartPage)
  }

  private def uploadSimpleSubmissionTemplateFromStartPage(): Unit = {
    When("the user clicks the Upload Submission Template link")
    SubmitNotificationStartPage.clickTaskListSectionLink(UploadSubmissionTemplate)
    Then("the user is on the upload submission template page")
    assertOnPage(UploadSubmissionTemplatePage)
    When("the user uploads a valid file")
    UploadSubmissionTemplatePage.upload(FourCompaniesFile)
    Then("the user is on the upload table page")
    assertOnPage(UploadTablePage)
    When("the user clicks the continue button")
    UploadTablePage.clickSubmissionButton()
    Then("the user is on the submit notification start page")
    assertOnPage(SubmitNotificationStartPage)
  }

  private def goToMoreThanOneSaoPageFromHomePage(): Unit = {
    Then("the user is on the account home page")
    assertOnPage(AccountHomePage)
    When("the user clicks the continue button")
    AccountHomePage.clickMakeSubmissionLink()
    Then("the user is on the submission type page")
    assertOnPage(SubmissionTypePage)
    When("the user clicks the Notification radio button")
    SubmissionTypePage.clickNotificationRadioButton()
    When("the user clicks the continue button")
    SubmissionTypePage.clickSubmissionButton()
    Then("the user is on the submit notification start page")
    assertOnPage(SubmitNotificationStartPage)
    When("the user clicks the Provide SAO details link")
    SubmitNotificationStartPage.clickTaskListSectionLink(ProvideSaoDetails)
    Then("the user is on the 'More Than One SAO' Page")
    assertOnPage(MoreThanOneSaoPage)
  }

  private def completeSubmitANotificationFromTaskListWithDefaultSelections(): Unit = {
    When("the user clicks the 'Submit Notification' link")
    SubmitNotificationStartPage.clickTaskListSectionLink(SubmitNotification)
    Then("the user is on the 'Additional Information' page")
    assertOnPage(AdditionalInformationPage)
    When("the user clicks the skip button")
    AdditionalInformationPage.clickSkipButton()
    Then("the user is on the 'Confirm Notification' page")
    assertOnPage(ConfirmNotificationPage)
    When("the user clicks the continue button")
    ConfirmNotificationPage.clickSubmissionButton()
    Then("the user is on the 'Check Your Answers' page")
    assertOnPage(CheckYourAnswersPage)
    When("the user clicks the continue button")
    CheckYourAnswersPage.clickSubmissionButton()
    Then("the user is on the confirmation page")
    assertOnPage(ConfirmationPage)
    When("the user clicks the continue button")
    ConfirmationPage.clickSubmissionButton()
    Then("the user is on the 'Submit Notification Complete' Pages")
    assertOnPage(SubmitNotificationCompletePage)
  }
}
