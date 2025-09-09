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
import uk.gov.hmrc.test.ui.utils.AffinityGroupEnum

object AuthLoginPage extends BasePage {
  override val pageUrl: String = TestConfiguration.url("auth-login-stub")

  private val redirectionUrlById: By = By.id("redirectionUrl")
  private val affinityGroupById: By  = By.id("affinityGroupSelect")
  private val authSubmitById: By     = By.id("submit-top")
  val errorMessageHeading: By        = By.xpath("//h1[contains(text(),'This page can’t be found')]")

  private val redirectUrl: String = baseRegUrl

  private def loadPage: this.type = {
    navigateTo(pageUrl)
    onPage()
    this
  }

  private def selectAffinityGroup(affinityGroup: AffinityGroupEnum): Unit =
    selectDropdownById(affinityGroupById).selectByVisibleText(affinityGroup.value)

  private def submitAuthPage(): Unit = click(authSubmitById)

  private def selectValidRedirectURLAndAffinityGroup(affinityGroup: AffinityGroupEnum): Unit = {
    loadPage
    sendKeys(redirectionUrlById, redirectUrl)
    selectAffinityGroup(affinityGroup)
    submitAuthPage()
  }

  def selectRedirectedURLAndAffinityGroup(affinityGroup: AffinityGroupEnum): Unit =
    selectValidRedirectURLAndAffinityGroup(affinityGroup)
}
