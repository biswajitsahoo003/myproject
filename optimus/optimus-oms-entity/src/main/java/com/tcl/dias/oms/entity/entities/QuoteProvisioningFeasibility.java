package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

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
@Table(name = "quote_provisioning_feasibility")
@NamedQuery(name = "QuoteProvisioningFeasibility.findAll", query = "SELECT q FROM QuoteProvisioningFeasibility q")
public class QuoteProvisioningFeasibility implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Lob
	@Column(name = "data_json")
	private String dataJson;

	@Column(name = "feasibility_period")
	private String feasibilityPeriod;

	@Column(name = "product_offering_id")
	private Integer productOfferingId;

	private String provider;

	@Column(name = "provider_details")
	private String providerDetails;

	private String tech;

	// bi-directional many-to-one association to MstProductFamily
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_famiy_id")
	private MstProductFamily mstProductFamily;

	public QuoteProvisioningFeasibility() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDataJson() {
		return this.dataJson;
	}

	public void setDataJson(String dataJson) {
		this.dataJson = dataJson;
	}

	public String getFeasibilityPeriod() {
		return this.feasibilityPeriod;
	}

	public void setFeasibilityPeriod(String feasibilityPeriod) {
		this.feasibilityPeriod = feasibilityPeriod;
	}

	public Integer getProductOfferingId() {
		return this.productOfferingId;
	}

	public void setProductOfferingId(Integer productOfferingId) {
		this.productOfferingId = productOfferingId;
	}

	public String getProvider() {
		return this.provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getProviderDetails() {
		return this.providerDetails;
	}

	public void setProviderDetails(String providerDetails) {
		this.providerDetails = providerDetails;
	}

	public String getTech() {
		return this.tech;
	}

	public void setTech(String tech) {
		this.tech = tech;
	}

	public MstProductFamily getMstProductFamily() {
		return this.mstProductFamily;
	}

	public void setMstProductFamily(MstProductFamily mstProductFamily) {
		this.mstProductFamily = mstProductFamily;
	}

}