package com.tcl.dias.products.gsc.beans;

import java.util.List;

/**
 * Country level service Bean for GSC Products
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscServiceMatrixBean {

	private String productName;
	private String countryName;
	private List<GscServiceMatrixAttributeBean> restrictions;
	private List<GscServiceMatrixAttributeBean> accessibility;
	private List<GscServiceMatrixAttributeBean> comments;
	private List<GscServiceMatrixAttributeBean> others;
	private List<List<GscServiceMatrixAttributeBean>> regions;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public List<GscServiceMatrixAttributeBean> getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(List<GscServiceMatrixAttributeBean> restrictions) {
		this.restrictions = restrictions;
	}

	public List<GscServiceMatrixAttributeBean> getAccessibility() {
		return accessibility;
	}

	public void setAccessibility(List<GscServiceMatrixAttributeBean> accessibility) {
		this.accessibility = accessibility;
	}

	public List<GscServiceMatrixAttributeBean> getComments() {
		return comments;
	}

	public void setComments(List<GscServiceMatrixAttributeBean> comments) {
		this.comments = comments;
	}

	public List<GscServiceMatrixAttributeBean> getOthers() {
		return others;
	}

	public void setOthers(List<GscServiceMatrixAttributeBean> others) {
		this.others = others;
	}

	public List<List<GscServiceMatrixAttributeBean>> getRegions() {
		return regions;
	}

	public void setRegions(List<List<GscServiceMatrixAttributeBean>> regions) {
		this.regions = regions;
	}
}
