package com.tcl.dias.sap.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"Optimus_Id","Circuit_Id","MRN_No","MIN_No","Courier_Name","Veh_Docket_No","Status","Remark"})
public class MinResponse {

	@JsonProperty("MIN_No")
	private String mINNo;

	@JsonProperty("Veh_Docket_No")
	private String vehDocketNo;

	@JsonProperty("Status")
	private String status;

	@JsonProperty("Optimus_Id")
	private String optimusId;

	@JsonProperty("Courier_Name")
	private String courierName;

	@JsonProperty("MRN_No")
	private String mRNNo;

	@JsonProperty("Circuit_Id")
	private String circuitId;

	@JsonProperty("Remark")
	private String remark;

	@JsonProperty("MIN_No")
	public void setMINNo(String mINNo){
		this.mINNo = mINNo;
	}

	@JsonProperty("MIN_No")
	public String getMINNo(){
		return mINNo;
	}

	public void setVehDocketNo(String vehDocketNo){
		this.vehDocketNo = vehDocketNo;
	}

	public String getVehDocketNo(){
		return vehDocketNo;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setOptimusId(String optimusId){
		this.optimusId = optimusId;
	}

	public String getOptimusId(){
		return optimusId;
	}

	public void setCourierName(String courierName){
		this.courierName = courierName;
	}

	public String getCourierName(){
		return courierName;
	}

	@JsonProperty("MRN_No")
	public void setMRNNo(String mRNNo){
		this.mRNNo = mRNNo;
	}

	@JsonProperty("MRN_No")
	public String getMRNNo(){
		return mRNNo;
	}

	public void setCircuitId(String circuitId){
		this.circuitId = circuitId;
	}

	public String getCircuitId(){
		return circuitId;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getRemark(){
		return remark;
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
				", circuitId='" + circuitId + '\'' +
				", remark='" + remark + '\'' +
				'}';
	}
}