package edu.ncsu.csc.itrust.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

//import edu.ncsu.csc.itrust.enums.TransactionType;

public class ViewSendReminderTest  extends iTrustSeleniumTest{
    private HtmlUnitDriver driver;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        gen.clearAllTables();
        gen.standardData();
    }

    public void testViewSendReminders() throws Exception{
        WebDriver driver = login("9000000001", "pw");
        driver.findElement(By.linkText("Send Reminders")).click();

        assertEquals("iTrust - Reminder homepage", driver.getTitle());

        new Select(driver.findElement(By.name("viewSelect"))).selectByValue("send");
        driver.findElement(By.id("select_View")).click();

        driver.findElement(By.name("number")).sendKeys("100");
        driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
        assertTrue(driver.getCurrentUrl().equals(ADDRESS + "auth/admin/viewApptReminders.jsp"));
    }

    public void testViewMyReminders() throws Exception{
        WebDriver driver = login("9000000001", "pw");
        driver.findElement(By.linkText("Send Reminders")).click();
        assertEquals("iTrust - Reminder homepage", driver.getTitle());
        new Select(driver.findElement(By.name("viewSelect"))).selectByValue("view");
        driver.findElement(By.id("select_View")).click();
        assertTrue(driver.getPageSource().contains("Subject"));
    }
}
