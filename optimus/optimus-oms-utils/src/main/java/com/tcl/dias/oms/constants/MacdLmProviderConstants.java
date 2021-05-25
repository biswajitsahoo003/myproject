package com.tcl.dias.oms.constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum MacdLmProviderConstants {

	// contains the list of Onnet LM Providers
	    MAN("MAN"),
	    RADWIN("RADWIN"),
	    TCL_UBR_PMP("TCL UBR PMP"),
	    VBL("VBL"),
		WIMAX("WIMAX"),
		COLOLASTMILE("Colocated Last Mile"),
		COLOLM("Colocated LM"),
		WAN_AGGREGATION("WAN Aggregation"),
		WAN_AGGREGATOR("WAN Aggregator"),
		ONNETWL("OnnetWL"),
		ONNETRF("OnnetRF"),
		TATA_COMMUNICATIONS("TATA COMMUNICATIONS");



	private String providerCode;

	    private MacdLmProviderConstants(String providerCode) {
	        this.providerCode = providerCode;
	    }
	    
	    private static final List<String> providerList = new ArrayList<>();

		static {
			providerList.addAll(Stream.of(MacdLmProviderConstants.values())
                    .map(MacdLmProviderConstants::getProviderCode)
                    .collect(Collectors.toList()));
			}

		public String getProviderCode() {
			return providerCode;
		}

		public void setProviderCode(String providerCode) {
			this.providerCode = providerCode;
		}

		public static List<String> getOnnetProviderlist() {
			return providerList;
		}

		public static List<String> getProviderlist() {
			return providerList;
		}
		
	
}
