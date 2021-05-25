package com.tcl.dias.servicefulfillmentutils.beans;

public class SerialNumberBean {
	private String materialCode;
	private String serialNumber;
	private String showSerialNumber;
	
	private String materialDescription;
	
	
	

	public String getMaterialDescription() {
		return materialDescription;
	}

	public void setMaterialDescription(String materialDescription) {
		this.materialDescription = materialDescription;
	}

	private String quantity;

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getShowSerialNumber() {
		return showSerialNumber;
	}

	public void setShowSerialNumber(String showSerialNumber) {
		this.showSerialNumber = showSerialNumber;
	}

	@Override
	public String toString() {
		return "SerialNumberBean{" + "materialCode='" + materialCode + '\'' + ", serialNumber='" + serialNumber + '\''
				+ ", showSerialNumber='" + showSerialNumber + '\'' + '}';
	}
}
