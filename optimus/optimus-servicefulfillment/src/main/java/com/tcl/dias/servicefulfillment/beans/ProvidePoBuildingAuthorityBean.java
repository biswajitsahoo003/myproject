package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This class is used to Provide PO to Building Authority
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProvidePoBuildingAuthorityBean extends TaskDetailsBaseBean {

	private List<AttachmentIdBean> documentIds;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private String contractEndDate;
	private Double oneTimeCharge;
	private Double recurringFixedCharge;
	private Double recurringVariableCharge;
	private Double taxes;
	private String bankGuarantee;
	private Double securityDeposit;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private String contractStartDate;

	private String invoiceNumber;

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
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

	public String getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(String contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public String getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(String contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

}
