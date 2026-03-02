package uk.gov.hmrc.test.ui.pages

import org.openqa.selenium.support.ui.ExpectedConditions
import uk.gov.hmrc.test.ui.support.PageSupport.fluentWait

trait CommonPage extends BasePage {
  def pageUrl: String

  def loadPage(): Unit = {
    driver.navigate().to(pageUrl)
    fluentWait.until(ExpectedConditions.urlToBe(pageUrl))
  }
}
