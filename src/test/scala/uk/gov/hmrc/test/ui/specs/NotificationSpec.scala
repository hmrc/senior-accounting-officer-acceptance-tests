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
import uk.gov.hmrc.test.ui.specs.tags.{SoloTests, SubmissionUITests, ZapTests}
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
      assertOnPage(CheckYourAnswersPage)
      assertTextOnPage(CheckYourAnswersPage.additionalInformationValueElement, "Test")

      And("the user confirms their answers by clicking 'Continue'")
      CheckYourAnswersPage.clickSubmissionButton()
      assertOnPage(SubmitPage)

      And("submits the notification")
      SubmitPage.clickSubmissionButton()
      assertOnPage(ConfirmationPage)

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

      Then("no text is displayed on the 'Check Your Answers' page")
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

      Then("no text is displayed on the 'Check Your Answers' page")
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

    // TODO: This will be the happy journey, updated with each new page
    Scenario(
      "A user has selected to submit a Notification only, when there was only one SAO in the financial year they can move successfully through the journey providing the SAO details",
      SubmissionUITests,
      ZapTests,
      SoloTests
    ) {
      Given("an authenticated user is on the more than one SAO page, selects No and presses continue")
      goToMoreThanOneSaoPageFromHub()
      MoreThanOneSaoPage.clickNoRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()

      When("they arrive on the SAO name page and enter the SAO name")
      println(s"URL: ${NotificationSaoNamePage.pageUrl}")
      println(s"Title: ${NotificationSaoNamePage.pageTitle}")
      assertOnPage(NotificationSaoNamePage)
      NotificationSaoNamePage.addName("Jane Doe")

//TODO - update when the next page in the journey is made
      Then("continue is pressed, no error appears and the user moves to the next page in the journey")
      NotificationSaoNamePage.clickSubmissionButton()
    }

    // TODO: This will be the error journey, updated with each new page
    Scenario(
      "A user has selected to submit a Notification only, when there was only one SAO in the financial year they receive the expected error messages but can correct the mistake and move successfully through the journey providing the SAO details",
      SubmissionUITests,
      ZapTests,
      SoloTests
    ) {
      Given("an authenticated user is on the more than one SAO page, selects neither radio button and presses continue")
      goToMoreThanOneSaoPageFromHub()
      MoreThanOneSaoPage.clickSubmissionButton()

      When("the error message appears, the user can select No and continue, moving to the Sao Name Page")
      MoreThanOneSaoPage.assertErrorShownOnPage()
      MoreThanOneSaoPage.clickNoRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()
      assertOnPage(NotificationSaoNamePage)

      And(" then user does not enter a name but continue is clicked an error message appears")
      NotificationSaoNamePage.clickSubmissionButton()
      NotificationSaoNamePage.assertErrorShownOnPage()

      // TODO - update when the next page in the journey is made
      Then(
        "the user can input a name and press continue, no error appears and the user moves to the next page in the journey"
      )
      NotificationSaoNamePage.addName("Jane Doe")
      NotificationSaoNamePage.clickSubmissionButton()
    }
  }

  private def goToAdditionalInformationPageFromHub(): Unit = {
    assertOnPage(AccountHomePage)
    AccountHomePage.clickSubmitNotificationLink()
    assertOnPage(SubmitNotificationStartPage)
    SubmitNotificationStartPage.clickSubmitNotificationLink()
    assertOnPage(GuidancePage)
    GuidancePage.clickSubmissionButton()
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
