package com.tcl.dias.serviceinventory.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * Bean class to hold sites for configurations
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class SISiteConfigurationBean {

	private String siteAddress;

	private Integer siOrderId;

	private Map<String, String> attributes = new HashMap<>();

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public Integer getSiOrderId() {
		return siOrderId;
	}

	public void setSiOrderId(Integer siOrderId) {
		this.siOrderId = siOrderId;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

}
