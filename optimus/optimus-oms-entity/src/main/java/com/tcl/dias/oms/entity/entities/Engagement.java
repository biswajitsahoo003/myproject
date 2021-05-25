package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "engagement")
@NamedQuery(name = "Engagement.findAll", query = "SELECT e FROM Engagement e")
public class Engagement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name = "engagement_name")
	private String engagementName;

	@Column(name = "erf_cus_customer_le_id")
	private Integer erfCusCustomerLeId;

	@Column(name = "erf_cus_sp_le_id")
	private Integer erfCusSpLeId;

	@Column(name = "engagement_type")
	private Integer enagementType;

	private Byte status;

	// bi-directional many-to-one association to Customer
	@ManyToOne(fetch = FetchType.LAZY)
	private Customer customer;

	// bi-directional many-to-one association to MstProductFamily
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_family_id")
	private MstProductFamily mstProductFamily;

	// bi-directional many-to-one association to Partner
	@ManyToOne(fetch = FetchType.LAZY)
	private Partner partner;

	@Column(name = "erf_cus_partner_le_id")
	private Integer erfCusPartnerLeId;

	// bi-directional many-to-one association to EngagementServiceSolution
	@OneToMany(mappedBy = "engagement")
	private Set<EngagementServiceSolution> engagementServiceSolutions;

	public Engagement() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getEngagementName() {
		return this.engagementName;
	}

	public void setEngagementName(String engagementName) {
		this.engagementName = engagementName;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public MstProductFamily getMstProductFamily() {
		return this.mstProductFamily;
	}

	public void setMstProductFamily(MstProductFamily mstProductFamily) {
		this.mstProductFamily = mstProductFamily;
	}

	public Partner getPartner() {
		return this.partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public Set<EngagementServiceSolution> getEngagementServiceSolutions() {
		return this.engagementServiceSolutions;
	}

	public void setEngagementServiceSolutions(Set<EngagementServiceSolution> engagementServiceSolutions) {
		this.engagementServiceSolutions = engagementServiceSolutions;
	}

	public Integer getErfCusCustomerLeId() {
		return erfCusCustomerLeId;
	}

	public void setErfCusCustomerLeId(Integer erfCusCustomerLeId) {
		this.erfCusCustomerLeId = erfCusCustomerLeId;
	}

	public Integer getErfCusSpLeId() {
		return erfCusSpLeId;
	}

	public void setErfCusSpLeId(Integer erfCusSpLeId) {
		this.erfCusSpLeId = erfCusSpLeId;
	}

	public Integer getEnagementType() {
		return enagementType;
	}

	public void setEnagementType(Integer enagementType) {
		this.enagementType = enagementType;
	}

	public Integer getErfCusPartnerLeId() {
		return erfCusPartnerLeId;
	}

	public void setErfCusPartnerLeId(Integer erfCusPartnerLeId) {
		this.erfCusPartnerLeId = erfCusPartnerLeId;
	}

	public EngagementServiceSolution addEngagementServiceSolution(EngagementServiceSolution engagementServiceSolution) {
		getEngagementServiceSolutions().add(engagementServiceSolution);
		engagementServiceSolution.setEngagement(this);

		return engagementServiceSolution;
	}

	public EngagementServiceSolution removeEngagementServiceSolution(
			EngagementServiceSolution engagementServiceSolution) {
		getEngagementServiceSolutions().remove(engagementServiceSolution);
		engagementServiceSolution.setEngagement(null);

		return engagementServiceSolution;
	}

}