
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
public class DataPolicySequences implements Serializable {

	@JsonProperty("sequenceId")
	private Integer sequenceId;
	@JsonProperty("sequenceName")
	private String sequenceName;
	@JsonProperty("baseAction")
	private String baseAction;
	@JsonProperty("sequenceType")
	private String sequenceType;
	@JsonProperty("sequenceIpType")
	private String sequenceIpType;
	@JsonProperty("match")
	private Match match;
	@JsonProperty("actions")
	private List<DataPolicyActions> actions;
	public Integer getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}
	public String getSequenceName() {
		return sequenceName;
	}
	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}
	public String getSequenceType() {
		return sequenceType;
	}
	public void setSequenceType(String sequenceType) {
		this.sequenceType = sequenceType;
	}
	public String getSequenceIpType() {
		return sequenceIpType;
	}
	public void setSequenceIpType(String sequenceIpType) {
		this.sequenceIpType = sequenceIpType;
	}
	public Match getMatch() {
		return match;
	}
	public void setMatch(Match match) {
		this.match = match;
	}
	
	public String getBaseAction() {
		return baseAction;
	}
	public void setBaseAction(String baseAction) {
		this.baseAction = baseAction;
	}
	public List<DataPolicyActions> getActions() {
		return actions;
	}
	public void setActions(List<DataPolicyActions> actions) {
		this.actions = actions;
	}
	
	public DataPolicySequences() {
	}
	@Override
	public String toString() {
		return "Sequences [sequenceId=" + sequenceId + ", sequenceName=" + sequenceName + ", sequenceType="
				+ sequenceType + ", sequenceIpType=" + sequenceIpType + ", match=" + match + ", actions=" + actions
				+ "]";
	}

	
}
