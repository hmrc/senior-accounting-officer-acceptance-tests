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
    AuthorityWizardPage.withAffinityGroup(Organisation).redirectToHub()
  }

  Feature("Submit Certificate") {
    // The  below scenario would be extended as in when the pages/features are ready
    Scenario(
      "A user can submit a certificate successfully from the 'Account Homepage' and submit as themselves",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user initiates a certificate submission from the 'Account Homepage' page")
      navigateToCertificateStartPage()

      When("the 'Provide the SAO's details' link is clicked")
      CertificateTaskListPage.clickTask1()

      Then("the user lands on the 'SAO full name' page")
      assertOnPage(CertificateSaoFullNamePage)

      When("the user enters a valid SAO name and clicks 'Continue'")
      CertificateSaoFullNamePage.addName(TestData.firstPersonName)
      CertificateSaoFullNamePage.clickSubmissionButton()

      Then("the user lands on the 'SAO email' page")
      assertOnPage(CertificateSaoEmailPage)

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

      When("the user enters additional information and clicks 'Continue'")
      AdditionalInformationPage.addInformation("No additional information for this certificate")
      AdditionalInformationPage.clickSubmissionButton()

      Then("the user lands on the 'Who is submitting' page")
      assertOnPage(CertificateWhoIsSubmittingPage)

      When("the user selects the SAO submitter option and clicks 'Continue'")
      CertificateWhoIsSubmittingPage.clickSaoSubmitterRadioButton()
      CertificateWhoIsSubmittingPage.clickSubmissionButton()

      Then("the user lands on the 'SAO declaration' page")
      assertOnPage(CertificateDeclarationSaoPage)

      When("the user completes the SAO declaration and clicks 'Continue'")
      CertificateDeclarationSaoPage.addDeclaration(TestData.firstPersonName)
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
      "A user can submit a certificate successfully from the 'Account Homepage' and submit on behalf of their SAO",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user initiates a certificate submission from the 'Account Homepage' page")
      navigateToCertificateStartPage()

      When("the 'Provide the SAO's details' link is clicked")
      CertificateTaskListPage.clickTask1()

      Then("the user lands on the 'SAO full name' page")
      assertOnPage(CertificateSaoFullNamePage)

      When("the user enters a valid SAO name and clicks 'Continue'")
      CertificateSaoFullNamePage.addName(TestData.firstPersonName)
      CertificateSaoFullNamePage.clickSubmissionButton()

      Then("the user lands on the 'SAO email' page")
      assertOnPage(CertificateSaoEmailPage)

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

      When("the user enters additional information and clicks 'Continue'")
      AdditionalInformationPage.addInformation("No additional information for this certificate")
      AdditionalInformationPage.clickSubmissionButton()

      Then("the user lands on the 'Who is submitting' page")
      assertOnPage(CertificateWhoIsSubmittingPage)

      When("the user selects the SAO submitter option and clicks 'Continue'")
      CertificateWhoIsSubmittingPage.clickStandInSubmitterRadioButton()
      CertificateWhoIsSubmittingPage.clickSubmissionButton()

      Then("the user lands on the 'SAO stand-in declaration' page")
      assertOnPage(CertificateDeclarationStandInPage)

      When("the user completes the SAO stand-in declaration and clicks 'Continue'")
      CertificateDeclarationStandInPage.addDeclaration(TestData.firstPersonName, TestData.secondPersonName)
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
      "Validate that choosing a Submission type is required for a certification submission",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'What would you like to submit?' page")
      assertOnPage(AccountHomePage)
      AccountHomePage.clickSubmitCertificateLink()
      assertOnPage(SubmissionTypePage)

      When("neither radio button is selected and the 'Continue' button is clicked")
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

      // TODO validation assertion for 'SAO full name'

      When("the user enters a valid SAO name and clicks 'Continue'")
      CertificateSaoFullNamePage.addName(TestData.firstPersonName)
      CertificateSaoFullNamePage.clickSubmissionButton()

      Then("the user lands on the 'SAO email' page")
      assertOnPage(CertificateSaoEmailPage)

      // TODO validation assertion for 'SAO email'

      When("the user enters a invalid SAO email and clicks 'Continue'")
      CertificateSaoEmailPage.assertHeadingMatches(s"What is the email address for ${TestData.firstPersonName}?")
      CertificateSaoEmailPage.addEmail("abc123")
      CertificateSaoEmailPage.clickSubmissionButton()
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

      When("the user enters additional information and clicks 'Continue'")
      AdditionalInformationPage.addInformation("No additional information for this certificate")
      AdditionalInformationPage.clickSubmissionButton()

      Then("the user lands on the 'Who is submitting' page")
      assertOnPage(CertificateWhoIsSubmittingPage)

      When("the 'Continue' button is clicked after neither radio button is selected")
      CertificateWhoIsSubmittingPage.clickSubmissionButton()

      Then("an error message is displayed")
      CertificateWhoIsSubmittingPage.assertErrorShownOnPage()

      When("the user selects the SAO submitter option and clicks 'Continue'")
      CertificateWhoIsSubmittingPage.clickSaoSubmitterRadioButton()
      CertificateWhoIsSubmittingPage.clickSubmissionButton()

      Then("the user lands on the 'SAO declaration' page")
      assertOnPage(CertificateDeclarationSaoPage)

      // TODO validation assertion for 'SAO declaration'

      When("the user completes the SAO declaration and clicks 'Continue'")
      CertificateDeclarationSaoPage.addDeclaration(TestData.firstPersonName)
      CertificateDeclarationSaoPage.clickSubmissionButton()

      Then("the user lands on the 'Check your answers' page")
      assertOnPage(CheckYourAnswersPage)

      // TODO validation assertion for 'Check your answers' in the 'SAO declaration' variation

      When("the user goes back to the 'Who is submitting' page")
      CertificateWhoIsSubmittingPage.loadPage()

      When("the user selects the SAO submitter option and clicks 'Continue'")
      CertificateWhoIsSubmittingPage.clickStandInSubmitterRadioButton()
      CertificateWhoIsSubmittingPage.clickSubmissionButton()

      Then("the user lands on the 'SAO stand-in declaration' page")
      assertOnPage(CertificateDeclarationStandInPage)
      // TODO validation assertion for 'Stand-in declaration'

      When("the user completes the SAO stand-in declaration and clicks 'Continue'")
      CertificateDeclarationStandInPage.addDeclaration(TestData.firstPersonName, TestData.secondPersonName)
      CertificateDeclarationStandInPage.clickSubmissionButton()

      Then("the user lands on the 'Check your answers' page")
      assertOnPage(CheckYourAnswersPage)

      // TODO validation assertion for 'Check your answers' in the 'Stand-in declaration' variation
    }
  }

  private def navigateToCertificateStartPage(): Unit = {
    AuthorityWizardPage.withAffinityGroup(Organisation).redirectToHub()
    assertOnPage(AccountHomePage)
    AccountHomePage.clickSubmitCertificateLink()
    assertOnPage(SubmissionTypePage)
    SubmissionTypePage.clickCertificateRadioButton()
    SubmissionTypePage.clickSubmissionButton()
    assertOnPage(CertificateTaskListPage)
  }
}
