package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the order_teams_license database table.
 * 
 * @author Srinivasa Raghavan
 */
@Entity
@Table(name = "order_teams_license")
@NamedQuery(name = "OrderTeamsLicense.findAll", query = "SELECT o FROM OrderTeamsLicense o")
public class OrderTeamsLicense implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "agreement_type")
	private String agreementType;

	private Double arc;

	@Column(name = "contract_period")
	private String contractPeriod;

	@Column(name = "created_time")
	private Date createdTime;

	@Column(name = "license_name")
	private String licenseName;

	private Double mrc;

	@Column(name = "no_of_licenses")
	private Integer noOfLicenses;

	private Double nrc;

	@Column(name = "order_version")
	private Integer orderVersion;

	private String provider;

	private Double tcv;

	// bi-directional many-to-one association to OrderTeamsDR
	@ManyToOne
	@JoinColumn(name = "order_teamsdr_id")
	private OrderTeamsDR orderTeamsDR;

	@Column(name = "sfdc_product_name")
	private String sfdcProductName;

	public OrderTeamsLicense() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAgreementType() {
		return this.agreementType;
	}

	public void setAgreementType(String agreementType) {
		this.agreementType = agreementType;
	}

	public Double getArc() {
		return this.arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public String getContractPeriod() {
		return this.contractPeriod;
	}

	public void setContractPeriod(String contractPeriod) {
		this.contractPeriod = contractPeriod;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getLicenseName() {
		return this.licenseName;
	}

	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}

	public Double getMrc() {
		return this.mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Integer getNoOfLicenses() {
		return this.noOfLicenses;
	}

	public void setNoOfLicenses(Integer noOfLicenses) {
		this.noOfLicenses = noOfLicenses;
	}

	public Double getNrc() {
		return this.nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Integer getOrderVersion() {
		return this.orderVersion;
	}

	public void setOrderVersion(Integer orderVersion) {
		this.orderVersion = orderVersion;
	}

	public String getProvider() {
		return this.provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Double getTcv() {
		return this.tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public OrderTeamsDR getOrderTeamsDR() {
		return this.orderTeamsDR;
	}

	public void setOrderTeamsDR(OrderTeamsDR orderTeamsDR) {
		this.orderTeamsDR = orderTeamsDR;
	}

	public String getSfdcProductName() {
		return sfdcProductName;
	}

	public void setSfdcProductName(String sfdcProductName) {
		this.sfdcProductName = sfdcProductName;
	}
}