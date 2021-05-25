package com.tcl.dias.servicefulfillment.beans.teamsdr;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * @author Syed Ali.
 * @createdAt 01/02/2021, Monday, 15:21
 */

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
