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
import uk.gov.hmrc.test.ui.adt.CompanyDetails

import scala.util.Random

object TestData {
  private val faker = new Faker(new java.util.Locale("en-GB"))

  val subscriptionId = "XMPLR0123456789"
  val name: String   = ".!#$%&’'*+/=?^_`{|}~-x!Q8r$*L9z+H=^" +
    "aaaaaaaaaABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

  val firstPersonName: String   = s"${faker.name().firstName()} ${faker.name().lastName()}-Test"
  val secondPersonName: String  = s"${faker.name().firstName()} ${faker.name().lastName()}-Test"
  val firstPersonEmail: String  = "9.!#$%&’'*+/=?^_`{|}~-x!Q8r$*L9z+H=^s@example.com"
  val secondPersonEmail: String = "valid.email+test@example.com"

  // this is a 'def' so that the value is new each time it's called
  def invalidEmail: String = {
    val options = Seq("test@example.", "test@.example.com", "test@example..com")
    options(Random.nextInt(options.length))
  }

  val minimumNameCharacterLimit: String          = "N"
  val maximumNameCharacterLimit: String          = name.take(105)
  val maximumNameCharacterLimitExceeded: String  = name.take(106)
  val maximumEmailCharacterLimitExceeded: String =
    "example-test-attftffioiuoiuoiuoiuuoiuoiuoikjjfiytdiyvkhhjgvkooooooooooooooooooooooooooockvjhvliyufovyufkuyfjh" +
      "gfkjyfovy8vyvo87g8giugouyfiytduiyrd6irds@oooooooooooooooooooooooooyvoutfoutdiytdiytitfiy5dtiydctiytdiytdi7t" +
      "do7tudo7futdyfouyfjuyfo7ufuoyfjouyf.com"
  val nameWithInvalidCharacters: String = "Test <>\""

  val Companies: Map[String, CompanyDetails] = Map(
    "DummyCompany" -> CompanyDetails(
      companyName = "Fake Company Ltd",
      referenceId = subscriptionId
    )
  )
}
