package com.tcl.dias.common.teamsdr.beans;

import java.util.Map;

/**
 * Bean for holding sub component hsn code .
 * 
 * @author Syed Ali.
 */
public class SubComponentHSNCodeBean {
	private Map<String, Map<String, String>> subComponents;

	public SubComponentHSNCodeBean() {
	}

	public Map<String, Map<String, String>> getSubComponents() {
		return subComponents;
	}

	public void setSubComponents(Map<String, Map<String, String>> subComponents) {
		this.subComponents = subComponents;
	}

	@Override
	public String toString() {
		return "SubComponentHSNCodeBean{" + "subComponents=" + subComponents + '}';
	}
}
