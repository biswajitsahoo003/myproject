package com.tcl.dias.oms.gsc.pdf.beans;

import com.tcl.dias.common.gsc.beans.GscMultiMacdServiceBean;
import com.tcl.dias.oms.gsc.beans.GscQuoteToLeBean;
import com.tcl.dias.oms.gsc.beans.GscSolutionBean;

import java.util.List;

/**
 * Class for GscCof Pdf Bean which contains the attributes for generating COF
 * PDF
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscCofPdfBean {
    private String orderRef;
    private String orderDate;
    private String orderType;
    private String baseUrl;
    private String customerContractingEntity;
    private String customerGstNumber;
    private String customerVatNumber;
    private String customerContactName;
    private String billingPaymentsName;
    private String customerContactNumber;
    private String billingContactNumber;
    private String customerEmailId;
    private String billingEmailId;
    private String supplierContactEntity;
    private String supplierAddress;
    private String supplierAccountManager;
    private String supplierContactNumber;
    private String supplierEmailId;
    private String supplierGstnNumber;
    private String supplierWithCopyToAddress;
    private String billingMethod;
    private String billingFreq;
    private String billingCurrency;
    private String billingType;
    private String paymentCurrency;
    private String paymentTerm;
    private String invoiceMethod;
    private String contractTerm;
    private String paymentOptions;
    private String billingIncrement;
    private String applicableTimeZone;
    private String noticeAddress;
    private String billingAddress;
    private String customerAddress;
    private String customerState;
    private String customerPincode;
    private String customerCity;
    private String customerCountry;
    private String productName;
    private Double totalMRC;
    private Double totalNRC;
    private Double totalARC;
    private Double totalTCV;
    private String presentDate;
    private String publicIp;
    private Boolean isNat = true;
    private Boolean isApproved = false;
    private Boolean isMSA = false;
    private Boolean isSS = false;
    private Boolean isSSStandard = false;
    private Boolean isSSPremium;
    private Boolean isDocusign = false;
    private Boolean isArc = true;
    private Boolean isInbound = false;
    private Boolean isAudioConfig = false;

    private Integer quoteId;
    private Integer quoteLeId;
    private Integer customerId;
    private String productFamilyName;
    private String accessType;
    private String profileName;
    private List<GscSolutionBean> solutions;
    private List<GscQuoteToLeBean> legalEntities;
    /* private List<GvpnQuotePdfBean> gvpnQuotes; */
    private Integer sizeAttribute;
//    private GvpnQuotePdfBean gvpnQuote;
    private String creditLimit;
    private String depositAmount;
    private String inboundVolume;
    private String outboundVolume;
    private String inboundVolumeCountry;
    private String supplierNoticeAddress;
    private Boolean isPartnerSellWith = false;
    private Boolean isPartnerSellThrough = false;
    private Boolean isWholesaleNgp = false;

    //--------PT-189--------//
    private String poNumber;
    private String poDate;

    private Boolean isInternational;

    //--------PT-467--------//
    private String approverName1;
    private String approverName2;
    private String approverEmail1;
    private String approverEmail2;
    private String approverSignedDate1;
    private String approverSignedDate2;
    private Boolean showReviewerTable = false;

    private Boolean isDomesticVoice = false;

    private String customerName1;
    private String customerEmail1;
    private String customerSignedDate1;
    private String customerName2;
    private String customerEmail2;
    private String customerSignedDate2;
    private Boolean showCustomerSignerTable = false;

    //-----Partner Managed Customer LE details----//
    private String partnerCustomerLeName;
    private String partnerCustomerLeState;
    private String partnerCustomerLeCity;
    private String partnerCustomerLeCountry;
    private String partnerCustomerLeZip;
    private String partnerCustomerLeWebsite;
    private String partnerCustomerAddress;
    private String partnerCustomerContactName;
    private String partnerCustomerContactEmail;

    private String departmentBilling;
    private String departmentName;

    private String outboundVolumeCountry;

    private List<String> siteAddress;

    private String quoteCategory;
    private Boolean isMacd = false;
    private Boolean removeOutbound = false;
    private List<GscMultiMacdServiceBean> gscMultiMacdServices;
    private Integer contractTermSimplified = 1;

    private String tnc;
    private Boolean isTnc = false;
    private String interconnectId;
    private String interconnectName;

    public Boolean getIsInternational() {
        return isInternational;
    }

    public void setIsInternational(Boolean isInternational) {
        this.isInternational = isInternational;
    }

    public String getInboundVolumeCountry() {
        return inboundVolumeCountry;
    }

    public void setInboundVolumeCountry(String inboundVolumeCountry) {
        this.inboundVolumeCountry = inboundVolumeCountry;
    }

    public String getOutboundVolumeCountry() {
        return outboundVolumeCountry;
    }

    public void setOutboundVolumeCountry(String outboundVolumeCountry) {
        this.outboundVolumeCountry = outboundVolumeCountry;
    }

    public String getInboundVolume() {
        return inboundVolume;
    }

    public void setInboundVolume(String inboundVolume) {
        this.inboundVolume = inboundVolume;
    }

    public String getOutboundVolume() {
        return outboundVolume;
    }

    public void setOutboundVolume(String outboundVolume) {
        this.outboundVolume = outboundVolume;
    }

    public String getOrderRef() {
        return orderRef;
    }

    public void setOrderRef(String orderRef) {
        this.orderRef = orderRef;
    }

    public Boolean getIsSS() {
        return isSS;
    }

    public void setIsSS(Boolean isSS) {
        this.isSS = isSS;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getCustomerContractingEntity() {
        return customerContractingEntity;
    }

    public void setCustomerContractingEntity(String customerContractingEntity) {
        this.customerContractingEntity = customerContractingEntity;
    }

    public String getCustomerGstNumber() {
        return customerGstNumber;
    }

    public void setCustomerGstNumber(String customerGstNumber) {
        this.customerGstNumber = customerGstNumber;
    }

    public String getCustomerVatNumber() {
        return customerVatNumber;
    }

    public void setCustomerVatNumber(String customerVatNumber) {
        this.customerVatNumber = customerVatNumber;
    }

    public String getCustomerContactName() {
        return customerContactName;
    }

    public void setCustomerContactName(String customerContactName) {
        this.customerContactName = customerContactName;
    }

    public String getBillingPaymentsName() {
        return billingPaymentsName;
    }

    public void setBillingPaymentsName(String billingPaymentsName) {
        this.billingPaymentsName = billingPaymentsName;
    }

    public String getCustomerContactNumber() {
        return customerContactNumber;
    }

    public void setCustomerContactNumber(String customerContactNumber) {
        this.customerContactNumber = customerContactNumber;
    }

    public String getBillingContactNumber() {
        return billingContactNumber;
    }

    public void setBillingContactNumber(String billingContactNumber) {
        this.billingContactNumber = billingContactNumber;
    }

    public String getCustomerEmailId() {
        return customerEmailId;
    }

    public void setCustomerEmailId(String customerEmailId) {
        this.customerEmailId = customerEmailId;
    }

    public String getBillingEmailId() {
        return billingEmailId;
    }

    public void setBillingEmailId(String billingEmailId) {
        this.billingEmailId = billingEmailId;
    }

    public String getSupplierContactEntity() {
        return supplierContactEntity;
    }

    public void setSupplierContactEntity(String supplierContactEntity) {
        this.supplierContactEntity = supplierContactEntity;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getSupplierAccountManager() {
        return supplierAccountManager;
    }

    public void setSupplierAccountManager(String supplierAccountManager) {
        this.supplierAccountManager = supplierAccountManager;
    }

    public String getSupplierContactNumber() {
        return supplierContactNumber;
    }

    public void setSupplierContactNumber(String supplierContactNumber) {
        this.supplierContactNumber = supplierContactNumber;
    }

    public String getSupplierEmailId() {
        return supplierEmailId;
    }

    public void setSupplierEmailId(String supplierEmailId) {
        this.supplierEmailId = supplierEmailId;
    }

    public String getSupplierGstnNumber() {
        return supplierGstnNumber;
    }

    public void setSupplierGstnNumber(String supplierGstnNumber) {
        this.supplierGstnNumber = supplierGstnNumber;
    }

    public String getSupplierWithCopyToAddress() {
        return supplierWithCopyToAddress;
    }

    public void setSupplierWithCopyToAddress(String supplierWithCopyToAddress) {
        this.supplierWithCopyToAddress = supplierWithCopyToAddress;
    }

    public String getBillingMethod() {
        return billingMethod;
    }

    public void setBillingMethod(String billingMethod) {
        this.billingMethod = billingMethod;
    }

    public String getBillingFreq() {
        return billingFreq;
    }

    public void setBillingFreq(String billingFreq) {
        this.billingFreq = billingFreq;
    }

    public String getBillingCurrency() {
        return billingCurrency;
    }

    public void setBillingCurrency(String billingCurrency) {
        this.billingCurrency = billingCurrency;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public String getPaymentCurrency() {
        return paymentCurrency;
    }

    public void setPaymentCurrency(String paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public String getInvoiceMethod() {
        return invoiceMethod;
    }

    public void setInvoiceMethod(String invoiceMethod) {
        this.invoiceMethod = invoiceMethod;
    }

    public String getContractTerm() {
        return contractTerm;
    }

    public void setContractTerm(String contractTerm) {
        this.contractTerm = contractTerm;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerState() {
        return customerState;
    }

    public void setCustomerState(String customerState) {
        this.customerState = customerState;
    }

    public String getCustomerPincode() {
        return customerPincode;
    }

    public void setCustomerPincode(String customerPincode) {
        this.customerPincode = customerPincode;
    }

    public String getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    public String getCustomerCountry() {
        return customerCountry;
    }

    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getTotalMRC() {
        return totalMRC;
    }

    public void setTotalMRC(Double totalMRC) {
        this.totalMRC = totalMRC;
    }

    public Double getTotalNRC() {
        return totalNRC;
    }

    public void setTotalNRC(Double totalNRC) {
        this.totalNRC = totalNRC;
    }

    public Double getTotalTCV() {
        return totalTCV;
    }

    public void setTotalTCV(Double totalTCV) {
        this.totalTCV = totalTCV;
    }

    public String getPresentDate() {
        return presentDate;
    }

    public void setPresentDate(String presentDate) {
        this.presentDate = presentDate;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public Boolean getIsNat() {
        return isNat;
    }

    public void setIsNat(Boolean isNat) {
        this.isNat = isNat;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    /**
     * @return the isMSA
     */
    public Boolean getIsMSA() {
        return isMSA;
    }

    /**
     * @param isMSA the isMSA to set
     */
    public void setIsMSA(Boolean isMSA) {
        this.isMSA = isMSA;
    }

    /**
     * @return the isSSStandard
     */
    public Boolean getIsSSStandard() {
        return isSSStandard;
    }

    /**
     * @param isSSStandard the isSSStandard to set
     */
    public void setIsSSStandard(Boolean isSSStandard) {
        this.isSSStandard = isSSStandard;
    }

    /**
     * @return the isSSPremium
     */
    public Boolean getIsSSPremium() {
        return isSSPremium;
    }

    /**
     * @param isSSPremium the isSSPremium to set
     */
    public void setIsSSPremium(Boolean isSSPremium) {
        this.isSSPremium = isSSPremium;
    }

    public Boolean getIsDocusign() {
        return isDocusign;
    }

    public void setIsDocusign(Boolean isDocusign) {
        this.isDocusign = isDocusign;
    }

    public Double getTotalARC() {
        return totalARC;
    }

    public void setTotalARC(Double totalARC) {
        this.totalARC = totalARC;
    }

    public Boolean getIsArc() {
        return isArc;
    }

    public void setIsArc(Boolean isArc) {
        this.isArc = isArc;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    public Integer getQuoteLeId() {
        return quoteLeId;
    }

    public void setQuoteLeId(Integer quoteLeId) {
        this.quoteLeId = quoteLeId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getProductFamilyName() {
        return productFamilyName;
    }

    public void setProductFamilyName(String productFamilyName) {
        this.productFamilyName = productFamilyName;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public List<GscSolutionBean> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<GscSolutionBean> solutions) {
        this.solutions = solutions;
    }

    public List<GscQuoteToLeBean> getLegalEntities() {
        return legalEntities;
    }

    public void setLegalEntities(List<GscQuoteToLeBean> legalEntities) {
        this.legalEntities = legalEntities;
    }

    /*
     * public List<GvpnQuotePdfBean> getGvpnQuotes() { return gvpnQuotes; }
     *
     * public void setGvpnQuotes(List<GvpnQuotePdfBean> gvpnQuotes) {
     * this.gvpnQuotes = gvpnQuotes; }
     */

    public Integer getSizeAttribute() {
        return sizeAttribute;
    }

    public void setSizeAttribute(Integer sizeAttribute) {
        this.sizeAttribute = sizeAttribute;
    }

//    public GvpnQuotePdfBean getGvpnQuote() {
//        return gvpnQuote;
//    }
//
//    public void setGvpnQuote(GvpnQuotePdfBean gvpnQuote) {
//        this.gvpnQuote = gvpnQuote;
//    }

    public Boolean getIsInbound() {
        return isInbound;
    }

    public void setIsInbound(Boolean isInbound) {
        this.isInbound = isInbound;
    }

    public Boolean getIsAudioConfig() {
        return isAudioConfig;
    }

    public void setIsAudioConfig(Boolean isAudioConfig) {
        this.isAudioConfig = isAudioConfig;
    }

    public String getPaymentOptions() {
        return paymentOptions;
    }

    public void setPaymentOptions(String paymentOptions) {
        this.paymentOptions = paymentOptions;
    }

    public String getBillingIncrement() {
        return billingIncrement;
    }

    public void setBillingIncrement(String billingIncrement) {
        this.billingIncrement = billingIncrement;
    }

    public String getApplicableTimeZone() {
        return applicableTimeZone;
    }

    public void setApplicableTimeZone(String applicableTimeZone) {
        this.applicableTimeZone = applicableTimeZone;
    }

    public String getNoticeAddress() {
        return noticeAddress;
    }

    public void setNoticeAddress(String noticeAddress) {
        this.noticeAddress = noticeAddress;
    }

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(String depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getSupplierNoticeAddress() {
        return supplierNoticeAddress;
    }

    public void setSupplierNoticeAddress(String supplierNoticeAddress) {
        this.supplierNoticeAddress = supplierNoticeAddress;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getPoDate() {
        return poDate;
    }

    public void setPoDate(String poDate) {
        this.poDate = poDate;
    }

    public String getApproverName1() {
        return approverName1;
    }

    public void setApproverName1(String approverName1) {
        this.approverName1 = approverName1;
    }

    public String getApproverName2() {
        return approverName2;
    }

    public void setApproverName2(String approverName2) {
        this.approverName2 = approverName2;
    }

    public String getApproverEmail1() {
        return approverEmail1;
    }

    public void setApproverEmail1(String approverEmail1) {
        this.approverEmail1 = approverEmail1;
    }

    public String getApproverEmail2() {
        return approverEmail2;
    }

    public void setApproverEmail2(String approverEmail2) {
        this.approverEmail2 = approverEmail2;
    }

    public String getApproverSignedDate1() {
        return approverSignedDate1;
    }

    public void setApproverSignedDate1(String approverSignedDate1) {
        this.approverSignedDate1 = approverSignedDate1;
    }

    public String getApproverSignedDate2() {
        return approverSignedDate2;
    }

    public void setApproverSignedDate2(String approverSignedDate2) {
        this.approverSignedDate2 = approverSignedDate2;
    }

    public Boolean getShowReviewerTable() {
        return showReviewerTable;
    }

    public void setShowReviewerTable(Boolean showReviewerTable) {
        this.showReviewerTable = showReviewerTable;
    }

    public Boolean getIsDomesticVoice() {
        return isDomesticVoice;
    }

    public void setIsDomesticVoice(Boolean domesticVoice) {
        isDomesticVoice = domesticVoice;
    }

    public String getCustomerName1() {
        return customerName1;
    }

    public void setCustomerName1(String customerName1) {
        this.customerName1 = customerName1;
    }

    public String getCustomerEmail1() {
        return customerEmail1;
    }

    public void setCustomerEmail1(String customerEmail1) {
        this.customerEmail1 = customerEmail1;
    }

    public String getCustomerSignedDate1() {
        return customerSignedDate1;
    }

    public void setCustomerSignedDate1(String customerSignedDate1) {
        this.customerSignedDate1 = customerSignedDate1;
    }

    public String getCustomerName2() {
        return customerName2;
    }

    public void setCustomerName2(String customerName2) {
        this.customerName2 = customerName2;
    }

    public String getCustomerEmail2() {
        return customerEmail2;
    }

    public void setCustomerEmail2(String customerEmail2) {
        this.customerEmail2 = customerEmail2;
    }

    public String getCustomerSignedDate2() {
        return customerSignedDate2;
    }

    public void setCustomerSignedDate2(String customerSignedDate2) {
        this.customerSignedDate2 = customerSignedDate2;
    }

    public Boolean getShowCustomerSignerTable() {
        return showCustomerSignerTable;
    }

    public void setShowCustomerSignerTable(Boolean showCustomerSignerTable) {
        this.showCustomerSignerTable = showCustomerSignerTable;
    }

    public String getPartnerCustomerLeName() {
        return partnerCustomerLeName;
    }

    public void setPartnerCustomerLeName(String partnerCustomerLeName) {
        this.partnerCustomerLeName = partnerCustomerLeName;
    }

    public String getPartnerCustomerLeState() {
        return partnerCustomerLeState;
    }

    public void setPartnerCustomerLeState(String partnerCustomerLeState) {
        this.partnerCustomerLeState = partnerCustomerLeState;
    }

    public String getPartnerCustomerLeCity() {
        return partnerCustomerLeCity;
    }

    public void setPartnerCustomerLeCity(String partnerCustomerLeCity) {
        this.partnerCustomerLeCity = partnerCustomerLeCity;
    }

    public String getPartnerCustomerLeCountry() {
        return partnerCustomerLeCountry;
    }

    public void setPartnerCustomerLeCountry(String partnerCustomerLeCountry) {
        this.partnerCustomerLeCountry = partnerCustomerLeCountry;
    }

    public String getPartnerCustomerLeZip() {
        return partnerCustomerLeZip;
    }

    public void setPartnerCustomerLeZip(String partnerCustomerLeZip) {
        this.partnerCustomerLeZip = partnerCustomerLeZip;
    }

    public String getPartnerCustomerLeWebsite() {
        return partnerCustomerLeWebsite;
    }

    public void setPartnerCustomerLeWebsite(String partnerCustomerLeWebsite) {
        this.partnerCustomerLeWebsite = partnerCustomerLeWebsite;
    }

    public String getDepartmentBilling() {
        return departmentBilling;
    }

    public void setDepartmentBilling(String departmentBilling) {
        this.departmentBilling = departmentBilling;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Boolean getIsPartnerSellWith() {
        return isPartnerSellWith;
    }

    public void setIsPartnerSellWith(Boolean partnerSellWith) {
        isPartnerSellWith = partnerSellWith;
    }

    public Boolean getIsPartnerSellThrough() {
        return isPartnerSellThrough;
    }

    public void setIsPartnerSellThrough(Boolean partnerSellThrough) {
        isPartnerSellThrough = partnerSellThrough;
    }

    public List<String> getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(List<String> siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getQuoteCategory() {
        return quoteCategory;
    }

    public void setQuoteCategory(String quoteCategory) {
        this.quoteCategory = quoteCategory;
    }

    public Boolean getMacd() {
        return isMacd;
    }

    public void setMacd(Boolean macd) {
        isMacd = macd;
    }

    public Boolean getRemoveOutbound() {
        return removeOutbound;
    }

    public void setRemoveOutbound(Boolean removeOutbound) {
        this.removeOutbound = removeOutbound;
    }

    public List<GscMultiMacdServiceBean> getGscMultiMacdServices() {
        return gscMultiMacdServices;
    }

    public void setGscMultiMacdServices(List<GscMultiMacdServiceBean> gscMultiMacdServices) {
        this.gscMultiMacdServices = gscMultiMacdServices;
    }

    public Integer getContractTermSimplified() {
        return contractTermSimplified;
    }

    public void setContractTermSimplified(Integer contractTermSimplified) {
        this.contractTermSimplified = contractTermSimplified;
    }

    public String getPartnerCustomerAddress() {
        return partnerCustomerAddress;
    }

    public void setPartnerCustomerAddress(String partnerCustomerAddress) {
        this.partnerCustomerAddress = partnerCustomerAddress;
    }

    public String getPartnerCustomerContactName() {
        return partnerCustomerContactName;
    }

    public void setPartnerCustomerContactName(String partnerCustomerContactName) {
        this.partnerCustomerContactName = partnerCustomerContactName;
    }

    public String getPartnerCustomerContactEmail() {
        return partnerCustomerContactEmail;
    }

    public void setPartnerCustomerContactEmail(String partnerCustomerContactEmail) {
        this.partnerCustomerContactEmail = partnerCustomerContactEmail;
    }

    public String getTnc() {
        return tnc;
    }

    public void setTnc(String tnc) {
        this.tnc = tnc;
    }

    public Boolean getIsTnc() {
        return isTnc;
    }

    public void setIsTnc(Boolean isTnc) {
        this.isTnc = isTnc;
    }

    public String getInterconnectId() {
        return interconnectId;
    }

    public void setInterconnectId(String interconnectId) {
        this.interconnectId = interconnectId;
    }

    public String getInterconnectName() {
        return interconnectName;
    }

    public void setInterconnectName(String interconnectName) {
        this.interconnectName = interconnectName;
    }

    public Boolean getIsWholesaleNgp() {
        return isWholesaleNgp;
    }

    public void setIsWholesaleNgp(Boolean wholesaleNgp) {
        this.isWholesaleNgp = wholesaleNgp;
    }

    @Override
    public String toString() {
        return "GscCofPdfBean{" +
                "orderRef='" + orderRef + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", orderType='" + orderType + '\'' +
                ", baseUrl='" + baseUrl + '\'' +
                ", customerContractingEntity='" + customerContractingEntity + '\'' +
                ", customerGstNumber='" + customerGstNumber + '\'' +
                ", customerVatNumber='" + customerVatNumber + '\'' +
                ", customerContactName='" + customerContactName + '\'' +
                ", billingPaymentsName='" + billingPaymentsName + '\'' +
                ", customerContactNumber='" + customerContactNumber + '\'' +
                ", billingContactNumber='" + billingContactNumber + '\'' +
                ", customerEmailId='" + customerEmailId + '\'' +
                ", billingEmailId='" + billingEmailId + '\'' +
                ", supplierContactEntity='" + supplierContactEntity + '\'' +
                ", supplierAddress='" + supplierAddress + '\'' +
                ", supplierAccountManager='" + supplierAccountManager + '\'' +
                ", supplierContactNumber='" + supplierContactNumber + '\'' +
                ", supplierEmailId='" + supplierEmailId + '\'' +
                ", supplierGstnNumber='" + supplierGstnNumber + '\'' +
                ", supplierWithCopyToAddress='" + supplierWithCopyToAddress + '\'' +
                ", billingMethod='" + billingMethod + '\'' +
                ", billingFreq='" + billingFreq + '\'' +
                ", billingCurrency='" + billingCurrency + '\'' +
                ", billingType='" + billingType + '\'' +
                ", paymentCurrency='" + paymentCurrency + '\'' +
                ", paymentTerm='" + paymentTerm + '\'' +
                ", invoiceMethod='" + invoiceMethod + '\'' +
                ", contractTerm='" + contractTerm + '\'' +
                ", paymentOptions='" + paymentOptions + '\'' +
                ", billingIncrement='" + billingIncrement + '\'' +
                ", applicableTimeZone='" + applicableTimeZone + '\'' +
                ", noticeAddress='" + noticeAddress + '\'' +
                ", billingAddress='" + billingAddress + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", customerState='" + customerState + '\'' +
                ", customerPincode='" + customerPincode + '\'' +
                ", customerCity='" + customerCity + '\'' +
                ", customerCountry='" + customerCountry + '\'' +
                ", productName='" + productName + '\'' +
                ", totalMRC=" + totalMRC +
                ", totalNRC=" + totalNRC +
                ", totalARC=" + totalARC +
                ", totalTCV=" + totalTCV +
                ", presentDate='" + presentDate + '\'' +
                ", publicIp='" + publicIp + '\'' +
                ", isNat=" + isNat +
                ", isApproved=" + isApproved +
                ", isMSA=" + isMSA +
                ", isSS=" + isSS +
                ", isSSStandard=" + isSSStandard +
                ", isSSPremium=" + isSSPremium +
                ", isDocusign=" + isDocusign +
                ", isArc=" + isArc +
                ", isInbound=" + isInbound +
                ", quoteId=" + quoteId +
                ", quoteLeId=" + quoteLeId +
                ", customerId=" + customerId +
                ", productFamilyName='" + productFamilyName + '\'' +
                ", accessType='" + accessType + '\'' +
                ", profileName='" + profileName + '\'' +
                ", solutions=" + solutions +
                ", legalEntities=" + legalEntities +
                ", sizeAttribute=" + sizeAttribute +
//                ", gvpnQuote=" + gvpnQuote +
                ", creditLimit='" + creditLimit + '\'' +
                ", depositAmount='" + depositAmount + '\'' +
                ", inboundVolume='" + inboundVolume + '\'' +
                ", outboundVolume='" + outboundVolume + '\'' +
                ", inboundVolumeCountry='" + inboundVolumeCountry + '\'' +
                ", supplierNoticeAddress='" + supplierNoticeAddress + '\'' +
                ", isPartnerSellWith=" + isPartnerSellWith +
                ", isPartnerSellThrough=" + isPartnerSellThrough +
                ", poNumber='" + poNumber + '\'' +
                ", poDate='" + poDate + '\'' +
                ", isInternational=" + isInternational +
                ", approverName1='" + approverName1 + '\'' +
                ", approverName2='" + approverName2 + '\'' +
                ", approverEmail1='" + approverEmail1 + '\'' +
                ", approverEmail2='" + approverEmail2 + '\'' +
                ", approverSignedDate1='" + approverSignedDate1 + '\'' +
                ", approverSignedDate2='" + approverSignedDate2 + '\'' +
                ", showReviewerTable=" + showReviewerTable +
                ", isDomesticVoice=" + isDomesticVoice +
                ", customerName1='" + customerName1 + '\'' +
                ", customerEmail1='" + customerEmail1 + '\'' +
                ", customerSignedDate1='" + customerSignedDate1 + '\'' +
                ", customerName2='" + customerName2 + '\'' +
                ", customerEmail2='" + customerEmail2 + '\'' +
                ", customerSignedDate2='" + customerSignedDate2 + '\'' +
                ", showCustomerSignerTable=" + showCustomerSignerTable +
                ", partnerCustomerLeName='" + partnerCustomerLeName + '\'' +
                ", partnerCustomerLeState='" + partnerCustomerLeState + '\'' +
                ", partnerCustomerLeCity='" + partnerCustomerLeCity + '\'' +
                ", partnerCustomerLeCountry='" + partnerCustomerLeCountry + '\'' +
                ", partnerCustomerLeZip='" + partnerCustomerLeZip + '\'' +
                ", partnerCustomerLeWebsite='" + partnerCustomerLeWebsite + '\'' +
                ", partnerCustomerContactName='" + partnerCustomerContactName + '\'' +
                ", partnerCustomerContactEmail='" + partnerCustomerContactEmail + '\'' +
                ", partnerCustomerAddress='" + partnerCustomerAddress + '\'' +
                ", departmentBilling='" + departmentBilling + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", outboundVolumeCountry='" + outboundVolumeCountry + '\'' +
                '}';
    }
}
