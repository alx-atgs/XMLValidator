package ru.atgs.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class PropertiesFile {

	// private static String PropsFileName = "xml.config.properties";

	public static void main(String[] args) throws IOException {

		String PropsFileName = "ps.config.properties";
		System.out.println("Вывод полного пути файла " + PropsFileName + ":");
		String PropsFileFullPath = PropertiesFile.getPropsFileFullPath(PropsFileName);
		System.out.println(PropsFileFullPath);
		System.out.println("------------");

		System.out.println("Вывод из файла " + PropsFileName + " всех ключей и их значений:");
		PropertiesFile.printAllprops(PropsFileName);
		System.out.println("------------");

		System.out.println("Вывод заданного ключа из файла " + PropsFileName + ":");
		// String key1 = "xmldeclaration";
		String key1 = "ps.servicename.1";
		System.out.println(key1 + " = " + PropertiesFile.getProps(PropsFileName).getProperty(key1));
		System.out.println("------------");

		System.out.println("Вывод ключей и их значений из файла " + PropsFileName
				+ ", если в нем есть ключи вида: key0, key1... и т.д.");
		// String key = "xsd.schema";
		String key = "ps.ipserver";
		List<String> StringProps = PropertiesFile.getStringProps(PropsFileName, key);
		System.out.println("Всего элементов: " + StringProps.size());
		for (int i = 0; i < StringProps.size(); i++) {
			System.out.println(key + "." + i + " = " + StringProps.get(i));
		}

	}

	/**
	 * Установка полного пути до файла Properties.
	 * 
	 * @param PropsFileName - задаем имя файла Properties.
	 * @return Получаемая строка с полным путем, который получаем от пути, откуда
	 *         запускаем приложение. Файл Properties должен лежать рядом с
	 *         jar-файлом или в корне проекта.
	 * @throws UnsupportedEncodingException
	 * @throws MalformedURLException
	 */
	private static String getPropsFileFullPath(String PropsFileName)
			throws UnsupportedEncodingException, MalformedURLException {

		String PropsDirPath = new AppStartUpPath().getDirPath().toString();

		String PropsFileFullPath = PropsDirPath + File.separator + PropsFileName;
		return PropsFileFullPath;
	}

	/**
	 * Считываем содержимое из задаваемого файла Properties
	 * 
	 * @param PropsFileName - задаем имя файла
	 * @return
	 * @return Возвращаем набор ключей и значений, из которого потом можем брать
	 *         нужные.
	 * @throws IOException
	 */
	public static Properties getProps(String PropsFileName) {

		Properties defaultProps = new Properties();
		// создаем поток для чтения из файла
		FileInputStream input = null;
		try {
			input = new FileInputStream(getPropsFileFullPath(PropsFileName));
			// загружаем свойства
			defaultProps.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return defaultProps;
	}

	/**
	 * Return array from properties file. Array must be defined as "key.0=value0",
	 * "key.1=value1", ...
	 * 
	 * @throws IOException
	 */
	public static List<String> getStringProps(String PropsFileName, String key) throws IOException {

		List<String> result = new LinkedList<>();
		// defining variable for assignment in loop condition part
		String value;
		Properties defaultProps = getProps(PropsFileName);
		// next value loading defined in condition part
		for (int i = 0; (value = defaultProps.getProperty(key + "." + i)) != null; i++) {
			result.add(value);
		}
		return result;
	}

	/**
	 * Вывод всех ключей и их значений из файла Properties/
	 * 
	 * @param PropsFileName - задаем имя файла
	 */
	public static void printAllprops(String PropsFileName) throws FileNotFoundException, IOException {

		Properties defaultProps = getProps(PropsFileName);
		Enumeration<?> e = defaultProps.propertyNames();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			String value = defaultProps.getProperty(key);
			System.out.println("Key : " + key + ", Value : " + value);
		}

	}

}
