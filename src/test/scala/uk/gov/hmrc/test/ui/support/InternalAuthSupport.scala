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

import play.api.libs.json.JsObject
import play.api.libs.json.Json
import uk.gov.hmrc.test.ui.conf.TestConfiguration

import java.net.URI
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.{HttpClient, HttpRequest, HttpResponse}

object InternalAuthSupport {

  val httpClient: HttpClient = HttpClient.newHttpClient()

  private val internalAuthUrl: String =
    s"${TestConfiguration.url("internal-auth")}/test-only/token"

  val internalAuthTokenRequest: JsObject = Json.obj(
    "token"       -> "1234",
    "principal"   -> "senior-accounting-officer-submission-frontend",
    "permissions" -> Json.arr(
      Json.obj(
        "resourceType"     -> "object-store",
        "resourceLocation" -> "senior-accounting-officer",
        "actions"          -> Json.arr("READ", "WRITE")
      )
    )
  )

  private def internalAuthRequest: HttpRequest = {
    HttpRequest
      .newBuilder()
      .uri(URI.create(internalAuthUrl))
      .header("Content-Type", "application/json")
      .POST(BodyPublishers.ofString(internalAuthTokenRequest.toString))
      .build()
  }

  def setupInternalAuth: Unit = {
    httpClient.send(internalAuthRequest, HttpResponse.BodyHandlers.discarding())
  }
}
