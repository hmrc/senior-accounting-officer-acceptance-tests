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
import uk.gov.hmrc.test.ui.adt.*
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.registration.RegistrationPage
import uk.gov.hmrc.test.ui.support.PageSupport.{assertUrl, selectDropdownById, sendKeys}
import uk.gov.hmrc.test.ui.support.SubmissionButtonSupport

object AuthorityWizardPage extends CommonPage with SubmissionButtonSupport {
  override val pageUrl: String   = TestConfiguration.url("auth-login-stub")
  override val pageTitle: String = ""

  private val redirectionUrlById: By           = By.id("redirectionUrl")
  private val affinityGroupById: By            = By.id("affinityGroupSelect")
  private val enrolment0EnrollmentNameById: By = By.id("enrolment[0].name")
  private val enrolment0IdNameById: By         = By.id("input-0-0-name")
  private val enrolment0IdValueById: By        = By.id("input-0-0-value")
  override def submissionButtonLocator: By     = By.id("submit-top")

  private val redirectHomePageUrl: String = TestConfiguration.url("senior-accounting-officer-hub-frontend")

  private def selectAffinityGroup(affinityGroup: AffinityGroup): Unit =
    selectDropdownById(affinityGroupById).selectByVisibleText(affinityGroup.toString)

  def withAffinityGroup(affinityGroup: AffinityGroup): AuthorityWizardConfig = AuthorityWizardConfig(affinityGroup)

  def redirectToRegistration(config: AuthorityWizardConfig): Unit =
    redirectTo(config, RegistrationPage.pageUrl)

  def redirectToHomePage(config: AuthorityWizardConfig): Unit =
    redirectTo(config, redirectHomePageUrl)

  private def redirectTo(config: AuthorityWizardConfig, url: String): Unit = {
    loadPage()
    sendKeys(redirectionUrlById, url)
    selectAffinityGroup(config.affinityGroup)
    config.enrolment.foreach { enrolment =>
      sendKeys(enrolment0EnrollmentNameById, enrolment.enrolmentName)
      sendKeys(enrolment0IdNameById, enrolment.idName)
      sendKeys(enrolment0IdValueById, enrolment.idValue)
    }
    clickSubmissionButton()
    assertUrl(url)
  }

  def withDsaoEnrollment(config: AuthorityWizardConfig)(subscriptionId: String): AuthorityWizardConfig = {
    config.copy(enrolment = Some(Enrolment(enrolmentName = "HMRC-DSAO-ORG", idName = "", idValue = subscriptionId)))
  }

}

final case class AuthorityWizardConfig private[pages] (
    affinityGroup: AffinityGroup,
    enrolment: Option[Enrolment] = None
) {
  def redirectToRegistration(): Unit = AuthorityWizardPage.redirectToRegistration(this)
  def redirectToHomePage(): Unit     = AuthorityWizardPage.redirectToHomePage(this)
  def withDsaoEnrollment(subscriptionId: String): AuthorityWizardConfig =
    AuthorityWizardPage.withDsaoEnrollment(this)(subscriptionId)
}
