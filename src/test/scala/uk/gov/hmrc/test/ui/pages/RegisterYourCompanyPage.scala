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
import uk.gov.hmrc.test.ui.pages.AuthLoginPage.getText

object RegisterYourCompanyPage extends BasePage {
  override val pageUrl: String = baseRegUrl

  private val companyDetails                                   = By.xpath("//a[normalize-space()='Company details']")
  private val contactDetails                                   = By.xpath("//a[normalize-space()='Contact details']")
  private val checkYourAnswersBeforeSubmittingYourRegistration =
    By.xpath("//a[contains(text(),'Check your answers before submitting your registra')]")

  private val serviceProblemMessage = By.xpath("//h1[normalize-space()='Sorry, there is a problem with the service']")

  def clickCompanyDetails(): Unit = {
    onPage()
    click(companyDetails)
  }

  def clickContactDetails(): Unit = {
    onPage()
    click(contactDetails)
  }

  def clickCheckYourAnswersBeforeSubmittingYourRegistration(): Unit = {
    onPage()
    click(checkYourAnswersBeforeSubmittingYourRegistration)
  }

  def displayedServiceProblemMessage(): Unit = {
    val displayServiceProblemMessage = getText(serviceProblemMessage)
    displayServiceProblemMessage should include("Sorry, there is a problem with the service")
  }
}
