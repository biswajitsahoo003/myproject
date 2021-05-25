package com.tcl.dias.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * This file contains the ConstantUtils.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ConstantUtils {

	// to do need to remove it
	final static Map<String, String> productName = new HashMap<>();
	final static Map<String, String> interfaceMap = new HashMap<>();

	public static  Map<String, String> getNameMap() {
		productName.put("IAS", "Internet Access Service");
		return productName;

	}

	public static  Map<String, String> getAttributeMap() {
		interfaceMap.put("Interface", "FE");
		return productName;

	}

}
