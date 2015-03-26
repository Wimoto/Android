package com.mobitexoft.utils;

public class TextUtils {

	public static boolean isNullOrEmpty(String input) {
		if (input == null) {
			return true;
		}
		return android.text.TextUtils.isEmpty(input.trim());
	}
	
}
