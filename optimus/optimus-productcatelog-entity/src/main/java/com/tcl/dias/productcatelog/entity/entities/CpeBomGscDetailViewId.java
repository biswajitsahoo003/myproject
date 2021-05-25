package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

public class CpeBomGscDetailViewId implements Serializable {
	private String bomName;
	private String productCode;
	private String longDesc;
	private Double listPrice;

	public CpeBomGscDetailViewId() {

	}

	public CpeBomGscDetailViewId(String bomName, String productCode, String longDesc, Double listPrice) {
		super();
		this.bomName = bomName;
		this.productCode = productCode;
		this.longDesc = longDesc;
		this.listPrice =listPrice;
		
	}

	@Override
	public String toString() {
		return "CpeBomGscDetailViewId [bomName=" + bomName + ", productCode=" + productCode + ", longDesc=" + longDesc +",listPrice="+listPrice + "]";
	}



}
