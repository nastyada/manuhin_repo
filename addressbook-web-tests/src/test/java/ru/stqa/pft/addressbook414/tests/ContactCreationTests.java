package ru.stqa.pft.addressbook414.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook414.model.TestBase;

public class ContactCreationTests extends TestBase {

  @Test
  public void testContactCreation() throws Exception {
    app.getContactHelper().createNewContact();
    app.getContactHelper().fillContactForm(new ContactData("1","2","3","4","5","6","7","8","9","10"));
    app.getNavigationHelper().submit();
    app.getNavigationHelper().goToHome();
  }

}