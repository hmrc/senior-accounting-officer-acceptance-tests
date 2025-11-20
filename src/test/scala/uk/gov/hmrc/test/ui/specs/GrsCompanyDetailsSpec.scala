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
      authLoginPage.enableGrsMicroserviceAndServiceHomePage(Organisation)
      registrationPage.clickEnterYourCompanyDetailsLink()
      assertOnPage(GrsCompanyDetailsPage)
      // feature enabling page, 
      // set feature toggles, 
      // auth stub, 
      Given("Authenticated user lands on company details page.")
      // - summary page, 
      // - navigation from continue button -> CRN input
      // - CRN continue button -> UTR input
      // - verify displayed company is correct -> continue / otherPath-yetToBeWritten
      When("Valid company details are added")
      // - click complete
      Then("The heading 'Enter your company details' is not a link")
      // check navigation to the completion page.
      
      // And("On GRS they Enter Company registration number")
      // GrsCompanyDetailsPage.enterCompanyRegistrationNumber()
      // And("On GRS they select 'Is this your business'")
      // GrsCompanyDetailsPage.selectYesForIsThisYourBusiness()
      // And("On GRS they enter 'Unique Taxpayer Reference' number")
      // GrsCompanyDetailsPage.enterUTRNumber()
      // And("On GRS they confirmed their answers on 'Check Your answers' page")
      // GrsCompanyDetailsPage.verifyCheckYourAnswers()

      // When("They are back to the 'Register Your Company' page")
      registrationPage.assertEnterYourCompanyDetailsLinkNotFound()
      And("The status of the Company Details must be Completed")
      registrationPage.assertSectionStatus(CompanyDetails, Completed)
    }
  }
}
