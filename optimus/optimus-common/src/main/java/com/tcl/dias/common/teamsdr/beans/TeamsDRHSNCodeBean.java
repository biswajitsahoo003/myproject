package com.tcl.dias.common.teamsdr.beans;

import java.util.Map;

/**
 * Bean for holding hsn data
 * 
 * @author Syed Ali.
 * @createdAt 23/12/2020, Wednesday, 11:46
 */
public class TeamsDRHSNCodeBean {
	private Map<String, SubComponentHSNCodeBean> components;

	public TeamsDRHSNCodeBean() {
	}

	public Map<String, SubComponentHSNCodeBean> getComponents() {
		return components;
	}

	public void setComponents(Map<String, SubComponentHSNCodeBean> components) {
		this.components = components;
	}

	@Override
	public String toString() {
		return "TeamsDRHSNCodeBean{" + "components=" + components + '}';
	}
}
