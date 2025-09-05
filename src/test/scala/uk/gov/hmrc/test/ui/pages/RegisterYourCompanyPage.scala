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

package uk.gov.hmrc.test.ui.pages

import org.openqa.selenium.By
import uk.gov.hmrc.selenium.webdriver.Driver
import uk.gov.hmrc.test.ui.pages.AuthLoginPage.getText

import java.time.Duration

object RegisterYourCompanyPage extends BasePage {
  override val pageUrl: String = baseRegUrl

  private val companyDetails                                   = By.xpath("//a[normalize-space()='Company details']")
  private val contactDetails                                   = By.xpath("//a[normalize-space()='Contact details']")
  private val checkYourAnswersBeforeSubmittingYourRegistration =
    By.xpath("//a[contains(text(),'Check your answers before submitting your registra')]")

  private val serviceProblemMessage = By.xpath("//h1[normalize-space()='Sorry, there is a problem with the service']")

  def verifyCompanyDetailsLink(): Unit = {
    Driver.instance.manage().timeouts().implicitlyWait(Duration.ofSeconds(1))
    Driver.instance.findElement(companyDetails).isEnabled
  }

  def clickCompanyDetails(): Unit = click(companyDetails)

  def verifyContactDetailsLink(): Unit = {
    Driver.instance.manage().timeouts().implicitlyWait(Duration.ofSeconds(1))
    Driver.instance.findElement(contactDetails).isEnabled
  }

  def clickContactDetails(): Unit = click(contactDetails)

  def verifyCheckYourAnswersBeforeSubmittingYourRegistrationLink(): Unit = {
    Driver.instance.manage().timeouts().implicitlyWait(Duration.ofSeconds(1))
    Driver.instance.findElement(checkYourAnswersBeforeSubmittingYourRegistration).isEnabled
  }

  def clickCheckYourAnswersBeforeSubmittingYourRegistration(): Unit =
    click(checkYourAnswersBeforeSubmittingYourRegistration)

  def displayedServiceProblemMessage(): Unit = {
    val displayServiceProblemMessage = getText(serviceProblemMessage)
    displayServiceProblemMessage should include("Sorry, there is a problem with the service")
  }

  def verifyRegisterYourCompanyPageURL(): Unit = {
    Driver.instance.manage().timeouts().implicitlyWait(Duration.ofSeconds(1))
    val registerYourCompanyPageURL = Driver.instance.getCurrentUrl
    registerYourCompanyPageURL should include(pageUrl)
  }

  def verifyRegisterYourCompanyPageTitle(): Unit = {
    val registerYourCompanyPageTitle = Driver.instance.getTitle
    registerYourCompanyPageTitle should include(
      "Register your company - Senior Accounting Officer notification and certificate - GOV.UK"
    )
  }
}
