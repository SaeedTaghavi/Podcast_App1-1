package com.projectemplate.musicpro.util;

import com.projectemplate.musicpro.config.GlobalValue;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtil {
	public static boolean checkUrl(String s) {
		return s.startsWith("http://") || s.startsWith("https://");
	}

	public static String unAccent(String s) {
		String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(temp).replaceAll("").replaceAll(GlobalValue.DD, "D").replace(GlobalValue.dd, "d");
	}

	public static boolean checkEndNextPage(String nextPage) {
		if (nextPage == null) {
			return false;
		}
		return nextPage.equals("end");
	}
}
