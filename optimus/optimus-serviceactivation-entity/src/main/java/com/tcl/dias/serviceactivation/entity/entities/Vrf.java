package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


/**
 * 
 * Vrf Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="vrf")
@NamedQuery(name="Vrf.findAll", query="SELECT v FROM Vrf v")
public class Vrf implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="vrf_id")
	private Integer vrfId;

	@Column(name="end_date")
	private Timestamp endDate;

	private Byte ismultivrf;

	@Column(name="isvrf_lite_enabled")
	private Byte isvrfLiteEnabled;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="mastervrf_serviceid")
	private String mastervrfServiceid;

	@Column(name="max_routes_value")
	private String maxRoutesValue;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="start_date")
	private Timestamp startDate;

	private String threshold;

	@Column(name="vrf_name")
	private String vrfName;

	@Column(name="warn_on")
	private String warnOn;

	//bi-directional many-to-one association to PolicyType
	@OneToMany(mappedBy="vrf")
	private Set<PolicyType> policyTypes;

	//bi-directional many-to-one association to ServiceDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="service_details_id")
	private ServiceDetail serviceDetail;

	@Column(name="vrf_project_name")
	private String vrfProjectName;

	@Column(name="slave_vrf_serviceid")
	private String slaveVrfServiceId;

	public Vrf() {
	}

	public Integer getVrfId() {
		return this.vrfId;
	}

	public void setVrfId(Integer vrfId) {
		this.vrfId = vrfId;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Byte getIsmultivrf() {
		return this.ismultivrf;
	}

	public void setIsmultivrf(Byte ismultivrf) {
		this.ismultivrf = ismultivrf;
	}

	public Byte getIsvrfLiteEnabled() {
		return this.isvrfLiteEnabled;
	}

	public void setIsvrfLiteEnabled(Byte isvrfLiteEnabled) {
		this.isvrfLiteEnabled = isvrfLiteEnabled;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getMastervrfServiceid() {
		return this.mastervrfServiceid;
	}

	public void setMastervrfServiceid(String mastervrfServiceid) {
		this.mastervrfServiceid = mastervrfServiceid;
	}

	public String getMaxRoutesValue() {
		return this.maxRoutesValue;
	}

	public void setMaxRoutesValue(String maxRoutesValue) {
		this.maxRoutesValue = maxRoutesValue;
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

	public String getThreshold() {
		return this.threshold;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}

	public String getVrfName() {
		return this.vrfName;
	}

	public void setVrfName(String vrfName) {
		this.vrfName = vrfName;
	}

	public String getWarnOn() {
		return this.warnOn;
	}

	public void setWarnOn(String warnOn) {
		this.warnOn = warnOn;
	}

	public Set<PolicyType> getPolicyTypes() {
		return this.policyTypes;
	}

	public void setPolicyTypes(Set<PolicyType> policyTypes) {
		this.policyTypes = policyTypes;
	}

	public PolicyType addPolicyType(PolicyType policyType) {
		getPolicyTypes().add(policyType);
		policyType.setVrf(this);

		return policyType;
	}

	public PolicyType removePolicyType(PolicyType policyType) {
		getPolicyTypes().remove(policyType);
		policyType.setVrf(null);

		return policyType;
	}

	public ServiceDetail getServiceDetail() {
		return this.serviceDetail;
	}

	public void setServiceDetail(ServiceDetail serviceDetail) {
		this.serviceDetail = serviceDetail;
	}

	public String getVrfProjectName() {
		return vrfProjectName;
	}

	public void setVrfProjectName(String vrfProjectName) {
		this.vrfProjectName = vrfProjectName;
	}

	public String getSlaveVrfServiceId() {
		return slaveVrfServiceId;
	}

	public void setSlaveVrfServiceId(String slaveVrfServiceId) {
		this.slaveVrfServiceId = slaveVrfServiceId;
	}

}