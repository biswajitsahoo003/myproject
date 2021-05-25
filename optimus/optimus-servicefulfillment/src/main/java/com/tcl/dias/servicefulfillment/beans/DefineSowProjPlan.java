package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

/**
 * This class is used to  Define Scope of Work & Project Plan for Offnet Scenario
 * 
 *
 * @author diksha garg
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class DefineSowProjPlan {
	
	private List<SowAttributesBean> attributes;

	public List<SowAttributesBean> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<SowAttributesBean> attributes) {
		this.attributes = attributes;
	}
}