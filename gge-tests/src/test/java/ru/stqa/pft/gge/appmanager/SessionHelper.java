package ru.stqa.pft.gge.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by Юрий on 05.03.2016.
 */
public class SessionHelper extends HelperBase {

  public SessionHelper(WebDriver wd) {
    super(wd);
  }

  public void login(String username, String password) {
  //  click(By.id("LoginForm"));
    type(By.name("user"), username);
    type(By.name("pass"), password);
    click(By.xpath("//form[@id='LoginForm']/input[3]"));



    wd.findElement(By.id("content")).click();
    wd.findElement(By.id("username")).click();
    wd.findElement(By.id("username")).clear();
    wd.findElement(By.id("username")).sendKeys(username);
    wd.findElement(By.id("password")).click();
    wd.findElement(By.id("password")).sendKeys(password);
    wd.findElement(By.id("submitBtn")).click();

  }

}
