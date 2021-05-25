package com.tcl.dias.serviceinventory.beans;

import com.tcl.dias.serviceinventory.izosdwan.beans.versa_forwarding_profile.ForwardingProfile_;

import java.util.List;

/**
 *
 */
public class ForwardingProfileBean {
	String profileName;
	String slaProfileName;
	String violationAction;
	List<SdwanPathPriorityBean> pathPriority;
	String recomputationTimer;
	String encryptionMethod;
	String replicationMethod;
	String loadBalancingMethod;

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getSlaProfileName() {
		return slaProfileName;
	}

	public void setSlaProfileName(String slaProfileName) {
		this.slaProfileName = slaProfileName;
	}

	public String getViolationAction() {
		return violationAction;
	}

	public void setViolationAction(String violationAction) {
		this.violationAction = violationAction;
	}

	public List<SdwanPathPriorityBean> getPathPriority() {
		return pathPriority;
	}

	public void setPathPriority(List<SdwanPathPriorityBean> pathPriority) {
		this.pathPriority = pathPriority;
	}

	public String getRecomputationTimer() {
		return recomputationTimer;
	}

	public void setRecomputationTimer(String recomputationTimer) {
		this.recomputationTimer = recomputationTimer;
	}

	public String getEncryptionMethod() {
		return encryptionMethod;
	}

	public void setEncryptionMethod(String encryptionMethod) {
		this.encryptionMethod = encryptionMethod;
	}

	public String getReplicationMethod() {
		return replicationMethod;
	}

	public void setReplicationMethod(String replicationMethod) {
		this.replicationMethod = replicationMethod;
	}

	public String getLoadBalancingMethod() {
		return loadBalancingMethod;
	}

	public void setLoadBalancingMethod(String loadBalancingMethod) {
		this.loadBalancingMethod = loadBalancingMethod;
	}
}
