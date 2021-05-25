package com.tcl.dias.oms.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This file contains the SiteDetail.java class. Bean class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class SiteDetail implements Serializable {

	private static final long serialVersionUID = -7410988290187762659L;
	private Integer siteId;
	private String siteCode;
	private Integer locationId;
	private String locationCode;
	private Integer secondLocationId;
	private String secondLocationCode;
	private List<ComponentDetail> components = new ArrayList<>();
	private Integer erfServiceInventoryServiceDetailId;
	private List<String> erfServiceInventoryTpsServiceId;
	private Integer erfServiceInventoryParentOrderId;
	private Integer tpsSfdcParentOptyId;
	
	private Integer linkId;
	private String linkCode;
	
	
	
	
	public Integer getLinkId() {
		return linkId;
	}
	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}
	public String getLinkCode() {
		return linkCode;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public Integer getLocationId() {
		return locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public List<ComponentDetail> getComponents() {
		return components;
	}

	public void setComponents(List<ComponentDetail> components) {
		this.components = components;
	}

	public Integer getSecondLocationId() {
		return secondLocationId;
	}

	public void setSecondLocationId(Integer secondLocationId) {
		this.secondLocationId = secondLocationId;
	}

	public String getSecondLocationCode() {
		return secondLocationCode;
	}

	public void setSecondLocationCode(String secondLocationCode) {
		this.secondLocationCode = secondLocationCode;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public Integer getErfServiceInventoryServiceDetailId() {
		return erfServiceInventoryServiceDetailId;
	}

	public void setErfServiceInventoryServiceDetailId(Integer erfServiceInventoryServiceDetailId) {
		this.erfServiceInventoryServiceDetailId = erfServiceInventoryServiceDetailId;
	}

	public List<String> getErfServiceInventoryTpsServiceId() {
		return erfServiceInventoryTpsServiceId;
	}

	public void setErfServiceInventoryTpsServiceId(List<String> erfServiceInventoryTpsServiceId) {
		this.erfServiceInventoryTpsServiceId = erfServiceInventoryTpsServiceId;
	}

	public Integer getErfServiceInventoryParentOrderId() {
		return erfServiceInventoryParentOrderId;
	}

	public void setErfServiceInventoryParentOrderId(Integer erfServiceInventoryParentOrderId) {
		this.erfServiceInventoryParentOrderId = erfServiceInventoryParentOrderId;
	}

	public Integer getTpsSfdcParentOptyId() {
		return tpsSfdcParentOptyId;
	}

	public void setTpsSfdcParentOptyId(Integer tpsSfdcParentOptyId) {
		this.tpsSfdcParentOptyId = tpsSfdcParentOptyId;
	}

	@Override
	public String toString() {
		return "SiteDetail [siteId=" + siteId + ", siteCode=" + siteCode + ", locationId=" + locationId
				+ ", locationCode=" + locationCode + ", secondLocationId=" + secondLocationId + ", secondLocationCode="
				+ secondLocationCode + ", components=" + components + ", erfServiceInventoryServiceDetailId="
				+ erfServiceInventoryServiceDetailId + ", erfServiceInventoryTpsServiceId="
				+ erfServiceInventoryTpsServiceId + ", erfServiceInventoryParentOrderId="
				+ erfServiceInventoryParentOrderId + ", tpsSfdcParentOptyId=" + tpsSfdcParentOptyId + "]";
	}
	

}
