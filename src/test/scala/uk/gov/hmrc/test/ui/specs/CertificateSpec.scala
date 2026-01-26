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

import uk.gov.hmrc.test.ui.pages.submission.certificate.SubmitCertificateStartPage
import uk.gov.hmrc.test.ui.pages.submission.notification.*
import uk.gov.hmrc.test.ui.pages.{AuthorityWizardPage, HubPage}
import uk.gov.hmrc.test.ui.specs.tags.{SoloTests, SubmissionUITests, ZapTests}
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

    //    Given a user submits a notification successfully,
    //    And clicks continue on the 'Notification Confirmation 'page,
    //    And lands on the 'Submit Certificate Guidance 'page,
    //    When the user clicks 'Continue',
    //    Then they land on the 'Is this the SAO' page.
    Scenario(
      "After a notification submission a user starts a certificate submission and can continue to the 'Is this your SAO' page",
      SubmissionUITests,
      ZapTests,
      SoloTests
    ) {
      Given("an authenticated user adds a notification from the 'Hub' page")
      AuthorityWizardPage.withAffinityGroup(Organisation).redirectToHub()
      addNotificationFromHub()

      And("continues to start a certificate submission") // ?
      // click continue from 'Confirmation' Page
      // assertOnPage(certStartPage)

      When("the user clicks 'Continue' from the 'Submit Certificate Guidance' page") // ?

      Then("the user lands on the 'Is this your SAO' page")

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
