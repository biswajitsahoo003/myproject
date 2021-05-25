package com.tcl.dias.customer.bean;


import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.customer.entity.entities.CallidusData;

/**
 * Bean including Partner Commisions
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CallidusPartnerCommisions {

    private int id;
    private String partnerName;
    private String partnerId;
    private String partnerProfile;
    private String partnerRegion;
    private String segment;
    private String opptyId;
    private String circuitId;
    private String copfID;
    private String crnID;
    private String secsID;
    private String billingStartDate;
    private String billingEndDate;
    private String subAgent;
    private String invoiceNumber;
    private Integer opportunityTerm;
    private String obDate;
    private String commissionedDate;
    private String contractExpiryDate;
    private String commissionType;
    private String baseCommissionLevelPercentage;
    private String discountLevelCommissionPercentage;
    private String multiYearCommissionPercentage;
    private String dealRegPercentage;
    private String compDate;
    private String customerName;
    private String serviceName;
    private String tranCurrency;
    private Double acvInTranCurrency;
    private Double billedValueInTranCurrency;
    private Double exchangeRate;
    private String partnerBaseCurrency;
    private Double billedValueInBaseCurrency;
    private Double incentiveValueInBaseCurrency;
    private String remarks;
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

    public String getCircuitId() {
        return circuitId;
    }

    public void setCircuitId(String circuitId) {
        this.circuitId = circuitId;
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

    public String getContractExpiryDate() {
        return contractExpiryDate;
    }

    public void setContractExpiryDate(String contractExpiryDate) {
        this.contractExpiryDate = contractExpiryDate;
    }

    public static CallidusPartnerCommisions fromCallidusData(CallidusData callidusData) {
        CallidusPartnerCommisions callidusPartnerCommision = new CallidusPartnerCommisions();
        callidusPartnerCommision.setId(callidusData.getId());
        callidusPartnerCommision.setPartnerName(callidusData.getPartnerName());
        callidusPartnerCommision.setPartnerId(callidusData.getPartnerId());
        callidusPartnerCommision.setPartnerProfile(callidusData.getPartnerProfile());
        callidusPartnerCommision.setSegment(callidusData.getSegment());
        callidusPartnerCommision.setPartnerBaseCurrency(callidusData.getPartnerBaseCurrency());
        callidusPartnerCommision.setOpptyId(callidusData.getOpptyId());
        callidusPartnerCommision.setCircuitId(callidusData.getCircuitID());
        callidusPartnerCommision.setCopfID(callidusData.getCopfID());
        callidusPartnerCommision.setSecsID(callidusData.getSecsID());
        callidusPartnerCommision.setCrnID(callidusData.getCrnID());
        callidusPartnerCommision.setBillingStartDate(callidusData.getBillingStartDate());
        callidusPartnerCommision.setBillingEndDate(callidusData.getBillingEndDate());
        callidusPartnerCommision.setSubAgent(callidusData.getSubAgent());
        callidusPartnerCommision.setCircuitId(callidusData.getCircuitID());
        callidusPartnerCommision.setInvoiceNumber(callidusData.getInvoiceNumber());
        callidusPartnerCommision.setOpportunityTerm(callidusData.getOpportunityTerm());
        callidusPartnerCommision.setObDate(callidusData.getObDate());
        callidusPartnerCommision.setCommissionedDate(callidusData.getCommissionedDate());
        callidusPartnerCommision.setContractExpiryDate(callidusData.getContractExpiryDate());
        callidusPartnerCommision.setCommissionType(callidusData.getCommissionType());
        callidusPartnerCommision.setBaseCommissionLevelPercentage(callidusData.getBaseCommissionLevelPercentage());
        callidusPartnerCommision.setDiscountLevelCommissionPercentage(callidusData.getDiscountLevelCommissionPercentage());
        callidusPartnerCommision.setMultiYearCommissionPercentage(callidusData.getMultiYearCommissionPercentage());
        callidusPartnerCommision.setDealRegPercentage(callidusData.getDealRegPercentage());
        callidusPartnerCommision.setCompDate(callidusData.getCompDate());
        callidusPartnerCommision.setCustomerName(callidusData.getCustomerName());
        callidusPartnerCommision.setServiceName(callidusData.getServiceName());
        callidusPartnerCommision.setTranCurrency(callidusData.getTranCurrency());
        callidusPartnerCommision.setAcvInTranCurrency(Utils.doRoundForTwoDecimal(callidusData.getAcvInTranCurrency()));
        callidusPartnerCommision.setBilledValueInTranCurrency(Utils.doRoundForTwoDecimal(callidusData.getBilledValueInTranCurrency()));
        callidusPartnerCommision.setExchangeRate(Utils.doRoundForTwoDecimal(callidusData.getExchangeRate()));
        callidusPartnerCommision.setBilledValueInBaseCurrency(Utils.doRoundForTwoDecimal(callidusData.getBilledValueInBaseCurrency()));
        callidusPartnerCommision.setIncentiveValueInBaseCurrency(Utils.doRoundForTwoDecimal(callidusData.getIncentiveValueInBaseCurrency()));
        callidusPartnerCommision.setRemarks(callidusData.getRemarks());
        callidusPartnerCommision.setCommissionedPercentage(callidusData.getCommissionedPercentage());
        return callidusPartnerCommision;
    }
}
