/*
 * Copyright 2026 HM Revenue & Customs
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

package uk.gov.hmrc.test.ui.pages.submission.notification

import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.CommonPage
import uk.gov.hmrc.test.ui.support.PageSupport.{assertElementIsClickable, assertLinkHasText, assertTextOnPage}
import uk.gov.hmrc.test.ui.support.SubmissionButtonSupport

object ConfirmationPage extends CommonPage with SubmissionButtonSupport {
  override def pageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/notification/confirmation?notificationIdReferenceNumber=${getNotificationReferenceNumber}"

  override val pageTitle: String =
    "Submit a notification - Notification submitted - Senior Accounting Officer notification and certificate - GOV.UK"

  val downloadPdfLink: By =
    By.cssSelector("""#download-pdf-link[href="#"]""")

  val printPageLink: By =
    By.cssSelector("""#print-page-link[href="#"]""")

  def getNotificationReferenceNumber: String = {
    val notificationReference: By = testId("notification-reference-number")
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(notificationReference))
    driver.findElement(notificationReference).getText
  }

  def assertReferenceNumberEquals(number: String): Unit = {
    val referenceNumberElement: By = testId("notification-reference-number")
    fluentWait.until(ExpectedConditions.visibilityOfElementLocated(referenceNumberElement))
    driver.findElement(referenceNumberElement).getText mustBe number
  }

  def assertTextInLink(link: By, text: String): Unit = {
    assertElementIsClickable(link)
    assertTextOnPage(link, text)
  }

  def assertLinkHasTextOnPage(link: By, expectedText: String): Unit = {
    assertLinkHasText(link, expectedText)
  }

}
