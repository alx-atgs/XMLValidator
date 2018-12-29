package ru.atgs.test;

import java.io.File;

import ru.atgs.utils.ObjUtil;

public class Test_ObjUtil {

	public static void main(String[] args) {

		int num = 1;

		String pathnamefile = "D:/Work/WorkProject/Gazpromneft-AERO/XMLValidator/XML/SUZVS USER final.xml";
		String xmlString = xmlstr(1);

		String pathname;
		if (num == 1) {
			pathname = pathnamefile;
		} else {
			pathname = xmlString;
		}
		//
		// Test ObjUtil.getContentAsString
		String charset1 = "utf-8";
		String out = ObjUtil.getContentAsString(pathname, charset1);
		System.out.println("getContentAsString:");
		System.out.println("pathname.length = " + pathname.length());
		System.out.println("out.length = " + out.length());
		System.out.println("--------------------------");

		//
		// Тест ObjUtil.stringToFile
		String inputstr = "русский текст для проверки кодировки";
		String targetFilePath = ObjUtil.tempPath() + File.separator + "~11111111.txt";
		String charset = "utf-8";
		File filepath = ObjUtil.stringToFile(inputstr, targetFilePath, charset);
		System.out.println("stringToFile:\nfilepath = " + filepath);
		System.out.println("--------------------------");

		//
		// Test ObjUtil.getFirstLine
		System.out.println("getFirstLine:\n" + ObjUtil.getFirstLine(pathname));
		System.out.println("--------------------------");

		//
		// Test ObjUtil.getNumline
		int numLine = 1;
		System.out.println("getNumline: Взята строка № " + numLine + " :\n" + ObjUtil.getNumline(pathname, numLine));
		System.out.println("--------------------------");

		// Test String DelBom
		String charset2 = "utf-8";
		String inputString = ObjUtil.getContentAsString(pathname, charset2);
		// String inputString = "jndvbhkjndbefvkj";
		String out1 = ObjUtil.delBOM(inputString);
		System.out.println("delBOM(inputString):");
		System.out.println("pathname.length = " + inputString.length());
		System.out.println("out.length = " + out1.length());
		System.out.println("--------------------------");

		// Test File DelBom
		File filename = new File(pathname);
		String out2 = ObjUtil.delBOM(filename);
		System.out.println(filename);
		System.out.println("delBOM(filename):");
		System.out.println("filename.length = " + filename.length());
		System.out.println("out.length = " + out2.length());
		System.out.println("--------------------------");

		// Test tail
		int NumberLines = 5; // количество последних строк
		String lastLinestr = ObjUtil.tail(pathname, NumberLines);
		System.out.println(lastLinestr);
		System.out.println("--------------------------");

		// ObjUtil.NewMethod();
		System.out.println("--------------------------");

	}

	public static String xmlstr(int num) {
		String str1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		str1 = str1
				+ "\t<IATA_AIDX_FuelNotifRQ Version=\"1\" xmlns=\"http://www.iata.org/IATA/2007/00\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.iata.org/IATA/2007/00 IATA_AIDX_FuelNotifRQ-new.xsd \">";
		str1 = str1 + "\t\t  <Originator Code=\"AF\" CompanyShortName=\"Air France\" />";
		str1 = str1 + "\t\t  <FuelFlightLeg InternationalStatus=\"International\">";
		str1 = str1 + "\t\t    <LegIdentifier>";
		str1 = str1 + "\t\t      <Airline CodeContext=\"3\">AF</Airline>";
		str1 = str1 + "\t\t      <FlightNumber>1745</FlightNumber>";
		str1 = str1 + "\t\t      <DepartureAirport CodeContext=\"3\">SVO</DepartureAirport>";
		str1 = str1 + "\t\t      <ArrivalAirport CodeContext=\"3\">CDG</ArrivalAirport>";
		str1 = str1 + "\t\t      <OriginDate>2018-09-30</OriginDate>";
		str1 = str1 + "\t\t    </LegIdentifier>";
		str1 = str1 + "\t\t  </FuelFlightLeg>";
		str1 = str1 + "\t\t</IATA_AIDX_FuelNotifRQ>";
		// str1 = str1;
		String str2 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n";
		str2 = str2
				+ "\t<IATA_AIDX_FuelNotifRQ xmlns=\"http://www.iata.org/IATA/2007/00\" xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:jms1=\"http://www.tibco.com/namespaces/tnt/plugins/jms\" xmlns:ns0=\"soa://Framework/EEB/Common/Schemas/EEBMessage-v1_0\" TimeStamp=\"2018-04-17T09:45:24.064Z\" Version=\"1\" Target=\"Production\" xsi:schemaLocation=\"http://www.iata.org/IATA/2007/00 file:///R:/Газпром%20нефть%20-%20Аэро/Подразделения/Блок%20продаж/АСКУ/1.%20Методология/3.%20IATA/4.%20PreTransaction/СУЗВС/v3/IATA_AIDX_FuelNotifRQ.xsd\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" EchoToken=\"123\">\r\n";
		str2 = str2 + "\t  <Originator Code=\"AF\" CompanyShortName=\"Air France\" />\r\n";
		str2 = str2 + "\t  <FuelFlightLeg InternationalStatus=\"International\">\r\n";
		str2 = str2 + "\t    <LegIdentifier>\r\n";
		str2 = str2 + "\t      \t<Airline CodeContext=\"3\">AF</Airline>\r\n";
		str2 = str2 + "\t \t\t<FlightNumber>1745</FlightNumber>\r\n";
		str2 = str2 + "\t    </LegIdentifier>\r\n";
		str2 = str2 + "\t</IATA_AIDX_FuelNotifRQ>\r\n";
		String str = null;
		if (num == 1) {
			str = str1;
		} else if (num == 2) {
			str = str2;
		}

		return str;
	}
}
