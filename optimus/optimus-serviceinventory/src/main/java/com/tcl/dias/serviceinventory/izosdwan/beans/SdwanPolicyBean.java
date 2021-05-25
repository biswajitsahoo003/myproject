package com.tcl.dias.serviceinventory.izosdwan.beans;

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
public class SdwanPolicyBean {

    private String templateName;
    private TrafficSteeringPolicy trafficSteeringPolicies;
    private QosPolicy qosPolicy;
    private Integer trafficSteeringPolicyCount = 0;
    private Integer qosPolicyCount = 0;
    private Integer sdwanSysId;
    private String organisationName;
    private String instanceRegion;
    private Integer firewallPolicyCount;
    private FirewallPolicyConfig firewallPolicyConfig;
    private Map<String,List<AccessPolicy>> accessPolicyMap;
    
    
    public Integer getFirewallPolicyCount() {
		return firewallPolicyCount;
	}

	public void setFirewallPolicyCount(Integer firewallPolicyCount) {
		this.firewallPolicyCount = firewallPolicyCount;
	}

	public FirewallPolicyConfig getFirewallPolicyConfig() {
		return firewallPolicyConfig;
	}

	public void setFirewallPolicyConfig(FirewallPolicyConfig firewallPolicyConfig) {
		this.firewallPolicyConfig = firewallPolicyConfig;
	}

	public Map<String, List<AccessPolicy>> getAccessPolicyMap() {
		return accessPolicyMap;
	}

	public void setAccessPolicyMap(Map<String, List<AccessPolicy>> accessPolicyMap) {
		this.accessPolicyMap = accessPolicyMap;
	}

	public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public TrafficSteeringPolicy getTrafficSteeringPolicies() {
        return trafficSteeringPolicies;
    }

    public void setTrafficSteeringPolicies(TrafficSteeringPolicy trafficSteeringPolicies) {
        this.trafficSteeringPolicies = trafficSteeringPolicies;
    }

    public QosPolicy getQosPolicy() {
        return qosPolicy;
    }

    public void setQosPolicy(QosPolicy qosPolicy) {
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
}

