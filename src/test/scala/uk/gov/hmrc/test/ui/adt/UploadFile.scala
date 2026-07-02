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

package uk.gov.hmrc.test.ui.adt

enum UploadFile(val filename: String) {
  case EmptyFile                extends UploadFile("Submission-template-empty-v15.csv")
  case FourCompaniesFile        extends UploadFile("Submission-template-4-companies-moderately-complex.csv")
  case InvalidQualificationFile extends UploadFile("Submission-template-invalid-qualification.csv")
  case InvalidTypeFile          extends UploadFile("invalid.REASON.csv")
  case InfectedFile             extends UploadFile("infected.VIRUS_NAME.csv")
  case UnknownErrorFile         extends UploadFile("unknown.REASON.csv")
  case RejectedFile             extends UploadFile("reject.UnexpectedContent.csv")
}
