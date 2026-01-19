package uk.gov.hmrc.test.ui.pages.submission.notification

import uk.gov.hmrc.test.ui.pages.BasePage
import uk.gov.hmrc.test.ui.adt.PageLink
import org.openqa.selenium.By
import uk.gov.hmrc.test.ui.adt.NotificationPageLink.*

class SubmitNotificationStartPage extends BasePage {
  override val pageUrl: String   = "/senior-accounting-officer/submission/notification/start"
  override val pageTitle: String =
    "Submit a notification - Senior Accounting Officer notification and certificate - GOV.UK"

  private val pageLinks: Map[PageLink, (By, String)] = Map(
    TemplateGuidanceLink   -> (
      By.cssSelector("p.govuk-body a"),
      s"${pageUrl.stripSuffix("/")}/business-match"
    ),
    UploadTemplateLink     -> (
      By.cssSelector("ul.govuk-task-list > li.govuk-task-list__item--with-link:nth-of-type(1)"),
      s"${pageUrl.stripSuffix("/")}/business-match"
    ),
    SubmitNotificationLink -> (
      By.cssSelector("ul.govuk-task-list > li.govuk-task-list__item--with-link:nth-of-type(2)"),
      s"${pageUrl.stripSuffix("/")}/business-match"
    )
  )

  private val sectionLocators: Map[RegistrationPageSection, By] = Map(
    RegistrationPageSection.CompanyDetails -> By.cssSelector("#company-details-status"),
    RegistrationPageSection.ContactDetails -> By.cssSelector("#contacts-details-status")
  )
}
