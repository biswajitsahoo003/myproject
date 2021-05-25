package com.tcl.dias.customer.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity Class for Callidus Data
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "callidus_data")
@NamedQuery(name = "CallidusData.findAll", query = "SELECT c FROM CallidusData c")
public class CallidusData {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true, nullable = false)
    private int id;

    @Column(name = "partner_name")
    private String partnerName;

    @Column(name = "partner_ID")
    private String partnerId;

    @Column(name = "partner_profile")
    private String partnerProfile;

    @Column(name = "partner_region")
    private String partnerRegion;

    @Column(name = "segment")
    private String segment;

    @Column(name = "oppty_ID")
    private String opptyId;

    @Column(name = "circuit_ID")
    private String circuitID;

    @Column(name = "COPFID")
    private String copfID;

    @Column(name = "CRNID")
    private String crnID;

    @Column(name = "SECSID")
    private String secsID;

    @Column(name = "billing_start_date")
    private String billingStartDate;

    @Column(name = "billing_end_date")
    private String billingEndDate;

    @Column(name = "sub_agent")
    private String subAgent;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "opportunity_term")
    private Integer opportunityTerm;

    @Column(name = "OB_date")
    private String obDate;

    @Column(name = "commissioned_date")
    private String commissionedDate;

    @Column(name = "commission_type")
    private String commissionType;

    @Column(name = "contract_expiry_date")
    private String contractExpiryDate;

    @Column(name = "base_commission_level_percentage")
    private String baseCommissionLevelPercentage;

    @Column(name = "discount_level_commission_percentage")
    private String discountLevelCommissionPercentage;

    @Column(name = "multi_year_commission_percentage")
    private String multiYearCommissionPercentage;

    @Column(name = "deal_reg_percentage")
    private String dealRegPercentage;

    @Column(name = "comp_date")
    private String compDate;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "tran_currency")
    private String tranCurrency;

    @Column(name = "ACV_in_tran_currency")
    private Double acvInTranCurrency;

    @Column(name = "billed_value_in_tran_currency")
    private Double billedValueInTranCurrency;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @Column(name = "partner_base_currency")
    private String partnerBaseCurrency;

    @Column(name = "billed_value_in_base_currency")
    private Double billedValueInBaseCurrency;

    @Column(name = "incentive_value_in_base_currency")
    private Double incentiveValueInBaseCurrency;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "commissioned_percentage")
    private String commissionedPercentage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerProfile() {
        return partnerProfile;
    }

    public void setPartnerProfile(String partnerProfile) {
        this.partnerProfile = partnerProfile;
    }

    public String getPartnerRegion() {
        return partnerRegion;
    }

    public void setPartnerRegion(String partnerRegion) {
        this.partnerRegion = partnerRegion;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getOpptyId() {
        return opptyId;
    }

    public void setOpptyId(String opptyId) {
        this.opptyId = opptyId;
    }

    public String getCircuitID() {
        return circuitID;
    }

    public void setCircuitID(String circuitID) {
        this.circuitID = circuitID;
    }

    public String getCopfID() {
        return copfID;
    }

    public void setCopfID(String copfID) {
        this.copfID = copfID;
    }

    public String getCrnID() {
        return crnID;
    }

    public void setCrnID(String crnID) {
        this.crnID = crnID;
    }

    public String getSecsID() {
        return secsID;
    }

    public void setSecsID(String secsID) {
        this.secsID = secsID;
    }

    public String getBillingStartDate() {
        return billingStartDate;
    }

    public void setBillingStartDate(String billingStartDate) {
        this.billingStartDate = billingStartDate;
    }

    public String getBillingEndDate() {
        return billingEndDate;
    }

    public void setBillingEndDate(String billingEndDate) {
        this.billingEndDate = billingEndDate;
    }

    public String getSubAgent() {
        return subAgent;
    }

    public void setSubAgent(String subAgent) {
        this.subAgent = subAgent;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Integer getOpportunityTerm() {
        return opportunityTerm;
    }

    public void setOpportunityTerm(Integer opportunityTerm) {
        this.opportunityTerm = opportunityTerm;
    }

    public String getObDate() {
        return obDate;
    }

    public void setObDate(String obDate) {
        this.obDate = obDate;
    }

    public String getCommissionedDate() {
        return commissionedDate;
    }

    public void setCommissionedDate(String commissionedDate) {
        this.commissionedDate = commissionedDate;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String commissionType) {
        this.commissionType = commissionType;
    }

    public String getContractExpiryDate() {
        return contractExpiryDate;
    }

    public void setContractExpiryDate(String contractExpiryDate) {
        this.contractExpiryDate = contractExpiryDate;
    }

    public String getBaseCommissionLevelPercentage() {
        return baseCommissionLevelPercentage;
    }

    public void setBaseCommissionLevelPercentage(String baseCommissionLevelPercentage) {
        this.baseCommissionLevelPercentage = baseCommissionLevelPercentage;
    }

    public String getDiscountLevelCommissionPercentage() {
        return discountLevelCommissionPercentage;
    }

    public void setDiscountLevelCommissionPercentage(String discountLevelCommissionPercentage) {
        this.discountLevelCommissionPercentage = discountLevelCommissionPercentage;
    }

    public String getMultiYearCommissionPercentage() {
        return multiYearCommissionPercentage;
    }

    public void setMultiYearCommissionPercentage(String multiYearCommissionPercentage) {
        this.multiYearCommissionPercentage = multiYearCommissionPercentage;
    }

    public String getDealRegPercentage() {
        return dealRegPercentage;
    }

    public void setDealRegPercentage(String dealRegPercentage) {
        this.dealRegPercentage = dealRegPercentage;
    }

    public String getCompDate() {
        return compDate;
    }

    public void setCompDate(String compDate) {
        this.compDate = compDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getTranCurrency() {
        return tranCurrency;
    }

    public void setTranCurrency(String tranCurrency) {
        this.tranCurrency = tranCurrency;
    }

    public Double getAcvInTranCurrency() {
        return acvInTranCurrency;
    }

    public void setAcvInTranCurrency(Double acvInTranCurrency) {
        this.acvInTranCurrency = acvInTranCurrency;
    }

    public Double getBilledValueInTranCurrency() {
        return billedValueInTranCurrency;
    }

    public void setBilledValueInTranCurrency(Double billedValueInTranCurrency) {
        this.billedValueInTranCurrency = billedValueInTranCurrency;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getPartnerBaseCurrency() {
        return partnerBaseCurrency;
    }

    public void setPartnerBaseCurrency(String partnerBaseCurrency) {
        this.partnerBaseCurrency = partnerBaseCurrency;
    }

    public Double getBilledValueInBaseCurrency() {
        return billedValueInBaseCurrency;
    }

    public void setBilledValueInBaseCurrency(Double billedValueInBaseCurrency) {
        this.billedValueInBaseCurrency = billedValueInBaseCurrency;
    }

    public Double getIncentiveValueInBaseCurrency() {
        return incentiveValueInBaseCurrency;
    }

    public void setIncentiveValueInBaseCurrency(Double incentiveValueInBaseCurrency) {
        this.incentiveValueInBaseCurrency = incentiveValueInBaseCurrency;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCommissionedPercentage() {
        return commissionedPercentage;
    }

    public void setCommissionedPercentage(String commissionedPercentage) {
        this.commissionedPercentage = commissionedPercentage;
    }
}
