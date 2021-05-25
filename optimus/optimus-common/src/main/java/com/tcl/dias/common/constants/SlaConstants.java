package com.tcl.dias.common.constants;

/**
 * This file contains the SlaConstants.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum SlaConstants {

	NETWORK_UP_TIME(1, "Network Uptime"), PACKET_DROP(2, "Packet Drop %"),

	LATENCY(3, "Latency / Round Trip Delay RTD"), TIER_ONE_1(1, "Tier 1"), TIER_TWO_2(2, "Tier 2"), TIER_THREE_3(3,
			"Tier 3"),

	TIER_ONE(52, "Tier 1"), TIER_TWO(53, "Tier 2"), TIER_THREE(54, "Tier 3"), ONNET(56, "OnNet"), OFFNET(57,
			"OffNet"), NONE(55, "None"), ROUND_TRIP_DELAY(1,"Round Trip Delay (ms)"),;

	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	private SlaConstants(int id, String name) {
		this.id = id;
		this.name = name;
	}

}
