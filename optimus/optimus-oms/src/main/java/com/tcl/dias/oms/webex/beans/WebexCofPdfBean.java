package com.tcl.dias.oms.webex.beans;

import com.tcl.dias.common.webex.beans.WebexProductPricesResponse;
import com.tcl.dias.oms.gsc.beans.GscQuoteToLeBean;
import com.tcl.dias.oms.gsc.beans.GscSolutionBean;
import com.tcl.dias.oms.gvpn.pdf.beans.GvpnQuotePdfBean;

import java.util.List;
import java.util.Map;

/**
 * Class for Webex Cof Pdf Bean which contains the attributes for generating COF
 * PDF
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class WebexCofPdfBean {
	private String orderRef;
	private String opportunityId;
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
	private String outboundVolumeCountry;

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
	private GvpnQuotePdfBean gvpnQuote;
	private String creditLimit;
	private String depositAmount;
	private String inboundVolume;
	private String outboundVolume;
	private String inboundVolumeCountry;
	private String supplierNoticeAddress;
	private Boolean isPartner = false;

	// webex Cof Attibutes
	private WebexSolutionBean webexSolutionBean;
	private Map<Integer, List<QuoteUcaasBean>> endpoints;
	private String primaryBridge;
	private String audioModel;
	private String paymentModel;
	private String totalMrc;
	private String totalPrice;
	private String license;
	private Boolean dialIn;
	private Boolean dialOut;
	private Boolean dialBack;
	private WebexProductPricesResponse pricesResponse;
	private QuoteUcaasBean skuDetails;
	// --------PT-189--------//
	private String poNumber;

	private String poDate;

	private Boolean isInternational;

	//Existing GVPN attributes
	private boolean isExistingGVPN;
	private String serviceId;
	private String siteAddress;
	private String aliasName;
	private String bandwidth;

	public WebexSolutionBean getWebexSolutionBean() {
		return webexSolutionBean;
	}

	public void setWebexSolutionBean(WebexSolutionBean webexSolutionBean) {
		this.webexSolutionBean = webexSolutionBean;
	}

	public String getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}

	public String getPrimaryBridge() {
		return primaryBridge;
	}

	public void setPrimaryBridge(String primaryBridge) {
		this.primaryBridge = primaryBridge;
	}

	public Boolean getIsInternational() {
		return isInternational;
	}

	public void setIsInternational(Boolean isInternational) {
		this.isInternational = isInternational;
	}

	public Boolean getIsPartner() {
		return isPartner;
	}

	public void setIsPartner(Boolean partner) {
		isPartner = partner;
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

	public GvpnQuotePdfBean getGvpnQuote() {
		return gvpnQuote;
	}

	public void setGvpnQuote(GvpnQuotePdfBean gvpnQuote) {
		this.gvpnQuote = gvpnQuote;
	}

	public Boolean getIsInbound() {
		return isInbound;
	}

	public void setIsInbound(Boolean isInbound) {
		this.isInbound = isInbound;
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

	public String getAudioModel() {
		return audioModel;
	}

	public void setAudioModel(String audioModel) {
		this.audioModel = audioModel;
	}

	public WebexProductPricesResponse getPricesResponse() {
		return pricesResponse;
	}

	public void setPricesResponse(WebexProductPricesResponse pricesResponse) {
		this.pricesResponse = pricesResponse;
	}

	public Boolean getDialIn() {
		return dialIn;
	}

	public void setDialIn(Boolean dialIn) {
		this.dialIn = dialIn;
	}

	public Boolean getDialOut() {
		return dialOut;
	}

	public void setDialOut(Boolean dialOut) {
		this.dialOut = dialOut;
	}

	public Boolean getDialBack() {
		return dialBack;
	}

	public void setDialBack(Boolean dialBack) {
		this.dialBack = dialBack;
	}

	public String getPaymentModel() {
		return paymentModel;
	}

	public void setPaymentModel(String paymentModel) {
		this.paymentModel = paymentModel;
	}

	public QuoteUcaasBean getSkuDetails() {
		return skuDetails;
	}

	public void setSkuDetails(QuoteUcaasBean skuDetails) {
		this.skuDetails = skuDetails;
	}

	public boolean getIsExistingGVPN() {
		return isExistingGVPN;
	}

	public void setIsExistingGVPN(boolean existing) {
		isExistingGVPN = existing;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	public Map<Integer, List<QuoteUcaasBean>> getEndpoints() {
		return endpoints;
	}

	public void setEndpoints(Map<Integer, List<QuoteUcaasBean>> endpoints) {
		this.endpoints = endpoints;
	}
}
