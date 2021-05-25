package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * 
 * Entity Class for quote_npl_link table
 * 
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "quote_npl_link")
@NamedQuery(name = "QuoteNplLink.findAll", query = "SELECT q FROM QuoteNplLink q")
public class QuoteNplLink implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "link_code")
	private String linkCode;

	private Byte status;

	@Column(name = "site_A_id")
	private Integer siteAId;

	@Column(name = "site_B_id")
	private Integer siteBId;

	@Column(name = "product_solution_id")
	private Integer productSolutionId;

	@Column(name = "quote_id")
	private Integer quoteId;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "workflow_status")
	private String workflowStatus;

	@Column(name = "chargeable_distance")
	private String chargeableDistance;

	@Column(name = "link_type")
	private String linkType;
	
	@Column(name = "siteA_type")
	private String siteAType;
	
	@Column(name = "siteB_type")
	private String siteBType;
	
	@Column(name = "mrc")
	private Double mrc;

	@Column(name = "nrc")
	private Double nrc;

	@Column(name = "arc")
	private Double arc;
	
	@Column(name = "tcv")
	private Double tcv;
	
	@Column(name = "requestor_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date requestorDate;
	
	@Column(name = "effective_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date effectiveDate;
	
	@Column(name = "feasibility")
	private Byte feasibility;
	
	@Column(name = "fp_status")
	private String fpStatus;
	

	@Column(name = "mf_task_triggered")
	private Integer mfTaskTriggered;
	
	@Column(name = "mf_status")
	private String mfStatus;
	
	@Column(name = "is_task_triggered")
	private Integer isTaskTriggered;
	
	@Column(name = "commercial_rejection_status")
	private String commercialRejectionStatus;
	
	@Column(name = "commercial_approve_status")
	private String commercialApproveStatus;
	
	
	@Column(name = "link_bulk_update")
	private String linkBulkUpdate;	
	
	
	//added for sitewisebilling
	// bi-directional many-to-one association to QuoteSiteBillingDetails
	@OneToMany(mappedBy = "quoteNplLink")
	private Set<QuoteSiteBillingDetails> quoteSiteBillingDetails;
	
	
		
	

	public Set<QuoteSiteBillingDetails> getQuoteSiteBillingDetails() {
		return quoteSiteBillingDetails;
	}

	public void setQuoteSiteBillingDetails(Set<QuoteSiteBillingDetails> quoteSiteBillingDetails) {
		this.quoteSiteBillingDetails = quoteSiteBillingDetails;
	}

	public String getLinkBulkUpdate() {
		return linkBulkUpdate;
	}

	public void setLinkBulkUpdate(String linkBulkUpdate) {
		this.linkBulkUpdate = linkBulkUpdate;
	}

	public String getCommercialRejectionStatus() {
		return commercialRejectionStatus;
	}

	public void setCommercialRejectionStatus(String commercialRejectionStatus) {
		this.commercialRejectionStatus = commercialRejectionStatus;
	}

	public String getCommercialApproveStatus() {
		return commercialApproveStatus;
	}

	public void setCommercialApproveStatus(String commercialApproveStatus) {
		this.commercialApproveStatus = commercialApproveStatus;
	}

	public Integer getIsTaskTriggered() {
		return isTaskTriggered;
	}

	public void setIsTaskTriggered(Integer isTaskTriggered) {
		this.isTaskTriggered = isTaskTriggered;
	}

	public String getMfStatus() {
		return mfStatus;
	}

	public void setMfStatus(String mfStatus) {
		this.mfStatus = mfStatus;
	}

	public Integer getMfTaskTriggered() {
		return mfTaskTriggered;
	}

	public void setMfTaskTriggered(Integer mfTaskTriggered) {
		this.mfTaskTriggered = mfTaskTriggered;
	}

	// bi-directional many-to-one association to Product
	@OneToMany(mappedBy = "quoteNplLink")
	private List<LinkFeasibility> linkFeasibilities;
	
	public Byte getFeasibility() {
		return feasibility;
	}

	public void setFeasibility(Byte feasibility) {
		this.feasibility = feasibility;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	@OneToMany(mappedBy = "quoteNplLink")
	private Set<QuoteNplLinkSla> quoteNplLinkSlas;

	public Set<QuoteNplLinkSla> getQuoteNplLinkSlas() {
		return quoteNplLinkSlas;
	}

	public void setQuoteNplLinkSlas(Set<QuoteNplLinkSla> quoteNplLinkSlas) {
		this.quoteNplLinkSlas = quoteNplLinkSlas;
	}

	public Date getRequestorDate() {
		return requestorDate;
	}

	public void setRequestorDate(Date requestorDate) {
		this.requestorDate = requestorDate;
	}

	public QuoteNplLink() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLinkCode() {
		return linkCode;
	}

	public void setLinkCode(String linkCode) {
		this.linkCode = linkCode;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Integer getSiteAId() {
		return siteAId;
	}

	public void setSiteAId(Integer siteAId) {
		this.siteAId = siteAId;
	}

	public Integer getSiteBId() {
		return siteBId;
	}

	public void setSiteBId(Integer siteBId) {
		this.siteBId = siteBId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public Integer getQuoteId() {
		return quoteId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	public String getChargeableDistance() {
		return chargeableDistance;
	}

	public void setChargeableDistance(String chargeableDistance) {
		this.chargeableDistance = chargeableDistance;
	}

	public Integer getProductSolutionId() {
		return productSolutionId;
	}

	public void setProductSolutionId(Integer productSolutionId) {
		this.productSolutionId = productSolutionId;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public String getSiteAType() {
		return siteAType;
	}

	public void setSiteAType(String siteAType) {
		this.siteAType = siteAType;
	}

	public String getSiteBType() {
		return siteBType;
	}

	public void setSiteBType(String siteBType) {
		this.siteBType = siteBType;
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

	public String getFpStatus() {
		return fpStatus;
	}

	public void setFpStatus(String fpStatus) {
		this.fpStatus = fpStatus;
	}

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public List<LinkFeasibility> getLinkFeasibilities() {
		return linkFeasibilities;
	}

	public void setLinkFeasibilities(List<LinkFeasibility> linkFeasibilities) {
		this.linkFeasibilities = linkFeasibilities;
	}

}