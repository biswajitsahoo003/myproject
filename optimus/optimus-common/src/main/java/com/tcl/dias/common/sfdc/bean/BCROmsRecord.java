package com.tcl.dias.common.sfdc.bean;

/**
 * This file contains the BCROmsRecord.java class. used for sfdc
 * 
 * @author Muthuselvi S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

public class BCROmsRecord {
	
	private String opportunityId;
	private BCROptyProperitiesBean bcrOptyProperitiesBeans;
	private String recordTypeName;
	private String custAttribute;
	
	
	
	
	public String getCustAttribute() {
		return custAttribute;
	}
	public void setCustAttribute(String custAttribute) {
		this.custAttribute = custAttribute;
	}
	public String getRecordTypeName() {
		return recordTypeName;
	}
	public void setRecordTypeName(String recordTypeName) {
		this.recordTypeName = recordTypeName;
	}
	public String getOpportunityId() {
		return opportunityId;
	}
	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}
	public BCROptyProperitiesBean getBcrOptyProperitiesBeans() {
		return bcrOptyProperitiesBeans;
	}
	public void setBcrOptyProperitiesBeans(BCROptyProperitiesBean bcrOptyProperitiesBeans) {
		this.bcrOptyProperitiesBeans = bcrOptyProperitiesBeans;
	}
	
}
