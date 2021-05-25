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
@Table(name = "quote_to_le")
@NamedQuery(name = "QuoteToLe.findAll", query = "SELECT q FROM QuoteToLe q")
public class QuoteToLe implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "quote_le_code")
	private String quoteLeCode;

	@Column(name = "currency_id")
	private Integer currencyId;

	@Column(name = "erf_cus_customer_legal_entity_id")
	private Integer erfCusCustomerLegalEntityId;

	@Column(name = "erf_cus_sp_legal_entity_id")
	private Integer erfCusSpLegalEntityId;

	@Column(name = "final_mrc")
	private Double finalMrc;

	@Column(name = "final_nrc")
	private Double finalNrc;

	@Column(name = "final_arc")
	private Double finalArc;

	@Column(name = "proposed_mrc")
	private Double proposedMrc;

	@Column(name = "proposed_nrc")
	private Double proposedNrc;

	@Column(name = "proposed_arc")
	private Double proposedArc;

	@Column(name = "total_tcv")
	private Double totalTcv;

	private String stage;

	@Column(name = "sub_stage")
	private String subStage;

	@Column(name = "tps_sfdc_opty_id")
	private String tpsSfdcOptyId;

	@Column(name = "quote_type")
	private String quoteType;

	@Column(name = "erf_service_inventory_parent_order_id")
	private Integer erfServiceInventoryParentOrderId;

	@Column(name = "tps_sfdc_parent_opty_id")
	private Integer tpsSfdcParentOptyId;

	@Column(name = "source_system")
	private String sourceSystem;

	@Column(name = "quote_category")
	private String quoteCategory;

	@Column(name = "is_bundle")
	private Character isBundle;
	
	@Column(name="change_request_summary")
	private String changeRequestSummary;

	@Column(name = "is_multicircuit")
	private Byte isMultiCircuit;

	@Column(name = "is_amended")
	private Byte isAmended;


	//for demo orders
	@Column(name = "is_demo")
	private Byte isDemo;

	@Column(name = "demo_type")
	private String demoType;


	// bi-directional many-to-one association to OmsAttachment
	@OneToMany(mappedBy = "quoteToLe")
	private Set<OmsAttachment> omsAttachments;

	// bi-directional many-to-one association to QuoteDelegation
	@OneToMany(mappedBy = "quoteToLe")
	private Set<QuoteDelegation> quoteDelegations;

	// bi-directional many-to-one association to QuoteLeAttributeValue
	@OneToMany(mappedBy = "quoteToLe")
	private Set<QuoteLeAttributeValue> quoteLeAttributeValues;

	// bi-directional many-to-one association to Quote
	@ManyToOne(fetch = FetchType.LAZY)
	private Quote quote;

	// bi-directional many-to-one association to QuoteToLeProductFamily
	@OneToMany(mappedBy = "quoteToLe")
	private Set<QuoteToLeProductFamily> quoteToLeProductFamilies;
	
	// bi-directional many-to-one association to QuoteToLeProductFamily
	@OneToMany(mappedBy = "quoteToLe")
	private Set<QuoteIllSiteToService> quoteIllSiteToServices;


	@Column(name="term_in_months")
	private String termInMonths;
	
	@Column(name = "currency_code")
	private String currencyCode;
	
	@Column(name = "erf_service_inventory_service_detail_id")
	private Integer erfServiceInventoryServiceDetailId;
	
	
	@Column(name = "erf_service_inventory_tps_service_id")
	private String erfServiceInventoryTpsServiceId;

	@Column(name = "classification")
	private String classification;
	
	@Column(name="preapproved_opportunity_flag")
	private Byte preapprovedOpportunityFlag;
	
	@Column(name="tps_sfdc_approved_mrc")
	private Double tpsSfdcApprovedMrc;
	
	@Column(name="tps_sfdc_approved_nrc")
	private Double tpsSfdcApprovedNrc;
	
	@Column(name="tps_sfdc_approved_by")
	private String tpsSfdcApprovedBy;
	
	@Column(name="tps_sfdc_reserved_by")
	private String tpsSfdcReservedBy;
	
	@Column(name = "tps_sfdc_credit_approval_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tpsSfdcCreditApprovalDate;
	
	@Column(name="tps_sfdc_credit_remarks")
	private String tpsSfdcCreditRemarks;
	
	@Column(name="tps_sfdc_differential_mrc")
	private Double tpsSfdcDifferentialMrc;
	
	@Column(name="tps_sfdc_status_credit_control")
	private String tpsSfdcStatusCreditControl;
	
	@Column(name="variation_approved_flag")
	private Byte variationApprovedFlag;
	
	@Column(name="tps_sfdc_security_deposit_amount")
	private Double tpsSfdcSecurityDepositAmount;
	
	@Column(name="tps_sfdc_credit_limit")
	private Double tpsSfdcCreditLimit;

	@Column(name="credit_check_triggered")
	private Byte creditCheckTriggered;
	
	 @Column(name = "commercial_status")
	 private String commercialStatus;

	 @Column(name = "amendment_parent_order_code")
	 private String amendmentParentOrderCode;
	 
	 @Column(name = "commercial_quote_rejection_status")
	 private String commercialQuoteRejectionStatus;
	 
	 @Column(name = "quote_rejection_comment")
	 private String quoteRejectionComment;
	 
	 @Column(name = "is_commercial_triggered")
	 private Integer  isCommercialTriggered;
	 
	 @Column(name = "is_wholesale")
	 private Byte isWholesale;
	 
	 //ADDED for multiVrf
	 @Column(name = "is_multi_vrf")
	 private String  isMultiVrf;
	  
	 @Column(name="cancelled_parent_order_code")
	 private String cancelledParentOrderCode;
	 
	 @Column(name = "quote_bulk_update")
	 private String quoteBulkUpdate;
	 
	 @Column(name = "sitelevel_billing")
	 private String siteLevelBilling;

	@Column(name="is_ack_mail_triggered")
	private Integer isAckMailTriggered;

	@Column(name="ack_mail_triggered_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date ackMailTriggeredDate;

	public String getQuoteBulkUpdate() {
		return quoteBulkUpdate;
	}

	public void setQuoteBulkUpdate(String quoteBulkUpdate) {
		this.quoteBulkUpdate = quoteBulkUpdate;
	}

	public String getIsMultiVrf() {
		return isMultiVrf;
	}

	public void setIsMultiVrf(String isMultiVrf) {
		this.isMultiVrf = isMultiVrf;
	}


	public Integer getIsCommercialTriggered() {
		return isCommercialTriggered;
	}

	public void setIsCommercialTriggered(Integer isCommercialTriggered) {
		this.isCommercialTriggered = isCommercialTriggered;
	}

	public String getCommercialQuoteRejectionStatus() {
		return commercialQuoteRejectionStatus;
	}

	public void setCommercialQuoteRejectionStatus(String commercialQuoteRejectionStatus) {
		this.commercialQuoteRejectionStatus = commercialQuoteRejectionStatus;
	}

	public String getQuoteRejectionComment() {
		return quoteRejectionComment;
	}

	public void setQuoteRejectionComment(String quoteRejectionComment) {
		this.quoteRejectionComment = quoteRejectionComment;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getTermInMonths() {
		return termInMonths;
	}

	public void setTermInMonths(String termInMonths) {
		this.termInMonths = termInMonths;
	}

	public QuoteToLe() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCurrencyId() {
		return this.currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Integer getErfCusCustomerLegalEntityId() {
		return this.erfCusCustomerLegalEntityId;
	}

	public void setErfCusCustomerLegalEntityId(Integer erfCusCustomerLegalEntityId) {
		this.erfCusCustomerLegalEntityId = erfCusCustomerLegalEntityId;
	}

	public Integer getErfCusSpLegalEntityId() {
		return this.erfCusSpLegalEntityId;
	}

	public void setErfCusSpLegalEntityId(Integer erfCusSpLegalEntityId) {
		this.erfCusSpLegalEntityId = erfCusSpLegalEntityId;
	}

	public Double getFinalMrc() {
		return this.finalMrc;
	}

	public void setFinalMrc(Double finalMrc) {
		this.finalMrc = finalMrc;
	}

	public Double getFinalNrc() {
		return this.finalNrc;
	}

	public void setFinalNrc(Double finalNrc) {
		this.finalNrc = finalNrc;
	}

	public Double getProposedMrc() {
		return this.proposedMrc;
	}

	public void setProposedMrc(Double proposedMrc) {
		this.proposedMrc = proposedMrc;
	}

	public Double getProposedNrc() {
		return this.proposedNrc;
	}

	public void setProposedNrc(Double proposedNrc) {
		this.proposedNrc = proposedNrc;
	}

	public String getStage() {
		return this.stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getSubStage() {
		return subStage;
	}

	public void setSubStage(String subStage) {
		this.subStage = subStage;
	}

	public String getTpsSfdcOptyId() {
		return this.tpsSfdcOptyId;
	}

	public void setTpsSfdcOptyId(String tpsSfdcOptyId) {
		this.tpsSfdcOptyId = tpsSfdcOptyId;
	}

	public Set<OmsAttachment> getOmsAttachments() {
		return this.omsAttachments;
	}

	public void setOmsAttachments(Set<OmsAttachment> omsAttachments) {
		this.omsAttachments = omsAttachments;
	}

	public OmsAttachment addOmsAttachment(OmsAttachment omsAttachment) {
		getOmsAttachments().add(omsAttachment);
		omsAttachment.setQuoteToLe(this);

		return omsAttachment;
	}

	public OmsAttachment removeOmsAttachment(OmsAttachment omsAttachment) {
		getOmsAttachments().remove(omsAttachment);
		omsAttachment.setQuoteToLe(null);

		return omsAttachment;
	}

	public Set<QuoteDelegation> getQuoteDelegations() {
		return this.quoteDelegations;
	}

	public void setQuoteDelegations(Set<QuoteDelegation> quoteDelegations) {
		this.quoteDelegations = quoteDelegations;
	}

	public QuoteDelegation addQuoteDelegation(QuoteDelegation quoteDelegation) {
		getQuoteDelegations().add(quoteDelegation);
		quoteDelegation.setQuoteToLe(this);

		return quoteDelegation;
	}

	public QuoteDelegation removeQuoteDelegation(QuoteDelegation quoteDelegation) {
		getQuoteDelegations().remove(quoteDelegation);
		quoteDelegation.setQuoteToLe(null);

		return quoteDelegation;
	}

	public Set<QuoteLeAttributeValue> getQuoteLeAttributeValues() {
		return this.quoteLeAttributeValues;
	}

	public void setQuoteLeAttributeValues(Set<QuoteLeAttributeValue> quoteLeAttributeValues) {
		this.quoteLeAttributeValues = quoteLeAttributeValues;
	}

	public QuoteLeAttributeValue addQuoteLeAttributeValue(QuoteLeAttributeValue quoteLeAttributeValue) {
		getQuoteLeAttributeValues().add(quoteLeAttributeValue);
		quoteLeAttributeValue.setQuoteToLe(this);

		return quoteLeAttributeValue;
	}

	public QuoteLeAttributeValue removeQuoteLeAttributeValue(QuoteLeAttributeValue quoteLeAttributeValue) {
		getQuoteLeAttributeValues().remove(quoteLeAttributeValue);
		quoteLeAttributeValue.setQuoteToLe(null);

		return quoteLeAttributeValue;
	}

	public Quote getQuote() {
		return this.quote;
	}

	public void setQuote(Quote quote) {
		this.quote = quote;
	}

	public Set<QuoteToLeProductFamily> getQuoteToLeProductFamilies() {
		return this.quoteToLeProductFamilies;
	}

	public void setQuoteToLeProductFamilies(Set<QuoteToLeProductFamily> quoteToLeProductFamilies) {
		this.quoteToLeProductFamilies = quoteToLeProductFamilies;
	}

	public QuoteToLeProductFamily addQuoteToLeProductFamily(QuoteToLeProductFamily quoteToLeProductFamily) {
		getQuoteToLeProductFamilies().add(quoteToLeProductFamily);
		quoteToLeProductFamily.setQuoteToLe(this);

		return quoteToLeProductFamily;
	}

	public QuoteToLeProductFamily removeQuoteToLeProductFamily(QuoteToLeProductFamily quoteToLeProductFamily) {
		getQuoteToLeProductFamilies().remove(quoteToLeProductFamily);
		quoteToLeProductFamily.setQuoteToLe(null);

		return quoteToLeProductFamily;
	}

	public Double getFinalArc() {
		return finalArc;
	}

	public void setFinalArc(Double finalArc) {
		this.finalArc = finalArc;
	}

	public Double getProposedArc() {
		return proposedArc;
	}

	public void setProposedArc(Double proposedArc) {
		this.proposedArc = proposedArc;
	}

	public Double getTotalTcv() {
		return totalTcv;
	}

	public void setTotalTcv(Double totalTcv) {
		this.totalTcv = totalTcv;
	}

	public String getQuoteType() {
		return quoteType;
	}

	public void setQuoteType(String quoteType) {
		this.quoteType = quoteType;
	}

	public Integer getErfServiceInventoryParentOrderId() {
		return erfServiceInventoryParentOrderId;
	}

	public void setErfServiceInventoryParentOrderId(Integer erfServiceInventoryParentOrderId) {
		this.erfServiceInventoryParentOrderId = erfServiceInventoryParentOrderId;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getQuoteCategory() {
		return quoteCategory;
	}

	public void setQuoteCategory(String quoteCategory) {
		this.quoteCategory = quoteCategory;
	}

	public Character getIsBundle() {
		return isBundle;
	}

	public void setIsBundle(Character isBundle) {
		this.isBundle = isBundle;
	}

	public String getChangeRequestSummary() {
		return changeRequestSummary;
	}

	public void setChangeRequestSummary(String changeRequestSummary) {
		this.changeRequestSummary = changeRequestSummary;
	}

	/**
	 * @return the erfServiceInventoryServiceDetailId
	 */
	public Integer getErfServiceInventoryServiceDetailId() {
		return erfServiceInventoryServiceDetailId;
	}

	/**
	 * @param erfServiceInventoryServiceDetailId the erfServiceInventoryServiceDetailId to set
	 */
	public void setErfServiceInventoryServiceDetailId(Integer erfServiceInventoryServiceDetailId) {
		this.erfServiceInventoryServiceDetailId = erfServiceInventoryServiceDetailId;
	}

	/**
	 * @return the erfServiceInventoryTpsServiceId
	 */
	public String getErfServiceInventoryTpsServiceId() {
		return erfServiceInventoryTpsServiceId;
	}

	/**
	 * @param erfServiceInventoryTpsServiceId the erfServiceInventoryTpsServiceId to set
	 */
	public void setErfServiceInventoryTpsServiceId(String erfServiceInventoryTpsServiceId) {
		this.erfServiceInventoryTpsServiceId = erfServiceInventoryTpsServiceId;
	}

	public Integer getTpsSfdcParentOptyId() {
		return tpsSfdcParentOptyId;
	}

	public void setTpsSfdcParentOptyId(Integer tpsSfdcParentOptyId) {
		this.tpsSfdcParentOptyId = tpsSfdcParentOptyId;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	/**
	 * @return the preapprovedOpportunityFlag
	 */
	public Byte getPreapprovedOpportunityFlag() {
		return preapprovedOpportunityFlag;
	}

	/**
	 * @param preapprovedOpportunityFlag the preapprovedOpportunityFlag to set
	 */
	public void setPreapprovedOpportunityFlag(Byte preapprovedOpportunityFlag) {
		this.preapprovedOpportunityFlag = preapprovedOpportunityFlag;
	}

	/**
	 * @return the tpsSfdcApprovedMrc
	 */
	public Double getTpsSfdcApprovedMrc() {
		return tpsSfdcApprovedMrc;
	}

	/**
	 * @param tpsSfdcApprovedMrc the tpsSfdcApprovedMrc to set
	 */
	public void setTpsSfdcApprovedMrc(Double tpsSfdcApprovedMrc) {
		this.tpsSfdcApprovedMrc = tpsSfdcApprovedMrc;
	}

	/**
	 * @return the tpsSfdcApprovedNrc
	 */
	public Double getTpsSfdcApprovedNrc() {
		return tpsSfdcApprovedNrc;
	}

	/**
	 * @param tpsSfdcApprovedNrc the tpsSfdcApprovedNrc to set
	 */
	public void setTpsSfdcApprovedNrc(Double tpsSfdcApprovedNrc) {
		this.tpsSfdcApprovedNrc = tpsSfdcApprovedNrc;
	}

	/**
	 * @return the tpsSfdcApprovedBy
	 */
	public String getTpsSfdcApprovedBy() {
		return tpsSfdcApprovedBy;
	}

	/**
	 * @param tpsSfdcApprovedBy the tpsSfdcApprovedBy to set
	 */
	public void setTpsSfdcApprovedBy(String tpsSfdcApprovedBy) {
		this.tpsSfdcApprovedBy = tpsSfdcApprovedBy;
	}

	/**
	 * @return the tpsSfdcReservedBy
	 */
	public String getTpsSfdcReservedBy() {
		return tpsSfdcReservedBy;
	}

	/**
	 * @param tpsSfdcReservedBy the tpsSfdcReservedBy to set
	 */
	public void setTpsSfdcReservedBy(String tpsSfdcReservedBy) {
		this.tpsSfdcReservedBy = tpsSfdcReservedBy;
	}

	/**
	 * @return the tpsSfdcCreditApprovalDate
	 */
	public Date getTpsSfdcCreditApprovalDate() {
		return tpsSfdcCreditApprovalDate;
	}

	/**
	 * @param tpsSfdcCreditApprovalDate the tpsSfdcCreditApprovalDate to set
	 */
	public void setTpsSfdcCreditApprovalDate(Date tpsSfdcCreditApprovalDate) {
		this.tpsSfdcCreditApprovalDate = tpsSfdcCreditApprovalDate;
	}

	/**
	 * @return the tpsSfdcCreditRemarks
	 */
	public String getTpsSfdcCreditRemarks() {
		return tpsSfdcCreditRemarks;
	}

	/**
	 * @param tpsSfdcCreditRemarks the tpsSfdcCreditRemarks to set
	 */
	public void setTpsSfdcCreditRemarks(String tpsSfdcCreditRemarks) {
		this.tpsSfdcCreditRemarks = tpsSfdcCreditRemarks;
	}

	/**
	 * @return the tpsSfdcDifferentialMrc
	 */
	public Double getTpsSfdcDifferentialMrc() {
		return tpsSfdcDifferentialMrc;
	}

	/**
	 * @param tpsSfdcDifferentialMrc the tpsSfdcDifferentialMrc to set
	 */
	public void setTpsSfdcDifferentialMrc(Double tpsSfdcDifferentialMrc) {
		this.tpsSfdcDifferentialMrc = tpsSfdcDifferentialMrc;
	}

	/**
	 * @return the tpsSfdcStatusCreditControl
	 */
	public String getTpsSfdcStatusCreditControl() {
		return tpsSfdcStatusCreditControl;
	}

	/**
	 * @param tpsSfdcStatusCreditControl the tpsSfdcStatusCreditControl to set
	 */
	public void setTpsSfdcStatusCreditControl(String tpsSfdcStatusCreditControl) {
		this.tpsSfdcStatusCreditControl = tpsSfdcStatusCreditControl;
	}

	/**
	 * @return the variationApprovedFlag
	 */
	public Byte getVariationApprovedFlag() {
		return variationApprovedFlag;
	}

	/**
	 * @param variationApprovedFlag the variationApprovedFlag to set
	 */
	public void setVariationApprovedFlag(Byte variationApprovedFlag) {
		this.variationApprovedFlag = variationApprovedFlag;
	}

	/**
	 * @return the tpsSfdcSecurityDepositAmount
	 */
	public Double getTpsSfdcSecurityDepositAmount() {
		return tpsSfdcSecurityDepositAmount;
	}

	/**
	 * @param tpsSfdcSecurityDepositAmount the tpsSfdcSecurityDepositAmount to set
	 */
	public void setTpsSfdcSecurityDepositAmount(Double tpsSfdcSecurityDepositAmount) {
		this.tpsSfdcSecurityDepositAmount = tpsSfdcSecurityDepositAmount;
	}

	/**
	 * @return the creditCheckTriggered
	 */
	public Byte getCreditCheckTriggered() {
		return creditCheckTriggered;
	}

	/**
	 * @param creditCheckTriggered the creditCheckTriggered to set
	 */
	public void setCreditCheckTriggered(Byte creditCheckTriggered) {
		this.creditCheckTriggered = creditCheckTriggered;
	}

	/**
	 * @return the tpsSfdcCreditLimit
	 */
	public Double getTpsSfdcCreditLimit() {
		return tpsSfdcCreditLimit;
	}

	/**
	 * @param tpsSfdcCreditLimit the tpsSfdcCreditLimit to set
	 */
	public void setTpsSfdcCreditLimit(Double tpsSfdcCreditLimit) {
		this.tpsSfdcCreditLimit = tpsSfdcCreditLimit;
	}

	public String getCommercialStatus() {
		return commercialStatus;
	}

	public void setCommercialStatus(String commercialStatus) {
		this.commercialStatus = commercialStatus;
	}

	public Byte getIsMultiCircuit() {
		return isMultiCircuit;
	}

	public void setIsMultiCircuit(Byte isMultiCircuit) {
		this.isMultiCircuit = isMultiCircuit;
	}


	public String getAmendmentParentOrderCode() {
		return amendmentParentOrderCode;
	}

	public void setAmendmentParentOrderCode(String amendmentParentOrderCode) {
		this.amendmentParentOrderCode = amendmentParentOrderCode;
	}

	public Byte getIsAmended() {
		return isAmended;
	}

	public void setIsAmended(Byte isAmended) {
		this.isAmended = isAmended;
	}

	public Byte getIsDemo() {
		return isDemo;
	}

	public void setIsDemo(Byte isDemo) {
		this.isDemo = isDemo;
	}

	public String getDemoType() {
		return demoType;
	}

	public void setDemoType(String demoType) {
		this.demoType = demoType;
	}

	public Byte getIsWholesale() {
		return isWholesale;
	}

	public void setIsWholesale(Byte isWholesale) {
		this.isWholesale = isWholesale;
	}

	public String getCancelledParentOrderCode() {
		return cancelledParentOrderCode;
	}

	public void setCancelledParentOrderCode(String cancelledParentOrderCode) {
		this.cancelledParentOrderCode = cancelledParentOrderCode;
	}

	public String getSiteLevelBilling() {
		return siteLevelBilling;
	}

	public void setSiteLevelBilling(String siteLevelBilling) {
		this.siteLevelBilling = siteLevelBilling;
	}
	
	public String getQuoteLeCode() {
		return quoteLeCode;
	}

	public void setQuoteLeCode(String quoteLeCode) {
		this.quoteLeCode = quoteLeCode;
	}

	public Set<QuoteIllSiteToService> getQuoteIllSiteToServices() {
		return quoteIllSiteToServices;
	}

	public void setQuoteIllSiteToServices(Set<QuoteIllSiteToService> quoteIllSiteToServices) {
		this.quoteIllSiteToServices = quoteIllSiteToServices;
	}

	public Integer getIsAckMailTriggered() { return isAckMailTriggered;	}

	public void setIsAckMailTriggered(Integer isAckMailTriggered) {	this.isAckMailTriggered = isAckMailTriggered; }

	public Date getAckMailTriggeredDate() { return ackMailTriggeredDate; }

	public void setAckMailTriggeredDate(Date ackMailTriggeredDate) { this.ackMailTriggeredDate = ackMailTriggeredDate; }

}
