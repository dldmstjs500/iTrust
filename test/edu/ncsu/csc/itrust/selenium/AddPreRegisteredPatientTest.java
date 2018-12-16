package edu.ncsu.csc.itrust.selenium;

import edu.ncsu.csc.itrust.enums.TransactionType;
import org.openqa.selenium.*;

import java.util.concurrent.TimeUnit;


public class AddPreRegisteredPatientTest extends iTrustSeleniumTest{
	
	protected WebDriver driver;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		gen.clearAllTables();
		gen.standardData();
		gen.preRegisteredPatientData();
	}
	

	
	public void testCreatePatient() throws Exception{
		//Login
		WebDriver wd = new Driver();
		// Implicitly wait at most 2 seconds for each element to load
		wd.manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

		wd.get("http://localhost:8080/iTrust/util/addPreregisteredPatient.jsp");
		// log in using the given username and password
		WebElement first = wd.findElement(By.name("firstName"));
		WebElement last = wd.findElement(By.name("lastName"));
		WebElement email = wd.findElement(By.name("email"));
		WebElement pass = wd.findElement(By.name("password"));
		WebElement conf = wd.findElement(By.name("confirmPassword"));
		first.sendKeys("First");
		last.sendKeys("Last");
		email.sendKeys("mail1@google.com");
		pass.sendKeys("asdf1");
		conf.sendKeys("asdf1");
		pass.submit();


		assertEquals("iTrust - Add Pre-Registered Patient", wd.getTitle());
		WebElement msg = wd.findElement(By.id("mid"));
		wd = login(msg.getAttribute("name"),"asdf1");
		assertEquals("iTrust - Patient Home", wd.getTitle());
		WebElement p = wd.findElement(By.name("preReg"));

		assertEquals("preReg", p.getAttribute("id"));



	}

	public void testPrePatientList() throws Exception{
		//Login
		driver = login("9000000000", "pw");
		assertEquals("iTrust - HCP Home", driver.getTitle());
		assertLogged(TransactionType.HOME_VIEW, 9000000000L, 0L, "");

		driver.get("http://localhost:8080/iTrust/auth/hcp-uap/viewPreRegisteredPatientList.jsp");
		WebElement p = driver.findElement(By.id("patientList"));
		assertEquals("display fTable", p.getAttribute("class"));
		WebElement r = driver.findElement(By.id("regButton0"));
		r.click();
		WebElement d = driver.findElement(By.id("deButton0"));
		d.click();
		driver.get("http://localhost:8080/iTrust/auth/hcp-uap/viewPreRegisteredPatientList.jsp");
		try{
			WebElement h = driver.findElement(By.id("regButton0"));
			assertEquals("Not","Equal");
		}
		catch (NoSuchElementException e)
		{
			assertEquals("Equal", "Equal");
		}







	}

}