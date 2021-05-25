
package com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CiscoSiteListDetails implements Serializable {

	@JsonProperty("listId")
	private String listId;
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("entries")
	private List<SiteIdEntries> siteIdentries;
	
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
	
	@JsonProperty("isActivatedByVsmart")
	private String isActivatedByVsmart;
	
	@JsonProperty("listId")
	public String getListId() {
		return listId;
	}
	@JsonProperty("listId")
	public void setListId(String listId) {
		this.listId = listId;
	}
	@JsonProperty("name")
	public String getName() {
		return name;
	}
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}
	@JsonProperty("type")
	public String getType() {
		return type;
	}
	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}
	@JsonProperty("entries")
	public List<SiteIdEntries> getSiteIdentries() {
		return siteIdentries;
	}
	@JsonProperty("entries")
	public void setSiteIdentries(List<SiteIdEntries> siteIdentries) {
		this.siteIdentries = siteIdentries;
	}
	@JsonProperty("lastUpdated")
	public String getLastUpdated() {
		return lastUpdated;
	}
	@JsonProperty("lastUpdated")
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	@JsonProperty("owner")
	public String getOwner() {
		return owner;
	}
	@JsonProperty("owner")
	public void setOwner(String owner) {
		this.owner = owner;
	}
	@JsonProperty("readOnly")
	public String getReadOnly() {
		return readOnly;
	}
	@JsonProperty("readOnly")
	public void setReadOnly(String readOnly) {
		this.readOnly = readOnly;
	}
	@JsonProperty("version")
	public String getVersion() {
		return version;
	}
	@JsonProperty("version")
	public void setVersion(String version) {
		this.version = version;
	}
	@JsonProperty("referenceCount")
	public String getReferenceCount() {
		return referenceCount;
	}
	@JsonProperty("referenceCount")
	public void setReferenceCount(String referenceCount) {
		this.referenceCount = referenceCount;
	}
	@JsonProperty("references")
	public List<References> getReferences() {
		return references;
	}
	@JsonProperty("references")
	public void setReferences(List<References> references) {
		this.references = references;
	}
	@JsonProperty("isActivatedByVsmart")
	public String getIsActivatedByVsmart() {
		return isActivatedByVsmart;
	}
	@JsonProperty("isActivatedByVsmart")
	public void setIsActivatedByVsmart(String isActivatedByVsmart) {
		this.isActivatedByVsmart = isActivatedByVsmart;
	}
	@Override
	public String toString() {
		return "CiscoSiteListDetails [listId=" + listId + ", name=" + name + ", type=" + type + ", description="
				+ description + ", siteIdentries=" + siteIdentries + ", lastUpdated=" + lastUpdated + ", owner=" + owner
				+ ", readOnly=" + readOnly + ", version=" + version + ", referenceCount=" + referenceCount
				+ ", references=" + references + ", isActivatedByVsmart=" + isActivatedByVsmart + "]";
	}
	
}