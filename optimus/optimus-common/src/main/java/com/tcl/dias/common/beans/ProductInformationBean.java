package com.tcl.dias.common.beans;
/**
 * This bean gives the Service count based on product
 * @author ANANDHI VIJAY
 *
 */
public class ProductInformationBean {
	
	private Integer productId;
	private String productName;
	private Integer count;
	private Integer networkProductCounts;
	private String productDesc;
	private String isMacdEnabledFlag;
	private String productShortName;
	
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getNetworkProductCounts() {
		return networkProductCounts;
	}
	public void setNetworkProductCounts(Integer networkProductCounts) {
		this.networkProductCounts = networkProductCounts;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getIsMacdEnabledFlag() {
		return isMacdEnabledFlag;
	}
	public void setIsMacdEnabledFlag(String isMacdEnabledFlag) {
		this.isMacdEnabledFlag = isMacdEnabledFlag;
	}
	public String getProductShortName() {
		return productShortName;
	}
	public void setProductShortName(String productShortName) {
		this.productShortName = productShortName;
	}

}
