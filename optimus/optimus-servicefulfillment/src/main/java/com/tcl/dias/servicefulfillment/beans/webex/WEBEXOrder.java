package com.tcl.dias.servicefulfillment.beans.webex;

import java.util.List;

public class WEBEXOrder {

private int orderId;

private List<ServiceDetails> listOfServiceDetails;

public int getOrderId() {
	return orderId;
}

public void setOrderId(int orderId) {
	this.orderId = orderId;
}

public List<ServiceDetails> getListOfServiceDetails() {
	return listOfServiceDetails;
}

public void setListOfServiceDetails(List<ServiceDetails> listOfServiceDetails) {
	this.listOfServiceDetails = listOfServiceDetails;
}



}
