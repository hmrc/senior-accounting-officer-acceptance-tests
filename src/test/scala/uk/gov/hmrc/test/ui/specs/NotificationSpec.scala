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

import uk.gov.hmrc.test.ui.adt.PageSectionStatus.{CannotStartYet, NotStarted}
import uk.gov.hmrc.test.ui.adt.RegistrationPageLink.EnterYourCompanyDetailsLink
import uk.gov.hmrc.test.ui.adt.RegistrationPageSection.{CompanyDetails, ContactDetails}
import uk.gov.hmrc.test.ui.pages.AuthorityWizardPage
import uk.gov.hmrc.test.ui.pages.grs.LimitedCompanyStubConfigurationPage
import uk.gov.hmrc.test.ui.pages.registration.FeatureTogglePage
import uk.gov.hmrc.test.ui.pages.registration.GrsHost.GrsStubOnRegistrationFrontEnd
import uk.gov.hmrc.test.ui.pages.submission.notification.*
import uk.gov.hmrc.test.ui.specs.tags.{RegistrationTests, SoloTests, ZapTests}
import uk.gov.hmrc.test.ui.support.AffinityGroup.Organisation
import uk.gov.hmrc.test.ui.support.PageSupport.{assertOnPage, pageUrl}
import uk.gov.hmrc.test.ui.support.PageSupport.fluentWait
import org.openqa.selenium.support.ui.ExpectedConditions

class NotificationSpec extends BaseSpec {

  Feature("Submit Notification") {

    Scenario(
      "The submit notification start page displays with the correct page elements and values when starting a new submission",
      RegistrationTests,
      ZapTests,
      SoloTests
    ) {
      Given("an authenticated user lands on the registration page to register a business")
      LimitedCompanyStubConfigurationPage.setStubbedDependencies()
      FeatureTogglePage.setGrsHost(GrsStubOnRegistrationFrontEnd)

      AuthorityWizardPage.selectValidRedirectUrlAndAffinityGroup2(Organisation)

      SubmitNotificationStartPage.navigateTo(SubmitNotificationStartPage.pageUrl)
      fluentWait.until(ExpectedConditions.urlToBe(SubmitNotificationStartPage.pageUrl))
      assertOnPage(SubmitNotificationStartPage)

//      Then("the page displays with the expected page elements and values required at the start of a new registration")
//      RegistrationPage.assertLinkIsVisibleWithText(EnterYourCompanyDetailsLink)
//      RegistrationPage.assertSectionStatus(CompanyDetails, NotStarted)
//      RegistrationPage.assertEnterYourContactDetailsLinkNotFound()
//      RegistrationPage.assertSectionStatus(ContactDetails, CannotStartYet)
//      RegistrationPage.assertSubmitButtonDoesNotExist()
    }
  }

}
