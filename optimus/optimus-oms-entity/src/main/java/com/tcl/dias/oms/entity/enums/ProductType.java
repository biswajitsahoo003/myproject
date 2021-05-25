package com.tcl.dias.oms.entity.enums;

public enum ProductType {
	
	GVPN("GVPN"),
	IAS("IAS"),
	BYON("BYON Internet"),
	GVPN_LOCAL_LOOP_BW("GVPN Local Loop BW "),
	GVPN_PORT_BW("GVPN Port BW"),
	IAS_LOCAL_LOOP_BW("IAS Local Loop Bw"),
	IAS_PORT_BW("IAS Port Bw"),
	BYON_PORT_BW("BYON Port Bw"),
	BYON_LOCAL_LOOP_BW("BYON Local Loop Bw"),
	Primary("Primary"),
	Secondary("Secondary");
	private String productTypeName;
	
	public String productTypeName() {
		return productTypeName;
	}
	
	ProductType(String productName){
		this.productTypeName=productName;
	}
	

}
