package com.tcl.dias.servicefulfillment.beans.gsc;

import java.io.Serializable;
import java.util.List;

public class ProvisioningValidationBean implements Serializable {

	private String productName;
	private List<ServiceBean> services;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<ServiceBean> getServices() {
		return services;
	}

	public void setServices(List<ServiceBean> services) {
		this.services = services;
	}

	@Override
	public String toString() {
		return "ProvisioningValidationBean [productName=" + productName + ", serviceList=" + services + "]";
	}

}
