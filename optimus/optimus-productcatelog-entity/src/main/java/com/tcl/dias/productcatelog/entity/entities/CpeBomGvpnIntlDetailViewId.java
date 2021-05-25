package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

public class CpeBomGvpnIntlDetailViewId implements Serializable {
	private String bomName;
	private String productCode;
	private String longDesc;
	private Double listPrice;
	
	public CpeBomGvpnIntlDetailViewId() {

	}
	
	public CpeBomGvpnIntlDetailViewId( String bomName, String productCode, String longDesc,Double listPrice) {
		super();
		this.bomName = bomName;
		this.productCode = productCode;
		this.longDesc = longDesc;
		this.listPrice =listPrice;
		
	}

	@Override
	public String toString() {
		return "CpeBomGvpnIntlDetailViewId [bomName=" + bomName + ", productCode=" + productCode + ", longDesc=" + longDesc +",listPrice="+listPrice + "]";
	}



}
