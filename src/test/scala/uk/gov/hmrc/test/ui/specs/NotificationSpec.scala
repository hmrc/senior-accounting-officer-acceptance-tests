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

import uk.gov.hmrc.test.ui.adt.AffinityGroup.Organisation
import uk.gov.hmrc.test.ui.pages.submission.notification.*
import uk.gov.hmrc.test.ui.pages.{AccountHomePage, AuthorityWizardPage}
import uk.gov.hmrc.test.ui.specs.tags.{SubmissionUITests, ZapTests}
import uk.gov.hmrc.test.ui.support.PageSupport.*

class NotificationSpec extends BaseSpec {

  override def beforeEach(): Unit = {
    super.beforeEach()
    AuthorityWizardPage.withAffinityGroup(Organisation).redirectToHub()
  }

  Feature("Submit Notification") {

    Scenario(
      "A user can submit a notification successfully when additional information is added and not changed",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user initiates adding a notification from the 'Hub' page")
      goToAdditionalInformationPageFromHub()

      When("additional information is added")
      AdditionalInformationPage.addInformation("Test")
      AdditionalInformationPage.clickSubmissionButton()
      assertOnPage(ConfirmNotificationPage)
      ConfirmNotificationPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Test")

      And("the user confirms their answers by clicking 'Continue'")
      CheckYourAnswersPage.clickSubmissionButton()

      Then("the given notification reference number is successfully returned")
      ConfirmationPage.assertReferenceNumberEquals("SAONOT0123456789")
    }

    Scenario(
      "When continuing with no additional information an error presents and is cleared on populating additional information and pressing continue",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user arrives on the additional information page during a notification submission")
      goToAdditionalInformationPageFromHub()

      When("pressing continue without providing additional information")
      AdditionalInformationPage.clickSubmissionButton()
      assertPageWithError(AdditionalInformationPage)

      Then("an error appears on screen")
      AdditionalInformationPage.assertErrorShownOnPage()

      And(
        "on continuing after adding additional information the text added is displayed on the 'Check Your Answers' page"
      )
      AdditionalInformationPage.addInformation("Test")
      AdditionalInformationPage.clickSubmissionButton()
      assertOnPage(ConfirmNotificationPage)
      ConfirmNotificationPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Test")
    }

    Scenario(
      "When continuing with no additional information an error presents and is cleared on pressing 'Skip'",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user arrives on the additional information page during a notification submission")
      goToAdditionalInformationPageFromHub()

      When("pressing 'Continue' without providing additional information")
      AdditionalInformationPage.clickSubmissionButton()
      assertPageWithError(AdditionalInformationPage)

      Then("an error appears on screen")
      AdditionalInformationPage.assertErrorShownOnPage()

      And(
        "on pressing 'Skip', no text is displayed on the 'Check Your Answers' page"
      )
      AdditionalInformationPage.clickSkipButton()
      assertOnPage(ConfirmNotificationPage)
      ConfirmNotificationPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "")
    }

    Scenario(
      "When pressing 'Skip' with no additional information, no text is displayed on the 'Check Your Answers' page",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user arrives on the 'Additional Information' page during a notification submission")
      goToAdditionalInformationPageFromHub()

      When("pressing 'Skip' without providing additional information")
      AdditionalInformationPage.clickSkipButton()
      assertOnPage(ConfirmNotificationPage)
      Then("no text is displayed on the 'Check Your Answers' page")
      ConfirmNotificationPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "")
    }

    Scenario(
      "When pressing 'Skip' with additional information added, no text is displayed on the 'Check Your Answers' page",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user arrives on the 'Additional Information' page during a notification submission")
      goToAdditionalInformationPageFromHub()

      When("pressing skip with additional information provided")
      AdditionalInformationPage.addInformation("Test")
      AdditionalInformationPage.clickSkipButton()
      assertOnPage(ConfirmNotificationPage)
      Then("no text is displayed on the 'Check Your Answers' page")
      ConfirmNotificationPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "")
    }

    Scenario(
      "When selecting to change additional information from the 'Check Your Answers' page the changes are persisted",
      SubmissionUITests,
      ZapTests
    ) {
      Given(
        "an authenticated user provides additional information and arrives on the 'Check Your Answers' page during a notification submission"
      )
      goToAdditionalInformationPageFromHub()
      AdditionalInformationPage.addInformation("Test")
      AdditionalInformationPage.clickSubmissionButton()
      assertOnPage(ConfirmNotificationPage)
      ConfirmNotificationPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Test")

      When("pressing the change link and updating the provided additional information")
      CheckYourAnswersPage.clickAdditionalInformationChangeLink()
      assertOnPage(AdditionalInformationPage.changePageUrl)
      AdditionalInformationPage.addInformation("New Test For Changed Text")

      Then("on pressing 'Continue' the updated text is displayed on the 'Check Your Answers' page")
      AdditionalInformationPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "New Test For Changed Text")
    }

    Scenario(
      "When selecting to change additional information from the 'Check Your Answers' page and when 'Skip' is pressed then the changes are not persisted",
      SubmissionUITests,
      ZapTests
    ) {
      Given(
        "an authenticated user provides additional information and arrives on the 'Check Your Answers' page during a notification submission"
      )
      goToAdditionalInformationPageFromHub()
      AdditionalInformationPage.addInformation("Test")
      AdditionalInformationPage.clickSubmissionButton()
      assertOnPage(ConfirmNotificationPage)
      ConfirmNotificationPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Test")

      When("pressing the change link and updating the provided additional information")
      CheckYourAnswersPage.clickAdditionalInformationChangeLink()
      assertOnPage(AdditionalInformationPage.changePageUrl)
      AdditionalInformationPage.addInformation("New Test For Changed Text")

      Then("on pressing 'Skip' the updated text is not displayed on the 'Check Your Answers' page")
      AdditionalInformationPage.clickSkipButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "")
    }

    Scenario(
      "When selecting to change additional information from the 'Check Your Answers' page, if text is removed and continue is pressed an error is shown",
      SubmissionUITests,
      ZapTests
    ) {
      Given(
        "an authenticated user provides additional information and arrives on the 'Check Your Answers' page during a notification submission"
      )
      goToAdditionalInformationPageFromHub()
      AdditionalInformationPage.addInformation("Test")
      AdditionalInformationPage.clickSubmissionButton()
      assertOnPage(ConfirmNotificationPage)
      ConfirmNotificationPage.clickSubmissionButton()
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Test")

      When("pressing the change link and clearing the existing additional information")
      CheckYourAnswersPage.clickAdditionalInformationChangeLink()
      assertOnPage(AdditionalInformationPage.changePageUrl)
      AdditionalInformationPage.addInformation("")

      Then("on pressing 'Continue' an error appears on screen")
      AdditionalInformationPage.clickSubmissionButton()
      AdditionalInformationPage.assertErrorShownOnPage()
    }

    Scenario(
      "Complete a notification only, providing SAO details for the only SAO in the financial year",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'More than one SAO' page")
      goToMoreThanOneSaoPageFromHub()

      When("the 'Continue' button is clicked after the 'No' radio button is selected")
      MoreThanOneSaoPage.clickNoRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()

      Then("the user lands on the 'NotificationOneSaoNamePage' page")
      assertOnPage(NotificationFirstSaoNamePage)

      When("the 'Continue' button is clicked after an SAO name is entered")
      NotificationFirstSaoNamePage.addName("Jane Doe")
      NotificationFirstSaoNamePage.clickSubmissionButton()

      Then("the user lands on the Account Homepage")
//      assertOnPage(AccountHomePage) TODO: comment back in once navigation is implemented by devs
    }

    Scenario(
      "Validate that SAO details are required during a notification only submission",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user does not select any option after landing on the 'More than one SAO' page")
      goToMoreThanOneSaoPageFromHub()

      When("the 'Continue' button is clicked")
      MoreThanOneSaoPage.clickSubmissionButton()

      Then("an error message is displayed")
      MoreThanOneSaoPage.assertErrorShownOnPage()

      When("the 'Continue' button is clicked after the 'No' radio button is selected")
      MoreThanOneSaoPage.clickNoRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()

      Then("the user lands on the 'NotificationOneSaoNamePage' page")
      assertOnPage(NotificationFirstSaoNamePage)

      When("the 'Continue' button is clicked after no name is entered")
      NotificationFirstSaoNamePage.clickSubmissionButton()

      Then("an error message is displayed")
      NotificationFirstSaoNamePage.assertErrorShownOnPage()

      When("the 'Continue' button is clicked after a name is entered")
      NotificationFirstSaoNamePage.addName("Jane Doe")
      NotificationFirstSaoNamePage.clickSubmissionButton()

      Then("the user lands on the Account Homepage")
//      assertOnPage(AccountHomePage) TODO: comment back in once navigation is implemented by devs
    }
  }

  private def goToAdditionalInformationPageFromHub(): Unit = {
    assertOnPage(AccountHomePage)
    AccountHomePage.clickSubmitNotificationLink()
    assertOnPage(SubmitNotificationStartPage)
    SubmitNotificationStartPage.clickSubmitNotificationLink()
    assertOnPage(AdditionalInformationPage)
  }

  private def goToMoreThanOneSaoPageFromHub(): Unit = {
    assertOnPage(AccountHomePage)
    AccountHomePage.clickSubmitNotificationLink()
    assertOnPage(SubmitNotificationStartPage)
    SubmitNotificationStartPage.clickProvideSaoDetailsLink()
    assertOnPage(MoreThanOneSaoPage)
  }
}
