
package com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Bean for receiving and mapping Traffic steering policy/rule response from
 * Versa REST API
 * 
 * @author Srinivasa Raghavan
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "definitionId" })
public class Match implements Serializable {

	@JsonProperty("entries")
	private List<SequenceEntries> entries;

	public List<SequenceEntries> getEntries() {
		return entries;
	}

	public void setEntries(List<SequenceEntries> entries) {
		this.entries = entries;
	}

	@Override
	public String toString() {
		return "Match [entries=" + entries + "]";
	}
	

	
}
