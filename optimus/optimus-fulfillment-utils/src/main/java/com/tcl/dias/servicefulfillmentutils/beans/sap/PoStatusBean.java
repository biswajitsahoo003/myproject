package com.tcl.dias.servicefulfillmentutils.beans.sap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PoStatusBean{

	@JsonProperty("pOStatus")
	private String pOStatus;

	@JsonProperty("pONumber")
	private String pONumber;

	@JsonProperty("pRNumber")
	private String pRNumber;

	private String remark;

	public void setPOStatus(String pOStatus){
		this.pOStatus = pOStatus;
	}

	public String getPOStatus(){
		return pOStatus;
	}

	public void setPONumber(String pONumber){
		this.pONumber = pONumber;
	}

	public String getPONumber(){
		return pONumber;
	}

	public void setPRNumber(String pRNumber){
		this.pRNumber = pRNumber;
	}

	public String getPRNumber(){
		return pRNumber;
	}

	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getRemark(){
		return remark;
	}

	@Override
 	public String toString(){
		return
			"PoStatusBean{" +
			"pO_Status = '" + pOStatus + '\'' +
			",pO_Number = '" + pONumber + '\'' +
			",pR_Number = '" + pRNumber + '\'' +
			",remark = '" + remark + '\'' +
			"}";
		}
}