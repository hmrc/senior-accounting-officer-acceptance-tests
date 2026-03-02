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
import uk.gov.hmrc.test.ui.pages.CommonPage
import uk.gov.hmrc.test.ui.support.SubmissionButtonSupport

enum GrsHost(val isStubGrsChecked: Boolean) {
  case GrsStubOnRegistrationFrontEnd extends GrsHost(isStubGrsChecked = true)
  case GrsMicroservice extends GrsHost(isStubGrsChecked = false)
}

object FeatureTogglePage extends CommonPage with SubmissionButtonSupport {
  override val pageUrl: String   = s"${RegistrationPage.pageUrl}/test-only/feature-toggle"
  override val pageTitle: String = ""

  private val stubGrsCheckbox: By = By.id("stubGrs")

  def setGrsHost(grsHost: GrsHost): Unit = {
    loadPage()
    setStubGrs(isChecked = grsHost.isStubGrsChecked)
    FeatureTogglePage.clickSubmissionButton()
  }

  def setStubGrs(isChecked: Boolean): Unit = {
    val checkboxElement = driver.findElement(stubGrsCheckbox)
    if (checkboxElement.isSelected != isChecked) {
      checkboxElement.click()
    }
  }
}
