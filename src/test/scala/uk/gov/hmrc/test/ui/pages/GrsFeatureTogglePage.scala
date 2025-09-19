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
import org.openqa.selenium.support.ui.ExpectedConditions
import uk.gov.hmrc.test.ui.pages.RegisterYourCompanyPage.{baseRegUrl, click}

object GrsFeatureTogglePage extends BasePage {
  override val pageUrl: String = baseRegUrl

  private val grsFeatureTogglePageUrl: String = s"$pageUrl/test-only/feature-toggle"

  private val stubGrsCheckbox: By              = By.id("stubGrs")
  private val grsAllowsRelativeUrlCheckbox: By = By.id("grsAllowsRelativeUrl")

  def unselectStubGrsCheckbox(): Unit = {
    val radioButtonSelection = driver.findElement(stubGrsCheckbox)
    if (radioButtonSelection.isSelected) {
      radioButtonSelection.click() // Unselect
    }
  }

  def selectGrsAllowsRelativeUrlCheckbox(): Unit =
    click(grsAllowsRelativeUrlCheckbox)

  def loadFeatureTogglePage(): Unit =
    navigateTo(grsFeatureTogglePageUrl)
    fluentWait.until(ExpectedConditions.urlToBe(grsFeatureTogglePageUrl))
}
