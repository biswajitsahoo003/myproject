package com.tcl.dias.oms.beans;

import java.util.Date;
	
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * TerminationQuoteDetailsBean file
 * 
 *
 * @author Veera Balasubramanian
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TerminationQuoteDetailsBean {

	private Integer quoteId;
	private Integer quoteToLeId;
	private String quoteCode;
	private String tpsSFDCOptyId;
	private String quoteCategory;
	private String quoteStage;
	private Date quoteCreatedTime;
	private Date terminationCreatedTime;
	private Date o2cCallInitiatedTime;
	private String salesTaskResponse;
	
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
	public String getTpsSFDCOptyId() {
		return tpsSFDCOptyId;
	}
	public void setTpsSFDCOptyId(String tpsSFDCOptyId) {
		this.tpsSFDCOptyId = tpsSFDCOptyId;
	}
	public String getQuoteCategory() {
		return quoteCategory;
	}
	public void setQuoteCategory(String quoteCategory) {
		this.quoteCategory = quoteCategory;
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
	public Date getTerminationCreatedTime() {
		return terminationCreatedTime;
	}
	public void setTerminationCreatedTime(Date terminationCreatedTime) {
		this.terminationCreatedTime = terminationCreatedTime;
	}
	public Date getO2cCallInitiatedTime() {
		return o2cCallInitiatedTime;
	}
	public void setO2cCallInitiatedTime(Date o2cCallInitiatedTime) {
		this.o2cCallInitiatedTime = o2cCallInitiatedTime;
	}
	public String getSalesTaskResponse() {
		return salesTaskResponse;
	}
	public void setSalesTaskResponse(String salesTaskResponse) {
		this.salesTaskResponse = salesTaskResponse;
	}
	@Override
	public String toString() {
		return "TerminationQuoteDetailsBean [quoteId=" + quoteId + ", quoteToLeId=" + quoteToLeId + ", quoteCode="
				+ quoteCode + ", tpsSFDCOptyId=" + tpsSFDCOptyId + ", quoteCategory=" + quoteCategory + ", quoteStage="
				+ quoteStage + ", quoteCreatedTime=" + quoteCreatedTime + ", terminationCreatedTime="
				+ terminationCreatedTime + ", o2cCallInitiatedTime=" + o2cCallInitiatedTime + ", salesTaskResponse="
				+ salesTaskResponse + "]";
	}
	
}
