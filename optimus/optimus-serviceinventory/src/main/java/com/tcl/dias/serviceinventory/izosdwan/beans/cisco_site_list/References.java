
package com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * CISCO SITE LIST APIs
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "data" })
public class References implements Serializable {

	@JsonProperty("id")
	private String id;
	
	@JsonProperty("type")
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "References [id=" + id + ", type=" + type + "]";
	}
	
	
	
	
}
