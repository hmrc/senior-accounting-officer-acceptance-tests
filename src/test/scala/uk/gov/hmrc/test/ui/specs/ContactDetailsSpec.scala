/*
 * Copyright 2025 HM Revenue & Customs
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

class ContactDetailsSpec extends BaseSpec {

  Feature("Contact Details page") {
    Scenario("Completion of First Contact Details", RegistrationTests, ZapTests) {
      AuthLoginPage.enableGrsStubAndServiceHomePage(Organisation)

      RegisterYourCompanyPage.clickEnterYourCompanyDetailsLink()
      GrsStubPage.clickStubResponseButton()

      RegisterYourCompanyPage.clickEnterYourContactDetailsLink()
      ContactDetailsPage.clickContinueButtonElement()

      ContactDetailsPage.verifyContactDetailsFieldTitle()

      ContactDetailsPage.enterFullName()
      ContactDetailsPage.clickContinueButtonElement()

      ContactDetailsPage.enterRole()
      ContactDetailsPage.clickContinueButtonElement()

      ContactDetailsPage.verifyContactDetailsFieldTitle()

      ContactDetailsPage.enterEmailAddress()
      ContactDetailsPage.clickContinueButtonElement()

      ContactDetailsPage.enterPhoneNumber()
      ContactDetailsPage.clickContinueButtonElement()

      ContactDetailsPage.selectYes()
      ContactDetailsPage.clickContinueButtonElement()

      ContactDetailsPage.verifyCheckYourAnswersTitle()

      ContactDetailsPage.verifyFirstContactDetailsInCheckYourAnswersPage()

      ContactDetailsPage.clickContinueButtonElement()

      RegisterYourCompanyPage.verifyContactDetailsStatusCompleted()
    }

    Scenario("Completion of Second Contact Details", RegistrationTests, ZapTests) {
      AuthLoginPage.enableGrsStubAndServiceHomePage(Organisation)

      RegisterYourCompanyPage.clickEnterYourCompanyDetailsLink()
      GrsStubPage.clickStubResponseButton()

      RegisterYourCompanyPage.clickEnterYourContactDetailsLink()
      ContactDetailsPage.clickContinueButtonElement()

      ContactDetailsPage.verifyContactDetailsFieldTitle()

      ContactDetailsPage.enterFullName()
      ContactDetailsPage.clickContinueButtonElement()

      ContactDetailsPage.enterRole()
      ContactDetailsPage.clickContinueButtonElement()

      ContactDetailsPage.verifyContactDetailsFieldTitle()

      ContactDetailsPage.enterEmailAddress()
      ContactDetailsPage.clickContinueButtonElement()

      ContactDetailsPage.enterPhoneNumber()
      ContactDetailsPage.clickContinueButtonElement()

      ContactDetailsPage.selectNo()
      ContactDetailsPage.clickContinueButtonElement()

      ContactDetailsPage.verifyContactDetailsFieldTitle()

      ContactDetailsPage.enterFullName()
      ContactDetailsPage.clickContinueButtonElement()

      ContactDetailsPage.enterRole()
      ContactDetailsPage.clickContinueButtonElement()

      ContactDetailsPage.verifyContactDetailsFieldTitle()

      ContactDetailsPage.enterEmailAddress()
      ContactDetailsPage.clickContinueButtonElement()

      ContactDetailsPage.enterPhoneNumber()
      ContactDetailsPage.clickContinueButtonElement()

      ContactDetailsPage.selectYes()
      ContactDetailsPage.clickContinueButtonElement()

      ContactDetailsPage.verifyCheckYourAnswersTitle()

      ContactDetailsPage.verifySecondContactDetailsInCheckYourAnswersPage()

      ContactDetailsPage.clickContinueButtonElement()

      RegisterYourCompanyPage.verifyContactDetailsStatusCompleted()
    }
  }
}
