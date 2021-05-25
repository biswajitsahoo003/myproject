package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

public class UifnProcurementBean {

	private Integer serviceId;

	private List<String> numbers;

	public Integer getServiceId() {
		return serviceId;
	}

	public List<String> getNumbers() {
		return numbers;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public void setNumbers(List<String> numbers) {
		this.numbers = numbers;
	}
}
