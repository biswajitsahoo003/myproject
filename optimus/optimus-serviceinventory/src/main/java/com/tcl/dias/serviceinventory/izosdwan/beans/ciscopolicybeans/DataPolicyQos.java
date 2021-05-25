
package com.tcl.dias.serviceinventory.izosdwan.beans.ciscopolicybeans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Bean for receiving and mapping QOS policy/rule response from cisco REST API
 * @author SGanta
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "rules"
})
public class DataPolicyQos implements Serializable
{
	@JsonProperty("name")
	private String name;
	@JsonProperty("type")
	private String type;
	@JsonProperty("description")
	private String description;
	
	@JsonProperty("defaultAction")
	private DataPolicyActions defaultAction;
	
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
	
	private final static long serialVersionUID = 6935002515305267075L;

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
	
	
	
	
	public DataPolicyActions getDefaultAction() {
		return defaultAction;
	}
	public void setDefaultAction(DataPolicyActions defaultAction) {
		this.defaultAction = defaultAction;
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

	public DataPolicyQos() {
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((infoTag == null) ? 0 : infoTag.hashCode());
		result = prime * result + ((lastUpdated == null) ? 0 : lastUpdated.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((referenceCount == null) ? 0 : referenceCount.hashCode());
		result = prime * result + ((references == null) ? 0 : references.hashCode());
		result = prime * result + ((sequences == null) ? 0 : sequences.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((defaultAction == null) ? 0 : defaultAction.hashCode());
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
		DataPolicyQos other = (DataPolicyQos) obj;
		
		if (defaultAction == null) {
			if (other.defaultAction != null)
				return false;
		} else if (!defaultAction.equals(other.defaultAction))
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
