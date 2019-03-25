
/**
 * Developed by Alexander Kuznetsov
 * GitHub - https://github.com/___/
 */

package ru.atgs.main;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ru.atgs.utils.Check;
import ru.atgs.utils.LogApp;
import ru.atgs.utils.ObjUtil;
import ru.atgs.xml.utils.XmlUtil;
import ru.atgs.xml.utils.XmlValidator;

/**
 * Класс XMLbyXSDvalidation позволяет проверить входящую xml-строку на
 * соответствие схеме XSD.
 * 
 * @version 1.0.02 (2019-03-25)
 * 
 * @author Alexander Kuznetsov
 *
 */
public class XMLbyXSDvalidation {

	final static String Version = "1.0.02";
	final static String PropsFileName = "xml.config.properties";

	private static String inputFilePathOrXMLmessage;
	private static String schemaDirPath;
	private static String LogFormat;

	private static String xmlString;
	static String xmlFileName;
	static String xmlDirPath;
	static String xmlFilePath;
	private static String schemaFIlelPath;

	private static String LogFile = "xmlmessage.log";
	private static String logFilePath;

	private static String result;

	public static void main(String[] args) {

		if (args.length == 1 && args[0].toUpperCase().toLowerCase().trim().equals("version")) {
			XMLbyXSDvalidation.version();
			return;
		} else if (args.length != 3) {
			printUsage();
		} else {
			inputFilePathOrXMLmessage = args[0];
			schemaDirPath = args[1];
			LogFormat = args[2];

			String printOut = XMLbyXSDvalidation.run(inputFilePathOrXMLmessage, schemaDirPath, LogFormat);
			System.out.println(printOut);
		}
	}

	public static String run(String inputPathOrTxt, String schemaDirPath, String LogFormat) {

//		
		// Проверяем, что папка со схемами существует и создаем в ней
		// папку для логов (если в ней еще такой нет)
		if (!Check.dir(schemaDirPath)) {
			result = "Путь к XSD-схемам не найден.\nPath to XSD-schemas not found.";
			return result;
		}

		//
		// задаем настройки для вывода лога
		// будем класть логи в заданную нам папку со схемами
		String LogDirPath = schemaDirPath + File.separator + "Logs";
		if (!Check.dir(LogDirPath)) {
			boolean success = (new File(LogDirPath)).mkdir();
			if (!success) {
				result = "Невозможно создать папку Logs: " + LogDirPath + "\nUnable to create Logs folder: "
						+ LogDirPath;
				return result;
			}
		}
		String dateLogFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd(HH`mm`ss)"));
		logFilePath = LogDirPath + File.separator + dateLogFileName + "." + LogFile;
		LogApp.setLogFormat(LogFormat);
		LogApp.setPathLogFile(logFilePath);

		//
		// предполагаем, что первый аргумент - это xml-сообщение
		xmlString = inputPathOrTxt;
		// но проверим, вдруг пришла строка с указанием пути к существующему файлу,
		// тогда возьмем его содержимое.
		if (Check.file(inputPathOrTxt)) {
			xmlFileName = (new File(inputPathOrTxt)).getName().toString();
			xmlDirPath = (new File(inputPathOrTxt)).getParentFile().toString();
			xmlFilePath = inputPathOrTxt;
			xmlString = ObjUtil.getContentAsString(xmlFilePath, "utf-8");
		}

		//
		// проверяем во входящем xml-сообщении наличие BOM.
		// если false, то дальше может не найтись rootElement, также как и XmlHeader.
		if (Check.foundForUtf8BOM(ObjUtil.string2inputStream(xmlString))) {
			result = "Удалите BOM (Byte Order Mark) из XML-сообщения.\nRemove BOM (Byte Order Mark) from the XML message.";
			LogApp.info(result);
			return result;
		}
		// проверяем во входящей xml-строке наличие корректного XmlHeader, используя
		// регулярные выражения.
		if (!XmlUtil.checkXmlHeader(xmlString)) {
			result = "XmlHeader записан некорректно.\nXmlHeader is incorrect.";
			LogApp.info(result);
			return result;
		}

		// проверяем что входящая xml-строка содержит XmlHeader, равный значению
		// элемента xmldeclaration из файла свойств (xml.config.properties).
		if (!XmlUtil.checkInputXml(xmlString)) {
			result = "XmlHeader записан некорректно.\nXmlHeader is incorrect.";
			LogApp.info(result);
			return result;
		}

		// берем корневой элемент сообщения и по нему вычисляем какой схему использовать
		String rootElement = XmlUtil.getRootElement(xmlString);
		if (!rootElement.equals("false")) {
			String xsdname = XmlUtil.getXsdShemaName(rootElement);
			if (!xsdname.equals("false")) {
				schemaFIlelPath = schemaDirPath + File.separator + xsdname;
				if (!Check.file(schemaFIlelPath) || schemaFIlelPath == null) {
					result = "XSD-схема не найдена в папке: " + schemaFIlelPath + "\nXSD-schemas not found in "
							+ schemaFIlelPath;
					LogApp.info(result);
					return result;
				}
			} else {
				result = "Входящее сообщение не соответстует ни одной XSD-схеме.\nThe message does not match any XSD schema.";
				LogApp.info(result);
				return result;
			}
		} else {
			result = "Невозможно определить корневой элемент.\nThe root element cannot be defined.";
			LogApp.info(result);
			return result;
		}

		// PrintVars();

		// System.out.println("==========================================");
		String resultvalide = XmlValidator.byXsd(xmlString, schemaFIlelPath, logFilePath);
		result = resultvalide;
		LogApp.info(result);
		// System.out.println("Результат валидации:\n" + result);
		// System.out.println("==========================================");
		// PrintVars();

		return result;
	}

	/**
	 * 
	 */
	public static void version() {
		System.out.println("\nXMLbyXSDvalidation.jar version: " + Version + "\n");
	}

	/**
	 * вывод значений переменных
	 */
	private static void PrintVars() {
		List<String> Vars = new ArrayList<>();
		Vars.add("xmlFileName = " + xmlFileName);
		Vars.add("xmlDirPath = " + xmlDirPath);
		Vars.add("xmlFilePath = " + xmlFilePath);
		Vars.add("---------------");
		Vars.add("schemaDirPath = " + schemaDirPath);
		Vars.add("schemaFIlelPath = " + schemaFIlelPath);
		Vars.add("---------------");
		Vars.add("logFilePath = " + logFilePath);
		// for (int i = 0; i < Vars.size(); i++) {
		// System.out.println(Vars.get(i));
		// }
		for (String param : Vars) {
			System.out.println(param);
		}

	}

	private static void printUsage() {
		System.out.println("Usage : java -jar XMLbyXSDvalidation.jar <xmlFilePath> <schemaDirPath> <LogFormat>");
		System.out.println("\nShow XMLbyXSDvalidation.jar version:");
		System.out.println("Usage: java -jar XMLbyXSDvalidation.jar version\n");
	}
}