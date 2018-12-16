package edu.ncsu.csc.itrust.selenium;

import edu.ncsu.csc.itrust.enums.TransactionType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class EpidemicsStatisticsTest extends iTrustSeleniumTest {

    protected void setUp() throws Exception {
        super.setUp();
        gen.clearAllTables();
        gen.standardData();
        gen.malaria_epidemic();
        gen.influenza_epidemic();
    }

    protected void tearDown() throws IOException, SQLException {
        gen.clearAllTables();
    }

    public void testMalariaEpidemicStatisticsRequest() throws Exception {
        HtmlUnitDriver driver = (HtmlUnitDriver) login("9000000000", "pw");
        assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");


        driver.findElement(By.linkText("Diagnosis Trends")).click();
        WebElement actionTypeSelectElement = driver.findElement(By.name("viewSelect"));
        Select actionTypeSelect = new Select(actionTypeSelectElement);
        actionTypeSelect.selectByVisibleText("Epidemics");
        driver.findElement(By.id("select_View")).click();

        assertThat(driver.getPageSource())
                .as("Page contains table for epidemic entry")
                .contains("Epidemic Evaluation");

        // Enter in data into fields
        WebElement diagnosisTypeSelectElement = driver.findElement(By.name("icdCode"));
        WebElement zipCodeField = driver.findElement(By.name("zipCode"));
        WebElement startDateField = driver.findElement(By.name("startDate"));
        WebElement thresholdField = driver.findElement(By.name("threshold"));
        Select diagnosisTypeSelect = new Select(diagnosisTypeSelectElement);
        diagnosisTypeSelect.selectByVisibleText("84.50 - Malaria");
        zipCodeField.clear();
        zipCodeField.sendKeys("27607");
        startDateField.clear();
        startDateField.sendKeys("11/02/2017");
        thresholdField.clear();
        thresholdField.sendKeys("110");
        driver.findElement(By.id("select_diagnosis")).click();

        assertThat(driver.getPageSource())
                .as("System reports that there is a malaria epidemic")
                .contains("THERE IS AN EPIDEMIC OCCURRING IN THIS REGION!");
        assertThat(driver.getPageSource())
                .as("Chart element's stage should exist")
                .contains("id=\"stage\"");
        assertThat(driver.getPageSource())
                .as("Chart element's initialization array code should exist")
                .contains("\"Current Period\", 1");
    }

    public void testInfluenzaEpidemicStatisticsRequest() throws Exception {
        HtmlUnitDriver driver = (HtmlUnitDriver) login("9000000000", "pw");
        assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");


        driver.findElement(By.linkText("Diagnosis Trends")).click();
        WebElement actionTypeSelectElement = driver.findElement(By.name("viewSelect"));
        Select actionTypeSelect = new Select(actionTypeSelectElement);
        actionTypeSelect.selectByVisibleText("Epidemics");
        driver.findElement(By.id("select_View")).click();

        assertThat(driver.getPageSource())
                .as("Page contains table for epidemic entry")
                .contains("Epidemic Evaluation");

        // Enter in data into fields
        WebElement diagnosisTypeSelectElement = driver.findElement(By.name("icdCode"));
        WebElement zipCodeField = driver.findElement(By.name("zipCode"));
        WebElement startDateField = driver.findElement(By.name("startDate"));
        Select diagnosisTypeSelect = new Select(diagnosisTypeSelectElement);
        diagnosisTypeSelect.selectByVisibleText("487.00 - Influenza");
        zipCodeField.clear();
        zipCodeField.sendKeys("27607");
        startDateField.clear();
        startDateField.sendKeys("11/02/2017");
        driver.findElement(By.id("select_diagnosis")).click();

        assertThat(driver.getPageSource())
                .as("System reports that there is a malaria epidemic")
                .contains("THERE IS AN EPIDEMIC OCCURRING IN THIS REGION!");
        assertThat(driver.getPageSource())
                .as("Chart element's stage should exist")
                .contains("id=\"stage\"");
        assertThat(driver.getPageSource())
                .as("Chart element's initialization array code should exist")
                .contains("\"Current Period\", 1");
    }

    public void testEightWeeksDiagnosisTrends() throws Exception {
        HtmlUnitDriver driver = (HtmlUnitDriver) login("9000000000", "pw");
        assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");

        // Go to diagnosis statistics page, choose to view trends
        driver.findElement(By.linkText("Diagnosis Trends")).click();
        WebElement actionTypeSelectElement = driver.findElement(By.name("viewSelect"));
        Select actionTypeSelect = new Select(actionTypeSelectElement);
        actionTypeSelect.selectByVisibleText("Trends (Past 8 Weeks)");
        driver.findElement(By.id("select_View")).click();

        // Make sure we have the Diagnosis Statistics entry box somewhere on page
        assertThat(driver.getPageSource())
                .as("Table for Diagnosis Statistics entry is visible")
                .contains("Diagnosis Statistics");

        // Enter in data into fields
        WebElement diagnosisTypeSelectElement = driver.findElement(By.name("icdCode"));
        WebElement zipCodeField = driver.findElement(By.name("zipCode"));
        WebElement startDateField = driver.findElement(By.name("startDate"));
        Select diagnosisTypeSelect = new Select(diagnosisTypeSelectElement);
        diagnosisTypeSelect.selectByVisibleText("84.50 - Malaria");
        zipCodeField.clear();
        zipCodeField.sendKeys("27607");
        startDateField.clear();
        startDateField.sendKeys("11/02/2017");
        driver.findElement(By.id("select_diagnosis")).click();

        assertThat(driver.getPageSource())
                .as("Page source should have the chart/table element")
                .contains("chart_div");
        assertThat(driver.getPageSource())
                .as("Page should contain chart element's array initialization code")
                .contains("[\"Week\",\"Regional Stats\",\"Zipcode Stats\"]")
                .contains(" var chart = new google.visualization.BarChart(document.getElementById('chart_div'));\n" +
                        "            chart.draw(data, options);");
    }
}
