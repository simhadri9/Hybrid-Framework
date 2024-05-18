package commonFunctions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import utilities.PropertyFileUtil;

public class FunctionLibrary {
	public static WebDriver driver;
	//method for lunching browser
	public static WebDriver startBrowser() throws Throwable
	{
		if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("chrome"))
		{
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			
		}
		else if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("firefox"))
		{
			driver = new FirefoxDriver();
		}
		else
		{
			Reporter.log("Browser value is not matching",true);
		}
		return driver;
	}
	//method for lunching URL
	
	public static void openUrl() throws Throwable
	{
		driver.get(PropertyFileUtil.getValueForKey("Url"));
	}
	//method for wait for any elemeny
	public static void waitforElement(String LocatorType,String LocatorValue,String TestData)
	{
	
		WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(Integer.parseInt(TestData)));
		if(LocatorType.equalsIgnoreCase("id"))
		{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LocatorValue)));
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LocatorValue)));
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(LocatorValue)));
		}
	}
	//method for textboxes
	public static void typeAction(String LocatorType,String LocatorValue,String TestData)
	{
		if(LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).clear();
			driver.findElement(By.id(LocatorValue)).sendKeys(TestData);
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocatorValue)).clear();
			driver.findElement(By.xpath(LocatorValue)).sendKeys(TestData);
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocatorValue)).clear();
			driver.findElement(By.name(LocatorValue)).sendKeys(TestData);
		}
		
	}
	//method for click any webelement
	public static void clickAction(String LocatorType,String LocatorValue)
	{
		if(LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocatorValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocatorValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocatorValue)).sendKeys(Keys.ENTER);
		}
	}
	//method for validate title
	public static void validatetitle(String Expected_Title)
	{
		String Actual_Title = driver.getTitle();
		try {
		Assert.assertEquals(Actual_Title, Expected_Title,"Title Not mtching");
		}catch(AssertionError a)
		{
			System.out.println(a.getMessage());
		}
	}
	public static void closeBrowser()
	{
		driver.quit();
	}
	//method for generate date
	public static String generateDate()
	{
		java.util.Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat("YYYY_MM_dd hh_mm_ss");
		return df.format(date);
	}
	//method for listboxes
	public static void dropDownAction(String LocatorType,String LocatorValue,String TestData)
	{
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			int value = Integer.parseInt(TestData);
			Select element = new Select(driver.findElement(By.xpath(LocatorValue)));
			element.selectByIndex(value);
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			int value = Integer.parseInt(TestData);
			Select element = new Select(driver.findElement(By.id(LocatorValue)));
			element.selectByIndex(value);
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			int value = Integer.parseInt(TestData);
			Select element = new Select(driver.findElement(By.name(LocatorValue)));
			element.selectByIndex(value);
		}
	}
	//method for stock number capture into notepad
	public static void captureStockNum(String LocatorType,String LocatorValue) throws Throwable
	{
		String stockNum = "";
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			stockNum = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			stockNum = driver.findElement(By.name(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			stockNum = driver.findElement(By.id(LocatorValue)).getAttribute("value");
		}
		FileWriter fw = new FileWriter("./CaptureData/stockNumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(stockNum);
		bw.flush();
		bw.close();
	}
	//method for stock table
	public static void stockTable() throws Throwable
	{
		FileReader fr = new FileReader("./CaptureData/stockNumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String exp_data = br.readLine();
		if(!driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search_textbox"))).isDisplayed())
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search_panel"))).click();
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search_textbox"))).clear();
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search_textbox"))).sendKeys(exp_data);
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("serch_button"))).click();
			Thread.sleep(4000);
			String Act_data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
			Reporter.log(exp_data+"    "+Act_data,true);
			try {
			Assert.assertEquals(exp_data,Act_data,"stock number is not matching");
			}catch(AssertionError a)
			{
				System.out.println(a.getMessage());
			}
	}
	//method for capture number
	public static void capturesupnum (String LocatorType,String LocatorValue) throws Throwable
	{
		String suppliernum = "";
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			suppliernum = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			suppliernum = driver.findElement(By.name(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			suppliernum = driver.findElement(By.id(LocatorValue)).getAttribute("value");
		}
		FileWriter fw = new FileWriter("./CaptureData/suppliernum.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(suppliernum);
		bw.flush();
		bw.close();
	}
	//method for suppiler table
	public static void supplierTable()throws Throwable
	{
		FileReader fr = new FileReader("./CaptureData/suppliernum.txt");
		BufferedReader br = new BufferedReader(fr);
		String exp_data = br.readLine();
		if(!driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search_textbox"))).isDisplayed())
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search_panel"))).click();
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search_textbox"))).clear();
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search_textbox"))).sendKeys(exp_data);
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("serch_button"))).click();
			Thread.sleep(4000);
			String act_data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[6]/div/span/span")).getText();
			Reporter.log(act_data+"   "+exp_data,true);
			try {
			Assert.assertEquals(act_data, exp_data, "suppliernumber not matching");
			}catch (AssertionError a) {
				System.out.println(a.getMessage());
			}
	}
	//method for capture customer number
	public static void captureCusnum(String LocatorType,String LocatorValue) throws Throwable
	{
		String  customernum = "";
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			customernum = driver.findElement(By.xpath(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			customernum = driver.findElement(By.name(LocatorValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			customernum = driver.findElement(By.id(LocatorValue)).getAttribute("value");
		}
		FileWriter fw = new FileWriter("./CaptureData/ customernum.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write( customernum);
		bw.flush();
		bw.close();
	}
	   //method for customer table
		public static void customerTable() throws Throwable
		{
			FileReader fr = new FileReader("./CaptureData/suppliernum.txt");
			BufferedReader br = new BufferedReader(fr);
			String exp_data = br.readLine();
			if(!driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search_textbox"))).isDisplayed())
				driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search_panel"))).click();
				driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search_textbox"))).clear();
				driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search_textbox"))).sendKeys(exp_data);
				driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("serch_button"))).click();
				Thread.sleep(4000);
				String act_data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[5]/div/span/span")).getText();
				Reporter.log(act_data+"   "+exp_data,true);
				try {
					Assert.assertEquals(act_data, exp_data, "customer number is not matchng ");
				} catch (AssertionError e) {
					System.out.println(e.getMessage());
				}
		}

}







