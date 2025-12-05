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
import uk.gov.hmrc.test.ui.pages.BasePage
import uk.gov.hmrc.test.ui.support.PageSupport.fluentWait

object LimitedCompanyStubConfigurationPage extends BasePage {
  override val pageUrl: String = TestConfiguration.url("incorporated-entity-identification-frontend")
  val featureEnableUrl: String = s"$pageUrl/test-only/feature-switches"
  val pageTitle: String        = ""

  private val companiesHouseStubCheckbox: By   = By.id("feature-switch.companies-house-stub")
  private val submitButton: By          = By.xpath("//button[@type='submit']")

  def setStubbedDependencies(): Unit = {
    loadFeatureEnablingPage()
    setCompaniesHouseStubCheckbox(checked = true)
    click(submitButton)
  }

  def setCompaniesHouseStubCheckbox(checked: Boolean): Unit = {
    val checkbox = driver.findElement(companiesHouseStubCheckbox)

    if (checkbox.isSelected != checked) checkbox.click()
  }
  
  def loadFeatureEnablingPage(): Unit = {
    navigateTo(featureEnableUrl)
    fluentWait.until(ExpectedConditions.urlToBe(featureEnableUrl))
  }
}
