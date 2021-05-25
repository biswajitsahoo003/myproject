
package com.tcl.dias.l2oworkflowutils.beans;

import java.util.List;

public class Preference {

	private List<String> orderType;
	private List<String> product;
	private List<String> status;
	private List<String> taskName;

	private List<String> city;

	public List<String> getOrderType() {
		return orderType;
	}

	public void setOrderType(List<String> orderType) {
		this.orderType = orderType;
	}

	public List<String> getProduct() {
		return product;
	}

	public void setProduct(List<String> product) {
		this.product = product;
	}

	public List<String> getStatus() {
		return status;
	}

	public void setStatus(List<String> status) {
		this.status = status;
	}

	public List<String> getTaskName() {
		return taskName;
	}

	public void setTaskName(List<String> taskName) {
		this.taskName = taskName;
	}

	public List<String> getCity() {
		return city;
	}

	public void setCity(List<String> city) {
		this.city = city;
	}

}
