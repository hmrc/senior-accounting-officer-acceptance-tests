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

import uk.gov.hmrc.test.ui.adt.RegistrationPageSection.CompanyDetails
import uk.gov.hmrc.test.ui.adt.RegistrationPageSectionStatus.Completed
import uk.gov.hmrc.test.ui.pages.*
import uk.gov.hmrc.test.ui.specs.tags.*
import uk.gov.hmrc.test.ui.support.AffinityGroup.Organisation

class GrsCompanyDetailsSpec extends BaseSpec {

  Feature("GRS integration") {
    Scenario(
      "Successfully integration with GRS microservice",
      RegistrationTests,
      ZapTests
    ) {
      val authLoginPage    = new AuthLoginPage
      val registrationPage = new RegistrationPage

      Given("An authenticated organisation user successfully navigated to the Register Your Company page")
      authLoginPage.enableGrsMicroserviceAndServiceHomePage(Organisation)
      And("They click on 'Enter your company details' heading link")
      registrationPage.clickEnterYourCompanyDetailsLink()

      And("On GRS they view the 'Company Details' page")
      GrsCompanyDetailsPage.verifyGrsCompanyDetailsPageURL()
      And("On GRS they Enter Company registration number")
      GrsCompanyDetailsPage.enterCompanyRegistrationNumber()
      And("On GRS they select 'Is this your business'")
      GrsCompanyDetailsPage.selectYesForIsThisYourBusiness()
      And("On GRS they enter 'Unique Taxpayer Reference' number")
      GrsCompanyDetailsPage.enterUTRNumber()
      And("On GRS they confirmed their answers on 'Check Your answers' page")
      GrsCompanyDetailsPage.verifyCheckYourAnswers()

      When("They are back to the 'Register Your Company' page")
      Then("The heading 'Enter your company details' is not a link")
      registrationPage.verifyEnterYourCompanyDetailsLinkIsEmpty()
      And("The status of the Company Details must be Completed")
      registrationPage.assertSectionStatus(CompanyDetails, Completed)
    }
  }
}
