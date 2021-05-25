package com.tcl.dias.oms.izosdwan.beans;

public class OrderSiteCategoryBean {

	private Integer id;
	private Integer erfLocSitebLocationId;
	private String siteCategory;
	private Integer quoteId;
	
	
	public OrderSiteCategoryBean() {
		super();
	}

	public OrderSiteCategoryBean(Integer id, Integer erfLocSitebLocationId, String siteCategory, Integer quoteId) {
		super();
		this.id = id;
		this.erfLocSitebLocationId = erfLocSitebLocationId;
		this.siteCategory = siteCategory;
		this.quoteId = quoteId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getErfLocSitebLocationId() {
		return erfLocSitebLocationId;
	}

	public void setErfLocSitebLocationId(Integer erfLocSitebLocationId) {
		this.erfLocSitebLocationId = erfLocSitebLocationId;
	}

	public String getSiteCategory() {
		return siteCategory;
	}

	public void setSiteCategory(String siteCategory) {
		this.siteCategory = siteCategory;
	}

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	@Override
	public String toString() {
		return "OrderSiteCategoryBean [id=" + id + ", erfLocSitebLocationId=" + erfLocSitebLocationId
				+ ", siteCategory=" + siteCategory + ", quoteId=" + quoteId + "]";
	}
	
}
