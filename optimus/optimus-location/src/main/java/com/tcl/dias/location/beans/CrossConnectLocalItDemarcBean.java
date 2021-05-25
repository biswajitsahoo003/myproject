package com.tcl.dias.location.beans;

import java.util.List;

import com.tcl.dias.common.beans.DemarcationBean;
import com.tcl.dias.common.beans.LocationItContact;

public class CrossConnectLocalItDemarcBean {

	private List<LocationItContact> contactList;

	private List<DemarcationBean> demarcationList;
	
	private Integer customerId;

	public List<LocationItContact> getContactList() {
		return contactList;
	}

	public void setContactList(List<LocationItContact> contactList) {
		this.contactList = contactList;
	}

	public List<DemarcationBean> getDemarcationList() {
		return demarcationList;
	}

	public void setDemarcationList(List<DemarcationBean> demarcationList) {
		this.demarcationList = demarcationList;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
	
}
