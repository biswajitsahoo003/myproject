package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import java.util.List;

public class ConfigureNmbrMappingBean {
	public String id;
	public String version;
	public String state;
	public ServiceSpecificationBean serviceSpecification;
	public List<CNMAttributeBean> serviceCharacteristic;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public ServiceSpecificationBean getServiceSpecification() {
		return serviceSpecification;
	}

	public void setServiceSpecification(ServiceSpecificationBean serviceSpecification) {
		this.serviceSpecification = serviceSpecification;
	}

	public List<CNMAttributeBean> getServiceCharacteristic() {
		return serviceCharacteristic;
	}

	public void setServiceCharacteristic(List<CNMAttributeBean> serviceCharacteristic) {
		this.serviceCharacteristic = serviceCharacteristic;
	}

}
