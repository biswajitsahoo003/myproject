package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * This file contains the SEASiteInfoBean.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SEASiteInfoBean implements Serializable{
	
	private Integer totalCount;
	private Integer mappedCount;
	private Integer unMappedCount;
    private List<String> mappedBreakupLocation;
    private List<Integer> locationsIds;
    private List<SEAMappedSiteDetailsBean> mappedSiteDetails;
    private List<SEASiteDetailsBean> unMappedSiteDetails;
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getMappedCount() {
		return mappedCount;
	}
	public void setMappedCount(Integer mappedCount) {
		this.mappedCount = mappedCount;
	}
	public Integer getUnMappedCount() {
		return unMappedCount;
	}
	public void setUnMappedCount(Integer unMappedCount) {
		this.unMappedCount = unMappedCount;
	}
	public List<String> getMappedBreakupLocation() {
		return mappedBreakupLocation;
	}
	public void setMappedBreakupLocation(List<String> mappedBreakupLocation) {
		this.mappedBreakupLocation = mappedBreakupLocation;
	}
	public List<Integer> getLocationsIds() {
		return locationsIds;
	}
	public void setLocationsIds(List<Integer> locationsIds) {
		this.locationsIds = locationsIds;
	}
	public List<SEAMappedSiteDetailsBean> getMappedSiteDetails() {
		return mappedSiteDetails;
	}
	public void setMappedSiteDetails(List<SEAMappedSiteDetailsBean> mappedSiteDetails) {
		this.mappedSiteDetails = mappedSiteDetails;
	}
	public List<SEASiteDetailsBean> getUnMappedSiteDetails() {
		return unMappedSiteDetails;
	}
	public void setUnMappedSiteDetails(List<SEASiteDetailsBean> unMappedSiteDetails) {
		this.unMappedSiteDetails = unMappedSiteDetails;
	}
    
    
}
