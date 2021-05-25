package com.tcl.dias.oms.ipc.beans;

public class IPCGeoCodeResponseBean {

	private String status;
	
	private String message;
	
	private String corrId;
	
	private IPCGeoAddressBean addressObj;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCorrId() {
		return corrId;
	}

	public void setCorrId(String corrId) {
		this.corrId = corrId;
	}

	public IPCGeoAddressBean getAddressObj() {
		return addressObj;
	}

	public void setAddressObj(IPCGeoAddressBean addressObj) {
		this.addressObj = addressObj;
	}

	@Override
	public String toString() {
		return "IPCGeoCodeResponseBean [status=" + status + ", message=" + message + ", corrId=" + corrId
				+ ", addressObj=" + addressObj + "]";
	}
	
}
