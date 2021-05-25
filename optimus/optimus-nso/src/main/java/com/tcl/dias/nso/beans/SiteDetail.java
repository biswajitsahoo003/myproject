/**
 * 
 */
package com.tcl.dias.nso.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.nso.beans.ComponentDetail;

/**
 * @author KarMani
 *
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

	@Override
	public String toString() {
		return "SiteDetail [siteId=" + siteId + ", locationId=" + locationId + ", locationCode=" + locationCode
				+ ", secondLocationId=" + secondLocationId + ", secondLocationCode=" + secondLocationCode
				+ ", components=" + components + "]";
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

	

}

