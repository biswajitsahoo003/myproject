package com.tcl.dias.common.beans;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * This bean binds the product offering details
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ProductOfferingsBean implements Serializable{
		
	private String productOfferingsName;
	
	private String productOfferingsCode;
	
	private List<String> productOfferingsDescription;
	
	private List<AddonsBean> addons;
	
	private String mrc;
	
	private String nrc;
	
	private String action;
	
	private boolean isRecommended;

	public String getProductOfferingsName() {
		return productOfferingsName;
	}

	public void setProductOfferingsName(String productOfferingsName) {
		this.productOfferingsName = productOfferingsName;
	}

	public String getProductOfferingsCode() {
		return productOfferingsCode;
	}

	public void setProductOfferingsCode(String productOfferingsCode) {
		this.productOfferingsCode = productOfferingsCode;
	}

	public List<String> getProductOfferingsDescription() {
		return productOfferingsDescription;
	}

	public void setProductOfferingsDescription(List<String> productOfferingsDescription) {
		this.productOfferingsDescription = productOfferingsDescription;
	}

	public List<AddonsBean> getAddons() {
		return addons;
	}

	public void setAddons(List<AddonsBean> addons) {
		this.addons = addons;
	}

	public String getMrc() {
		return mrc;
	}

	public void setMrc(String mrc) {
		this.mrc = mrc;
	}

	public String getNrc() {
		return nrc;
	}

	public void setNrc(String nrc) {
		this.nrc = nrc;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public boolean getIsRecommended() {
		return isRecommended;
	}

	public void setIsRecommended(boolean isRecommended) {
		this.isRecommended = isRecommended;
	}
	
}
