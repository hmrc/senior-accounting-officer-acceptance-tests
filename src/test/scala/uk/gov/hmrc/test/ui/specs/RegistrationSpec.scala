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

import uk.gov.hmrc.test.ui.adt.RegistrationPageLink.EnterYourCompanyDetailsLink
import uk.gov.hmrc.test.ui.adt.RegistrationPageSection.{CompanyDetails, ContactDetails}
import uk.gov.hmrc.test.ui.adt.RegistrationPageSectionStatus.{CannotStartYet, NotStarted}
import uk.gov.hmrc.test.ui.pages.*
import uk.gov.hmrc.test.ui.specs.tags.*
import uk.gov.hmrc.test.ui.support.AffinityGroup.Organisation
import uk.gov.hmrc.test.ui.support.PageSupport.assertOnPage

class RegistrationSpec extends BaseSpec {

  Feature("Business registration") {

    Scenario(
      "The registration page displays with the correct page elements and values when starting a new registration",
      RegistrationTests,
      ZapTests
    ) {
      val authLoginPage    = new AuthLoginPage
      val registrationPage = new RegistrationPage

      Given("An authenticated user lands on the registration page to register a business")
      authLoginPage.selectRedirectedUrlAndAffinityGroup(Organisation)
      assertOnPage(registrationPage)

      Then("The page displays with the expected page elements and values required at the start of a new registration")
      // registrationPage.assertLinkIsVisible(registrationPage.enterYourCompanyDetailsLink)
      // registrationPage.assertSubmitButtonIsNotVisible()
//      registrationPage.verifyCompanyDetailsField()
//      registrationPage.assertEnterYourCompanyDetailsLinkIsVisibleWithText()
      registrationPage.assertLinkIsVisibleWithText(EnterYourCompanyDetailsLink)
//      assertLinkIsVisibleWithText(
//        registrationPage,
//        registrationPage.companyDetailsField,
//        registrationPage.enterYourCompanyDetailsLinkAddress
//      )
      registrationPage.assertSectionStatus(CompanyDetails, NotStarted)
      registrationPage.assertContactDetailsFieldIsNotALink()
      registrationPage.assertSectionStatus(ContactDetails, CannotStartYet)
      registrationPage.verifySubmitButtonDoesNotExist()
    }

    Scenario(
      "After adding company details successfully the action states update correctly on the registration page",
      RegistrationTests,
      ZapTests
    ) {
      val authLoginPage    = new AuthLoginPage
      val grsStubPage      = new GrsStubPage
      val registrationPage = new RegistrationPage

      Given("A user successfully adds company details from the registration page")
      authLoginPage.enableGrsStubAndServiceHomePage(Organisation)
      registrationPage.clickEnterYourCompanyDetailsLink()
      grsStubPage.clickStubResponseButton()

      Then("The action states displayed on the registration page are updated correctly")
      registrationPage.verifyEnterYourContactDetailsLink()
      registrationPage.verifyContactDetailsStatusNotStarted()
    }
  }
}
