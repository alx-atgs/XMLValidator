package ru.atgs.xml.utils;
/**
 * 
 * byXsd
 * 
 * byXsdStrong 
 * byXsdNotSecure 
 * byXsdGetErrors 
 * SchemaFactoryDefault
 * SchemaFactoryNotSecure
 * 
 */

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import ru.atgs.utils.ObjUtil;

/**
 * This class contains methods to validate XML by XSD. You can implement this
 * class into your project.
 * 
 * @author Alexander Kuznetsov
 */

public class XmlValidator {

	private static SchemaFactory factory;

	/**
	 * 
	 * @param xmlString
	 * @param schemaFullPath
	 * @param logPath
	 * @return
	 */

	public static String byXsd(String xmlString, String schemaFilePath, String logFilePath) {

		String backstring = null;

		//
		// вызов метода проверки xmlString по схеме со строгой проверкой
		boolean valid = XmlValidator.byXsdStrong(xmlString, schemaFilePath);
		if (valid) {
			backstring = String.valueOf(valid);
			// LogApp.setPathLogFile(logFilePath);
			// LogApp.info("Validation returned success.");
			//
			// если не прошла строгая проверка, то проверка с отключенной
			// factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
			/*
			 * } else { valid = XmlValidator.byXsdNotSecure(xmlString, schemaFilePath); //
			 * если вновь не прошла, то вывод списка с ошибками if (valid) { //
			 * System.out.println("result: XML is valid against schema(s) with //
			 * FEATURE_SECURE_PROCESSING = false"); backstring = String.valueOf(valid);
			 * 
			 * // если не прошла проверка с отключенной FEATURE_SECURE_PROCESSING, то
			 * собираем // ошибки
			 * 
			 */
		} else {
			// LogApp.setPathLogFile(LogXMLerror);
			List<Exception> exceptions = XmlValidator.byXsdGetErrors(xmlString, schemaFilePath);
			// String xmlErrors = "Total number of errors: " +
			// String.valueOf(exceptions.size()) + "\n";
			String xmlErrors = "";
			for (Exception e : exceptions) {
				xmlErrors += e.getMessage() + "\n";
			}
			// LogApp.info(xmlErrors);
			backstring = xmlErrors;
		}
		return backstring;
	}

	/**
	 * This method validate XML by input XML as String and XSD path to File.
	 * 
	 * @param xml     input XML as String
	 * @param xsdPath input XSD File Path
	 * @return true or false, valid or not
	 */

	public static boolean byXsdStrong(String xmlString, String schemaFilePath) {
		try {
			// проверяем существование файла, указанного как файл с XSD-схемой
			File schemaFile = new File(schemaFilePath);
			if (!schemaFile.exists() && !schemaFile.isFile()) {
				// System.out.println("Не найден указанный файл со схемой XSD." +
				// schemaFilePath);
				return false;
			}
			// если xmlString есть путь к xml-файлу, то берем содержимое файла как строку
			File xmlFile = new File(xmlString);
			if (xmlFile.exists() && xmlFile.isFile()) {
				xmlString = ObjUtil.getContentAsString(xmlString, "UTF-8");
			}

			// непосредственно валидация
			SchemaFactory factory = SchemaFactoryDefault();
			Schema schema = factory.newSchema(new File(schemaFilePath));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new StringReader(xmlString)));

		} catch (IOException e) {
			// System.out.println("IOException: ошибка в byXsdStrong:\n" + e.getMessage());
			return false;
		} catch (SAXException e) {
			// System.out.println("SAXException: ошибка в byXsdStrong:\n" + e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param xml
	 * @param xsdPath
	 * @return
	 */

	public static boolean byXsdNotSecure(String xmlString, String schemaFilePath) {
		try {
			// проверяем существование файла, указанного как файл с XSD-схемой
			File schemaFile = new File(schemaFilePath);
			if (!schemaFile.exists() && !schemaFile.isFile()) {
				// System.out.println("Не найден указанный файл со схемой XSD." +
				// schemaFilePath);
				return false;
			}
			// если xmlString есть путь к xml-файлу, то берем содержимое файла как строку
			File xmlFile = new File(xmlString);
			if (xmlFile.exists() && xmlFile.isFile()) {
				xmlString = ObjUtil.getContentAsString(xmlString, "UTF-8");
			}

			// непосредственно валидация с отключенным FEATURE_SECURE_PROCESSING
			SchemaFactory factory = SchemaFactoryNotSecure();
			Schema schema = factory.newSchema(new File(schemaFilePath));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(new StringReader(xmlString)));

		} catch (IOException e) {
			// System.out.println("IOException: ошибка в byXsdNotSecure:\n" +
			// e.getMessage());
			return false;
		} catch (SAXException e) {
			// System.out.println("SAXException: ошибка в byXsdNotSecure:\n" +
			// e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 
	 * @param xmlString
	 * @param schemaFilePath
	 * @return
	 */

	public static List<Exception> byXsdGetErrors(String xmlString, String schemaFilePath) {

		List<Exception> exceptions = new ArrayList<Exception>();
		try {
			// проверяем существование файла, указанного как файл с XSD-схемой
			File schemaFile = new File(schemaFilePath);
			if (!schemaFile.exists() && !schemaFile.isFile()) {
				exceptions.add(
						new Exception("XmlValidator.byXsdGetErrors: XSD-схема: " + schemaFilePath + " не найдена.\n"
								+ "XmlValidator.byXsdGetErrors: XSD-schemas: " + schemaFilePath + " not found."));
			}
			// если xmlString есть путь к xml-файлу, то берем содержимое файла как строку
			File xmlFile = new File(xmlString);
			if (xmlFile.exists() && xmlFile.isFile()) {
				xmlString = ObjUtil.getContentAsString(xmlString, "UTF-8");
			}

			// непосредственно валидация с установленным setErrorHandler для вывода ошибок
			SchemaFactory factory = SchemaFactoryNotSecure();
			Validator validator = factory.newSchema(new File(schemaFilePath)).newValidator();
			validator.setErrorHandler(new XSDValidatorErrorHandler(exceptions));
			// просто StreamSource некорректно, т.к. на вход string
			// validator.validate(new StreamSource(new StringReader(xmlString)));
			validator.validate(new StreamSource(new StringReader(xmlString)));
		} catch (IOException e) {
			// System.out.println(e.getMessage());
		} catch (SAXException e) {
			// System.out.println(e.getMessage());
		}
		if (exceptions.isEmpty()) {
			exceptions.add(new Exception("XmlValidator: The validation is successful."));
		}
		return exceptions;
	}

	/**
	 * 
	 * @return
	 */

	private static SchemaFactory SchemaFactoryDefault() {
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		return factory;
	}

	/**
	 * 
	 * @return
	 * @throws SAXNotRecognizedException
	 * @throws SAXNotSupportedException
	 */

	private static SchemaFactory SchemaFactoryNotSecure() {

		try {
			SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			// снижаем уровень проверки по безопасности (вероятность проведение
			// атаки отказ на обслуживание повышается)
			factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, false);
			return factory;

		} catch (SAXNotRecognizedException e) {
			System.out.println(e.getMessage());
		} catch (SAXNotSupportedException e) {
			System.out.println(e.getMessage());
		}
		return factory;
	}

}