package com.tcl.dias.oms.beans;

/**
 * TerminationWaiverDetailsBean file
 * 
 *
 * @author Veera Balasubramanian
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class TerminationWaiverDetailsBean {
	
	private String serviceId;
	
	private Double proposedEtc;
	
	private Double finalEtc; 
	
	private String etcWaiverType;
	
	private String etcWaiverPolicy;
	
	private String proposedBySales;
	
	private String waiverApprovalRemarks;
	 
	private String waiverRemarks;
	
	private String compensatoryDetails;

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Double getProposedEtc() {
		return proposedEtc;
	}

	public void setProposedEtc(Double proposedEtc) {
		this.proposedEtc = proposedEtc;
	}

	public Double getFinalEtc() {
		return finalEtc;
	}

	public void setFinalEtc(Double finalEtc) {
		this.finalEtc = finalEtc;
	}
	
	public String getEtcWaiverType() {
		return etcWaiverType;
	}

	public void setEtcWaiverType(String etcWaiverType) {
		this.etcWaiverType = etcWaiverType;
	}

	public String getEtcWaiverPolicy() {
		return etcWaiverPolicy;
	}

	public void setEtcWaiverPolicy(String etcWaiverPolicy) {
		this.etcWaiverPolicy = etcWaiverPolicy;
	}

	public String getProposedBySales() {
		return proposedBySales;
	}

	public void setProposedBySales(String proposedBySales) {
		this.proposedBySales = proposedBySales;
	}

	public String getWaiverApprovalRemarks() {
		return waiverApprovalRemarks;
	}

	public void setWaiverApprovalRemarks(String etcRemarks) {
		this.waiverApprovalRemarks = etcRemarks;
	}

	public String getWaiverRemarks() {
		return waiverRemarks;
	}

	public void setWaiverRemarks(String waiverRemarks) {
		this.waiverRemarks = waiverRemarks;
	}
	
	public String getCompensatoryDetails() {
		return compensatoryDetails;
	}

	public void setCompensatoryDetails(String compensatoryDetails) {
		this.compensatoryDetails = compensatoryDetails;
	}

	@Override
	public String toString() {
		return "TerminationWaiverDetailsBean [serviceId=" + serviceId + ", proposedEtc=" + proposedEtc + ", finalEtc="
				+ finalEtc + ", etcWaiverType=" + etcWaiverType + ", etcWaiverPolicy=" + etcWaiverPolicy
				+ ", proposedBySales=" + proposedBySales + ", waiverApprovalRemarks=" + waiverApprovalRemarks + ", waiverRemarks="
				+ waiverRemarks + ", compensatoryDetails=" + compensatoryDetails + "]";
	}

}
