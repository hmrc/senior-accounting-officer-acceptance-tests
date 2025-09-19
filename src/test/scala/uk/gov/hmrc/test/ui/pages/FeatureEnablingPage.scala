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
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.AuthLoginPage.navigateTo

object FeatureEnablingPage extends BasePage {
  override val pageUrl: String = TestConfiguration.url("feature-switches")

  private val companiesHouseStubCheckbox: By   = By.id("feature-switch.companies-house-stub")
  private val businessVerificationCheckbox: By = By.id("feature-switch.business-verification-stub")
  private val submitButtonByXpath: By          = By.xpath("//button[@type='submit']")

  def selectCompaniesHouseStubCheckbox(): Unit = {
    val checkboxElement = driver.findElement(companiesHouseStubCheckbox)

    if (!checkboxElement.isSelected) {
      checkboxElement.click()
    }
  }

  def selectBusinessVerificationCheckbox(): Unit = {
    val checkboxElement = driver.findElement(businessVerificationCheckbox)

    if (!checkboxElement.isSelected) {
      checkboxElement.click()
    }
  }

  def clickSubmitButtonByXpath(): Unit =
    click(submitButtonByXpath)

  def loadFeatureEnablingPage: this.type = {
    navigateTo(pageUrl)
    onPage()
    this
  }
}
