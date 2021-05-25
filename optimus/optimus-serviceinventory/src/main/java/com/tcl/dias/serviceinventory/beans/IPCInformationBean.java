package com.tcl.dias.serviceinventory.beans;

import java.sql.Timestamp;
import java.util.List;

import com.tcl.dias.common.beans.ServiceDetailBean;

/**
 * This file contains the IPCInformationBean.java class. Bean class
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class IPCInformationBean {
	
	private Integer orderId;
	private String serviceId;
	private String location;
	private Boolean isMacdInitiated;
	private Timestamp serviceCreatedDate;
	private ServiceDetailBean serviceDetailBean;
	private List<AssetDetailBean> vmDetailList;
	private List<AssetDetailBean> accessDetailList;
	private List<AssetDetailBean> addOnDetailList;
	private String siteType;
	
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public ServiceDetailBean getServiceDetailBean() {
		return serviceDetailBean;
	}
	public void setServiceDetailBean(ServiceDetailBean serviceDetailBean) {
		this.serviceDetailBean = serviceDetailBean;
	}
	public List<AssetDetailBean> getVmDetailList() {
		return vmDetailList;
	}
	public void setVmDetailList(List<AssetDetailBean> vmDetailList) {
		this.vmDetailList = vmDetailList;
	}
	public List<AssetDetailBean> getAccessDetailList() {
		return accessDetailList;
	}
	public void setAccessDetailList(List<AssetDetailBean> accessDetailList) {
		this.accessDetailList = accessDetailList;
	}
	public List<AssetDetailBean> getAddOnDetailList() {
		return addOnDetailList;
	}
	public void setAddOnDetailList(List<AssetDetailBean> addOnDetailList) {
		this.addOnDetailList = addOnDetailList;
	}
	public Boolean getIsMacdInitiated() {
		return isMacdInitiated;
	}
	public void setIsMacdInitiated(Boolean isMacdInitiated) {
		this.isMacdInitiated = isMacdInitiated;
	}
	public Timestamp getServiceCreatedDate() {
		return serviceCreatedDate;
	}
	public void setServiceCreatedDate(Timestamp serviceCreatedDate) {
		this.serviceCreatedDate = serviceCreatedDate;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
}
