
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
public class CiscoPoliciesAddressListBean implements Serializable {

	@JsonProperty("data")
	private List<CiscoPolicyAddressBean> ciscoAdressApps;

	public List<CiscoPolicyAddressBean> getCiscoAdressApps() {
		return ciscoAdressApps;
	}

	public void setCiscoAdressApps(List<CiscoPolicyAddressBean> ciscoAdressApps) {
		this.ciscoAdressApps = ciscoAdressApps;
	}

	@Override
	public String toString() {
		return "CiscoPoliciesAddressListBean [ciscoAdressApps=" + ciscoAdressApps + "]";
	}
	
	

}
