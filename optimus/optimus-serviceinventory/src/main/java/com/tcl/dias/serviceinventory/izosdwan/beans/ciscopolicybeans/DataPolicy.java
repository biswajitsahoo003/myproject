
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
 * Bean for receiving and mapping Traffic steering policy/rule response from Versa REST API
 * @author Srinivasa Raghavan
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "rules"
})
public class DataPolicy implements Serializable
{

	@JsonProperty("definitionId")
	private String definitionId;
	@JsonProperty("name")
	private String name;
	@JsonProperty("type")
	private String type;
	@JsonProperty("description")
	private String description;
	@JsonProperty("sequences")
	private List<DataPolicySequences> sequences;
	@JsonProperty("lastUpdated")
	private String lastUpdated;
	@JsonProperty("owner")
	private String owner;
	@JsonProperty("infoTag")
	private String infoTag;
	@JsonProperty("referenceCount")
	private String referenceCount;
	@JsonProperty("references")
	private List<References> references;
	@JsonProperty("activatedId")
	private List<String> activatedId;
	@JsonProperty("isActivatedByVsmart")
	private boolean isActivatedByVsmart;
	private final static long serialVersionUID = 6935002515305267075L;

	public String getDefinitionId() {
		return definitionId;
	}
	public void setDefinitionId(String definitionId) {
		this.definitionId = definitionId;
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
	
	public List<DataPolicySequences> getSequences() {
		return sequences;
	}
	public void setSequences(List<DataPolicySequences> sequences) {
		this.sequences = sequences;
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
	public String getInfoTag() {
		return infoTag;
	}
	public void setInfoTag(String infoTag) {
		this.infoTag = infoTag;
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
	public DataPolicy() {
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activatedId == null) ? 0 : activatedId.hashCode());
		result = prime * result + ((definitionId == null) ? 0 : definitionId.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((infoTag == null) ? 0 : infoTag.hashCode());
		result = prime * result + (isActivatedByVsmart ? 1231 : 1237);
		result = prime * result + ((lastUpdated == null) ? 0 : lastUpdated.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((referenceCount == null) ? 0 : referenceCount.hashCode());
		result = prime * result + ((references == null) ? 0 : references.hashCode());
		result = prime * result + ((sequences == null) ? 0 : sequences.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataPolicy other = (DataPolicy) obj;
		if (activatedId == null) {
			if (other.activatedId != null)
				return false;
		} else if (!activatedId.equals(other.activatedId))
			return false;
		if (definitionId == null) {
			if (other.definitionId != null)
				return false;
		} else if (!definitionId.equals(other.definitionId))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (infoTag == null) {
			if (other.infoTag != null)
				return false;
		} else if (!infoTag.equals(other.infoTag))
			return false;
		if (isActivatedByVsmart != other.isActivatedByVsmart)
			return false;
		if (lastUpdated == null) {
			if (other.lastUpdated != null)
				return false;
		} else if (!lastUpdated.equals(other.lastUpdated))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (referenceCount == null) {
			if (other.referenceCount != null)
				return false;
		} else if (!referenceCount.equals(other.referenceCount))
			return false;
		if (references == null) {
			if (other.references != null)
				return false;
		} else if (!references.equals(other.references))
			return false;
		if (sequences == null) {
			if (other.sequences != null)
				return false;
		} else if (!sequences.equals(other.sequences))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
}
