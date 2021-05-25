package com.tcl.dias.oms.constants;

public enum MstDelegationConstants {

	PORT_ARC("port_arc"),
	PORT_NRC("port_nrc"),
	ADDITIONAL_IP_ARC("additional_IP_ARC"),
	BURST_PER_MB_PRICE_ARC("burst_per_MB_price_ARC"),
	CPE_INSTALL_NRC("CPE_Install_NRC"),
	CPE_OUTRIGHT_NRC("CPE_Outright_NRC"),
	CPE_RENTAL_ARC("CPE_Rental_ARC"),
	CPE_MANAGEMENT_ARC("CPE_Management_ARC"),
	LM_PORT_ARC("lm_port_arc"),
	LM_PORT_NRC("lm_port_nrc");
	
	String constants;

	private MstDelegationConstants(String constants) {
		this.constants = constants;
	}
	public String getConstant() {
		return constants;
	}
	
	@Override
	public String toString() {
		return this.getConstant();
	}
	
}
