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

package uk.gov.hmrc.test.ui.utils

import scala.util.Random

trait TestDataGenerator {

  private val rand = new Random()

  def randomString(length: Int): String =
    rand.alphanumeric.filter(_.isLetter).take(length).mkString

  def randomFullName(): String = {
    val firstName = randomString(6).capitalize
    val lastName  = randomString(8).capitalize
    s"$firstName $lastName"
  }

  def randomEmail(): String = {
    val username = randomString(10).toLowerCase
    val domain   = randomString(5).toLowerCase
    val tld      = randomString(3).toLowerCase
    s"$username@$domain.$tld"
  }

  def randomUkPhoneNumber(): String = {
    val prefix = "07" + (rand.nextInt(900) + 100)
    val suffix = f"${rand.nextInt(1000000)}%06d"
    s"$prefix $suffix"
  }

  def randomRole(): String = {
    val titles = Seq("Chief Financial Officer", "Engineer", "Manager", "Analyst", "Designer", "Chief Technical Officer")
    titles(rand.nextInt(titles.length))
  }
}
