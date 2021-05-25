package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

/**
 * 
 * Rip Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "rip")
@NamedQuery(name="Rip.findAll", query="SELECT r FROM Rip r")
public class Rip implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="rip_id")
	private Integer ripId;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="group_name")
	private String groupName;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="local_preference")
	private String localPreference;

	@Column(name="local_preference_v6")
	private String localPreferenceV6;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="start_date")
	private Timestamp startDate;

	private String version;

	//bi-directional many-to-one association to InterfaceProtocolMapping
	@OneToMany(mappedBy="rip")
	private Set<InterfaceProtocolMapping> interfaceProtocolMappings;

	//bi-directional many-to-one association to PolicyType
	@OneToMany(mappedBy="rip")
	private Set<PolicyType> policyTypes;

	public Rip() {
	}

	public Integer getRipId() {
		return this.ripId;
	}

	public void setRipId(Integer ripId) {
		this.ripId = ripId;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLocalPreference() {
		return this.localPreference;
	}

	public void setLocalPreference(String localPreference) {
		this.localPreference = localPreference;
	}

	public String getLocalPreferenceV6() {
		return this.localPreferenceV6;
	}

	public void setLocalPreferenceV6(String localPreferenceV6) {
		this.localPreferenceV6 = localPreferenceV6;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Set<InterfaceProtocolMapping> getInterfaceProtocolMappings() {
		return this.interfaceProtocolMappings;
	}

	public void setInterfaceProtocolMappings(Set<InterfaceProtocolMapping> interfaceProtocolMappings) {
		this.interfaceProtocolMappings = interfaceProtocolMappings;
	}

	public InterfaceProtocolMapping addInterfaceProtocolMapping(InterfaceProtocolMapping interfaceProtocolMapping) {
		getInterfaceProtocolMappings().add(interfaceProtocolMapping);
		interfaceProtocolMapping.setRip(this);

		return interfaceProtocolMapping;
	}

	public InterfaceProtocolMapping removeInterfaceProtocolMapping(InterfaceProtocolMapping interfaceProtocolMapping) {
		getInterfaceProtocolMappings().remove(interfaceProtocolMapping);
		interfaceProtocolMapping.setRip(null);

		return interfaceProtocolMapping;
	}

	public Set<PolicyType> getPolicyTypes() {
		return this.policyTypes;
	}

	public void setPolicyTypes(Set<PolicyType> policyTypes) {
		this.policyTypes = policyTypes;
	}

	public PolicyType addPolicyType(PolicyType policyType) {
		getPolicyTypes().add(policyType);
		policyType.setRip(this);

		return policyType;
	}

	public PolicyType removePolicyType(PolicyType policyType) {
		getPolicyTypes().remove(policyType);
		policyType.setRip(null);

		return policyType;
	}

}