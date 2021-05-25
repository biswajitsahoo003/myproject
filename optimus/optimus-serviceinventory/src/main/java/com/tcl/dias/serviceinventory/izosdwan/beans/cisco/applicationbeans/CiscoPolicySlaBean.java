
package com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list.References;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CiscoPolicySlaBean implements Serializable {

	@JsonProperty("listId")
	private String listId;
	@JsonProperty("name")
	private String name;
	@JsonProperty("type")
	private String type;
	@JsonProperty("description")
	private String description;
	@JsonProperty("entries")
	private List<SlaEntries> entries;
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
	@JsonProperty("references")
	private List<References> references;
	@JsonProperty("activatedId")
	private List<String> activatedId;
	@JsonProperty("isActivatedByVsmart")
	private boolean isActivatedByVsmart;
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
	
	
	public List<SlaEntries> getEntries() {
		return entries;
	}
	public void setEntries(List<SlaEntries> entries) {
		this.entries = entries;
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
	public List<References> getReferences() {
		return references;
	}
	public void setReferences(List<References> references) {
		this.references = references;
	}
	public List<String> getActivatedId() {
		return activatedId;
	}
	public void setActivatedId(List<String> activatedId) {
		this.activatedId = activatedId;
	}
	public boolean isActivatedByVsmart() {
		return isActivatedByVsmart;
	}
	public void setActivatedByVsmart(boolean isActivatedByVsmart) {
		this.isActivatedByVsmart = isActivatedByVsmart;
	}
	@Override
	public String toString() {
		return "CiscoPolicyApplicationBean [listId=" + listId + ", name=" + name + ", type=" + type + ", description="
				+ description + ", entries=" + entries + ", lastUpdated=" + lastUpdated + ", owner=" + owner
				+ ", readOnly=" + readOnly + ", version=" + version + ", referenceCount=" + referenceCount
				+ ", references=" + references + ", activatedId=" + activatedId + ", isActivatedByVsmart="
				+ isActivatedByVsmart + "]";
	}
		
}