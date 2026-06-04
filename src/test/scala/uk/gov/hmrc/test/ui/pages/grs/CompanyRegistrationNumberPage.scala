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

package uk.gov.hmrc.test.ui.pages.grs

import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.{BasePage, DynamicUrlWithUnknownParam, StaticTitle}
import uk.gov.hmrc.test.ui.support.PageSupport.clickContinueButton

import java.util.regex.Pattern

object CompanyRegistrationNumberPage extends BasePage with DynamicUrlWithUnknownParam[String] with StaticTitle {

  private val grsHost: String = TestConfiguration.url("incorporated-entity-identification-frontend")

  override def pageTitle: String =
    "Company registration number - Senior Accounting Officer notification and certificate - GOV.UK"

  val validGrsStubCompanyRegistrationNumber: String = "AB123456"

  val companyRegistrationNumberTextBox: By = By.id("companyNumber")

  def enterCrnAndSubmit(): Unit = {
    fluentWait
      .until(ExpectedConditions.visibilityOfElementLocated(companyRegistrationNumberTextBox))
      .sendKeys(validGrsStubCompanyRegistrationNumber)
    clickContinueButton()
  }
  override protected val urlRegex: String =
    s"^${Pattern.quote(grsHost)}/([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})/company-number"

  override def extractParams: String = {
    val UrlPattern       = urlRegex.r
    val UrlPattern(uuid) = driver.getCurrentUrl: @unchecked

    uuid
  }
}
