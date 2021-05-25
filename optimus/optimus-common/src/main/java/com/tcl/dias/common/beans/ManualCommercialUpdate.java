package com.tcl.dias.common.beans;

import java.util.List;

public class ManualCommercialUpdate {
	private String quoteId;

	private String productName;

	private String approverLevel1;
	
	private String approverLevel2;
	
	private String approverLevel3;
	
	List<String> sites;

	List<String> links;
	
	private String discountApprovalLevel;

	public List<String> getLinks() {
		return links;
	}

	public void setLinks(List<String> links) {
		this.links = links;
	}

	public String getDiscountApprovalLevel() {
		return discountApprovalLevel;
	}

	public void setDiscountApprovalLevel(String discountApprovalLevel) {
		this.discountApprovalLevel = discountApprovalLevel;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getApproverLevel1() {
		return approverLevel1;
	}

	public void setApproverLevel1(String approverLevel1) {
		this.approverLevel1 = approverLevel1;
	}

	public String getApproverLevel2() {
		return approverLevel2;
	}

	public void setApproverLevel2(String approverLevel2) {
		this.approverLevel2 = approverLevel2;
	}

	public String getApproverLevel3() {
		return approverLevel3;
	}

	public void setApproverLevel3(String approverLevel3) {
		this.approverLevel3 = approverLevel3;
	}

	public List<String> getSites() {
		return sites;
	}

	public void setSites(List<String> sites) {
		this.sites = sites;
	}

	@Override
	public String toString() {
		return "ManualCommercialUpdate [quoteId=" + quoteId + ", productName=" + productName + ", approverLevel1="
				+ approverLevel1 + ", approverLevel2=" + approverLevel2 + ", approverLevel3=" + approverLevel3
				+ ", sites=" + sites + ", links=" + links + ", discountApprovalLevel=" + discountApprovalLevel + "]";
	}
	

}
