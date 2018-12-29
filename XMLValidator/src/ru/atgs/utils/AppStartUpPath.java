package ru.atgs.utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Класс для определения пути к каталогу, из которого запущен jar-файл.
 * 
 * String apppath = AppStartUpPath.getPath();
 */
public class AppStartUpPath {

	private static String outPath;

	public static void main(String[] args) {
		AppStartUpPath startUpPath = new AppStartUpPath();
		try {
			System.out.println("startUpPath: " + startUpPath.getDirPath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String path = getPath();
		System.out.println(path);
	}

	public static String getPath() {

		AppStartUpPath startUpPath = new AppStartUpPath();
		try {
			outPath = startUpPath.getDirPath().toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return outPath;

	}

	/**
	 * @return Путь к каталогу, в котором расположен jar-файл с классом
	 *         AppStartUpPath.
	 */
	public Path getDirPath() throws UnsupportedEncodingException, MalformedURLException {
		URL startupUrl = getClass().getProtectionDomain().getCodeSource().getLocation();
		Path path = null;
		try {
			path = Paths.get(startupUrl.toURI());
		} catch (Exception e) {
			try {
				path = Paths.get(new URL(startupUrl.getPath()).getPath());
			} catch (Exception ipe) {
				path = Paths.get(startupUrl.getPath());
			}
		}
		path = path.getParent();
		return path;
	}
}