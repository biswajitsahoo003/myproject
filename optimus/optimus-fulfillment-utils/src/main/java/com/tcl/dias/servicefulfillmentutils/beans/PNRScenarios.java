package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

public class PNRScenarios {
	
	public static final Map<String, List<String>> pnrScenarioAndTaskMap = new HashedMap<>();
	
	static {
		pnrScenarioAndTaskMap.put("Onnet Wireline - Near Connect", Arrays.asList("lm-cc-conduct-site-survey",
				"lm-conduct-site-survey", "cpe-hardware-pr", "cpe-license-pr"));
		pnrScenarioAndTaskMap.put("Onnet Wireline - Connected Building",
				Arrays.asList("lm-cc-conduct-site-survey", "lm-conduct-site-survey", "lm-conduct-site-survey-man",
						"lm-conduct-site-survey-ss-man", "cpe-hardware-pr", "cpe-license-pr"));
		pnrScenarioAndTaskMap.put("Onnet Wireline - Connected Customer",
				Arrays.asList("select-mux", "get-mux-info-async", "cpe-hardware-pr", "cpe-license-pr"));
		pnrScenarioAndTaskMap.put("Onnet Wireless", Arrays.asList("enrich-service-design",
				"enrich-service-design-jeopardy", "cpe-hardware-pr", "cpe-license-pr", "install-mast"));
		pnrScenarioAndTaskMap.put("Offnet Wireless",
				Arrays.asList("provide-po-colo", "po-offnet-lm-provider", "cpe-hardware-pr", "cpe-license-pr"));
		pnrScenarioAndTaskMap.put("Offnet Wireline",
				Arrays.asList("provide-po-colo", "po-offnet-lm-provider", "cpe-hardware-pr", "cpe-license-pr"));
		
	}

}
