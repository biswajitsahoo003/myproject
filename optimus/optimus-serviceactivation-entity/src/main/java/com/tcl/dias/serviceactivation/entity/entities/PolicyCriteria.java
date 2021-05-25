package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

/**
 * 
 * PolicyCriteria Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="policy_criteria")
@NamedQuery(name="PolicyCriteria.findAll", query="SELECT p FROM PolicyCriteria p")
public class PolicyCriteria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="policy_criteria_id")
	private Integer policyCriteriaId;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="matchcriteria_prefixlist_preprovisioned")
	private Byte matchcriteriaPprefixlistPreprovisioned;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="matchcriteria_acl")
	private Byte matchcriteriaAcl;

	@Column(name="matchcriteria_neighbour_community")
	private Byte matchcriteriaNeighbourCommunity;

	@Column(name="matchcriteria_regex_aspath_aspath")
	private String matchcriteriaRegexAspathAspath;

	@Column(name="matchcriteria_prefixlist")
	private Byte matchcriteriaPrefixlist;

	@Column(name="matchcriteria_prefixlist_name")
	private String matchcriteriaPrefixlistName;

	@Column(name="matchcriteria_protocol")
	private Byte matchcriteriaProtocol;

	@Column(name="matchcriteria_regex_aspath")
	private Byte matchcriteriaRegexAspath;

	@Column(name="matchcriteria_regex_aspath_name")
	private String matchcriteriaRegexAspathName;

	@Column(name="modified_by")
	private String modifiedBy;

	private String name;

	@Column(name="setcriteria_acl")
	private Byte setcriteriaAcl;

	@Column(name="setcriteria_aspath_prepend")
	private Byte setcriteriaAspathPrepend;

	@Column(name="setcriteria_aspath_prepend_index")
	private String setcriteriaAspathPrependIndex;

	@Column(name="setcriteria_aspath_prepend_name")
	private String setcriteriaAspathPrependName;

	@Column(name="setcriteria_local_preference")
	private Byte setcriteriaLocalPreference;

	@Column(name="setcriteria_localpreference_name")
	private String setcriteriaLocalpreferenceName;

	@Column(name="setcriteria_metric")
	private Byte setcriteriaMetric;

	@Column(name="setcriteria_metric_name")
	private String setcriteriaMetricName;

	@Column(name="setcriteria_neighbour_community")
	private Byte setcriteriaNeighbourCommunity;

	@Column(name="setcriteria_regex_aspath_aspath")
	private String setcriteriaRegexAspathAspath;

	@Column(name="setcriteria_regex_aspath")
	private Byte setcriteriaRegexAspath;

	@Column(name="setcriteria_regex_aspath_name")
	private String setcriteriaRegexAspathName;

	@Column(name="start_date")
	private Timestamp startDate;

	@Column(name="term_name")
	private String termName;

	@Column(name="term_setcriteria_action")
	private String termSetcriteriaAction;

	//bi-directional many-to-one association to AclPolicyCriteria
	@OneToMany(mappedBy="policyCriteria")
	private Set<AclPolicyCriteria> aclPolicyCriterias;

	//bi-directional many-to-one association to NeighbourCommunityConfig
	@OneToMany(mappedBy="policyCriteria",fetch = FetchType.EAGER)
	private Set<NeighbourCommunityConfig> neighbourCommunityConfigs;

	//bi-directional many-to-one association to PolicycriteriaProtocol
	@OneToMany(mappedBy="policyCriteria",fetch = FetchType.EAGER)
	private Set<PolicycriteriaProtocol> policycriteriaProtocols;

	//bi-directional many-to-one association to PrefixlistConfig
	@OneToMany(mappedBy="policyCriteria",fetch = FetchType.EAGER)
	private Set<PrefixlistConfig> prefixlistConfigs;

	//bi-directional many-to-one association to RegexAspathConfig
	@OneToMany(mappedBy="policyCriteria",fetch = FetchType.EAGER)
	private Set<RegexAspathConfig> regexAspathConfigs;

	public PolicyCriteria() {
	}

	public Integer getPolicyCriteriaId() {
		return this.policyCriteriaId;
	}

	public void setPolicyCriteriaId(Integer policyCriteriaId) {
		this.policyCriteriaId = policyCriteriaId;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Byte getMatchcriteriaAcl() {
		return this.matchcriteriaAcl;
	}

	public void setMatchcriteriaAcl(Byte matchcriteriaAcl) {
		this.matchcriteriaAcl = matchcriteriaAcl;
	}

	public Byte getMatchcriteriaNeighbourCommunity() {
		return this.matchcriteriaNeighbourCommunity;
	}

	public void setMatchcriteriaNeighbourCommunity(Byte matchcriteriaNeighbourCommunity) {
		this.matchcriteriaNeighbourCommunity = matchcriteriaNeighbourCommunity;
	}

	

	public Byte getMatchcriteriaPrefixlist() {
		return this.matchcriteriaPrefixlist;
	}

	public void setMatchcriteriaPrefixlist(Byte matchcriteriaPrefixlist) {
		this.matchcriteriaPrefixlist = matchcriteriaPrefixlist;
	}

	public String getMatchcriteriaPrefixlistName() {
		return this.matchcriteriaPrefixlistName;
	}

	public void setMatchcriteriaPrefixlistName(String matchcriteriaPrefixlistName) {
		this.matchcriteriaPrefixlistName = matchcriteriaPrefixlistName;
	}

	public Byte getMatchcriteriaProtocol() {
		return this.matchcriteriaProtocol;
	}

	public void setMatchcriteriaProtocol(Byte matchcriteriaProtocol) {
		this.matchcriteriaProtocol = matchcriteriaProtocol;
	}

	public Byte getMatchcriteriaRegexAspath() {
		return this.matchcriteriaRegexAspath;
	}

	public void setMatchcriteriaRegexAspath(Byte matchcriteriaRegexAspath) {
		this.matchcriteriaRegexAspath = matchcriteriaRegexAspath;
	}

	public String getMatchcriteriaRegexAspathName() {
		return this.matchcriteriaRegexAspathName;
	}

	public void setMatchcriteriaRegexAspathName(String matchcriteriaRegexAspathName) {
		this.matchcriteriaRegexAspathName = matchcriteriaRegexAspathName;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getSetcriteriaAcl() {
		return this.setcriteriaAcl;
	}

	public void setSetcriteriaAcl(Byte setcriteriaAcl) {
		this.setcriteriaAcl = setcriteriaAcl;
	}

	public Byte getSetcriteriaAspathPrepend() {
		return this.setcriteriaAspathPrepend;
	}

	public void setSetcriteriaAspathPrepend(Byte setcriteriaAspathPrepend) {
		this.setcriteriaAspathPrepend = setcriteriaAspathPrepend;
	}

	public String getSetcriteriaAspathPrependIndex() {
		return this.setcriteriaAspathPrependIndex;
	}

	public void setSetcriteriaAspathPrependIndex(String setcriteriaAspathPrependIndex) {
		this.setcriteriaAspathPrependIndex = setcriteriaAspathPrependIndex;
	}

	public String getSetcriteriaAspathPrependName() {
		return this.setcriteriaAspathPrependName;
	}

	public void setSetcriteriaAspathPrependName(String setcriteriaAspathPrependName) {
		this.setcriteriaAspathPrependName = setcriteriaAspathPrependName;
	}

	public Byte getSetcriteriaLocalPreference() {
		return this.setcriteriaLocalPreference;
	}

	public void setSetcriteriaLocalPreference(Byte setcriteriaLocalPreference) {
		this.setcriteriaLocalPreference = setcriteriaLocalPreference;
	}

	public String getSetcriteriaLocalpreferenceName() {
		return this.setcriteriaLocalpreferenceName;
	}

	public void setSetcriteriaLocalpreferenceName(String setcriteriaLocalpreferenceName) {
		this.setcriteriaLocalpreferenceName = setcriteriaLocalpreferenceName;
	}

	public Byte getSetcriteriaMetric() {
		return this.setcriteriaMetric;
	}

	public void setSetcriteriaMetric(Byte setcriteriaMetric) {
		this.setcriteriaMetric = setcriteriaMetric;
	}

	public String getSetcriteriaMetricName() {
		return this.setcriteriaMetricName;
	}

	public void setSetcriteriaMetricName(String setcriteriaMetricName) {
		this.setcriteriaMetricName = setcriteriaMetricName;
	}

	public Byte getSetcriteriaNeighbourCommunity() {
		return this.setcriteriaNeighbourCommunity;
	}

	public void setSetcriteriaNeighbourCommunity(Byte setcriteriaNeighbourCommunity) {
		this.setcriteriaNeighbourCommunity = setcriteriaNeighbourCommunity;
	}

	

	public Byte getSetcriteriaRegexAspath() {
		return this.setcriteriaRegexAspath;
	}

	public void setSetcriteriaRegexAspath(Byte setcriteriaRegexAspath) {
		this.setcriteriaRegexAspath = setcriteriaRegexAspath;
	}

	public String getSetcriteriaRegexAspathName() {
		return this.setcriteriaRegexAspathName;
	}

	public void setSetcriteriaRegexAspathName(String setcriteriaRegexAspathName) {
		this.setcriteriaRegexAspathName = setcriteriaRegexAspathName;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getTermName() {
		return this.termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public String getTermSetcriteriaAction() {
		return this.termSetcriteriaAction;
	}

	public void setTermSetcriteriaAction(String termSetcriteriaAction) {
		this.termSetcriteriaAction = termSetcriteriaAction;
	}

	public Set<AclPolicyCriteria> getAclPolicyCriterias() {
		return this.aclPolicyCriterias;
	}

	public void setAclPolicyCriterias(Set<AclPolicyCriteria> aclPolicyCriterias) {
		this.aclPolicyCriterias = aclPolicyCriterias;
	}

	public AclPolicyCriteria addAclPolicyCriteria(AclPolicyCriteria aclPolicyCriteria) {
		getAclPolicyCriterias().add(aclPolicyCriteria);
		aclPolicyCriteria.setPolicyCriteria(this);

		return aclPolicyCriteria;
	}

	public AclPolicyCriteria removeAclPolicyCriteria(AclPolicyCriteria aclPolicyCriteria) {
		getAclPolicyCriterias().remove(aclPolicyCriteria);
		aclPolicyCriteria.setPolicyCriteria(null);

		return aclPolicyCriteria;
	}

	public Set<NeighbourCommunityConfig> getNeighbourCommunityConfigs() {
		return this.neighbourCommunityConfigs;
	}

	public void setNeighbourCommunityConfigs(Set<NeighbourCommunityConfig> neighbourCommunityConfigs) {
		this.neighbourCommunityConfigs = neighbourCommunityConfigs;
	}

	public NeighbourCommunityConfig addNeighbourCommunityConfig(NeighbourCommunityConfig neighbourCommunityConfig) {
		getNeighbourCommunityConfigs().add(neighbourCommunityConfig);
		neighbourCommunityConfig.setPolicyCriteria(this);

		return neighbourCommunityConfig;
	}

	public NeighbourCommunityConfig removeNeighbourCommunityConfig(NeighbourCommunityConfig neighbourCommunityConfig) {
		getNeighbourCommunityConfigs().remove(neighbourCommunityConfig);
		neighbourCommunityConfig.setPolicyCriteria(null);

		return neighbourCommunityConfig;
	}

	public Set<PolicycriteriaProtocol> getPolicycriteriaProtocols() {
		return this.policycriteriaProtocols;
	}

	public void setPolicycriteriaProtocols(Set<PolicycriteriaProtocol> policycriteriaProtocols) {
		this.policycriteriaProtocols = policycriteriaProtocols;
	}

	public PolicycriteriaProtocol addPolicycriteriaProtocol(PolicycriteriaProtocol policycriteriaProtocol) {
		getPolicycriteriaProtocols().add(policycriteriaProtocol);
		policycriteriaProtocol.setPolicyCriteria(this);

		return policycriteriaProtocol;
	}

	public PolicycriteriaProtocol removePolicycriteriaProtocol(PolicycriteriaProtocol policycriteriaProtocol) {
		getPolicycriteriaProtocols().remove(policycriteriaProtocol);
		policycriteriaProtocol.setPolicyCriteria(null);

		return policycriteriaProtocol;
	}

	public Set<PrefixlistConfig> getPrefixlistConfigs() {
		return this.prefixlistConfigs;
	}

	public void setPrefixlistConfigs(Set<PrefixlistConfig> prefixlistConfigs) {
		this.prefixlistConfigs = prefixlistConfigs;
	}

	public PrefixlistConfig addPrefixlistConfig(PrefixlistConfig prefixlistConfig) {
		getPrefixlistConfigs().add(prefixlistConfig);
		prefixlistConfig.setPolicyCriteria(this);

		return prefixlistConfig;
	}

	public PrefixlistConfig removePrefixlistConfig(PrefixlistConfig prefixlistConfig) {
		getPrefixlistConfigs().remove(prefixlistConfig);
		prefixlistConfig.setPolicyCriteria(null);

		return prefixlistConfig;
	}

	public Set<RegexAspathConfig> getRegexAspathConfigs() {
		return this.regexAspathConfigs;
	}

	public void setRegexAspathConfigs(Set<RegexAspathConfig> regexAspathConfigs) {
		this.regexAspathConfigs = regexAspathConfigs;
	}

	public RegexAspathConfig addRegexAspathConfig(RegexAspathConfig regexAspathConfig) {
		getRegexAspathConfigs().add(regexAspathConfig);
		regexAspathConfig.setPolicyCriteria(this);

		return regexAspathConfig;
	}

	public RegexAspathConfig removeRegexAspathConfig(RegexAspathConfig regexAspathConfig) {
		getRegexAspathConfigs().remove(regexAspathConfig);
		regexAspathConfig.setPolicyCriteria(null);

		return regexAspathConfig;
	}

	public Byte getMatchcriteriaPprefixlistPreprovisioned() {
		return matchcriteriaPprefixlistPreprovisioned;
	}

	public void setMatchcriteriaPprefixlistPreprovisioned(Byte matchcriteriaPprefixlistPreprovisioned) {
		this.matchcriteriaPprefixlistPreprovisioned = matchcriteriaPprefixlistPreprovisioned;
	}

	public String getMatchcriteriaRegexAspathAspath() {
		return matchcriteriaRegexAspathAspath;
	}

	public void setMatchcriteriaRegexAspathAspath(String matchcriteriaRegexAspathAspath) {
		this.matchcriteriaRegexAspathAspath = matchcriteriaRegexAspathAspath;
	}

	public String getSetcriteriaRegexAspathAspath() {
		return setcriteriaRegexAspathAspath;
	}

	public void setSetcriteriaRegexAspathAspath(String setcriteriaRegexAspathAspath) {
		this.setcriteriaRegexAspathAspath = setcriteriaRegexAspathAspath;
	}

}