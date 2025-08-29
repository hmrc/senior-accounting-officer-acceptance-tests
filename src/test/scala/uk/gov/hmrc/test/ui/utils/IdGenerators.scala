/*
 * Copyright 2024 HM Revenue & Customs
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

package uk.gov.hmrc.test.ui.utils

import uk.gov.hmrc.domain.{Generator, SaUtrGenerator}

trait IdGenerators {

  val randomisedNino: String = new Generator().nextNino.toString()
  val randomisedUtr: String  = new SaUtrGenerator().nextSaUtr.toString()

  // prefixes
  val matchingCtUtr: String    = "111"
  val alreadyRegCtUtr: String  = "222"
  val automatchedCtUtr: String = "333"
  val NonMatchingCtUtr: String = "555"
  val individualNino: String   = "AA1"
  val validSaUtr: String       = "501"

  def generateUtr(prefix: String): String =
    prefix + randomisedUtr.substring(3)

  def generateNino(prefix: String): String =
    prefix + randomisedNino.substring(3)

}
