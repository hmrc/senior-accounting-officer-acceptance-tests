/*
 * Copyright 2023 HM Revenue & Customs
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

import uk.gov.hmrc.test.ui.pages.*
import uk.gov.hmrc.test.ui.specs.tags.*

class AuthorityWizardSpec extends BaseSpec {

  Feature("Authority Wizard page") {

    Scenario(
      "Select a valid redirect URL and affinity group",
      RegistrationTests,
      ZapTests
    ) {
      Given("The user enters the localhost URL and selects the redirect URL and affinity group")
      AuthLoginPage.loginAsNonAutomatchedOrgAdmin()
    }

    Scenario(
      "Select an invalid redirect URL with a valid affinity group",
      RegistrationTests,
      ZapTests
    ) {
      Given("The user enters the localhost URL and selects the redirect URL and affinity group")
      AuthLoginPage.loginAsInvalidNonAutomatchedOrgAdmin()
      Then("The user sees an error message")
      AuthLoginPage.errorMessageDisplayed()
    }
  }
}
