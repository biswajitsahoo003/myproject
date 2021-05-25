package com.tcl.dias.oms.ipc.constants;

/*
 * This file contains the IPC Order Stages.
 * 
 * @author Mohamed Danish A
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum OrderStagingConstants {

	ORDER_CREATED("Order Placed", "ORDER_CONFIRMED"), //ORDER_PLACED 
	ORDER_ENRICHMENT("Order Enrichment"), //NOT USED
	ORDER_COMPLETED("Order Completed"),
	ORDER_PROVISIONED("Order provisioned"), 
	SERVICE_ACCEPTANCE("Service Acceptance"),
	BILLING_STARTS("Billing Starts"), 
	EXPERIENCE_SURVEY("Experience survey");

	private String desc;
	private String subStage;

	private OrderStagingConstants(String desc) {
		this.desc = desc;
		this.subStage = this.name();
	}

	private OrderStagingConstants(String desc, String subStage) {
		this.desc = desc;
		this.subStage = subStage;
	}

	public String getDesc() {
		return desc;
	}

	public String getSubStage() {
		return subStage;
	}

}
