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
import uk.gov.hmrc.test.ui.pages.grs.*
import uk.gov.hmrc.test.ui.pages.registration.RegistrationPage
import uk.gov.hmrc.test.ui.specs.tags.*
import uk.gov.hmrc.test.ui.support.AffinityGroup.Organisation

class GrsIntegrationSpec extends BaseSpec {

  Feature("Generic Registration Service (GRS) integration") {
    Scenario(
      "Integrate successfully with GRS",
      RegistrationTests,
      ZapTests
    ) {
      Given("an authenticated user accesses the Generic Registration Service")
      LimitedCompanyStubConfigurationPage.setStubbedDependencies()
      AuthorityWizardPage.enableGrsMicroserviceAndServiceHomePage(Organisation)
      RegistrationPage.clickEnterYourCompanyDetailsLink()

      When("the user completes a business match successfully")
      CompanyRegistrationNumberPage.verifyGrsCompanyDetailsPageURL()
      CompanyRegistrationNumberPage.enterCompanyRegistrationNumber()
      IsThisYourBusinessPage.selectYesForIsThisYourBusiness()
      UniqueTaxpayerReferencePage.enterUTRNumber()
      GrsCheckYourAnswersPage.verifyCheckYourAnswers()

      Then("the company details have a status of 'Completed' on the registration dashboard")
      RegistrationPage.assertSectionStatus(CompanyDetails, Completed)

      And("the 'Enter your company details' link is disabled")
      RegistrationPage.assertEnterYourCompanyDetailsLinkNotFound()
    }
  }
}

