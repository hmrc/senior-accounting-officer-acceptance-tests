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
import uk.gov.hmrc.test.ui.pages.submission.notification.*
import uk.gov.hmrc.test.ui.pages.{AuthorityWizardPage, HubPage}
import uk.gov.hmrc.test.ui.specs.tags.{SubmissionUITests, ZapTests}
import uk.gov.hmrc.test.ui.support.AffinityGroup.Organisation
import uk.gov.hmrc.test.ui.support.PageSupport.*

class CertificateSpec extends BaseSpec {

  Feature("Submit Certificate") {

    Scenario(
      "The 'upload another submission template' link is displayed on the 'Submit Certificate Guidance' page when initiating a certificate submission from the 'Hub' page",
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

    Scenario(
      "After a notification submission a user starts a certificate submission and can continue to the 'Is this your SAO' page",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user completes the submit notification journey")
      AuthorityWizardPage.withAffinityGroup(Organisation).redirectToHub()
      addNotificationFromHub()

      And("continues to start a certificate submission")
      clickElement(submitButton)
      assertOnPage(SubmitCertificateStartPage)

      When("the user clicks 'Continue' from the 'Submit Certificate Guidance' page")
      clickElement(submitButton)

      Then("the user lands on the 'Is this your SAO' page")
      assertOnPage(IsThisTheSaoPage)
    }

    Scenario(
      "After a user has continued from the 'Submit Certificate Guidance' page, they can successfully navigate to the 'SAO Email' page when selecting the 'Yes' radio button",
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
      "During a certificate submission, after submitting a valid SAO name the user lands on the 'SAO Email' page",
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

      And("the user continues with 'No' selected")
      clickRadioElement(IsThisTheSaoPage.noRadioButton)
      clickElement(submitButton)

      When("a name is added on the 'SAO Name' page")
      assertOnPage(SaoNamePage)
      sendKeys(SaoNamePage.saoNameInput, "John Wick")
      clickElement(submitButton)

      Then("the user lands on the 'SAO Email' page")
      assertOnPage(SaoEmailPage)
    }

    // name page - no name submitted - error

    Scenario(
      "After a user has continued from the 'Submit Certificate Guidance' page, when selecting neither radio button they see an error message",
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

      When("the user selects neither radio button and clicks 'Continue'")
      clickElement(submitButton)

      Then("the user can see an error message")
      assertTextOnPage(IsThisTheSaoPage.errorTitle, "There is a problem")
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
    assertOnPage(CheckYourAnswersPage)
    clickElement(submitButton)
    assertOnPage(SubmitPage)
    clickElement(SubmitPage.confirmAndSubmitButton)
    assertOnPage(ConfirmationPage)
    ConfirmationPage.assertReferenceNumberEquals("SAONOT0123456789")
  }
}
