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
import uk.gov.hmrc.test.ui.support.PageSupport.clickElement
import uk.gov.hmrc.test.ui.support.SubmissionButtonSupport

import java.nio.file.Paths

object UploadSubmissionTemplatePage extends CommonPage with SubmissionButtonSupport {

  override val pageUrl: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/notification/upload"

  override val pageTitle: String =
    "Upload a submission template for your notification - Senior Accounting Officer notification and certificate - GOV.UK"

  private val hiddenFileInputLocator = By.cssSelector(".govuk-file-upload")

  override def clickSubmissionButton(): Unit = {
    clickElement(submissionButtonLocator)
    fluentWait.until(
      ExpectedConditions.textToBePresentInElementLocated(
        By.cssSelector("h1"),
        "Review the companies in your notification"
      )
    )
  }

  def chooseFile(resourceName: String): Unit = {
    val fileUrl      = getClass.getClassLoader.getResource(resourceName)
    val absolutePath = Paths.get(fileUrl.toURI).toString
    driver.findElement(hiddenFileInputLocator).sendKeys(absolutePath)
  }
}
