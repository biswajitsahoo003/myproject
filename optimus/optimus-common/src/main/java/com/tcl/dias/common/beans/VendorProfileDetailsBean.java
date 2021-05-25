package com.tcl.dias.common.beans;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * This bean class contains product offering details based on vendors 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class VendorProfileDetailsBean implements Serializable{
	public String vendor;
	public List<ProductOfferingsBean> productOfferingsBeans;
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public List<ProductOfferingsBean> getProductOfferingsBeans() {
		return productOfferingsBeans;
	}
	public void setProductOfferingsBeans(List<ProductOfferingsBean> productOfferingsBeans) {
		this.productOfferingsBeans = productOfferingsBeans;
	}
	
}
