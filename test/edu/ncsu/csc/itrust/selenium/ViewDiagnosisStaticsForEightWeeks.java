package edu.ncsu.csc.itrust.selenium;

import edu.ncsu.csc.itrust.enums.TransactionType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.sql.SQLException;

public class ViewDiagnosisStaticsForEightWeeks extends iTrustSeleniumTest {

    protected void setUp() throws Exception {
        super.setUp();
        gen.clearAllTables();
        gen.standardData();
        gen.uc14s3();
    }

    protected void tearDown() throws IOException, SQLException {
        gen.clearAllTables();
    }

    public void testViewDiagnosisStaticsForEightWeeksRequest() throws Exception {
        HtmlUnitDriver driver = (HtmlUnitDriver) login("9000000000", "pw");
        assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");


        driver.findElement(By.linkText("Diagnosis Trends")).click();
        WebElement actionTypeSelectElement = driver.findElement(By.name("viewSelect"));
        Select actionTypeSelect = new Select(actionTypeSelectElement);
        actionTypeSelect.selectByVisibleText("Trends (Past 8 Weeks)");
        driver.findElement(By.id("select_View")).click();



        WebElement diagnosisTypeSelectElemeent = driver.findElement(By.name("icdCode"));
        WebElement zipCodeField = driver.findElement(By.name("zipCode"));
        WebElement startDateField = driver.findElement(By.name("startDate"));

        Select diagnosisTypeSelect = new Select(diagnosisTypeSelectElemeent);
        diagnosisTypeSelect.selectByVisibleText("84.50 - Malaria");
        zipCodeField.clear();
        zipCodeField.sendKeys("27607");
        startDateField.clear();
        startDateField.sendKeys("11/02/2017");

        driver.findElement(By.id("select_diagnosis")).click();

        assertTrue("Chart element's stage should exist",
                driver.getPageSource().contains("id=\"chart_div\""));


    }
}
