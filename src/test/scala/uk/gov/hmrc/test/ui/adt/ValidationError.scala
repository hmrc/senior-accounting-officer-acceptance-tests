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

enum ValidationError(val errorMessage: String) {
  case InvalidFileTypeError extends ValidationError("The selected file must be a CSV")
  case InfectedFileError    extends ValidationError("The selected file contains a virus")
  case UnknownUploadError   extends ValidationError("The selected file could not be uploaded – try again")
  case InvalidEmailError extends ValidationError("Email address must be in the correct format, like name@example.com")
  case MissingEmailError extends ValidationError("Enter the email address of a person or team")
  case MaximumEmailCharacterLimitExceededError extends ValidationError("Email address must be 254 characters or less")
  case MissingNameError            extends ValidationError("Enter the name of the person or team we can contact")
  case NameTooLongError            extends ValidationError("Name of the person or team must be 105 characters or less")
  case InvalidNameCharactersError  extends ValidationError("Name of the person or team must not include <, >, or \"")
  case NoElementChosenContactError extends ValidationError("Select yes if you would like to add another contact")
}
