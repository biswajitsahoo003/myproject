package com.tcl.dias.common.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MuxDetailBean{

	@JsonProperty("mux_details")
	private List<MuxDetailsItem> muxDetails;

	public void setMuxDetails(List<MuxDetailsItem> muxDetails){
		this.muxDetails = muxDetails;
	}

	public List<MuxDetailsItem> getMuxDetails(){
		return muxDetails;
	}

	@Override
 	public String toString(){
		return 
			"MuxDetailBean{" + 
			"mux_details = '" + muxDetails + '\'' + 
			"}";
		}
}