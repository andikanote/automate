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

public class JAGA2GO {
	@Keyword
	def Jaga2Go(String strNo, int excelRow){

		TestData dataJAGA 			= findTestData('LOGINJAGA')
		String strUsername			= dataJAGA.getValue('USERNAME', excelRow)
		String strPassword			= dataJAGA.getValue('PASSWORD', excelRow)
		GlobalVariable.strGlbMenu = 'Login'

		//Boolean cekObjekExist = Mobile.verifyElementExist(findTestObject('Object Repository/JAGA2GO/txtViewSelamatDatang'), 1, FailureHandling.OPTIONAL)

		//while (cekObjekExist == false) {
		//cekObjekExist = Mobile.verifyElementExist(findTestObject('Object Repository/JAGA2GO/txtViewSelamatDatang'), 2, FailureHandling.OPTIONAL)

		//if (cekObjekExist == false) {
		//logout
		//Thread.sleep(100)
		//}

		//SLIDER
		Mobile.tapAtPosition(835, 2050, FailureHandling.STOP_ON_FAILURE)
		keyNKC.KeywordNKC.CaptureScreen(strNo)
		Mobile.tapAtPosition(835, 2050, FailureHandling.STOP_ON_FAILURE)
		keyNKC.KeywordNKC.CaptureScreen(strNo)
		Mobile.tapAtPosition(835, 2050, FailureHandling.STOP_ON_FAILURE) //btnMasuk LoginPage
		keyNKC.KeywordNKC.CaptureScreen(strNo)
		Thread.sleep(1000)

		//USERNAME
		Mobile.tapAtPosition(200, 845, FailureHandling.STOP_ON_FAILURE)
		Mobile.tapAtPosition(800, 500, FailureHandling.STOP_ON_FAILURE)
		Thread.sleep(1000)
		Mobile.setText(findTestObject('Object Repository/JAGA2GO/txtUsername'), strUsername, 0)
		keyNKC.KeywordNKC.CaptureScreen(strNo)
		Thread.sleep(1000)


		//PASSWORD
		Mobile.tapAtPosition(200, 1050, FailureHandling.STOP_ON_FAILURE)
		Mobile.tapAtPosition(800, 500, FailureHandling.STOP_ON_FAILURE)
		Mobile.setText(findTestObject('Object Repository/JAGA2GO/txtPassword'), strPassword, 0)
		keyNKC.KeywordNKC.CaptureScreen(strNo)
		Thread.sleep(1000)

		//btnLogin
		Mobile.tap(findTestObject('Object Repository/JAGA2GO/btnLogin'), 0)
		Thread.sleep(1000)

		//VALIDASI LOGIN
		Boolean cekLoginExist = Mobile.verifyElementExist(findTestObject('Object Repository/JAGA2GO/txtViewSelamatDatang'), 1, FailureHandling.OPTIONAL)

		if (GlobalVariable.strGlbMenu == 'Login') {
			if (cekLoginExist == true) {
				GlobalVariable.strGlbStatus = 'PASSED'
				GlobalVariable.strGlbKeterangan = 'LOGIN BERHASIL'
			} else {
				GlobalVariable.strGlbStatus = 'FAILED'
				GlobalVariable.strGlbKeterangan = 'LOGIN GAGAL'
			}
		}

		Mobile.tapAtPosition(100, 200, FailureHandling.STOP_ON_FAILURE)

	}
}


