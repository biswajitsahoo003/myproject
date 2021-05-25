package com.tcl.dias.serviceinventory.beans;


import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SdwanCpePerformanceDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cpeName;
	private List<PerformanceAttributes> links;
	private List<String> interfaceNames;

	
	public String getCpeName() {
		return cpeName;
	}

	public void setCpeName(String cpeName) {
		this.cpeName = cpeName;
	}

		public List<PerformanceAttributes> getLinks() {
		return links;
	}

	public void setLinks(List<PerformanceAttributes> links) {
		this.links = links;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SdwanCpePerformanceDetails that = (SdwanCpePerformanceDetails) o;
		return cpeName.equals(that.cpeName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cpeName);
	}

	public List<String> getInterfaceNames() {
		return interfaceNames;
	}

	public void setInterfaceNames(List<String> interfaceNames) {
		this.interfaceNames = interfaceNames;
	}
	
	
}
