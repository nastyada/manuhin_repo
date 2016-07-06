package ru.stqa.pft.gge.appmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import ru.stqa.pft.gge.model.GeneratorData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static org.testng.Assert.fail;

/**
 * Created by Юрий on 05.03.2016.
 */
public class HelperBase {
  protected WebDriver wd;

  public HelperBase(WebDriver wd) {
    this.wd = wd;
  }

  public void click(By locator) {
    wd.findElement(locator).click();
  }

  protected void type(By locator, String text) {
    if (text != null) {
      String existingText = wd.findElement(locator).getAttribute("value");
      if (! text.equals(existingText)) {
        click(locator);
        wd.findElement(locator).clear();
        wd.findElement(locator).sendKeys(text);
      }
    }
  }

  protected void type(WebElement webElement, String text) {
    if (text != null) {
      String existingText = webElement.getAttribute("value");
      if (! text.equals(existingText)) {
        webElement.click();
        webElement.clear();
        webElement.sendKeys(text);
      }
    }
  }

  protected void attach(By locator, File file) {
    if (file != null) {
      wd.findElement(locator).sendKeys(file.getAbsolutePath());
    }
  }

  protected boolean isAlertPresent() {
    try {
      wd.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  public Boolean isWaitedCboxOverlay(boolean isProdServer) throws InterruptedException {
    return waitElementFast(By.xpath("//div[@id=\"cboxOverlay\"]"));
  }

  private Boolean waitElementFast(By locator) throws InterruptedException {
    Boolean isWaited = false;

    for (int second = 0;; second++) {
      if (second >= 5) {
        break;
      }
      try {
        if (isElementPresent(locator)) {
          isWaited = true;
          break;
        }
      }
      catch (Exception e) {
      }
      Thread.sleep(1000);
    }

    return isWaited;
  }

  public void waitLoadPage(boolean isProdServer) throws InterruptedException {
    waitElement(By.xpath("//div[@id=\"cboxOverlay\" and contains(@style,\"display: none\")]"));
  }

  protected void waitElement(By locator) throws InterruptedException {
    for (int second = 0;; second++) {
      if (second >= 60) {
        fail("timeout");
      }
      try {
        if (isElementPresent(locator)) {
          break;
        }
      }
      catch (Exception e) {
      }
      Thread.sleep(1000);
    }
  }

  protected boolean isElementPresent(By locator) {
    try {
      wd.findElement(locator);
      return true;
    } catch (NoSuchElementException ex) {
      return false;
    }
  }

  public Boolean waitForDisplayed(WebElement next) throws InterruptedException {
    Boolean wfd = false;
    for (int ii = 1; ii < 50; ii++) {
      if (next.isDisplayed()) {
        wfd = true;
        break;
      } else {
        Thread.sleep(200);
      }
    }
    return wfd;
  }

  public void saveAsJson(List<GeneratorData> vitrinas, File file) throws IOException {
    Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation().create();
    String json = gson.toJson(vitrinas);
    try (Writer writer = new FileWriter(file)) {
      writer.write(json);
    }
  }
}
