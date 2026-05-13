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
  val thirdPersonName: String   = s"${faker.name().fullName()}-Test"
  val firstPersonEmail: String  = emailForUser(firstPersonName)
  val secondPersonEmail: String = emailForUser(secondPersonName)
  val thirdPersonEmail: String  = emailForUser(thirdPersonName)

  private def generatePerson(): Person = {
    val name  = s"${faker.name().fullName()}-Test"
    val email = emailForUser(name)
    Person(name, email)
  }

  def generateNewEmail(): String = {
    emailForUser(firstPersonName)
  }

  def emailForUser(name: String): String = {
    s"${name.toLowerCase.replace(" ", ".")}@example.com"
  }

  def create(numberOfPersons: Persons)(test: ScenarioData => Unit): Unit = {
    val data = numberOfPersons match {
      case Persons.One =>
        ScenarioData(firstPerson = generatePerson())
      case Persons.Two =>
        ScenarioData(
          firstPerson = generatePerson(),
          secondPerson = generatePerson()
        )
      case Persons.Three =>
        ScenarioData(
          firstPerson = generatePerson(),
          secondPerson = generatePerson(),
          thirdPerson = generatePerson()
        )
    }
    test(data)
  }
}

enum Persons {
  case One
  case Two
  case Three
}

case class Person(name: String, email: String)

case class ScenarioData(
    firstPerson: Person = Person("", ""),
    secondPerson: Person = Person("", ""),
    thirdPerson: Person = Person("", "")
)
