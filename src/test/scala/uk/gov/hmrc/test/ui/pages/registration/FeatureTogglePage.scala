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

package uk.gov.hmrc.test.ui.pages.registration

import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import uk.gov.hmrc.test.ui.pages.BasePage
import uk.gov.hmrc.test.ui.support.PageSupport.{clickSubmitButton, fluentWait}

enum GrsHost(val isStubGrsChecked: Boolean) {
  case GrsStubOnRegistrationFrontEnd extends GrsHost(isStubGrsChecked = true)
  case GrsMicroservice extends GrsHost(isStubGrsChecked = false)
}

object FeatureTogglePage extends BasePage {
  override val pageUrl: String                = baseRegUrl
  val pageTitle: String                       = ""
  private val grsFeatureTogglePageUrl: String = s"$pageUrl/test-only/feature-toggle"

  private val stubGrsCheckbox: By = By.id("stubGrs")

  def setGrsHost(grsHost: GrsHost): Unit = {
    goToPage()
    setStubGrs(isChecked = grsHost.isStubGrsChecked)
    clickSubmitButton()
  }

  def goToPage(): Unit = {
    driver.navigate().to(grsFeatureTogglePageUrl)
    fluentWait.until(ExpectedConditions.urlToBe(grsFeatureTogglePageUrl))
  }

  def setStubGrs(isChecked: Boolean): Unit = {
    val checkboxElement = driver.findElement(stubGrsCheckbox)
    if (checkboxElement.isSelected != isChecked) {
      checkboxElement.click()
    }
  }
}
