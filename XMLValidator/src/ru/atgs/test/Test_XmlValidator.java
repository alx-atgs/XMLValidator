package ru.atgs.test;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import ru.atgs.utils.Check;
import ru.atgs.utils.ObjUtil;
import ru.atgs.xml.utils.XmlUtil;
import ru.atgs.xml.utils.XmlValidator;

public class Test_XmlValidator {
	public static void main(String[] args) {

		// * byXsd
		// *
		// * byXsdStrong
		// * byXsdNotSecure
		// * byXsdGetErrors

		String DirXmlFile = "D:/Work/WorkProject/Gazpromneft-AERO/XMLValidator/XML";
		String DirPathSchema = "D:/Work/WorkProject/Gazpromneft-AERO/XMLValidator/Schema";

		String goodxmlstring = Test_XmlUtil.xmlString(1);
		String badxmlstring = Test_XmlUtil.xmlString(2);
		String SchemaXmlString = "D:/Work/WorkProject/Gazpromneft-AERO/XMLValidator/Schema/IATA_AIDX_FuelNotifRQ.xsd";

		// String xml1 = "file_with_bom.xml";
		// String xml2 = "file_without_bom.xml";
		// String xml3 = "ticketnum_req.xml";
		// String xml4 = "GolfCountryClub.xml";
		String xml5 = "AF SVO(DT).xml";
		String xml = xml5;

		String xmlfile = DirXmlFile + File.separator + xml;
		// xmlfile = ObjUtil.delBOM(xmlfile);
		String file2string = ObjUtil.getContentAsString(xmlfile, "utf-8");
		// file2string = ObjUtil.delBOM(file2string);

		String re = XmlUtil.getRootElement(file2string);
		String xn = XmlUtil.getXsdShemaName(re);
		String sFP = DirPathSchema + File.separator + xn;
		// String schemaFilePath = "";
		System.out.println("Путь к xml-файлу: " + xmlfile);
		System.out.println("Применяемая схема: " + sFP);

		// Strong
		System.out.println();
		System.out.println("Strong-------------------------");
		boolean res1 = XmlValidator.byXsdStrong(file2string, sFP);
		System.out.println(String.valueOf("Input XML-string: " + res1));
		boolean res2 = XmlValidator.byXsdStrong(xmlfile, sFP);
		System.out.println(String.valueOf("Input XML-file path: " + res2));
		boolean res11 = XmlValidator.byXsdStrong(goodxmlstring, SchemaXmlString);
		System.out.println(String.valueOf("Input from goodxmlstring: " + res11));
		boolean res21 = XmlValidator.byXsdStrong(badxmlstring, SchemaXmlString);
		System.out.println(String.valueOf("Input from badxmlstring: " + res21));

		// NotStrong
		System.out.println();
		System.out.println("NotStrong-------------------------");
		boolean res3 = XmlValidator.byXsdNotSecure(file2string, sFP);
		System.out.println(String.valueOf("Input XML-string: " + res3));
		boolean res4 = XmlValidator.byXsdNotSecure(xmlfile, sFP);
		System.out.println(String.valueOf("Input XML-file path: " + res4));

		// OutError
		System.out.println();
		System.out.println("OutError-------------------------");
		List<Exception> res5 = XmlValidator.byXsdGetErrors(file2string, sFP);
		System.out.println("Input XML-string:");
		for (Exception e : res5) {
			System.out.println(e.toString());
		}
		List<Exception> res6 = XmlValidator.byXsdGetErrors(xmlfile, sFP);
		System.out.println("Input XML-file path:");
		for (Exception e : res6) {
			System.out.println(e.toString());
		}
		List<Exception> res51 = XmlValidator.byXsdGetErrors(goodxmlstring, SchemaXmlString);
		System.out.println("Input from goodxmlstring:");
		// for (int i = 0; i < res51.size(); i++) {
		// System.out.println(res51.get(i));
		for (Exception e : res51) {
			System.out.println(e.toString());
		}
		List<Exception> res61 = XmlValidator.byXsdGetErrors(badxmlstring, SchemaXmlString);
		System.out.println("Input from badxmlstring:");
		for (Exception e : res61) {
			System.out.println(e.toString());
		}

		System.out.println();
		System.out.println("===============================================");

		// All together
		// String LogFormat = "txt";
		String dateLogFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd(HH`mm`ss)"));
		String logDirPath = DirXmlFile + File.separator + "Logs";
		if (!Check.dir(logDirPath)) {
			new File(logDirPath).mkdir();
		}
		String logFilePath = logDirPath + File.separator + dateLogFileName + ".test_logfile.log";
		System.out.println("All together with Logs-------------------------");
		String ares3 = XmlValidator.byXsd(file2string, sFP, logFilePath);
		System.out.println("Input XML-string: " + ares3);
		String ares4 = XmlValidator.byXsd(xmlfile, sFP, logFilePath);
		System.out.println("Input XML-file path: " + ares4);
		String ares31 = XmlValidator.byXsd(goodxmlstring, SchemaXmlString, logFilePath);
		System.out.println("Input from goodxmlstring: " + ares31);
		String ares41 = XmlValidator.byXsd(badxmlstring, SchemaXmlString, logFilePath);
		System.out.println("Input from badxmlstring: " + ares41);

	}

}