package com.tcl.dias.serviceinventory.beans;

import java.util.List;

import com.tcl.dias.common.beans.CustomerLegalEntityDetails;
import com.tcl.dias.common.beans.ServiceDetailBean;

/**
 * Bean class for GdeSIServiceInformation
 * @author archchan
 * 
 *
 */
public class GdeSIServiceInformationBean {
	
	public List<String> siteACities;
	public List<String> siteBCities;
	public List<String> alias;
	public List<ServiceDetailBean> serviceDetailBeans;
	public Integer totalItems;
	public Integer totalPages;
	public List<CustomerLegalEntityDetails> customerLegalEntityDetails;
	private List<ContactBeans> contacts;
	
	
	public List<ContactBeans> getContacts() {
		return contacts;
	}
	public void setContacts(List<ContactBeans> contacts) {
		this.contacts = contacts;
	}
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	public List<ServiceDetailBean> getServiceDetailBeans() {
		return serviceDetailBeans;
	}
	public void setServiceDetailBeans(List<ServiceDetailBean> serviceDetailBeans) {
		this.serviceDetailBeans = serviceDetailBeans;
	}
	public Integer getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(Integer totalItems) {
		this.totalItems = totalItems;
	}
	public List<String> getSiteACities() {
		return siteACities;
	}
	public void setSiteACities(List<String> siteACities) {
		this.siteACities = siteACities;
	}
	public List<String> getSiteBCities() {
		return siteBCities;
	}
	public void setSiteBCities(List<String> siteBCities) {
		this.siteBCities = siteBCities;
	}
	public List<String> getAlias() {
		return alias;
	}
	public void setAlias(List<String> alias) {
		this.alias = alias;
	}

	public List<CustomerLegalEntityDetails> getCustomerLegalEntityDetails() {
		return customerLegalEntityDetails;
	}

	public void setCustomerLegalEntityDetails(List<CustomerLegalEntityDetails> customerLegalEntityDetails) {
		this.customerLegalEntityDetails = customerLegalEntityDetails;
	}

	@Override
	public String toString() {
		return "GdeSIServiceInformationBean [siteACities=" + siteACities + ", siteBCities=" + siteBCities + ", alias="
				+ alias + ", serviceDetailBeans=" + serviceDetailBeans + ", totalItems=" + totalItems + ", totalPages="
				+ totalPages + ", customerLegalEntityDetails=" + customerLegalEntityDetails + ", contacts=" + contacts
				+ "]";
	}
}
