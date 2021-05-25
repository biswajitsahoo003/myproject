package com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans;

import java.util.List;
import java.util.Map;

import com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.AccessPolicy;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration.FirewallPolicyConfig;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos.QosPolicy;
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering.TrafficSteeringPolicy;

/**
 * Bean for storing sdwan policies
 * @author Srinivasa Raghavan
 */
public class CiscoSdwanPolicyBean {

    private List<String> siteListName;
    private AppRoutePolicy trafficSteeringPolicies;
    private DataPolicy qosPolicy;
    private Integer trafficSteeringPolicyCount = 0;
    private Integer qosPolicyCount = 0;
    private Integer sdwanSysId;
    private String organisationName;
    private String instanceRegion;
	public List<String> getSiteListName() {
		return siteListName;
	}
	public void setSiteListName(List<String> siteListName) {
		this.siteListName = siteListName;
	}
	public AppRoutePolicy getTrafficSteeringPolicies() {
		return trafficSteeringPolicies;
	}
	public void setTrafficSteeringPolicies(AppRoutePolicy trafficSteeringPolicies) {
		this.trafficSteeringPolicies = trafficSteeringPolicies;
	}
	public DataPolicy getQosPolicy() {
		return qosPolicy;
	}
	public void setQosPolicy(DataPolicy qosPolicy) {
		this.qosPolicy = qosPolicy;
	}
	public Integer getTrafficSteeringPolicyCount() {
		return trafficSteeringPolicyCount;
	}
	public void setTrafficSteeringPolicyCount(Integer trafficSteeringPolicyCount) {
		this.trafficSteeringPolicyCount = trafficSteeringPolicyCount;
	}
	public Integer getQosPolicyCount() {
		return qosPolicyCount;
	}
	public void setQosPolicyCount(Integer qosPolicyCount) {
		this.qosPolicyCount = qosPolicyCount;
	}
	public Integer getSdwanSysId() {
		return sdwanSysId;
	}
	public void setSdwanSysId(Integer sdwanSysId) {
		this.sdwanSysId = sdwanSysId;
	}
	public String getOrganisationName() {
		return organisationName;
	}
	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}
	public String getInstanceRegion() {
		return instanceRegion;
	}
	public void setInstanceRegion(String instanceRegion) {
		this.instanceRegion = instanceRegion;
	}
	@Override
	public String toString() {
		return "CiscoSdwanPolicyBean [siteListName=" + siteListName + ", trafficSteeringPolicies="
				+ trafficSteeringPolicies + ", qosPolicy=" + qosPolicy + ", trafficSteeringPolicyCount="
				+ trafficSteeringPolicyCount + ", qosPolicyCount=" + qosPolicyCount + ", sdwanSysId=" + sdwanSysId
				+ ", organisationName=" + organisationName + ", instanceRegion=" + instanceRegion + "]";
	}
    
    }

