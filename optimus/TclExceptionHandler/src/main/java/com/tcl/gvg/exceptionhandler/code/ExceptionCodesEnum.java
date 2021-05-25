package com.tcl.gvg.exceptionhandler.code;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Manojkumar R
 *
 */
public enum ExceptionCodesEnum {
	E_0XOCFFFF("CommonException", "0XOCFFFF"), E_0XOCUC01("NullPointerException", "0XOCUC01"), E_0XOCUC02(
			"ArrayIndexOutOfBoundsException",
			"0XOCUC02"), E_0XOCUC03("ArithmeticException", "0XOCUC03"), E_0XOCUC04("ClassCastException",
					"0XOCUC04"), E_0XOCUC05("IllegalArgumentException", "0XOCUC05"), E_0XOCUC06("IllegalStateException",
							"0XOCUC06"), E_0XOCUC07("NumberFormatException", "0XOCUC07"), E_0XOCUC08("AssertionError",
									"0XOCUC08"), E_0XOCUC09("ExceptionInInitializerError", "0XOCUC09"), E_0XOCUC10(
											"StackOverflowError",
											"0XOCUC10"), E_0XOCUC11("NoClassDefFoundError", "0XOCUC11"), E_0XOCC001(
													"Exception",
													"0XOCC001"), E_0XOCC002("IOException", "0XOCC002"), E_0XOCC003(
															"FileNotFoundException",
															"0XOCC003"), E_0XOCC004("ParseException",
																	"0XOCC004"), E_0XOCC005("ClassNotFoundException",
																			"0XOCC005"), E_0XOCC006(
																					"CloneNotSupportedException",
																					"0XOCC006"), E_0XOCC007(
																							"InstantiationException",
																							"0XOCC007"), E_0XOCC008(
																									"InterruptedException",
																									"0XOCC008"), E_0XOCC009(
																											"NoSuchMethodException",
																											"0XOCC009"), E_0XOCC010(
																													"NoSuchFieldException",
																													"0XOCC010"), E_0XOCC011(
																															"NoSuchMethodError",
																															"0XOCC011");
	String className;
	String errorCode;

	private ExceptionCodesEnum(String className, String errorCode) {
		this.className = className;
		this.errorCode = errorCode;
	}

	private static final Map<String, ExceptionCodesEnum> CODE_MAP = new HashMap<>();
	private static final Map<String, ExceptionCodesEnum> ERROR_CLASS_MAP = new HashMap<>();

	static {
		for (ExceptionCodesEnum type : ExceptionCodesEnum.values()) {
			ERROR_CLASS_MAP.put(type.getClassName(), type);
		}
		for (ExceptionCodesEnum type : ExceptionCodesEnum.values()) {
			CODE_MAP.put(type.getErrorCode(), type);
		}
	}

	public static final ExceptionCodesEnum getByClassName(String value) {
		return ERROR_CLASS_MAP.get(value);
	}

	public static final ExceptionCodesEnum getByCode(String value) {
		return CODE_MAP.get(value);
	}

	public String getClassName() {
		return className;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
