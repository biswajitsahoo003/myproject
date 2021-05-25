
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_address_group;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "address-groups" })
public class AddressGroupView {

	@JsonProperty("address-groups")
	private AddressGroups addressGroups;

	@JsonProperty("address-groups")
	public AddressGroups getAddressGroups() {
		return addressGroups;
	}

	@JsonProperty("address-groups")
	public void setAddressGroups(AddressGroups addressGroups) {
		this.addressGroups = addressGroups;
	}

}
