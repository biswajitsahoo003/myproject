package com.tcl.dias.servicefulfillment.beans.webex;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class TrackEndpointBean extends TaskDetailsBaseBean {
	
    private String deliveryStatus;
    
    private String endpointDeliveryDate;

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

	public String getEndpointDeliveryDate() {
		return endpointDeliveryDate;
	}

	public void setEndpointDeliveryDate(String endpointDeliveryDate) {
		this.endpointDeliveryDate = endpointDeliveryDate;
	}
    
}
