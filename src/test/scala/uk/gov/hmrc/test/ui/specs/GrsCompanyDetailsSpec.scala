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

class GrsCompanyDetailsSpec extends BaseSpec {

  Feature("Company Details page") {
    Scenario(
      "Successfully integration with GRS microservice",
      RegistrationTests,
      ZapTests,
      SoloTests
    ) {
      Given("An authenticated organisation user successfully navigated to the Register Your Company page")
      AuthLoginPage.enableGrsMicroserviceAndServiceHomePage(Organisation)
      And("They click on 'Enter your company details' link")
      RegisterYourCompanyPage.clickEnterYourCompanyDetailsLink()

      And("On GRS they view the 'Company Details' page")
      GrsCompanyDetailsPage.verifyGrsCompanyDetailsPageURL()
      And("On GRS they Enter Company registration number")
      GrsCompanyDetailsPage.enterCompanyRegistrationNumber()
      And("On GRS they select 'Is this your business'")
      GrsCompanyDetailsPage.selectYesForIsThisYourBusiness()
      And("On GRS they enter 'Unique Taxpayer Reference' number")
      GrsCompanyDetailsPage.enterUTRNumber()
      And("On GRS they verify your answers at 'Check Your answers' page")
      GrsCompanyDetailsPage.verifyCheckYourAnswers()

      When("They are back to the 'Register Your Company' page")
      Then("The title 'Enter your company details' is not a link")
      RegisterYourCompanyPage.verifyEnterYourCompanyDetailsLinkIsEmpty()
      And("The status of the Company Details must be Completed")
      RegisterYourCompanyPage.verifyCompanyDetailsStatusCompleted()
      And("The Submit button does not exist")
      RegisterYourCompanyPage.verifySubmitButtonDoestNotExist()
    }
  }
}
