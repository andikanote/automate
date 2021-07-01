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
//import java.math.BigInteger
import java.util.concurrent.TimeUnit
//import org.apache.commons.lang3.time.StopWatch

Date sekarang = new Date()

//CustomKeywords.'newIGO.cekProposalASL.compareASL'()
String tgl = sekarang.format('yyyy-MM-dd')
//String TujuanProp = strTujuanProposalMIP
GlobalVariable.fullFlow = true
GlobalVariable.strGlbDate = tgl
GlobalVariable.strGlbStatus = ''
GlobalVariable.strGlbKeterangan = ''
GlobalVariable.strGlbCrash = false
GlobalVariable.strGlbAlamat = false
GlobalVariable.strGlbNomor = 1
GlobalVariable.strGlbNumImage = 0
GlobalVariable.strGlbMenu = 'Login'


GlobalVariable.strGlbCaptureFolder = (((('C:\\OttoAutomation\\CAPTURE\\' + GlobalVariable.strGlbMenu) + '\\') 
	+ GlobalVariable.strGlbDate) + '\\')
println(GlobalVariable.strGlbCaptureFolder)

StopWatch runTime = StopWatch.createStarted()

getLastRow = findTestData('LOGINJAGA').getRowNumbers()

for (int excelRow : (4..getLastRow)) { //buat running semua data
//for (int excelRow : (1..1)) { //buat running data tertentu, ganti aja angkanya
	
	//mulai timer
	runTime.reset()
	runTime.start()
	
	//Nomor data
	TestData dataNKC 	= findTestData('LOGINJAGA')
	String strNo		= dataNKC.getValue('NO', excelRow)
	
	CustomKeywords.'keyNKC.JAGA2GO.Jaga2Go'(strNo, excelRow) //Panggil sub login di keywords
	CustomKeywords.'keyNKC.KeywordNKC.callStatus'(strNo, 'LOGINJAGA', 3) //REPORT DATA LOGIN
	
}
//Mobile.pressBack()