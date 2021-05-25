package com.tcl.dias.oms.npl.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.npl.constants.SiteTypeConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.beans.ComponentDetail;

/**
 * This file contains the NplSiteDetail.java class. Bean class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class NplSiteDetail implements Serializable {

	private static final long serialVersionUID = -7410988290187762659L;
	private Integer siteId;
	private Integer locationId;
	private String locationCode;
	private Integer secondLocationId;
	private String secondLocationCode;
	private List<ComponentDetail> components = new ArrayList<>();
	private Boolean siteChangeflag = false;
	private Integer erfServiceInventoryServiceDetailId;
	private String erfServiceInventoryTpsServiceId;
	private Integer erfServiceInventoryParentOrderId;
	private Integer tpsSfdcParentOptyId;
	private String siteType;
	private SiteTypeConstants type;
	
	
	
	


	public SiteTypeConstants getType() {
		return type;
	}

	public void setType(SiteTypeConstants type) {
		this.type = type;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public Boolean getSiteChangeflag() { return siteChangeflag; }

	public void setSiteChangeflag(Boolean siteChangeflag) { this.siteChangeflag = siteChangeflag; }

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

	public Integer getErfServiceInventoryServiceDetailId() {
		return erfServiceInventoryServiceDetailId;
	}

	public void setErfServiceInventoryServiceDetailId(Integer erfServiceInventoryServiceDetailId) {
		this.erfServiceInventoryServiceDetailId = erfServiceInventoryServiceDetailId;
	}

	public String getErfServiceInventoryTpsServiceId() {
		return erfServiceInventoryTpsServiceId;
	}

	public void setErfServiceInventoryTpsServiceId(String erfServiceInventoryTpsServiceId) {
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
		return "NplSiteDetail{" +
				"siteId=" + siteId +
				", locationId=" + locationId +
				", locationCode='" + locationCode + '\'' +
				", secondLocationId=" + secondLocationId +
				", secondLocationCode='" + secondLocationCode + '\'' +
				", components=" + components +
				", siteChangeflag=" + siteChangeflag +
				", erfServiceInventoryServiceDetailId=" + erfServiceInventoryServiceDetailId +
				", erfServiceInventoryTpsServiceId='" + erfServiceInventoryTpsServiceId + '\'' +
				", erfServiceInventoryParentOrderId=" + erfServiceInventoryParentOrderId +
				", tpsSfdcParentOptyId=" + tpsSfdcParentOptyId +
				'}';
	}
}
