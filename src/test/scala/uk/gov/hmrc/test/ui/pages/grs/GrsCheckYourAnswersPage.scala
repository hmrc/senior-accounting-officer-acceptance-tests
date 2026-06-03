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
import uk.gov.hmrc.test.ui.pages.{BasePage, DynamicUrlWithKnownParam, StaticTitle}
import uk.gov.hmrc.test.ui.support.PageSupport.clickContinueButton

object GrsCheckYourAnswersPage extends BasePage with DynamicUrlWithKnownParam[String] with StaticTitle {
  private val grsHost: String = TestConfiguration.url("incorporated-entity-identification-frontend")

  override def pageUrl(param: String): String = s"$grsHost/$param/check-your-answers-business"
  override def pageTitle: String              =
    "Check your answers - Senior Accounting Officer notification and certificate - GOV.UK"

  private val companyReferenceNumber: By =
    By.cssSelector("div.govuk-summary-list__row:nth-child(1) .govuk-summary-list__value")
  private val uniqueTaxpayerReference: By =
    By.cssSelector("div.govuk-summary-list__row:nth-child(2) .govuk-summary-list__value")

  def assertAnswersAndSubmit(): Unit = {
    assertCrnValue()
    assertUtrValue()
    clickContinueButton()
  }

  def assertCrnValue(): Unit = {
    val crnValue = fluentWait
      .until(ExpectedConditions.visibilityOfElementLocated(companyReferenceNumber))
    crnValue.getText mustBe "AB123456"
  }

  def assertUtrValue(): Unit = {
    val utrValue = fluentWait
      .until(ExpectedConditions.visibilityOfElementLocated(uniqueTaxpayerReference))
    utrValue.getText mustBe "1234567890"
  }
}
