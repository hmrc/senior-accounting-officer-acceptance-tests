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

import uk.gov.hmrc.test.ui.conf.TestConfiguration

import java.net.URI
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.{HttpClient, HttpRequest, HttpResponse}

object InternalAuthorisationSupport {

  val httpClient: HttpClient = HttpClient.newHttpClient()

  private val internalAuthorisationProxyPath: String =
    s"${TestConfiguration.url("senior-accounting-officer-submission-frontend")}/test-only/internal-auth/object-store"

  private def internalAuthorisationRequest: HttpRequest = {
    HttpRequest
      .newBuilder()
      .uri(URI.create(internalAuthorisationProxyPath))
      .POST(BodyPublishers.noBody())
      .build()
  }

  def setupInternalAuthorisation: Unit = {
    httpClient.send(internalAuthorisationRequest, HttpResponse.BodyHandlers.discarding())
  }
}
