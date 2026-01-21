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
import uk.gov.hmrc.test.ui.specs.tags.{RegistrationTests, SoloTests, ZapTests}
import uk.gov.hmrc.test.ui.support.AffinityGroup.Organisation
import uk.gov.hmrc.test.ui.support.PageSupport.*

class NotificationSpec extends BaseSpec {

  Feature("Submit Notification") {

    Scenario(
      "A user can submit a notification successfully when additional information is added and not changed",
      RegistrationTests, // TODO: should this be notification tests?
      ZapTests
    ) {
      Given("an authenticated user initiates adding a notification from the hub page")
      AuthorityWizardPage.withAffinityGroup(Organisation).redirectToHub()
      assertOnPage(HubPage)
      clickElement(HubPage.submitNotificationLink)
      assertOnPage(SubmitNotificationStartPage)
      clickElement(SubmitNotificationStartPage.submitNotificationLink)
      assertOnPage(GuidancePage)
      clickElement(submitButton)
      assertOnPage(AdditionalInformationPage)

      When("additional information is added")
      sendKeys(AdditionalInformationPage.additionalInformationTextBox, "Test")
      clickElement(submitButton)
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Test")

      And("the user confirms their answers by clicking continue")
      clickElement(submitButton)
      assertOnPage(SubmitPage)

      And("submits the notification")
      clickElement(SubmitPage.confirmAndSubmitButton)
      assertOnPage(ConfirmationPage)

      Then("the given notification reference number is successfully returned")
      ConfirmationPage.assertReferenceNumberEquals("SAONOT0123456789")
    }

    Scenario(
      "When continuing with no additional information an error presents and is cleared on populating additional information and pressing continue",
      RegistrationTests,
      ZapTests
    ) {
      Given(
        "an authenticated user arrives on the additional information page during a notification submission"
      )
      AuthorityWizardPage.withAffinityGroup(Organisation).redirectToHub()
      assertOnPage(HubPage)
      clickElement(HubPage.submitNotificationLink)
      assertOnPage(SubmitNotificationStartPage)
      clickElement(SubmitNotificationStartPage.submitNotificationLink)
      assertOnPage(GuidancePage)
      clickElement(submitButton)
      assertOnPage(AdditionalInformationPage)

      When("pressing continue without providing additional information")
      clickElement(submitButton)
      assertPageWithError(AdditionalInformationPage)

      Then("an error appears on screen")
      assertTextOnPage(AdditionalInformationPage.errorTitle, "There is a problem")

      And(
        "on continuing after adding additional information the text added is displayed on the 'Check Your Answers' page"
      )
      sendKeys(AdditionalInformationPage.additionalInformationTextBox, "Test")
      clickElement(submitButton)
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Test")
    }

    // Additional information screen > do not add text > Continue > error > SKIP > no text in CYA
    Scenario(
      "When continuing with no additional information an error presents and is cleared on pressing skip",
      RegistrationTests,
      ZapTests,
      SoloTests // TODO: remove SoloTests from the Scenario before merging
    ) {
      Given(
        "an authenticated user arrives on the additional information page during a notification submission"
      )
      AuthorityWizardPage.withAffinityGroup(Organisation).redirectToHub()
      assertOnPage(HubPage)
      clickElement(HubPage.submitNotificationLink)
      assertOnPage(SubmitNotificationStartPage)
      clickElement(SubmitNotificationStartPage.submitNotificationLink)
      assertOnPage(GuidancePage)
      clickElement(submitButton)
      assertOnPage(AdditionalInformationPage)

      When("pressing continue without providing additional information")
      clickElement(submitButton)
      assertPageWithError(AdditionalInformationPage)

      Then("an error appears on screen")
      assertTextOnPage(AdditionalInformationPage.errorTitle, "There is a problem")

      And(
        "on pressing skip, no text is displayed on the 'Check Your Answers' page"
      )
      clickElement(AdditionalInformationPage.skipButton)
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "")
    }

    // basic scenarios:
    // skip additional information flow
    // Additional information screen > do not add text > Skip > no text in CYA
    // Additional information screen > add text > Skip > no text in CYA

    // change answer flow
    // CYA > Change > lands on '/change-additional-information' > make a change > Continue > change appears in CYA

    // Decide later
    // CYA > Change > make a change > SKIP > no text in CYA
    // CYA > Change > remove text > Continue > error

  }

}
