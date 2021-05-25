
package com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Versa APIs stubs (CPE status)
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "data" })
public class CiscoPoliciesSlaListBean implements Serializable {

	@JsonProperty("data")
	private List<CiscoPolicySlaBean> ciscoPolicySlaBean;

	public List<CiscoPolicySlaBean> getCiscoPolicySlaBean() {
		return ciscoPolicySlaBean;
	}

	public void setCiscoPolicySlaBean(List<CiscoPolicySlaBean> ciscoPolicySlaBean) {
		this.ciscoPolicySlaBean = ciscoPolicySlaBean;
	}

	@Override
	public String toString() {
		return "CiscoPoliciesSlaListBean [ciscoPolicySlaBean=" + ciscoPolicySlaBean + "]";
	}

}
