package com.tcl.dias.common.serviceinventory.beans;

import java.util.List;
import java.util.Map;

public class OrderSummaryBeanResponse {
	
	private Map<String,List<SIServiceInfoBean>> serviceMap;

	public Map<String, List<SIServiceInfoBean>> getServiceMap() {
		return serviceMap;
	}

	public void setServiceMap(Map<String, List<SIServiceInfoBean>> serviceMap) {
		this.serviceMap = serviceMap;
	}
	
	

}
