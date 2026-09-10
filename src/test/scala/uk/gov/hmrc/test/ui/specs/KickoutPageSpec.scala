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
import uk.gov.hmrc.test.ui.pages.registration.RegistrationPage
import uk.gov.hmrc.test.ui.pages.{AuthorityWizardPage, NotEnrolledPage}
import uk.gov.hmrc.test.ui.specs.tags.{RegistrationUITests, ZapTests}
import uk.gov.hmrc.test.ui.support.PageSupport.assertOnPage

class KickoutPageSpec extends BaseSpec {

  Feature("Kickout Page") {
    Scenario("Attempt to access the service without an active DSAO enrolment", RegistrationUITests, ZapTests) {
      Given("a user attempts to access the homepage without an active DSAO enrolment")
      AuthorityWizardPage.withAffinityGroup(Organisation).redirectToNotEnrolledPage()

      Then("the user is redirected to the 'not-enrolled' page")
      assertOnPage(NotEnrolledPage)
      NotEnrolledPage.assertLinkHasTextOnPage(
        NotEnrolledPage.registerLink,
        "register to submit a Senior Accounting Officer notification and certificate service"
      )

      When("the 'register to submit a Senior Accounting Officer notification and certificate service' link is clicked")
      NotEnrolledPage.clickRegisterForServiceLink()

      Then("the user lands on the registration page")
      assertOnPage(RegistrationPage)
    }
  }
}
