package ru.atgs.test;

import ru.atgs.utils.LogApp;

public class Test_LogApp {

	public static void main(String[] args) {
		LogApp.setLogFormat("txt");
		LogApp.setOwnconsole(true);

		LogApp.setPathLogFile("D:/Work/WorkProject/Gazpromneft-AERO/XMLValidator/Schema/Test_LogApp.log");
		String Messagetxt = "Русский текст. Сохраняется в UTF-8\nEnglish text";
		LogApp.info(Messagetxt);

		LogApp.setLogFormat("xml");
		try {
			throw new Exception("Simulating an exception");
		} catch (Exception var4) {
			LogApp.setPathLogFile("D:/Work/WorkProject/Gazpromneft-AERO/XMLValidator/Schema/Test_LogApp_err.log");
			String ErrString = var4.getMessage().toString();
			LogApp.info(ErrString);
		}

		// "%t" обозначает каталог temp, который хранится в переменной среды "tmp"
		// альтернатива указанию пути: "c:/Temp/";
		// String PathLogFile = "%t";
		// setPathLogFile(PathLogFile + "/Test_LogApp.log");

	}
}