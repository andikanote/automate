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

public class KeywordNKC {

	@Keyword
	def static CaptureScreen(String strNo) {
		//CaptureImage
		String strGlbImage = ''
		int intAwal
		GlobalVariable.strGlbNumImage =	(GlobalVariable.strGlbNumImage + 1)
		strGlbImage = ('000' + GlobalVariable.strGlbNumImage)
		intAwal = strGlbImage.length() - 3
		strGlbImage = strGlbImage.substring(intAwal)
		Mobile.takeScreenshot(GlobalVariable.strGlbCaptureFolder + 'Capture_' + strNo + '_' + strGlbImage + '.jpg', FailureHandling.STOP_ON_FAILURE)
	}

	@Keyword
	def static SwipeDownIfObjectNotOnScreen(String objectLocator){
		GlobalVariable.strGlbCekBoolean = Mobile.verifyElementExist(findTestObject(objectLocator), 1, FailureHandling.OPTIONAL)
		while (GlobalVariable.strGlbCekBoolean == false) {
			Mobile.swipe(500, 1000, 500, 500)
			GlobalVariable.strGlbCekBoolean = Mobile.verifyElementExist(findTestObject(objectLocator), 1, FailureHandling.OPTIONAL)
		}
	}

	@Keyword
	def Login(String Agent, String Password, String No) {

		//Tap Masuk
		Mobile.tap(findTestObject('PAGE LOGIN/btnMasuk'), 0)
		Thread.sleep(1000)

		//Validasi Halaman Login
		Boolean cekLogin = Mobile.verifyElementExist(findTestObject('PAGE LOGIN/txtKodeAgen'), 1, FailureHandling.OPTIONAL)
		//Boolean cekLeads = Mobile.verifyElementExist(findTestObject('IGORevamp/android.widget.ImageView0'), 1, FailureHandling.OPTIONAL)
		if (GlobalVariable.strGlbMenu == 'Login') {
			if (cekLogin == false) {
				keywordAFIONE.cekLogout()
			}
		}

		//Kode Agen
		Mobile.sendKeys(findTestObject('PAGE LOGIN/txtKodeAgen'), Agent)

		//Password
		Mobile.sendKeys(findTestObject('PAGE LOGIN/txtKataSandi'), Password)

		//Capture
		keywordAFIONE.CaptureScreen(No)

		//Tap Masuk/Login
		Mobile.tap(findTestObject('PAGE LOGIN/btnLogin'), 0)

		Thread.sleep(3000)

		//Validasi Login
		cekLogin = Mobile.verifyElementExist(findTestObject('PAGE LOGIN/txtKodeAgen'), 1, FailureHandling.OPTIONAL)

		if (GlobalVariable.strGlbMenu == 'Login') {
			if (cekLogin == true) {
				GlobalVariable.strGlbStatus = 'FAILED'
			} else {
				GlobalVariable.strGlbStatus = 'PASSED'
			}
		}
	}


	@Keyword
	def static cekLogout() {

		Boolean cekLogin = Mobile.verifyElementExist(findTestObject('PAGE LOGIN/btnMasuk'), 1, FailureHandling.OPTIONAL)

		if (cekLogin == false) {
			Mobile.hideKeyboard(FailureHandling.OPTIONAL)
			Mobile.tap(findTestObject('PAGE HOME/btnAkun'), 1)
			Mobile.tap(findTestObject('PAGE AKUN/btnKeluar'), 1)
			Mobile.tap(findTestObject('PAGE AKUN/btnConfirmLogout'), 5)
			Mobile.verifyElementExist(findTestObject('PAGE LOGIN/txtKodeAgen'), 7)
		}
	}

	//CALL STATUS PASSED OR FAILED
	@Keyword
	def callStatus(String strNo, String namaMenu, int kolomAwal) {
		FileInputStream file = new FileInputStream (new File('C:\\OttoAutomation\\data\\master_data_jaga.xlsx'))
		XSSFWorkbook workbook = new XSSFWorkbook(file)
		XSSFSheet sheet = workbook.getSheet(namaMenu)
		int strGetRow = Integer.parseInt(strNo)

		sheet.getRow(strGetRow).createCell(kolomAwal+1).setCellValue(GlobalVariable.strGlbStatus)
		sheet.getRow(strGetRow).createCell(6).setCellValue(GlobalVariable.strGlbKeterangan)

		String ExpectedResult	= sheet.getRow(strGetRow).getCell(kolomAwal).getStringCellValue();
		String ActualResult 	= sheet.getRow(strGetRow).getCell(kolomAwal+1).getStringCellValue();

		if (ExpectedResult == ActualResult) {
			sheet.getRow(strGetRow).createCell(kolomAwal+2).setCellValue('PASSED')
		} else {
			sheet.getRow(strGetRow).createCell(kolomAwal+2).setCellValue('FAILED')
		}

		file.close()

		FileOutputStream outFile =new FileOutputStream(new File('C:\\OttoAutomation\\data\\master_data_jaga.xlsx'))
		workbook.write(outFile)
		outFile.close()
	}

	@Keyword
	def GetDate() {
		//CaptureImage
		Date today = new Date()
		GlobalVariable.strGlbDate = today.format('yyyy-MM-dd')
	}

	@Keyword
	def GetBulan(String blnNow, String blnSkr){
		if (blnNow.length() == 3) {
			if (blnNow == 'Jan') {
				GlobalVariable.intBulan = 1
			} else if (blnNow == 'Feb') {
				GlobalVariable.intBulan = 2
			} else if (blnNow == 'Mar') {
				GlobalVariable.intBulan = 3
			} else if (blnNow == 'Apr') {
				GlobalVariable.intBulan = 4
			} else if (blnNow == 'May') {
				GlobalVariable.intBulan = 5
			} else if (blnNow == 'Jun') {
				GlobalVariable.intBulan = 6
			} else if (blnNow == 'Jul') {
				GlobalVariable.intBulan = 7
			} else if (blnNow == 'Aug') {
				GlobalVariable.intBulan = 8
			} else if (blnNow == 'Sep') {
				GlobalVariable.intBulan = 9
			} else if (blnNow == 'Oct') {
				GlobalVariable.intBulan = 10
			} else if (blnNow == 'Nov') {
				GlobalVariable.intBulan = 11
			} else if (blnNow == 'Dec') {
				GlobalVariable.intBulan = 12
			}
		} else if (blnSkr.length() == 3) {
			if (blnSkr == 'Jan') {
				GlobalVariable.intBulan = 1
			} else if (blnSkr == 'Feb') {
				GlobalVariable.intBulan = 2
			} else if (blnSkr == 'Mar') {
				GlobalVariable.intBulan = 3
			} else if (blnSkr == 'Apr') {
				GlobalVariable.intBulan = 4
			} else if (blnSkr == 'Mei') {
				GlobalVariable.intBulan = 5
			} else if (blnSkr == 'Jun') {
				GlobalVariable.intBulan = 6
			} else if (blnSkr == 'Jul') {
				GlobalVariable.intBulan = 7
			} else if (blnSkr == 'Agt') {
				GlobalVariable.intBulan = 8
			} else if (blnSkr == 'Sep') {
				GlobalVariable.intBulan = 9
			} else if (blnSkr == 'Okt') {
				GlobalVariable.intBulan = 10
			} else if (blnSkr == 'Nov') {
				GlobalVariable.intBulan = 11
			} else if (blnSkr == 'Des') {
				GlobalVariable.intBulan = 12
			}
		}
	}

	@Keyword
	def static GetOldTanggal(String strTanggal, Boolean teksCepet) {
		String Tanggal = new String()
		String Bulan = new String()
		String Tahun = new String()
		String Usia = new String()
		int intBulan
		int intTahun
		int intTahunSkr

		Date today = new Date()
		String dateNow = today.format('dd/MM/yyyy')
		String tanggalNow = strTanggal.substring(0, 2)
		tanggalNow = Integer.parseInt(tanggalNow)
		String bulanNow = strTanggal.substring(3, 5)
		bulanNow = Integer.parseInt(bulanNow)
		String tahunNow = strTanggal.substring(6, 10)
		tahunNow = Integer.parseInt(tahunNow)
		Tanggal = strTanggal.substring(0, 2)
		Tanggal = Integer.parseInt(Tanggal)
		Tanggal = Tanggal.toString()
		Bulan = strTanggal.substring(3, 5)

		int angkaBulan = Integer.parseInt(Bulan)
		Tahun = strTanggal.substring(6, 10)
		//Tahun = Integer.parseInt(Tahun)
		String tahunSkr = Mobile.getText(findTestObject('PAGE REGISTRASI/btnCalendarTahun'), 1, FailureHandling.OPTIONAL)
		intTahun = Integer.parseInt(Tahun)
		intTahunSkr = Integer.parseInt(tahunSkr)

		Boolean minTahun1 = Mobile.verifyGreaterThan(intTahunSkr, intTahun, FailureHandling.OPTIONAL)
		println(minTahun1)

		Boolean minTahun2 = Mobile.verifyGreaterThan(intTahun, intTahunSkr, FailureHandling.OPTIONAL)
		println(minTahun2)

		if (intTahun == intTahunSkr) {
			Thread.sleep(100)
		} else {
			Mobile.tap(findTestObject('PAGE REGISTRASI/btnCalendarTahun'), 1)
			TestObject teksTahun = new TestObject('newObjectTahun1')
			println(teksTahun)
			teksTahun.addProperty('text', ConditionType.EQUALS, Tahun)
			println(teksTahun)
			Boolean cekTahun = Mobile.verifyElementExist(teksTahun, 1, FailureHandling.OPTIONAL)
			println(cekTahun)

			//tambahan untuk mempercepat
			//			if (teksCepet == true){
			//				for (def index : (1..7)) {
			//					Mobile.swipe(1000, 500, 1000, 700)
			//				}
			//			}
			//==========================
			if (cekTahun == false) {
				if (minTahun1 == true) {
					while (cekTahun == false) {
						int angkaIndex = 0

						if (intTahun >= intTahunSkr-8 && intTahun <= intTahunSkr) {
							angkaIndex = 1
						} else if (intTahun >= intTahunSkr-12){
							angkaIndex = 2
						} else if (intTahun >= intTahunSkr-16){
							angkaIndex = 3
						} else if (intTahun >= intTahunSkr-20){
							angkaIndex = 4
						} else if (intTahun >= intTahunSkr-24){
							angkaIndex = 5
						} else if (intTahun >= intTahunSkr-28){
							angkaIndex = 6
						} else if (intTahun >= intTahunSkr-32){
							angkaIndex = 7
						} else if (intTahun >= intTahunSkr-36){
							angkaIndex = 8
						} else if (intTahun >= intTahunSkr-40){
							angkaIndex = 9
						} else if (intTahun >= intTahunSkr-44){
							angkaIndex = 10
						} else if (intTahun >= intTahunSkr-48){
							angkaIndex = 11
						} else if (intTahun >= intTahunSkr-52){
							angkaIndex = 12
						} else if (intTahun >= intTahunSkr-56){
							angkaIndex = 13
						} else if (intTahun >= intTahunSkr-60){
							angkaIndex = 14
						} else if (intTahun >= intTahunSkr-64){
							angkaIndex = 15
						} else if (intTahun >= intTahunSkr-68){
							angkaIndex = 16
						} else if (intTahun >= intTahunSkr-72){
							angkaIndex = 17
						} else if (intTahun >= intTahunSkr-76){
							angkaIndex = 18
						} else if (intTahun >= intTahunSkr-80){
							angkaIndex = 19
						} else {
							angkaIndex = 20
						}

						for (def index : (1..angkaIndex)) {
							Mobile.swipe(500, 1000, 500, 1300)
							//cari koordinat swipe tahun mengecil
						}

						cekTahun = Mobile.verifyElementExist(teksTahun, 1, FailureHandling.OPTIONAL)

						//EAppsIgo.CaptureScreen('Tes')
						if (cekTahun == false) {
							while (cekTahun == false) {
								Mobile.swipe(500, 1000, 500, 1300)
								//cari koordinat swipe tahun mengecil
								cekTahun = Mobile.verifyElementExist(teksTahun, 1, FailureHandling.OPTIONAL)
							}
						}

						Mobile.tap(teksTahun, 1)
					}
				} else if (minTahun2 == true) {
					while (cekTahun == false) {
						Mobile.swipe(500, 1000, 500, 700)
						//cari koordinat swipe tahun membesar

						cekTahun = Mobile.verifyElementExist(teksTahun, 1, FailureHandling.OPTIONAL)

						if (cekTahun == true) {
							Mobile.tap(teksTahun, 1)
						}
					}
				}
			} else {
				Mobile.tap(teksTahun, 1)
			}
		}

		//===============
		String cekBulan = Mobile.getText(findTestObject('PAGE REGISTRASI/lblTglSkrg'), 1, FailureHandling.OPTIONAL)
		println(cekBulan)
		String[] bulanSkr = cekBulan.split(' ')
		String blnNow = bulanSkr[1]
		String blnSkr = bulanSkr[2]
		println(blnNow)
		println(blnSkr)

		if (blnNow.length() == 3) {
			if (blnNow == 'Jan') {
				intBulan = 1
			} else if (blnNow == 'Feb') {
				intBulan = 2
			} else if (blnNow == 'Mar') {
				intBulan = 3
			} else if (blnNow == 'Apr') {
				intBulan = 4
			} else if (blnNow == 'May') {
				intBulan = 5
			} else if (blnNow == 'Jun') {
				intBulan = 6
			} else if (blnNow == 'Jul') {
				intBulan = 7
			} else if (blnNow == 'Aug') {
				intBulan = 8
			} else if (blnNow == 'Sep') {
				intBulan = 9
			} else if (blnNow == 'Oct') {
				intBulan = 10
			} else if (blnNow == 'Nov') {
				intBulan = 11
			} else if (blnNow == 'Dec') {
				intBulan = 12
			}
		} else if (blnSkr.length() == 3) {
			if (blnSkr == 'Jan') {
				intBulan = 1
			} else if (blnSkr == 'Feb') {
				intBulan = 2
			} else if (blnSkr == 'Mar') {
				intBulan = 3
			} else if (blnSkr == 'Apr') {
				intBulan = 4
			} else if (blnSkr == 'Mei') {
				intBulan = 5
			} else if (blnSkr == 'Jun') {
				intBulan = 6
			} else if (blnSkr == 'Jul') {
				intBulan = 7
			} else if (blnSkr == 'Agt') {
				intBulan = 8
			} else if (blnSkr == 'Sep') {
				intBulan = 9
			} else if (blnSkr == 'Okt') {
				intBulan = 10
			} else if (blnSkr == 'Nov') {
				intBulan = 11
			} else if (blnSkr == 'Des') {
				intBulan = 12
			}
		}

		println(intBulan)
		println(angkaBulan)
		Boolean minBulan1 = Mobile.verifyGreaterThan(angkaBulan, intBulan, FailureHandling.OPTIONAL)
		println(minBulan1)
		Boolean minBulan2 = Mobile.verifyGreaterThan(intBulan, angkaBulan, FailureHandling.OPTIONAL)
		println(minBulan2)

		if (minBulan1 == true) {
			int minBulan = (angkaBulan - intBulan)

			println(minBulan)

			for (def index : (1..minBulan)) {
				Mobile.swipe(725, 1000, 500, 1000)
				//findTestObject('PAGE REGISTRASI/btnNextMonth')
			}
		} else if (minBulan2 == true) {
			int minBulan = (intBulan - angkaBulan)

			println(minBulan)

			for (def index : (1..minBulan)) {
				Mobile.swipe(500, 1000, 725, 1000)
				//findTestObject('PAGE REGISTRASI/btnPrevMonth')
			}
		}

		TestObject teksTgl = new TestObject('newObjectTanggal')
		//teksTgl.addProperty('text', ConditionType.EQUALS, Tanggal)
		WebUI.modifyObjectProperty(teksTgl, 'xpath', 'equals', "//*[contains(name(), 'View')][" + Tanggal + "]", true)
		Mobile.tap(teksTgl, 1)
		Thread.sleep(1000)
		Mobile.tap(findTestObject('PAGE REGISTRASI/btnCalendarOK'), 1)
	}

	//===========================================PROPOSAL====================================================
	@Keyword
	def cekProposal(String strNo, String strTujuanProposal, String strNama, String strTanggal, String strKelamin, String strKelasPekerjaan, String strJenisPekerjaan,
			String strNamaTTG, String strTanggalTTG, String strKelaminTTG, String strHubunganTTG, String strKelasPekerjaanTTG, String strJenisPekerjaanTTG) {

		Boolean teksCepet = false
		String tahunNowTtg
		int intTahunSkrTtg
		int bedaTahunTtg
		int intTahunSkr
		int bedaTahun
		Date today 			= new Date()
		String dateNow 		= today.format('yyyy')
		int intTahun 		= Integer.parseInt(dateNow)
		if (strTanggal != ''){
			String tahunNow = strTanggal.substring(6, 10)
			intTahunSkr		= Integer.parseInt(tahunNow)
			bedaTahun 		= intTahun - intTahunSkr
		}
		String validasiNama		  = ''
		String validasiTanggal	  = ''
		String validasiKelamin	  = ''
		String validasiNamaTTG	  = ''
		String validasiTanggalTTG = ''
		String validasiKelaminTTG = ''
		String validasiHubungan	  = ''

		if (strTujuanProposal == 'Anda Sayangi') {
			if (strTanggalTTG != ''){
				tahunNowTtg  = strTanggalTTG.substring(6, 10)
				intTahunSkrTtg  = Integer.parseInt(tahunNowTtg)
				bedaTahunTtg	= intTahun - intTahunSkrTtg
			}
		}

		if (GlobalVariable.fullFlow == true){
			keywordIGO.CaptureScreen(strNo)
		}

		if (strTujuanProposal == 'Anda') {
			Mobile.tap(findTestObject('IGORevamp/btnAnda'), 0)
		} else {
			Mobile.tap(findTestObject('IGORevamp/btnAndaSayangi'), 0)
		}

		String cekUsia = Mobile.getText(findTestObject('IGORevamp/txtMOCUsia'), 0)

		if (cekUsia == '') {
			//Mobile.tap(findTestObject('IGORevamp/txtPemegangPolis'), 0)
			Mobile.sendKeys(findTestObject('IGORevamp/txtPemegangPolis'), strNama, FailureHandling.OPTIONAL)
			//Mobile.hideKeyboard(FailureHandling.OPTIONAL)
			if (strTanggal != '') {
				if (bedaTahun >= 18 && bedaTahun <= 80){
					Mobile.tap(findTestObject('IGORevamp/android.widget.Button2'), 0) //btn pilih tanggal
					keywordIGO.GetOldTanggal(strTanggal, teksCepet)
				}
			}
			if (strKelamin != '') {
				Mobile.tap(findTestObject('IGORevamp/spinnerKelamin'), 0, FailureHandling.OPTIONAL)
				TestObject teksKlm = new TestObject('newObjectKlm')
				teksKlm.addProperty('text', ConditionType.EQUALS, strKelamin)
				Mobile.tap(teksKlm, 1, FailureHandling.OPTIONAL)
			}
		}

		//		=====================UNTUK GET TEXT BUAT VALIDASI=========================
		//		TestObject teksTglLhr = new TestObject('newObjectLhr')
		//		teksTglLhr.addProperty('xpath', ConditionType.EQUALS, '//hierarchy/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.ScrollView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[2]/android.widget.RelativeLayout[1]/android.widget.EditText[1]')
		//
		//		TestObject teksKlmin = new TestObject('newObjectKlmin')
		//		teksKlmin.addProperty('xpath', ConditionType.EQUALS, '//hierarchy/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.ScrollView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[3]/android.widget.Spinner[1]/android.widget.TextView[1]')
		//
		//		String getNama 	  = Mobile.getText(findTestObject('IGORevamp/txtPemegangPolis'), 0)
		//		String getTgl	  = Mobile.getText(teksTglLhr, 0)
		//		String getKelamin = Mobile.getText(teksKlmin, 0)
		//		String a = GlobalVariable.strGlbCekLeadsNama
		//		String b = GlobalVariable.strGlbCekLeadsTanggal
		//		String c = GlobalVariable.strGlbCekLeadsKelamin
		//
		//		if (getNama == GlobalVariable.strGlbCekLeadsNama){
		//			validasiNama = 'PASSED'
		//		} else if (getNama != GlobalVariable.strGlbCekLeadsNama){
		//			validasiNama = 'FAILED'
		//		}
		//
		//		if (getTgl == GlobalVariable.strGlbCekLeadsTanggal){
		//			validasiTanggal = 'PASSED'
		//		} else if (getTgl != GlobalVariable.strGlbCekLeadsTanggal){
		//			validasiTanggal = 'FAILED'
		//		}
		//
		//		if (getKelamin == GlobalVariable.strGlbCekLeadsKelamin){
		//			validasiKelamin = 'PASSED'
		//		} else if (getKelamin != GlobalVariable.strGlbCekLeadsKelamin){
		//			validasiKelamin = 'FAILED'
		//		}

		//		else {
		//			Mobile.getText(findTestObject('IGORevamp/txtPemegangPolis'), 0)
		//
		//			Mobile.getText(findTestObject('IGORevamp/spinnerKelamin'), 0)
		//		}
		//if (strTujuanProposal == 'Anda') {
		//String cekKerjaan = Mobile.getText(findTestObject('IGORevamp/txtMOCTeksKerja'), 0)

		//======================DIUBAH LAGI BOSS==========================
		//if (cekKerjaan == 'Silahkan pilih') {
		//ini jadi field biasa, bukan spinner
		//trus disini input pekerjaan di field search pekerjaan
		//kalo cocok, klik pekerjaannya

		//		Mobile.tap(findTestObject('IGORevamp/txtNewKelasPekerjaan'), 0, FailureHandling.OPTIONAL)
		//		Thread.sleep(200)
		//		Mobile.sendKeys(findTestObject('IGORevamp/txtSearchPekerjaan'), strKelasPekerjaan, FailureHandling.OPTIONAL)
		//		Mobile.pressBack()
		//		TestObject teksKerjaan = new TestObject('newObjectKerja')
		//		teksKerjaan.addProperty('text', ConditionType.EQUALS, strKelasPekerjaan)
		//		teksKerjaan.addProperty('class', ConditionType.EQUALS, 'android.widget.TextView')
		//
		//		//		Boolean cekKerja = Mobile.verifyElementExist(teksKerjaan, 1, FailureHandling.OPTIONAL)
		//		//		if (cekKerja == false) {
		//		//			while (cekKerja == false){
		//		//				Mobile.swipe(1500, 900, 1500, 500)
		//		//				cekKerja = Mobile.verifyElementExist(teksKerjaan, 1, FailureHandling.OPTIONAL)
		//		//			}
		//		//		}
		//		Mobile.tap(teksKerjaan, 0, FailureHandling.OPTIONAL)

		if (strJenisPekerjaan != ''){
			Mobile.tap(findTestObject('IGORevamp/txtNewJenisPekerjaan'), 0, FailureHandling.OPTIONAL)
			Thread.sleep(200)
			Mobile.sendKeys(findTestObject('IGORevamp/txtSearchPekerjaan'), strJenisPekerjaan, FailureHandling.OPTIONAL)
			Mobile.pressBack()
			TestObject teksKerjaan2 = new TestObject('newObjectKerja2')
			teksKerjaan2.addProperty('text', ConditionType.EQUALS, strJenisPekerjaan)
			teksKerjaan2.addProperty('class', ConditionType.EQUALS, 'android.widget.TextView')
			//		cekKerja = Mobile.verifyElementExist(teksKerjaan2, 1, FailureHandling.OPTIONAL)
			//		if (cekKerja == false) {
			//			while (cekKerja == false){
			//				Mobile.swipe(1500, 900, 1500, 500)
			//				cekKerja = Mobile.verifyElementExist(teksKerjaan2, 1, FailureHandling.OPTIONAL)
			//			}
			//		}
			Mobile.tap(teksKerjaan2, 0, FailureHandling.OPTIONAL)
		}
		//===============================================================

		if (strTujuanProposal == 'Anda Sayangi') {
			String cekUsiaTtg = Mobile.getText(findTestObject('IGORevamp/txtUsiaTertanggung'), 0)

			String ah = strNamaTTG

			if (cekUsiaTtg == '') {
				//Mobile.tap(findTestObject('IGORevamp/txtNamaTtg'), 0)
				Mobile.sendKeys(findTestObject('IGORevamp/txtNamaTtg'), strNamaTTG)
				//Mobile.hideKeyboard(FailureHandling.OPTIONAL)
				if (strTanggalTTG != ''){
					if (bedaTahunTtg >= 0 && bedaTahunTtg <= 80){
						Mobile.tap(findTestObject('IGORevamp/btnTanggalTtg'), 0) //btn pilih tanggal
						teksCepet = true
						keywordIGO.GetOldTanggal(strTanggalTTG, teksCepet)
					}
				}
				if (strKelaminTTG != '') {
					Mobile.tap(findTestObject('IGORevamp/txtKelaminTtg'), 0)
					if (strKelaminTTG == 'Laki-Laki') {
						Mobile.tap(findTestObject('IGORevamp/android.widget.TextView1 - Laki-Laki'), 0)
					} else {
						Mobile.tap(findTestObject('IGORevamp/android.widget.TextView2 - Perempuan'), 0)
					}
				}

				if (strHubunganTTG != ''){
					Mobile.tap(findTestObject('IGORevamp/txtHubunganTtg'), 0)
					TestObject teksHub = new TestObject('newObjectHub')
					teksHub.addProperty('text', ConditionType.EQUALS, strHubunganTTG)
					Boolean cekHub = Mobile.verifyElementExist(teksHub, 1, FailureHandling.OPTIONAL)
					//	if (cekHub == false) {
					//	Mobile.swipe(1200, 1000, 1200, 700)
					//	}
					Mobile.tap(teksHub, 1)
				}

				//			Mobile.tap(findTestObject('IGORevamp/txtNewKelasPekerjaanTtg'), 0, FailureHandling.OPTIONAL)
				//			Thread.sleep(200)
				//			Mobile.sendKeys(findTestObject('IGORevamp/txtSearchPekerjaan'), strKelasPekerjaanTTG, FailureHandling.OPTIONAL)
				//			Mobile.pressBack()
				//			TestObject teksKerjaan3 = new TestObject('newObjectKerja3')
				//			teksKerjaan3.addProperty('text', ConditionType.EQUALS, strKelasPekerjaanTTG)
				//			teksKerjaan3.addProperty('class', ConditionType.EQUALS, 'android.widget.TextView')
				//			//			Boolean cekKerja2 = Mobile.verifyElementExist(teksKerjaan3, 1, FailureHandling.OPTIONAL)
				//			//			if (cekKerja2 == false) {
				//			//				while (cekKerja2 == false) {
				//			//					Mobile.swipe(1500, 1000, 1500, 700)
				//			//					cekKerja2 = Mobile.verifyElementExist(teksKerjaan3, 1, FailureHandling.OPTIONAL)
				//			//				}
				//			//			}
				//			Mobile.tap(teksKerjaan3, 0)
			}

			if (strJenisPekerjaanTTG != ''){
				Mobile.tap(findTestObject('IGORevamp/txtNewJenisPekerjaanTtg'), 0, FailureHandling.OPTIONAL)
				Thread.sleep(200)
				Boolean cekCari = Mobile.verifyElementExist(findTestObject('IGORevamp/txtSearchPekerjaan'), 1, FailureHandling.OPTIONAL)
				if (cekCari == true){
					Mobile.sendKeys(findTestObject('IGORevamp/txtSearchPekerjaan'), strJenisPekerjaanTTG, FailureHandling.OPTIONAL)
					Mobile.pressBack()
					TestObject teksKerjaan4 = new TestObject('newObjectKerja4')
					teksKerjaan4.addProperty('text', ConditionType.EQUALS, strJenisPekerjaanTTG)
					teksKerjaan4.addProperty('class', ConditionType.EQUALS, 'android.widget.TextView')
					//			cekKerja2 = Mobile.verifyElementExist(teksKerjaan4, 1, FailureHandling.OPTIONAL)
					//			if (cekKerja2 == false) {
					//				while (cekKerja2 == false) {
					//					Mobile.swipe(1500, 1000, 1500, 700)
					//					cekKerja2 = Mobile.verifyElementExist(teksKerjaan4, 1, FailureHandling.OPTIONAL)
					//				}
					//			}
					Mobile.tap(teksKerjaan4, 0)
				}
			}

			//=========================================KHUSUS 'ANDA SAYANGI'=================================
			//			TestObject teksTglLhr1 = new TestObject('newObjectLhr1')
			//			teksTglLhr1.addProperty('xpath', ConditionType.EQUALS, '//hierarchy/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.ScrollView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[2]/android.widget.RelativeLayout[1]/android.widget.EditText[1]')
			//
			//			TestObject teksKlmin1  = new TestObject('newObjectKlmin1')
			//			teksKlmin1.addProperty('xpath', ConditionType.EQUALS, '//hierarchy/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.ScrollView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[3]/android.widget.Spinner[1]/android.widget.TextView[1]')
			//
			//			TestObject teksHub  = new TestObject('newObjectHub')
			//			teksHub.addProperty('xpath', ConditionType.EQUALS, '//hierarchy/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.ScrollView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.LinearLayout[2]/android.widget.RelativeLayout[1]/android.widget.Spinner[1]/android.widget.TextView[1]')
			//
			//			String getNama1	   = Mobile.getText(findTestObject('IGORevamp/txtNamaTtg'), 0)
			//			String getTgl1	   = Mobile.getText(teksTglLhr, 0)
			//			String getKelamin1 = Mobile.getText(teksKlmin, 0)
			//			String getHubungan = Mobile.getText(teksHub, 0)
			////		String d = GlobalVariable.strGlbCekLeadsNama
			////		String e = GlobalVariable.strGlbCekLeadsTanggal
			////		String f = GlobalVariable.strGlbCekLeadsKelamin
			//
			//			if (getNama1 == GlobalVariable.strGlbCekLeadsNamaTTG){
			//				validasiNamaTTG = 'PASSED'
			//			} else if (getNama1 != GlobalVariable.strGlbCekLeadsNamaTTG){
			//				validasiNamaTTG = 'FAILED'
			//			}
			//
			//			if (getTgl1 == GlobalVariable.strGlbCekLeadsTanggalTTG){
			//				validasiTanggalTTG = 'PASSED'
			//			} else if (getTgl1 != GlobalVariable.strGlbCekLeadsTanggalTTG){
			//				validasiTanggalTTG = 'FAILED'
			//			}
			//
			//			if (getKelamin1 == GlobalVariable.strGlbCekLeadsKelaminTTG){
			//				validasiKelaminTTG = 'PASSED'
			//			} else if (getKelamin1 != GlobalVariable.strGlbCekLeadsKelaminTTG){
			//				validasiKelaminTTG = 'FAILED'
			//			}
			//
			//			if (getHubungan == GlobalVariable.strGlbCekLeadsHubungan){
			//				validasiHubungan = 'PASSED'
			//			} else if (getHubungan != GlobalVariable.strGlbCekLeadsHubungan){
			//				validasiHubungan = 'FAILED'
			//			}
		}

		//	if (cekProses == true) {
		//		keywordIGO.CaptureScreen(strNo)
		//	} else {
		//		Thread.sleep(2000)
		//	}

		if (GlobalVariable.fullFlow == true){
			keywordIGO.CaptureScreen(strNo)
		}

		//		keywordIGO.tulisExcelInfo(strNo, validasiNama, validasiTanggal, validasiKelaminTTG, validasiNamaTTG, validasiTanggalTTG, validasiKelaminTTG, validasiHubungan)

		Mobile.tap(findTestObject('IGORevamp/btnPilihSolusi'), 1)

	}

	@Keyword
	def static tulisExcelInfo(String strNo, String validasiNama, String validasiTanggal, String validasiKelamin, String validasiNamaTTG, String validasiTanggalTTG, String validasiKelaminTTG, String validasiHubungan) {
		FileInputStream file = new FileInputStream (new File("C:\\Project\\IGORevamp\\Datatable\\IGORevamp.xlsx"))
		XSSFWorkbook workbook = new XSSFWorkbook(file)
		XSSFSheet sheet = workbook.getSheet('INFO')
		int intGetRow = Integer.parseInt(strNo)
		int intGetRow1 = intGetRow + 1

		sheet.getRow(intGetRow1).createCell(0).setCellValue(strNo)
		sheet.getRow(intGetRow1).createCell(3).setCellValue(validasiNama)
		sheet.getRow(intGetRow1).createCell(4).setCellValue(validasiTanggal)
		sheet.getRow(intGetRow1).createCell(5).setCellValue(validasiKelamin)
		sheet.getRow(intGetRow1).createCell(6).setCellValue(validasiNamaTTG)
		sheet.getRow(intGetRow1).createCell(7).setCellValue(validasiTanggalTTG)
		sheet.getRow(intGetRow1).createCell(8).setCellValue(validasiKelaminTTG)
		sheet.getRow(intGetRow1).createCell(9).setCellValue(validasiHubungan)

		file.close()

		FileOutputStream outFile =new FileOutputStream(new File("C:\\Project\\IGORevamp\\Datatable\\IGORevamp.xlsx"))
		workbook.write(outFile)
		outFile.close()
	}

	@Keyword
	def EappsCekAlamat(String strNo) {
		String strProvinsiTadi
		String strKotaTadi
		String strKecamatanTadi
		String strKelurahanTadi
		Boolean errKecamatan
		Boolean strProvinsiCek
		Boolean strKotaCek
		Boolean strKecamatanCek
		Boolean strKelurahanCek
		int intAngka = 1
		int cekAngka

		Thread.sleep(3000)

		Mobile.swipe(1000, 800, 1000, 200)

		Mobile.tap(findTestObject('IGORevamp/EApps/lblEAInformasiAlamat'), 1)

		Mobile.swipe(1000, 800, 1000, 200)

		for (int excelRow : (1..4371)) {
			cekAngka = 1
			String strStatus = 'PASSED'
			String strKeterangan = ' - '

			TestData dataLeads = findTestData('CekAlamat')
			String strProvinsi	= dataLeads.getValue('PROVINSI', excelRow)
			String strKota		= dataLeads.getValue('KOTA', excelRow)
			String strKecamatan	= dataLeads.getValue('KECAMATAN', excelRow)
			String strKelurahan	= dataLeads.getValue('KELURAHAN', excelRow)

			if (strProvinsi != strProvinsiTadi){
				strProvinsi = strProvinsi.toUpperCase()
				Mobile.tap(findTestObject('IGORevamp/EApps/spnEAProvinsi'), 1)
				TestObject teksProv = new TestObject('newObjectProv')
				teksProv.addProperty('text', ConditionType.EQUALS, strProvinsi)

				strProvinsiCek = Mobile.verifyElementExist(teksProv, 2, FailureHandling.OPTIONAL)

				if (strProvinsiCek == false){
					while (cekAngka != 3 & strProvinsiCek == false){
						Mobile.swipe(1000, 700, 1000, 350)

						strProvinsiCek = Mobile.verifyElementExist(teksProv, 1, FailureHandling.OPTIONAL)

						if (strProvinsiCek == false){
							cekAngka = cekAngka + 1
						}
					}
				}

				if (strProvinsiCek == false){
					intAngka = intAngka + 1
					strProvinsiTadi  = strProvinsi
					strKotaTadi		 = strKota
					strKecamatanTadi = strKecamatan
					strKelurahanTadi = strKelurahan
					strStatus = 'FAILED'
					strKeterangan = 'Nama Provinsi tidak ada'
					keywordIGO.tulisCekAlamat(excelRow, strStatus, strKeterangan)
					Mobile.pressBack()
					continue
				} else {
					Mobile.tap(teksProv, 1)
				}
			}

			if (strKota != strKotaTadi){
				strKota = strKota.toUpperCase()
				Mobile.tap(findTestObject('IGORevamp/EApps/spnEAKota'), 1)
				TestObject teksKota = new TestObject('newObjectKota')
				teksKota.addProperty('text', ConditionType.EQUALS, strKota)

				strKotaCek = Mobile.verifyElementExist(teksKota, 2, FailureHandling.OPTIONAL)

				if (strKotaCek == false){
					while (cekAngka != 3 & strKotaCek == false){
						Mobile.swipe(1000, 600, 1000, 250)

						strKotaCek = Mobile.verifyElementExist(teksKota, 1, FailureHandling.OPTIONAL)

						if (strKotaCek == false){
							cekAngka = cekAngka + 1
						}
					}
				}

				if (strKotaCek == false){
					intAngka = intAngka + 1
					strProvinsiTadi  = strProvinsi
					strKotaTadi		 = strKota
					strKecamatanTadi = strKecamatan
					strKelurahanTadi = strKelurahan
					strStatus = 'FAILED'
					strKeterangan = 'Nama Kota tidak ada'
					keywordIGO.tulisCekAlamat(excelRow, strStatus, strKeterangan)
					Mobile.pressBack()
					continue
				} else {
					Mobile.tap(teksKota, 1)
				}
			}

			if (strKecamatan == strKecamatanTadi & errKecamatan == true){
				intAngka = intAngka + 1
				strProvinsiTadi  = strProvinsi
				strKotaTadi		 = strKota
				strKecamatanTadi = strKecamatan
				strKelurahanTadi = strKelurahan
				strStatus = 'FAILED'
				strKeterangan = 'Nama Kecamatan tidak ada'
				keywordIGO.tulisCekAlamat(excelRow, strStatus, strKeterangan)
				continue
			}

			if (strKecamatan != strKecamatanTadi){
				errKecamatan = false
				strKecamatan = strKecamatan.toUpperCase()
				Mobile.tap(findTestObject('IGORevamp/EApps/spnEAKecamatan'), 1)
				TestObject teksKecamatan = new TestObject('newObjectKecamatan')
				teksKecamatan.addProperty('text', ConditionType.EQUALS, strKecamatan)

				strKecamatanCek = Mobile.verifyElementExist(teksKecamatan, 2, FailureHandling.OPTIONAL)

				if (strKecamatanCek == false){
					while (cekAngka != 5 & strKecamatanCek == false){
						Mobile.swipe(1000, 600, 1000, 250)

						strKecamatanCek = Mobile.verifyElementExist(teksKecamatan, 1, FailureHandling.OPTIONAL)

						if (strKecamatanCek == false){
							cekAngka = cekAngka + 1
						}
					}
				}

				if (strKecamatanCek == false){
					intAngka = intAngka + 1
					strProvinsiTadi  = strProvinsi
					strKotaTadi		 = strKota
					strKecamatanTadi = strKecamatan
					strKelurahanTadi = strKelurahan
					strStatus = 'FAILED'
					strKeterangan = 'Nama Kecamatan tidak ada'
					errKecamatan = true
					keywordIGO.tulisCekAlamat(excelRow, strStatus, strKeterangan)
					Mobile.pressBack()
					continue
				} else {
					//					Mobile.tap(teksKecamatan, 1)
					keywordIGO.tulisCekAlamat(excelRow, strStatus, strKeterangan)
					Mobile.pressBack()
				}
			}

			//			if (strKelurahan != strKelurahanTadi){
			//				strKelurahan = strKelurahan.toUpperCase()
			//				Mobile.tap(findTestObject('IGORevamp/EApps/spnEAKelurahan'), 1)
			//				TestObject teksKelurahan = new TestObject('newObjectKelurahan')
			//				teksKelurahan.addProperty('text', ConditionType.EQUALS, strKelurahan)
			//
			//				strKelurahanCek = Mobile.verifyElementExist(teksKelurahan, 2, FailureHandling.OPTIONAL)
			//
			//				if (strKelurahanCek == false){
			//					while (cekAngka != 5 & strKelurahanCek == false){
			//						Mobile.swipe(1000, 700, 1000, 350)
			//
			//						strKelurahanCek = Mobile.verifyElementExist(teksKelurahan, 1, FailureHandling.OPTIONAL)
			//
			//						if (strKelurahanCek == false){
			//							cekAngka = cekAngka + 1
			//						}
			//					}
			//				}
			//
			//				if (strKelurahanCek == false){
			//					intAngka = intAngka + 1
			//					strProvinsiTadi  = strProvinsi
			//					strKotaTadi		 = strKota
			//					strKecamatanTadi = strKecamatan
			//					strKelurahanTadi = strKelurahan
			//					strStatus = 'FAILED'
			//					strKeterangan = 'Nama Kelurahan tidak ada'
			//					keywordIGO.tulisCekAlamat(excelRow, strStatus, strKeterangan)
			//					Mobile.pressBack()
			//					continue
			//				} else {
			//					keywordIGO.tulisCekAlamat(excelRow, strStatus, strKeterangan)
			//					Mobile.pressBack()
			//				}
			//			}

			//mantap banyak banget bujaaank
			if (intAngka == 1) {
				Mobile.swipe(1000, 800, 1000, 400)
			}

			intAngka = intAngka + 1
			strProvinsiTadi  = strProvinsi
			strKotaTadi		 = strKota
			strKecamatanTadi = strKecamatan
			strKelurahanTadi = strKelurahan
		}
	}

	@Keyword
	def static tulisCekAlamat(int intNo, String Status, String Keterangan) {
		//		FileInputStream file = new FileInputStream (new File("C:\\Project\\IGORevamp\\Datatable\\Cek Alamat v1.2.xlsx"))
		FileInputStream file = new FileInputStream (new File("C:\\Project\\IGORevamp\\Datatable\\Book1.xlsx"))
		XSSFWorkbook workbook = new XSSFWorkbook(file)
		XSSFSheet sheet = workbook.getSheet('Sheet1')

		sheet.createRow(intNo).createCell(0).setCellValue(Status)
		sheet.getRow(intNo).createCell(1).setCellValue(Keterangan)

		file.close()

		//		FileOutputStream outFile =new FileOutputStream(new File("C:\\Project\\IGORevamp\\Datatable\\Cek Alamat v1.2.xlsx"))
		FileOutputStream outFile =new FileOutputStream(new File("C:\\Project\\IGORevamp\\Datatable\\Book1.xlsx"))
		workbook.write(outFile)
		outFile.close()
	}
}