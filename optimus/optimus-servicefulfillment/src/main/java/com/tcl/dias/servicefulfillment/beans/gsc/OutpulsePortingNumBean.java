package com.tcl.dias.servicefulfillment.beans.gsc;

public class OutpulsePortingNumBean {
	private String outpulse;
	private String tollFreeNumber;
	private String isPortingNumber;

	public String getOutpulse() {
		return outpulse;
	}

	public void setOutpulse(String outpulse) {
		this.outpulse = outpulse;
	}

	public String getTollFreeNumber() {
		return tollFreeNumber;
	}

	public void setTollFreeNumber(String tollFreeNumber) {
		this.tollFreeNumber = tollFreeNumber;
	}

	public String getIsPortingNumber() {
		return isPortingNumber;
	}

	public void setIsPortingNumber(String isPortingNumber) {
		this.isPortingNumber = isPortingNumber;
	}

	@Override
	public String toString() {
		return "OutpulsePortingNumBean [outpulse=" + outpulse + ", tollFreeNumber=" + tollFreeNumber
				+ ", isPortingNumber=" + isPortingNumber + "]";
	}

}
