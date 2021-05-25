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
@Table(name = "order_to_le")
@NamedQuery(name = "OrderToLe.findAll", query = "SELECT o FROM OrderToLe o")
public class OrderToLe implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "currency_id")
	private Integer currencyId;

	@Column(name = "order_le_code")
	private String orderLeCode;

	@Column(name = "end_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

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

	@Column(name = "order_type")
	private String orderType;

	@Column(name = "erf_service_inventory_parent_order_id")
	private Integer erfServiceInventoryParentOrderId;

	@Column(name = "erf_service_inventory_tps_service_id")
	private String erfServiceInventoryTpsServiceId;

	@Column(name = "tps_sfdc_parent_opty_id")
	private Integer tpsSfdcParentOptyId;

	@Column(name = "source_system")
	private String sourceSystem;

	@Column(name = "order_category")
	private String orderCategory;

	@Column(name = "is_bundle")
	private Character isBundle;

	@Column(name = "is_amended")
	private Byte isAmended;

	private String stage;

	@Column(name = "sub_stage")
	private String subStage;

	@Column(name = "start_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Column(name = "tps_sfdc_copf_id")
	private String tpsSfdcCopfId;

	// bi-directional many-to-one association to OmsAttachment
	@OneToMany(mappedBy = "orderToLe")
	private Set<OmsAttachment> omsAttachments;

	// bi-directional many-to-one association to Order
	@ManyToOne(fetch = FetchType.LAZY)
	private Order order;

	// bi-directional many-to-one association to OrderToLeProductFamily
	@OneToMany(mappedBy = "orderToLe")
	private Set<OrderToLeProductFamily> orderToLeProductFamilies;

	// bi-directional many-to-one association to OrdersLeAttributeValue
	@OneToMany(mappedBy = "orderToLe")
	private Set<OrdersLeAttributeValue> ordersLeAttributeValues;
	
	@Column(name="term_in_months")
	private String termInMonths;
	
	@Column(name = "currency_code")
	private String currencyCode;

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
	private Byte creditCheckTrigerred;
	
	 @Column(name="is_multicircuit")
	 private Byte isMultiCircuit;
	 
	 @Column(name="change_request_summary")
	 private String changeRequestSummary;
	 
	 //for demo orders
	@Column(name = "is_demo")
	private Byte isDemo;

	@Column(name = "demo_type")
	private String demoType;
	
	//ADDED for multiVrf
	@Column(name = "is_multi_vrf")
	private String  isMultiVrf;
	
	@Column(name = "quote_bulk_update")
	 private String quoteBulkUpdate;
	
	@Column(name = "sitelevel_billing")
	 private String siteLevelBilling;
	
	
	 
	 
	 
	public String getSiteLevelBilling() {
		return siteLevelBilling;
	}

	public void setSiteLevelBilling(String siteLevelBilling) {
		this.siteLevelBilling = siteLevelBilling;
	}

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

	@Column(name = "is_wholesale")
	private Byte isWholesale;
	
	 @Column(name="cancelled_parent_order_code")
	 private String cancelledParentOrderCode;

	public String getChangeRequestSummary() {
		return changeRequestSummary;
	}

	public void setChangeRequestSummary(String changeRequestSummary) {
		this.changeRequestSummary = changeRequestSummary;
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

	public OrderToLe() {
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

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getTpsSfdcCopfId() {
		return this.tpsSfdcCopfId;
	}

	public void setTpsSfdcCopfId(String tpsSfdcCopfId) {
		this.tpsSfdcCopfId = tpsSfdcCopfId;
	}

	public Set<OmsAttachment> getOmsAttachments() {
		return this.omsAttachments;
	}

	public void setOmsAttachments(Set<OmsAttachment> omsAttachments) {
		this.omsAttachments = omsAttachments;
	}

	public OmsAttachment addOmsAttachment(OmsAttachment omsAttachment) {
		getOmsAttachments().add(omsAttachment);
		omsAttachment.setOrderToLe(this);

		return omsAttachment;
	}

	public OmsAttachment removeOmsAttachment(OmsAttachment omsAttachment) {
		getOmsAttachments().remove(omsAttachment);
		omsAttachment.setOrderToLe(null);

		return omsAttachment;
	}

	public Order getOrder() {
		return this.order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Set<OrderToLeProductFamily> getOrderToLeProductFamilies() {
		return this.orderToLeProductFamilies;
	}

	public void setOrderToLeProductFamilies(Set<OrderToLeProductFamily> orderToLeProductFamilies) {
		this.orderToLeProductFamilies = orderToLeProductFamilies;
	}

	public OrderToLeProductFamily addOrderToLeProductFamily(OrderToLeProductFamily orderToLeProductFamily) {
		getOrderToLeProductFamilies().add(orderToLeProductFamily);
		orderToLeProductFamily.setOrderToLe(this);

		return orderToLeProductFamily;
	}

	public OrderToLeProductFamily removeOrderToLeProductFamily(OrderToLeProductFamily orderToLeProductFamily) {
		getOrderToLeProductFamilies().remove(orderToLeProductFamily);
		orderToLeProductFamily.setOrderToLe(null);

		return orderToLeProductFamily;
	}

	public Set<OrdersLeAttributeValue> getOrdersLeAttributeValues() {
		return this.ordersLeAttributeValues;
	}

	public void setOrdersLeAttributeValues(Set<OrdersLeAttributeValue> ordersLeAttributeValues) {
		this.ordersLeAttributeValues = ordersLeAttributeValues;
	}

	public OrdersLeAttributeValue addOrdersLeAttributeValue(OrdersLeAttributeValue ordersLeAttributeValue) {
		getOrdersLeAttributeValues().add(ordersLeAttributeValue);
		ordersLeAttributeValue.setOrderToLe(this);

		return ordersLeAttributeValue;
	}

	public OrdersLeAttributeValue removeOrdersLeAttributeValue(OrdersLeAttributeValue ordersLeAttributeValue) {
		getOrdersLeAttributeValues().remove(ordersLeAttributeValue);
		ordersLeAttributeValue.setOrderToLe(null);

		return ordersLeAttributeValue;
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

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Integer getErfServiceInventoryParentOrderId() {
		return erfServiceInventoryParentOrderId;
	}

	public void setErfServiceInventoryParentOrderId(Integer erfServiceInventoryParentOrderId) {
		this.erfServiceInventoryParentOrderId = erfServiceInventoryParentOrderId;
	}

	public String getErfServiceInventoryTpsServiceId() {
		return erfServiceInventoryTpsServiceId;
	}

	public void setErfServiceInventoryTpsServiceId(String erfServiceInventoryTpsServiceId) {
		this.erfServiceInventoryTpsServiceId = erfServiceInventoryTpsServiceId;
	}

	public Integer getTpsSfdcParentOptyId() {
		return tpsSfdcParentOptyId;
	}

	public void setTpsSfdcParentOptyId(Integer tpsSfdcParentOptyId) {
		this.tpsSfdcParentOptyId = tpsSfdcParentOptyId;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public Character getIsBundle() {
		return isBundle;
	}

	public void setIsBundle(Character isBundle) {
		this.isBundle = isBundle;
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
	 * @return the creditCheckTrigerred
	 */
	public Byte getCreditCheckTrigerred() {
		return creditCheckTrigerred;
	}

	/**
	 * @param creditCheckTrigerred the creditCheckTrigerred to set
	 */
	public void setCreditCheckTrigerred(Byte creditCheckTrigerred) {
		this.creditCheckTrigerred = creditCheckTrigerred;
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

	public Byte getIsMultiCircuit() {
		return isMultiCircuit;
	}

	public void setIsMultiCircuit(Byte isMultiCircuit) {
		this.isMultiCircuit = isMultiCircuit;
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

	public String getOrderLeCode() {
		return orderLeCode;
	}

	public void setOrderLeCode(String orderLeCode) {
		this.orderLeCode = orderLeCode;
	}

	public String getSubStage() {
		return subStage;
	}

	public void setSubStage(String subStage) {
		this.subStage = subStage;
	}
}
