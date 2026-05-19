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

package uk.gov.hmrc.test.ui.support

import org.openqa.selenium.By
import uk.gov.hmrc.test.ui.pages.BasePage
import uk.gov.hmrc.test.ui.support.PageSupport.sendKeys

import java.time.LocalDate

trait DayMonthYearInputSupport {

  this: BasePage =>
  def dayInput: By   = By.id("value.day")
  def monthInput: By = By.id("value.month")
  def yearInput: By  = By.id("value.year")

  def addDate(date: LocalDate): Unit = {
    sendKeys(dayInput, date.getDayOfMonth.toString)
    sendKeys(monthInput, date.getMonthValue.toString)
    sendKeys(yearInput, date.getYear.toString)
  }

}
