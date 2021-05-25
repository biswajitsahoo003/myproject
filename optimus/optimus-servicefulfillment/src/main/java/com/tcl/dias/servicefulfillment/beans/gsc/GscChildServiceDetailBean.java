package com.tcl.dias.servicefulfillment.beans.gsc;

import java.io.Serializable;
import java.util.List;

public class GscChildServiceDetailBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String productName;
	
	private List<GscChildServiceBean> services;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<GscChildServiceBean> getServices() {
		return services;
	}

	public void setServices(List<GscChildServiceBean> services) {
		this.services = services;
	}
}
