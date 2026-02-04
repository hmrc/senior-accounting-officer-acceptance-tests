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

import uk.gov.hmrc.test.ui.pages.submission.certificate.*
import uk.gov.hmrc.test.ui.pages.submission.certificate.CheckYourAnswersPage as CertificateCheckYourAnswersPage
import uk.gov.hmrc.test.ui.pages.submission.notification.*
import uk.gov.hmrc.test.ui.pages.submission.notification.CheckYourAnswersPage as NotificationCheckYourAnswersPage
import uk.gov.hmrc.test.ui.pages.{AuthorityWizardPage, HubPage}
import uk.gov.hmrc.test.ui.specs.tags.{SubmissionUITests, ZapTests}
import uk.gov.hmrc.test.ui.support.AffinityGroup.Organisation
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
      AuthorityWizardPage.withAffinityGroup(Organisation).redirectToHub()
      assertOnPage(HubPage)
      startCertificateJourney()
      clickElement(HubPage.submitCertificateLink)
      assertOnPage(SubmitCertificateStartPage)

      When("the user clicks 'Continue' on the 'Submit Certificate Guidance' page")
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
      assertOnPage(CertificateCheckYourAnswersPage) // Page title needs to be updated as part of SAOD-561

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
      AuthorityWizardPage.withAffinityGroup(Organisation).redirectToHub()
      assertOnPage(HubPage)
      startCertificateJourney()
      clickElement(HubPage.submitCertificateLink)
      assertOnPage(SubmitCertificateStartPage)

      Then("the 'upload another submission template' link is displayed on the 'Submit Certificate Guidance' page")
      assertElementIsClickable(SubmitCertificateStartPage.uploadSubmissionTemplateLink)
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
      ZapTests
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
      assertOnPage(CertificateCheckYourAnswersPage) // Page title needs to be updated as part of SAOD-561

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

  private def startCertificateJourney(): Unit = {
    // TODO: (MA - 26/01) Temporary workaround until data is available at this point in the journey.
    clickElement(HubPage.submitNotificationLink)
    assertOnPage(SubmitNotificationStartPage)
    driver.navigate().back()
    assertOnPage(HubPage)
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
