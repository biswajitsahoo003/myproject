package com.tcl.dias.oms.gvpn.pdf.beans;

import java.util.List;

import com.tcl.dias.common.beans.CpeDetails;

/**
 * 
 * This file contains the GvpnQuotePdfBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GvpnQuotePdfBean {
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
	private String vpnName;
	private List<GvpnCommercial> commercials;
	private List<GvpnSolution> solutions;
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
	private String ikey;
	private Boolean isGst = false;
	private Boolean isVat = true;

	private Boolean isGstSup = false;
	private Boolean isVatSup = true;
	// MACD Change ----->
	private String sfdcOpportunityId;
	private String serviceId;
	private String quoteCategory;
	private String quoteType;
	private String accessType;
	private String accessProvider;
	private Boolean isCpeChanged = false;
	private String linkType;
	private String primaryOldCpe;
	private String secondaryOldCpe;
	private String serviceCombinationType;
	private String primaryServiceId;
	private String secondaryServiceId;
	private Boolean isMultiCircuit = false;
	// <----- MACD Change

	// <--- currency Formatting changes
	private String totalMRCFormatted;
	private String totalNRCFormatted;
	private String totalTCVFormatted;
	private String totalARCFormatted;

	// currency Formatting changes --->
	private Boolean isPartner = false;
	private Boolean isPartnerSellWith = false;
	private Boolean isPartnerSellThrough = false;

	// --------PT-189--------//
	private String poNumber;
	private String poDate;

	// --IAP-41 and IAP-10--//

	private Boolean isInternational;

	private String parallelRunDays;
	private String customerVatNumber;
	private String supplierVatNumber;

	private String creditLimit;
	private String securityDepositAmount;
	// --------PT-467--------//
	private String approverName1;
	private String approverName2;
	private String approverEmail1;
	private String approverEmail2;
	private String approverSignedDate1;
	private String approverSignedDate2;
	private Boolean showReviewerTable = false;

	private String customerName1;
	private String customerEmail1;
	private String customerSignedDate1;
	private String customerName2;
	private String customerEmail2;
	private String customerSignedDate2;
	private Boolean showCustomerSignerTable = false;

	private String tnc;
	private Boolean isTnc = false;
	private Integer amendment;
	private String parentOrderNo;

	private String demarcationApartment;
	private String demarcationFloor;
	private String demarcationRack;
	private String demarcationRoom;
	private String isOrderEnrichmentAttributesProvided;

	//for demo
	private Boolean demoOrder;
	private String demoType;
	private String billingType;

	//PIPF-212
	private String ownerEmailSfdc;
	private String ownerNameSfdc;
	
	//added for mvrf
	private String mvrfSolution;
	private String noOfVrf;
	
	// MVRFs
	
	private VrfBean vrfBean;
	
	private String commercialChanges;
	private Boolean commercialSignerTable = false;
	private String effectiveDate;
	
	//sitelevelbilling annexure;
	private MultiSiteAnnexure multiSiteAnnexure;
	private Boolean isMultiSiteAnnexure = false;
	
	
	public Boolean getIsMultiSiteAnnexure() {
		return isMultiSiteAnnexure;
	}

	public void setIsMultiSiteAnnexure(Boolean isMultiSiteAnnexure) {
		this.isMultiSiteAnnexure = isMultiSiteAnnexure;
	}
	
	public MultiSiteAnnexure getMultiSiteAnnexure() {
		return multiSiteAnnexure;
	}

	public void setMultiSiteAnnexure(MultiSiteAnnexure multiSiteAnnexure) {
		this.multiSiteAnnexure = multiSiteAnnexure;
	}

	public String getMvrfSolution() {
		return mvrfSolution;
	}

	public void setMvrfSolution(String mvrfSolution) {
		this.mvrfSolution = mvrfSolution;
	}

	public String getNoOfVrf() {
		return noOfVrf;
	}

	public void setNoOfVrf(String noOfVrf) {
		this.noOfVrf = noOfVrf;
	}

	public Boolean getIsPartnerSellWith() {
		return isPartnerSellWith;
	}

	public void setIsPartnerSellWith(Boolean partnerSellWith) {
		isPartnerSellWith = partnerSellWith;
	}

	public Boolean getIsPartner() {
		return isPartner;
	}

	public void setIsPartner(Boolean partner) {
		isPartner = partner;
	}

	public Boolean getIsPartnerSellThrough() {
		return isPartnerSellThrough;
	}

	public void setIsPartnerSellThrough(Boolean partnerSellThrough) {
		isPartnerSellThrough = partnerSellThrough;
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

	public List<GvpnCommercial> getCommercials() {
		return commercials;
	}

	public void setCommercials(List<GvpnCommercial> commercials) {
		this.commercials = commercials;
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

	public List<GvpnSolution> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<GvpnSolution> solutions) {
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
	 * @return the isSS
	 */
	public Boolean getIsSS() {
		return isSS;
	}

	/**
	 * @param isSS the isSS to set
	 */
	public void setIsSS(Boolean isSS) {
		this.isSS = isSS;
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

	/**
	 * @return the isSSEnhanced
	 */
	public Boolean getIsSSEnhanced() {
		return isSSEnhanced;
	}

	/**
	 * @param isSSEnhanced the isSSEnhanced to set
	 */
	public void setIsSSEnhanced(Boolean isSSEnhanced) {
		this.isSSEnhanced = isSSEnhanced;
	}

	/**
	 * @return the isArc
	 */
	public Boolean getIsArc() {
		return isArc;
	}

	/**
	 * @param isArc the isArc to set
	 */
	public void setIsArc(Boolean isArc) {
		this.isArc = isArc;
	}

	/**
	 * @return the totalARC
	 */
	public Double getTotalARC() {
		return totalARC;
	}

	/**
	 * @param totalARC the totalARC to set
	 */
	public void setTotalARC(Double totalARC) {
		this.totalARC = totalARC;
	}

	/**
	 * @return the baseUrl
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * @param baseUrl the baseUrl to set
	 */
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

	// MACD Change ----->

	public String getSfdcOpportunityId() {
		return sfdcOpportunityId;
	}

	public void setSfdcOpportunityId(String sfdcOpportunityId) {
		this.sfdcOpportunityId = sfdcOpportunityId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getQuoteCategory() {
		return quoteCategory;
	}

	public void setQuoteCategory(String quoteCategory) {
		this.quoteCategory = quoteCategory;
	}

	public String getQuoteType() {
		return quoteType;
	}

	public void setQuoteType(String quoteType) {
		this.quoteType = quoteType;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getAccessProvider() {
		return accessProvider;
	}

	public void setAccessProvider(String accessProvider) {
		this.accessProvider = accessProvider;
	}

	public String getLinkType() {
		return linkType;
	}

	public void setLinkType(String linkType) {
		this.linkType = linkType;
	}

	public Boolean getIsCpeChanged() {
		return isCpeChanged;
	}

	public void setIsCpeChanged(Boolean isCpeChanged) {
		this.isCpeChanged = isCpeChanged;
	}

	public String getServiceCombinationType() {
		return serviceCombinationType;
	}

	public void setServiceCombinationType(String serviceCombinationType) {
		this.serviceCombinationType = serviceCombinationType;
	}

	public String getVpnName() {
		return vpnName;
	}

	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}

	public String getPrimaryOldCpe() {
		return primaryOldCpe;
	}

	public void setPrimaryOldCpe(String primaryOldCpe) {
		this.primaryOldCpe = primaryOldCpe;
	}

	public String getSecondaryOldCpe() {
		return secondaryOldCpe;
	}

	public void setSecondaryOldCpe(String secondaryOldCpe) {
		this.secondaryOldCpe = secondaryOldCpe;
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

	public String getSecondaryServiceId() {
		return secondaryServiceId;
	}

	public void setSecondaryServiceId(String secondaryServiceId) {
		this.secondaryServiceId = secondaryServiceId;
	}

	public Boolean getIsInternational() {
		return isInternational;
	}

	public void setIsInternational(Boolean isInternational) {
		this.isInternational = isInternational;
	}

	public String getPrimaryServiceId() {
		return primaryServiceId;
	}

	public void setPrimaryServiceId(String primaryServiceId) {
		this.primaryServiceId = primaryServiceId;
	}

	/**
	 * @return the parallelRunDays
	 */
	public String getParallelRunDays() {
		return parallelRunDays;
	}

	/**
	 * @param parallelRunDays the parallelRunDays to set
	 */
	public void setParallelRunDays(String parallelRunDays) {
		this.parallelRunDays = parallelRunDays;
	}

	/**
	 * @return the creditLimit
	 */
	public String getCreditLimit() {
		return creditLimit;
	}

	/**
	 * @param creditLimit the creditLimit to set
	 */
	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}

	/**
	 * @return the securityDepositAmount
	 */
	public String getSecurityDepositAmount() {
		return securityDepositAmount;
	}

	/**
	 * @param securityDepositAmount the securityDepositAmount to set
	 */
	public void setSecurityDepositAmount(String securityDepositAmount) {
		this.securityDepositAmount = securityDepositAmount;
	}

	public String getCustomerVatNumber() {
		return customerVatNumber;
	}

	public void setCustomerVatNumber(String customerVatNumber) {
		this.customerVatNumber = customerVatNumber;
	}

	public String getSupplierVatNumber() {
		return supplierVatNumber;
	}

	public void setSupplierVatNumber(String supplierVatNumber) {
		this.supplierVatNumber = supplierVatNumber;
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

	public Boolean getIsMultiCircuit() {
		return isMultiCircuit;
	}

	public void setIsMultiCircuit(Boolean multiCircuit) {
		isMultiCircuit = multiCircuit;
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

	public Integer getAmendment() {
		return amendment;
	}

	public void setAmendment(Integer amendment) {
		this.amendment = amendment;
	}

	public String getParentOrderNo() {
		return parentOrderNo;
	}

	public String getDemarcationApartment() {
		return demarcationApartment;
	}

	public void setDemarcationApartment(String demarcationApartment) {
		this.demarcationApartment = demarcationApartment;
	}

	public String getDemarcationFloor() {
		return demarcationFloor;
	}

	public void setDemarcationFloor(String demarcationFloor) {
		this.demarcationFloor = demarcationFloor;
	}

	public String getDemarcationRack() {
		return demarcationRack;
	}

	public void setDemarcationRack(String demarcationRack) {
		this.demarcationRack = demarcationRack;
	}

	public String getDemarcationRoom() {
		return demarcationRoom;
	}

	public void setDemarcationRoom(String demarcationRoom) {
		this.demarcationRoom = demarcationRoom;
	}

	public String getIsOrderEnrichmentAttributesProvided() {
		return isOrderEnrichmentAttributesProvided;
	}

	public void setIsOrderEnrichmentAttributesProvided(String isOrderEnrichmentAttributesProvided) {
		this.isOrderEnrichmentAttributesProvided = isOrderEnrichmentAttributesProvided;
	}

	public void setParentOrderNo(String parentOrderNo) {
		this.parentOrderNo = parentOrderNo;
		

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

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public String getIkey() {
		return ikey;
	}

	public void setIkey(String ikey) {
		this.ikey = ikey;
	}

	public String getOwnerEmailSfdc() {
		return ownerEmailSfdc;
	}

	public void setOwnerEmailSfdc(String ownerEmailSfdc) {
		this.ownerEmailSfdc = ownerEmailSfdc;
	}

	public String getOwnerNameSfdc() {
		return ownerNameSfdc;
	}

	public void setOwnerNameSfdc(String ownerNameSfdc) {
		this.ownerNameSfdc = ownerNameSfdc;
	}

	public VrfBean getVrfBean() {
		return vrfBean;
	}

	public void setVrfBean(VrfBean vrfBean) {
		this.vrfBean = vrfBean;
	}

	
	public String getCommercialChanges() {
		return commercialChanges;
	}

	public void setCommercialChanges(String commercialChanges) {
		this.commercialChanges = commercialChanges;
	}

	public Boolean getCommercialSignerTable() {
		return commercialSignerTable;
	}

	public void setCommercialSignerTable(Boolean commercialSignerTable) {
		this.commercialSignerTable = commercialSignerTable;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	@Override
	public String toString() {
		return "GvpnQuotePdfBean{" +
				"orderRef='" + orderRef + '\'' +
				", orderDate='" + orderDate + '\'' +
				", orderType='" + orderType + '\'' +
				", customerContractingEntity='" + customerContractingEntity + '\'' +
				", customerGstNumber='" + customerGstNumber + '\'' +
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
				", billingMethod='" + billingMethod + '\'' +
				", billingFreq='" + billingFreq + '\'' +
				", billingCurrency='" + billingCurrency + '\'' +
				", paymentCurrency='" + paymentCurrency + '\'' +
				", paymentTerm='" + paymentTerm + '\'' +
				", invoiceMethod='" + invoiceMethod + '\'' +
				", contractTerm='" + contractTerm + '\'' +
				", billingAddress='" + billingAddress + '\'' +
				", customerAddress='" + customerAddress + '\'' +
				", customerState='" + customerState + '\'' +
				", customerPincode='" + customerPincode + '\'' +
				", customerCity='" + customerCity + '\'' +
				", customerCountry='" + customerCountry + '\'' +
				", productName='" + productName + '\'' +
				", totalMRC=" + totalMRC +
				", totalNRC=" + totalNRC +
				", totalTCV=" + totalTCV +
				", totalARC=" + totalARC +
				", vpnName='" + vpnName + '\'' +
				", commercials=" + commercials +
				", solutions=" + solutions +
				", presentDate='" + presentDate + '\'' +
				", publicIp='" + publicIp + '\'' +
				", isNat=" + isNat +
				", isApproved=" + isApproved +
				", isMSA=" + isMSA +
				", isSS=" + isSS +
				", isSSStandard=" + isSSStandard +
				", isSSPremium=" + isSSPremium +
				", isSSEnhanced=" + isSSEnhanced +
				", isArc=" + isArc +
				", baseUrl='" + baseUrl + '\'' +
				", isDocusign=" + isDocusign +
				", cpeDetails=" + cpeDetails +
				", ikey='" + ikey + '\'' +
				", isGst=" + isGst +
				", isVat=" + isVat +
				", isGstSup=" + isGstSup +
				", isVatSup=" + isVatSup +
				", sfdcOpportunityId='" + sfdcOpportunityId + '\'' +
				", serviceId='" + serviceId + '\'' +
				", quoteCategory='" + quoteCategory + '\'' +
				", quoteType='" + quoteType + '\'' +
				", accessType='" + accessType + '\'' +
				", accessProvider='" + accessProvider + '\'' +
				", isCpeChanged=" + isCpeChanged +
				", linkType='" + linkType + '\'' +
				", primaryOldCpe='" + primaryOldCpe + '\'' +
				", secondaryOldCpe='" + secondaryOldCpe + '\'' +
				", serviceCombinationType='" + serviceCombinationType + '\'' +
				", primaryServiceId='" + primaryServiceId + '\'' +
				", secondaryServiceId='" + secondaryServiceId + '\'' +
				", isMultiCircuit=" + isMultiCircuit +
				", totalMRCFormatted='" + totalMRCFormatted + '\'' +
				", totalNRCFormatted='" + totalNRCFormatted + '\'' +
				", totalTCVFormatted='" + totalTCVFormatted + '\'' +
				", totalARCFormatted='" + totalARCFormatted + '\'' +
				", isPartner=" + isPartner +
				", isPartnerSellWith=" + isPartnerSellWith +
				", isPartnerSellThrough=" + isPartnerSellThrough +
				", poNumber='" + poNumber + '\'' +
				", poDate='" + poDate + '\'' +
				", isInternational=" + isInternational +
				", parallelRunDays='" + parallelRunDays + '\'' +
				", customerVatNumber='" + customerVatNumber + '\'' +
				", supplierVatNumber='" + supplierVatNumber + '\'' +
				", creditLimit='" + creditLimit + '\'' +
				", securityDepositAmount='" + securityDepositAmount + '\'' +
				", approverName1='" + approverName1 + '\'' +
				", approverName2='" + approverName2 + '\'' +
				", approverEmail1='" + approverEmail1 + '\'' +
				", approverEmail2='" + approverEmail2 + '\'' +
				", approverSignedDate1='" + approverSignedDate1 + '\'' +
				", approverSignedDate2='" + approverSignedDate2 + '\'' +
				", showReviewerTable=" + showReviewerTable +
				", customerName1='" + customerName1 + '\'' +
				", customerEmail1='" + customerEmail1 + '\'' +
				", customerSignedDate1='" + customerSignedDate1 + '\'' +
				", customerName2='" + customerName2 + '\'' +
				", customerEmail2='" + customerEmail2 + '\'' +
				", customerSignedDate2='" + customerSignedDate2 + '\'' +
				", showCustomerSignerTable=" + showCustomerSignerTable +
				", tnc='" + tnc + '\'' +
				", isTnc=" + isTnc +
				", amendment=" + amendment +
				", parentOrderNo='" + parentOrderNo + '\'' +
				", demarcationApartment='" + demarcationApartment + '\'' +
				", demarcationFloor='" + demarcationFloor + '\'' +
				", demarcationRack='" + demarcationRack + '\'' +
				", demarcationRoom='" + demarcationRoom + '\'' +
				", isOrderEnrichmentAttributesProvided='" + isOrderEnrichmentAttributesProvided + '\'' +
				", demoOrder=" + demoOrder +
				", demoType='" + demoType + '\'' +
				", billingType='" + billingType + '\'' +
				", ownerEmailSfdc='" + ownerEmailSfdc + '\'' +
				", ownerNameSfdc='" + ownerNameSfdc + '\'' +
				'}';
	}


}
