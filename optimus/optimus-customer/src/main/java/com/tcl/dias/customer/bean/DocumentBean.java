package com.tcl.dias.customer.bean;

import java.util.List;


public class DocumentBean {
	
	private List<Integer> referenceId;
	private String referenceName;
	private String attachmentType;
	private String requestId;
	private Integer orderToLeId;
	private Integer quoteToLeId;
	private String url;
	
	public List<Integer> getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(List<Integer> referenceId) {
		this.referenceId = referenceId;
	}
	public String getReferenceName() {
		return referenceName;
	}
	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}
	public String getAttachmentType() {
		return attachmentType;
	}
	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public Integer getOrderToLeId() {
		return orderToLeId;
	}
	public void setOrderToLeId(Integer orderToLeId) {
		this.orderToLeId = orderToLeId;
	}
	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}
	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "DocumentBean [referenceId=" + referenceId + ", referenceName=" + referenceName + ", attachmentType="
				+ attachmentType + ", requestId=" + requestId + ", orderToLeId=" + orderToLeId + ", quoteToLeId="
				+ quoteToLeId + ", url=" + url + "]";
	}
	
	
	

}
