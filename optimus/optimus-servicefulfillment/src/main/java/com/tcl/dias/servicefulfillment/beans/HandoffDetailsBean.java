package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;

/**
 * This class is used to define attributes in handoff details for track and complete Lm Delivery
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class HandoffDetailsBean extends BaseRequest{

	private String nniId;
	private String customerInnerVlan;
	private String nniTimeSlotKlm;
	private String klmPopEnd;
	private String au4;
	private String customerEndHandoff;
	private String customerEndPort;
	private String lmTestResults;
	private String HandoverType;
	private String offnetLmDeviationRemarks;
	private String offnetLmDeviationApprovals;
	private String providerReferenceId;
	private List<AttachmentIdBean> documentIds;
	private String supplierMuxInterface;
	private String supplierMuxPort;
	private String supplierMuxDdf;
	private String cloudName;
	private String iorId;
	private String klm;

	private String mastDeviation;
	private String powerCordLengthDeviation;
	private String slaDeviation;
	private String delayReason;
	
	private String remarks;
	
	

	
	
	
	
	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDelayReason() {
		return delayReason;
	}

	public void setDelayReason(String delayReason) {
		this.delayReason = delayReason;
	}

	public String getMastDeviation() {
		return mastDeviation;
	}

	public void setMastDeviation(String mastDeviation) {
		this.mastDeviation = mastDeviation;
	}

	public String getPowerCordLengthDeviation() {
		return powerCordLengthDeviation;
	}

	public void setPowerCordLengthDeviation(String powerCordLengthDeviation) {
		this.powerCordLengthDeviation = powerCordLengthDeviation;
	}

	public String getSlaDeviation() {
		return slaDeviation;
	}

	public void setSlaDeviation(String slaDeviation) {
		this.slaDeviation = slaDeviation;
	}

	public String getNniId() {
		return nniId;
	}

	public void setNniId(String nniId) {
		this.nniId = nniId;
	}

	public String getCustomerInnerVlan() {
		return customerInnerVlan;
	}

	public void setCustomerInnerVlan(String customerInnerVlan) {
		this.customerInnerVlan = customerInnerVlan;
	}

	public String getNniTimeSlotKlm() {
		return nniTimeSlotKlm;
	}

	public void setNniTimeSlotKlm(String nniTimeSlotKlm) {
		this.nniTimeSlotKlm = nniTimeSlotKlm;
	}

	public String getKlmPopEnd() {
		return klmPopEnd;
	}

	public void setKlmPopEnd(String klmPopEnd) {
		this.klmPopEnd = klmPopEnd;
	}

	public String getAu4() {
		return au4;
	}

	public void setAu4(String au4) {
		this.au4 = au4;
	}

	public String getCustomerEndHandoff() {
		return customerEndHandoff;
	}

	public void setCustomerEndHandoff(String customerEndHandoff) {
		this.customerEndHandoff = customerEndHandoff;
	}

	public String getCustomerEndPort() {
		return customerEndPort;
	}

	public void setCustomerEndPort(String customerEndPort) {
		this.customerEndPort = customerEndPort;
	}

	public String getLmTestResults() {
		return lmTestResults;
	}

	public void setLmTestResults(String lmTestResults) {
		this.lmTestResults = lmTestResults;
	}

	public String getHandoverType() {
		return HandoverType;
	}

	public void setHandoverType(String handoverType) {
		HandoverType = handoverType;
	}

	public String getOffnetLmDeviationRemarks() {
		return offnetLmDeviationRemarks;
	}

	public void setOffnetLmDeviationRemarks(String offnetLmDeviationRemarks) {
		this.offnetLmDeviationRemarks = offnetLmDeviationRemarks;
	}

	public String getOffnetLmDeviationApprovals() {
		return offnetLmDeviationApprovals;
	}

	public void setOffnetLmDeviationApprovals(String offnetLmDeviationApprovals) {
		this.offnetLmDeviationApprovals = offnetLmDeviationApprovals;
	}

	public String getProviderReferenceId() {
		return providerReferenceId;
	}

	public void setProviderReferenceId(String providerReferenceId) {
		this.providerReferenceId = providerReferenceId;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getSupplierMuxInterface() {
		return supplierMuxInterface;
	}

	public void setSupplierMuxInterface(String supplierMuxInterface) {
		this.supplierMuxInterface = supplierMuxInterface;
	}

	public String getSupplierMuxPort() {
		return supplierMuxPort;
	}

	public void setSupplierMuxPort(String supplierMuxPort) {
		this.supplierMuxPort = supplierMuxPort;
	}

	public String getSupplierMuxDdf() {
		return supplierMuxDdf;
	}

	public void setSupplierMuxDdf(String supplierMuxDdf) {
		this.supplierMuxDdf = supplierMuxDdf;
	}

	public String getCloudName() {
		return cloudName;
	}

	public void setCloudName(String cloudName) {
		this.cloudName = cloudName;
	}

	public String getIorId() {
		return iorId;
	}

	public void setIorId(String iorId) {
		this.iorId = iorId;
	}

	public String getKlm() {
		return klm;
	}

	public void setKlm(String klm) {
		this.klm = klm;
	}
	
	

}
