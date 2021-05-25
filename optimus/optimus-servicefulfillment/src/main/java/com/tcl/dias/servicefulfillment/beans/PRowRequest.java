package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited used for prow request
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PRowRequest extends TaskDetailsBaseBean {
	private String buildingAuthorityName;
	private String buildingAuthorityAddress;
	private String buildingAuthorityContactNumber;
	private String buildingAuthorityEmailId;
	private String contractRequired;
	private List<AttachmentIdBean> documentIds;
	private String buildingAuthorityVendorId;
	private Double oneTimeCharge;
	private Double recurringFixedCharge;
	private Double recurringVariableCharge;
	private Double taxes;
	private String bankGuarantee;
	private Double securityDeposit;


	public String getBuildingAuthorityName() {
		return buildingAuthorityName;
	}

	public void setBuildingAuthorityName(String buildingAuthorityName) {
		this.buildingAuthorityName = buildingAuthorityName;
	}

	public String getBuildingAuthorityAddress() {
		return buildingAuthorityAddress;
	}

	public void setBuildingAuthorityAddress(String buildingAuthorityAddress) {
		this.buildingAuthorityAddress = buildingAuthorityAddress;
	}

	public String getBuildingAuthorityContactNumber() {
		return buildingAuthorityContactNumber;
	}

	public void setBuildingAuthorityContactNumber(String buildingAuthorityContactNumber) {
		this.buildingAuthorityContactNumber = buildingAuthorityContactNumber;
	}

	public String getContractRequired() {
		return contractRequired;
	}

	public void setContractRequired(String contractRequired) {
		this.contractRequired = contractRequired;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}


	public Double getOneTimeCharge() {
		return oneTimeCharge;
	}

	public void setOneTimeCharge(Double oneTimeCharge) {
		this.oneTimeCharge = oneTimeCharge;
	}

	public Double getRecurringFixedCharge() {
		return recurringFixedCharge;
	}

	public void setRecurringFixedCharge(Double recurringFixedCharge) {
		this.recurringFixedCharge = recurringFixedCharge;
	}

	public Double getRecurringVariableCharge() {
		return recurringVariableCharge;
	}

	public void setRecurringVariableCharge(Double recurringVariableCharge) {
		this.recurringVariableCharge = recurringVariableCharge;
	}

	public Double getTaxes() {
		return taxes;
	}

	public void setTaxes(Double taxes) {
		this.taxes = taxes;
	}

	public String getBankGuarantee() {
		return bankGuarantee;
	}

	public void setBankGuarantee(String bankGuarantee) {
		this.bankGuarantee = bankGuarantee;
	}

	public Double getSecurityDeposit() {
		return securityDeposit;
	}

	public void setSecurityDeposit(Double securityDeposit) {
		this.securityDeposit = securityDeposit;
	}

	public String getBuildingAuthorityVendorId() {
		return buildingAuthorityVendorId;
	}

	public void setBuildingAuthorityVendorId(String buildingAuthorityVendorId) {
		this.buildingAuthorityVendorId = buildingAuthorityVendorId;
	}

	public String getBuildingAuthorityEmailId() {
		return buildingAuthorityEmailId;
	}

	public void setBuildingAuthorityEmailId(String buildingAuthorityEmailId) {
		this.buildingAuthorityEmailId = buildingAuthorityEmailId;
	}
	

}
