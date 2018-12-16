package edu.ncsu.csc.itrust.selenium;

import edu.ncsu.csc.itrust.enums.TransactionType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class ViewCauseOfDeathReportTest extends iTrustSeleniumTest {
    private HtmlUnitDriver driver;

    @Override
    protected void setUp() throws Exception {
        super.setUp(); // clear tables is called in super
        gen.clearAllTables();
        gen.standardData();
    }

    /**
     * Login, enter data that should return a patient death, then check that the table has the right values
     * @throws Exception
     */
    public void testViewCauseOfDeathReport() throws Exception {
        //login and navigate to cause of death page
        driver = (HtmlUnitDriver)login("9000000000","pw");
        assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
        driver.findElement(By.linkText("Cause of Death Report")).click();
        assertLogged(TransactionType.DEATH_TRENDS_VIEW, 9000000000L, 0, "");

        //Enter in the data
        Select genderField = new Select(driver.findElement(By.name("gender")));
        genderField.selectByVisibleText("All");
        driver.findElement(By.name("startYear")).sendKeys("2000");
        driver.findElement(By.name("endYear")).sendKeys("2010");
        driver.findElementById("view_report").click();

        //Check that data field corresponds to what it should for the basic data set
        List<WebElement> rows = driver.findElementsByTagName("tr");
        assertEquals(rows.size(), 5);
        List<WebElement> col = rows.get(1).findElements(By.tagName("td"));
        assertEquals(col.get(0).getText(), "All");
        assertEquals(col.get(1).getText(), "250.10");
        assertEquals(col.get(2).getText(), "Diabetes with ketoacidosis");
        assertEquals(col.get(3).getText(), "1");
        col = rows.get(2).findElements(By.tagName("td"));
        assertEquals(col.get(0).getText(), "All");
        assertEquals(col.get(1).getText(), "N/A");
        assertEquals(col.get(2).getText(), "N/A");
        assertEquals(col.get(3).getText(), "0");
        col = rows.get(3).findElements(By.tagName("td"));
        assertEquals(col.get(0).getText(), "Yours");
        assertEquals(col.get(1).getText(), "250.10");
        assertEquals(col.get(2).getText(), "Diabetes with ketoacidosis");
        assertEquals(col.get(3).getText(), "1");
        col = rows.get(4).findElements(By.tagName("td"));
        assertEquals(col.get(0).getText(), "Yours");
        assertEquals(col.get(1).getText(), "N/A");
        assertEquals(col.get(2).getText(), "N/A");
        assertEquals(col.get(3).getText(), "0");

    }

    // Login, enter an invalid year range (2010 - 2000), then check that the error is shown
    public void testViewCauseOfDeathReportBadYear() throws Exception {
        //login and navigate to cause of death page
        driver = (HtmlUnitDriver)login("9000000000","pw");
        assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");
        driver.findElement(By.linkText("Cause of Death Report")).click();
        assertLogged(TransactionType.DEATH_TRENDS_VIEW, 9000000000L, 0, "");

        //Enter in the data with a bad year range
        Select genderField = new Select(driver.findElement(By.name("gender")));
        genderField.selectByVisibleText("All");
        driver.findElement(By.name("startYear")).sendKeys("2010");
        driver.findElement(By.name("endYear")).sendKeys("2000");
        driver.findElementById("view_report").click();

        //Check that the error is shown
        List<WebElement> errors = driver.findElementsByXPath("//div[@class='errorList']");
        assertTrue(errors.size() == 1);
        assertEquals(errors.iterator().next().getText(), "Start date must be before end date!");
    }
}
