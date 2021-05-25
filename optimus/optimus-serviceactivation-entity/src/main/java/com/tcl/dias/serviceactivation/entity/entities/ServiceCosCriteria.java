package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


/**
 * 
 * ServiceCosCriteria Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="service_cos_criteria")
@NamedQuery(name="ServiceCosCriteria.findAll", query="SELECT s FROM ServiceCosCriteria s")
public class ServiceCosCriteria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="service_cos_id")
	private Integer serviceCosId;

	@Column(name="bw_bpsunit")
	private String bwBpsunit;

	@Column(name="classification_criteria")
	private String classificationCriteria;

	@Column(name="cos_name")
	private String cosName;

	@Column(name="cos_percent")
	private String cosPercent;

	@Column(name="dhcp_val1")
	private String dhcpVal1;

	@Column(name="dhcp_val2")
	private String dhcpVal2;

	@Column(name="dhcp_val3")
	private String dhcpVal3;

	@Column(name="dhcp_val4")
	private String dhcpVal4;

	@Column(name="dhcp_val5")
	private String dhcpVal5;

	@Column(name="dhcp_val6")
	private String dhcpVal6;

	@Column(name="dhcp_val7")
	private String dhcpVal7;

	@Column(name="dhcp_val8")
	private String dhcpVal8;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="ipprecedence_val1")
	private String ipprecedenceVal1;

	@Column(name="ipprecedence_val2")
	private String ipprecedenceVal2;

	@Column(name="ipprecedence_val3")
	private String ipprecedenceVal3;

	@Column(name="ipprecedence_val4")
	private String ipprecedenceVal4;

	@Column(name="ipprecedence_val5")
	private String ipprecedenceVal5;

	@Column(name="ipprecedence_val6")
	private String ipprecedenceVal6;

	@Column(name="ipprecedence_val7")
	private String ipprecedenceVal7;

	@Column(name="ipprecedence_val8")
	private String ipprecedenceVal8;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="start_date")
	private Timestamp startDate;

	//bi-directional many-to-one association to AclPolicyCriteria
	@OneToMany(mappedBy="serviceCosCriteria")
	@OrderBy("aclPolicyId ASC")
	private Set<AclPolicyCriteria> aclPolicyCriterias;

	//bi-directional many-to-one association to ServiceQo
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="service_qos_service_qos_id")
	private ServiceQo serviceQo;

	public ServiceCosCriteria() {
	}

	public Integer getServiceCosId() {
		return this.serviceCosId;
	}

	public void setServiceCosId(Integer serviceCosId) {
		this.serviceCosId = serviceCosId;
	}

	public String getBwBpsunit() {
		return this.bwBpsunit;
	}

	public void setBwBpsunit(String bwBpsunit) {
		this.bwBpsunit = bwBpsunit;
	}

	public String getClassificationCriteria() {
		return this.classificationCriteria;
	}

	public void setClassificationCriteria(String classificationCriteria) {
		this.classificationCriteria = classificationCriteria;
	}

	public String getCosName() {
		return this.cosName;
	}

	public void setCosName(String cosName) {
		this.cosName = cosName;
	}

	public String getCosPercent() {
		return this.cosPercent;
	}

	public void setCosPercent(String cosPercent) {
		this.cosPercent = cosPercent;
	}

	public String getDhcpVal1() {
		return this.dhcpVal1;
	}

	public void setDhcpVal1(String dhcpVal1) {
		this.dhcpVal1 = dhcpVal1;
	}

	public String getDhcpVal2() {
		return this.dhcpVal2;
	}

	public void setDhcpVal2(String dhcpVal2) {
		this.dhcpVal2 = dhcpVal2;
	}

	public String getDhcpVal3() {
		return this.dhcpVal3;
	}

	public void setDhcpVal3(String dhcpVal3) {
		this.dhcpVal3 = dhcpVal3;
	}

	public String getDhcpVal4() {
		return this.dhcpVal4;
	}

	public void setDhcpVal4(String dhcpVal4) {
		this.dhcpVal4 = dhcpVal4;
	}

	public String getDhcpVal5() {
		return this.dhcpVal5;
	}

	public void setDhcpVal5(String dhcpVal5) {
		this.dhcpVal5 = dhcpVal5;
	}

	public String getDhcpVal6() {
		return this.dhcpVal6;
	}

	public void setDhcpVal6(String dhcpVal6) {
		this.dhcpVal6 = dhcpVal6;
	}

	public String getDhcpVal7() {
		return this.dhcpVal7;
	}

	public void setDhcpVal7(String dhcpVal7) {
		this.dhcpVal7 = dhcpVal7;
	}

	public String getDhcpVal8() {
		return this.dhcpVal8;
	}

	public void setDhcpVal8(String dhcpVal8) {
		this.dhcpVal8 = dhcpVal8;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getIpprecedenceVal1() {
		return this.ipprecedenceVal1;
	}

	public void setIpprecedenceVal1(String ipprecedenceVal1) {
		this.ipprecedenceVal1 = ipprecedenceVal1;
	}

	public String getIpprecedenceVal2() {
		return this.ipprecedenceVal2;
	}

	public void setIpprecedenceVal2(String ipprecedenceVal2) {
		this.ipprecedenceVal2 = ipprecedenceVal2;
	}

	public String getIpprecedenceVal3() {
		return this.ipprecedenceVal3;
	}

	public void setIpprecedenceVal3(String ipprecedenceVal3) {
		this.ipprecedenceVal3 = ipprecedenceVal3;
	}

	public String getIpprecedenceVal4() {
		return this.ipprecedenceVal4;
	}

	public void setIpprecedenceVal4(String ipprecedenceVal4) {
		this.ipprecedenceVal4 = ipprecedenceVal4;
	}

	public String getIpprecedenceVal5() {
		return this.ipprecedenceVal5;
	}

	public void setIpprecedenceVal5(String ipprecedenceVal5) {
		this.ipprecedenceVal5 = ipprecedenceVal5;
	}

	public String getIpprecedenceVal6() {
		return this.ipprecedenceVal6;
	}

	public void setIpprecedenceVal6(String ipprecedenceVal6) {
		this.ipprecedenceVal6 = ipprecedenceVal6;
	}

	public String getIpprecedenceVal7() {
		return this.ipprecedenceVal7;
	}

	public void setIpprecedenceVal7(String ipprecedenceVal7) {
		this.ipprecedenceVal7 = ipprecedenceVal7;
	}

	public String getIpprecedenceVal8() {
		return this.ipprecedenceVal8;
	}

	public void setIpprecedenceVal8(String ipprecedenceVal8) {
		this.ipprecedenceVal8 = ipprecedenceVal8;
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

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Set<AclPolicyCriteria> getAclPolicyCriterias() {
		return this.aclPolicyCriterias;
	}

	public void setAclPolicyCriterias(Set<AclPolicyCriteria> aclPolicyCriterias) {
		this.aclPolicyCriterias = aclPolicyCriterias;
	}

	public AclPolicyCriteria addAclPolicyCriteria(AclPolicyCriteria aclPolicyCriteria) {
		getAclPolicyCriterias().add(aclPolicyCriteria);
		aclPolicyCriteria.setServiceCosCriteria(this);

		return aclPolicyCriteria;
	}

	public AclPolicyCriteria removeAclPolicyCriteria(AclPolicyCriteria aclPolicyCriteria) {
		getAclPolicyCriterias().remove(aclPolicyCriteria);
		aclPolicyCriteria.setServiceCosCriteria(null);

		return aclPolicyCriteria;
	}

	public ServiceQo getServiceQo() {
		return this.serviceQo;
	}

	public void setServiceQo(ServiceQo serviceQo) {
		this.serviceQo = serviceQo;
	}

}