package com.tcl.dias.serviceinventory.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to list the serviceIds
 */
public class ServiceIdListBean {

    private List<String> serviceIdList=new ArrayList<>();
    private Boolean isTermination;

    public List<String> getServiceIdList() {
        return serviceIdList;
    }

    public void setServiceIdList(List<String> serviceIdList) {
        this.serviceIdList = serviceIdList;
    }

	public Boolean getIsTermination() {
		return isTermination;
	}

	public void setIsTermination(Boolean isTermination) {
		this.isTermination = isTermination;
	}
    
    
}
