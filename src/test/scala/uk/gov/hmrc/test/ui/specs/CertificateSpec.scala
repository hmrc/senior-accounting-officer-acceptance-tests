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
import uk.gov.hmrc.test.ui.pages.submission.*
import uk.gov.hmrc.test.ui.pages.submission.certificate.*
import uk.gov.hmrc.test.ui.pages.{AccountHomePage, AuthorityWizardPage}
import uk.gov.hmrc.test.ui.specs.tags.*
import uk.gov.hmrc.test.ui.support.PageSupport.*
import uk.gov.hmrc.test.ui.support.TestData

class CertificateSpec extends BaseSpec {
  override def beforeEach(): Unit = {
    super.beforeEach()
    AuthorityWizardPage.withAffinityGroup(Organisation).redirectToHomePage()
  }

  Feature("Submit Certificate") {
    // The  below scenario would be extended as in when the pages/features are ready
    Scenario(
      "An authorised senior accounting officer can submit a certificate successfully from the 'Account Homepage'",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user initiates a certificate submission from the 'Account Homepage' page")
      navigateToCertificateStartPage()

      When("the 'Provide the SAO's details' link is clicked")
      CertificateTaskListPage.clickTask1()

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

      Then("the user returns to the certificate task list")
      assertOnPage(CertificateTaskListPage)

      When("the 'Upload the submission template' link is clicked")
      CertificateTaskListPage.clickTask2()

      Then("the user lands on the 'Upload a submission template' page")
      assertOnPage(UploadSubmissionTemplatePage)

      When("the user clicks 'Continue' after choosing a file for upload")
      UploadSubmissionTemplatePage.clickSubmissionButton()

      Then("the user lands on the 'Review Qualified' page")
      assertOnPage(UploadReviewQualifiedPage)

      When("the 'Continue' button is clicked")
      UploadReviewQualifiedPage.clickSubmissionButton()

      Then("the user lands on the 'Review Unqualified' page")
      assertOnPage(UploadReviewUnqualifiedPage)

      When("the 'Continue' button is clicked")
      UploadReviewUnqualifiedPage.clickSubmissionButton()

      Then("the user returns to the certificate task list")
      assertOnPage(CertificateTaskListPage)

      When("the user continues from the additional information task")
      CertificateTaskListPage.clickTask3()

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

      Then("the user returns to the certificate task list")
      assertOnPage(CertificateTaskListPage)
    }

    Scenario(
      "A user can submit a certificate successfully from the 'Account Homepage' on behalf of their SAO",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user initiates a certificate submission from the 'Account Homepage' page")
      navigateToCertificateStartPage()

      When("the 'Provide the SAO's details' link is clicked")
      CertificateTaskListPage.clickTask1()

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

      Then("the user returns to the certificate task list")
      assertOnPage(CertificateTaskListPage)

      When("the 'Upload the submission template' link is clicked")
      CertificateTaskListPage.clickTask2()

      Then("the user lands on the 'Upload a submission template' page")
      assertOnPage(UploadSubmissionTemplatePage)

      When("the 'Continue' button is clicked after choosing a file for upload")
      UploadSubmissionTemplatePage.clickSubmissionButton()

      Then("the user lands on the 'Review Qualified' page")
      assertOnPage(UploadReviewQualifiedPage)

      When("the 'Continue' button is clicked")
      UploadReviewQualifiedPage.clickSubmissionButton()

      Then("the user lands on the 'Review Unqualified' page")
      assertOnPage(UploadReviewUnqualifiedPage)

      When("the 'Continue' button is clicked")
      UploadReviewUnqualifiedPage.clickSubmissionButton()

      Then("the user returns to the certificate task list")
      assertOnPage(CertificateTaskListPage)

      When("the user continues from the additional information task")
      CertificateTaskListPage.clickTask3()

      Then("the user lands on the 'Additional information' page")
      assertOnPage(AdditionalInformationPage)

      When("the user clicks 'Continue' after adding additional information")
      AdditionalInformationPage.addInformation("No additional information for this certificate")
      AdditionalInformationPage.clickSubmissionButton()

      Then("the user lands on the 'Who is submitting' page")
      assertOnPage(CertificateWhoIsSubmittingPage)

      When("the user clicks 'Continue' after selecting the SAO submitter radio option")
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

      Then("the user returns to the certificate task list")
      assertOnPage(CertificateTaskListPage)
    }

    Scenario(
      "Validate that mandatory details are required for a certification submission",
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
      SubmissionTypePage.assertErrorShownOnPage()

      When("the 'Continue' button is clicked after the 'A certificate' radio button is selected")
      SubmissionTypePage.clickCertificateRadioButton()
      SubmissionTypePage.clickSubmissionButton()

      Then("the user lands on the 'Submit a certificate' page")
      assertOnPage(CertificateTaskListPage)

      When("the 'Provide the SAO's details' link is clicked")
      CertificateTaskListPage.clickTask1()

      Then("the user lands on the 'SAO full name' page")
      assertOnPage(CertificateSaoFullNamePage)

      When("the 'Continue' button is clicked after no name was added")
      CertificateSaoFullNamePage.clickSubmissionButton()

      Then("an error message is displayed")
      CertificateSaoFullNamePage.assertErrorShownOnPage()

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
      CertificateSaoEmailPage.assertErrorShownOnPage()

      When("the user enters a valid SAO email and clicks 'Continue'")
      CertificateSaoEmailPage.addEmail(TestData.firstPersonEmail)
      CertificateSaoEmailPage.clickSubmissionButton()

      Then("the user returns to the certificate task list")
      assertOnPage(CertificateTaskListPage)

      When("the 'Upload the submission template' link is clicked")
      CertificateTaskListPage.clickTask2()

      Then("the user lands on the 'Upload a submission template' page")
      assertOnPage(UploadSubmissionTemplatePage)

      When("the 'Continue' button is clicked after choosing a file for upload")
      UploadSubmissionTemplatePage.clickSubmissionButton()

      Then("the user lands on the 'Review Qualified' page")
      assertOnPage(UploadReviewQualifiedPage)

      When("the 'Continue' button is clicked")
      UploadReviewQualifiedPage.clickSubmissionButton()

      Then("the user lands on the 'Review Unqualified' page")
      assertOnPage(UploadReviewUnqualifiedPage)

      When("the 'Continue' button is clicked")
      UploadReviewUnqualifiedPage.clickSubmissionButton()

      Then("the user returns to the certificate task list")
      assertOnPage(CertificateTaskListPage)

      When("the user continues from the additional information task")
      CertificateTaskListPage.clickTask3()

      Then("the user lands on the 'Additional information' page")
      assertOnPage(AdditionalInformationPage)

      // TODO validation assertion for 'Additional information'

      When("the user clicks 'Continue' after adding additional information")
      AdditionalInformationPage.addInformation("No additional information for this certificate")
      AdditionalInformationPage.clickSubmissionButton()

      Then("the user lands on the 'Who is submitting' page")
      assertOnPage(CertificateWhoIsSubmittingPage)

      When("the 'Continue' button is clicked after neither radio button is selected")
      CertificateWhoIsSubmittingPage.clickSubmissionButton()

      Then("an error message is displayed")
      CertificateWhoIsSubmittingPage.assertErrorShownOnPage()

      When("the user clicks 'Continue' after selecting the SAO submitter radio option")
      CertificateWhoIsSubmittingPage.clickSaoSubmitterRadioButton()
      CertificateWhoIsSubmittingPage.clickSubmissionButton()

      Then("the user lands on the SAO's 'Confirm the certificate' page")
      assertOnPage(CertificateDeclarationSaoPage)

      When("the 'Confirm' button is clicked after no name is added")
      CertificateDeclarationSaoPage.clickSubmissionButton()

      Then("an error message is displayed")
      CertificateDeclarationSaoPage.assertErrorShownOnPage()

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
      CertificateDeclarationStandInPage.assertErrorShownOnPage()

      When("the 'Confirm' button is clicked after an 'SAO' name is entered but not the 'stand in submitter' name")
      CertificateDeclarationStandInPage.addSaoName(TestData.firstPersonName)
      CertificateDeclarationStandInPage.clearStandInNameField()
      CertificateDeclarationStandInPage.clickSubmissionButton()

      Then("an error message is displayed")
      CertificateDeclarationStandInPage.assertErrorShownOnPage()

      When("the 'Confirm' button is clicked after an 'stand in submitter' name is entered but not the 'SAO' name")
      CertificateDeclarationStandInPage.addStandInSubmitterName(TestData.secondPersonName)
      CertificateDeclarationStandInPage.clearSaoNameField()
      CertificateDeclarationStandInPage.clickSubmissionButton()

      Then("an error message is displayed")
      CertificateDeclarationStandInPage.assertErrorShownOnPage()

      When("the 'Confirm' button is clicked after both names are entered")
      CertificateDeclarationStandInPage.addSaoName(TestData.firstPersonName)
      CertificateDeclarationStandInPage.addStandInSubmitterName(TestData.secondPersonName)
      CertificateDeclarationStandInPage.clickSubmissionButton()

      Then("the user lands on the 'Check your answers' page")
      assertOnPage(CheckYourAnswersPage)

      // TODO validation assertion for 'Check your answers' in the 'Stand-in declaration' variation
    }
  }

  private def navigateToCertificateStartPage(): Unit = {
    AuthorityWizardPage.withAffinityGroup(Organisation).redirectToHomePage()
    assertOnPage(AccountHomePage)
    AccountHomePage.clickSubmitCertificateLink()
    assertOnPage(SubmissionTypePage)
    SubmissionTypePage.clickCertificateRadioButton()
    SubmissionTypePage.clickSubmissionButton()
    assertOnPage(CertificateTaskListPage)
  }

}
