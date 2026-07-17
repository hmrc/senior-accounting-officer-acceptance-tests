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
import uk.gov.hmrc.test.ui.support.PageSupport.*
import uk.gov.hmrc.test.ui.support.{PageSupport, TestData}

import java.time.LocalDate

class NotificationSpec extends BaseSpec {

  override def beforeEach(): Unit = {
    super.beforeEach()
    AuthorityWizardPage.withAffinityGroup(Organisation).withDsaoEnrolment(TestData.subscriptionId).redirectToHomePage()
  }

  Feature("Submit Notification") {

    Scenario(
      "Notification task list shows the correct initial state",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the notification start page at the start of a new submission")
      assertOnPage(AccountHomePage)
      AccountHomePage.clickMakeSubmissionLink()
      assertOnPage(SubmissionTypePage)
      SubmissionTypePage.clickNotificationRadioButton()
      SubmissionTypePage.clickSubmissionButton()
      assertOnPage(SubmitNotificationStartPage)

      Then("the task list displays each element in the correct state with the correct status")
      SubmitNotificationStartPage.assertTaskListSectionStatus(ProvideSaoDetails, NotStarted)
      SubmitNotificationStartPage.assertStatusHighlightedBlue(ProvideSaoDetails)
      SubmitNotificationStartPage.assertTaskListSectionNameIsHyperlink(ProvideSaoDetails)
      SubmitNotificationStartPage.assertTaskListSectionStatus(UploadSubmissionTemplate, CannotStartYet)
      SubmitNotificationStartPage.assertStatusNotHighlighted(UploadSubmissionTemplate)
      SubmitNotificationStartPage.assertTaskListSectionNameIsNotHyperlink(UploadSubmissionTemplate)
      SubmitNotificationStartPage.assertTaskListSectionStatus(SubmitNotification, CannotStartYet)
      SubmitNotificationStartPage.assertStatusNotHighlighted(SubmitNotification)
      SubmitNotificationStartPage.assertTaskListSectionNameIsNotHyperlink(SubmitNotification)
    }

    Scenario(
      "A user can submit a notification successfully when additional information is added and not changed",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user initiates adding a notification from the 'Hub' page")
      goToAdditionalInformationPageFromHomePage()

      When("additional information is added")
      AdditionalInformationPage.addInformation("Test")
      AdditionalInformationPage.clickSubmissionButton()
      assertOnPage(ConfirmNotificationPage)
      ConfirmNotificationPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Test")

      And("the user confirms their answers by clicking 'Continue'")
      CheckYourAnswersPage.clickSubmissionButton()

      Then("the user lands on the 'Confirmation' page")
      assertOnPage(ConfirmationPage)

      And("a unique reference number is displayed on screen")
      ConfirmationPage.assertReferenceNumberEquals(ConfirmationPage.notificationReference)

      And("the expected 'download a pdf' and 'print this page' links are present")
      ConfirmationPage.assertLinkHasTextOnPage(ConfirmationPage.downloadPdfLink, "Download a PDF")
      ConfirmationPage.assertLinkHasTextOnPage(ConfirmationPage.printPageLink, "Print this page")
    }

    Scenario(
      "When continuing with no additional information an error presents and is cleared on populating additional information and pressing continue",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user arrives on the additional information page during a notification submission")
      goToAdditionalInformationPageFromHomePage()

      When("pressing continue without providing additional information")
      AdditionalInformationPage.clickSubmissionButton()
      assertPageWithError(AdditionalInformationPage)

      Then("an error appears on screen")
      AdditionalInformationPage.assertErrorSummaryDisplayed()

      And(
        "on continuing after adding additional information the text added is displayed on the 'Check Your Answers' page"
      )
      AdditionalInformationPage.addInformation("Test")
      AdditionalInformationPage.clickSubmissionButton()
      assertOnPage(ConfirmNotificationPage)
      ConfirmNotificationPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Test")
    }

    Scenario(
      "When continuing with no additional information an error presents and is cleared on pressing 'Skip'",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user arrives on the additional information page during a notification submission")
      goToAdditionalInformationPageFromHomePage()

      When("pressing 'Continue' without providing additional information")
      AdditionalInformationPage.clickSubmissionButton()
      assertPageWithError(AdditionalInformationPage)

      Then("an error appears on screen")
      AdditionalInformationPage.assertErrorSummaryDisplayed()

      And("on pressing 'Skip', 'Not provided' value is displayed on the 'Check Your Answers' page")
      AdditionalInformationPage.clickSkipButton()
      assertOnPage(ConfirmNotificationPage)
      ConfirmNotificationPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Not provided")
    }

    Scenario(
      "When pressing 'Skip' with no additional information, no text is displayed on the 'Check Your Answers' page",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user arrives on the 'Additional Information' page during a notification submission")
      goToAdditionalInformationPageFromHomePage()

      When("pressing 'Skip' without providing additional information")
      AdditionalInformationPage.clickSkipButton()
      assertOnPage(ConfirmNotificationPage)

      Then("'Not provided' value is displayed on the 'Check Your Answers' page")
      ConfirmNotificationPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Not provided")
    }

    Scenario(
      "When pressing 'Skip' with additional information added, no text is displayed on the 'Check Your Answers' page",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user arrives on the 'Additional Information' page during a notification submission")
      goToAdditionalInformationPageFromHomePage()

      When("pressing skip with additional information provided")
      AdditionalInformationPage.addInformation("Test")
      AdditionalInformationPage.clickSkipButton()
      assertOnPage(ConfirmNotificationPage)

      Then("'Not provided' value is displayed on the 'Check Your Answers' page")
      ConfirmNotificationPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Not provided")
    }

    Scenario(
      "When selecting to change additional information from the 'Check Your Answers' page the changes are persisted",
      SubmissionUITests,
      ZapTests
    ) {
      Given(
        "an authenticated user provides additional information and arrives on the 'Check Your Answers' page during a notification submission"
      )
      goToAdditionalInformationPageFromHomePage()
      AdditionalInformationPage.addInformation("Test")
      AdditionalInformationPage.clickSubmissionButton()
      assertOnPage(ConfirmNotificationPage)
      ConfirmNotificationPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Test")

      When("pressing the change link and updating the provided additional information")
      CheckYourAnswersPage.clickAdditionalInformationChangeLink()
      assertUrl(AdditionalInformationPage.changePageUrl)
      AdditionalInformationPage.addInformation("New Test For Changed Text")

      Then("on pressing 'Continue' the updated text is displayed on the 'Check Your Answers' page")
      AdditionalInformationPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "New Test For Changed Text")
    }

    Scenario(
      "When selecting to change additional information from the 'Check Your Answers' page and when 'Skip' is pressed then the changes are not persisted",
      SubmissionUITests,
      ZapTests
    ) {
      Given(
        "an authenticated user provides additional information and arrives on the 'Check Your Answers' page during a notification submission"
      )
      goToAdditionalInformationPageFromHomePage()
      AdditionalInformationPage.addInformation("Test")
      AdditionalInformationPage.clickSubmissionButton()
      assertOnPage(ConfirmNotificationPage)
      ConfirmNotificationPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Test")

      When("pressing the change link and updating the provided additional information")
      CheckYourAnswersPage.clickAdditionalInformationChangeLink()
      assertUrl(AdditionalInformationPage.changePageUrl)
      AdditionalInformationPage.addInformation("New Test For Changed Text")

      Then("on pressing 'Skip' the updated text is not displayed is on the 'Check Your Answers' page")
      AdditionalInformationPage.clickSkipButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Not provided")
    }

    Scenario(
      "When the existing additional information text is removed, post clicking the change link from 'CYA' page, then the error message is displayed",
      SubmissionUITests,
      ZapTests
    ) {
      Given(
        "an authenticated user provides additional information and arrives on the 'Check Your Answers' page during a notification submission"
      )
      goToAdditionalInformationPageFromHomePage()
      AdditionalInformationPage.addInformation("Test")
      AdditionalInformationPage.clickSubmissionButton()
      assertOnPage(ConfirmNotificationPage)
      ConfirmNotificationPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Test")

      When("pressing the change link and clearing the existing additional information")
      CheckYourAnswersPage.clickAdditionalInformationChangeLink()
      assertUrl(AdditionalInformationPage.changePageUrl)
      AdditionalInformationPage.addInformation("")

      Then("on pressing 'Continue' an error appears on screen")
      AdditionalInformationPage.clickSubmissionButton()
      AdditionalInformationPage.assertErrorSummaryDisplayed()
    }

    Scenario(
      "When selecting to change SAO name from the 'Check Your Answers' page during a notification submission",
      SubmissionUITests,
      ZapTests
    ) {
      Given(
        "an authenticated user provides SAO name and arrives on the 'Check Your Answers' page during a notification submission"
      )
      goToAdditionalInformationPageFromHomePage()
      AdditionalInformationPage.clickSkipButton()
      assertOnPage(ConfirmNotificationPage)
      ConfirmNotificationPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      When("user changes the SAO name by clicking on the change link")
      CheckYourAnswersPage.clickSaoNameChangeLink()
      assertUrl(SingleSaoNamePage.changePageUrl)
      SingleSaoNamePage.addName("Jade Dancing")
      SingleSaoNamePage.clickSubmissionButton()
      assertTextOnPage(CheckYourAnswersPage.saoNameValueElement, "Jade Dancing")

      And("the user confirms their answers by clicking 'Continue'")
      CheckYourAnswersPage.clickSubmissionButton()

      Then("the user lands on the 'Confirmation' page")
      assertOnPage(ConfirmationPage)
    }

    Scenario(
      "When changing the SAO name from the 'Check Your Answers' page during a notification submission, without making any changes to the name",
      SubmissionUITests,
      ZapTests
    ) {
      Given(
        "an authenticated user provides SAO name and arrives on the 'Check Your Answers' page during a notification submission"
      )
      goToAdditionalInformationPageFromHomePage()
      AdditionalInformationPage.clickSkipButton()
      assertOnPage(ConfirmNotificationPage)
      ConfirmNotificationPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      When("the user returns to the 'Check Your Answers' page without saving any changes ")
      CheckYourAnswersPage.clickSaoNameChangeLink()
      assertUrl(SingleSaoNamePage.changePageUrl)
      SingleSaoNamePage.clickSubmissionButton()

      Then("the original SAO name is displayed on the 'Check Your Answers' page")
      assertTextOnPage(CheckYourAnswersPage.saoNameValueElement, "Cat Noir")

      When("the user confirms their answers by clicking 'Continue'")
      CheckYourAnswersPage.clickSubmissionButton()

      Then("the user lands on the 'Confirmation' page")
      assertOnPage(ConfirmationPage)
    }

    Scenario(
      "Complete a notification providing details for a single SAO in the financial year",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'More than one SAO' page")
      goToMoreThanOneSaoPageFromHomePage()

      When("the 'Continue' button is clicked after the 'No' radio button is selected")
      MoreThanOneSaoPage.clickNoRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()

      Then("the user lands on the 'What is the name of the SAO' page")
      assertOnPage(SingleSaoNamePage)

      When("the 'Continue' button is clicked after an SAO name is entered")
      SingleSaoNamePage.addName("Jane Doe")
      SingleSaoNamePage.clickSubmissionButton()

      Then("the user lands on the 'Submit a notification' start page")
      assertOnPage(SubmitNotificationStartPage)

      And("the task list displays each element in the correct state with the correct status")
      SubmitNotificationStartPage.assertTaskListSectionStatus(ProvideSaoDetails, Completed)
      SubmitNotificationStartPage.assertStatusNotHighlighted(ProvideSaoDetails)
      SubmitNotificationStartPage.assertTaskListSectionNameIsNotHyperlink(ProvideSaoDetails)
      SubmitNotificationStartPage.assertTaskListSectionStatus(UploadSubmissionTemplate, NotStarted)
      SubmitNotificationStartPage.assertStatusHighlightedBlue(UploadSubmissionTemplate)
      SubmitNotificationStartPage.assertTaskListSectionNameIsHyperlink(UploadSubmissionTemplate)
      SubmitNotificationStartPage.assertTaskListSectionStatus(SubmitNotification, CannotStartYet)
      SubmitNotificationStartPage.assertStatusNotHighlighted(SubmitNotification)
      SubmitNotificationStartPage.assertTaskListSectionNameIsNotHyperlink(SubmitNotification)

      When("the 'Upload the submission template' link is clicked")
      SubmitNotificationStartPage.clickTaskListSectionLink(UploadSubmissionTemplate)

      Then("the user lands on the 'Upload a submission template' page")
      assertOnPage(UploadSubmissionTemplatePage)

      And("the guidance link is present and correct")
      UploadSubmissionTemplatePage.assertTemplateGuidanceLinkFoundWithCorrectAttributes()

      When("the 'Continue' button is clicked after choosing a file for upload")
      UploadSubmissionTemplatePage.upload(FourCompaniesFile)

      Then("the user lands on the 'Review the companies in your notification' page")
      assertOnPage(UploadTablePage)

      When("the 'Continue' button is clicked")
      UploadTablePage.clickSubmissionButton()

      Then("the user lands on the 'Submit a notification' start page")
      assertOnPage(SubmitNotificationStartPage)

      And("the task list displays each element in the correct state with the correct status")
      SubmitNotificationStartPage.assertTaskListSectionStatus(ProvideSaoDetails, Completed)
      SubmitNotificationStartPage.assertStatusNotHighlighted(ProvideSaoDetails)
      SubmitNotificationStartPage.assertTaskListSectionNameIsNotHyperlink(ProvideSaoDetails)
      SubmitNotificationStartPage.assertTaskListSectionStatus(UploadSubmissionTemplate, Completed)
      SubmitNotificationStartPage.assertStatusNotHighlighted(UploadSubmissionTemplate)
      SubmitNotificationStartPage.assertTaskListSectionNameIsNotHyperlink(UploadSubmissionTemplate)
      SubmitNotificationStartPage.assertTaskListSectionStatus(SubmitNotification, NotStarted)
      SubmitNotificationStartPage.assertStatusHighlightedBlue(SubmitNotification)
      SubmitNotificationStartPage.assertTaskListSectionNameIsHyperlink(SubmitNotification)

      When("the 'Submit the notification' link is clicked")
      SubmitNotificationStartPage.clickTaskListSectionLink(SubmitNotification)

      Then("the user lands on the 'Additional information' page")
      assertOnPage(AdditionalInformationPage)

      When("the 'Skip' button is clicked")
      AdditionalInformationPage.clickSkipButton()

      Then("the user lands on the 'Confirm your notification' page")
      assertOnPage(ConfirmNotificationPage)

      When("the 'Continue' button is clicked")
      ConfirmNotificationPage.clickSubmissionButton()

      Then("the user lands on the 'Check your answers' page")
      assertOnPage(CheckYourAnswersPage)

      When("the 'Continue' button is clicked")
      CheckYourAnswersPage.clickSubmissionButton()

      Then("the user lands on the 'Confirmation' page")
      assertOnPage(ConfirmationPage)

      When("the 'Continue' button is clicked")
      ConfirmationPage.clickSubmissionButton()

      Then("the user lands on the 'Submit a notification' complete page")
      assertOnPage(SubmitNotificationCompletePage)

      And("the task list displays each element in the correct state with the correct status")
      SubmitNotificationCompletePage.assertTaskListSectionStatus(ProvideSaoDetails, Completed)
      SubmitNotificationCompletePage.assertStatusNotHighlighted(ProvideSaoDetails)
      SubmitNotificationCompletePage.assertTaskListSectionNameIsNotHyperlink(ProvideSaoDetails)
      SubmitNotificationCompletePage.assertTaskListSectionStatus(UploadSubmissionTemplate, Completed)
      SubmitNotificationCompletePage.assertStatusNotHighlighted(UploadSubmissionTemplate)
      SubmitNotificationCompletePage.assertTaskListSectionNameIsNotHyperlink(UploadSubmissionTemplate)
      SubmitNotificationCompletePage.assertTaskListSectionStatus(SubmitNotification, Completed)
      SubmitNotificationCompletePage.assertStatusNotHighlighted(SubmitNotification)
      SubmitNotificationCompletePage.assertTaskListSectionNameIsNotHyperlink(SubmitNotification)

      When("the 'Go back to the homepage' button is clicked")
      SubmitNotificationCompletePage.clickSubmissionButton()

      Then("the user lands on the 'Account Homepage'")
      assertOnPage(AccountHomePage)
    }

    Scenario(
      "Review uploaded company details and upload an updated notification submission",
      SubmissionUITests,
      ZapTests
    ) {
      Given("a random example value of 'Emi A Ward' is chosen as the last SAO during a notification submission")
      And("a random example value of 'Piet Van De Merwe' is chosen as the previous SAO")
      When("the authenticated user chooses to upload an empty notification submission file")
      goToMoreThanOneSaoPageFromHomePage()
      MoreThanOneSaoPage.clickYesRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()
      assertOnPage(MultiSaoNamePage)
      MultiSaoNamePage.addName("Emi A Ward")
      MultiSaoNamePage.clickSubmissionButton()
      assertOnPage(MultiSaoFirstStartDatePage)
      MultiSaoFirstStartDatePage.addDate(LocalDate.now().minusDays(40))
      MultiSaoFirstStartDatePage.clickSubmissionButton()
      assertOnPage(WhoWasTheSaoBeforePage)
      WhoWasTheSaoBeforePage.addName("Piet Van De Merwe")
      WhoWasTheSaoBeforePage.clickSubmissionButton()
      assertOnPage(MultiSaoSecondStartDatePage)
      MultiSaoSecondStartDatePage.addDate(LocalDate.now().minusDays(100))
      MultiSaoSecondStartDatePage.clickSubmissionButton()
      assertOnPage(MultiSaoSecondEndDatePage)
      MultiSaoSecondEndDatePage.addDate(LocalDate.now().minusDays(47))
      MultiSaoSecondEndDatePage.clickSubmissionButton()
      assertOnPage(MultiSaoAreAllAddedPage)
      MultiSaoAreAllAddedPage.clickYesRadioButton()
      MultiSaoAreAllAddedPage.clickSubmissionButton()
      assertOnPage(SubmitNotificationStartPage)
      SubmitNotificationStartPage.clickTaskListSectionLink(UploadSubmissionTemplate)
      assertOnPage(UploadSubmissionTemplatePage)
      UploadSubmissionTemplatePage.upload(EmptyFile)

      And("the user lands on the 'Review the companies in your notification' page")
      assertOnPage(UploadTablePage)

      Then("The page displays the correct company count of '0' as the upload file is empty")
      And("'Emi A Ward' as the responsible SAO")
      UploadTablePage.assertCompanyCountAndSaoMatch(companyCount = 0, expectedSao = "Emi A Ward")

      And("The table contains no company details but only the text 'No parsed rows'")
      UploadTablePage.assertCompanyTableEmpty()

      When("the 'upload an updated submission template' link is clicked")
      UploadTablePage.clickUploadUpdatedTemplateLink()

      Then("the user lands on the 'Upload a submission template' page")
      assertOnPage(UploadSubmissionTemplatePage)

      When("the 'Continue' button is clicked after choosing a file with '4' companies in the upload file")
      UploadSubmissionTemplatePage.upload(FourCompaniesFile)

      Then("the user lands on the 'Review the companies in your notification' page")
      assertOnPage(UploadTablePage)

      Then("The page displays the correct company count of '4'")
      And("'Emi A Ward' as the responsible SAO")
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
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user provides details for a single SAO in a notification submission")
      assertOnPage(AccountHomePage)
      AccountHomePage.clickMakeSubmissionLink()
      assertOnPage(SubmissionTypePage)
      SubmissionTypePage.clickNotificationRadioButton()
      SubmissionTypePage.clickSubmissionButton()
      assertOnPage(SubmitNotificationStartPage)
      provideSingleSaoDetailsFromStartPage(TestData.firstPersonName)

      When("attempting to upload a submission template which has 1 invalid qualification set for a company")
      SubmitNotificationStartPage.clickTaskListSectionLink(UploadSubmissionTemplate)
      assertOnPage(UploadSubmissionTemplatePage)
      UploadSubmissionTemplatePage.upload(InvalidQualificationFile)

      Then("the expected error page displays with a count of 1 error")
      assertOnPage(UploadTableErrorPage)
      UploadTableErrorPage.assertParagraphWithErrorCountMatches(errorCount = 1)

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
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'More than one SAO' page")
      goToMoreThanOneSaoPageFromHomePage()

      When("the 'Continue' button is clicked after selecting 'Yes'")
      MoreThanOneSaoPage.clickYesRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()

      Then("the user lands on the 'What is the name of the last SAO' page")
      assertOnPage(MultiSaoNamePage)

      When("the 'Continue' button is clicked after entering a name of 'Jack Sparrow'")
      MultiSaoNamePage.addName("Jack Sparrow")
      MultiSaoNamePage.clickSubmissionButton()

      Then("the user lands on the 'What date did Jack Sparrow become the SAO' page")
      assertOnPage(MultiSaoFirstStartDatePage)
      MultiSaoFirstStartDatePage.assertHeadingMatches("What date did Jack Sparrow become the SAO?")

      When("the 'Continue' button is clicked after adding a date 30 days in the past")
      MultiSaoFirstStartDatePage.addDate(LocalDate.now().minusDays(30))
      MultiSaoFirstStartDatePage.clickSubmissionButton()

      Then("the user lands on the 'Who was the SAO before Jack Sparrow' page")
      assertOnPage(WhoWasTheSaoBeforePage)

      And("the page displays the correct content")
      WhoWasTheSaoBeforePage.assertHeadingMatches("Who was the SAO before Jack Sparrow?")
      WhoWasTheSaoBeforePage.assertHintMatches("This is the person who held the role before Jack Sparrow")

      When("the 'Continue' button is clicked after entering a name of 'Gert Bo'")
      WhoWasTheSaoBeforePage.addName("Gert Bo")
      WhoWasTheSaoBeforePage.clickSubmissionButton()

      Then("the user lands on the 'When did Gert Bo’s responsibility as the SAO start' page")
      assertOnPage(MultiSaoSecondStartDatePage)
      MultiSaoSecondStartDatePage.assertHeadingMatches("When did Gert Bo’s responsibility as the SAO start?")

      When("the 'Continue' button is clicked after adding a date 90 days in the past")
      MultiSaoSecondStartDatePage.addDate(LocalDate.now().minusDays(90))
      MultiSaoSecondStartDatePage.clickSubmissionButton()

      Then("the user lands on the 'When did Gert Bo stop being the SAO' page")
      assertOnPage(MultiSaoSecondEndDatePage)
      MultiSaoSecondEndDatePage.assertHeadingMatches("When did Gert Bo stop being the SAO?")

      When("the 'Continue' button is clicked after adding a date 65 days in the past")
      MultiSaoSecondEndDatePage.addDate(LocalDate.now().minusDays(65))
      MultiSaoSecondEndDatePage.clickSubmissionButton()

      Then(
        "the user lands on the 'Have you added all the SAO for the financial year this notification relates to' page"
      )
      assertOnPage(MultiSaoAreAllAddedPage)

      When("the 'Continue' button is clicked after the 'No' radio button is selected")
      MultiSaoAreAllAddedPage.clickNoRadioButton()
      MultiSaoAreAllAddedPage.clickSubmissionButton()

      Then("the user lands on the 'Who was the SAO before Gert Bo' page")
      assertUrl(WhoWasTheSaoBeforePage.changePageUrl)
      WhoWasTheSaoBeforePage.assertHeadingMatches("Who was the SAO before Gert Bo?")

      When("the 'Continue' button is clicked after entering a name of 'Alex Rhodes'")
      WhoWasTheSaoBeforePage.addName("Alex Rhodes")
      WhoWasTheSaoBeforePage.clickSubmissionButton()

      Then("the user lands on the 'When did Alex Rhodes’s responsibility as the SAO start' page")
      assertUrl(MultiSaoSecondStartDatePage.changePageUrl)

      When("the 'Continue' button is clicked after adding a start date 90 days in the past for the new SAO")
      MultiSaoSecondStartDatePage.addDate(LocalDate.now().minusDays(90))
      MultiSaoSecondStartDatePage.clickSubmissionButton()

      Then("the user lands on the 'When did Alex Rhodes stop being the SAO' page")
      assertUrl(MultiSaoSecondEndDatePage.pageUrlWithSaoIndexOne)

      When("the 'Continue' button is clicked after adding an end date 70 days in the past for the new SAO")
      MultiSaoSecondEndDatePage.addDate(LocalDate.now().minusDays(70))
      MultiSaoSecondEndDatePage.clickSubmissionButton()

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
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'More than one SAO' page")
      goToMoreThanOneSaoPageFromHomePage()

      When("the 'Continue' button is clicked after selecting no radio options")
      MoreThanOneSaoPage.clickSubmissionButton()

      Then("an error message is displayed")
      MoreThanOneSaoPage.assertErrorSummaryDisplayed()

      When("the 'Continue' button is clicked after the 'No' radio button is selected")
      MoreThanOneSaoPage.clickNoRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()

      Then("the user lands on the 'What is the name of the SAO' page")
      assertOnPage(SingleSaoNamePage)

      When("the 'Continue' button is clicked after no name was entered")
      SingleSaoNamePage.clickSubmissionButton()

      Then("an error message is displayed")
      SingleSaoNamePage.assertErrorSummaryDisplayed()

      When("the 'Continue' button is clicked after a name is entered")
      SingleSaoNamePage.addName("Jane Doe")
      SingleSaoNamePage.clickSubmissionButton()

      // TODO: Update the below step when the page is fully developed. This will land on the 'Account Homepage'.
      Then("the user lands on the 'Submit a notification' start page")
      assertOnPage(SubmitNotificationStartPage)
    }

    Scenario(
      "Validate that SAO details are required for a notification submission given there are multiple SAO's in the financial year",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'More than one SAO' page")
      goToMoreThanOneSaoPageFromHomePage()

      When("the 'Continue' button is clicked after selecting 'Yes'")
      MoreThanOneSaoPage.clickYesRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()

      Then("the user lands on the 'What is name of the last SAO' question page")
      assertOnPage(MultiSaoNamePage)

      When("the 'Continue' button is clicked after no name is entered")
      MultiSaoNamePage.clickSubmissionButton()

      Then("an error message is displayed")
      MultiSaoNamePage.assertErrorSummaryDisplayed()

      When("the 'Continue' button is clicked after entering a name of 'Jerry Hatrix'")
      MultiSaoNamePage.addName("Jerry Hatrix")
      MultiSaoNamePage.clickSubmissionButton()

      Then("the user lands on the 'What date did Jerry Hatrix become the SAO' page")
      assertOnPage(MultiSaoFirstStartDatePage)

      When("the 'Continue' button is clicked after no date is entered")
      MultiSaoFirstStartDatePage.clickSubmissionButton()

      Then("an error message is displayed")
      MultiSaoFirstStartDatePage.assertErrorSummaryDisplayed()

      When("the 'Continue' button is clicked after adding a date 30 days in the past")
      MultiSaoFirstStartDatePage.addDate(LocalDate.now().minusDays(30))
      MultiSaoFirstStartDatePage.clickSubmissionButton()

      Then("the user lands on the 'Who was the SAO before Jerry Hatrix' question page")
      assertOnPage(WhoWasTheSaoBeforePage)

      When("the 'Continue' button is clicked after no name is entered")
      WhoWasTheSaoBeforePage.clickSubmissionButton()

      Then("an error message is displayed")
      WhoWasTheSaoBeforePage.assertErrorSummaryDisplayed()

      When("the 'Continue' button is clicked after entering a name for a preceding SAO of 'Jock B'")
      WhoWasTheSaoBeforePage.addName("Jock B")
      WhoWasTheSaoBeforePage.clickSubmissionButton()

      Then("the user lands on the 'When did Jock B’s responsibility as the SAO start' page")
      assertOnPage(MultiSaoSecondStartDatePage)

      When("the 'Continue' button is clicked after no date is entered")
      MultiSaoSecondStartDatePage.clickSubmissionButton()

      Then("an error message is displayed")
      MultiSaoSecondStartDatePage.assertErrorSummaryDisplayed()

      When("the 'Continue' button is clicked after adding a date 45 days in the past")
      MultiSaoSecondStartDatePage.addDate(LocalDate.now().minusDays(45))
      MultiSaoSecondStartDatePage.clickSubmissionButton()

      Then("the user lands on the 'When did Jock B stop being the SAO' page")
      assertOnPage(MultiSaoSecondEndDatePage)
      MultiSaoSecondEndDatePage.assertHeadingMatches("When did Jock B stop being the SAO?")

      When("the 'Continue' button is clicked after no date is entered")
      MultiSaoSecondEndDatePage.clickSubmissionButton()

      Then("an error message is displayed")
      MultiSaoSecondEndDatePage.assertErrorSummaryDisplayed()

      When("the 'Continue' button is clicked after adding a date 35 days in the past")
      MultiSaoFirstStartDatePage.addDate(LocalDate.now().minusDays(35))
      MultiSaoSecondEndDatePage.clickSubmissionButton()

      Then(
        "the user lands on the 'Have you added all the SAO for the financial year this notification relates to' page"
      )
      assertOnPage(MultiSaoAreAllAddedPage)

      When("the 'Continue' button is clicked without a radio button being selected")
      MultiSaoAreAllAddedPage.clickSubmissionButton()

      Then("an error appears on screen")
      MultiSaoAreAllAddedPage.assertErrorSummaryDisplayed()

      When("the 'Continue' button is clicked after selecting 'Yes'")
      MultiSaoAreAllAddedPage.clickYesRadioButton()
      MultiSaoAreAllAddedPage.clickSubmissionButton()

      Then("the user lands on the 'Submit a notification' start page")
      assertOnPage(SubmitNotificationStartPage)
    }

    Scenario(
      "Validate that 'dynamic' SAO details are retained on selecting the 'back link' during a notification submission",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user has added 'Shane Warne' as the last SAO")
      goToMoreThanOneSaoPageFromHomePage()
      MoreThanOneSaoPage.clickYesRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()
      assertOnPage(MultiSaoNamePage)
      MultiSaoNamePage.addName("Shane Warne")
      MultiSaoNamePage.clickSubmissionButton()
      assertOnPage(MultiSaoFirstStartDatePage)
      MultiSaoFirstStartDatePage.addDate(LocalDate.now().minusDays(20))
      MultiSaoFirstStartDatePage.clickSubmissionButton()
      assertOnPage(WhoWasTheSaoBeforePage)

      And("has added 'Jonty Rhodes' as a prior SAO")
      WhoWasTheSaoBeforePage.addName("Jonty Rhodes")
      WhoWasTheSaoBeforePage.clickSubmissionButton()
      assertOnPage(MultiSaoSecondStartDatePage)
      MultiSaoSecondStartDatePage.addDate(LocalDate.now().minusDays(65))
      MultiSaoSecondStartDatePage.clickSubmissionButton()
      assertOnPage(MultiSaoSecondEndDatePage)
      MultiSaoSecondEndDatePage.addDate(LocalDate.now().minusDays(35))
      MultiSaoSecondEndDatePage.clickSubmissionButton()

      And("the user lands on the 'Have you added all the SAO for the financial year this notification relates to' page")
      assertOnPage(MultiSaoAreAllAddedPage)

      When("the 'Back' link is clicked")
      MultiSaoAreAllAddedPage.clickBackLink()

      Then("the user lands on the 'When did Jonty Rhodes stop being the SAO' page")
      assertOnPage(MultiSaoSecondEndDatePage)

      And("the page displays the correct content")
      MultiSaoSecondEndDatePage.assertHeadingMatches("When did Jonty Rhodes stop being the SAO?")

      When("the 'Back' link is clicked")
      MultiSaoSecondEndDatePage.clickBackLink()

      Then("the user lands on the 'When did Jonty Rhodes’s responsibility as the SAO start' page")
      assertOnPage(MultiSaoSecondStartDatePage)

      And("the page displays the correct content")
      MultiSaoSecondStartDatePage.assertHeadingMatches("When did Jonty Rhodes’s responsibility as the SAO start?")

      When("the 'Back' link is clicked")
      MultiSaoSecondStartDatePage.clickBackLink()

      Then("the user lands on the 'Who was the SAO before Shane Warne' page")
      assertOnPage(WhoWasTheSaoBeforePage)

      And("the page displays the correct content")
      WhoWasTheSaoBeforePage.assertHeadingMatches("Who was the SAO before Shane Warne?")
      WhoWasTheSaoBeforePage.assertHintMatches("This is the person who held the role before Shane Warne")
      WhoWasTheSaoBeforePage.assertNameMatches("Jonty Rhodes")

      When("the 'Back' link is clicked")
      WhoWasTheSaoBeforePage.clickBackLink()

      Then("the user lands on the 'What date did Shane Warne become the SAO' page")
      assertOnPage(MultiSaoFirstStartDatePage)

      And("the page displays the correct content")
      MultiSaoFirstStartDatePage.assertHeadingMatches("What date did Shane Warne become the SAO?")
    }
  }

  private def goToAdditionalInformationPageFromHomePage(): Unit = {
    assertOnPage(AccountHomePage)
    AccountHomePage.clickMakeSubmissionLink()
    assertOnPage(SubmissionTypePage)
    SubmissionTypePage.clickNotificationRadioButton()
    SubmissionTypePage.clickSubmissionButton()
    assertOnPage(SubmitNotificationStartPage)
    provideSingleSaoDetailsFromStartPage()
    uploadSimpleSubmissionTemplateFromStartPage()
    SubmitNotificationStartPage.clickTaskListSectionLink(SubmitNotification)
    assertOnPage(AdditionalInformationPage)
  }

  private def provideSingleSaoDetailsFromStartPage(name: String = "Cat Noir"): Unit = {
    SubmitNotificationStartPage.clickTaskListSectionLink(ProvideSaoDetails)
    assertOnPage(MoreThanOneSaoPage)
    MoreThanOneSaoPage.clickNoRadioButton()
    MoreThanOneSaoPage.clickSubmissionButton()
    assertOnPage(SingleSaoNamePage)
    SingleSaoNamePage.addName(name)
    SingleSaoNamePage.clickSubmissionButton()
    assertOnPage(SubmitNotificationStartPage)
  }

  private def uploadSimpleSubmissionTemplateFromStartPage(): Unit = {
    SubmitNotificationStartPage.clickTaskListSectionLink(UploadSubmissionTemplate)
    assertOnPage(UploadSubmissionTemplatePage)
    UploadSubmissionTemplatePage.upload(FourCompaniesFile)
    assertOnPage(UploadTablePage)
    UploadTablePage.clickSubmissionButton()
    assertOnPage(SubmitNotificationStartPage)
  }

  private def goToMoreThanOneSaoPageFromHomePage(): Unit = {
    assertOnPage(AccountHomePage)
    AccountHomePage.clickMakeSubmissionLink()
    assertOnPage(SubmissionTypePage)
    SubmissionTypePage.clickNotificationRadioButton()
    SubmissionTypePage.clickSubmissionButton()
    assertOnPage(SubmitNotificationStartPage)
    SubmitNotificationStartPage.clickTaskListSectionLink(ProvideSaoDetails)
    assertOnPage(MoreThanOneSaoPage)
  }
}
