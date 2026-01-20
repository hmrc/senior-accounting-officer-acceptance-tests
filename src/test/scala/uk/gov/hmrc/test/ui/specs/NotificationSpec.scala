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

import uk.gov.hmrc.test.ui.pages.submission.notification.{AdditionalInformationPage, CheckYourAnswersPage, GuidancePage, SubmitNotificationStartPage}
import uk.gov.hmrc.test.ui.pages.{AuthorityWizardPage, HubPage}
import uk.gov.hmrc.test.ui.specs.tags.{RegistrationTests, SoloTests, ZapTests}
import uk.gov.hmrc.test.ui.support.AffinityGroup.Organisation
import uk.gov.hmrc.test.ui.support.PageSupport.{assertOnPage, clickElement, sendKeys, submitButton}

class NotificationSpec extends BaseSpec {

  Feature("Submit Notification") {

    Scenario(
      "A user can submit a notification successfully when additional information is added and not changed",
      RegistrationTests,
      ZapTests,
      SoloTests // TODO: remove SoloTests from the Scenario before merging
    ) {
      Given("an authenticated user initiates adding a notification from the hub page")
      AuthorityWizardPage.withAffinityGroup(Organisation).redirectToHub()
      assertOnPage(HubPage)

      // click submit a notification link on hub page
      clickElement(HubPage.submitNotificationLink)
      // assert on page
      assertOnPage(SubmitNotificationStartPage)

      // click submit a notification link on start page
      clickElement(SubmitNotificationStartPage.submitNotificationLink)
      assertOnPage(GuidancePage)

      // click continue on guidance page
      clickElement(submitButton)
      assertOnPage(AdditionalInformationPage)

      When("additional information is added")
      // type stuff
      sendKeys(AdditionalInformationPage.additionalInformationTextBox, "Test")

      // click continue on additional information page
      clickElement(submitButton)

      // assert on page
      assertOnPage(CheckYourAnswersPage)

      And("the user confirms their answers by clicking continue")
      // click continue on check your answers page

      And("submits the notification")
      // click confirm and submit on submit notification page
      Then("a notification reference number is successfully returned")
      // assert reference number on page
    }

    // basic scenarios:

    // skip additional information flow
    // change answer flow
  }

}
