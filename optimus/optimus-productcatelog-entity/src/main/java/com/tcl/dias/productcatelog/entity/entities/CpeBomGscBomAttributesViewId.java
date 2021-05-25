package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

public class CpeBomGscBomAttributesViewId implements Serializable {
	private String productCode;
	private String longDesc;
	private Double listPrice;

	public CpeBomGscBomAttributesViewId() {

	}

	public CpeBomGscBomAttributesViewId(String productCode, String longDesc, Double listPrice) {
		super();
		this.productCode = productCode;
		this.longDesc = longDesc;
		this.listPrice =listPrice;
		
	}

	@Override
	public String toString() {
		return "CpeBomGscBomAttributesViewId [productCode=" + productCode + ", longDesc=" + longDesc +",listPrice="+listPrice + "]";
	}



}
