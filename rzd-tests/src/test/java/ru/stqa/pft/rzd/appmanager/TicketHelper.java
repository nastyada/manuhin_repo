package ru.stqa.pft.rzd.appmanager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.stqa.pft.rzd.model.Train;
import ru.stqa.pft.rzd.model.Trains;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Юрий on 05.03.2016.
 */
public class TicketHelper extends HelperBase {

  private WebElement locator;

  public TicketHelper(WebDriver wd) {
    super(wd);
  }

  public void chooseDirection() throws InterruptedException {
    waitElement(By.id("name0"), 100);
    type(By.id("name0"), "САНКТ-ПЕТЕРБ");
    waitElement(By.cssSelector("div.station"), 100);
    click(By.cssSelector("div.station"));
    Thread.sleep(10000);
    waitElement(By.id("name1"), 100);
    type(By.id("name1"), "МОС");
    waitElement(By.xpath("//div[7]/div[1]"), 100);
    click(By.xpath("//div[7]/div[1]"));
    Thread.sleep(10000);
  }

  public void chooseDate(By dateLocator) {
    click(By.id("buttonDate"));
    click(dateLocator);
    click(By.id("Submit"));
  }

  public void chosenTrain(By locator) {
    List<WebElement> elements = wd.findElements(By.xpath("//*[@class=\"trlist__trlist-row trslot \"]"));


    radioButton(locator);
  }

  public Trains all() throws InterruptedException, ParseException {
    Thread.sleep(10000);
    Trains trains = new Trains();
    List<WebElement> rows = wd.findElements(By.xpath("//*[@class=\"trlist__trlist-row trslot \"]"));
    for (WebElement row: rows) {
      List<WebElement> cells = row.findElements(By.tagName("td"));
      int id = Integer.parseInt(cells.get(0).findElement(By.xpath(".//input[1]")).getAttribute("value"));
      // номер поезда
      String number = cells.get(2)
              .findElement(By.xpath(".//*[@class=\"trlist__cell-pointdata__tr-num train-num-0\"]")).getText();
      // наименование поезда
      String name = "";
     // if (isElementPresent(cells.get(2)
     //         .findElement(By.xpath(".//*[@class=\"trlist__cell-pointdata__tr-brand\"]"))) {
     //   name = cells.get(2)
     //           .findElement(By.xpath(".//*[@class=\"trlist__cell-pointdata__tr-brand\"]")).getText();
     // }
      // станция отправления
      String stationFrom = cells.get(3)
              .findElement(By.xpath(".//*[@class=\"trlist__cell-pointdata__station-name\"]")).getText();
      // дата отправления
      String dateFrom = cells.get(3)
              .findElement(By.xpath(".//*[@class=\"trlist__cell-pointdata__date-sub\"]")).getText()
              .substring(1).trim();
      // время отправления
      String timeFrom = cells.get(3)
              .findElement(By.xpath(".//*[@class=\"trlist__cell-pointdata__time\"]")).getText();
      // станция назначения
      String stationTo = cells.get(7)
              .findElement(By.xpath(".//*[@class=\"trlist__cell-pointdata__station-name\"]")).getText();
      // дата прибытия
      String dateTo = cells.get(7)
              .findElement(By.xpath(".//*[@class=\"trlist__cell-pointdata__date-sub\"]")).getText()
              .substring(1).trim();
      // время прибытия
      String timeTo = cells.get(7)
              .findElement(By.xpath(".//*[@class=\"trlist__cell-pointdata__time\"]")).getText();
      // тип вагона (сидячий, купе, св, плацкарт...)
      String typePlace = cells.get(9).getText();
      // Кол-во мест нужного типа
      int countPlace = Integer.parseInt(cells.get(10).getText());
      // Цена билета
      int price = Integer.parseInt(cells.get(11).getText().replaceAll("руб.","").trim());

      Date datetimeFrom = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(dateFrom + " " + timeFrom + ":00");
      Date datetimeTo = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(dateTo + " " + timeTo + ":00");

      trains.add(new Train().withId(id).withNumber(number).withName(name)
              .withDateTimeFrom(datetimeFrom).withStationFrom(stationFrom)
              .withDateTimeTo(datetimeTo).withStationTo(stationTo)
              .withTypePlace(typePlace).withCountPlace(countPlace).withPrice(price));
    }
    return trains;
  }

  public void chooseType(String typePl) throws InterruptedException {
    Thread.sleep(10000);
    String typePlaceXpath = "//*[@class=\"dottedLink\" and @name=\"nothing\"]";
    Integer delay = 60;
    waitElement(By.xpath(typePlaceXpath), delay);
    click(By.xpath(typePlaceXpath));
    if (typePl.equals("Купе")) {
      click(By.xpath("//*[@type=\"checkbox\" and @name=\"car-type4\"]"));
    } else if (typePl.equals("Сидячий")) {
      click(By.xpath("//*[@type=\"checkbox\" and @name=\"car-type3\"]"));
    }
    click(By.xpath("//*[@id='Submit']/span[2]"));
  }

  public Train chooseTrain(Trains trains, String typePlace, Integer countPlace, Date dateFrom, Date dateTo, String condition) {
    Train train = new Train();
    if (condition == "до") {
      //WebElement chosenUser = users.stream().filter((u) -> !(u.getText().equals(adminLogin))).findFirst().get();
     // assertThat(after, equalTo(
     //         before.withAdded(contact.withId(after.stream().mapToInt((c) -> (c.getId())).max().getAsInt()))));
      //asList(contact.getEmail(), contact.getEmail2(), contact.getEmail3())
      //        .stream().filter((s) -> ! s.equals(""))
      //        .collect(Collectors.joining("\n"));
      // assertThat(uiGroups, equalTo(dbGroups
      //.stream().map((g) -> new GroupData().withId(g.getId()).withName(g.getName()))
      //        .collect(Collectors.toSet())));
      int minPriceTrain = trains.stream()
              .filter((t) -> (t.getDatetimeFrom().after(dateFrom)))
              .filter((t) -> (t.getDatetimeTo().before(dateTo)))
              .filter((t) -> (t.getCountPlace() >= countPlace))
              .mapToInt((t) -> (t.getPrice())).min().getAsInt();
      int id = trains.stream()
              .filter((t) -> (t.getDatetimeFrom().after(dateFrom)))
              .filter((t) -> (t.getDatetimeTo().before(dateTo)))
              .filter((t) -> (t.getCountPlace() >= countPlace))
              .filter((t) -> (t.getPrice().equals(minPriceTrain))).findFirst().get().getId();
      train = trains.stream().filter((t) -> (t.getId().equals(id))).findFirst().get();
    } else {
      int minPriceTrain = trains.stream()
              .filter((t) -> (t.getDatetimeFrom().after(dateFrom)))
              .filter((t) -> (t.getDatetimeTo().after(dateTo)))
              .filter((t) -> (t.getCountPlace() >= countPlace))
              .mapToInt((t) -> (t.getPrice())).min().getAsInt();
      int id = trains.stream()
              .filter((t) -> (t.getDatetimeFrom().after(dateFrom)))
              .filter((t) -> (t.getDatetimeTo().after(dateTo)))
              .filter((t) -> (t.getCountPlace() >= countPlace))
              .filter((t) -> (t.getPrice().equals(minPriceTrain))).findFirst().get().getId();
      train = trains.stream().filter((t) -> (t.getId().equals(id))).findFirst().get();
    }

    return train;
  }
}
