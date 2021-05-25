package com.tcl.dias.common.utils;

/**
 * 
 * @author Manojkumar R
 *
 */
public enum UserType {

	CUSTOMER("customer"), INTERNAL_USERS("sales"),SUPER_ADMIN("super_admin"), PARTNER("partner");

	private final String type;

	UserType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return this.type;
	}

}
