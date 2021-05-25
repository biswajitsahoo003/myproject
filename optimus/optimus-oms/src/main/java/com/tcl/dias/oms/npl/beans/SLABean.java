package com.tcl.dias.oms.npl.beans;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.utils.Constants;




@JsonInclude(Include.NON_NULL)
public class SLABean {

	@NotNull(message = Constants.SLA_FACTOR_EMPTY)
	private String factor;

	@NotNull(message = Constants.SLA_VALUE_EMPTY)
	private String value;

	/*
	 * method to retrieve the SLA factor
	 */
	public String getFactor() {
		return factor;
	}

	/*
	 * method to set the SLA factor
	 */
	public void setFactor(String factor) {
		this.factor = factor;
	}

	/*
	 * method to retrieve the value for the SLA factor
	 */
	public String getValue() {
		return value;
	}

	/*
	 * method to set the value for the SLA factor
	 */
	public void setValue(String value) {
		this.value = value;
	}

	
	@Override
	public String toString() {
		return "SLABean [factor=" + factor + ", value=" + value + "]";
	}

}
