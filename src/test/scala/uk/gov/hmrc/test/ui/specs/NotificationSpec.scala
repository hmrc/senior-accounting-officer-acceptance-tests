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

import java.time.LocalDate

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
      "Complete a notification only, providing details for a single SAO in the financial year",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'More than one SAO' page")
      goToMoreThanOneSaoPageFromHub()

      When("the 'Continue' button is clicked after the 'No' radio button is selected")
      MoreThanOneSaoPage.clickNoRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()

      Then("the user lands on the 'What is the name of the SAO' page")
      assertOnPage(SingleSaoNamePage)

      When("the 'Continue' button is clicked after an SAO name is entered")
      SingleSaoNamePage.addName("Jane Doe")
      SingleSaoNamePage.clickSubmissionButton()

      // TODO: Update the below step when the page is fully developed. This will land on the 'Account Homepage'.
      Then("the user lands on the 'Submit a notification' start page")
      assertOnPage(SubmitNotificationStartPage)
    }

    Scenario(
      "Complete a notification providing multiple SAO's for the financial year",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'More than one SAO' page")
      goToMoreThanOneSaoPageFromHub()

      When("the 'Continue' button is clicked after selecting 'Yes'")
      MoreThanOneSaoPage.clickYesRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()

      Then("the user lands on the 'What is the name of the last SAO' page")
      assertOnPage(MultiSaoNamePage)

      When("the 'Continue' button is clicked after entering a name of 'Jack Sparrow'")
      MultiSaoNamePage.addName("Jack Sparrow")
      MultiSaoNamePage.clickSubmissionButton()

      Then("the user lands on the 'What date did Jack Sparrow become the SAO' page")
      assertOnPage(MultiSaoFirstStartDatePage)

      And("the page displays the correct content")
      MultiSaoFirstStartDatePage.assertHeadingMatches("What date did Jack Sparrow become the SAO?")

      When("the 'Continue' button is clicked after adding a date 30 days in the past")
      MultiSaoFirstStartDatePage.addDate(LocalDate.now().minusDays(30))
      MultiSaoFirstStartDatePage.clickSubmissionButton()

      Then("the user lands on the 'Who was the SAO before Jack Sparrow' page")
      assertOnPage(WhoWasTheSaoBeforePage)

      And("the page displays the correct content")
      WhoWasTheSaoBeforePage.assertHeadingMatches("Who was the SAO before Jack Sparrow?")
      WhoWasTheSaoBeforePage.assertHintMatches("This is the person who held the role before Jack Sparrow")

      When("the 'Continue' button is clicked after entering a name of 'Gert Bo'")
      WhoWasTheSaoBeforePage.addName("Gert Bo")
      WhoWasTheSaoBeforePage.clickSubmissionButton()

      Then("the user lands on the 'When did Gert Bo’s responsibility as the SAO start' page")
      assertOnPage(MultiSaoSecondStartDatePage)

      And("the page displays the correct content")
      MultiSaoSecondStartDatePage.assertHeadingMatches("When did Gert Bo’s responsibility as the SAO start?")

      When("the 'Continue' button is clicked after adding a date 90 days in the past")
      MultiSaoSecondStartDatePage.addDate(LocalDate.now().minusDays(90))
      MultiSaoSecondStartDatePage.clickSubmissionButton()

      Then("the user lands on the 'NotificationMoreSaoSecondEndDate' page")
      assertOnPage(MultiSaoSecondEndDatePage)
    }

    Scenario(
      "Validate that SAO details are required for a notification submission given a single SAO in the financial year",
      SubmissionUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the 'More than one SAO' page")
      goToMoreThanOneSaoPageFromHub()

      When("the 'Continue' button is clicked after selecting no radio options")
      MoreThanOneSaoPage.clickSubmissionButton()

      Then("an error message is displayed")
      MoreThanOneSaoPage.assertErrorShownOnPage()

      When("the 'Continue' button is clicked after the 'No' radio button is selected")
      MoreThanOneSaoPage.clickNoRadioButton()
      MoreThanOneSaoPage.clickSubmissionButton()

      Then("the user lands on the 'What is the name of the SAO' page")
      assertOnPage(SingleSaoNamePage)

      When("the 'Continue' button is clicked after no name was entered")
      SingleSaoNamePage.clickSubmissionButton()

      Then("an error message is displayed")
      SingleSaoNamePage.assertErrorShownOnPage()

      When("the 'Continue' button is clicked after a name is entered")
      SingleSaoNamePage.addName("Jane Doe")
      SingleSaoNamePage.clickSubmissionButton()

      // TODO: Update the below step when the page is fully developed. This will land on the 'Account Homepage'.
      Then("the user lands on the 'Submit a notification' start page")
      assertOnPage(SubmitNotificationStartPage)
    }
  }

  Scenario(
    "Validate that SAO details are required for a notification submission given there are multiple SAO's in the financial year",
    SubmissionUITests,
    ZapTests
  ) {
    Given("an authenticated user lands on the 'More than one SAO' page")
    goToMoreThanOneSaoPageFromHub()

    When("the 'Continue' button is clicked after selecting 'Yes'")
    MoreThanOneSaoPage.clickYesRadioButton()
    MoreThanOneSaoPage.clickSubmissionButton()

    Then("the user lands on the 'What is name of the last SAO' question page")
    assertOnPage(MultiSaoNamePage)

    When("the 'Continue' button is clicked after no name is entered")
    MultiSaoNamePage.clickSubmissionButton()

    Then("an error message is displayed")
    MultiSaoNamePage.assertErrorShownOnPage()

    When("the 'Continue' button is clicked after entering a name of 'Jerry Hatrix'")
    MultiSaoNamePage.addName("Jerry Hatrix")
    MultiSaoNamePage.clickSubmissionButton()

    Then("the user lands on the 'What date did Jerry Hatrix become the SAO' page")
    assertOnPage(MultiSaoFirstStartDatePage)

    When("the 'Continue' button is clicked after no date is entered")
    MultiSaoFirstStartDatePage.clickSubmissionButton()

    Then("an error message is displayed")
    MultiSaoFirstStartDatePage.assertErrorShownOnPage()

    When("the 'Continue' button is clicked after adding a date 30 days in the past")
    MultiSaoFirstStartDatePage.addDate(LocalDate.now().minusDays(30))
    MultiSaoFirstStartDatePage.clickSubmissionButton()

    Then("the user lands on the 'Who was the SAO before Jerry Hatrix' question page")
    assertOnPage(WhoWasTheSaoBeforePage)

    When("the 'Continue' button is clicked after no name is entered")
    WhoWasTheSaoBeforePage.clickSubmissionButton()

    Then("an error message is displayed")
    WhoWasTheSaoBeforePage.assertErrorShownOnPage()

    When("the 'Continue' button is clicked after entering a name for a preceding SAO of 'Jock B'")
    WhoWasTheSaoBeforePage.addName("Jock B")
    WhoWasTheSaoBeforePage.clickSubmissionButton()

    Then("the user lands on the 'When did Jock B’s responsibility as the SAO start' page")
    assertOnPage(MultiSaoSecondStartDatePage)

    When("the 'Continue' button is clicked after no date is entered")
    MultiSaoSecondStartDatePage.clickSubmissionButton()

    Then("an error message is displayed")
    MultiSaoSecondStartDatePage.assertErrorShownOnPage()

    When("the 'Continue' button is clicked after adding a date 45 days in the past")
    MultiSaoSecondStartDatePage.addDate(LocalDate.now().minusDays(45))
    MultiSaoSecondStartDatePage.clickSubmissionButton()

    // TODO: Update the below step when the page is fully developed
    Then("the user lands on the 'NotificationMoreSaoSecondEndDate' page")
    assertOnPage(MultiSaoSecondEndDatePage)
  }

  Scenario(
    "Validate that 'dynamic' SAO details are retained on selecting the 'back link' during a notification submission",
    SubmissionUITests,
    ZapTests
  ) {
    Given("an authenticated user has added 'Shane Warne'as the last SAO")
    goToMoreThanOneSaoPageFromHub()
    MoreThanOneSaoPage.clickYesRadioButton()
    MoreThanOneSaoPage.clickSubmissionButton()
    assertOnPage(MultiSaoNamePage)
    MultiSaoNamePage.addName("Shane Warne")
    MultiSaoNamePage.clickSubmissionButton()
    assertOnPage(MultiSaoFirstStartDatePage)
    MultiSaoFirstStartDatePage.addDate(LocalDate.now().minusDays(20))
    MultiSaoFirstStartDatePage.clickSubmissionButton()
    assertOnPage(WhoWasTheSaoBeforePage)

    And("has added 'Jonty Rhodes' as a prior SAO")
    WhoWasTheSaoBeforePage.addName("Jonty Rhodes")
    WhoWasTheSaoBeforePage.clickSubmissionButton()
    assertOnPage(MultiSaoSecondStartDatePage)
    MultiSaoSecondStartDatePage.addDate(LocalDate.now().minusDays(65))
    MultiSaoSecondStartDatePage.clickSubmissionButton()

    And("is on the page asking for the date 'Jonty Rhodes' ended their responsibility")
    assertOnPage(MultiSaoSecondEndDatePage)

    When("the 'Back' link is clicked")
    MultiSaoSecondEndDatePage.clickBackLink()

    Then("the user lands on the 'When did Jonty Rhodes’s responsibility as the SAO start' page")
    assertOnPage(MultiSaoSecondStartDatePage)

    And("the page displays the correct content")
    MultiSaoSecondStartDatePage.assertHeadingMatches("When did Jonty Rhodes’s responsibility as the SAO start?")

    When("the 'Back' link is clicked")
    MultiSaoSecondStartDatePage.clickBackLink()

    Then("the user lands on the 'Who was the SAO before Shane Warne' page")
    assertOnPage(WhoWasTheSaoBeforePage)

    And("the page displays the correct content")
    WhoWasTheSaoBeforePage.assertHeadingMatches("Who was the SAO before Shane Warne?")
    WhoWasTheSaoBeforePage.assertHintMatches("This is the person who held the role before Shane Warne")
    WhoWasTheSaoBeforePage.assertNameMatches("Jonty Rhodes")

    When("the 'Back' link is clicked")
    WhoWasTheSaoBeforePage.clickBackLink()

    Then("the user lands on the 'What date did Shane Warne become the SAO' page")
    assertOnPage(MultiSaoFirstStartDatePage)

    And("the page displays the correct content")
    MultiSaoFirstStartDatePage.assertHeadingMatches("What date did Shane Warne become the SAO?")
  }

  Scenario(
    "Complete a notification only, providing all details for more than one SAO in the financial year",
    SubmissionUITests,
    ZapTests
  ) {
    Given("an authenticated user has added 'Shane Warne' as the last SAO")
    goToMoreThanOneSaoPageFromHub()
    MoreThanOneSaoPage.clickYesRadioButton()
    MoreThanOneSaoPage.clickSubmissionButton()
    assertOnPage(MultiSaoNamePage)
    MultiSaoNamePage.addName("Shane Warne")
    MultiSaoNamePage.clickSubmissionButton()
    assertOnPage(MultiSaoFirstStartDatePage)
    MultiSaoFirstStartDatePage.addDate(LocalDate.now().minusDays(20))
    MultiSaoFirstStartDatePage.clickSubmissionButton()
    assertOnPage(WhoWasTheSaoBeforePage)

    And("has added 'Jonty Rhodes' as a prior SAO")
    WhoWasTheSaoBeforePage.addName("Jonty Rhodes")
    WhoWasTheSaoBeforePage.clickSubmissionButton()
    assertOnPage(MultiSaoSecondStartDatePage)
    MultiSaoSecondStartDatePage.addDate(LocalDate.now().minusDays(65))
    MultiSaoSecondStartDatePage.clickSubmissionButton()
    assertOnPage(MultiSaoSecondEndDatePage)
    MultiSaoSecondEndDatePage.addDate(LocalDate.now().minusDays(65))
    MultiSaoSecondEndDatePage.clickSubmissionButton()

    And("is on the page asking 'if all the SAO for the financial year this notification relates to?'")
    assertOnPage(AreAllAddedPage)

    When("the 'Yes' radio button is clicked")
    AreAllAddedPage.clickYesRadioButton()
    AreAllAddedPage.clickSubmissionButton()

    Then("the user lands on the 'Submit a notification' start page")
    assertOnPage(SubmitNotificationStartPage)
  }

  Scenario(
    "Complete a notification only, but choosing 'No' to 'Have you added all the SAO for the financial year this notification relates to?' question and completing the journey",
    SubmissionUITests,
    ZapTests
  ) {
    Given("an authenticated user has added 'Shane Warne' as the last SAO")
    goToMoreThanOneSaoPageFromHub()
    MoreThanOneSaoPage.clickYesRadioButton()
    MoreThanOneSaoPage.clickSubmissionButton()
    assertOnPage(MultiSaoNamePage)
    MultiSaoNamePage.addName("Shane Warne")
    MultiSaoNamePage.clickSubmissionButton()
    assertOnPage(MultiSaoFirstStartDatePage)
    MultiSaoFirstStartDatePage.addDate(LocalDate.now().minusDays(20))
    MultiSaoFirstStartDatePage.clickSubmissionButton()
    assertOnPage(WhoWasTheSaoBeforePage)

    And("has added 'Jonty Rhodes' as a prior SAO")
    WhoWasTheSaoBeforePage.addName("Jonty Rhodes")
    WhoWasTheSaoBeforePage.clickSubmissionButton()
    assertOnPage(MultiSaoSecondStartDatePage)
    MultiSaoSecondStartDatePage.addDate(LocalDate.now().minusDays(65))
    MultiSaoSecondStartDatePage.clickSubmissionButton()
    assertOnPage(MultiSaoSecondEndDatePage)
    MultiSaoSecondEndDatePage.addDate(LocalDate.now().minusDays(65))
    MultiSaoSecondEndDatePage.clickSubmissionButton()

    And("is on the page asking 'if all the SAO for the financial year this notification relates to?'")
    assertOnPage(AreAllAddedPage)

    When("the 'No' radio button is clicked")
    AreAllAddedPage.clickNoRadioButton()
    AreAllAddedPage.clickSubmissionButton()

    Then("the user lands on the 'WhoWasTheSaoBeforePage' page")
    assertOnPage(WhoWasTheSaoBeforePage.changePageUrl)

    And("a new name is provided after changing the radio option to 'No'")
    WhoWasTheSaoBeforePage.addName("Alex Rhodes")
    WhoWasTheSaoBeforePage.clickSubmissionButton()
    assertOnPage(MultiSaoSecondStartDatePage.changePageUrl)
    MultiSaoSecondStartDatePage.addDate(LocalDate.now().minusDays(65))
    MultiSaoSecondStartDatePage.clickSubmissionButton()
    assertOnPage(MultiSaoSecondEndDatePage.changePageUrl)
    MultiSaoSecondEndDatePage.addDate(LocalDate.now().minusDays(65))
    MultiSaoSecondEndDatePage.clickSubmissionButton()

    And("is on the page asking 'if all the SAO for the financial year this notification relates to?'")
    assertOnPage(AreAllAddedPage.changePageUrl)

    When("the 'Yes' radio button is clicked to complete the notification submission")
    AreAllAddedPage.clickYesRadioButton()
    AreAllAddedPage.clickSubmissionButton()

    Then("the user lands on the 'Submit a notification' start page")
    assertOnPage(SubmitNotificationStartPage)
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

   Scenario(
    "Complete a notification only, but choosing 'No' to 'Have you added all the SAO for the financial year this notification relates to?' question and completing the journey",
    SubmissionUITests,
    ZapTests
  ) {
    Given("an authenticated user has added 'Shane Warne' as the last SAO")
    goToMoreThanOneSaoPageFromHub()
    MoreThanOneSaoPage.clickYesRadioButton()
    MoreThanOneSaoPage.clickSubmissionButton()
    assertOnPage(MultiSaoNamePage)
    MultiSaoNamePage.addName("Shane Warne")
    MultiSaoNamePage.clickSubmissionButton()
    assertOnPage(MultiSaoFirstStartDatePage)
    MultiSaoFirstStartDatePage.addDate(LocalDate.now().minusDays(20))
    MultiSaoFirstStartDatePage.clickSubmissionButton()
    assertOnPage(WhoWasTheSaoBeforePage)

    And("has added 'Jonty Rhodes' as a prior SAO")
    WhoWasTheSaoBeforePage.addName("Jonty Rhodes")
    WhoWasTheSaoBeforePage.clickSubmissionButton()
    assertOnPage(MultiSaoSecondStartDatePage)
    MultiSaoSecondStartDatePage.addDate(LocalDate.now().minusDays(65))
    MultiSaoSecondStartDatePage.clickSubmissionButton()
    assertOnPage(MultiSaoSecondEndDatePage)
    MultiSaoSecondEndDatePage.addDate(LocalDate.now().minusDays(65))
    MultiSaoSecondEndDatePage.clickSubmissionButton()

    And("is on the page asking 'if all the SAO for the financial year this notification relates to?'")
    assertOnPage(AreAllAddedPage)

    When("the 'No' radio button is clicked")
    AreAllAddedPage.clickNoRadioButton()
    AreAllAddedPage.clickSubmissionButton()

    Then("the user lands on the 'WhoWasTheSaoBeforePage' page")
    assertOnPage(WhoWasTheSaoBeforePage.changePageUrl)

    And("a new name is provided after changing the radio option to 'No'")
    WhoWasTheSaoBeforePage.addName("Alex Rhodes")
    WhoWasTheSaoBeforePage.clickSubmissionButton()
    assertOnPage(MultiSaoSecondStartDatePage.changePageUrl)
    MultiSaoSecondStartDatePage.addDate(LocalDate.now().minusDays(65))
    MultiSaoSecondStartDatePage.clickSubmissionButton()
    assertOnPage(MultiSaoSecondEndDatePage.changePageUrl)
    MultiSaoSecondEndDatePage.addDate(LocalDate.now().minusDays(65))
    MultiSaoSecondEndDatePage.clickSubmissionButton()

    And("is on the page asking 'if all the SAO for the financial year this notification relates to?'")
    assertOnPage(AreAllAddedPage.changePageUrl)

    When("the 'Yes' radio button is clicked to complete the notification submission")
    AreAllAddedPage.clickYesRadioButton()
    AreAllAddedPage.clickSubmissionButton()

    Then("the user lands on the 'Submit a notification' start page")
    assertOnPage(SubmitNotificationStartPage)
  }
}
