package com.tcl.dias.oms.pdf.beans;

import java.util.List;

import com.tcl.dias.common.beans.CpeDetails;
import com.tcl.dias.oms.gvpn.pdf.beans.MultiSiteAnnexure;

/**
 * This file is the bean class for quote and cof pdf
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class IllQuotePdfBean {
	private String orderRef;
	private String orderDate;
	private String orderType;
	private String baseUrl;
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
	private String totalMRC;
	private String totalNRC;
	private String totalARC;
	private String totalTCV;
	private List<IllCommercial> commercials;
	private List<IllSolution> solutions;
	private String presentDate;
	private String publicIp;
	private Boolean isNat = true;
	private Boolean isApproved = false;
	private Boolean isMSA = false;
	private Boolean isSS = false;
	private Boolean isSSStandard;
	private Boolean isSSPremium;
	private Boolean isDocusign = false;
	private Boolean isArc = true;
	private CpeDetails cpeDetails;
	private String tnc;
	private Boolean isTnc = false;
	private String ikey;

	// MACD Change ----->
	private String sfdcOpportunityId;
	private String serviceId;
	private String quoteCategory;
	private String quoteType;
	private String accessType;
	private String accessProvider;
	private Boolean isCpeChanged = false;
	private String linkType;
	private String oldCpe;
	private String serviceCombinationType;
	private String primaryServiceId;
	private String secondaryServiceId;
	private String primaryOldCpe;
	private String secondaryOldCpe;
	
	//<----- MACD Change
	private Boolean isPartner=false;
	private Boolean isPartnerSellWith=false;
	private Boolean isPartnerSellThrough=false;
	private Boolean isMultiCircuit = false;

	//--------PT-189--------//

	private String poNumber;
	private String poDate;

	private String parallelRunDays;
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
	private String demarcationApartment;
	private String demarcationFloor;
	private String demarcationRack;
	private String demarcationRoom;
	private Integer amendment;
	private String parentOrderNo;
	private String isOrderEnrichmentAttributesProvided;

	//for demo
	private Boolean demoOrder;
	private String demoType;
	private String billingType;
	private String commercialName;
	private String commercialEmail;
	private String commercialSignedDate;

	//for renewals
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

	public List<IllCommercial> getCommercials() {
		return commercials;
	}

	public void setCommercials(List<IllCommercial> commercials) {
		this.commercials = commercials;
	}

	public String getTotalMRC() {
		return totalMRC;
	}

	public void setTotalMRC(String totalMRC) {
		this.totalMRC = totalMRC;
	}

	public String getTotalNRC() {
		return totalNRC;
	}

	public void setTotalNRC(String totalNRC) {
		this.totalNRC = totalNRC;
	}

	public String getTotalTCV() {
		return totalTCV;
	}

	public void setTotalTCV(String totalTCV) {
		this.totalTCV = totalTCV;
	}

	public List<IllSolution> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<IllSolution> solutions) {
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

	public String getTotalARC() {
		return totalARC;
	}

	public void setTotalARC(String totalARC) {
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

	/**
	 * @return the cpeDetails
	 */
	public CpeDetails getCpeDetails() {
		return cpeDetails;
	}

	/**
	 * @param cpeDetails the cpeDetails to set
	 */
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

	public String getOldCpe() {
		return oldCpe;
	}

	public void setOldCpe(String oldCpe) {
		this.oldCpe = oldCpe;
	}

	public Boolean getIsCpeChanged() {
		return isCpeChanged;
	}

	public void setIsCpeChanged(Boolean isCpeChanged) {
		this.isCpeChanged = isCpeChanged;
	}
	// <----- MACD Change

	public String getServiceCombinationType() {
		return serviceCombinationType;
	}

	public void setServiceCombinationType(String serviceCombinationType) {
		this.serviceCombinationType = serviceCombinationType;
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

	public Boolean getIsMultiCircuit() { return isMultiCircuit; }

	public void setIsMultiCircuit(Boolean multiCircuit) { isMultiCircuit = multiCircuit;}

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

	public Integer getAmendment() {
		return amendment;
	}

	public void setAmendment(Integer amendment) {
		this.amendment = amendment;
	}

	public String getParentOrderNo() {
		return parentOrderNo;
	}

	public void setParentOrderNo(String parentOrderNo) {
		this.parentOrderNo = parentOrderNo;
	}

	public String getIsOrderEnrichmentAttributesProvided() {
		return isOrderEnrichmentAttributesProvided;
	}

	public void setIsOrderEnrichmentAttributesProvided(String isOrderEnrichmentAttributesProvided) {
		this.isOrderEnrichmentAttributesProvided = isOrderEnrichmentAttributesProvided;
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
	
	public String getCommercialName() {
		return commercialName;
	}

	public void setCommercialName(String commercialName) {
		this.commercialName = commercialName;
	}

	public String getCommercialEmail() {
		return commercialEmail;
	}

	public void setCommercialEmail(String commercialEmail) {
		this.commercialEmail = commercialEmail;
	}

	public String getCommercialSignedDate() {
		return commercialSignedDate;
	}

	public void setCommercialSignedDate(String commercialSignedDate) {
		this.commercialSignedDate = commercialSignedDate;
	}
	
	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	@Override
	public String toString() {
		return "IllQuotePdfBean{" +
				"orderRef='" + orderRef + '\'' +
				", orderDate='" + orderDate + '\'' +
				", orderType='" + orderType + '\'' +
				", baseUrl='" + baseUrl + '\'' +
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
				", totalMRC='" + totalMRC + '\'' +
				", totalNRC='" + totalNRC + '\'' +
				", totalARC='" + totalARC + '\'' +
				", totalTCV='" + totalTCV + '\'' +
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
				", isDocusign=" + isDocusign +
				", isArc=" + isArc +
				", cpeDetails=" + cpeDetails +
				", tnc='" + tnc + '\'' +
				", isTnc=" + isTnc +
				", ikey='" + ikey + '\'' +
				", sfdcOpportunityId='" + sfdcOpportunityId + '\'' +
				", serviceId='" + serviceId + '\'' +
				", quoteCategory='" + quoteCategory + '\'' +
				", quoteType='" + quoteType + '\'' +
				", accessType='" + accessType + '\'' +
				", accessProvider='" + accessProvider + '\'' +
				", isCpeChanged=" + isCpeChanged +
				", linkType='" + linkType + '\'' +
				", oldCpe='" + oldCpe + '\'' +
				", serviceCombinationType='" + serviceCombinationType + '\'' +
				", primaryServiceId='" + primaryServiceId + '\'' +
				", secondaryServiceId='" + secondaryServiceId + '\'' +
				", primaryOldCpe='" + primaryOldCpe + '\'' +
				", secondaryOldCpe='" + secondaryOldCpe + '\'' +
				", isPartner=" + isPartner +
				", isPartnerSellWith=" + isPartnerSellWith +
				", isPartnerSellThrough=" + isPartnerSellThrough +
				", isMultiCircuit=" + isMultiCircuit +
				", poNumber='" + poNumber + '\'' +
				", poDate='" + poDate + '\'' +
				", parallelRunDays='" + parallelRunDays + '\'' +
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
				", demarcationApartment='" + demarcationApartment + '\'' +
				", demarcationFloor='" + demarcationFloor + '\'' +
				", demarcationRack='" + demarcationRack + '\'' +
				", demarcationRoom='" + demarcationRoom + '\'' +
				", amendment=" + amendment +
				", parentOrderNo='" + parentOrderNo + '\'' +
				", isOrderEnrichmentAttributesProvided='" + isOrderEnrichmentAttributesProvided + '\'' +
				", demoOrder=" + demoOrder +
				", demoType='" + demoType + '\'' +
				", billingType='" + billingType + '\'' +
				'}';
	}
}
