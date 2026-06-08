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
      SubmitCertificateStartPage.clickTask1()
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
    }

    Scenario(
      "A user can submit a certificate successfully from the 'Account Homepage' and submit on behalf of their SAO",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user initiates a certificate submission from the 'Account Homepage' page")
      navigateToCertificateStartPage()
      SubmitCertificateStartPage.clickTask2()
      assertOnPage(UploadSubmissionTemplatePage)
      UploadSubmissionTemplatePage.clickSubmissionButton()
      assertOnPage(UploadReviewQualifiedPage)
      UploadReviewQualifiedPage.clickSubmissionButton()
      assertOnPage(UploadReviewUnqualifiedPage)
      UploadReviewUnqualifiedPage.clickSubmissionButton()
      assertOnPage(SubmitCertificateStartPage)
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
      SubmissionTypePage.clickSubmissionButton()

      When("neither radio button is selected and the 'Continue' button is clicked")
      SubmissionTypePage.assertErrorShownOnPage()
    }
  }

  private def navigateToCertificateStartPage(): Unit = {
    AuthorityWizardPage.withAffinityGroup(Organisation).redirectToHub()
    assertOnPage(AccountHomePage)
    AccountHomePage.clickSubmitCertificateLink()
    assertOnPage(SubmissionTypePage)
    SubmissionTypePage.clickCertificateRadioButton()
    SubmissionTypePage.clickSubmissionButton()
    assertOnPage(SubmitCertificateStartPage)
  }
}
