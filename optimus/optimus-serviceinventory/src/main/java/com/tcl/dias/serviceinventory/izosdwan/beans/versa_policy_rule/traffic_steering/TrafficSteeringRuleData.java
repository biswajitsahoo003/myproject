package com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Bean class to fetch TrafficSteeringRule details from 
 * for given rule name
 * @author archchan
 *
 */
public class TrafficSteeringRuleData {

	@JsonProperty("rule")
    private Rule rule = null;

	public Rule getRule() {
		return rule;
	}

	public void setRule(Rule rule) {
		this.rule = rule;
	}

	@Override
	public String toString() {
		return "TrafficSteeringRuleData [rule=" + rule + "]";
	}
	
}
