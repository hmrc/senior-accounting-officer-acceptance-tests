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

import uk.gov.hmrc.test.ui.pages.{AuthorityWizardPage, HubPage}
import uk.gov.hmrc.test.ui.specs.tags.{RegistrationTests, SoloTests, ZapTests}
import uk.gov.hmrc.test.ui.support.AffinityGroup.Organisation
import uk.gov.hmrc.test.ui.support.PageSupport.assertOnPage

class NotificationSpec extends BaseSpec {

  Feature("Submit Notification") {

    Scenario(
      "The submit notification start page displays with the correct page elements and values when starting a new submission",
      RegistrationTests,
      ZapTests,
      SoloTests // DO NOT MERGE
    ) {
      Given("an authenticated user lands on the submit a notification page")
//      AuthorityWizardPage.selectValidRedirectUrlAndAffinityGroup2(Organisation)
      AuthorityWizardPage.withAffinityGroup(Organisation).redirectToHub()
      assertOnPage(HubPage)

//      Then("the page displays with the expected page elements and values required at the start of a new registration")
//      RegistrationPage.assertLinkIsVisibleWithText(EnterYourCompanyDetailsLink)
//      RegistrationPage.assertSectionStatus(CompanyDetails, NotStarted)
//      RegistrationPage.assertEnterYourContactDetailsLinkNotFound()
//      RegistrationPage.assertSectionStatus(ContactDetails, CannotStartYet)
//      RegistrationPage.assertSubmitButtonDoesNotExist()
    }
  }

}
