package com.tcl.dias.serviceinventory.beans;

/**
 * Bean class to hold orderes number
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SIOrderNumberBean {

	private String orderId;

	private String number;

	private String callType;

	private String outpulse;

	private String status;

	private String originNetwork;

	private String tigerOrderId;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getOutpulse() {
		return outpulse;
	}

	public void setOutpulse(String outpulse) {
		this.outpulse = outpulse;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOriginNetwork() {
		return originNetwork;
	}

	public void setOriginNetwork(String originNetwork) {
		this.originNetwork = originNetwork;
	}

	public String getTigerOrderId() {
		return tigerOrderId;
	}

	public void setTigerOrderId(String tigerOrderId) {
		this.tigerOrderId = tigerOrderId;
	}

	@Override
	public String toString() {
		return "SIOrderNumberBean{" +
				"orderId='" + orderId + '\'' +
				", number='" + number + '\'' +
				", callType='" + callType + '\'' +
				", outpulse='" + outpulse + '\'' +
				", status='" + status + '\'' +
				", originNetwork='" + originNetwork + '\'' +
				", tigerOrderId='" + tigerOrderId + '\'' +
				'}';
	}
}
