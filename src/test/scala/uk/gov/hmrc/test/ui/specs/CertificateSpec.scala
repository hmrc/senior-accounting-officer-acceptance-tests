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
import uk.gov.hmrc.test.ui.adt.UploadFile.FourCompaniesFile
import uk.gov.hmrc.test.ui.pages.submission.*
import uk.gov.hmrc.test.ui.pages.submission.certificate.*
import uk.gov.hmrc.test.ui.pages.{AccountHomePage, AuthorityWizardPage}
import uk.gov.hmrc.test.ui.specs.tags.*
import uk.gov.hmrc.test.ui.support.PageSupport.*
import uk.gov.hmrc.test.ui.support.TestData

class CertificateSpec extends BaseSpec {
  override def beforeEach(): Unit = {
    super.beforeEach()
    AuthorityWizardPage.withAffinityGroup(Organisation).withDsaoEnrolment("123").redirectToHomePage()
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

      Then("the task list displays each element in the correct state with the correct status")
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
      "An authorised senior accounting officer can submit a certificate successfully from the 'Account Homepage'",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user initiates a certificate submission from the 'Account Homepage'")
      navigateToCertificateStartPage()

      When("the 'Provide the SAO's details' link is clicked")
      CertificateTaskListPage.clickTaskListSectionLink(ProvideSaoDetails)

      Then("the user lands on the 'SAO full name' page")
      assertOnPage(CertificateSaoFullNamePage)

      When("the 'Continue' button is clicked after a valid SAO name is added")
      CertificateSaoFullNamePage.addName(TestData.firstPersonName)
      CertificateSaoFullNamePage.clickSubmissionButton()

      Then("the user lands on the 'SAO email' page")
      assertOnPage(CertificateSaoEmailPage)

      When("the user clicks 'Continue' after adding a valid SAO email")
      CertificateSaoEmailPage.addEmail(TestData.firstPersonEmail)
      CertificateSaoEmailPage.clickSubmissionButton()

      Then("the user returns to the 'Certificate Task List'")
      assertUrl(CertificateTaskListPage.taskListTwoPageUrl)

      And("the task list displays each element in the correct state with the correct status")
      CertificateTaskListPage.assertTaskListSectionStatus(ProvideSaoDetails, Completed)
      CertificateTaskListPage.assertStatusNotHighlighted(ProvideSaoDetails)
      CertificateTaskListPage.assertTaskListSectionNameIsNotHyperlink(ProvideSaoDetails)
      CertificateTaskListPage.assertTaskListSectionStatus(UploadSubmissionTemplate, NotStarted)
      CertificateTaskListPage.assertStatusHighlightedBlue(UploadSubmissionTemplate)
      CertificateTaskListPage.assertTaskListSectionNameIsHyperlink(UploadSubmissionTemplate)
      CertificateTaskListPage.assertTaskListSectionStatus(SubmitCertificate, CannotStartYet)
      CertificateTaskListPage.assertStatusNotHighlighted(SubmitCertificate)
      CertificateTaskListPage.assertTaskListSectionNameIsNotHyperlink(SubmitCertificate)

      When("the 'Upload the submission template' link is clicked")
      CertificateTaskListPage.clickTaskListSectionLink(UploadSubmissionTemplate)

      Then("the user lands on the 'Upload a submission template' page")
      assertOnPage(UploadSubmissionTemplatePage)

      And("the guidance link is present and correct")
      UploadSubmissionTemplatePage.assertTemplateGuidanceLinkFoundWithCorrectAttributes()

      When("the user clicks 'Continue' after choosing a file for upload")
      UploadSubmissionTemplatePage.upload(FourCompaniesFile)

      Then("the user lands on the 'Review the companies with a qualifying certificate' page")
      assertOnPage(UploadReviewQualifiedPage)

      When("the 'Continue' button is clicked")
      UploadReviewQualifiedPage.clickSubmissionButton()

      Then("the user lands on the 'Review Unqualified' page")
      assertOnPage(UploadReviewUnqualifiedPage)

      When("the 'Continue' button is clicked")
      UploadReviewUnqualifiedPage.clickSubmissionButton()

      Then("the user returns to the 'Certificate Task List'")
      assertUrl(CertificateTaskListPage.taskListThreePageUrl)

      And("the task list displays each element in the correct state with the correct status")
      CertificateTaskListPage.assertTaskListSectionStatus(ProvideSaoDetails, Completed)
      CertificateTaskListPage.assertStatusNotHighlighted(ProvideSaoDetails)
      CertificateTaskListPage.assertTaskListSectionNameIsNotHyperlink(ProvideSaoDetails)
      CertificateTaskListPage.assertTaskListSectionStatus(UploadSubmissionTemplate, Completed)
      CertificateTaskListPage.assertStatusNotHighlighted(UploadSubmissionTemplate)
      CertificateTaskListPage.assertTaskListSectionNameIsNotHyperlink(UploadSubmissionTemplate)
      CertificateTaskListPage.assertTaskListSectionStatus(SubmitCertificate, NotStarted)
      CertificateTaskListPage.assertStatusHighlightedBlue(SubmitCertificate)
      CertificateTaskListPage.assertTaskListSectionNameIsHyperlink(SubmitCertificate)

      When("the user continues from the additional information task")
      CertificateTaskListPage.clickTaskListSectionLink(SubmitCertificate)

      Then("the user lands on the 'Additional information' page")
      assertOnPage(AdditionalInformationPage)

      When("the user clicks 'Continue' after adding additional information")
      AdditionalInformationPage.addInformation("No additional information for this certificate")
      AdditionalInformationPage.clickSubmissionButton()

      Then("the user lands on the 'Who is submitting' page")
      assertOnPage(CertificateWhoIsSubmittingPage)

      When("the user clicks 'Continue' after selecting the SAO submitter radio option")
      CertificateWhoIsSubmittingPage.clickSaoSubmitterRadioButton()
      CertificateWhoIsSubmittingPage.clickSubmissionButton()

      Then("the user lands on the 'SAO declaration' page")
      assertOnPage(CertificateDeclarationSaoPage)

      When("the user clicks 'Continue' after adding the SAO name to complete the declaration")
      CertificateDeclarationSaoPage.addSaoName(TestData.firstPersonName)
      CertificateDeclarationSaoPage.clickSubmissionButton()

      Then("the user lands on the 'Check your answers' page")
      assertOnPage(CheckYourAnswersPage)

      When("the user clicks 'Continue'")
      CheckYourAnswersPage.clickSubmissionButton()

      Then("the user lands on the 'Confirmation' page")
      assertOnPage(ConfirmationPage)

      When("the user clicks 'Continue'")
      ConfirmationPage.clickSubmissionButton()

      Then("the user returns to the 'Certificate Task List'")
      assertUrl(CertificateTaskListPage.taskListCompletePageUrl)

      And("the task list displays each element in the correct state with the correct status")
      CertificateTaskListPage.assertTaskListSectionStatus(ProvideSaoDetails, Completed)
      CertificateTaskListPage.assertStatusNotHighlighted(ProvideSaoDetails)
      CertificateTaskListPage.assertTaskListSectionNameIsNotHyperlink(ProvideSaoDetails)
      CertificateTaskListPage.assertTaskListSectionStatus(UploadSubmissionTemplate, Completed)
      CertificateTaskListPage.assertStatusNotHighlighted(UploadSubmissionTemplate)
      CertificateTaskListPage.assertTaskListSectionNameIsNotHyperlink(UploadSubmissionTemplate)
      CertificateTaskListPage.assertTaskListSectionStatus(SubmitCertificate, Completed)
      CertificateTaskListPage.assertStatusNotHighlighted(SubmitCertificate)
      CertificateTaskListPage.assertTaskListSectionNameIsNotHyperlink(SubmitCertificate)

      When("the user clicks on 'Go back to homepage' button")
      CertificateTaskListPage.clickSubmissionButton()

      Then("user returns to the Account homepage")
      assertOnPage(AccountHomePage)
    }

    Scenario(
      "A user can submit a certificate successfully from the 'Account Homepage' on behalf of their SAO",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user initiates a certificate submission from the 'Account Homepage'")
      navigateToCertificateStartPage()

      When("the 'Provide the SAO's details' link is clicked")
      CertificateTaskListPage.clickTaskListSectionLink(ProvideSaoDetails)

      Then("the user lands on the 'SAO full name' page")
      assertOnPage(CertificateSaoFullNamePage)

      When("the user clicks 'Continue' after adding a valid SAO name")
      CertificateSaoFullNamePage.addName(TestData.firstPersonName)
      CertificateSaoFullNamePage.clickSubmissionButton()

      Then("the user lands on the 'SAO email' page")
      assertOnPage(CertificateSaoEmailPage)

      When("the user clicks 'Continue' after adding a valid SAO email")
      CertificateSaoEmailPage.addEmail(TestData.firstPersonEmail)
      CertificateSaoEmailPage.clickSubmissionButton()

      Then("the user returns to the 'Certificate Task List'")
      assertUrl(CertificateTaskListPage.taskListTwoPageUrl)

      When("the 'Upload the submission template' link is clicked")
      CertificateTaskListPage.clickTaskListSectionLink(UploadSubmissionTemplate)

      Then("the user lands on the 'Upload a submission template' page")
      assertOnPage(UploadSubmissionTemplatePage)

      When("the 'Continue' button is clicked after choosing a file for upload")
      UploadSubmissionTemplatePage.upload(FourCompaniesFile)

      Then("the user lands on the 'Review the companies with a qualifying certificate' page")
      assertOnPage(UploadReviewQualifiedPage)

      When("the 'Continue' button is clicked")
      UploadReviewQualifiedPage.clickSubmissionButton()

      Then("the user lands on the 'Review Unqualified' page")
      assertOnPage(UploadReviewUnqualifiedPage)

      When("the 'Continue' button is clicked")
      UploadReviewUnqualifiedPage.clickSubmissionButton()

      Then("the user returns to the 'Certificate Task List'")
      assertUrl(CertificateTaskListPage.taskListThreePageUrl)

      When("the user continues from the additional information task")
      CertificateTaskListPage.clickTaskListSectionLink(SubmitCertificate)

      Then("the user lands on the 'Additional information' page")
      assertOnPage(AdditionalInformationPage)

      When("the user clicks 'Continue' after adding additional information")
      AdditionalInformationPage.addInformation("No additional information for this certificate")
      AdditionalInformationPage.clickSubmissionButton()

      Then("the user lands on the 'Who is submitting' page")
      assertOnPage(CertificateWhoIsSubmittingPage)

      When("the user clicks 'Continue' after selecting the stand-in submitter radio option")
      CertificateWhoIsSubmittingPage.clickStandInSubmitterRadioButton()
      CertificateWhoIsSubmittingPage.clickSubmissionButton()

      Then("the user lands on the 'SAO stand-in declaration' page")
      assertOnPage(CertificateDeclarationStandInPage)

      When("the user clicks 'Continue' after adding the SAO and stand-in submitter names to complete the declaration")
      CertificateDeclarationStandInPage.addSaoName(TestData.firstPersonName)
      CertificateDeclarationStandInPage.addStandInSubmitterName(TestData.secondPersonName)
      CertificateDeclarationStandInPage.clickSubmissionButton()

      Then("the user lands on the 'Check your answers' page")
      assertOnPage(CheckYourAnswersPage)

      When("the user clicks 'Continue'")
      CheckYourAnswersPage.clickSubmissionButton()

      Then("the user lands on the 'Confirmation' page")
      assertOnPage(ConfirmationPage)

      When("the user clicks 'Continue'")
      ConfirmationPage.clickSubmissionButton()

      Then("the user returns to the 'Certificate Task List'")
      assertUrl(CertificateTaskListPage.taskListCompletePageUrl)
    }

    Scenario(
      "Re-upload the same company details with 'unqualified' certificates in a certificate submission",
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
      assertUrl(CertificateTaskListPage.taskListTwoPageUrl)

      When("a submission template is successfully uploaded")
      CertificateTaskListPage.clickTaskListSectionLink(UploadSubmissionTemplate)
      assertOnPage(UploadSubmissionTemplatePage)
      UploadSubmissionTemplatePage.upload(FourCompaniesFile)

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
      UploadSubmissionTemplatePage.upload(FourCompaniesFile)

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

      Then("the user returns to the 'Certificate Task List'")
      assertUrl(CertificateTaskListPage.taskListThreePageUrl)

      And("the 'Upload the submission template' task is shown as completed")
      CertificateTaskListPage.assertTaskListSectionStatus(UploadSubmissionTemplate, Completed)
    }

    Scenario(
      "Validate that mandatory details are required for a certification submission",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'What would you like to submit?' page")
      assertOnPage(AccountHomePage)
      AccountHomePage.clickSubmitCertificateLink()
      assertOnPage(SubmissionTypePage)

      When("the 'Continue' button is clicked after neither radio button is selected")
      SubmissionTypePage.clickSubmissionButton()

      Then("an error message is displayed")
      SubmissionTypePage.assertErrorSummaryDisplayed()

      When("the 'Continue' button is clicked after the 'A certificate' radio button is selected")
      SubmissionTypePage.clickCertificateRadioButton()
      SubmissionTypePage.clickSubmissionButton()

      Then("the user lands on the 'Submit a certificate' page")
      assertUrl(CertificateTaskListPage.taskListOnePageUrl)

      When("the 'Provide the SAO's details' link is clicked")
      CertificateTaskListPage.clickTaskListSectionLink(ProvideSaoDetails)

      Then("the user lands on the 'SAO full name' page")
      assertOnPage(CertificateSaoFullNamePage)

      When("the 'Continue' button is clicked after no name was added")
      CertificateSaoFullNamePage.clickSubmissionButton()

      Then("an error message is displayed")
      CertificateSaoFullNamePage.assertErrorSummaryDisplayed()

      When("the 'Continue' button is clicked after adding a valid SAO name")
      CertificateSaoFullNamePage.addName(TestData.firstPersonName)
      CertificateSaoFullNamePage.clickSubmissionButton()

      Then("the user lands on the 'SAO email' page")
      assertOnPage(CertificateSaoEmailPage)

      And("the page heading displays the correct SAO name")
      CertificateSaoEmailPage.assertHeadingMatches(s"What is the email address for ${TestData.firstPersonName}?")

      When("the user clicks 'Continue' after adding an invalid SAO email")
      CertificateSaoEmailPage.addEmail("abc123")
      CertificateSaoEmailPage.clickSubmissionButton()

      Then("an error message is displayed")
      CertificateSaoEmailPage.assertErrorSummaryDisplayed()

      When("the user enters a valid SAO email and clicks 'Continue'")
      CertificateSaoEmailPage.addEmail(TestData.firstPersonEmail)
      CertificateSaoEmailPage.clickSubmissionButton()

      Then("the user returns to the 'Certificate Task List'")
      assertUrl(CertificateTaskListPage.taskListTwoPageUrl)

      When("the 'Upload the submission template' link is clicked")
      CertificateTaskListPage.clickTaskListSectionLink(UploadSubmissionTemplate)

      Then("the user lands on the 'Upload a submission template' page")
      assertOnPage(UploadSubmissionTemplatePage)

      When("the 'Continue' button is clicked after choosing a file for upload")
      UploadSubmissionTemplatePage.upload(FourCompaniesFile)

      Then("the user lands on the 'Review the companies with a qualifying certificate' page")
      assertOnPage(UploadReviewQualifiedPage)

      When("the 'Continue' button is clicked")
      UploadReviewQualifiedPage.clickSubmissionButton()

      Then("the user lands on the 'Review Unqualified' page")
      assertOnPage(UploadReviewUnqualifiedPage)

      When("the 'Continue' button is clicked")
      UploadReviewUnqualifiedPage.clickSubmissionButton()

      Then("the user returns to the 'Certificate Task List'")
      assertUrl(CertificateTaskListPage.taskListThreePageUrl)

      When("the user continues from the additional information task")
      CertificateTaskListPage.clickTaskListSectionLink(SubmitCertificate)

      Then("the user lands on the 'Additional information' page")
      assertOnPage(AdditionalInformationPage)

      When("pressing continue without providing additional information")
      AdditionalInformationPage.clickSubmissionButton()

      Then("an error appears on screen")
      AdditionalInformationPage.assertErrorSummaryDisplayed()

      When("the user clicks 'Continue' after adding additional information")
      AdditionalInformationPage.addInformation("No additional information for this certificate")
      AdditionalInformationPage.clickSubmissionButton()

      Then("the user lands on the 'Who is submitting' page")
      assertOnPage(CertificateWhoIsSubmittingPage)

      When("the 'Continue' button is clicked after neither radio button is selected")
      CertificateWhoIsSubmittingPage.clickSubmissionButton()

      Then("an error message is displayed")
      CertificateWhoIsSubmittingPage.assertErrorSummaryDisplayed()

      When("the user clicks 'Continue' after selecting the SAO submitter radio option")
      CertificateWhoIsSubmittingPage.clickSaoSubmitterRadioButton()
      CertificateWhoIsSubmittingPage.clickSubmissionButton()

      Then("the user lands on the SAO's 'Confirm the certificate' page")
      assertOnPage(CertificateDeclarationSaoPage)

      When("the 'Confirm' button is clicked after no name is added")
      CertificateDeclarationSaoPage.clickSubmissionButton()

      Then("an error message is displayed")
      CertificateDeclarationSaoPage.assertErrorSummaryDisplayed()

      When("the user clicks 'Continue' after adding the SAO name to complete the declaration")
      CertificateDeclarationSaoPage.addSaoName(TestData.firstPersonName)
      CertificateDeclarationSaoPage.clickSubmissionButton()

      Then("the user lands on the 'Check your answers' page")
      assertOnPage(CheckYourAnswersPage)

      // TODO validation assertion for 'Check your answers' in the 'SAO declaration' variation

      When("the 'Back' link is clicked")
      CheckYourAnswersPage.clickBackLink()

      Then("the user lands on the SAO's 'Confirm the certificate' page showing the previous error")
      assertOnPage(CertificateDeclarationSaoPage)

      When("the 'Back' link is clicked")
      CertificateWhoIsSubmittingPage.clickBackLink()

      Then("the user lands on the SAO's 'Confirm the certificate' page with no error showing")
      assertOnPage(CertificateDeclarationSaoPage)

      When("the 'Back' link is clicked")
      CertificateWhoIsSubmittingPage.clickBackLink()

      Then("the user lands on the 'Who is submitting' page")
      assertOnPage(CertificateWhoIsSubmittingPage)

      When("the user clicks 'Continue' after selecting the stand-in submitter radio option")
      CertificateWhoIsSubmittingPage.clickStandInSubmitterRadioButton()
      CertificateWhoIsSubmittingPage.clickSubmissionButton()

      Then("the user lands on the 'SAO stand-in declaration' page")
      assertOnPage(CertificateDeclarationStandInPage)

      When("the 'Confirm' button is clicked after no names are entered")
      CertificateDeclarationStandInPage.clickSubmissionButton()

      Then("an error message is displayed")
      CertificateDeclarationStandInPage.assertErrorSummaryDisplayed()

      When("the 'Confirm' button is clicked after an 'SAO' name is entered but not the 'stand in submitter' name")
      CertificateDeclarationStandInPage.addSaoName(TestData.firstPersonName)
      CertificateDeclarationStandInPage.clearStandInNameField()
      CertificateDeclarationStandInPage.clickSubmissionButton()

      Then("an error message is displayed")
      CertificateDeclarationStandInPage.assertErrorSummaryDisplayed()

      When("the 'Confirm' button is clicked after an 'stand in submitter' name is entered but not the 'SAO' name")
      CertificateDeclarationStandInPage.addStandInSubmitterName(TestData.secondPersonName)
      CertificateDeclarationStandInPage.clearSaoNameField()
      CertificateDeclarationStandInPage.clickSubmissionButton()

      Then("an error message is displayed")
      CertificateDeclarationStandInPage.assertErrorSummaryDisplayed()

      When("the 'Confirm' button is clicked after both names are entered")
      CertificateDeclarationStandInPage.addSaoName(TestData.firstPersonName)
      CertificateDeclarationStandInPage.addStandInSubmitterName(TestData.secondPersonName)
      CertificateDeclarationStandInPage.clickSubmissionButton()

      Then("the user lands on the 'Check your answers' page")
      assertOnPage(CheckYourAnswersPage)

      // TODO validation assertion for 'Check your answers' in the 'Stand-in declaration' variation
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
      assertUrl(CertificateTaskListPage.taskListTwoPageUrl)

      When("a submission template is successfully uploaded")
      CertificateTaskListPage.clickTaskListSectionLink(UploadSubmissionTemplate)
      assertOnPage(UploadSubmissionTemplatePage)
      UploadSubmissionTemplatePage.upload(FourCompaniesFile)

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
      UploadSubmissionTemplatePage.upload(FourCompaniesFile)

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
      "Additional information can be skipped during a certificate submission",
      CertificateUITests,
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the additional information page during a certificate submission")
      goToAdditionalInformationPageFromHomePage()

      When("clicking 'skip' without providing additional information")
      AdditionalInformationPage.clickSkipButton()

      Then("the user lands on the 'Who is submitting the certificate?' page")
      assertOnPage(CertificateWhoIsSubmittingPage)

      When("the user clicks the 'back' link")
      CertificateWhoIsSubmittingPage.clickBackLink()

      Then("the user lands on the 'Additional Information' page")
      assertOnPage(AdditionalInformationPage)

      When("clicking 'skip' after providing additional information")
      AdditionalInformationPage.addInformation("Adding information again")
      AdditionalInformationPage.clickSkipButton()

      Then("the user lands on the 'Who is submitting the certificate?' page")
      assertOnPage(CertificateWhoIsSubmittingPage)

      // TODO: extend the test to check the additional information on the CYA page once development is complete
    }
  }

  private def navigateToCertificateStartPage(): Unit = {
    AuthorityWizardPage.withAffinityGroup(Organisation).redirectToHomePage()
    assertOnPage(AccountHomePage)
    AccountHomePage.clickSubmitCertificateLink()
    assertOnPage(SubmissionTypePage)
    SubmissionTypePage.clickCertificateRadioButton()
    SubmissionTypePage.clickSubmissionButton()
    assertUrl(CertificateTaskListPage.taskListOnePageUrl)
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
    assertUrl(CertificateTaskListPage.taskListTwoPageUrl)
  }

  private def completeUploadSubmissionTemplateTask(): Unit = {
    CertificateTaskListPage.clickTaskListSectionLink(UploadSubmissionTemplate)
    assertOnPage(UploadSubmissionTemplatePage)
    UploadSubmissionTemplatePage.upload(FourCompaniesFile)
    assertOnPage(UploadReviewQualifiedPage)
    UploadReviewQualifiedPage.clickSubmissionButton()
    assertOnPage(UploadReviewUnqualifiedPage)
    UploadReviewUnqualifiedPage.clickSubmissionButton()
    assertUrl(CertificateTaskListPage.taskListThreePageUrl)
  }
}
