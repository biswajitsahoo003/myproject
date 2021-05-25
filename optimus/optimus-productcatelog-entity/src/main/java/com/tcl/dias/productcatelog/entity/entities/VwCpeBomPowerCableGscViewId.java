package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

public class VwCpeBomPowerCableGscViewId implements Serializable {
	private String bomName;
	private String productCode;
	private String productDescription;
	private String countryName;

	public VwCpeBomPowerCableGscViewId() {

	}

	public VwCpeBomPowerCableGscViewId(String bomName, String productCode, String productDescription, String countryName) {
		this.bomName = bomName;
		this.productCode = productCode;
		this.productDescription = productDescription;
		this.countryName = countryName;
	}

	@Override
	public String toString() {
		return "VwCpeBomPowerCableGscViewId{" +
				"bomName='" + bomName + '\'' +
				", productCode='" + productCode + '\'' +
				", productDescription='" + productDescription + '\'' +
				", countryName=" + countryName +
				'}';
	}
}
