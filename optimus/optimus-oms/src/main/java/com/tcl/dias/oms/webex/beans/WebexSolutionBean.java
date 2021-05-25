package com.tcl.dias.oms.webex.beans;

import java.util.List;

/**
 * WebexSolutionBean holds the solution details of webex product.
 * 
 * @author arjayapa
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class WebexSolutionBean {

	private Integer solutionId;
	private String offeringName;
	private String solutionCode;
	private String productName;
	private String dealId;
	private String response;
	private List<QuoteUcaasBean> ucaasQuotes;
	private CiscoDealAttributesBean dealAttributes;
	private String status;
	private String message;
	private String contractPeriod;

	public Integer getSolutionId() {
		return solutionId;
	}

	public void setSolutionId(Integer solutionId) {
		this.solutionId = solutionId;
	}

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public String getSolutionCode() {
		return solutionCode;
	}

	public void setSolutionCode(String solutionCode) {
		this.solutionCode = solutionCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getDealId() {
		return dealId;
	}

	public void setDealId(String dealId) {
		this.dealId = dealId;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public List<QuoteUcaasBean> getUcaasQuotes() {
		return ucaasQuotes;
	}

	public void setUcaasQuotes(List<QuoteUcaasBean> ucaasQuotes) {
		this.ucaasQuotes = ucaasQuotes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getContractPeriod() {
		return contractPeriod;
	}

	public void setContractPeriod(String contractPeriod) {
		this.contractPeriod = contractPeriod;
	}

	public CiscoDealAttributesBean getDealAttributes() {
		return dealAttributes;
	}

	public void setDealAttributes(CiscoDealAttributesBean dealAttributes) {
		this.dealAttributes = dealAttributes;
	}

	@Override
	public String toString() {
		return "WebexSolutionBean [solutionId=" + solutionId + ", offeringName=" + offeringName + ", solutionCode="
				+ solutionCode + ", productName=" + productName + ", dealId=" + dealId + ", response=" + response
				+ ", ucaasQuotes=" + ucaasQuotes + ", dealAttributes=" + dealAttributes + ", status=" + status + ", message=" + message + "contractPeriod="
				+ contractPeriod + "]";
	}
}
