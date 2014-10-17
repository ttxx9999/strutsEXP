package org.vit.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadFileContent {
	public static String getFileContent() throws Exception {
		InputStream inputStream = Runtime.getRuntime().getClass().getResourceAsStream("/resource/small.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String temp = "";
		while (reader.ready()) {
			temp += reader.readLine() + "\n";
		}
		return temp;
	}
}
