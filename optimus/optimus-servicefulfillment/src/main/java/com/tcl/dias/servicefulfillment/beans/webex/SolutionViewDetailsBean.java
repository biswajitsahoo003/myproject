package com.tcl.dias.servicefulfillment.beans.webex;
import java.util.List;

public class SolutionViewDetailsBean {
	private Integer solutionId;
	private List<OrderSolutionViewBean> orders;
	
	public Integer getSolutionId() {
		return solutionId;
	}
	public void setSolutionId(Integer solutionId) {
		this.solutionId = solutionId;
	}
	public List<OrderSolutionViewBean> getOrders() {
		return orders;
	}
	public void setOrders(List<OrderSolutionViewBean> orders) {
		this.orders = orders;
	}
	
	
}
