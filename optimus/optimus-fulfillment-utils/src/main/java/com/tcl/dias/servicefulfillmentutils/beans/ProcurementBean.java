package com.tcl.dias.servicefulfillmentutils.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Savanya
 *
 */
@JsonInclude(Include.NON_NULL)
public class ProcurementBean {

	private Integer procurementId;
	private Integer scOrderId;
	private String solutionName;
	private String cloudCode;
	private String wbsNumber;
	private String wbsValue;
	private String glccNumber;
	private String glccValue;
	private String poNumber;
	private String poValue;
	private String poReleaseDate;
	private String procurementType;
	private String receiptDate;
	private String contractStartDate;
	private String contractEndDate;
	
	public Integer getProcurementId() {
		return procurementId;
	}
	public void setProcurementId(Integer procurementId) {
		this.procurementId = procurementId;
	}
	public Integer getScOrderId() {
		return scOrderId;
	}
	public void setScOrderId(Integer scOrderId) {
		this.scOrderId = scOrderId;
	}
	public String getSolutionName() {
		return solutionName;
	}
	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}
	public String getCloudCode() {
		return cloudCode;
	}
	public void setCloudCode(String cloudCode) {
		this.cloudCode = cloudCode;
	}
	public String getWbsNumber() {
		return wbsNumber;
	}
	public void setWbsNumber(String wbsNumber) {
		this.wbsNumber = wbsNumber;
	}
	public String getWbsValue() {
		return wbsValue;
	}
	public void setWbsValue(String wbsValue) {
		this.wbsValue = wbsValue;
	}
	public String getGlccNumber() {
		return glccNumber;
	}
	public void setGlccNumber(String glccNumber) {
		this.glccNumber = glccNumber;
	}
	public String getGlccValue() {
		return glccValue;
	}
	public void setGlccValue(String glccValue) {
		this.glccValue = glccValue;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	public String getPoValue() {
		return poValue;
	}
	public void setPoValue(String poValue) {
		this.poValue = poValue;
	}
	public String getProcurementType() {
		return procurementType;
	}
	public void setProcurementType(String procurementType) {
		this.procurementType = procurementType;
	}
	public String getPoReleaseDate() {
		return poReleaseDate;
	}
	public void setPoReleaseDate(String poReleaseDate) {
		this.poReleaseDate = poReleaseDate;
	}
	public String getReceiptDate() {
		return receiptDate;
	}
	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}
	public String getContractStartDate() {
		return contractStartDate;
	}
	public void setContractStartDate(String contractStartDate) {
		this.contractStartDate = contractStartDate;
	}
	public String getContractEndDate() {
		return contractEndDate;
	}
	public void setContractEndDate(String contractEndDate) {
		this.contractEndDate = contractEndDate;
	}
	
	@Override
	public String toString() {
		return "ProcurementBean [scOrderId=" + scOrderId + ", solutionName=" + solutionName + ", cloudCode=" + cloudCode
				+ ", wbsNumber=" + wbsNumber + ", wbsValue=" + wbsValue + ", glccNumber=" + glccNumber + ", glccValue="
				+ glccValue + ", poNumber=" + poNumber + ", poValue=" + poValue + ", poReleaseDate=" + poReleaseDate
				+ ", procurementType=" + procurementType + ", receiptDate=" + receiptDate + ", contractStartDate="
				+ contractStartDate + ", contractEndDate=" + contractEndDate + "]";
	}
	
}
