package com.tcl.dias.servicefulfillmentutils.beans.sap;

public class MinResponse {

	private String mINNo;
	private String vehDocketNo;
	private String status;
	private String optimusId;
	private String courierName;
	private String mRNNo;

	public String getmINNo() {
		return mINNo;
	}

	public void setmINNo(String mINNo) {
		this.mINNo = mINNo;
	}

	public String getVehDocketNo() {
		return vehDocketNo;
	}

	public void setVehDocketNo(String vehDocketNo) {
		this.vehDocketNo = vehDocketNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOptimusId() {
		return optimusId;
	}

	public void setOptimusId(String optimusId) {
		this.optimusId = optimusId;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public String getmRNNo() {
		return mRNNo;
	}

	public void setmRNNo(String mRNNo) {
		this.mRNNo = mRNNo;
	}

	@Override
	public String toString() {
		return "MinResponse{" +
				"mINNo='" + mINNo + '\'' +
				", vehDocketNo='" + vehDocketNo + '\'' +
				", status='" + status + '\'' +
				", optimusId='" + optimusId + '\'' +
				", courierName='" + courierName + '\'' +
				", mRNNo='" + mRNNo + '\'' +
				'}';
	}
}