package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * ServiceQo Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="service_qos")
@NamedQuery(name="ServiceQo.findAll", query="SELECT s FROM ServiceQo s")
public class ServiceQo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="service_qos_id")
	private Integer serviceQosId;

	@Column(name="childqos_policyname")
	private String childqosPolicyname;

	@Column(name="cos_package")
	private String cosPackage;

	@Column(name="cos_profile")
	private String cosProfile;

	@Column(name="cos_type")
	private String cosType;

	@Column(name="cos_update_action")
	private String cosUpdateAction;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="flexi_cos_identifier")
	private String flexiCosIdentifier;

	@Column(name="isbandwidth_applicable")
	private Byte isbandwidthApplicable;

	@Column(name="isdefault_fc")
	private Byte isdefaultFc;

	private Byte isflexicos;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="nc_traffic")
	private Byte ncTraffic;

	@Column(name="pir_bw")
	private String pirBw;

	@Column(name="pir_bw_unit")
	private String pirBwUnit;

	@Column(name="qos_policyname")
	private String qosPolicyname;

	@Column(name="qos_trafiic_mode")
	private String qosTrafiicMode;

	@Column(name="start_date")
	private Timestamp startDate;

	@Column(name="summation_of_bw")
	private String summationOfBw;

	//bi-directional many-to-one association to ServiceCosCriteria
	@OneToMany(mappedBy="serviceQo", fetch = FetchType.EAGER)
	@OrderBy("serviceCosId ASC")
	private Set<ServiceCosCriteria> serviceCosCriterias;

	//bi-directional many-to-one association to ServiceDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="service_details_id")
	private ServiceDetail serviceDetail;

	public ServiceQo() {
	}

	public Integer getServiceQosId() {
		return this.serviceQosId;
	}

	public void setServiceQosId(Integer serviceQosId) {
		this.serviceQosId = serviceQosId;
	}

	public String getChildqosPolicyname() {
		return this.childqosPolicyname;
	}

	public void setChildqosPolicyname(String childqosPolicyname) {
		this.childqosPolicyname = childqosPolicyname;
	}

	public String getCosPackage() {
		return this.cosPackage;
	}

	public void setCosPackage(String cosPackage) {
		this.cosPackage = cosPackage;
	}

	public String getCosProfile() {
		return this.cosProfile;
	}

	public void setCosProfile(String cosProfile) {
		this.cosProfile = cosProfile;
	}

	public String getCosType() {
		return this.cosType;
	}

	public void setCosType(String cosType) {
		this.cosType = cosType;
	}

	public String getCosUpdateAction() {
		return this.cosUpdateAction;
	}

	public void setCosUpdateAction(String cosUpdateAction) {
		this.cosUpdateAction = cosUpdateAction;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getFlexiCosIdentifier() {
		return this.flexiCosIdentifier;
	}

	public void setFlexiCosIdentifier(String flexiCosIdentifier) {
		this.flexiCosIdentifier = flexiCosIdentifier;
	}

	public Byte getIsbandwidthApplicable() {
		return this.isbandwidthApplicable;
	}

	public void setIsbandwidthApplicable(Byte isbandwidthApplicable) {
		this.isbandwidthApplicable = isbandwidthApplicable;
	}

	public Byte getIsdefaultFc() {
		return this.isdefaultFc;
	}

	public void setIsdefaultFc(Byte isdefaultFc) {
		this.isdefaultFc = isdefaultFc;
	}

	public Byte getIsflexicos() {
		return this.isflexicos;
	}

	public void setIsflexicos(Byte isflexicos) {
		this.isflexicos = isflexicos;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Byte getNcTraffic() {
		return this.ncTraffic;
	}

	public void setNcTraffic(Byte ncTraffic) {
		this.ncTraffic = ncTraffic;
	}

	public String getPirBw() {
		return this.pirBw;
	}

	public void setPirBw(String pirBw) {
		this.pirBw = pirBw;
	}

	public String getPirBwUnit() {
		return this.pirBwUnit;
	}

	public void setPirBwUnit(String pirBwUnit) {
		this.pirBwUnit = pirBwUnit;
	}

	public String getQosPolicyname() {
		return this.qosPolicyname;
	}

	public void setQosPolicyname(String qosPolicyname) {
		this.qosPolicyname = qosPolicyname;
	}

	public String getQosTrafiicMode() {
		return this.qosTrafiicMode;
	}

	public void setQosTrafiicMode(String qosTrafiicMode) {
		this.qosTrafiicMode = qosTrafiicMode;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getSummationOfBw() {
		return this.summationOfBw;
	}

	public void setSummationOfBw(String summationOfBw) {
		this.summationOfBw = summationOfBw;
	}

	public Set<ServiceCosCriteria> getServiceCosCriterias() {
		return this.serviceCosCriterias;
	}

	public void setServiceCosCriterias(Set<ServiceCosCriteria> serviceCosCriterias) {
		this.serviceCosCriterias = serviceCosCriterias;
	}

	public ServiceCosCriteria addServiceCosCriteria(ServiceCosCriteria serviceCosCriteria) {
		getServiceCosCriterias().add(serviceCosCriteria);
		serviceCosCriteria.setServiceQo(this);

		return serviceCosCriteria;
	}

	public ServiceCosCriteria removeServiceCosCriteria(ServiceCosCriteria serviceCosCriteria) {
		getServiceCosCriterias().remove(serviceCosCriteria);
		serviceCosCriteria.setServiceQo(null);

		return serviceCosCriteria;
	}

	public ServiceDetail getServiceDetail() {
		return this.serviceDetail;
	}

	public void setServiceDetail(ServiceDetail serviceDetail) {
		this.serviceDetail = serviceDetail;
	}

}