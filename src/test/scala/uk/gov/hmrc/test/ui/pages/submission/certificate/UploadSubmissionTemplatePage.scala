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

package uk.gov.hmrc.test.ui.pages.submission.certificate

import org.openqa.selenium.support.ui.{ExpectedConditions, FluentWait}
import org.openqa.selenium.{By, WebDriver}
import uk.gov.hmrc.test.ui.adt.UploadFile
import uk.gov.hmrc.test.ui.adt.UploadFile.*
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.CommonPage
import uk.gov.hmrc.test.ui.pages.submission.SubmissionTemplateGuidancePage
import uk.gov.hmrc.test.ui.support.PageSupport.extractRelativeUrl
import uk.gov.hmrc.test.ui.support.SubmissionButtonSupport

import scala.concurrent.duration.*

import java.nio.file.Paths

object UploadSubmissionTemplatePage extends CommonPage with SubmissionButtonSupport {

  override val pageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/certificate/upload"

  override val pageTitle: String =
    "Upload a submission template for your certificate - Senior Accounting Officer notification and certificate - GOV.UK"

  private val hiddenFileInputLocator: By = By.className("govuk-file-upload")
  val pageHeadingElement: By             = By.tagName("h1")
  val pageHeadingText: String            = "Upload a submission template"
  val guidanceLinkLocator: By            = By.id("template-guidance")

  def upload(file: UploadFile): Unit = {
    chooseFile(file.filename)
    clickSubmissionButton()
    waitForTextInHeading(getExpectedLandingPageHeading(file))
  }

  def assertTemplateGuidanceLinkFoundWithCorrectAttributes(): Unit = {
    val expectedGuidanceLinkHrefValue = extractRelativeUrl(SubmissionTemplateGuidancePage.pageUrl)
    val guidanceLink                  = driver.findElement(guidanceLinkLocator)
    guidanceLink.getAttribute("target") mustBe "_blank"
    extractRelativeUrl(guidanceLink.getAttribute("href")) mustBe expectedGuidanceLinkHrefValue
  }

  private def chooseFile(resourceName: String): Unit = {
    val fileUrl = Option(getClass.getClassLoader.getResource(resourceName)).getOrElse {
      throw new IllegalArgumentException(
        s"Resource '$resourceName' was not found! Ensure the file exists in 'src/test/resources'."
      )
    }
    val absolutePath = Paths.get(fileUrl.toURI).toString
    driver.findElement(hiddenFileInputLocator).sendKeys(absolutePath)
  }

  private def getExpectedLandingPageHeading(file: UploadFile): String = file match {
    case InvalidTypeFile  => pageHeadingText
    case InfectedFile     => pageHeadingText
    case UnknownErrorFile => pageHeadingText
    case RejectedFile     => pageHeadingText
    case _                => UploadReviewQualifiedPage.pageHeadingText
  }

  private def waitForTextInHeading(text: String): Unit = {
    fluentWaitWithLongDelay.until(
      ExpectedConditions.textToBePresentInElementLocated(pageHeadingElement, text)
    )
  }

  private def fluentWaitWithLongDelay: FluentWait[WebDriver] = {
    fluentWait(timeout = 7.seconds, polling = 250.milliseconds)
  }
}
