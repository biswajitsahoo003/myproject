package com.tcl.dias.products.izosdwan.beans;

import java.util.List;

public class CpeBomDetails {

	private String cpeName;
	private List<CpeSpecifications>  cpeSpecifications;
	public String getCpeName() {
		return cpeName;
	}
	public void setCpeName(String cpeName) {
		this.cpeName = cpeName;
	}
	public List<CpeSpecifications> getCpeSpecifications() {
		return cpeSpecifications;
	}
	public void setCpeSpecifications(List<CpeSpecifications> cpeSpecifications) {
		this.cpeSpecifications = cpeSpecifications;
	}
	
}
