
package com.tcl.dias.docusign.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * This file contains the TabStatuses.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "TabStatus" })
public class TabStatuses {

	@JsonProperty("TabStatus")
	private TabStatus tabStatus;

	@JsonProperty("TabStatus")
	public TabStatus getTabStatus() {
		return tabStatus;
	}

	@JsonProperty("TabStatus")
	public void setTabStatus(TabStatus tabStatus) {
		this.tabStatus = tabStatus;
	}

}
