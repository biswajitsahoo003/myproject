package com.tcl.dias.oms.izopc.pdf.beans;

import java.util.List;

import com.tcl.dias.common.beans.CpeDetails;

/**
 * Bean class to hold data related to IZOPC quote 
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

public class IzoPcQuotePdfBean {
	private String orderRef;
	private String orderDate;
	private String orderType;
	private String customerContractingEntity;
	private String customerGstNumber;
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
	private String billingMethod;
	private String billingFreq;
	private String billingCurrency;
	private String paymentCurrency;
	private String paymentTerm;
	private String invoiceMethod;
	private String contractTerm;
	private String billingAddress;
	private String customerAddress;
	private String customerState;
	private String customerPincode;
	private String customerCity;
	private String customerCountry;
	private String productName;
	private Double totalMRC;
	private Double totalNRC;
	private Double totalTCV;
	private Double totalARC;
	private List<IzoPcCommercial> commercials;
	private List<IzoPcSolution> solutions;
	private String presentDate;
	private String publicIp;
	private Boolean isNat = true;
	private Boolean isApproved = false;
	private Boolean isMSA = false;
	private Boolean isSS = false;
	private Boolean isSSStandard;
	private Boolean isSSPremium;
	private Boolean isSSEnhanced;
	private Boolean isArc = true;
	private String baseUrl;
	private Boolean isDocusign = false;
	private CpeDetails cpeDetails;
	private Boolean isGst = false;
	private Boolean isVat = true;
	
	private Boolean isGstSup = false;
	private Boolean isVatSup = true;
	
	// Value holds currency specific formatted values
	private String totalMRCFormatted;
	private String totalNRCFormatted;
	private String totalTCVFormatted;
	private String totalARCFormatted;
	
	//--------PT-189--------//
	private String poNumber;
	private String poDate;
	
	//Macd Attributes
	private String sfdcOpportunityId;
	private Boolean isMultiCircuit = false;
	private Integer amendment;
	private String serviceCombinationType;
	private Boolean demoOrder;
	private String demoType;
	private String quoteCategory;
	private String parentOrderNo;
	private String serviceId;
	private String primaryServiceId;
	private String secondaryServiceId;
	private String linkType;
	
	private Boolean isInternational;
	
	
	public Boolean getIsInternational() {
		return isInternational;
	}
	public void setIsInternational(Boolean isInternational) {
		this.isInternational = isInternational;
	}
	public String getTotalMRCFormatted() {
		return totalMRCFormatted;
	}
	public void setTotalMRCFormatted(String totalMRCFormatted) {
		this.totalMRCFormatted = totalMRCFormatted;
	}
	public String getTotalNRCFormatted() {
		return totalNRCFormatted;
	}
	public void setTotalNRCFormatted(String totalNRCFormatted) {
		this.totalNRCFormatted = totalNRCFormatted;
	}
	public String getTotalTCVFormatted() {
		return totalTCVFormatted;
	}
	public void setTotalTCVFormatted(String totalTCVFormatted) {
		this.totalTCVFormatted = totalTCVFormatted;
	}
	public String getTotalARCFormatted() {
		return totalARCFormatted;
	}
	public void setTotalARCFormatted(String totalARCFormatted) {
		this.totalARCFormatted = totalARCFormatted;
	}
	public Boolean getIsGstSup() {
		return isGstSup;
	}
	public void setIsGstSup(Boolean isGstSup) {
		this.isGstSup = isGstSup;
	}
	public Boolean getIsVatSup() {
		return isVatSup;
	}
	public void setIsVatSup(Boolean isVatSup) {
		this.isVatSup = isVatSup;
	}
	public Boolean getIsGst() {
		return isGst;
	}
	public void setIsGst(Boolean isGst) {
		this.isGst = isGst;
	}
	public Boolean getIsVat() {
		return isVat;
	}
	public void setIsVat(Boolean isVat) {
		this.isVat = isVat;
	}
	public String getOrderRef() {
		return orderRef;
	}
	public void setOrderRef(String orderRef) {
		this.orderRef = orderRef;
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
	public Double getTotalARC() {
		return totalARC;
	}
	public void setTotalARC(Double totalARC) {
		this.totalARC = totalARC;
	}
	public List<IzoPcCommercial> getCommercials() {
		return commercials;
	}
	public void setCommercials(List<IzoPcCommercial> commercials) {
		this.commercials = commercials;
	}
	public List<IzoPcSolution> getSolutions() {
		return solutions;
	}
	public void setSolutions(List<IzoPcSolution> solutions) {
		this.solutions = solutions;
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
	public Boolean getIsMSA() {
		return isMSA;
	}
	public void setIsMSA(Boolean isMSA) {
		this.isMSA = isMSA;
	}
	public Boolean getIsSS() {
		return isSS;
	}
	public void setIsSS(Boolean isSS) {
		this.isSS = isSS;
	}
	public Boolean getIsSSStandard() {
		return isSSStandard;
	}
	public void setIsSSStandard(Boolean isSSStandard) {
		this.isSSStandard = isSSStandard;
	}
	public Boolean getIsSSPremium() {
		return isSSPremium;
	}
	public void setIsSSPremium(Boolean isSSPremium) {
		this.isSSPremium = isSSPremium;
	}
	public Boolean getIsSSEnhanced() {
		return isSSEnhanced;
	}
	public void setIsSSEnhanced(Boolean isSSEnhanced) {
		this.isSSEnhanced = isSSEnhanced;
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
	public Boolean getIsDocusign() {
		return isDocusign;
	}
	public void setIsDocusign(Boolean isDocusign) {
		this.isDocusign = isDocusign;
	}
	public CpeDetails getCpeDetails() {
		return cpeDetails;
	}
	public void setCpeDetails(CpeDetails cpeDetails) {
		this.cpeDetails = cpeDetails;
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
	public String getSfdcOpportunityId() {
		return sfdcOpportunityId;
	}
	public void setSfdcOpportunityId(String sfdcOpportunityId) {
		this.sfdcOpportunityId = sfdcOpportunityId;
	}
	public Boolean getIsMultiCircuit() {
		return isMultiCircuit;
	}
	public void setIsMultiCircuit(Boolean isMultiCircuit) {
		this.isMultiCircuit = isMultiCircuit;
	}
	public Integer getAmendment() {
		return amendment;
	}
	public void setAmendment(Integer amendment) {
		this.amendment = amendment;
	}
	public String getServiceCombinationType() {
		return serviceCombinationType;
	}
	public void setServiceCombinationType(String serviceCombinationType) {
		this.serviceCombinationType = serviceCombinationType;
	}
	public Boolean getDemoOrder() {
		return demoOrder;
	}
	public void setDemoOrder(Boolean demoOrder) {
		this.demoOrder = demoOrder;
	}
	public String getDemoType() {
		return demoType;
	}
	public void setDemoType(String demoType) {
		this.demoType = demoType;
	}
	public String getQuoteCategory() {
		return quoteCategory;
	}
	public void setQuoteCategory(String quoteCategory) {
		this.quoteCategory = quoteCategory;
	}
	public String getParentOrderNo() {
		return parentOrderNo;
	}
	public void setParentOrderNo(String parentOrderNo) {
		this.parentOrderNo = parentOrderNo;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getPrimaryServiceId() {
		return primaryServiceId;
	}
	public void setPrimaryServiceId(String primaryServiceId) {
		this.primaryServiceId = primaryServiceId;
	}
	public String getSecondaryServiceId() {
		return secondaryServiceId;
	}
	public void setSecondaryServiceId(String secondaryServiceId) {
		this.secondaryServiceId = secondaryServiceId;
	}
	public String getLinkType() {
		return linkType;
	}
	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}
	
	
	
}
