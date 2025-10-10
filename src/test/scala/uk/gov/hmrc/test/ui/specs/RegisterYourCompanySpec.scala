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
import uk.gov.hmrc.test.ui.utils.AffinityGroup.Organisation

class RegisterYourCompanySpec extends BaseSpec {

  Feature("Register Your company page") {
    Scenario("Successfully navigated to the Register your company page", RegistrationTests, ZapTests) {
      AuthLoginPage.selectRedirectedUrlAndAffinityGroup(Organisation)

      RegisterYourCompanyPage.verifyRegisterYourCompanyPageURL()

      RegisterYourCompanyPage.verifyRegisterYourCompanyPageTitle()

      RegisterYourCompanyPage.verifyCompanyDetailsField()
    }

    Scenario(
      "Successfully view Company and Contact Details in the Register your company page",
      RegistrationTests,
      ZapTests
    ) {
      AuthLoginPage.selectRedirectedUrlAndAffinityGroup(Organisation)

      RegisterYourCompanyPage.verifyRegisterYourCompanyPageURL()

      RegisterYourCompanyPage.verifyEnterYourCompanyDetailsLink()

      RegisterYourCompanyPage.verifyCompanyDetailsStatusNotStarted()

      RegisterYourCompanyPage.verifyContactDetailsField()

      RegisterYourCompanyPage.verifyContactDetailsStatusCannotStartYet()

      RegisterYourCompanyPage.verifySubmitButtonDoestNotExist()
    }

    Scenario("Can navigate to the Contact Details page after GRS", RegistrationTests, ZapTests) {
      AuthLoginPage.enableGrsStubAndServiceHomePage(Organisation)

      RegisterYourCompanyPage.clickEnterYourCompanyDetailsLink()

      GrsStubPage.clickStubResponseButton()

      RegisterYourCompanyPage.verifyEnterYourContactDetailsLink()

      RegisterYourCompanyPage.verifyContactDetailsStatusNotStarted()
    }
  }
}
