package com.tcl.dias.common.beans;

import java.io.Serializable;
/**
 * 
 * This file contains the ProductLocationBean.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ProductLocationBean implements Serializable{
	private Integer id;
	
	private String name;
	
	private String intlDialCode;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntlDialCode() {
		return intlDialCode;
	}

	public void setIntlDialCode(String intlDialCode) {
		this.intlDialCode = intlDialCode;
	}
	
	
}
