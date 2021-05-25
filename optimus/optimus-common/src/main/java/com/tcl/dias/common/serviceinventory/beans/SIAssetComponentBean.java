package com.tcl.dias.common.serviceinventory.beans;


/**
 * Bean class to contain Service Inventory asset data
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 *
 */
public class SIAssetComponentBean {
	
	private String name;
	
	private String value;
	
	private String category;
	
	private Double mrc;
	
	private Double nrc;
	
	private Double arc;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}
	
}
