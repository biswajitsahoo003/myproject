package com.tcl.dias.servicefulfillmentutils.beans;



import java.util.List;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class TerminateBackhaulPo extends BaseRequest {
	
	private String offnetTerminationDate;
	private String isSupplierEtcAvailable;
	private String supplierEtcCharges;
	private List<AttachmentIdBean> documentIds;
	
	private boolean isTermination;
	
	private String terminationEffectiveDate;
	
	
	
	
	
	
	/**
	 * @return the terminationEffectiveDate
	 */
	public String getTerminationEffectiveDate() {
		return terminationEffectiveDate;
	}
	/**
	 * @param terminationEffectiveDate the terminationEffectiveDate to set
	 */
	public void setTerminationEffectiveDate(String terminationEffectiveDate) {
		this.terminationEffectiveDate = terminationEffectiveDate;
	}
	/**
	 * @return the isTermination
	 */
	public boolean isTermination() {
		return isTermination;
	}
	/**
	 * @param isTermination the isTermination to set
	 */
	public void setTermination(boolean isTermination) {
		this.isTermination = isTermination;
	}
	public String getOffnetTerminationDate() {
		return offnetTerminationDate;
	}
	public void setOffnetTerminationDate(String offnetTerminationDate) {
		this.offnetTerminationDate = offnetTerminationDate;
	}
	public String getIsSupplierEtcAvailable() {
		return isSupplierEtcAvailable;
	}
	public void setIsSupplierEtcAvailable(String isSupplierEtcAvailable) {
		this.isSupplierEtcAvailable = isSupplierEtcAvailable;
	}
	public String getSupplierEtcCharges() {
		return supplierEtcCharges;
	}
	public void setSupplierEtcCharges(String supplierEtcCharges) {
		this.supplierEtcCharges = supplierEtcCharges;
	}
	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}
	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
}
