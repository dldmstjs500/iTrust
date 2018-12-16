package edu.ncsu.csc.itrust.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class ViewTransactionLogsTest  extends iTrustSeleniumTest{

    private WebDriver driver;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        gen.clearAllTables();
        gen.standardData();
    }

    public void testViewTransactionLogs() throws Exception {
        // login as admin
        driver = login("9000000001", "pw");
        // select View Transaction Logs from the menu
        driver.findElement(By.linkText("View Transaction Logs")).click();
        // check web title
        assertEquals("iTrust - View Transaction Logs", driver.getTitle());
        // select "all" option from logged-in user role
        Select dropdown1 = new Select(driver.findElement(By.name("logging")));
        dropdown1.selectByValue("all");
        // select "all" option from secondary user role
        Select dropdown2 = new Select(driver.findElement(By.name("secondary")));
        dropdown2.selectByValue("all");
        // set up start date and end date options: 06/01/2007 - 06/30/2007
        WebElement startDate = driver.findElement(By.name("startdate"));
        startDate.clear();
        startDate.sendKeys("06/01/2007");
        WebElement endDate = driver.findElement(By.name("enddate"));
        endDate.clear();
        endDate.sendKeys("06/30/2007");
        // select "all" which is "-1" option from transaction type
        Select dropdown3 = new Select(driver.findElement(By.name("transactionType")));
        dropdown3.selectByValue("-1");
        // click View button
        driver.findElement(By.xpath("//input[@value='View']")).sendKeys(Keys.RETURN);
        // check if the view button directed user to the result page
        assertEquals("iTrust - Transaction Logs Result", driver.getTitle());
        // check if the result table has correct data
        WebElement entryTable = driver.findElements(By.tagName("table")).get(0);
        List<WebElement> tableRows = entryTable.findElements(By.tagName("td"));
        assertEquals("hcp", tableRows.get(0).getText());
        assertEquals("patient", tableRows.get(1).getText());
        assertEquals("PRESCRIPTION_REPORT_VIEW", tableRows.get(2).getText());
        assertEquals("Viewed patient records", tableRows.get(3).getText());
        assertEquals("2007-06-25 06:54:59.0", tableRows.get(4).getText());
    }

    public void testSummaryTransactionResult() throws Exception{
        // same process as the previous test
        driver = login("9000000001", "pw");
        driver.findElement(By.linkText("View Transaction Logs")).click();
        assertEquals("iTrust - View Transaction Logs", driver.getTitle());
        Select dropdown1 = new Select(driver.findElement(By.name("logging")));
        dropdown1.selectByValue("all");
        Select dropdown2 = new Select(driver.findElement(By.name("secondary")));
        dropdown2.selectByValue("all");
        WebElement startDate = driver.findElement(By.name("startdate"));
        startDate.clear();
        startDate.sendKeys("06/01/2007");
        WebElement endDate = driver.findElement(By.name("enddate"));
        endDate.clear();
        endDate.sendKeys("06/30/2007");
        Select dropdown3 = new Select(driver.findElement(By.name("transactionType")));
        dropdown3.selectByValue("-1");
        // click Summarize button
        driver.findElement(By.xpath("//input[@value='Summarize']")).sendKeys(Keys.RETURN);
        // check if the summarize button directed user to the summary result page
        assertEquals("iTrust - Transaction Logs Result Summary", driver.getTitle());
        // check if the result page has correct number of graphs
        List<WebElement> elems = driver.findElements(By.xpath("//ul/div"));
        // the number of graphs
        assertEquals(4, elems.size());
        assertEquals("first_div", elems.get(0).getAttribute("id"));
        assertEquals("second_div", elems.get(1).getAttribute("id"));
        assertEquals("monthly_div", elems.get(2).getAttribute("id"));
        assertEquals("type_div", elems.get(3).getAttribute("id"));
    }
}

