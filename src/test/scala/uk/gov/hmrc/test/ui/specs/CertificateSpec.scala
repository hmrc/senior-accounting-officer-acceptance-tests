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

import org.scalatest.*
import uk.gov.hmrc.test.ui.adt.AffinityGroup.Organisation
import uk.gov.hmrc.test.ui.adt.CertificateTaskListSection.*
import uk.gov.hmrc.test.ui.adt.PageSectionStatus.*
import uk.gov.hmrc.test.ui.adt.UploadFile.*
import uk.gov.hmrc.test.ui.adt.ValidationError.*
import uk.gov.hmrc.test.ui.pages.submission.*
import uk.gov.hmrc.test.ui.pages.submission.certificate.*
import uk.gov.hmrc.test.ui.pages.{AccountHomePage, AuthorityWizardPage}
import uk.gov.hmrc.test.ui.specs.tags.*
import uk.gov.hmrc.test.ui.support.InternalAuthorisationSupport.setupInternalAuthorisation
import uk.gov.hmrc.test.ui.support.PageSupport.*
import uk.gov.hmrc.test.ui.support.{PageSupport, TestData}

class CertificateSpec extends BaseSpec {
  override def beforeEach(): Unit = {
    super.beforeEach()
    AuthorityWizardPage.withAffinityGroup(Organisation).withDsaoEnrolment(TestData.subscriptionId).redirectToHomePage()
    setupInternalAuthorisation
  }

  Feature("Submit Certificate") {

    Scenario(
      "Certification task list shows the correct initial state",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the certification start page at the start of a new submission")
      navigateToCertificateStartPage()

      Then(
        "the 'Submit a certificate' task list page is displayed with each section in the correct state with the correct status"
      )
      CertificateTaskListPage.assertTaskListSectionStatus(ProvideSaoDetails, NotStarted)
      CertificateTaskListPage.assertStatusHighlightedBlue(ProvideSaoDetails)
      CertificateTaskListPage.assertTaskListSectionNameIsHyperlink(ProvideSaoDetails)
      CertificateTaskListPage.assertTaskListSectionStatus(UploadSubmissionTemplate, CannotStartYet)
      CertificateTaskListPage.assertStatusNotHighlighted(UploadSubmissionTemplate)
      CertificateTaskListPage.assertTaskListSectionNameIsNotHyperlink(UploadSubmissionTemplate)
      CertificateTaskListPage.assertTaskListSectionStatus(SubmitCertificate, CannotStartYet)
      CertificateTaskListPage.assertStatusNotHighlighted(SubmitCertificate)
      CertificateTaskListPage.assertTaskListSectionNameIsNotHyperlink(SubmitCertificate)
    }

    Scenario(
      "An authorised senior accounting officer submits a certificate from the 'Account Homepage'",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user initiates a certificate submission from the 'Account Homepage'")
      navigateToCertificateStartPage()

      When("the user selects 'Provide the SAO’s details' link")
      CertificateTaskListPage.clickTaskListSectionLink(ProvideSaoDetails)

      Then("the 'What is the name of the SAO responsible for the certificate?' page is displayed")
      assertOnPage(CertificateSaoFullNamePage)

      When("the user enters the SAO’s name")
      CertificateSaoFullNamePage.addName(TestData.firstPersonName)

      And("the user selects 'Continue' after a valid SAO name is added")
      CertificateSaoFullNamePage.clickSubmissionButton()

      Then("the 'What is the email address for SAO name?' page is displayed")
      assertOnPage(CertificateSaoEmailPage)

      When("the user enters the SAO’s email address")
      CertificateSaoEmailPage.addEmail(TestData.firstPersonEmail)

      And("the user selects 'Continue'")
      CertificateSaoEmailPage.clickSubmissionButton()

      Then("the user returns to the 'Submit a certificate task list' page")
      assertOnPage(CertificateTaskListPage)

      And(
        "the 'Submit a certificate task list' page is displayed showing 'Provide the SAO’s details' section as Completed"
      )
      CertificateTaskListPage.assertTaskListSectionStatus(ProvideSaoDetails, Completed)
      CertificateTaskListPage.assertStatusNotHighlighted(ProvideSaoDetails)
      CertificateTaskListPage.assertTaskListSectionNameIsHyperlink(ProvideSaoDetails)
      CertificateTaskListPage.assertTaskListSectionStatus(UploadSubmissionTemplate, NotStarted)
      CertificateTaskListPage.assertStatusHighlightedBlue(UploadSubmissionTemplate)
      CertificateTaskListPage.assertTaskListSectionNameIsHyperlink(UploadSubmissionTemplate)
      CertificateTaskListPage.assertTaskListSectionStatus(SubmitCertificate, CannotStartYet)
      CertificateTaskListPage.assertStatusNotHighlighted(SubmitCertificate)
      CertificateTaskListPage.assertTaskListSectionNameIsNotHyperlink(SubmitCertificate)

      When("the user selects 'Upload the submission template' link")
      CertificateTaskListPage.clickTaskListSectionLink(UploadSubmissionTemplate)

      Then("the 'Upload a submission template' page is displayed")
      assertOnPage(UploadSubmissionTemplatePage)

      And("the guidance link is present and correct")
      UploadSubmissionTemplatePage.assertTemplateGuidanceLinkFoundWithCorrectAttributes()

      When("the user uploads a valid submission template and selects 'Continue'")
      UploadSubmissionTemplatePage.uploadAndSubmit(FourCompaniesFile)

      Then("the 'Review the companies with a qualified certificate' page is displayed")
      assertOnPage(UploadReviewQualifiedPage)

      When("the user selects 'Continue'")
      UploadReviewQualifiedPage.clickSubmissionButton()

      Then("the 'Review the companies with an unqualified certificate' page is displayed")
      assertOnPage(UploadReviewUnqualifiedPage)

      When("the user selects 'Continue'")
      UploadReviewUnqualifiedPage.clickSubmissionButton()

      Then("the user returns to the 'Submit a certificate task list'")
      assertOnPage(CertificateTaskListPage)

      And(
        "the Submit a certificate task list page is displayed, showing 'Provide the SAO’s details' and 'Upload the submission template' sections as 'Completed'"
      )
      CertificateTaskListPage.assertTaskListSectionStatus(ProvideSaoDetails, Completed)
      CertificateTaskListPage.assertStatusNotHighlighted(ProvideSaoDetails)
      CertificateTaskListPage.assertTaskListSectionNameIsHyperlink(ProvideSaoDetails)
      CertificateTaskListPage.assertTaskListSectionStatus(UploadSubmissionTemplate, Completed)
      CertificateTaskListPage.assertStatusNotHighlighted(UploadSubmissionTemplate)
      CertificateTaskListPage.assertTaskListSectionNameIsHyperlink(UploadSubmissionTemplate)
      CertificateTaskListPage.assertTaskListSectionStatus(SubmitCertificate, NotStarted)
      CertificateTaskListPage.assertStatusHighlightedBlue(SubmitCertificate)
      CertificateTaskListPage.assertTaskListSectionNameIsHyperlink(SubmitCertificate)

      When("the user selects 'Submit the certificate' link")
      CertificateTaskListPage.clickTaskListSectionLink(SubmitCertificate)

      Then("the 'Additional information about your certificate' page is displayed")
      assertOnPage(AdditionalInformationPage)

      When("the user enters additional information")
      AdditionalInformationPage.addInformation("additional information for this certificate")

      And("the user selects 'Continue'")
      AdditionalInformationPage.clickSubmissionButton()

      Then("the 'Certificate submission and authorisation' page is displayed")
      assertOnPage(CertificateWhoIsSubmittingPage)

      When("the user selects 'The SAO' radio option")
      CertificateWhoIsSubmittingPage.clickSaoSubmitterRadioButton()

      And("the user selects 'Continue'")
      CertificateWhoIsSubmittingPage.clickSubmissionButton()

      When("the 'Declaration' page is displayed")
      assertOnPage(CertificateDeclarationSaoPage)

      And("the user enters their full name")
      CertificateDeclarationSaoPage.addSaoName(TestData.firstPersonName)

      And("the user selects 'Confirm'")
      CertificateDeclarationSaoPage.clickSubmissionButton()

      Then("the 'Check your answers' page is displayed")
      assertOnPage(CheckYourAnswersPage)

      When("the user reviews the certificate details")
      assertTextOnPage(CheckYourAnswersPage.certificateSaoFullNameValue, TestData.firstPersonName)
      assertTextOnPage(CheckYourAnswersPage.certificateSaoEmailValue, TestData.firstPersonEmail)
      assertTextOnPage(CheckYourAnswersPage.certificateWhoIsSubmittingValue, "The SAO")
      assertTextOnPage(CheckYourAnswersPage.certificateDeclarationSaoValue, TestData.firstPersonName)
      assertTextOnPage(
        CheckYourAnswersPage.certificateAdditionalInformationValue,
        "additional information for this certificate"
      )

      And("the user clicks 'Confirm and submit'")
      CheckYourAnswersPage.clickSubmissionButton()

      Then("the 'Submit a certificate task list' page is displayed showing all tasks as 'Completed'")
      CertificateTaskListPage.assertCompleteUrl()

      When("the user clicks 'View your confirmation'")
      CertificateTaskListPage.clickSubmissionButton()

      Then("the user lands on the 'Certificate confirmation' page")
      ConfirmationPage.assertUrl()

      And("the expected page title is displayed")
      assertPageTitle(ConfirmationPage.pageTitle)

      And("the expected reference is displayed on the url")
      ConfirmationPage.assertReferenceNumberMatchesUrl()

      And("the expected 'download a pdf' link is present")
      ConfirmationPage.assertLinkHasTextOnPage(ConfirmationPage.downloadPdfLink, "download a PDF")

      And("the expected 'print this page; link is present")
      ConfirmationPage.assertLinkHasTextOnPage(ConfirmationPage.printPageLink, "print this page")

      And("the 'download a pdf' link targets the certificate pdf download route")
      ConfirmationPage.assertDownloadPdfLinkTargetsPdf()
    }

    Scenario(
      "A user submits a certificate from the 'Account Homepage' on behalf of their SAO",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user initiates a certificate submission from the 'Account Homepage'")
      navigateToCertificateStartPage()

      When("the user selects 'Provide the SAO’s details' link")
      CertificateTaskListPage.clickTaskListSectionLink(ProvideSaoDetails)

      Then("the user lands on the 'What is the name of the SAO responsible for the certificate?' page")
      assertOnPage(CertificateSaoFullNamePage)

      When("the user selects 'Continue' after adding a valid SAO name")
      CertificateSaoFullNamePage.addName(TestData.firstPersonName)
      CertificateSaoFullNamePage.clickSubmissionButton()

      Then("the user lands on the 'What is the email address for SAO name?' page")
      assertOnPage(CertificateSaoEmailPage)

      When("the user enters the SAO’s email")
      CertificateSaoEmailPage.addEmail(TestData.firstPersonEmail)

      And("the user selects 'Continue'")
      CertificateSaoEmailPage.clickSubmissionButton()

      Then("the user returns to the 'Certificate Task List'")
      assertOnPage(CertificateTaskListPage)

      When("the 'Upload the submission template' link is clicked")
      CertificateTaskListPage.clickTaskListSectionLink(UploadSubmissionTemplate)

      Then("the user lands on the 'Upload a submission template' page")
      assertOnPage(UploadSubmissionTemplatePage)

      When("the 'Continue' button is clicked after choosing a file for upload")
      UploadSubmissionTemplatePage.uploadAndSubmit(FourCompaniesFile)

      Then("the user lands on the 'Review the companies with a qualified certificate' page")
      assertOnPage(UploadReviewQualifiedPage)

      When("the 'Continue' button is clicked")
      UploadReviewQualifiedPage.clickSubmissionButton()

      Then("the user lands on the 'Review the companies with an unqualified certificate' page")
      assertOnPage(UploadReviewUnqualifiedPage)

      When("the 'Continue' button is clicked")
      UploadReviewUnqualifiedPage.clickSubmissionButton()

      Then("the user returns to the 'Certificate Task List'")
      assertOnPage(CertificateTaskListPage)

      When("the user selects 'Submit the certificate' link")
      CertificateTaskListPage.clickTaskListSectionLink(SubmitCertificate)

      Then("the user lands on the 'Additional information about your certificate' page")
      assertOnPage(AdditionalInformationPage)

      When("the user enters additional information")
      AdditionalInformationPage.addInformation("additional information for this certificate")

      And("the user selects 'Continue'")
      AdditionalInformationPage.clickSubmissionButton()

      Then("the user lands on the 'Who is submitting' page")
      assertOnPage(CertificateWhoIsSubmittingPage)

      When("the user selects the stand-in submitter radio option")
      CertificateWhoIsSubmittingPage.clickStandInSubmitterRadioButton()

      And("the user selects 'Continue'")
      CertificateWhoIsSubmittingPage.clickSubmissionButton()

      Then("the user lands on the 'SAO stand-in declaration' page")
      assertOnPage(CertificateDeclarationStandInPage)

      When("the user enters their name")
      CertificateDeclarationStandInPage.addSaoName(TestData.firstPersonName)

      And("the user enters the SAO’s name")
      CertificateDeclarationStandInPage.addStandInSubmitterName(TestData.secondPersonName)

      And("the user clicks 'Continue'")
      CertificateDeclarationStandInPage.clickSubmissionButton()

      Then("the user lands on the 'Check your answers' page")
      assertOnPage(CheckYourAnswersPage)

      When("the user clicks 'Confirm and submit'")
      CheckYourAnswersPage.clickSubmissionButton()

      Then("the user lands on the 'Certificate Task List'")
      CertificateTaskListPage.assertCompleteUrl()

      When("user clicks on 'View your confirmation'")
      CertificateTaskListPage.clickSubmissionButton()

      Then("the user lands on the 'Certificate confirmation' page")
      ConfirmationPage.assertUrl()

      And("the expected page title is displayed")
      assertPageTitle(ConfirmationPage.pageTitle)

      And("the expected reference is displayed on the url")
      ConfirmationPage.assertReferenceNumberMatchesUrl()

      And("the expected 'download a pdf' link is present")
      ConfirmationPage.assertLinkHasTextOnPage(ConfirmationPage.downloadPdfLink, "download a PDF")

      And("the expected 'print this page; link is present")
      ConfirmationPage.assertLinkHasTextOnPage(ConfirmationPage.printPageLink, "print this page")

      And("the 'download a pdf' link targets the certificate pdf download route")
      ConfirmationPage.assertDownloadPdfLinkTargetsPdf()
    }

    Scenario(
      "Re-upload the same company details with 'unqualified' certificates in a certificate submission",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user initiates a certificate submission from the 'Account Homepage'")
      navigateToCertificateStartPage()

      And("the first task in the task list is completed")
      CertificateTaskListPage.clickTaskListSectionLink(ProvideSaoDetails)
      assertOnPage(CertificateSaoFullNamePage)
      CertificateSaoFullNamePage.addName(TestData.firstPersonName)
      CertificateSaoFullNamePage.clickSubmissionButton()
      assertOnPage(CertificateSaoEmailPage)
      CertificateSaoEmailPage.addEmail(TestData.firstPersonEmail)
      CertificateSaoEmailPage.clickSubmissionButton()
      assertOnPage(CertificateTaskListPage)

      When("a submission template is successfully uploaded")
      When("the user selects the 'Upload the submission template' link")
      CertificateTaskListPage.clickTaskListSectionLink(UploadSubmissionTemplate)

      Then("the 'Upload a submission template' page is displayed")
      assertOnPage(UploadSubmissionTemplatePage)

      When("the user uploads a valid submission template and selects 'Continue'")
      UploadSubmissionTemplatePage.uploadAndSubmit(FourCompaniesFile)

      Then("the user lands on the 'Review the companies with a qualified certificate' page")
      assertOnPage(UploadReviewQualifiedPage)

      When("the 'Continue' button is clicked")
      UploadReviewQualifiedPage.clickSubmissionButton()

      Then("the user lands on the 'Review the companies with an unqualified certificate' page")
      assertOnPage(UploadReviewUnqualifiedPage)

      And("the paragraph content shows the expected text including the dynamic values derived from the upload file")
      UploadReviewUnqualifiedPage.assertFirstParagraphMatches(totalCompanyCount = 4)
      UploadReviewUnqualifiedPage.assertDeclarationParagraphMatches(
        unqualifiedCompanyCount = 4,
        expectedSao = TestData.firstPersonName
      )

      When("the 'upload an updated submission template' link is clicked")
      UploadReviewUnqualifiedPage.clickUploadUpdatedTemplateLink()

      Then("the user is redirected back to the 'Upload a submission template' page")
      assertOnPage(UploadSubmissionTemplatePage)

      When("the 'Continue' button is clicked having the original upload file still chosen")
      UploadSubmissionTemplatePage.uploadAndSubmit(FourCompaniesFile)

      Then("the user lands on the 'Review the companies with a qualified certificate' page")
      assertOnPage(UploadReviewQualifiedPage)

      When("the 'Continue' button is clicked")
      UploadReviewQualifiedPage.clickSubmissionButton()

      Then("the user lands on the 'Review the companies with an unqualified certificate' page")
      assertOnPage(UploadReviewUnqualifiedPage)

      And("The paragraph content has not changed")
      UploadReviewUnqualifiedPage.assertFirstParagraphMatches(totalCompanyCount = 4)
      UploadReviewUnqualifiedPage.assertDeclarationParagraphMatches(
        unqualifiedCompanyCount = 4,
        expectedSao = TestData.firstPersonName
      )

      When("the 'Continue' button is clicked")
      UploadReviewQualifiedPage.clickSubmissionButton()

      Then("the user returns to the 'Certificate Task List' page")
      assertOnPage(CertificateTaskListPage)

      And("the 'Upload the submission template' task is shown as completed")
      CertificateTaskListPage.assertTaskListSectionStatus(UploadSubmissionTemplate, Completed)
    }

    Scenario(
      "Attempting to upload unacceptable submission template files to Upscan which returns the appropriate error",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user initiates a certificate submission from the 'Account Homepage'")
      navigateToCertificateStartPage()

      And("the first task in the task list is completed")
      CertificateTaskListPage.clickTaskListSectionLink(ProvideSaoDetails)
      assertOnPage(CertificateSaoFullNamePage)
      CertificateSaoFullNamePage.addName(TestData.firstPersonName)
      CertificateSaoFullNamePage.clickSubmissionButton()
      assertOnPage(CertificateSaoEmailPage)
      CertificateSaoEmailPage.addEmail(TestData.firstPersonEmail)
      CertificateSaoEmailPage.clickSubmissionButton()
      assertOnPage(CertificateTaskListPage)

      When("attempting to upload an invalid submission template")
      CertificateTaskListPage.clickTaskListSectionLink(UploadSubmissionTemplate)
      assertOnPage(UploadSubmissionTemplatePage)
      UploadSubmissionTemplatePage.uploadAndSubmit(InvalidQualificationFile)

      Then("the expected error page displays with a count of 4 errors")
      assertOnPage(UploadTableErrorPage)
      UploadTableErrorPage.assertParagraphWithErrorCountMatches(errorCount = 4)

      When("the 'Return to file upload' button is clicked")
      UploadTableErrorPage.clickSubmissionButton()

      Then("the user is returned to the 'Upload a submission template' page")
      assertOnPage(UploadSubmissionTemplatePage)

      When("attempting to upload 'invalid.REASON.csv' submission template")
      UploadSubmissionTemplatePage.uploadAndSubmit(InvalidTypeFile)

      Then("the appropriate error is shown on screen")
      UploadSubmissionTemplatePage.assertValidationErrorDisplayed(InvalidFileTypeError)

      When("attempting to upload 'infected.VIRUS_NAME.csv' submission template")
      UploadSubmissionTemplatePage.uploadAndSubmit(InfectedFile)

      Then("the appropriate error is shown on screen")
      UploadSubmissionTemplatePage.assertValidationErrorDisplayed(InfectedFileError)

      When("attempting to upload 'unknown.REASON.csv' submission template")
      UploadSubmissionTemplatePage.uploadAndSubmit(UnknownErrorFile)

      Then("the appropriate error is shown on screen")
      UploadSubmissionTemplatePage.assertValidationErrorDisplayed(UnknownUploadError)
    }

    Scenario(
      "Access template guidance and the 'Download a submission template' link in the certificate submission journey",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user initiates a certificate submission")
      navigateToCertificateStartPage()

      And("first task in the task list is completed")
      CertificateTaskListPage.clickTaskListSectionLink(ProvideSaoDetails)
      assertOnPage(CertificateSaoFullNamePage)
      CertificateSaoFullNamePage.addName(TestData.firstPersonName)
      CertificateSaoFullNamePage.clickSubmissionButton()
      assertOnPage(CertificateSaoEmailPage)
      CertificateSaoEmailPage.addEmail(TestData.firstPersonEmail)
      CertificateSaoEmailPage.clickSubmissionButton()
      assertOnPage(CertificateTaskListPage)

      When("attempting to upload an invalid submission template")
      CertificateTaskListPage.clickTaskListSectionLink(UploadSubmissionTemplate)
      assertOnPage(UploadSubmissionTemplatePage)
      UploadSubmissionTemplatePage.assertTemplateGuidanceLinkFoundWithCorrectAttributes()

      When("the 'Read guidance on how to complete the submission template (opens in new tab)' link is clicked")
      UploadSubmissionTemplatePage.clickReadGuidanceLink()

      Then(
        "the 'How to complete and submit your submission template' page opens on a new tab which the user navigates to"
      )
      switchTab(1)
      assertUrl(SubmissionTemplateGuidancePage.pageUrlForNewTab)
      assertPageTitle(SubmissionTemplateGuidancePage.pageTitle)

      And("the 'Download a submission template' link is present with the correct path")
      SubmissionTemplateGuidancePage.assertDownloadSubmissionTemplateLinkFound()

      When("the user closes the active tab and returns to the 'Upload a submission template' page tab")
      closeCurrentTab()
      assertOnPage(UploadSubmissionTemplatePage)

      And("the 'Continue' button is clicked after choosing a submission template file with an 'invalid structure'")
      UploadSubmissionTemplatePage.uploadAndSubmit(InvalidFormatFile)

      Then("the user lands on the 'There is a problem with your submission template file' error page")
      assertOnPage(UploadTemplateErrorPage)
      UploadTemplateErrorPage.assertParagraphDescribesInvalidTemplateError()

      When(
        "the 'Download a submission template and read guidance on how to complete it (opens in new tab)' link is clicked"
      )
      UploadTemplateErrorPage.clickDownloadTemplateAndReadGuidanceLink()

      Then(
        "the 'How to complete and submit your submission template' page opens on a new tab which the user navigates to"
      )
      switchTab(1)
      assertUrl(SubmissionTemplateGuidancePage.pageUrlForNewTab)
      assertPageTitle(SubmissionTemplateGuidancePage.pageTitle)

      And("the 'Download a submission template' link is present with the correct path")
      SubmissionTemplateGuidancePage.assertDownloadSubmissionTemplateLinkFound()

      When(
        "the user closes the active tab and returns to the 'There is a problem with your submission template file' page tab"
      )
      closeCurrentTab()
      assertOnPage(UploadTemplateErrorPage)
      UploadTemplateErrorPage.assertParagraphDescribesInvalidTemplateError()

      And("the 'Upload a submission template' button is clicked")
      UploadTemplateErrorPage.clickSubmissionButton()

      Then("the user lands on the 'Upload a submission template' page")
      assertOnPage(UploadSubmissionTemplatePage)

      When("the 'Continue' button is clicked after choosing a file with 'invalid certificate data' in the upload file")
      UploadSubmissionTemplatePage.uploadAndSubmit(ValidNotificationDataWithInvalidCertificateDataFile)

      And("the user lands on the 'There is a problem with your submission template file' page")
      assertOnPage(UploadTemplateErrorPage)
      UploadTemplateErrorPage.assertParagraphDescribesTemplateDataErrors()

      And(
        "the 'Download a submission template and read guidance on how to complete it (opens in new tab)' link is clicked"
      )
      UploadTemplateErrorPage.clickDownloadTemplateAndReadGuidanceLink()

      Then(
        "the 'How to complete and submit your submission template' page opens on a new tab which the user navigates to"
      )
      switchTab(1)
      assertUrl(SubmissionTemplateGuidancePage.pageUrlForNewTab)
      assertPageTitle(SubmissionTemplateGuidancePage.pageTitle)

      And("the 'Download a submission template' link is present with the correct path")
      SubmissionTemplateGuidancePage.assertDownloadSubmissionTemplateLinkFound()
    }

    Scenario(
      "Validate that mandatory details are required for a certification submission",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'What would you like to submit?' page")
      assertOnPage(AccountHomePage)
      AccountHomePage.clickMakeSubmissionLink()
      assertOnPage(SubmissionTypePage)

      When("the 'Continue' button is clicked")
      SubmissionTypePage.clickSubmissionButton()

      Then("an error message is displayed")
      SubmissionTypePage.assertErrorSummaryDisplayed()

      When("the 'A certificate' radio button is selected")
      SubmissionTypePage.clickCertificateRadioButton()

      And("the 'Continue' button is clicked")
      SubmissionTypePage.clickSubmissionButton()

      Then("the user lands on the 'Submit a certificate' page")
      assertOnPage(CertificateTaskListPage)

      When("the 'Provide the SAO’s details' link is clicked")
      CertificateTaskListPage.clickTaskListSectionLink(ProvideSaoDetails)

      Then("the user lands on the 'What is the name of the SAO responsible for the certificate?' page")
      assertOnPage(CertificateSaoFullNamePage)

      When("the 'Continue' button is clicked")
      CertificateSaoFullNamePage.clickSubmissionButton()

      Then("an error message 'Enter the name of the SAO responsible for the certificate' is displayed")
      CertificateSaoFullNamePage.assertValidationErrorDisplayed(MissingSAONameForCertificateError)

      When("the 'Continue' button is clicked after adding a valid SAO name")
      CertificateSaoFullNamePage.addName(TestData.firstPersonName)
      CertificateSaoFullNamePage.clickSubmissionButton()

      Then("the user lands on the 'SAO email' page")
      assertOnPage(CertificateSaoEmailPage)

      When("the user clicks 'Continue'")
      CertificateSaoEmailPage.clickSubmissionButton()

      Then("an error message 'Enter the email address of the SAO' is displayed")
      CertificateSaoEmailPage.assertValidationErrorDisplayed(MissingSaoEmailForCertificateError)

      And("the page heading displays the correct SAO name")
      CertificateSaoEmailPage.assertHeadingMatches(s"What is the email address for ${TestData.firstPersonName}?")

      When("the user clicks 'Continue' after adding an invalid SAO email")
      CertificateSaoEmailPage.addEmail("abc123")

      And("the user clicks 'Continue'")
      CertificateSaoEmailPage.clickSubmissionButton()

      Then("an error message 'Email address must be in the correct format, like name@example.com' is displayed")
      CertificateSaoEmailPage.assertValidationErrorDisplayed(InvalidEmailError)

      When("the user enters a valid SAO email")
      CertificateSaoEmailPage.addEmail(TestData.firstPersonEmail)

      And("the user clicks 'Continue'")
      CertificateSaoEmailPage.clickSubmissionButton()

      Then("the user returns to the 'Certificate Task List'")
      assertOnPage(CertificateTaskListPage)

      When("the 'Upload the submission template' link is clicked")
      CertificateTaskListPage.clickTaskListSectionLink(UploadSubmissionTemplate)

      Then("the user lands on the 'Upload a submission template' page")
      assertOnPage(UploadSubmissionTemplatePage)

      When("a valid submission template is uploaded")
      UploadSubmissionTemplatePage.uploadAndSubmit(FourCompaniesFile)

      Then("the user lands on the 'Review the companies with a qualified certificate' page")
      assertOnPage(UploadReviewQualifiedPage)

      When("the 'Continue' button is clicked")
      UploadReviewQualifiedPage.clickSubmissionButton()

      Then("the user lands on the 'Review the companies with an unqualified certificate' page")
      assertOnPage(UploadReviewUnqualifiedPage)

      When("the 'Continue' button is clicked")
      UploadReviewUnqualifiedPage.clickSubmissionButton()

      Then("the user returns to the 'Certificate Task List'")
      assertOnPage(CertificateTaskListPage)

      When("the user selects 'Submit the certificate' link")
      CertificateTaskListPage.clickTaskListSectionLink(SubmitCertificate)

      Then("the user lands on the 'Additional information' page")
      assertOnPage(AdditionalInformationPage)

      When("the user clicks 'Continue'")
      AdditionalInformationPage.clickSubmissionButton()

      Then(
        "an error message 'Enter information about your certificate, or select skip if there’s nothing to add' is displayed"
      )
      AdditionalInformationPage.assertValidationErrorDisplayed(MissingCertificateAdditionalInformationError)

      When("the user clicks 'Continue' after adding additional information")
      AdditionalInformationPage.addInformation("additional information for this certificate")
      AdditionalInformationPage.clickSubmissionButton()

      Then("the user lands on the 'Who is submitting' page")
      assertOnPage(CertificateWhoIsSubmittingPage)

      When("the 'Continue' button is clicked after neither radio button is selected")
      CertificateWhoIsSubmittingPage.clickSubmissionButton()

      Then("the error message 'Select who is submitting the certificate' is displayed")
      CertificateWhoIsSubmittingPage.assertValidationErrorDisplayed(NoElementChosenWhoIsSubmittingError)

      When("the user selects 'The SAO' radio option")
      CertificateWhoIsSubmittingPage.clickSaoSubmitterRadioButton()

      And("the user clicks 'Continue'")
      CertificateWhoIsSubmittingPage.clickSubmissionButton()

      Then("the user lands on the SAO’s 'Declaration' page")
      assertOnPage(CertificateDeclarationSaoPage)

      When("the 'Confirm' button is clicked")
      CertificateDeclarationSaoPage.clickSubmissionButton()

      Then("the error message 'Enter the name of the SAO authorised to submit the certificate' is displayed")
      CertificateDeclarationSaoPage.assertValidationErrorDisplayed(MissingAuthorisedSAONameToSubmitError)

      When("the user enters the SAO name")
      CertificateDeclarationSaoPage.addSaoName(TestData.firstPersonName)

      And("the user clicks 'Confirm'")
      CertificateDeclarationSaoPage.clickSubmissionButton()

      Then("the user lands on the 'Check your answers' page")
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.certificateSaoFullNameValue, TestData.firstPersonName)
      assertTextOnPage(CheckYourAnswersPage.certificateSaoEmailValue, TestData.firstPersonEmail)
      assertTextOnPage(CheckYourAnswersPage.certificateWhoIsSubmittingValue, "The SAO")
      assertTextOnPage(CheckYourAnswersPage.certificateDeclarationSaoValue, TestData.firstPersonName)
      assertTextOnPage(
        CheckYourAnswersPage.certificateAdditionalInformationValue,
        "additional information for this certificate"
      )

      When("the 'Back' link is clicked")
      CheckYourAnswersPage.clickBackLink()

      Then("the user lands on the SAO's 'Confirm the certificate'")
      assertOnPage(CertificateDeclarationSaoPage)

      When("the 'Back' link is clicked")
      CertificateWhoIsSubmittingPage.clickBackLink()

      Then("the user lands on the SAO's 'Confirm the certificate' page with no error showing")
      assertOnPage(CertificateDeclarationSaoPage)

      When("the 'Back' link is clicked")
      CertificateWhoIsSubmittingPage.clickBackLink()

      Then("the user lands on the 'Who is submitting' page")
      assertOnPage(CertificateWhoIsSubmittingPage)

      When("the user selects 'A person authorised to submit on behalf of the SAO' radio option")
      CertificateWhoIsSubmittingPage.clickStandInSubmitterRadioButton()

      And("the user clicks 'Continue'")
      CertificateWhoIsSubmittingPage.clickSubmissionButton()

      Then("the user lands on the 'SAO stand-in declaration' page")
      assertOnPage(CertificateDeclarationStandInPage)

      When("the 'Confirm' button is clicked")
      CertificateDeclarationStandInPage.clickSubmissionButton()

      Then("an error message is displayed")
      CertificateDeclarationStandInPage.assertErrorSummaryDisplayed()

      When("the 'Confirm' button is clicked after an 'SAO' name is entered but not the 'stand in submitter' name")
      CertificateDeclarationStandInPage.addSaoName(TestData.firstPersonName)
      CertificateDeclarationStandInPage.clearStandInNameField()

      Then("the 'Confirm' button is clicked")
      CertificateDeclarationStandInPage.clickSubmissionButton()

      Then("the error message 'Enter the name of the SAO who authorised you to submit the certificate' is displayed")
      CertificateDeclarationStandInPage.assertErrorSummaryDisplayed()

      When("the 'stand in submitter' name is entered")
      CertificateDeclarationStandInPage.addStandInSubmitterName(TestData.secondPersonName)

      And("sao name field is empty")
      CertificateDeclarationStandInPage.clearSaoNameField()

      And("the 'Confirm' button is clicked")
      CertificateDeclarationStandInPage.clickSubmissionButton()

      Then("the error message 'Enter your full name' is displayed")
      CertificateDeclarationStandInPage.assertErrorSummaryDisplayed()

      When("the user enters a SAO name")
      CertificateDeclarationStandInPage.addSaoName(TestData.firstPersonName)

      And("the user enters stand-in submitter name")
      CertificateDeclarationStandInPage.addStandInSubmitterName(TestData.secondPersonName)

      When("the 'Confirm' button is clicked")
      CertificateDeclarationStandInPage.clickSubmissionButton()

      Then("the user lands on the 'Check your answers' page")
      assertOnPage(CheckYourAnswersPage)

      And("the user is shown the relevant authorised SAO entries")
      assertTextOnPage(CheckYourAnswersPage.certificateDeclarationSaoValue, TestData.firstPersonName)
      assertTextOnPage(CheckYourAnswersPage.certificateDeclarationStandInValue, TestData.secondPersonName)
    }

    Scenario(
      "Attempting to upload an invalid certificate submission template shows the shared error state",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user initiates a certificate submission from the 'Account Homepage'")
      navigateToCertificateStartPage()

      And("SAO details are provided to complete the first task in the task list")
      CertificateTaskListPage.clickTaskListSectionLink(ProvideSaoDetails)
      assertOnPage(CertificateSaoFullNamePage)
      CertificateSaoFullNamePage.addName(TestData.firstPersonName)
      CertificateSaoFullNamePage.clickSubmissionButton()
      assertOnPage(CertificateSaoEmailPage)
      CertificateSaoEmailPage.addEmail(TestData.firstPersonEmail)
      CertificateSaoEmailPage.clickSubmissionButton()
      assertOnPage(CertificateTaskListPage)

      When("attempting to upload a certificate submission template which has 1 invalid qualification set for a company")
      CertificateTaskListPage.clickTaskListSectionLink(UploadSubmissionTemplate)
      assertOnPage(UploadSubmissionTemplatePage)
      UploadSubmissionTemplatePage.uploadAndSubmit(InvalidQualificationFile)

      Then("the expected certificate upload error page displays with a count of 4 errors")
      assertOnPage(UploadTableErrorPage)
      UploadTableErrorPage.assertParagraphWithErrorCountMatches(errorCount = 4)

      When("the 'Return to file upload' button is clicked")
      UploadTableErrorPage.clickSubmissionButton()

      Then("the user is returned to the 'Upload a submission template' page")
      assertOnPage(UploadSubmissionTemplatePage)
    }

    Scenario(
      "Re-upload the same company details with 'qualified' certificates in a certificate submission",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user initiates a certificate submission from the 'Account Homepage'")
      navigateToCertificateStartPage()

      And("SAO details are provided to complete the first task in the task list")
      CertificateTaskListPage.clickTaskListSectionLink(ProvideSaoDetails)
      assertOnPage(CertificateSaoFullNamePage)
      CertificateSaoFullNamePage.addName(TestData.firstPersonName)
      CertificateSaoFullNamePage.clickSubmissionButton()
      assertOnPage(CertificateSaoEmailPage)
      CertificateSaoEmailPage.assertHeadingMatches(s"What is the email address for ${TestData.firstPersonName}?")
      CertificateSaoEmailPage.addEmail(TestData.firstPersonEmail)
      CertificateSaoEmailPage.clickSubmissionButton()
      assertOnPage(CertificateTaskListPage)

      When("the 'Upload the submission template' is clicked")
      CertificateTaskListPage.clickTaskListSectionLink(UploadSubmissionTemplate)

      Then("the user clicks 'Continue'")
      assertOnPage(UploadSubmissionTemplatePage)

      When("a valid submission template is uploaded")
      UploadSubmissionTemplatePage.uploadAndSubmit(FourCompaniesFile)

      Then("the user lands on 'Review the companies with a qualified certificate' page")
      assertOnPage(UploadReviewQualifiedPage)

      And("the paragraph content shows the expected text including the dynamic values derived from the upload file")
      UploadReviewQualifiedPage.assertFirstParagraphMatches(totalCompanyCount = 4)
      UploadReviewQualifiedPage.assertDeclarationParagraphMatches(
        qualifiedCompanyCount = 0,
        expectedSao = TestData.firstPersonName
      )

      When("the 'upload an updated submission template' link is clicked")
      UploadReviewQualifiedPage.clickUploadUpdatedTemplateLink()

      Then("the user is redirected back to the 'Upload a submission template' page")
      assertOnPage(UploadSubmissionTemplatePage)

      When("the 'Continue' button is clicked after re-selecting the original upload file")
      UploadSubmissionTemplatePage.uploadAndSubmit(FourCompaniesFile)

      Then("the user lands on the 'Review the companies with a qualified certificate' page")
      assertOnPage(UploadReviewQualifiedPage)

      And("The paragraph content has not changed")
      UploadReviewQualifiedPage.assertFirstParagraphMatches(totalCompanyCount = 4)
      UploadReviewQualifiedPage.assertDeclarationParagraphMatches(
        qualifiedCompanyCount = 0,
        expectedSao = TestData.firstPersonName
      )

      When("the 'Continue' button is clicked")
      UploadReviewQualifiedPage.clickSubmissionButton()

      Then("the user lands on 'Review the companies with an unqualified certificate' page")
      assertOnPage(UploadReviewUnqualifiedPage)
    }

    Scenario(
      "An upload certificate data file exceeding the size limit is rejected",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user initiates a certificate submission from the 'Account Homepage'")
      navigateToCertificateStartPage()

      And("SAO details are provided to complete the first task in the task list")
      CertificateTaskListPage.clickTaskListSectionLink(ProvideSaoDetails)
      assertOnPage(CertificateSaoFullNamePage)
      CertificateSaoFullNamePage.addName(TestData.firstPersonName)
      CertificateSaoFullNamePage.clickSubmissionButton()
      assertOnPage(CertificateSaoEmailPage)
      CertificateSaoEmailPage.assertHeadingMatches(s"What is the email address for ${TestData.firstPersonName}?")
      CertificateSaoEmailPage.addEmail(TestData.firstPersonEmail)
      CertificateSaoEmailPage.clickSubmissionButton()
      assertOnPage(CertificateTaskListPage)
      CertificateTaskListPage.clickTaskListSectionLink(UploadSubmissionTemplate)

      And("A submission template containing certificate data that exceeds the allowed size is uploaded")
      assertOnPage(UploadSubmissionTemplatePage)
      UploadSubmissionTemplatePage.uploadAndSubmit(TooLargeDataFile)

      Then("the error message is displayed")
      UploadSubmissionTemplatePage.assertValidationErrorDisplayed(FileSizeExceededError)
    }

    Scenario(
      "Additional information can be skipped during a certificate submission",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'Additional Information' page")
      goToAdditionalInformationPageFromHomePage()

      When("the user selects 'Skip' without providing additional information")
      AdditionalInformationPage.clickSkipButton()

      Then("the user lands on the 'Who is submitting the certificate?' page")
      assertOnPage(CertificateWhoIsSubmittingPage)

      When("the user clicks the 'Back' link")
      CertificateWhoIsSubmittingPage.clickBackLink()

      Then("the user lands on the 'Additional Information' page")
      assertOnPage(AdditionalInformationPage)

      When("the user selects 'skip' after providing additional information")
      AdditionalInformationPage.addInformation("Adding information again")
      AdditionalInformationPage.clickSkipButton()

      Then("the user lands on the 'Who is submitting the certificate?' page")
      assertOnPage(CertificateWhoIsSubmittingPage)

      And("the user selects the 'The SAO' radio option")
      CertificateWhoIsSubmittingPage.clickSaoSubmitterRadioButton()

      And("the user selects 'Continue'")
      CertificateWhoIsSubmittingPage.clickSubmissionButton()

      Then("the user lands on the 'SAO declaration' page")
      assertOnPage(CertificateDeclarationSaoPage)

      When("the user clicks 'Continue' after adding the SAO name to complete the declaration")
      CertificateDeclarationSaoPage.addSaoName(TestData.firstPersonName)
      CertificateDeclarationSaoPage.clickSubmissionButton()

      Then("the user lands on the 'Check your answers' page")
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.certificateSaoFullNameValue, TestData.firstPersonName)
      assertTextOnPage(CheckYourAnswersPage.certificateSaoEmailValue, TestData.firstPersonEmail)
      assertTextOnPage(CheckYourAnswersPage.certificateWhoIsSubmittingValue, "The SAO")
      assertTextOnPage(CheckYourAnswersPage.certificateDeclarationSaoValue, TestData.firstPersonName)
      assertTextOnPage(
        CheckYourAnswersPage.certificateAdditionalInformationValue,
        "Not provided"
      )
    }

    Scenario(
      "Validate error when SAO name responsible for Certificate contains invalid characters",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("the user lands on the 'submit a certificate SAO full name' page")
      navigateToCertificateStartPage()
      CertificateTaskListPage.clickTaskListSectionLink(ProvideSaoDetails)
      assertOnPage(CertificateSaoFullNamePage)

      When("the user enters a SAO name containing invalid characters")
      CertificateSaoFullNamePage.addName(TestData.nameWithInvalidCharacters)

      And("the user selects 'Continue'")
      CertificateSaoFullNamePage.clickSubmissionButton()

      Then("an error is shown")
      CertificateSaoFullNamePage.assertValidationErrorDisplayed(InvalidSAONameCharactersError)
    }

    Scenario(
      "Validate error when SAO name responsible for Certificate exceeds 105 characters",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("the user lands on the 'submit a certificate SAO full name' page")
      navigateToCertificateStartPage()
      CertificateTaskListPage.clickTaskListSectionLink(ProvideSaoDetails)
      assertOnPage(CertificateSaoFullNamePage)

      When("the user enters a SAO name exceeding 105 characters")
      CertificateSaoFullNamePage.addName(TestData.nameCharacterLimitExceeded)
      CertificateSaoFullNamePage.clickSubmissionButton()

      Then("an error is shown")
      CertificateSaoFullNamePage.assertValidationErrorDisplayed(SAONameTooLongError)
    }

    Scenario(
      "Validate error when SAO name authorised to submit the certificate contains invalid characters",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("the user lands on the 'submit certificate confirm SAO' page")
      navigateToSubmitCertificateConfirmPage()

      When("the user enters a SAO name containing invalid characters")
      CertificateDeclarationSaoPage.addSaoName(TestData.nameWithInvalidCharacters)

      And("the user selects 'Continue'")
      CertificateDeclarationSaoPage.clickSubmissionButton()

      Then("an error is shown")
      CertificateDeclarationSaoPage.assertValidationErrorDisplayed(InvalidSAONameCharactersError)
    }

    Scenario(
      "Validate error when SAO name authorised to submit the certificate exceeds 105 characters",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("the user lands on the 'submit certificate confirm SAO' page")
      navigateToSubmitCertificateConfirmPage()

      When("the user enters a SAO name exceeding 105 characters")
      CertificateDeclarationSaoPage.addSaoName(TestData.nameCharacterLimitExceeded)

      And("the user selects 'Continue'")
      CertificateDeclarationSaoPage.clickSubmissionButton()

      Then("an error is shown")
      CertificateDeclarationSaoPage.assertValidationErrorDisplayed(SAONameTooLongError)
    }

    Scenario(
      "Validate error when SAO email authorised to submit the certificate exceeds 254 characters",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user initiates a certificate submission from the 'Account Homepage'")
      navigateToCertificateStartPage()

      When("the user selects 'Provide the SAO's details' link")
      CertificateTaskListPage.clickTaskListSectionLink(ProvideSaoDetails)

      And("the user enters SAO name")
      CertificateSaoFullNamePage.addName(TestData.firstPersonName)

      And("the user selects 'Continue'")
      CertificateSaoFullNamePage.clickSubmissionButton()

      Then("user lands on the 'What is email address for SAO Name?' page")
      assertOnPage(CertificateSaoEmailPage)

      And("the user enters a SAO email details exceeding 254 characters")
      CertificateSaoEmailPage.addEmail(TestData.emailCharacterLimitExceeded)

      And("the user selects 'Continue'")
      CertificateSaoEmailPage.clickSubmissionButton()

      Then("an error page is shown")
      assertPageWithError(CertificateSaoEmailPage)

      And("the error message 'Email address must be 254 characters or less' is displayed")
      CertificateSaoEmailPage.assertValidationErrorDisplayed(emailCharacterLimitExceededError)
    }

    Scenario(
      "Error is displayed when additional information exceeds the character limit",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user is on the 'Additional information' page")
      goToAdditionalInformationPageFromHomePage()

      When("the user enters additional information that exceeds the character limit")
      AdditionalInformationPage.addInformation(TestData.additionalInformationLimitExceeded)

      And("the user selects 'Continue")
      AdditionalInformationPage.clickSubmissionButton()

      Then("the error page is shown")
      assertPageWithError(AdditionalInformationPage)

      And(
        "the error message 'Additional information about your certificate must be 5000 characters or less' is displayed"
      )
      AdditionalInformationPage.assertValidationErrorDisplayed(CertificateAdditionalInformationTooLongError)
    }

    Scenario(
      "Validate errors when the authorised submitter attempts submits the certificate with empty fields",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("the user lands on the 'SAO Declaration' page")
      navigateToSubmitCertificateConfirmAsStandInSubmitterPage()

      When("the user selects 'Confirm'")
      CertificateDeclarationStandInPage.clickSubmissionButton()

      Then("the error page is shown")
      assertPageWithError(CertificateDeclarationStandInPage)

      And("the error message 'Enter your full name' is displayed")
      CertificateDeclarationStandInPage.assertValidationListErrorDisplayed(SAOStandInSubmitterNameEmpty)

      And("the error message 'Enter the name of the SAO who authorised you to submit the certificate' is displayed")
      CertificateDeclarationStandInPage.assertValidationListErrorDisplayed(SAOStandInSubmitterAuthorisedByNameEmpty)
    }

    Scenario(
      "Validate errors when authorised attempts to submit the certificate with fields where character limit is exceeded",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("the user lands on the 'Declaration' page")
      navigateToSubmitCertificateConfirmAsStandInSubmitterPage()

      When("the user enters the SAO name that exceeds 105 characters")
      CertificateDeclarationStandInPage.addSaoName(TestData.nameCharacterLimitExceeded)

      And("the user enters the stand in submitter name exceeding 105 characters")
      CertificateDeclarationStandInPage.addStandInSubmitterName(TestData.nameCharacterLimitExceeded)

      When("the user selects 'Confirm'")
      CertificateDeclarationStandInPage.clickSubmissionButton()

      Then("the error page is shown")
      assertPageWithError(CertificateDeclarationStandInPage)

      And("the error message 'Your name must be 105 characters or less' is displayed")
      CertificateDeclarationStandInPage.assertValidationListErrorDisplayed(SAOStandInSubmitterNameTooLongError)

      And("the error message 'Name of the SAO must be 105 characters or less' is displayed")
      CertificateDeclarationStandInPage.assertValidationListErrorDisplayed(SAONameTooLongError)

      CertificateDeclarationStandInPage.clearStandInNameField()
      CertificateDeclarationStandInPage.clearSaoNameField()
    }

    Scenario(
      "Validate errors when the authorised submitter attempts to submit the certificate with fields that have invalid characters",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("the user lands on the 'SAO Declaration' page")
      navigateToSubmitCertificateConfirmAsStandInSubmitterPage()

      When("the user enters the stand in submitter name with invalid characters")
      CertificateDeclarationStandInPage.addStandInSubmitterName(TestData.nameWithInvalidCharacters)

      And("the user enters a SAO with invalid characters")
      CertificateDeclarationStandInPage.addSaoName(TestData.nameWithInvalidCharacters)

      And("the user selects 'Confirm'")
      CertificateDeclarationStandInPage.clickSubmissionButton()

      Then("the error page is shown")
      assertPageWithError(CertificateDeclarationStandInPage)

      And("the error message 'Your name must not include <, > or \"' is displayed")
      CertificateDeclarationStandInPage.assertValidationListErrorDisplayed(SAOStandInSubmitterNameInvalid)

      And("the error message 'Name of the SAO must not include <, > or \"' is displayed")
      CertificateDeclarationStandInPage.assertValidationListErrorDisplayed(InvalidSAONameCharactersError)

      CertificateDeclarationStandInPage.clearStandInNameField()
      CertificateDeclarationStandInPage.clearSaoNameField()
    }

    Scenario(
      "Validate that an authorised SAO did not amend the details using the 'Change' Links on the CYA (Check Your Answers) page during the submission process",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given(
        "an authenticated sao user enters all mandatory details to submit a certificate from the 'Account Homepage'"
      )
      navigateToSubmitCertificateConfirmPage()
      CertificateDeclarationSaoPage.addSaoName(TestData.firstPersonName)
      CertificateDeclarationSaoPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.certificateSaoFullNameValue, TestData.firstPersonName)
      assertTextOnPage(CheckYourAnswersPage.certificateSaoEmailValue, TestData.firstPersonEmail)
      assertTextOnPage(CheckYourAnswersPage.certificateWhoIsSubmittingValue, "The SAO")
      assertTextOnPage(CheckYourAnswersPage.certificateDeclarationSaoValue, TestData.firstPersonName)
      assertTextOnPage(
        CheckYourAnswersPage.certificateAdditionalInformationValue,
        "Not provided"
      )

      When("the user selects the 'Change' link to amend the 'SAO name'")
      CheckYourAnswersPage.clickCertificateSaoNameChangeLink()

      Then("the user is redirected to 'What is the name of the SAO responsible for the certificate?' page")
      assertUrl(CertificateSaoFullNamePage.changeSaoFullNamePageUrl)

      And("the user clicks 'Continue' without amending the SAO name")
      CertificateSaoFullNamePage.clickSubmissionButton()

      Then("the user lands on the 'Check Your Answers' page")
      assertOnPage(CheckYourAnswersPage)

      And("the sao name value is unchanged")
      assertTextOnPage(CheckYourAnswersPage.certificateSaoFullNameValue, TestData.firstPersonName)

      When("the user selects the 'Change' link to amend the 'SAO email address'")
      CheckYourAnswersPage.clickCertificateSaoEmailChangeLink()

      Then("the user is redirected to 'What is the email address for SAO name?' page")
      assertUrl(CertificateSaoEmailPage.changeSaoEmailPageUrl)

      And("the user selects 'Continue' without changing the SAO email")
      CertificateSaoFullNamePage.clickSubmissionButton()

      Then("the user lands on the 'Check Your Answers' page")
      assertOnPage(CheckYourAnswersPage)

      Then("the SAO email value is unchanged")
      assertTextOnPage(CheckYourAnswersPage.certificateSaoEmailValue, TestData.firstPersonEmail)

      When("the user selects the 'Change' link to amend 'Who is submitting the certificate?' detail")
      CheckYourAnswersPage.clickCertificateWhoIsSubmittingChangeLink()

      Then("the user lands on the transaction 'Who is submitting the certificate?' page")
      assertUrl(CertificateWhoIsSubmittingPage.transactionCertificateWhoIsSubmittingPageUrl)

      When("the user selects 'Continue'")
      CertificateWhoIsSubmittingPage.clickSubmissionButton()

      Then("user lands on the 'Check Your Answer' page")
      assertOnPage(CheckYourAnswersPage)

      Then("the 'Who is submitting the certificate' value is unchanged")
      assertTextOnPage(CheckYourAnswersPage.certificateWhoIsSubmittingValue, "The SAO")

      When("the user selects the 'Change' link to amend 'SAO name on the declaration' detail")
      CheckYourAnswersPage.clickCertificateDeclarationSaoNameChangeLink()

      Then("the user is redirected to the 'Change SAO Declaration' page")
      assertUrl(CertificateDeclarationSaoPage.changeDeclarationSaoPageUrl)

      And("the user selects 'Confirm'")
      CertificateDeclarationSaoPage.clickSubmissionButton()

      Then("the user lands on the 'Check Your Answer' page")
      assertOnPage(CheckYourAnswersPage)

      Then("the SAO declaration value is unchanged")
      assertTextOnPage(CheckYourAnswersPage.certificateDeclarationSaoValue, TestData.firstPersonName)

      When("the user selects the 'Change' link to amend 'Additional information about your certificate' detail")
      CheckYourAnswersPage.clickCertificateAdditionalInformationLink()

      Then("the user is redirected to the 'changeCertificateAdditionalInformation' url")
      assertUrl(AdditionalInformationPage.changeCertificateAdditionalInformationPageUrl)

      When("the user selects 'Skip'")
      AdditionalInformationPage.clickSkipButton()

      Then("the user lands on the 'Check Your Answer' page")
      assertOnPage(CheckYourAnswersPage)

      Then("the 'Additional information' value is unchanged")
      assertTextOnPage(
        CheckYourAnswersPage.certificateAdditionalInformationValue,
        "Not provided"
      )
    }

    Scenario(
      "Validate that an authorised SAO amends the details using the Change Link on the CYA (Check Your Answers) page during the submission process",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given(
        "an authenticated sao user enters all mandatory details to submit a certificate from the 'Account Homepage'"
      )
      navigateToSubmitCertificateConfirmPage()
      CertificateDeclarationSaoPage.addSaoName(TestData.firstPersonName)
      CertificateDeclarationSaoPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)

      When("the user selects the 'Change' link to amend the 'SAO name'")
      CheckYourAnswersPage.clickCertificateSaoNameChangeLink()

      Then("the user is redirected to 'What is the name of the SAO responsible for the certificate?' page")
      assertUrl(CertificateSaoFullNamePage.changeSaoFullNamePageUrl)

      When("the user enters a different SAO name")
      CertificateSaoFullNamePage.addName(TestData.secondPersonName)

      And("the user selects 'Continue'")
      CertificateSaoFullNamePage.clickSubmissionButton()

      Then("the user lands on the 'Check Your Answers' page")
      assertOnPage(CheckYourAnswersPage)

      And("the new SAO name is displayed")
      assertTextOnPage(CheckYourAnswersPage.certificateSaoFullNameValue, TestData.secondPersonName)

      When("the user selects the 'Change' link to amend the 'SAO email address'")
      CheckYourAnswersPage.clickCertificateSaoEmailChangeLink()

      Then("the user is redirected to 'What is the email address for SAO name?' page")
      assertUrl(CertificateSaoEmailPage.changeSaoEmailPageUrl)

      When("the user enters a different SAO email address")
      CertificateSaoEmailPage.addEmail(TestData.secondPersonEmail)

      And("the user selects 'Continue'")
      CertificateSaoEmailPage.clickSubmissionButton()

      Then("the user lands on the 'Check Your Answers' page")
      assertOnPage(CheckYourAnswersPage)

      Then(
        "the new sao email value is displayed"
      )
      assertTextOnPage(CheckYourAnswersPage.certificateSaoEmailValue, TestData.secondPersonEmail)

      When("the user selects the 'Change' link to amend 'Who is submitting the certificate?' detail")
      CheckYourAnswersPage.clickCertificateWhoIsSubmittingChangeLink()

      Then("the user is redirected to the 'Who is submitting the certificate?' page")
      assertUrl(CertificateWhoIsSubmittingPage.transactionCertificateWhoIsSubmittingPageUrl)

      When("the user selects the 'A person authorised to submit on behalf of the SAO' radio option")
      CertificateWhoIsSubmittingPage.clickStandInSubmitterRadioButton()

      And("the user selects 'Continue'")
      CertificateWhoIsSubmittingPage.clickSubmissionButton()

      Then("the user lands on the 'stand in Declaration' page")
      assertUrl(CertificateDeclarationStandInPage.transactionDeclarationStandInPageUrl)

      When("user enters a Stand in submitter name")
      CertificateDeclarationStandInPage.addStandInSubmitterName(TestData.secondPersonName)

      And("the user enters the SAO name")
      CertificateDeclarationStandInPage.addSaoName(TestData.firstPersonName)

      And("the user selects 'Confirm'")
      CertificateDeclarationStandInPage.clickSubmissionButton()

      Then("the user lands on 'Check Your Answers' page")
      assertOnPage(CheckYourAnswersPage)

      Then("the amended 'Who is submitting the certificate?' value is correctly displayed")
      assertTextOnPage(
        CheckYourAnswersPage.certificateWhoIsSubmittingValue,
        "A person authorised to submit on behalf of the SAO"
      )

      And("the 'Authorised name on the declaration' value is displayed")
      assertTextOnPage(CheckYourAnswersPage.certificateDeclarationStandInValue, TestData.secondPersonName)

      And("the 'SAO name on the declaration' value is displayed")
      assertTextOnPage(CheckYourAnswersPage.certificateDeclarationSaoValue, TestData.firstPersonName)

      When("the user selects the 'Change' link to amend the 'Additional information about your certificate' value")
      CheckYourAnswersPage.clickCertificateAdditionalInformationLink()

      Then("the user is redirected to the 'Change Additional information about your certificate' page")
      assertUrl(AdditionalInformationPage.changeCertificateAdditionalInformationPageUrl)

      When("the user enters additional information")
      AdditionalInformationPage.addInformation("additional information for this certificate")

      When("the user selects 'Continue'")
      AdditionalInformationPage.clickSubmissionButton()

      Then("the user lands on 'Check Your Answers' page")
      assertOnPage(CheckYourAnswersPage)

      And("the new additional information is displayed")
      assertTextOnPage(
        CheckYourAnswersPage.certificateAdditionalInformationValue,
        "additional information for this certificate"
      )
    }
  }

  private def navigateToCertificateStartPage(): Unit = {
    assertOnPage(AccountHomePage)
    AccountHomePage.clickMakeSubmissionLink()
    assertOnPage(SubmissionTypePage)
    SubmissionTypePage.clickCertificateRadioButton()
    SubmissionTypePage.clickSubmissionButton()
    assertOnPage(CertificateTaskListPage)
  }

  private def goToAdditionalInformationPageFromHomePage(): Unit = {
    navigateToCertificateStartPage()
    completeProvideSaoDetailsTask()
    completeUploadSubmissionTemplateTask()
    CertificateTaskListPage.clickTaskListSectionLink(SubmitCertificate)
    assertOnPage(AdditionalInformationPage)
  }

  private def completeProvideSaoDetailsTask(): Unit = {
    CertificateTaskListPage.clickTaskListSectionLink(ProvideSaoDetails)
    assertOnPage(CertificateSaoFullNamePage)
    CertificateSaoFullNamePage.addName(TestData.firstPersonName)
    CertificateSaoFullNamePage.clickSubmissionButton()
    assertOnPage(CertificateSaoEmailPage)
    CertificateSaoEmailPage.addEmail(TestData.firstPersonEmail)
    CertificateSaoEmailPage.clickSubmissionButton()
    assertOnPage(CertificateTaskListPage)
  }

  private def completeUploadSubmissionTemplateTask(): Unit = {
    CertificateTaskListPage.clickTaskListSectionLink(UploadSubmissionTemplate)
    assertOnPage(UploadSubmissionTemplatePage)
    UploadSubmissionTemplatePage.uploadAndSubmit(FourCompaniesFile)
    assertOnPage(UploadReviewQualifiedPage)
    UploadReviewQualifiedPage.clickSubmissionButton()
    assertOnPage(UploadReviewUnqualifiedPage)
    UploadReviewUnqualifiedPage.clickSubmissionButton()
    assertOnPage(CertificateTaskListPage)
  }

  private def navigateToSubmitCertificateConfirmPage(): Unit = {
    goToAdditionalInformationPageFromHomePage()
    AdditionalInformationPage.clickSkipButton()
    assertOnPage(CertificateWhoIsSubmittingPage)
    CertificateWhoIsSubmittingPage.clickSaoSubmitterRadioButton()
    CertificateWhoIsSubmittingPage.clickSubmissionButton()
    assertOnPage(CertificateDeclarationSaoPage)
  }

  private def navigateToSubmitCertificateConfirmAsStandInSubmitterPage(): Unit = {
    goToAdditionalInformationPageFromHomePage()
    AdditionalInformationPage.clickSkipButton()
    assertOnPage(CertificateWhoIsSubmittingPage)
    CertificateWhoIsSubmittingPage.clickStandInSubmitterRadioButton()
    CertificateWhoIsSubmittingPage.clickSubmissionButton()
    assertOnPage(CertificateDeclarationStandInPage)
  }
}
