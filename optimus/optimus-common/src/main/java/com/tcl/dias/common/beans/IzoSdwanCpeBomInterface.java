package com.tcl.dias.common.beans;


/**
 * @author mpalanis
 *
 */

public class IzoSdwanCpeBomInterface {
	
	private String bomNameCd;
	
	private String physicalResourceCd;
	
	private String productCategory;
	
	private String interfaceType;
	
	private String description;
	
	private String provider;
	
	private String quantity;
	
	private String cpeModelEndOfSale;
	
	private String cpeModelEndOfLife;

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getProvider() {
		return provider;
	}

	public String getCpeModelEndOfSale() {
		return cpeModelEndOfSale;
	}

	public void setCpeModelEndOfSale(String cpeModelEndOfSale) {
		this.cpeModelEndOfSale = cpeModelEndOfSale;
	}

	public String getCpeModelEndOfLife() {
		return cpeModelEndOfLife;
	}

	public void setCpeModelEndOfLife(String cpeModelEndOfLife) {
		this.cpeModelEndOfLife = cpeModelEndOfLife;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getBomNameCd() {
		return bomNameCd;
	}

	public void setBomNameCd(String bomNameCd) {
		this.bomNameCd = bomNameCd;
	}

	public String getPhysicalResourceCd() {
		return physicalResourceCd;
	}

	public void setPhysicalResourceCd(String physicalResourceCd) {
		this.physicalResourceCd = physicalResourceCd;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}


	public String getInterfaceType() {
		return interfaceType;
	}

	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	

}
