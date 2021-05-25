package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * 
 * AluSchedulerPolicy Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="alu_scheduler_policy")
@NamedQuery(name="AluSchedulerPolicy.findAll", query="SELECT a FROM AluSchedulerPolicy a")
public class AluSchedulerPolicy implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="alu_scheduler_policy_id")
	private Integer aluSchedulerPolicyId;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="sap_egress_policyname")
	private String sapEgressPolicyname;

	@Column(name="sap_egress_preprovisioned")
	private Byte sapEgressPreprovisioned;

	@Column(name="sap_ingress_policyname")
	private String sapIngressPolicyname;

	@Column(name="sap_ingress_preprovisioned")
	private Byte sapIngressPreprovisioned;

	@Column(name="scheduler_policy_ispreprovisioned")
	private Byte schedulerPolicyIspreprovisioned;

	@Column(name="scheduler_policy_name")
	private String schedulerPolicyName;

	@Column(name="start_date")
	private Timestamp startDate;

	@Column(name="total_cir_bw")
	private String totalCirBw;

	@Column(name="total_pir_bw")
	private String totalPirBw;

	//bi-directional many-to-one association to ServiceDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="service_details_id")
	private ServiceDetail serviceDetail;

	public AluSchedulerPolicy() {
	}

	public Integer getAluSchedulerPolicyId() {
		return this.aluSchedulerPolicyId;
	}

	public void setAluSchedulerPolicyId(Integer aluSchedulerPolicyId) {
		this.aluSchedulerPolicyId = aluSchedulerPolicyId;
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

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getSapEgressPolicyname() {
		return this.sapEgressPolicyname;
	}

	public void setSapEgressPolicyname(String sapEgressPolicyname) {
		this.sapEgressPolicyname = sapEgressPolicyname;
	}

	public Byte getSapEgressPreprovisioned() {
		return this.sapEgressPreprovisioned;
	}

	public void setSapEgressPreprovisioned(Byte sapEgressPreprovisioned) {
		this.sapEgressPreprovisioned = sapEgressPreprovisioned;
	}

	public String getSapIngressPolicyname() {
		return this.sapIngressPolicyname;
	}

	public void setSapIngressPolicyname(String sapIngressPolicyname) {
		this.sapIngressPolicyname = sapIngressPolicyname;
	}

	public Byte getSapIngressPreprovisioned() {
		return this.sapIngressPreprovisioned;
	}

	public void setSapIngressPreprovisioned(Byte sapIngressPreprovisioned) {
		this.sapIngressPreprovisioned = sapIngressPreprovisioned;
	}

	public Byte getSchedulerPolicyIspreprovisioned() {
		return this.schedulerPolicyIspreprovisioned;
	}

	public void setSchedulerPolicyIspreprovisioned(Byte schedulerPolicyIspreprovisioned) {
		this.schedulerPolicyIspreprovisioned = schedulerPolicyIspreprovisioned;
	}

	public String getSchedulerPolicyName() {
		return this.schedulerPolicyName;
	}

	public void setSchedulerPolicyName(String schedulerPolicyName) {
		this.schedulerPolicyName = schedulerPolicyName;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getTotalCirBw() {
		return this.totalCirBw;
	}

	public void setTotalCirBw(String totalCirBw) {
		this.totalCirBw = totalCirBw;
	}

	public String getTotalPirBw() {
		return this.totalPirBw;
	}

	public void setTotalPirBw(String totalPirBw) {
		this.totalPirBw = totalPirBw;
	}

	public ServiceDetail getServiceDetail() {
		return this.serviceDetail;
	}

	public void setServiceDetail(ServiceDetail serviceDetail) {
		this.serviceDetail = serviceDetail;
	}

}