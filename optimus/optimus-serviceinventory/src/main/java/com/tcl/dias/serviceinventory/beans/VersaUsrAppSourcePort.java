package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author archchan
 *
 */
public class VersaUsrAppSourcePort implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("value")
	private Integer value;
	
	@JsonProperty("low")
	private Integer low;
	
	@JsonProperty("high")
	private Integer high;

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getLow() {
		return low;
	}

	public void setLow(Integer low) {
		this.low = low;
	}

	public Integer getHigh() {
		return high;
	}

	public void setHigh(Integer high) {
		this.high = high;
	}

	@Override
	public String toString() {
		return "VersaUsrAppSourcePort [value=" + value + ", low=" + low + ", high=" + high + "]";
	}	
	
	
	

}
