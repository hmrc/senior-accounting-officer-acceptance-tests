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

package uk.gov.hmrc.test.ui.support

import org.openqa.selenium.{By, WebDriver}
import org.openqa.selenium.support.ui.{ExpectedConditions, FluentWait, Select, Wait}
import uk.gov.hmrc.test.ui.pages.{AuthLoginPage, BasePage, ContactDetailsPage, GrsStubPage, RegistrationPage}
//import uk.gov.hmrc.test.ui.specs.BaseSpec
import uk.gov.hmrc.test.ui.support.AffinityGroup.Organisation

import java.time.Duration

object PageSupport extends BasePage {
  def pageUrl: String   = ""
  def pageTitle: String = ""

  def fluentWait: Wait[WebDriver] = new FluentWait[WebDriver](driver)
    .withTimeout(Duration.ofSeconds(5))
    .pollingEvery(Duration.ofMillis(200))

  def selectDropdownById(id: By): Select = new Select(driver.findElement(id: By))

  def clickOnBackLink(): Unit =
    click(backLink)

  def clickSubmitButton(): Unit = {
    fluentWait.until(ExpectedConditions.elementToBeClickable(submitButton))
    click(submitButton)
  }

  def clickContinueButton(): Unit = {
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(continueButton))
    click(continueButton)
  }

  def assertOnPage(url: String = this.pageUrl): Unit = fluentWait.until(ExpectedConditions.urlToBe(url))

  def assertOnPage(page: BasePage): Unit = {
    fluentWait.until(_ => getCurrentUrl == page.pageUrl && getTitle == page.pageTitle)
    getCurrentUrl mustBe page.pageUrl
    getTitle mustBe page.pageTitle

    println(s"CURRENT URL: $getCurrentUrl \nCURRENT TITLE: $getTitle")
    println(s"URL: ${page.pageUrl} \nTITLE: ${page.pageTitle}")

  }

  def authenticateAndCompleteBusinessMatching(): RegistrationPage = {
    val authLoginPage      = new AuthLoginPage
    val grsStubPage        = new GrsStubPage
    val contactDetailsPage = new ContactDetailsPage
    val registrationPage   = new RegistrationPage

//    Given("An authenticated organisation user successfully navigates to the registration page")
    authLoginPage.enableGrsStubAndServiceHomePage(Organisation)

//    When("The 'Enter your company details' link is clicked and business matching is completed")
    registrationPage.clickEnterYourCompanyDetailsLink()
    grsStubPage.clickStubResponseButton()

//    And("The 'Enter your contact details' link is clicked followed by clicking 'Continue' on the contact details page")
    registrationPage.clickEnterYourContactDetailsLink()
    contactDetailsPage.clickContinueButtonElement()

    registrationPage
  }

}
