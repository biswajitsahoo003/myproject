package com.tcl.dias.oms.beans;

import java.util.List;

import com.tcl.dias.common.beans.MDMServiceDetailBean;
import com.tcl.dias.common.beans.ServiceDetailBean;

public class MDMServiceInventoryBean {
	
	public List<MDMServiceDetailBean> serviceDetailBeans;
	public Integer totalItems;
	public Integer totalPages;
	
	
	public List<MDMServiceDetailBean> getServiceDetailBeans() {
		return serviceDetailBeans;
	}
	public void setServiceDetailBeans(List<MDMServiceDetailBean> serviceDetailBeans) {
		this.serviceDetailBeans = serviceDetailBeans;
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
