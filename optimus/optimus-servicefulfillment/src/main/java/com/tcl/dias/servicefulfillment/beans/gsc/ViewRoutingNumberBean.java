package com.tcl.dias.servicefulfillment.beans.gsc;

public class ViewRoutingNumberBean {

	private Integer id;
	private String routingNumber;
	private String routingNoReservationEndDate;
	private String routingNoReservationId;

	public Integer getId() {
		return id;
	}

	public String getRoutingNumber() {
		return routingNumber;
	}

	public String getRoutingNoReservationEndDate() {
		return routingNoReservationEndDate;
	}

	public String getRoutingNoReservationId() {
		return routingNoReservationId;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setRoutingNumber(String routingNumber) {
		this.routingNumber = routingNumber;
	}

	public void setRoutingNoReservationEndDate(String routingNoReservationEndDate) {
		this.routingNoReservationEndDate = routingNoReservationEndDate;
	}

	public void setRoutingNoReservationId(String routingNoReservationId) {
		this.routingNoReservationId = routingNoReservationId;
	}
}
