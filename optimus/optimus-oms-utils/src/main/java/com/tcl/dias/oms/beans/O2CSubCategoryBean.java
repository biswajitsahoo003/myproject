package com.tcl.dias.oms.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This file contains the FRequest.java class.
 * 
 *
 * @author AnneF
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class O2CSubCategoryBean  implements Serializable {
	
	private static final long serialVersionUID = 8994816654530400751L;
	
	private String o2cOrderType;
	private String o2cOrderSubType;
	
	public String getO2cOrderType() {
		return o2cOrderType;
	}
	public void setO2cOrderType(String o2cOrderType) {
		this.o2cOrderType = o2cOrderType;
	}
	public String getO2cOrderSubType() {
		return o2cOrderSubType;
	}
	public void setO2cOrderSubType(String o2cOrderSubType) {
		this.o2cOrderSubType = o2cOrderSubType;
	}
	@Override
	public String toString() {
		return "O2CSubCategoryBean [o2cOrderType=" + o2cOrderType + ", o2cOrderSubType=" + o2cOrderSubType + "]";
	}
	
	

	
}
