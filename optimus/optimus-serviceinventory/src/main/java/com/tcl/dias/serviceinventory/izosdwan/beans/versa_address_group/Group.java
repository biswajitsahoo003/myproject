
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_address_group;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name", "address-list" })
public class Group {

	@JsonProperty("name")
	private String name;
	@JsonProperty("address-list")
	private List<String> addressList = null;

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("address-list")
	public List<String> getAddressList() {
		return addressList;
	}

	@JsonProperty("address-list")
	public void setAddressList(List<String> addressList) {
		this.addressList = addressList;
	}

}
