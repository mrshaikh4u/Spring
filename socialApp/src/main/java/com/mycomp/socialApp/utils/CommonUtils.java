package com.mycomp.socialApp.utils;

/**
 * Common Utils to be used across the App
 * 
 * @author Rahil
 *
 */
public class CommonUtils {
	public static boolean isStringNullorEmpty(String inputStr) {
		return inputStr == null || inputStr.trim()
		                                   .isEmpty();
	}
}
