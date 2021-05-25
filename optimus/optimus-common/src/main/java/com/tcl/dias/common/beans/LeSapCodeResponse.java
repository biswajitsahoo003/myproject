package com.tcl.dias.common.beans;

import java.util.ArrayList;
import java.util.List;

public class LeSapCodeResponse {
	
	private List<LeSapCodeBean> leSapCodes;

	public List<LeSapCodeBean> getLeSapCodes() {
		if(leSapCodes==null) {
			leSapCodes=new ArrayList<>();
		}
		return leSapCodes;
	}

	public void setLeSapCodes(List<LeSapCodeBean> leSapCodes) {
		this.leSapCodes = leSapCodes;
	}

	@Override
	public String toString() {
		return "LeSapCodeResponse{" +
				"leSapCodes=" + leSapCodes +
				'}';
	}
}
