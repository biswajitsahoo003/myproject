package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 * PolicyCriteria Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class PolicyCriteriaBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer policyCriteriaId;
	private Timestamp endDate;
	private Boolean matchCriteriaPrefixListPreprovisioned;
	private Timestamp lastModifiedDate;
	private Boolean matchcriteriaNeighbourCommunity;
	private String matchCriteriaRegexAsPathAsPath;
	private Boolean matchcriteriaPrefixlist;
	private String matchcriteriaPrefixlistName;
	private Boolean matchcriteriaProtocol;
	private String matchcriteriaProtocolName;
	private String matchcriteriaProtocolvalue;
	private Boolean matchcriteriaRegexAspath;
	private String matchcriteriaRegexAspathName;
	private String modifiedBy;
	private String name;
	private Boolean setcriteriaAspathPrepend;
	private String setcriteriaAspathPrependIndex;
	private String setcriteriaAspathPrependName;
	private Boolean setcriteriaLocalPreference;
	private String setcriteriaLocalpreferenceName;
	private Boolean setcriteriaMetric;
	private String setcriteriaMetricName;
	private Boolean setcriteriaNeighbourCommunity;
	private String setCriteriaRegexAsPathAsPath;
	private Boolean setcriteriaRegexAspath;
	private String setcriteriaRegexAspathName;
	private Timestamp startDate;
	private String termName;
	private String termSetcriteriaAction;
	private boolean isEdited;

	private Set<NeighbourCommunityConfigBean> neighbourCommunityConfigs;

	private Set<PrefixlistConfigBean> prefixlistConfigs;

	private Set<RegexAspathConfigBean> regexAspathConfigs;

	public Integer getPolicyCriteriaId() {
		return policyCriteriaId;
	}

	public void setPolicyCriteriaId(Integer policyCriteriaId) {
		this.policyCriteriaId = policyCriteriaId;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Boolean getMatchCriteriaPrefixListPreprovisioned() {
		return matchCriteriaPrefixListPreprovisioned;
	}

	public void setMatchCriteriaPrefixListPreprovisioned(Boolean matchCriteriaPrefixListPreprovisioned) {
		this.matchCriteriaPrefixListPreprovisioned = matchCriteriaPrefixListPreprovisioned;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Boolean getMatchcriteriaNeighbourCommunity() {
		return matchcriteriaNeighbourCommunity;
	}

	public void setMatchcriteriaNeighbourCommunity(Boolean matchcriteriaNeighbourCommunity) {
		this.matchcriteriaNeighbourCommunity = matchcriteriaNeighbourCommunity;
	}

	public String getMatchCriteriaRegexAsPathAsPath() {
		return matchCriteriaRegexAsPathAsPath;
	}

	public void setMatchCriteriaRegexAsPathAsPath(String matchCriteriaRegexAsPathAsPath) {
		this.matchCriteriaRegexAsPathAsPath = matchCriteriaRegexAsPathAsPath;
	}

	public Boolean getMatchcriteriaPrefixlist() {
		return matchcriteriaPrefixlist;
	}

	public void setMatchcriteriaPrefixlist(Boolean matchcriteriaPrefixlist) {
		this.matchcriteriaPrefixlist = matchcriteriaPrefixlist;
	}

	public String getMatchcriteriaPrefixlistName() {
		return matchcriteriaPrefixlistName;
	}

	public void setMatchcriteriaPrefixlistName(String matchcriteriaPrefixlistName) {
		this.matchcriteriaPrefixlistName = matchcriteriaPrefixlistName;
	}

	public Boolean getMatchcriteriaProtocol() {
		return matchcriteriaProtocol;
	}

	public void setMatchcriteriaProtocol(Boolean matchcriteriaProtocol) {
		this.matchcriteriaProtocol = matchcriteriaProtocol;
	}

	public String getMatchcriteriaProtocolName() {
		return matchcriteriaProtocolName;
	}

	public void setMatchcriteriaProtocolName(String matchcriteriaProtocolName) {
		this.matchcriteriaProtocolName = matchcriteriaProtocolName;
	}

	public String getMatchcriteriaProtocolvalue() {
		return matchcriteriaProtocolvalue;
	}

	public void setMatchcriteriaProtocolvalue(String matchcriteriaProtocolvalue) {
		this.matchcriteriaProtocolvalue = matchcriteriaProtocolvalue;
	}

	public Boolean getMatchcriteriaRegexAspath() {
		return matchcriteriaRegexAspath;
	}

	public void setMatchcriteriaRegexAspath(Boolean matchcriteriaRegexAspath) {
		this.matchcriteriaRegexAspath = matchcriteriaRegexAspath;
	}

	public String getMatchcriteriaRegexAspathName() {
		return matchcriteriaRegexAspathName;
	}

	public void setMatchcriteriaRegexAspathName(String matchcriteriaRegexAspathName) {
		this.matchcriteriaRegexAspathName = matchcriteriaRegexAspathName;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getSetcriteriaAspathPrepend() {
		return setcriteriaAspathPrepend;
	}

	public void setSetcriteriaAspathPrepend(Boolean setcriteriaAspathPrepend) {
		this.setcriteriaAspathPrepend = setcriteriaAspathPrepend;
	}

	public String getSetcriteriaAspathPrependIndex() {
		return setcriteriaAspathPrependIndex;
	}

	public void setSetcriteriaAspathPrependIndex(String setcriteriaAspathPrependIndex) {
		this.setcriteriaAspathPrependIndex = setcriteriaAspathPrependIndex;
	}

	public String getSetcriteriaAspathPrependName() {
		return setcriteriaAspathPrependName;
	}

	public void setSetcriteriaAspathPrependName(String setcriteriaAspathPrependName) {
		this.setcriteriaAspathPrependName = setcriteriaAspathPrependName;
	}

	public Boolean getSetcriteriaLocalPreference() {
		return setcriteriaLocalPreference;
	}

	public void setSetcriteriaLocalPreference(Boolean setcriteriaLocalPreference) {
		this.setcriteriaLocalPreference = setcriteriaLocalPreference;
	}

	public String getSetcriteriaLocalpreferenceName() {
		return setcriteriaLocalpreferenceName;
	}

	public void setSetcriteriaLocalpreferenceName(String setcriteriaLocalpreferenceName) {
		this.setcriteriaLocalpreferenceName = setcriteriaLocalpreferenceName;
	}

	public Boolean getSetcriteriaMetric() {
		return setcriteriaMetric;
	}

	public void setSetcriteriaMetric(Boolean setcriteriaMetric) {
		this.setcriteriaMetric = setcriteriaMetric;
	}

	public String getSetcriteriaMetricName() {
		return setcriteriaMetricName;
	}

	public void setSetcriteriaMetricName(String setcriteriaMetricName) {
		this.setcriteriaMetricName = setcriteriaMetricName;
	}

	public Boolean getSetcriteriaNeighbourCommunity() {
		return setcriteriaNeighbourCommunity;
	}

	public void setSetcriteriaNeighbourCommunity(Boolean setcriteriaNeighbourCommunity) {
		this.setcriteriaNeighbourCommunity = setcriteriaNeighbourCommunity;
	}

	public String getSetCriteriaRegexAsPathAsPath() {
		return setCriteriaRegexAsPathAsPath;
	}

	public void setSetCriteriaRegexAsPathAsPath(String setCriteriaRegexAsPathAsPath) {
		this.setCriteriaRegexAsPathAsPath = setCriteriaRegexAsPathAsPath;
	}

	public Boolean getSetcriteriaRegexAspath() {
		return setcriteriaRegexAspath;
	}

	public void setSetcriteriaRegexAspath(Boolean setcriteriaRegexAspath) {
		this.setcriteriaRegexAspath = setcriteriaRegexAspath;
	}

	public String getSetcriteriaRegexAspathName() {
		return setcriteriaRegexAspathName;
	}

	public void setSetcriteriaRegexAspathName(String setcriteriaRegexAspathName) {
		this.setcriteriaRegexAspathName = setcriteriaRegexAspathName;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getTermName() {
		return termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public String getTermSetcriteriaAction() {
		return termSetcriteriaAction;
	}

	public void setTermSetcriteriaAction(String termSetcriteriaAction) {
		this.termSetcriteriaAction = termSetcriteriaAction;
	}

	public Set<NeighbourCommunityConfigBean> getNeighbourCommunityConfigs() {
		
		if(neighbourCommunityConfigs==null) {
			neighbourCommunityConfigs=new HashSet<>();
		}
		return neighbourCommunityConfigs;
	}

	public void setNeighbourCommunityConfigs(Set<NeighbourCommunityConfigBean> neighbourCommunityConfigs) {
		this.neighbourCommunityConfigs = neighbourCommunityConfigs;
	}

	public Set<PrefixlistConfigBean> getPrefixlistConfigs() {
		
		if(prefixlistConfigs==null) {
			prefixlistConfigs=new HashSet<>();
		}
		return prefixlistConfigs;
	}

	public void setPrefixlistConfigs(Set<PrefixlistConfigBean> prefixlistConfigs) {
		this.prefixlistConfigs = prefixlistConfigs;
	}

	public Set<RegexAspathConfigBean> getRegexAspathConfigs() {
		
		if(regexAspathConfigs==null) {
			regexAspathConfigs=new HashSet<>();
		}
		return regexAspathConfigs;
	}

	public void setRegexAspathConfigs(Set<RegexAspathConfigBean> regexAspathConfigs) {
		this.regexAspathConfigs = regexAspathConfigs;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		isEdited = edited;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}