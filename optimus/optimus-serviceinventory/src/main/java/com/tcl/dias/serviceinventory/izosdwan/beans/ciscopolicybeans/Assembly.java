
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
public class Assembly implements Serializable {
	@JsonProperty("assembly")
	private List<AssemblyDetails> assemblyDetails;

	public List<AssemblyDetails> getAssemblyDetails() {
		return assemblyDetails;
	}

	public void setAssemblyDetails(List<AssemblyDetails> assemblyDetails) {
		this.assemblyDetails = assemblyDetails;
	}
	
}
