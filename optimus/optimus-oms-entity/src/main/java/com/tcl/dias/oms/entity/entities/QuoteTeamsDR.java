package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
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
 * The persistent class for the quote_teamsdr database table.
 * 
 * @author Srinivas Raghavan
 */
@Entity
@Table(name = "quote_teamsdr")
@NamedQuery(name = "QuoteTeamsDR.findAll", query = "SELECT q FROM QuoteTeamsDR q")
public class QuoteTeamsDR implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Double arc;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	private Double mrc;

	private Double nrc;

	@Column(name = "profile_name")
	private String profileName;

	@Column(name = "service_name")
	private String serviceName;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quote_to_le_id")
	private QuoteToLe quoteToLe;

	@Column(name = "quote_version")
	private Integer quoteVersion;

	private Byte status;

	private Double tcv;

	@Column(name = "no_of_users")
	private Integer noOfUsers;

	// bi-directional many-to-one association to ProductSolution
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_solution_id")
	private ProductSolution productSolution;

	@Column(name = "is_config")
	private Byte isConfig;

	public QuoteTeamsDR() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getArc() {
		return this.arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Double getMrc() {
		return this.mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return this.nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public String getProfileName() {
		return this.profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public QuoteToLe getQuoteToLe() {
		return this.quoteToLe;
	}

	public void setQuoteToLe(QuoteToLe quoteToLe) {
		this.quoteToLe = quoteToLe;
	}

	public Integer getQuoteVersion() {
		return this.quoteVersion;
	}

	public void setQuoteVersion(Integer quoteVersion) {
		this.quoteVersion = quoteVersion;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Double getTcv() {
		return this.tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public Integer getNoOfUsers() {
		return noOfUsers;
	}

	public void setNoOfUsers(Integer noOfUsers) {
		this.noOfUsers = noOfUsers;
	}

	public ProductSolution getProductSolution() {
		return productSolution;
	}

	public void setProductSolution(ProductSolution productSolution) {
		this.productSolution = productSolution;
	}

	public Byte getIsConfig() {
		return isConfig;
	}

	public void setIsConfig(Byte isConfig) {
		this.isConfig = isConfig;
	}
}
