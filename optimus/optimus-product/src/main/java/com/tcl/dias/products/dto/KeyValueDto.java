package com.tcl.dias.products.dto;

/**
 * This file contains the KeyValueDto.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class KeyValueDto {

	private String key;
	
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public KeyValueDto(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}
	
}
