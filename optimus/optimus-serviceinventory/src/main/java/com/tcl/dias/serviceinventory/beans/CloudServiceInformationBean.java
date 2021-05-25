package com.tcl.dias.serviceinventory.beans;

import java.util.List;

/**
 * Bean class to hold ipcinfo and its location details
 *
 * @author Dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class CloudServiceInformationBean {

	private List<IPCInformationBean> ipcInfoList; 
	private List<String> cityList;
	private List<String> businessUnitList;
	private List<String> zoneList;
	private List<String> partnerLeList;
	private Integer totalItems;
	private Integer totalPages;
	
	public List<IPCInformationBean> getIpcInfoList() {
		return ipcInfoList;
	}
	public void setIpcInfoList(List<IPCInformationBean> ipcInfoList) {
		this.ipcInfoList = ipcInfoList;
	}
	public List<String> getCityList() {
		return cityList;
	}
	public void setCityList(List<String> cityList) {
		this.cityList = cityList;
	}
	public List<String> getBusinessUnitList() {
		return businessUnitList;
	}
	public void setBusinessUnitList(List<String> businessUnitList) {
		this.businessUnitList = businessUnitList;
	}
	public List<String> getZoneList() {
		return zoneList;
	}
	public void setZoneList(List<String> zoneList) {
		this.zoneList = zoneList;
	}
	public List<String> getPartnerLeList() {
		return partnerLeList;
	}
	public void setPartnerLeList(List<String> partnerLeList) {
		this.partnerLeList = partnerLeList;
	}
	public Integer getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(Integer totalItems) {
		this.totalItems = totalItems;
	}
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
}
