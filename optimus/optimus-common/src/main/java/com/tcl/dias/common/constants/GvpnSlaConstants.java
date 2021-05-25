package com.tcl.dias.common.constants;

public enum GvpnSlaConstants {
	
	ROUND_TRIP_DELAY(1,"Round Trip Delay (ms)"),
	PACKET_DROP(2,"Packet Drop %"),
	NETWORK_UP_TIME(3,"Supplier Monthly Average Network uptime %"),
	SERVICE_AVAILABILITY(4,"Service Availability %"),
	TIME_TO_RESTORE(5,"Time To Restore (TTR) in Hrs"),
	MEAN_TIME_TO_RESTORE(6,"Mean Time To Restore (MTTR) in Hrs"),
	PKT_DELIVERY_RATIO_SERV_LEVEL_TGT(7,"Packet Delivery Ratio Service Level Target %"),
	MAX_NO_OF_INCIDENTS(8,"Maximum Number of Incidents"),
	JITTER_SERVICE_LEVEL_TGT(9,"Jitter Servicer Level Target (ms)"),
	TIER_ONE(52,"Tier 1");
	
	private int id;
	private String name;

	public int getId() {
		return id;
	}

	

	public String getName() {
		return name;
	}

	private GvpnSlaConstants(int id, String name) {
		this.id = id;
		this.name = name;
	}

	

}
