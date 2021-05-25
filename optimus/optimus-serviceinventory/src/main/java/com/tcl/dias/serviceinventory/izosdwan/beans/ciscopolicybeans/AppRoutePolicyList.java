
package com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans;

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
public class AppRoutePolicyList implements Serializable {

	@JsonProperty("data")
	private List<AppRoutePolicy> appRoutePolicy;

	public List<AppRoutePolicy> getAppRoutePolicy() {
		return appRoutePolicy;
	}

	public void setAppRoutePolicy(List<AppRoutePolicy> appRoutePolicy) {
		this.appRoutePolicy = appRoutePolicy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appRoutePolicy == null) ? 0 : appRoutePolicy.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppRoutePolicyList other = (AppRoutePolicyList) obj;
		if (appRoutePolicy == null) {
			if (other.appRoutePolicy != null)
				return false;
		} else if (!appRoutePolicy.equals(other.appRoutePolicy))
			return false;
		return true;
	}


}
