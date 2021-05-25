
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_address_group;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "group" })
public class AddressGroups {

	@JsonProperty("group")
	private List<Group> group = null;

	@JsonProperty("group")
	public List<Group> getGroup() {
		return group;
	}

	@JsonProperty("group")
	public void setGroup(List<Group> group) {
		this.group = group;
	}

}
