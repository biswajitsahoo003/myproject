package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;


/**
 * SiteDetailsSearchRequest bean input for sdwan search
 * @author archchan
 *
 */
public class SiteDetailsSearchRequest implements  Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String searchText;
	private Integer size;
	private Integer page;
	private String sortBy;
	private String sortOrder;
	private Integer productId;
	private Integer customerId;
	private Integer customerLeId;
	private Integer partnerId;
	private String groupBy;
	
	public String getSearchText() {
		return searchText;
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public String getSortBy() {
		return sortBy;
	}
	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Integer getCustomerLeId() {
		return customerLeId;
	}
	public void setCustomerLeId(Integer customerLeId) {
		this.customerLeId = customerLeId;
	}
	public Integer getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	@Override
	public String toString() {
		return "SiteDetailsSearchRequest{" + "searchText='" + searchText + '\'' + ", size=" + size + ", page=" + page
				+ ", sortBy='" + sortBy + '\'' + ", sortOrder='" + sortOrder + '\'' + ", productId=" + productId
				+ ", customerId=" + customerId + ", customerLeId=" + customerLeId + ", partnerId=" + partnerId
				+ ", groupBy='" + groupBy + '\'' + '}';
	}
}
