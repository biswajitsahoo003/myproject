package com.tcl.dias.products.webex.beans;

/**
 * Country code and its name for Webex Products
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class WebexProductLocationDetailBean {

	private String name;
	private String code;
	private String isdCode;

	public WebexProductLocationDetailBean() {

	}

	public WebexProductLocationDetailBean(String name) {
		this.name = name;
	}

	public WebexProductLocationDetailBean(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public WebexProductLocationDetailBean(String name, String code, String isdCode) {
		this.name = name;
		this.code = code;
		this.isdCode = isdCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIsdCode() {
		return isdCode;
	}

	public void setIsdCode(String isdCode) {
		this.isdCode = isdCode;
	}

}
