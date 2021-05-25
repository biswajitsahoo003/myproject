package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerOdrCreationBean {

	private String ownerType;
	private Integer customerId;
	private Integer ownerEntityId;
	private String contractServiceAbbr;
	private String solutionName;
	private String solutionId;
	private String serviceTypeCode;
	private Integer productNumber;
	private List<String> tags;
	private List<CustRequestsBean> custRequests;

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getOwnerEntityId() {
		return ownerEntityId;
	}

	public void setOwnerEntityId(Integer ownerEntityId) {
		this.ownerEntityId = ownerEntityId;
	}

	public String getContractServiceAbbr() {
		return contractServiceAbbr;
	}

	public void setContractServiceAbbr(String contractServiceAbbr) {
		this.contractServiceAbbr = contractServiceAbbr;
	}

	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	public String getSolutionId() {
		return solutionId;
	}

	public void setSolutionId(String solutionId) {
		this.solutionId = solutionId;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<CustRequestsBean> getCustRequests() {
		return custRequests;
	}

	public void setCustRequests(List<CustRequestsBean> custRequests) {
		this.custRequests = custRequests;
	}

	public String getServiceTypeCode() {
		return serviceTypeCode;
	}

	public Integer getProductNumber() {
		return productNumber;
	}

	public void setServiceTypeCode(String serviceTypeCode) {
		this.serviceTypeCode = serviceTypeCode;
	}

	public void setProductNumber(Integer productNumber) {
		this.productNumber = productNumber;
	}
}
