package com.tcl.dias.productcatelog.entity.entities;


import java.io.Serializable;
import javax.persistence.*;

import com.tcl.dias.productcatelog.entity.base.BaseEntity;




/**
 * 
 * The persistent class for the service_area_matrix_India_GVPN database table.
 * 
 */
@Entity
@Table(name="service_area_matrix_GVPN")
@NamedQuery(name="ServiceAreaMatrixGVPN.findAll", query="SELECT s FROM ServiceAreaMatrixGVPN s")
public class ServiceAreaMatrixGVPN extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name="location_id")
	private String locationId;
	
	@Column(name="region_nm")
	private String regionNm;
	
	@Column(name="state_nm")
	private String stateNm;
	
	@Column(name="city_nm")
	private String cityNm;
	
	@Column(name="location_details")
	private String locationDetails;
	
	@Column(name="floor_details")
	private String floorDetails;
	
	@Column(name="Parent_PoP_Details")
	private String parentPoPDetails;
	
	@Column(name="pop_type")
	private String popType;
	
	@Column(name="dual_pop")
	private String dualPop;
	
	@Column(name="pop_tier_cd")
	private String popTierCd;
	
	@Column(name="pop_sts")
	private String popSts;
	
	@Column(name="is_dual_pop_ind")
	private String isDualPopInd;
	
	@Column(name="country_dtl")
	private String country_dtl;
	
	@Column(name="Site_Code")
	private String siteCode;
	
	public ServiceAreaMatrixGVPN (){
		// do nothing
	}


	public String getLocationId() {
		return locationId;
	}


	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}


	public String getRegionNm() {
		return regionNm;
	}

	public String getStateNm() {
		return stateNm;
	}


	public String getLocationDetails() {
		return locationDetails;
	}


	public String getFloorDetails() {
		return floorDetails;
	}


	public String getParentPoPDetails() {
		return parentPoPDetails;
	}


	public String getPopType() {
		return popType;
	}


	public String getDualPop() {
		return dualPop;
	}


	public String getPopTierCd() {
		return popTierCd;
	}


	public String getPopSts() {
		return popSts;
	}


	public String getIsDualPopInd() {
		return isDualPopInd;
	}


	public String getCountry_dtl() {
		return country_dtl;
	}


	public String getSiteCode() {
		return siteCode;
	}


	public void setRegionNm(String regionNm) {
		this.regionNm = regionNm;
	}


	public void setStateNm(String stateNm) {
		this.stateNm = stateNm;
	}


	public void setLocationDetails(String locationDetails) {
		this.locationDetails = locationDetails;
	}


	public String getCityNm() {
		return cityNm;
	}


	public void setCityNm(String cityNm) {
		this.cityNm = cityNm;
	}


	public void setFloorDetails(String floorDetails) {
		this.floorDetails = floorDetails;
	}


	public void setParentPoPDetails(String parentPoPDetails) {
		this.parentPoPDetails = parentPoPDetails;
	}


	public void setPopType(String popType) {
		this.popType = popType;
	}


	public void setDualPop(String dualPop) {
		this.dualPop = dualPop;
	}


	public void setPopTierCd(String popTierCd) {
		this.popTierCd = popTierCd;
	}


	public void setPopSts(String popSts) {
		this.popSts = popSts;
	}


	public void setIsDualPopInd(String isDualPopInd) {
		this.isDualPopInd = isDualPopInd;
	}


	public void setCountry_dtl(String country_dtl) {
		this.country_dtl = country_dtl;
	}


	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	
}