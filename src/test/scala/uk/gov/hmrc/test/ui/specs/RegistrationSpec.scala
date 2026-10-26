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

import uk.gov.hmrc.test.ui.adt.AffinityGroup.{Agent, Individual, Organisation}
import uk.gov.hmrc.test.ui.adt.CredentialRole.Assistant
import uk.gov.hmrc.test.ui.adt.PageSectionStatus.{CannotStartYet, NotStarted}
import uk.gov.hmrc.test.ui.adt.RegistrationPageLink.EnterYourNominatedCompanyDetailsLink
import uk.gov.hmrc.test.ui.adt.RegistrationPageSection.{CompanyDetails, ContactDetails}
import uk.gov.hmrc.test.ui.pages.AuthorityWizardPage
import uk.gov.hmrc.test.ui.pages.grs.NominatedCompanyDetailsGuidancePage
import uk.gov.hmrc.test.ui.pages.registration.*
import uk.gov.hmrc.test.ui.pages.registration.GrsHost.GrsStubOnRegistrationFrontEnd
import uk.gov.hmrc.test.ui.specs.tags.*
import uk.gov.hmrc.test.ui.support.PageSupport.assertOnPage
import uk.gov.hmrc.test.ui.support.TestData

class RegistrationSpec extends BaseSpec {

  Feature("Business registration") {
    Scenario(
      "The registration page displays with the correct page elements and values when starting a new registration",
      RegistrationUITests,
      ZapTests
    ) {
      Given("an authenticated user lands on the registration page to register a business")
      AuthorityWizardPage.withAffinityGroup(Organisation).redirectToRegistration()

      Then("the page loads correctly to start the new registration")
      assertOnPage(RegistrationPage)

      And("the enter nominated company details link is visible")
      RegistrationPage.assertLinkIsVisibleWithText(EnterYourNominatedCompanyDetailsLink)

      And("the registration status for company details is 'Not Started'")
      RegistrationPage.assertRegistrationPageSectionStatus(CompanyDetails, NotStarted)

      And("the 'enter contact details' link isn't active")
      RegistrationPage.assertEnterYourContactDetailsLinkNotFound()

      And("the 'enter contact details' status is marked as 'Cannot Start Yet'")
      RegistrationPage.assertRegistrationPageSectionStatus(ContactDetails, CannotStartYet)

      And("the submission button is not visible")
      RegistrationPage.assertSubmitButtonDoesNotExist()
    }

    Scenario(
      "After adding company details successfully the company details task status is marked as complete",
      RegistrationUITests,
      ZapTests
    ) {
      Given("a user starts the lands on the registration landing page")
      FeatureTogglePage.setGrsHost(GrsStubOnRegistrationFrontEnd)
      AuthorityWizardPage.withAffinityGroup(Organisation).redirectToRegistration()

      When("the user clicks 'Enter nominated company details' link")
      RegistrationPage.clickEnterYourNominatedCompanyDetailsLink()

      Then("they land on the company details guidance page")
      assertOnPage(NominatedCompanyDetailsGuidancePage)

      When("the user clicks the continue button")
      NominatedCompanyDetailsGuidancePage.clickSubmissionButton()
      GrsStubPage.clickStubResponseButton()

      Then("the user is redirected back to the registration landing page")
      assertOnPage(RegistrationPage)

      And("the company details task status is marked as complete with the link no longer active")
      RegistrationPage.assertEnterYourContactDetailsLinkFound()
      RegistrationPage.assertRegistrationPageSectionStatus(ContactDetails, NotStarted)
    }

    Scenario(
      "Already enrolled user is prevented from registering",
      RegistrationUITests,
      ZapTests
    ) {
      Given("an enrolled user lands on the 'Your company has already registered' page")
      AuthorityWizardPage
        .withAffinityGroup(Organisation)
        .withDsaoEnrolment(TestData.subscriptionId)
        .redirectToCompanyAlreadyRegistered()

      And("the page links the user to the business tax account page")
      CompanyAlreadyRegisteredPage.assertLinkHasTextOnPage(
        CompanyAlreadyRegisteredPage.businessTaxAccountLink,
        "business tax account (opens in new tab)"
      )
    }

    Scenario(
      "An Agent attempts to sign-in and is redirected to 'You cannot access this service' page",
      RegistrationUITests,
      ZapTests
    ) {
      Given("An Agent attempts to sign into the registration journey")
      Then("they are redirected to the 'You cannot access this service' page")
      AuthorityWizardPage
        .withAffinityGroup(Agent)
        .redirectToAgentCannotAccessThisService()
      assertOnPage(AgentCannotAccessThisServicePage)

      And("the 'who can use this service' link is present")
      AgentCannotAccessThisServicePage.assertLinkHasTextOnPage(
        AgentCannotAccessThisServicePage.whoCanUseThisServiceLink,
        "Find out more about who can use this service"
      )
    }

    Scenario(
      "A user categorised with an affinity group of 'Individual' attempts to sign-in and is redirected to 'You cannot access this service' page",
      RegistrationUITests,
      ZapTests
    ) {
      Given("An Individual attempts to sign into the registration journey")
      Then("they are redirected to the 'You cannot access this service' page")
      AuthorityWizardPage
        .withAffinityGroup(Individual)
        .redirectToIndividualCannotAccessThisService()
      assertOnPage(IndividualCannotAccessThisServicePage)

      Then("the 'sign in to Government Gateway with an organisation account' link is present")
      IndividualCannotAccessThisServicePage.assertLinkHasTextOnPage(
        IndividualCannotAccessThisServicePage.whoCanUseThisServiceLink,
        "Find out more about who can use this service"
      )

      And("the 'Find out more about who can use this service' link is present")
      IndividualCannotAccessThisServicePage.assertLinkHasTextOnPage(
        IndividualCannotAccessThisServicePage.governmentGatewayOrganisationSignIn,
        "sign in to Government Gateway with an organisation account"
      )
    }

    Scenario(
      "User with credential role of 'assistant' and affinity group of 'Organisation' and is redirect to 'You cannot access this service' page",
      RegistrationUITests,
      ZapTests
    ) {
      Given("An assistant attempts to sign into the registration journey")
      Then("they are redirected to the 'You cannot access this service' page")
      AuthorityWizardPage
        .withAffinityGroup(Organisation)
        .withCredentialRole(Assistant)
        .redirectToStandardUserCannotAccessThisService()
      assertOnPage(StandardUserCannotAccessThisServicePage)

      And("the 'Find out more about who can use this service' link is present")
      StandardUserCannotAccessThisServicePage.assertLinkHasTextOnPage(
        StandardUserCannotAccessThisServicePage.governmentGatewayOrganisationSignIn,
        "sign in using an organisation Government Gateway ID"
      )
    }
  }
}
