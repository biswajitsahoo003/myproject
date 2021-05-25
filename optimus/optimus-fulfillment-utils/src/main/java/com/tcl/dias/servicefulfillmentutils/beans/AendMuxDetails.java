package com.tcl.dias.servicefulfillmentutils.beans;

public class AendMuxDetails {
	private String eorid;

	private String muxIP;

	private String eorprovisionStatus;

	private String muxprovisionStatus;

	private String muxName;

	private String bay;

    private String floor;

    private String priority;

    private String wing;

    private String message;

   

	public String getEorid() {
		return eorid;
	}



	public void setEorid(String eorid) {
		this.eorid = eorid;
	}



	public String getMuxIP() {
		return muxIP;
	}



	public void setMuxIP(String muxIP) {
		this.muxIP = muxIP;
	}



	public String getEorprovisionStatus() {
		return eorprovisionStatus;
	}



	public void setEorprovisionStatus(String eorprovisionStatus) {
		this.eorprovisionStatus = eorprovisionStatus;
	}



	public String getMuxprovisionStatus() {
		return muxprovisionStatus;
	}



	public void setMuxprovisionStatus(String muxprovisionStatus) {
		this.muxprovisionStatus = muxprovisionStatus;
	}



	public String getMuxName() {
		return muxName;
	}



	public void setMuxName(String muxName) {
		this.muxName = muxName;
	}



	public String getBay() {
		return bay;
	}



	public void setBay(String bay) {
		this.bay = bay;
	}



	public String getFloor() {
		return floor;
	}



	public void setFloor(String floor) {
		this.floor = floor;
	}



	public String getPriority() {
		return priority;
	}



	public void setPriority(String priority) {
		this.priority = priority;
	}



	public String getWing() {
		return wing;
	}



	public void setWing(String wing) {
		this.wing = wing;
	}



	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}



	@Override
	public String toString() {
		return "ClassPojo [eorid = " + eorid + ", muxIP = " + muxIP + ", eorprovisionStatus = " + eorprovisionStatus
				+ ", muxprovisionStatus = " + muxprovisionStatus + ", muxName = " + muxName + ", bay = " + bay
				+ ", floor = " + floor + ", priority = " + priority + ", wing = " + wing + ", message = " + message
				+ "]";
	}
}
