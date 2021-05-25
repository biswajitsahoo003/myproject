package com.tcl.dias.oms.izosdwan.beans;

import java.util.List;

/**
 * 
 * @author vpachava
 *
 */
public class VproxySolutionLevelCharges {

	private String solutionName;

	private String profileName;

	private List<VproxyChargableComponents> vproxyChargableComponents;

	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public List<VproxyChargableComponents> getVproxyChargableComponents() {
		return vproxyChargableComponents;
	}

	public void setVproxyChargableComponents(List<VproxyChargableComponents> vproxyChargableComponents) {
		this.vproxyChargableComponents = vproxyChargableComponents;
	}

}
