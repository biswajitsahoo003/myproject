package com.tcl.dias.l2oworkflowutils.beans;

/**
 * 
 * This file contains the MFResponseToOms.java class.
 * 
 *
 * @author Kruthika S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MFResponseToOms {
	private int quoteId;
	private int siteId;
	private String productName;
	private String cbFlag;
	
	public String getCbFlag() {
		return cbFlag;
	}
	public void setCbFlag(String cbFlag) {
		this.cbFlag = cbFlag;
	}
	public int getSiteId() {
		return siteId;
	}
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
	private String  mfStatus;
	
	public int getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(int quoteId) {
		this.quoteId = quoteId;
	}
	
	public String getMfStatus() {
		return mfStatus;
	}
	public void setMfStatus(String mfStatus) {
		this.mfStatus = mfStatus;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	

}
