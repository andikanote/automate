package keyNKC

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import org.eclipse.persistence.internal.oxm.record.json.JSONParser.pair_return as pair_return
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import com.kms.katalon.core.webui.driver.DriverFactory as DriverFactory
import org.openqa.selenium.WebDriver as WebDriver
import org.openqa.selenium.WebElement as WebElement
import org.openqa.selenium.Keys as Keys
import org.openqa.selenium.By as By
import io.appium.java_client.AppiumDriver as AppiumDriver
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory as MobileDriverFactory
import com.kms.katalon.core.annotation.Keyword as Keyword
import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper as MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.commons.lang3.time.StopWatch
import java.lang.Integer as Integer
import java.util.concurrent.TimeUnit

//import internal.GlobalVariable

public class NKCLogin {
	@Keyword
	def LoginNKC(String strNo, int excelRow){

		TestData dataNKC 			= findTestData('LOGIN')
		String strUsername			= dataNKC.getValue('USERNAME', excelRow)
		String strPassword			= dataNKC.getValue('PASSWORD', excelRow)
		GlobalVariable.strGlbMenu = 'Login'

		//Boolean cekObjekExist = Mobile.verifyElementExist(findTestObject('Object Repository/ObjLogin/LabelSelamatPagi'), 1, FailureHandling.OPTIONAL)

		//while (cekObjekExist == false) {
		//cekObjekExist = Mobile.verifyElementExist(findTestObject('Object Repository/ObjLogin/LabelSelamatPagi'), 2, FailureHandling.OPTIONAL)

		//if (cekObjekExist == false) {
		//logout
		//Thread.sleep(100)
		//}
		//}

		//ISI USERNAME
		Mobile.tap(findTestObject('Object Repository/NKCRepo/Username'), 0)
		Mobile.setText(findTestObject('Object Repository/NKCRepo/Username'), strUsername, 0)
		keyNKC.KeywordNKC.CaptureScreen(strNo)

		//ISI PASSWORD
		Mobile.tap(findTestObject('Object Repository/NKCRepo/Password'), 0)
		Mobile.setText(findTestObject('Object Repository/NKCRepo/Password'), strPassword, 0)
		keyNKC.KeywordNKC.CaptureScreen(strNo)

		Mobile.tap(findTestObject('Object Repository/NKCRepo/btnLogin'), 0)//BUTTON LOGIN
		Mobile.delay(20)

	}

}

