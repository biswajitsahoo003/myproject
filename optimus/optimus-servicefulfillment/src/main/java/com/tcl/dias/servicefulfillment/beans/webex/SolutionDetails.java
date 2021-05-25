package com.tcl.dias.servicefulfillment.beans.webex;

import java.util.List;

public class SolutionDetails {
private String  solutionId;
private List<WEBEXOrder>  listOfWebexOrders ;
public String getSolutionId() {
	return solutionId;
}
public void setSolutionId(String solutionId) {
	this.solutionId = solutionId;
}
public List<WEBEXOrder> getListOfWebexOrders() {
	return listOfWebexOrders;
}
public void setListOfWebexOrders(List<WEBEXOrder> listOfWebexOrders) {
	this.listOfWebexOrders = listOfWebexOrders;
}




}
