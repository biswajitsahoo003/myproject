package com.tcl.dias.oms.beans;

import java.io.Serializable;

public class QuoteComponentAttributeUpdateRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private String quoteCode;
	private Integer siteId;
	private Integer attributeId;
	private String attributeValue;
	private String type;
	public String getQuoteCode() {
		return quoteCode;
	}
	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public Integer getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(Integer attributeId) {
		this.attributeId = attributeId;
	}
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "QuoteComponentAttributeUpdateRequest [quoteCode=" + quoteCode + ", siteId=" + siteId + ", attributeId="
				+ attributeId + ", attributeValue=" + attributeValue + ", type=" + type + "]";
	}	

}
