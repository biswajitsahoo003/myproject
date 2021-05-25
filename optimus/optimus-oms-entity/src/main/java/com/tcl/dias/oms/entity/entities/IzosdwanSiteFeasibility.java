package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the izosdwan_site_feasibility database table.
 * 
 */
@Entity
@Table(name="izosdwan_site_feasibility")
@NamedQuery(name="IzosdwanSiteFeasibility.findAll", query="SELECT i FROM IzosdwanSiteFeasibility i")
public class IzosdwanSiteFeasibility implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_time")
	private Date createdTime;

	@Column(name="feasibility_check")
	private String feasibilityCheck;

	@Column(name="feasibility_code")
	private String feasibilityCode;

	@Column(name="feasibility_mode")
	private String feasibilityMode;

	@Column(name="feasibility_type")
	private String feasibilityType;

	@Column(name="is_selected")
	private Byte isSelected;

	private String provider;

	private Integer rank;

	@Lob
	@Column(name="response_json")
	private String responseJson;

	@Column(name="sfdc_feasibility_id")
	private String sfdcFeasibilityId;

	@Lob
	@Column(name="site_document")
	private byte[] siteDocument;

	@Column(name="site_document_name")
	private String siteDocumentName;

	@OneToOne
	@JoinColumn(name = "site_id")
	private QuoteIzosdwanSite quoteIzosdwanSite;

	private String type;
	
	//bi-directional many-to-one association to IzosdwanSiteFeasibilityAudit
	@OneToMany(mappedBy="izosdwanSiteFeasibility")
	private Set<IzosdwanSiteFeasibilityAudit> izosdwanSiteFeasibilityAudits;

	public IzosdwanSiteFeasibility() {
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

	public String getFeasibilityCheck() {
		return this.feasibilityCheck;
	}

	public void setFeasibilityCheck(String feasibilityCheck) {
		this.feasibilityCheck = feasibilityCheck;
	}

	public String getFeasibilityCode() {
		return this.feasibilityCode;
	}

	public void setFeasibilityCode(String feasibilityCode) {
		this.feasibilityCode = feasibilityCode;
	}

	public String getFeasibilityMode() {
		return this.feasibilityMode;
	}

	public void setFeasibilityMode(String feasibilityMode) {
		this.feasibilityMode = feasibilityMode;
	}

	public String getFeasibilityType() {
		return this.feasibilityType;
	}

	public void setFeasibilityType(String feasibilityType) {
		this.feasibilityType = feasibilityType;
	}

	public Byte getIsSelected() {
		return this.isSelected;
	}

	public void setIsSelected(Byte isSelected) {
		this.isSelected = isSelected;
	}

	public String getProvider() {
		return this.provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Integer getRank() {
		return this.rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public String getResponseJson() {
		return this.responseJson;
	}

	public void setResponseJson(String responseJson) {
		this.responseJson = responseJson;
	}

	public String getSfdcFeasibilityId() {
		return this.sfdcFeasibilityId;
	}

	public void setSfdcFeasibilityId(String sfdcFeasibilityId) {
		this.sfdcFeasibilityId = sfdcFeasibilityId;
	}

	public byte[] getSiteDocument() {
		return siteDocument;
	}

	public void setSiteDocument(byte[] siteDocument) {
		this.siteDocument = siteDocument;
	}

	public String getSiteDocumentName() {
		return this.siteDocumentName;
	}

	public void setSiteDocumentName(String siteDocumentName) {
		this.siteDocumentName = siteDocumentName;
	}

	

	public QuoteIzosdwanSite getQuoteIzosdwanSite() {
		return quoteIzosdwanSite;
	}

	public void setQuoteIzosdwanSite(QuoteIzosdwanSite quoteIzosdwanSite) {
		this.quoteIzosdwanSite = quoteIzosdwanSite;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<IzosdwanSiteFeasibilityAudit> getIzosdwanSiteFeasibilityAudits() {
		return izosdwanSiteFeasibilityAudits;
	}

	public void setIzosdwanSiteFeasibilityAudits(Set<IzosdwanSiteFeasibilityAudit> izosdwanSiteFeasibilityAudits) {
		this.izosdwanSiteFeasibilityAudits = izosdwanSiteFeasibilityAudits;
	}
	
}