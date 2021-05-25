package com.tcl.dias.common.utils;

import java.util.UUID;

/**
 * Token generator class to generate the unique random uuids for all the users
 * 
 * @author Manojkumar R
 *
 */
public class TokenGenerator {
	private TokenGenerator() {
	}

	/**
	 * create- Create the token
	 * 
	 * @return
	 */
	public static String create() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}
}
