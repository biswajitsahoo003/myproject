package com.tcl.dias.oms.beans;
/**
 * 
 * This bean is used for updating LCON details for feasiblity
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class LconUpdateBean {
	
	private Integer siteId;
	private String lconName;
	private String lconNumber;
	private String isRequestManualFeasibilityTriggered;
	private String lconRemarks;
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public String getLconName() {
		return lconName;
	}
	public void setLconName(String lconName) {
		this.lconName = lconName;
	}
	public String getLconNumber() {
		return lconNumber;
	}
	public void setLconNumber(String lconNumber) {
		this.lconNumber = lconNumber;
	}
	public String getLconRemarks() {
		return lconRemarks;
	}
	public void setLconRemarks(String lconRemarks) {
		this.lconRemarks = lconRemarks;
	}

	public String getIsRequestManualFeasibilityTriggered() {
		return isRequestManualFeasibilityTriggered;
	}

	public void setIsRequestManualFeasibilityTriggered(String isRequestManualFeasibilityTriggered) {
		this.isRequestManualFeasibilityTriggered = isRequestManualFeasibilityTriggered;
	}
}
