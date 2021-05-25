package com.tcl.dias.location.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/**
*
* This bean holds the location related details of a site  
*
* @author ANNE NISHA
* @link http://www.tatacommunications.com/
* @copyright 2018 Tata Communications Limited
*/
@JsonInclude(Include.NON_NULL)
public class SiteDetailBean implements Serializable {
	
	private static final long serialVersionUID = -7410988290187762659L;
	
	private String offeringName;
	private List<LocationBean> site = new ArrayList<>();
	private String type;
	
	public String getOfferingName() {
		return offeringName;
	}
	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}
	public List<LocationBean> getSite() {
		return site;
	}
	public void setSite(List<LocationBean> site) {
		this.site = site;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}


	
}
