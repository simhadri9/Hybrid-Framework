
package driverFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunctions.FunctionLibrary;
import utilities.ExcelFileUtil;

public class DriverScript {
public static WebDriver driver;
String inputpath = "./FileInput/DataEngine.xlsx";
String outputpath = "./FileOutput/HybridResults.xlsx";
ExtentReports report;
ExtentTest logger;
String TestCases = "MasterTestCases";
@Test
public void startTest() throws Throwable
{
	String Module_Status = "";
	//creating object for ExcelfileUtil class
	ExcelFileUtil xl = new ExcelFileUtil(inputpath);
	//itterate all rows in MasterTestCase
	for(int i=1;i<=xl.rowCount(TestCases);i++)
	{
		if(xl.getCellData(TestCases, i, 2).equalsIgnoreCase("y"))
		{
			//store Each test case into TCmodule
			String TCModule = xl.getCellData(TestCases, i, 1);
			report = new ExtentReports("./target/Reports/"+TCModule+FunctionLibrary.generateDate()+".html");
			logger = report.startTest(TCModule);
			//iterate all rows in each sheet TCModule
			for(int j=1;j<=xl.rowCount(TCModule);j++)
			{
				//call cells from TCModule
				String Description = xl.getCellData(TCModule, j, 0);
				String Object_Type = xl.getCellData(TCModule, j, 1);
				String Locator_Type = xl.getCellData(TCModule, j, 2);
				String Locator_Value =  xl.getCellData(TCModule, j, 3);
				String Test_Data = xl.getCellData(TCModule, j, 4);
				try 
				{
					if(Object_Type.equalsIgnoreCase("startBrowser"))
					{
						driver = FunctionLibrary.startBrowser();
						logger.log(LogStatus.INFO,Description);
					}
					if(Object_Type.equalsIgnoreCase("openUrl"))
					{
						FunctionLibrary.openUrl();
						logger.log(LogStatus.INFO,Description);
					}
					if(Object_Type.equalsIgnoreCase("waitforElement"))
					{
						FunctionLibrary.waitforElement(Locator_Type, Locator_Value, Test_Data);
						logger.log(LogStatus.INFO,Description);
					}
					if(Object_Type.equalsIgnoreCase("typeAction"))
					{
						FunctionLibrary.typeAction(Locator_Type, Locator_Value, Test_Data);
						logger.log(LogStatus.INFO,Description);
						Thread.sleep(3000);
						JavascriptExecutor js = (JavascriptExecutor) driver;
						js.executeScript("window.scrollBy(0,250)", "");
					}
					if(Object_Type.equalsIgnoreCase("clickAction"))
					{
						
						FunctionLibrary.clickAction(Locator_Type, Locator_Value);
						logger.log(LogStatus.INFO,Description);
					}
					if(Object_Type.equalsIgnoreCase("validatetitle"))
					{
						FunctionLibrary.validatetitle(Test_Data);
						logger.log(LogStatus.INFO,Description);
					}
					if(Object_Type.equalsIgnoreCase("closeBrowser"))
					{
						FunctionLibrary.closeBrowser();
						logger.log(LogStatus.INFO,Description);
					}
					if(Object_Type.equalsIgnoreCase("dropDownAction"))
					{
						FunctionLibrary.dropDownAction(Locator_Type, Locator_Value, Test_Data);
						logger.log(LogStatus.INFO,Description);
					}
					if(Object_Type.equalsIgnoreCase("captureStockNum"))
					{
						FunctionLibrary.captureStockNum(Locator_Type, Locator_Value);
						logger.log(LogStatus.INFO,Description);
					}
					if(Object_Type.equalsIgnoreCase("stockTable"))
					{
						FunctionLibrary.stockTable();
						logger.log(LogStatus.INFO,Description);
					}
					if(Object_Type.equalsIgnoreCase("capturesupnum"))
					{
						FunctionLibrary.capturesupnum(Locator_Type, Locator_Value);
						logger.log(LogStatus.INFO,Description);
					}
					if(Object_Type.equalsIgnoreCase("supplierTable"))
					{
						FunctionLibrary.supplierTable();
						logger.log(LogStatus.INFO,Description);
					}
					if(Object_Type.equalsIgnoreCase("captureCusnum"))
					{
						FunctionLibrary.captureCusnum(Locator_Type, Locator_Value);
						logger.log(LogStatus.INFO,Description);
					}
					if(Object_Type.equalsIgnoreCase("customerTable"))
					{
			
						FunctionLibrary.customerTable();
						logger.log(LogStatus.INFO,Description);
					}
					//write as pass into status cell in TCModule sheet
					xl.setCellData(TCModule, j, 5, "pass",outputpath);
					logger.log(LogStatus.PASS,Description);
					Module_Status = "true";
				}
				catch (Exception e)
				{
					System.out.println(e.getMessage());
					//write as fail into status cell in TCModule sheet
					xl.setCellData(TCModule, j, 5, "fail",outputpath);
					logger.log(LogStatus.FAIL,Description);
					Module_Status = "false";
				}
				if(Module_Status.equalsIgnoreCase("true"))
				{
					xl.setCellData(TestCases, i, 3, "pass", outputpath);
				}
				else
				{
					xl.setCellData(TestCases, i, 3, "fail", outputpath);
				}
				report.endTest(logger);
				report.flush();	
			}
		}
		else
		{
			//which ever testcase flag to N write as blocked into status cell in MastertestCase
			xl.setCellData(TestCases, i, 3, "Blocked",outputpath);
		}
	}
}
}









