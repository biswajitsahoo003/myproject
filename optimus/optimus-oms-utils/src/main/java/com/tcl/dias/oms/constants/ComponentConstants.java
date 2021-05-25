package com.tcl.dias.oms.constants;

/**
 * This file contains the ComponentConstants.java class. This enum have all the
 * constants related to Components
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum ComponentConstants {

	BANDWIDTH("Bandwidth Unit"),INTERFACE("Interface"),LOCAL_LOOP_BANDWIDTH("Local Loop Bandwidth"),WAN_IP_PROVIDED_BY("WAN IP Provided By"),CPE_BASIC_CHASSIS("CPE Basic Chassis"),CONNECTOR_TYPE("Connector Type"), PORT_CMP("Internet Port"),
	PORT_BANDWIDTH("Port Bandwidth"),CONTRACT_MONTH("TermInMonths"),SERVICE_VARIANT("Service Variant"),VPN_PORT("VPN Port"),NATIONAL_CONNECTIVITY("National Connectivity");

	private String component;

	private ComponentConstants(String component) {
		this.component = component;
	}

	public String getComponentsValue() {
		return component;
	}
}
