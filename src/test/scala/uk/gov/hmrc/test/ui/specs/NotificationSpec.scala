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

  override def beforeEach(): Unit = {
    super.beforeEach()
    AuthorityWizardPage.withAffinityGroup(Organisation).redirectToHub()
  }

  Feature("Submit Notification") {

    Scenario(
      "A user can submit a notification successfully when additional information is added and not changed",
      RegistrationTests, // TODO: should this be notification tests?
      ZapTests
    ) {
      Given("an authenticated user initiates adding a notification from the hub page")
      goToAdditionalInformationPageFromHub()

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
      Given("an authenticated user arrives on the additional information page during a notification submission")
      goToAdditionalInformationPageFromHub()

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

    Scenario(
      "When continuing with no additional information an error presents and is cleared on pressing skip",
      RegistrationTests,
      ZapTests
    ) {
      Given("an authenticated user arrives on the additional information page during a notification submission")
      goToAdditionalInformationPageFromHub()

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

    Scenario(
      "When pressing skip with no additional information, no text is displayed on the 'Check Your Answers' page",
      RegistrationTests,
      ZapTests
    ) {
      Given("an authenticated user arrives on the additional information page during a notification submission")
      goToAdditionalInformationPageFromHub()

      When("pressing skip without providing additional information")
      clickElement(AdditionalInformationPage.skipButton)

      Then("no text is displayed on the 'Check Your Answers' page")
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "")
    }

    Scenario(
      "When pressing skip with additional information added, no text is displayed on the 'Check Your Answers' page",
      RegistrationTests,
      ZapTests
    ) {
      Given("an authenticated user arrives on the additional information page during a notification submission")
      goToAdditionalInformationPageFromHub()

      When("pressing skip with additional information provided")
      sendKeys(AdditionalInformationPage.additionalInformationTextBox, "Test")
      clickElement(AdditionalInformationPage.skipButton)

      Then("no text is displayed on the 'Check Your Answers' page")
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "")
    }

    Scenario(
      "When selecting to change additional information from the 'Check Your Answers' page the changes are persisted",
      RegistrationTests,
      ZapTests,
      SoloTests // TODO: remove SoloTests from the Scenario before merging
    ) {
      Given(
        "an authenticated user provides additional information and arrives on the 'Check Your Answers' page during a notification submission"
      )
      goToAdditionalInformationPageFromHub()
      sendKeys(AdditionalInformationPage.additionalInformationTextBox, "Test")
      clickElement(submitButton)
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Test")

      When("pressing the change link and updating the provided additional information")
      clickElement(CheckYourAnswersPage.additionalInformationChangeLink)
      assertOnPage(AdditionalInformationPage.changePageUrl)
      sendKeys(AdditionalInformationPage.additionalInformationTextBox, "New Test For Changed Text")

      Then("on pressing continue the updated text is displayed on the 'Check Your Answers' page")
      clickElement(submitButton)
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "New Test For Changed Text")
    }

    // Decide later
    // CYA > Change > make a change > SKIP > no text in CYA
    // CYA > Change > remove text > Continue > error
  }

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
