package com.tcl.dias.oms.entity.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This file contains the QuoteDirectRouting.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "quote_teams_license")
@NamedQuery(name = "QuoteTeamsLicense.findAll", query = "SELECT q FROM QuoteTeamsLicense q")
public class QuoteTeamsLicense {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	// bi-directional many-to-one association to QuoteTeamsDR
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quote_teamsdr_id")
	private QuoteTeamsDR quoteTeamsDR;

	@Column(name = "no_of_licenses")
	private Integer noOfLicenses;

	@Column(name = "license_name")
	private String licenseName;

	@Column(name = "agreement_type")
	private String agreementType;

	@Column(name = "contract_period")
	private String contractPeriod;

	private String provider;

	@Column(name = "mrc")
	private Double mrc;

	@Column(name = "nrc")
	private Double nrc;

	@Column(name = "arc")
	private Double arc;

	@Column(name = "tcv")
	private Double tcv;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name = "quote_version")
	private Integer quoteVersion;

	@Column(name = "sfdc_product_name")
	private String sfdcProductName;

	public QuoteTeamsLicense() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNoOfLicenses() {
		return noOfLicenses;
	}

	public void setNoOfLicenses(Integer noOfLicenses) {
		this.noOfLicenses = noOfLicenses;
	}

	public String getLicenseName() {
		return licenseName;
	}

	public void setLicenseName(String licenseName) {
		this.licenseName = licenseName;
	}

	public String getAgreementType() {
		return agreementType;
	}

	public void setAgreementType(String agreementType) {
		this.agreementType = agreementType;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getQuoteVersion() {
		return quoteVersion;
	}

	public void setQuoteVersion(Integer quoteVersion) {
		this.quoteVersion = quoteVersion;
	}

	public String getContractPeriod() {
		return contractPeriod;
	}

	public void setContractPeriod(String contractPeriod) {
		this.contractPeriod = contractPeriod;
	}

	public QuoteTeamsDR getQuoteTeamsDR() {
		return quoteTeamsDR;
	}

	public void setQuoteTeamsDR(QuoteTeamsDR quoteTeamsDR) {
		this.quoteTeamsDR = quoteTeamsDR;
	}

	public String getSfdcProductName() {
		return sfdcProductName;
	}

	public void setSfdcProductName(String sfdcProductName) {
		this.sfdcProductName = sfdcProductName;
	}
}
