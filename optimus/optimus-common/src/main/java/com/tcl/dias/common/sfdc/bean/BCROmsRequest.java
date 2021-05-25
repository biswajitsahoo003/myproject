package com.tcl.dias.common.sfdc.bean;

import java.util.List;

/**
 * This file contains the BCROmsRequest.java class. used for sfdc
 * 
 * @author Muthuselvi S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


public class BCROmsRequest {
	private List<BCROmsRecord> bcrOmsRecords;
	private Integer tpdId;
	private Boolean isAddValid;

	
	public Boolean getIsAddValid() {
		return isAddValid;
	}
	public void setIsAddValid(Boolean isAddValid) {
		this.isAddValid = isAddValid;
	}
	public List<BCROmsRecord> getBcrOmsRecords() {
		return bcrOmsRecords;
	}
	public void setBcrOmsRecords(List<BCROmsRecord> bcrOmsRecords) {
		this.bcrOmsRecords = bcrOmsRecords;
	}
	public Integer getTpdId() {
		return tpdId;
	}
	public void setTpdId(Integer tpdId) {
		this.tpdId = tpdId;
	}
	
}
