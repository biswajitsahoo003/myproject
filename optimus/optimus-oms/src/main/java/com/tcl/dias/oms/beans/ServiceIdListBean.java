package com.tcl.dias.oms.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to list the serviceIds
 */
public class ServiceIdListBean {

    private List<String> serviceIdList=new ArrayList<>();
    private String productName;

    public List<String> getServiceIdList() {
        return serviceIdList;
    }

    public void setServiceIdList(List<String> serviceIdList) {
        this.serviceIdList = serviceIdList;
    }

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
    
    

}