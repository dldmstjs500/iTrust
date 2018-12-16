package edu.ncsu.csc.itrust.selenium;

import edu.ncsu.csc.itrust.enums.TransactionType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LabProceduresFilterTest extends iTrustSeleniumTest {
    protected void setUp() throws Exception {
        super.setUp();
        gen.clearAllTables();
        gen.standardData();
    }

    protected void tearDown() throws IOException, SQLException {
        gen.clearAllTables();
    }

    /**
     * Test the message inbox filter by navigating to it, checking the existing messages, adding a filter and
     * rechecking, then saving the filter and canceling and reloading to make sure it is saved.
     * @throws Exception
     */
    public void testLabProceduresFilter() throws Exception {
        //login and navigate to lab procedures
        HtmlUnitDriver driver = (HtmlUnitDriver) login("5000000001", "pw");
        assertLogged(TransactionType.HOME_VIEW, 5000000001L, 0L, "");
        driver.findElement(By.linkText("All Lab Procedures")).click();

        // Save each HCP into an arraylist
        WebElement table = driver.findElementById("inTransitTable");
        ArrayList<String> hcpNames = new ArrayList<String>();
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        rows.remove(0); // don't consider the header
        rows.remove(0); // don't consider the labels
        for (WebElement row : rows) {
            List<WebElement> cols = row.findElements(By.tagName("td"));
            hcpNames.add(cols.get(4).getText());
        }
        // Check that the number of messages is correct
        assertThat(Collections.frequency(hcpNames,"Kelly Doctor")).as("Number of lab procedures for Kelly Doctor before filtering").isEqualTo(1);
        assertThat(Collections.frequency(hcpNames,"Beaker Beaker")).as("Number of lab procedures for Beaker Beaker before filtering").isEqualTo(17);

        // Try a filter that should only retrieve the first element in the mailbox
        WebElement hcpNamefield = driver.findElement(By.name("hcp_name"));
        hcpNamefield.clear();
        hcpNamefield.sendKeys("Kelly Doctor");
        WebElement priorityField = driver.findElement(By.name("priority"));
        priorityField.clear();
        priorityField.sendKeys("1");

        // Test the filter
        driver.findElement(By.name("testFilter")).click();

        // Grab the HCP names again
        table = driver.findElementById("inTransitTable");
        hcpNames = new ArrayList<String>();
        rows = table.findElements(By.tagName("tr"));
        rows.remove(0); // don't consider the header
        rows.remove(0); // don't consider the labels
        for (WebElement row : rows) {
            List<WebElement> cols = row.findElements(By.tagName("td"));
            hcpNames.add(cols.get(4).getText());
        }
        assertThat(Collections.frequency(hcpNames,"Kelly Doctor")).as("Number of lab procedures for Kelly Doctor before filtering").isEqualTo(1);
        assertThat(Collections.frequency(hcpNames,"Beaker Beaker")).as("Number of lab procedures for Beaker Beaker before filtering").isEqualTo(0);

        // Save the filter
        driver.findElement(By.name("saveFilter")).click();

        // Clicking cancel should reload the same filter
        hcpNamefield = driver.findElement(By.name("hcp_name"));
        hcpNamefield.clear();
        hcpNamefield.sendKeys("Should be cleared by cancel");
        driver.findElement(By.name("cancel")).click();
        assertEquals("Kelly Doctor", driver.findElement(By.name("hcp_name")).getAttribute("value"));

        //login and navigate to inbox
        driver = (HtmlUnitDriver) login("5000000001", "pw");
        assertLogged(TransactionType.HOME_VIEW, 5000000001L, 0L, "");
        driver.findElement(By.linkText("All Lab Procedures")).click();

        // Sender filter should still be set to Andy Programmer
        assertEquals("Kelly Doctor", driver.findElement(By.name("hcp_name")).getAttribute("value"));
    }
}
