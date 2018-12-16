
package edu.ncsu.csc.itrust.selenium;

import edu.ncsu.csc.itrust.enums.TransactionType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageFilterTest extends iTrustSeleniumTest {

    protected void setUp() throws Exception {
        super.setUp();
        gen.clearAllTables();
        gen.standardData();
    }

    protected void tearDown() throws IOException, SQLException {
        gen.clearAllTables();
    }

    public WebDriver loginClickMail(String username, String password) throws Exception {
        WebDriver driver = login(username, password);
        driver.findElement(By.linkText("Message Inbox")).click();
        assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
        return driver;
    }

    /**
     * Test the message inbox filter by navigating to it, checking the existing messages, adding a filter and
     * rechecking, then saving the filter and canceling and reloading to make sure it is saved.
     * @throws Exception
     */


    public void testMessageInboxFilter() throws Exception {
        //login and navigate to inbox
        HtmlUnitDriver driver = (HtmlUnitDriver)loginClickMail("9000000000", "pw");

        // Save each message sender in an arraylist
        WebElement table = driver.findElementById("mailbox");
        ArrayList<String> messageSenders = new ArrayList<String>();
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        rows.remove(0); // don't consider the header
        for (WebElement row : rows) {
            List<WebElement> cols = row.findElements(By.tagName("td"));
            messageSenders.add(cols.get(0).getText());
        }
        // Check that the number of messages is correct
        assertTrue("Three messages received from Andy Programmer before filtering", Collections.frequency(messageSenders, "Andy Programmer") == 3);
        assertTrue("Ten messages received from Random Person before filtering", Collections.frequency(messageSenders, "Random Person") == 10);

        // Try a filter that should only retrieve the first element in the mailbox
        WebElement senderField = driver.findElement(By.name("sender"));
        senderField.clear();
        senderField.sendKeys("Andy Programmer");
        WebElement subjectField = driver.findElement(By.name("subject"));
        subjectField.clear();
        subjectField.sendKeys("Scratchy Throat");

        // Test the filter
        driver.findElement(By.name("testFilter")).click();

        // Grab the message senders again
        table = driver.findElementById("mailbox");
        messageSenders = new ArrayList<String>();
        rows = table.findElements(By.tagName("tr"));
        rows.remove(0); // don't consider the header
        for (WebElement row : rows) {
            List<WebElement> cols = row.findElements(By.tagName("td"));
            messageSenders.add(cols.get(0).getText());
        }
        assertTrue("One message received from Andy Programmer after filtering", Collections.frequency(messageSenders, "Andy Programmer") == 1);
        assertTrue("One message total after filtering", messageSenders.size() == 1);

        // Save the filter
        driver.findElement(By.name("saveFilter")).click();

        // Clicking cancel should reload the same filter
        senderField = driver.findElement(By.name("sender"));
        senderField.clear();
        senderField.sendKeys("Should be cleared by cancel");
        driver.findElement(By.name("cancel")).click();
        assertEquals("Andy Programmer", driver.findElement(By.name("sender")).getAttribute("value"));

        //login and navigate to inbox
        driver = (HtmlUnitDriver)loginClickMail("9000000000", "pw");


        // Sender filter should still be set to Andy Programmer
        assertEquals("Andy Programmer", driver.findElement(By.name("sender")).getAttribute("value"));

    }
}
