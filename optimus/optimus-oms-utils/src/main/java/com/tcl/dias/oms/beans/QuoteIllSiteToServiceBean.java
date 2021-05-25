package com.tcl.dias.oms.beans;


import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteToLe;

public class QuoteIllSiteToServiceBean {
	
	private Integer id;
	
	private String erfServiceInventoryTpsServiceId;
	
	private Integer erfServiceInventoryParentOrderId;
	
	private Integer quoteIllSiteId;
	
	private Integer erfServiceInventoryServiceDetailId;
	
	private Integer tpsSfdcParentOptyId;
	
	private String changeRequestSummary;
	
	private Integer quoteToLeId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getErfServiceInventoryTpsServiceId() {
		return erfServiceInventoryTpsServiceId;
	}

	public void setErfServiceInventoryTpsServiceId(String erfServiceInventoryTpsServiceId) {
		this.erfServiceInventoryTpsServiceId = erfServiceInventoryTpsServiceId;
	}

	public Integer getErfServiceInventoryParentOrderId() {
		return erfServiceInventoryParentOrderId;
	}

	public void setErfServiceInventoryParentOrderId(Integer erfServiceInventoryParentOrderId) {
		this.erfServiceInventoryParentOrderId = erfServiceInventoryParentOrderId;
	}


	public Integer getErfServiceInventoryServiceDetailId() {
		return erfServiceInventoryServiceDetailId;
	}

	public void setErfServiceInventoryServiceDetailId(Integer erfServiceInventoryServiceDetailId) {
		this.erfServiceInventoryServiceDetailId = erfServiceInventoryServiceDetailId;
	}

	public Integer getTpsSfdcParentOptyId() {
		return tpsSfdcParentOptyId;
	}

	public void setTpsSfdcParentOptyId(Integer tpsSfdcParentOptyId) {
		this.tpsSfdcParentOptyId = tpsSfdcParentOptyId;
	}

	public String getChangeRequestSummary() {
		return changeRequestSummary;
	}

	public void setChangeRequestSummary(String changeRequestSummary) {
		this.changeRequestSummary = changeRequestSummary;
	}

	public Integer getQuoteIllSiteId() {
		return quoteIllSiteId;
	}

	public void setQuoteIllSiteId(Integer quoteIllSiteId) {
		this.quoteIllSiteId = quoteIllSiteId;
	}

	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}

	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}

	@Override
	public String toString() {
		return "QuoteIllSiteToServiceBean [id=" + id + ", erfServiceInventoryTpsServiceId="
				+ erfServiceInventoryTpsServiceId + ", erfServiceInventoryParentOrderId="
				+ erfServiceInventoryParentOrderId + ", quoteIllSiteId=" + quoteIllSiteId
				+ ", erfServiceInventoryServiceDetailId=" + erfServiceInventoryServiceDetailId
				+ ", tpsSfdcParentOptyId=" + tpsSfdcParentOptyId + ", changeRequestSummary=" + changeRequestSummary
				+ ", quoteToLeId=" + quoteToLeId + "]";
	}

	
	
	

}
