package com.tcl.dias.preparefulfillment.beans;

import java.util.List;

public class SolutionViewDetailsBean {
	
	private ServiceSolutionViewBean solutionDetail;
	
	private List<OrderSolutionViewBean> orders;
	
	public ServiceSolutionViewBean getSolutionDetail() {
		return solutionDetail;
	}
	public void setSolutionDetail(ServiceSolutionViewBean solutionDetail) {
		this.solutionDetail = solutionDetail;
	}
	public List<OrderSolutionViewBean> getOrders() {
		return orders;
	}
	public void setOrders(List<OrderSolutionViewBean> orders) {
		this.orders = orders;
	}


}
