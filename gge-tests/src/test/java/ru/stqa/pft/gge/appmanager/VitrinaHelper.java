package ru.stqa.pft.gge.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.stqa.pft.gge.model.GeneratorData;

import java.util.List;
import java.util.Set;

/**
 * Created by manuhin on 21.04.2016.
 */
public class VitrinaHelper extends HelperBase {
  public VitrinaHelper(WebDriver wd) {
    super(wd);
  }

  public boolean selectVitrina(GeneratorData vitrina, boolean isProdServer) throws InterruptedException {
    for (int i = 1; i <= 5; i++) {
      selectRazdel(vitrina, isProdServer);
      selectMenuVitrin(vitrina, isProdServer);
      if (!vitrina.getMenuXpath().equals(vitrina.getVitrinaXpath())) {
        selectPodMenuVitrina(vitrina, isProdServer);
      }

      if (checkVitrinaName(vitrina)) {
        return true;
      } else {
        Thread.sleep(2000);
      }
    }
    return false;
  }

  private void selectPodMenuVitrina(GeneratorData vitrina, boolean isProdServer) throws InterruptedException {
    Thread.sleep(3000);
    recursiaVitrina(vitrina, 15, isProdServer);
  }

  public boolean checkVitrinaName(GeneratorData vitrina) {
    List<WebElement> elements = wd.findElements(By.xpath(".//div[@id=\"serviceBar\" and @class=\"serviceBar\"]//h3"));
    if (elements.size() == 1) {
      if (elements.iterator().next().getText().equals(vitrina.getVitrina())) {
        return true;
      }
    }
    return false;
  }

  private int recursiaVitrina(GeneratorData vitrina, int i, boolean isProdServer) throws InterruptedException {
    try {
      click(By.xpath(vitrina.getVitrinaXpath()));
      waitLoadPage(isProdServer);
      List<WebElement> elements = wd.findElements(By.xpath(".//div[@id=\"serviceBar\" and @class=\"serviceBar\"]//h3"));
      if (elements.size() == 1) {
        if (elements.iterator().next().getText().equals(vitrina.getVitrina())) {
          return 0;
        } else {
          i--;
          Thread.sleep(3000);
          selectRazdel(vitrina, isProdServer);
          Thread.sleep(3000);
          selectMenuVitrin(vitrina, isProdServer);
          Thread.sleep(3000);
          selectPodMenuVitrina(vitrina, isProdServer);
          return i;
        }
      }
      return i;
    } catch (Exception e) {
      Thread.sleep(3000);
      i--;
      if (i > 0) {
        selectRazdel(vitrina, isProdServer);
        selectMenuVitrin(vitrina, isProdServer);
        return recursiaVitrina(vitrina, i, isProdServer);
      }
      return i;
    }
  }

  private int recursiaMenu(GeneratorData vitrina, int i, boolean isProdServer) throws InterruptedException {
    try {
      click(By.xpath(vitrina.getMenuXpath()));
      return i;
    } catch (Exception e) {
      Thread.sleep(3000);
      i--;
      if (i > 0) {
        selectRazdel(vitrina, isProdServer);
        return recursiaMenu(vitrina, i, isProdServer);
      }
      return i;
    }
  }

  private void selectMenuVitrin(GeneratorData vitrina, boolean isProdServer) throws InterruptedException {
    waitLoadPage(isProdServer);
    Thread.sleep(3000);
    recursiaMenu(vitrina, 15, isProdServer);
  }

  public void fillAllFilters(boolean isProdServer) throws InterruptedException {
    waitLoadPage(isProdServer);
    String startXpath = ".//*[@class=\"form\"]";
    List<WebElement> elements = wd.findElements(By.xpath(startXpath));
    if (elements.size() == 1) {
      WebElement element = elements.iterator().next();
      fillFiltrCombobox(element);
      fillFiltrCheckbox(element);
      fillFiltrType(element, 0, "ав");
      fillFiltrType(element, 49, "ав");
      fillFiltrType(element, 47, "ав");
      fillFiltrType(element, 6, "01.04.2015");
      fillFiltrType(element, 9, "01.04.2015");
      fillFiltrReference(element, 16, isProdServer);
      fillFiltrReference(element, 15, isProdServer);
    }
  }

  private void fillFiltrReference(WebElement element, int attr, boolean isProdServer) throws InterruptedException {
    String xpathlocator = ".//*[@class=\"folderIco\" and @attr_type=\"" + attr + "\"]";
    List<WebElement> elements = element.findElements(By.xpath(xpathlocator));
    Set<String> winOld = wd.getWindowHandles();
    if (elements.size() > 0) {
      WebElement element2 = elements.iterator().next();
      if (elements.size() >= 1) {
        for (WebElement w : elements) {
          if (w.isDisplayed()) {

            w.click();

            Set<String> wNewSet = wd.getWindowHandles();
            int attempt = 0;
            while (wNewSet.size() < 2 && attempt < 15) {
              Thread.sleep(1000);
              wNewSet = wd.getWindowHandles();
              attempt++;
            }

            wNewSet.removeAll(winOld);
            String wNew = wNewSet.iterator().next();
            wd.switchTo().window(wNew);

            String xpathAll = "//input[@id=\"collect_all\"]";
            waitElement(By.xpath(xpathAll));
            click(By.xpath(xpathAll));

            String xpathSubmit = "//input[@id=\"submitButton\"]";
            waitElement(By.xpath(xpathSubmit));
            click(By.xpath(xpathSubmit));

            wd.switchTo().window(winOld.iterator().next());
            waitLoadPage(isProdServer);
          }
        }
      }
    }
  }

  public void buttonFind(boolean isProdServer) throws InterruptedException {
    waitElement(By.xpath("//div[@class='form']/input"));
    click(By.xpath("//div[@class='form']/input"));
    Thread.sleep(3000);
    waitLoadPage(isProdServer);
  }

  private void fillFiltrType(WebElement element, int attr, String text) throws InterruptedException {
    String xpathlocator = ".//*[@type=\"text\" and @attr_type=\"" + attr + "\"]";
    List<WebElement> elements = element.findElements(By.xpath(xpathlocator));
    if (elements.size() > 0) {
      WebElement element2 = elements.iterator().next();
      if (elements.size() >= 1) {
        for (WebElement w : elements) {
          if (w.isDisplayed()) {
            type(w, text);
          }
        }
      }
    }
  }

  private void fillFiltrCombobox(WebElement element) throws InterruptedException {
    String xpathlocator = ".//select";

    List<WebElement> elements = element.findElements(By.xpath(xpathlocator));
    if (elements.size() > 0) {
      WebElement element2 = elements.iterator().next();
      if (elements.size() >= 1) {
        for (WebElement w : elements) {
          List<WebElement> elementsValue = w.findElements(By.xpath("./option[last()]"));
          if (elementsValue.size() == 1) {
            WebElement ww = elementsValue.iterator().next();
            String id = w.getAttribute("id");
            String jstriptString = "$('select#" + id + "').find('option:last').attr('selected', 'selected').end().change()";
            ((JavascriptExecutor) wd).executeScript(jstriptString);
          }
        }
      }
    }
  }

  private void fillFiltrCheckbox(WebElement element) throws InterruptedException {
    String xpathlocator = ".//input[@type=\"checkbox\" and @class!=\"masked\"]";

    List<WebElement> elements = element.findElements(By.xpath(xpathlocator));
    if (elements.size() > 0) {
      WebElement element2 = elements.iterator().next();
      if (elements.size() >= 1) {
        for (WebElement w : elements) {
          if (w.isEnabled()) {
            String id = w.getAttribute("id");
            String jstriptString = "$('input#" + id + "').click()";
            ((JavascriptExecutor) wd).executeScript(jstriptString);
          }
        }
      }
    }
  }

  public void vizovRasshPoisk(boolean isProdServer) throws InterruptedException {
    String xpathRasshPoisk = "";
    if (isProdServer) {
      xpathRasshPoisk = ".//*[@class=\"singleButton searchBtn default-btn MAIN\"]"; // eis
    } else {
      xpathRasshPoisk = ".//*[@class=\"singleButton SERVICE searchBtn default-btn left right MAIN\"]"; // test-eis, 82-й
    }
    List<WebElement> elements = wd.findElements(By.xpath(xpathRasshPoisk));
    if (elements.size() == 1) {
      waitLoadPage(isProdServer);
      waitElement(By.xpath(xpathRasshPoisk));
      click(By.xpath(xpathRasshPoisk));
    }
  }

  public void vizovRasshPoiskMGE(boolean isProdServer) throws InterruptedException {
    String xpathRasshPoisk = "";
    if (isProdServer) {
      xpathRasshPoisk = "//*[@class=\"singleButton searchBtn default-btn left right MAIN\"]"; //
    } else {
      xpathRasshPoisk = "//*[@class=\"SERVICE_element singleButton SERVICE searchBtn default-btn left right MAIN\"]"; // 97-й
    }

    List<WebElement> elements = wd.findElements(By.xpath(xpathRasshPoisk));
    if (elements.size() == 1) {
      waitLoadPage(isProdServer);
      waitElement(By.xpath(xpathRasshPoisk));
      click(By.xpath(xpathRasshPoisk));
    }
  }

  private int recursiaRazdel(GeneratorData vitrina, int i) throws InterruptedException {
    try {
      click(By.xpath(vitrina.getRazdXpath()));
      return i;
    } catch (Exception e) {
      Thread.sleep(3000);
      i--;
      if (i > 0) {
        return recursiaRazdel(vitrina, i);
      }
      return i;
    }
  }

  private void selectRazdel(GeneratorData vitrina, boolean isProdServer) throws InterruptedException {
    waitLoadPage(isProdServer);
    recursiaRazdel(vitrina, 15);
  }

  public boolean isMistakes() {
    List<WebElement> elements = wd.findElements(By.xpath(".//*[@class=\"headTitle\"]"));
    if (elements.size() == 0) {
      return true;
    }
    return false;
  }
}
