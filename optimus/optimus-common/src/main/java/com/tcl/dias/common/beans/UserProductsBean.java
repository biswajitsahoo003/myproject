package com.tcl.dias.common.beans;

import java.io.Serializable;

/**
 * Bean for user inventory products
 * @author archchan
 *
 */
public class UserProductsBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String productName;
	private Integer productId;
	private String productCategory;
	private String productShortName;
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
	
	public String getProductShortName() {
		return productShortName;
	}
	public void setProductShortName(String productShortName) {
		this.productShortName = productShortName;
	}
	@Override
	public String toString() {
		return "UserProductsBean [productName=" + productName + ", productId=" + productId + ", productCategory="
				+ productCategory + ", productShortName=" + productShortName + "]";
	}

	
}
