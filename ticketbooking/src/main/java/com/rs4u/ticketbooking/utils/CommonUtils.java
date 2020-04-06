package com.rs4u.ticketbooking.utils;

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
