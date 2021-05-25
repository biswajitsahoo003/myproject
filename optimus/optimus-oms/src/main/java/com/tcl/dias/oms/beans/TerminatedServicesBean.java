package com.tcl.dias.oms.beans;

import java.util.Date;
	
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * TerminatedServicesBean file
 * 
 *
 * @author Veera Balasubramanian
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TerminatedServicesBean {

	private Integer quoteId;
	private Integer quoteToLeId;
	private String serviceId;
	private String quoteCode;
	private String quoteStage;
	private Date quoteCreatedTime;
	public Integer getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}
	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}
	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}
	public String getQuoteCode() {
		return quoteCode;
	}
	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getQuoteStage() {
		return quoteStage;
	}
	public void setQuoteStage(String quoteStage) {
		this.quoteStage = quoteStage;
	}
	public Date getQuoteCreatedTime() {
		return quoteCreatedTime;
	}
	public void setQuoteCreatedTime(Date quoteCreatedTime) {
		this.quoteCreatedTime = quoteCreatedTime;
	}
	@Override
	public String toString() {
		return "TerminatedServicesBean [quoteId=" + quoteId + ", quoteToLeId=" + quoteToLeId + ", quoteCode="
				+ quoteCode + ", serviceId=" + serviceId + ", quoteStage=" + quoteStage + ", quoteCreatedTime="
				+ quoteCreatedTime + "]";
	}
	
}
	