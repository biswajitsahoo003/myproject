
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
public class AssemblyDetails implements Serializable {
	
	@JsonProperty("definitionId")
	private String definitionId;
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("entries")
	private List<Entries> entries;

	public String getDefinitionId() {
		return definitionId;
	}

	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Entries> getEntries() {
		return entries;
	}

	public void setEntries(List<Entries> entries) {
		this.entries = entries;
	}

	@Override
	public String toString() {
		return "Assembly [definitionId=" + definitionId + ", type=" + type + ", entries=" + entries + "]";
	}
	

}
