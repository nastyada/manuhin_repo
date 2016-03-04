package ru.stqa.pft.addressbook414.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by Юрий on 28.02.2016.
 */
public class NavigationHelper extends HelperBase {

  public NavigationHelper(WebDriver driver) {
    super(driver);
  }

  public void goToGroup() {
    click(By.linkText("group page"));
  }

  public void goToHome() {
    click(By.linkText("home page"));
  }

  public void submit() {
    click(By.name("submit"));
  }

  public void update() {
    click(By.name("update"));
  }

  public void delete() {
    click(By.xpath("(.//*[@id='content']/form[2]/input[2])[1]"));

  }
}