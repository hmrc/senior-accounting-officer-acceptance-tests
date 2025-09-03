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

object RegisterYourCompanyPage extends BasePage {
  override val pageUrl: String = baseRegUrl

  private val companyDetails                 = By.xpath("//a[normalize-space()='Company details']")
  private val contactDetails                 = By.xpath("//a[normalize-space()='Contact details']")
  private val seniorAccountingOfficerDetails =
    By.xpath("//a[contains(text(),'Check your answers before submitting your registra')]")

  def clickCompanyDetails(): Unit = {
    onPage()
    click(companyDetails)
  }

  def clickContactDetails(): Unit = {
    onPage()
    click(contactDetails)
  }

  def clickSAODetails(): Unit = {
    onPage()
    click(seniorAccountingOfficerDetails)
  }
}
