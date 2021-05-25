package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import java.util.List;

public class GetRoutingNumberBean {

	private Integer quantity;
	
	private List<String> routingNo;
	
	private String routingNoReservationId;

	//@DateTimeFormat(pattern = "yyyy-MM-dd")
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String routingNoReservationEndDate;

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public List<String> getRoutingNo() {
		return routingNo;
	}

	public void setRoutingNo(List<String> routingNo) {
		this.routingNo = routingNo;
	}

	public String getRoutingNoReservationId() {
		return routingNoReservationId;
	}

	public void setRoutingNoReservationId(String routingNoReservationId) {
		this.routingNoReservationId = routingNoReservationId;
	}

	public String getRoutingNoReservationEndDate() {
		return routingNoReservationEndDate;
	}

	public void setRoutingNoReservationEndDate(String routingNoReservationEndDate) {
		this.routingNoReservationEndDate = routingNoReservationEndDate;
	}
}
