package com.tcl.dias.oms.gde.beans;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.beans.ComponentDetail;

@JsonInclude(Include.NON_NULL)
public class GdeSiteDetail {
	
	private Integer siteId;
	private Integer locationId;
	private String locationCode;
	private Integer secondLocationId;
	private String secondLocationCode;
	private List<ComponentDetail> components = new ArrayList<>();
	private Integer erfServiceInventoryServiceDetailId;
	private String erfServiceInventoryTpsServiceId;
	private Integer erfServiceInventoryParentOrderId;
	private Integer tpsSfdcParentOptyId;
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
	public List<ComponentDetail> getComponents() {
		return components;
	}
	public void setComponents(List<ComponentDetail> components) {
		this.components = components;
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
		return "GdeSiteDetail [siteId=" + siteId + ", locationId=" + locationId + ", locationCode=" + locationCode
				+ ", secondLocationId=" + secondLocationId + ", secondLocationCode=" + secondLocationCode
				+ ", components=" + components
				+ ", erfServiceInventoryServiceDetailId=" + erfServiceInventoryServiceDetailId
				+ ", erfServiceInventoryTpsServiceId=" + erfServiceInventoryTpsServiceId
				+ ", erfServiceInventoryParentOrderId=" + erfServiceInventoryParentOrderId + ", tpsSfdcParentOptyId="
				+ tpsSfdcParentOptyId + "]";
	}
	
	

}
