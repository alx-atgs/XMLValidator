package ru.atgs.test;

import ru.atgs.utils.Check;
import ru.atgs.utils.ObjUtil;
import ru.atgs.xml.utils.XmlUtil;

public class Test_XmlUtil {

	public static void main(String[] args) {

		// String inXMLStr =
		// "D:/Work/WorkProject/Gazpromneft-AERO/XMLValidator/XML/ticket.xml";
		String inXMLErr = "D:/Work/WorkProject/Gazpromneft-AERO/XMLValidator/XML/ticke.xml";

		String inXMLStr = xmlString(1);
		// String inXMLErr = xmlString(2);

		// если на входе файл, то переводим в строку
		if (Check.file(inXMLStr)) {
			inXMLStr = ObjUtil.getContentAsString(inXMLStr, "utf-8");
		}

		// правильный вариант
		System.out.println("Правильный вариант");
		System.out.println("============================");

		String fs = XmlUtil.getXmlHeader(inXMLStr);
		System.out.println("getXmlHeader: \n" + fs);
		System.out.println("----------------------------");

		boolean ff = XmlUtil.isXMLLike(inXMLStr);
		System.out.println("isXMLLike: " + ff);
		System.out.println("----------------------------");

		String rootElement = XmlUtil.getRootElement(inXMLStr);
		System.out.println("getRootElement: " + rootElement);
		System.out.println("----------------------------");

		String xsdname = XmlUtil.getXsdShemaName(rootElement);
		System.out.println("getXsdShemaName: " + xsdname);
		System.out.println("----------------------------");

		boolean b = XmlUtil.checkInputXml(inXMLStr);
		System.out.println(String.valueOf("checkInputXml: " + b));
		System.out.println("----------------------------");
		System.out.println("============================");

		// Неправильный вариант
		System.out.println("Неправильный вариант");
		System.out.println("============================");

		String fserr = XmlUtil.getXmlHeader(inXMLErr);
		System.out.println("getXmlHeader: " + fserr);
		System.out.println("----------------------------");

		boolean fferr = XmlUtil.isXMLLike(inXMLErr);
		System.out.println("isXMLLike: " + fferr);
		System.out.println("----------------------------");

		String rootElementerr = XmlUtil.getRootElement(inXMLErr);
		System.out.println("getRootElement: " + rootElementerr);
		System.out.println("----------------------------");

		String xsdnameerr = XmlUtil.getXsdShemaName(rootElementerr);
		System.out.println("getXsdShemaName: " + xsdnameerr);
		System.out.println("----------------------------");

		boolean berr = XmlUtil.checkInputXml(inXMLErr);
		System.out.println(String.valueOf("checkInputXml: " + berr));
		System.out.println("----------------------------");
		System.out.println("============================");

	}

	/**
	 * Метод для ТЕСТОВ
	 * 
	 * @param num
	 * @return
	 */

	public static String xmlString(int num) {
		String str1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
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
