package ru.atgs.xml.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Developed by Alexander Kuznetsov
 * GitHub - https://github.com/___/
 */
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * XML Error handler
 * 
 * @author Alexander Kuznetsov
 * 
 */
public class XSDValidatorErrorHandler implements ErrorHandler {

	private List<Exception> exceptions;
	private static int errorCount = 1;
	String DateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

	public XSDValidatorErrorHandler(List<Exception> exceptions) {
		this.exceptions = exceptions;
	}

	@Override
	public void error(SAXParseException exception) throws SAXException {
		exceptions.add(new Exception(DateTime + "	Error: " + errorCount++ + "	Line: " + exception.getLineNumber()
				+ "	Column: " + exception.getColumnNumber()));
		exceptions.add(exception);
	}

	@Override
	public void warning(SAXParseException exception) throws SAXException {
		exceptions.add(new Exception(DateTime + "	Warning: " + errorCount++ + "	Line: " + exception.getLineNumber()
				+ "	Column: " + exception.getColumnNumber()));
		exceptions.add(exception);
	}

	@Override
	public void fatalError(SAXParseException exception) throws SAXException {
		exceptions.add(new Exception(DateTime + "	Fatal error: " + errorCount++ + "	Line: "
				+ exception.getLineNumber() + "	Column: " + exception.getColumnNumber()));
		exceptions.add(exception);
	}

}
