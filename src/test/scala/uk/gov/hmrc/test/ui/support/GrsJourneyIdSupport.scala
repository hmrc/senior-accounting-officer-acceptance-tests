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

import uk.gov.hmrc.test.ui.pages.BasePage

trait GrsJourneyIdSupport {
  this: BasePage =>

  private val journeyIdRegex = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}".r

  protected def journeyId: String =
    journeyIdRegex.findFirstIn(driver.getCurrentUrl) match {
      case Some(id) => id
      case None     => throw new RuntimeException(s"Could not extract journey ID from URL: ${driver.getCurrentUrl}")
    }
}
