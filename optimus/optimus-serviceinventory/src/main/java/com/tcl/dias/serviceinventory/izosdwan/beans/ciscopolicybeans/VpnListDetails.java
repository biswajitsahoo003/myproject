
package com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans;

import java.io.Serializable;
import java.util.List;

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
public class VpnListDetails implements Serializable {

	@JsonProperty("listId")
	private String listId;

	@JsonProperty("name")
	private String name;

	@JsonProperty("type")
	private String type;

	@JsonProperty("description")
	private String description;

	@JsonProperty("lastUpdated")
	private String lastUpdated;
	@JsonProperty("owner")
	private String owner;
	@JsonProperty("readOnly")
	private String readOnly;
	@JsonProperty("version")
	private String version;
	@JsonProperty("referenceCount")
	private String referenceCount;
	@JsonProperty("isActivatedByVsmart")
	private String isActivatedByVsmart;
	@JsonProperty("references")
	private List<VpnReferences> references;
	@JsonProperty("entries")
	private List<VpnEntries> entries;
	public String getListId() {
		return listId;
	}
	public void setListId(String listId) {
		this.listId = listId;
	}
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
	public String getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getReferenceCount() {
		return referenceCount;
	}
	public void setReferenceCount(String referenceCount) {
		this.referenceCount = referenceCount;
	}
	public String getIsActivatedByVsmart() {
		return isActivatedByVsmart;
	}
	public void setIsActivatedByVsmart(String isActivatedByVsmart) {
		this.isActivatedByVsmart = isActivatedByVsmart;
	}
	public List<VpnReferences> getReferences() {
		return references;
	}
	public void setReferences(List<VpnReferences> references) {
		this.references = references;
	}
	public List<VpnEntries> getEntries() {
		return entries;
	}
	public void setEntries(List<VpnEntries> entries) {
		this.entries = entries;
	}

}
