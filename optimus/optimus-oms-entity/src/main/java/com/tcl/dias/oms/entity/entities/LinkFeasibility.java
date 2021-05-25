package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "link_feasibility")
@NamedQuery(name = "LinkFeasibility.findAll", query = "SELECT l FROM LinkFeasibility l")
public class LinkFeasibility implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	@JoinColumn(name = "link_id")
	private QuoteNplLink quoteNplLink;

	@Column(name = "feasibility_check")
	private String feasibilityCheck;

	@Column(name = "feasibility_mode")
	private String feasibilityMode;

	@Column(name = "feasibility_code")
	private String feasibilityCode;

	@Column(name = "provider")
	private String provider;

	private Integer rank;

	@Column(name = "response_json")
	private String responseJson;

	@Column(name = "is_selected")
	private Byte isSelected;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@Column(name = "type")
	private String type;
	
	@Column(name = "feasibility_mode_b")
	private String feasibilityModeB;
	
	@Column(name = "provider_b")
	private String providerB;

	@Column(name = "feasibility_type")
	private String feasibilityType;
	
	@Column(name = "feasibility_type_b")
	private String feasibilityTypeB;
	
	@Column(name = "site_document")
	private byte[] siteDocument;
	
	@Column(name ="site_document_name")
	private String siteDocumentName;
	
	@Column(name="cd_distance")
	private Integer cdDistance;
	
	
	

	public Integer getCdDistance() {
		return cdDistance;
	}

	public void setCdDistance(Integer cdDistance) {
		this.cdDistance = cdDistance;
	}

	public byte[] getSiteDocument() {
		return siteDocument;
	}

	public void setSiteDocument(byte[] siteDocument) {
		this.siteDocument = siteDocument;
	}

	public String getSiteDocumentName() {
		return siteDocumentName;
	}

	public void setSiteDocumentName(String siteDocumentName) {
		this.siteDocumentName = siteDocumentName;
	}

	public String getFeasibilityType() {
		return feasibilityType;
	}

	public void setFeasibilityType(String feasibilityType) {
		this.feasibilityType = feasibilityType;
	}

	public String getFeasibilityTypeB() {
		return feasibilityTypeB;
	}

	public void setFeasibilityTypeB(String feasibilityTypeB) {
		this.feasibilityTypeB = feasibilityTypeB;
	}

	public String getFeasibilityModeB() {
		return feasibilityModeB;
	}

	public void setFeasibilityModeB(String feasibilityModeB) {
		this.feasibilityModeB = feasibilityModeB;
	}

	public String getProviderB() {
		return providerB;
	}

	public void setProviderB(String providerB) {
		this.providerB = providerB;
	}

	public LinkFeasibility() {
		// DO NOTHING
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public QuoteNplLink getQuoteNplLink() {
		return quoteNplLink;
	}

	public void setQuoteNplLink(QuoteNplLink quoteNplLink) {
		this.quoteNplLink = quoteNplLink;
	}

	public String getFeasibilityCheck() {
		return feasibilityCheck;
	}

	public void setFeasibilityCheck(String feasibilityCheck) {
		this.feasibilityCheck = feasibilityCheck;
	}

	public String getFeasibilityMode() {
		return feasibilityMode;
	}

	public void setFeasibilityMode(String feasibilityMode) {
		this.feasibilityMode = feasibilityMode;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getResponseJson() {
		return responseJson;
	}

	public void setResponseJson(String responseJson) {
		this.responseJson = responseJson;
	}

	public Byte getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Byte isSelected) {
		this.isSelected = isSelected;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getFeasibilityCode() {
		return feasibilityCode;
	}

	public void setFeasibilityCode(String feasibilityCode) {
		this.feasibilityCode = feasibilityCode;
	}

}
