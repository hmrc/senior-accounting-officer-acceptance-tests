package uk.gov.hmrc.test.ui.pages.submission.notification

import org.openqa.selenium.By
import uk.gov.hmrc.test.ui.adt.NotificationPageLink.*
import uk.gov.hmrc.test.ui.adt.{NotificationPageSection, PageLink, PageSection}
import uk.gov.hmrc.test.ui.conf.TestConfiguration
import uk.gov.hmrc.test.ui.pages.BasePage

class SubmitNotificationStartPage extends BasePage {
  override val pageUrl: String   = TestConfiguration.url("senior-accounting-officer-submission-notification-frontend ")
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

  private val sectionLocators: Map[PageSection, By] = Map(
    NotificationPageSection.UploadTemplate     -> By.cssSelector("#upload-template-details-status"),
    NotificationPageSection.SubmitNotification -> By.cssSelector("#submit-notification-details-status")
  )
}
