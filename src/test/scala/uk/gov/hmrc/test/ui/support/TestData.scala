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

import com.github.javafaker.Faker

object TestData {
  private val faker = new Faker(new java.util.Locale("en-GB"))

  val firstPersonName: String   = s"${faker.name().fullName()}-Test"
  val secondPersonName: String  = s"${faker.name().fullName()}-Test"
  val firstPersonEmail: String  = emailForUser(firstPersonName)
  val secondPersonEmail: String = emailForUser(secondPersonName)

  def generateNewEmail(): String = {
    emailForUser(firstPersonName)
  }

  def emailForUser(name: String): String = {
    s"${name.toLowerCase.replace(" ", ".")}@example.com"
  }
}
