package com.tcl.dias.products.constants;

/**
 * This file contains the DataCenterType.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum DataCenterType {

	INDIA_DATA_CENTER("IDC"),
	INTERNATIONAL_DATA_CENTER("GDC");//holds dummy data for international data center
	
	private String dcType;

	public String getDcType() {
		return dcType;
	}

	private DataCenterType(String  dcType) {
		this.dcType = dcType;
	}

}
