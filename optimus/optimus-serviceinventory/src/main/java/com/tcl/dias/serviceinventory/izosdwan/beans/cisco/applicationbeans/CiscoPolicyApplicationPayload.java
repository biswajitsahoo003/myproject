
package com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CiscoPolicyApplicationPayload implements Serializable {

	@JsonProperty("name")
	private String name;
	@JsonProperty("type")
	private String type;
	@JsonProperty("description")
	private String description;
	@JsonProperty("entries")
	private List<ApplicationEntries> entries;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<ApplicationEntries> getEntries() {
		return entries;
	}
	public void setEntries(List<ApplicationEntries> entries) {
		this.entries = entries;
	}
			
}