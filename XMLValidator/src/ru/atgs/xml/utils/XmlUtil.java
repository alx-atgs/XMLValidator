package ru.atgs.xml.utils;
/**
 * 
 * checkInputXml
 * getXmlHeader
 * isXMLLike
 * getRootElement
 * getXsdShemaName
 * xmlString
 *  
 */

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ru.atgs.utils.Check;
import ru.atgs.utils.ObjUtil;
import ru.atgs.utils.PropertiesFile;

public class XmlUtil {

	private static String PropsFileName = "xml.config.properties";
	private static String xmlrootElement = "xml.rootelement";
	private static String xsdschema = "xsd.schema";

	public static void main(String[] args) {
		String inXMLStr = "D:/Work/WorkProject/Gazpromneft-AERO/XMLValidator/XML/ticket.xml";

		inXMLStr = ObjUtil.getContentAsString(inXMLStr, "utf-8");

		boolean checkInputXml = XmlUtil.checkInputXml(inXMLStr);
		System.out.println("checkInputXml: " + checkInputXml);

		String RootElement = getRootElement(inXMLStr);
		System.out.println(RootElement);

		String XsdShemaName = getXsdShemaName(RootElement);
		System.out.println(XsdShemaName);
	}

	/**
	 * Проверяем что входящая xml-строка в первой строке содержит XmlHeader, равный
	 * значению элемента xmldeclaration из файла свойств (xml.config.properties).
	 * 
	 * @param xmlString - входящая строка
	 * @return True or false, содержит или нет
	 */
	public static boolean checkInputXml(String xmlString) {

		// проверим что на входе XML структура, исключая декларацию
		// if (!isXMLLike(xml)) {
		// return false;
		// }

		// берем заголовок из входящей строки и обрабатываем его
		String xmlheader = getXmlHeader(xmlString).toUpperCase().toLowerCase().trim();

		// берем из файла Properties значение xmldeclaration и обрабатываем его
		String xmldeclaration = PropertiesFile.getProps(PropsFileName).getProperty("xmldeclaration");
		xmldeclaration = xmldeclaration.toUpperCase().toLowerCase().trim();

		// сравниваем два значения и возвращаем его результат
		boolean checkxmldeclaration = xmldeclaration.equals(xmlheader);
		if (checkxmldeclaration) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Возвращаем из входящей xml-строки XmlHeader, используя регулярные выражения.
	 * Запросто "спотыкается" на xml с BOM или с пробелами в первой строке. Все в
	 * xml должно быть очень корректно.
	 * 
	 * @param inXMLStr - входящая строка
	 * @return String - строка со значением или текстом с ошибкой
	 */

	public static String getXmlHeader(String inXMLStr) {

		String backString = null;
		// Маска для проверки xml декларации
		final String XML_HEADER = "(<\\?.*?\\?>)";
		final Pattern headerPattern = Pattern.compile(XML_HEADER,
				Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

		if (inXMLStr != null) {
			String trimmedString = inXMLStr.trim();

			// берем строку между вхождений символов < и >
			// System.out.println(trimmedString.startsWith("<"));
			if (trimmedString.startsWith("<") && trimmedString.endsWith(">")) {
				// создаем строку по маске XML_HEADER (декларации)
				Matcher headerMatcher = headerPattern.matcher(trimmedString);
				// если найдена, то возвращаем значение
				if (headerMatcher.find()) {
					backString = headerMatcher.group(0);
				}
			} else {
				backString = "XmlUtil.getXmlHeader: Ошибка. Во входящем сообщении не удалось обнаружить XmlHeader.\n"
						+ " Вероятно, в сообщении присутствует BOM (Byte Order Mark).";
			}
		}
		return backString;
	}

	/**
	 * Проверяем во входящей xml-строке наличие XmlHeader, используя регулярные
	 * выражения. Запросто "спотыкается" на xml с BOM или с пробелами в первой
	 * строке. Все в xml должно быть очень корректно.
	 * 
	 * @param inXMLStr - входящая строка
	 * @return True or false, корректный XmlHeader или некорректный
	 */

	public static boolean checkXmlHeader(String inXMLStr) {

		// Маска для проверки xml декларации
		final String XML_HEADER = "(<\\?.*?\\?>)";
		final Pattern headerPattern = Pattern.compile(XML_HEADER,
				Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

		if (inXMLStr != null) {
			// убираем пробелы в конце строки
			String trimmedString = inXMLStr.replaceAll("\\s+$", "");
			// берем строку между вхождений символов < и >
			// System.out.println(trimmedString.startsWith("<"));
			if (trimmedString.startsWith("<") && trimmedString.endsWith(">")) {
				// создаем строку по маске XML_HEADER (декларации)
				Matcher headerMatcher = headerPattern.matcher(trimmedString);
				// если найдена, то возвращаем значение
				if (headerMatcher.find()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Возвращает true, если input String проходит проверку на XML структуру. Строку
	 * ожидаем в кодировке UTF-8 (проверки тут еще нет). Если берем из файла, то
	 * кодировку производим принудительно.
	 * 
	 * @param inXMLStr inString a string that might be XML
	 * @return true of the string is XML, false otherwise
	 */

	public static boolean isXMLLike(String inXMLStr) {

		// Маска для проверки xml текста
		final String XML_PATTERN_STR = "<(\\S+?)(.*?)>(.*?)</\\1>";
		final Pattern pattern = Pattern.compile(XML_PATTERN_STR,
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

		// Маска для проверки xml декларации
		final String XML_HEADER = "(<\\?.*?\\?>)";
		final Pattern headerPattern = Pattern.compile(XML_HEADER,
				Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

		boolean result = false;

		if (inXMLStr != null) {
			String trimmedString = inXMLStr.trim();
			// trimmedString = ObjUtil.delBOM(trimmedString);

			// берем строку между вхождений символов < и >
			if (trimmedString.startsWith("<") && trimmedString.endsWith(">")) {
				// создаем строку по маске XML_HEADER (декларации)
				Matcher headerMatcher = headerPattern.matcher(trimmedString);
				// если найдена, но заменяем ее на "", т.к. остальная часть xml сообщения не
				// пройдет по этой маске
				if (headerMatcher.find()) {
					trimmedString = trimmedString.replace(headerMatcher.group(0), "");
					// System.out.println("headerMatcher = " + headerMatcher.group(0));
				}
				// trimmedString = trimmedString.trim();
				// проверяем по маске XML_PATTERN_STR и возвращаем результат
				Matcher matcher = pattern.matcher(trimmedString);
				result = matcher.matches();
			}
		}
		return result;
	}

	/**
	 * Определение значения корневого элемента в XML
	 * 
	 * @param xml - входящая xml-строка или xml-файл
	 * @return
	 */
	public static String getRootElement(String inputxmlStringOrFile) {

		String backString = "false";
		String backStringErr;
		try {
			// считаем, что пришла строка с путем к файлу
			if (Check.file(inputxmlStringOrFile)) {
				// Строим объектную модель исходного XML файла
				// final File xmlFile = new File(inputxmlStringOrFile);
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(new File(inputxmlStringOrFile));
				// Выполнять нормализацию не обязательно, но рекомендуется
				doc.getDocumentElement().normalize();

				backString = doc.getDocumentElement().getNodeName();

				// входящая строка не является путем к существующему файлу
				// проверяем пройдет ли проверку checkInputXml
			} else if (checkXmlHeader(inputxmlStringOrFile)) {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(new InputSource(new StringReader(inputxmlStringOrFile)));
				// Выполнять нормализацию не обязательно, но рекомендуется
				doc.getDocumentElement().normalize();

				backString = doc.getDocumentElement().getNodeName();
				// вариант через создание временного файла
				// String TempPath = ObjUtil.tempPath();
				// String TempFilePath = TempPath + File.separator + "~xml.tmp";
				// ObjUtil.stringToFile(inputxmlStringOrFile, TempFilePath, "utf-8");
				// xmlFilePath = TempFilePath;
				// new File(TempFullPath).delete(); - придумать как удалить после выполнения
				// метода

				// проверка checkInputXml не пройдена, вероятно несуществующий файл
			} else {
				backStringErr = "Ошибка в getRootElement. Во входящем XML некорректно записана первая строка или указан путь к несуществующему файлу.";
				System.out.println(backStringErr);

			}

		} catch (ParserConfigurationException e) {
			backStringErr = "XmlUtil.getRootElement: Ошибка при определении значения getRootElement\n" + e.getMessage();
			// System.out.println(backStringErr);

		} catch (SAXException e) {
			backStringErr = "XmlUtil.getRootElement: Ошибка при определении значения getRootElement\n" + e.getMessage();
			// System.out.println(backStringErr);

		} catch (IOException e) {
			backStringErr = "XmlUtil.getRootElement: Ошибка при определении значения getRootElement\n" + e.getMessage();
			// System.out.println(backStringErr);
		}

		return backString;
	}

	/**
	 * 
	 * @param rootElement
	 * @return
	 */
	public static String getXsdShemaName(String rootElement) {

		String backString = "false";
		String backStringErr;
		try {
			if (rootElement != null) {
				List<String> listRootelement = PropertiesFile.getStringProps(PropsFileName, xmlrootElement);
				List<String> listXsdschema = PropertiesFile.getStringProps(PropsFileName, xsdschema);
				for (int i = 0; i < listRootelement.size(); i++) {
					// System.out.println("xml.rootElement." + i + " = " + listRootelement.get(i));
					if (listRootelement.get(i).toUpperCase().toLowerCase().trim()
							.equals(rootElement.toUpperCase().toLowerCase().trim())) {
						// String j = String.valueOf(i);
						// System.out.println("j = " + j);
						backString = listXsdschema.get(i);
					}
				}
			} else {
				backStringErr = "XmlUtil.getXsdShemaName: Ошибка. Проверьте входящее значение корневого элемента. Текущее значение = "
						+ rootElement;
				System.out.println(backStringErr);
			}
		} catch (IOException e) {
			backStringErr = "XmlUtil.getXsdShemaName: Ошибка при определении значения getXsdShemaName\n"
					+ e.getMessage();
			// System.out.println(backStringErr);
		}
		return backString;
	}

}
