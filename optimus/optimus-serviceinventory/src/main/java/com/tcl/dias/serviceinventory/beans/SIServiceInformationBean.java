package com.tcl.dias.serviceinventory.beans;

import com.tcl.dias.common.beans.CustomerDataBean;
import com.tcl.dias.common.beans.CustomerLegalEntityDetails;
import com.tcl.dias.common.beans.GscServiceDetailBean;
import com.tcl.dias.common.beans.ServiceDetailBean;
import com.tcl.dias.common.gsc.beans.GscWholesaleInterconnectBean;
import com.tcl.dias.serviceinventory.entity.entities.SIServiceAttribute;

import java.util.List;

public class SIServiceInformationBean {
	
	public List<String> cities;
	public List<String> alias;
	public List<ServiceDetailBean> serviceDetailBeans;
	public Integer totalItems;
	public Integer totalPages;
	public List<CustomerLegalEntityDetails> customerLegalEntityDetails;
	private List<ContactBeans> contacts;
	public List<String> classifications;
	public List<CustomerDataBean> customerDetails;
	public List<GscServiceDetailBean> gscServiceDetailBeans;
	public List<SIServiceAttribute> siServiceAttributes;
	public List<GscWholesaleInterconnectBean> gscWholesaleInterconnectBeans;

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
	public List<String> getCities() {
		return cities;
	}
	public void setCities(List<String> cities) {
		this.cities = cities;
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

	public List<String> getClassifications() {
		return classifications;
	}

	public void setClassifications(List<String> classifications) {
		this.classifications = classifications;
	}

	public List<CustomerDataBean> getCustomerDetails() {
		return customerDetails;
	}

	public void setCustomerDetails(List<CustomerDataBean> customerDetails) {
		this.customerDetails = customerDetails;
	}

	public List<GscServiceDetailBean> getGscServiceDetailBeans() {
		return gscServiceDetailBeans;
	}

	public void setGscServiceDetailBeans(List<GscServiceDetailBean> gscServiceDetailBeans) {
		this.gscServiceDetailBeans = gscServiceDetailBeans;
	}

	public List<SIServiceAttribute> getSiServiceAttributes() {
		return siServiceAttributes;
	}
	public void setSiServiceAttributes(List<SIServiceAttribute> siServiceAttributes) {
		this.siServiceAttributes = siServiceAttributes;
	}
	public List<GscWholesaleInterconnectBean> getGscWholesaleInterconnectBeans() {
		return gscWholesaleInterconnectBeans;
	}
	public void setGscWholesaleInterconnectBeans(List<GscWholesaleInterconnectBean> gscWholesaleInterconnectBeans) {
		this.gscWholesaleInterconnectBeans = gscWholesaleInterconnectBeans;
	}
	@Override
	public String toString() {
		return "SIServiceInformationBean{" +
				"cities=" + cities +
				", alias=" + alias +
				", serviceDetailBeans=" + serviceDetailBeans +
				", totalItems=" + totalItems +
				", totalPages=" + totalPages +
				", customerLegalEntityDetails=" + customerLegalEntityDetails +
				", contacts=" + contacts +
				", classifications=" + classifications +
				", customerDetails=" + customerDetails +
				", gscServiceDetailBeans=" + gscServiceDetailBeans +
				", gscWholesaleInterconnectBeans=" + gscWholesaleInterconnectBeans +
				'}';
	}
}
