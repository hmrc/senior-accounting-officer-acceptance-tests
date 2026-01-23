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

import uk.gov.hmrc.test.ui.pages.submission.notification.*
import uk.gov.hmrc.test.ui.pages.{AuthorityWizardPage, HubPage}
import uk.gov.hmrc.test.ui.specs.tags.{SubmissionUITests, ZapTests}
import uk.gov.hmrc.test.ui.support.AffinityGroup.Organisation
import uk.gov.hmrc.test.ui.support.PageSupport.*

class CertificateSpec extends BaseSpec {

  Feature("Submit Certificate") {

// SCENARIOS
//    Given a user navigates from the 'Hub 'page to the 'Submit Certificate Guidance 'page,
//    Then the 'upload another submission template 'link is displayed.
    
//    Given a user submits a notification successfully,
//    And clicks continue on the 'Notification Confirmation 'page,
//    And lands on the 'Submit Certificate Guidance 'page,
//    When the user clicks 'Continue',
//    Then they land on the 'Is this the SAO' page.
    
    
    Scenario(
      "A user can submit a certificate successfully when additional information is added and not changed",
      SubmissionUITests,
      ZapTests
    ) {
//      Given("an authenticated user initiates adding a notification from the hub page")
//      AuthorityWizardPage.withAffinityGroup(Organisation).redirectToHub()
//      goToAdditionalInformationPageFromHub()
//
//      When("additional information is added")
//      sendKeys(AdditionalInformationPage.additionalInformationTextBox, "Test")
//      clickElement(submitButton)
//      assertOnPage(CheckYourAnswersPage)
//      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Test")
//
//      And("the user confirms their answers by clicking continue")
//      clickElement(submitButton)
//      assertOnPage(SubmitPage)
//
//      And("submits the notification")
//      clickElement(SubmitPage.confirmAndSubmitButton)
//      assertOnPage(ConfirmationPage)
//
//      Then("the given notification reference number is successfully returned")
//      ConfirmationPage.assertReferenceNumberEquals("SAONOT0123456789")
    }}



  private def goToAdditionalInformationPageFromHub(): Unit = {
    assertOnPage(HubPage)
    clickElement(HubPage.submitNotificationLink)
    assertOnPage(SubmitNotificationStartPage)
    clickElement(SubmitNotificationStartPage.submitNotificationLink)
    assertOnPage(GuidancePage)
    clickElement(submitButton)
    assertOnPage(AdditionalInformationPage)
  }
}
