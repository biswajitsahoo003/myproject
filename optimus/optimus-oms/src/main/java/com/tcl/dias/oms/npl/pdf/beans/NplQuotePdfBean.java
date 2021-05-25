package com.tcl.dias.oms.npl.pdf.beans;

import java.util.List;

import com.tcl.dias.oms.beans.EnrichmentDetailsBean;
import com.tcl.dias.oms.pdf.beans.IllSolution;

/**
 * This file contains the IllQuotePdfBean.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class NplQuotePdfBean {
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
	private Double totalARC;
	private Double totalNRC;
	private Double totalTCV;
	private List<NplCommercial> commercials;
	private List<NplSolution> solutions;
	private List<IllSolution> siteSolutions;
	private String presentDate;
	private String publicIp;
	private Boolean isNat = true;
	private Boolean isApproved = false;
	private Boolean isMSA = false;
	private Boolean isSS = false;
	private Boolean isDocusign = false;
	private String ikey;

	// MACD Change ----->
	private String sfdcOpportunityId;
	private String serviceId;
	private String quoteCategory;
	private String quoteType;
	private String accessType;
	private String accessProvider;
	private String linkType;
	private String serviceCombinationType;
	
	private Boolean isMultiCircuit = false;

	// Attributes added to hold currency specific formatted values
	// of ARC,NRC,TCV
	private String totalARCFormatted;
	private String totalNRCFormatted;
	private String totalTCVFormatted;

	// --------PT-189--------//
	private String poNumber;
	private String poDate;

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
	private String offeringType;

	private String demarcationApartment;
	private String demarcationFloor;
	private String demarcationRack;
	private String demarcationRoom;
	private String isOrderEnrichmentAttributesProvided;
	private String baseUrl;
	private EnrichmentDetailsBean enrichmentDetailsBean;

	// For partner
	private Boolean isPartner=false;
    private Boolean isPartnerSellWith=false;
    private Boolean isPartnerSellThrough=false;
    
    
    private String tnc;
	private Boolean isTnc = false;
	

	private String commercialChanges;
	//PIPF-212
	private String ownerEmailSfdc;
	private String ownerNameSfdc;
	private String effectiveDate;

	
	//sitelevelbilling annexure;
	private NplMultiSiteAnnexure multiSiteAnnexure;
	private Boolean isMultiSiteAnnexure = false;
	
	
	public Boolean getIsMultiSiteAnnexure() {
		return isMultiSiteAnnexure;
	}

	public void setIsMultiSiteAnnexure(Boolean isMultiSiteAnnexure) {
		this.isMultiSiteAnnexure = isMultiSiteAnnexure;
	}
	
	public NplMultiSiteAnnexure getMultiSiteAnnexure() {
		return multiSiteAnnexure;
	}

	public void setMultiSiteAnnexure(NplMultiSiteAnnexure multiSiteAnnexure) {
		this.multiSiteAnnexure = multiSiteAnnexure;
	}

	private String crossConnect;

	private Boolean demarcTableNeeded;

	private String siteBoldMmr;
	private Boolean siteFlag;


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

	public Boolean getIsMultiCircuit() {
		return isMultiCircuit;
	}

	public void setIsMultiCircuit(Boolean isMultiCircuit) {
		this.isMultiCircuit = isMultiCircuit;
	}

	public String getOfferingType() {
		return offeringType;
	}

	public void setOfferingType(String offeringType) {
		this.offeringType = offeringType;
	}

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	private String offeringName;

	public String getTotalARCFormatted() {
		return totalARCFormatted;
	}

	public void setTotalARCFormatted(String totalARCFormatted) {
		this.totalARCFormatted = totalARCFormatted;
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

	public List<NplCommercial> getCommercials() {
		return commercials;
	}

	public void setCommercials(List<NplCommercial> commercials) {
		this.commercials = commercials;
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

	public List<NplSolution> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<NplSolution> solutions) {
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

	public Double getTotalARC() {
		return totalARC;
	}

	public void setTotalARC(Double totalARC) {
		this.totalARC = totalARC;
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

	public Boolean getIsDocusign() {
		return isDocusign;
	}

	public void setIsDocusign(Boolean isDocusign) {
		this.isDocusign = isDocusign;
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

	public List<IllSolution> getSiteSolutions() {
		return siteSolutions;
	}

	public void setSiteSolutions(List<IllSolution> siteSolutions) {
		this.siteSolutions = siteSolutions;
	}

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

	public String getServiceCombinationType() {
		return serviceCombinationType;
	}

	public void setServiceCombinationType(String serviceCombinationType) {
		this.serviceCombinationType = serviceCombinationType;
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

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getIkey() {
		return ikey;
	}

	public void setIkey(String ikey) {
		this.ikey = ikey;
	}

	public EnrichmentDetailsBean getEnrichmentDetailsBean() {
		return enrichmentDetailsBean;
	}

	public void setEnrichmentDetailsBean(EnrichmentDetailsBean enrichmentDetailsBean) {
		this.enrichmentDetailsBean = enrichmentDetailsBean;
	}

	public Boolean getIsPartner() {
		return isPartner;
	}

	public void setIsPartner(Boolean partner) {
		isPartner = partner;
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
    public String getCommercialChanges() {
		return commercialChanges;
	}

	public void setCommercialChanges(String commercialChanges) {
		this.commercialChanges = commercialChanges;
	}
		
	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getCrossConnect() {
		return crossConnect;
	}

	public void setCrossConnect(String crossConnect) {
		this.crossConnect = crossConnect;
	}

	public Boolean getDemarcTableNeeded() {
		return demarcTableNeeded;
	}

	public void setDemarcTableNeeded(Boolean demarcTableNeeded) {
		this.demarcTableNeeded = demarcTableNeeded;
	}

	public String getSiteBoldMmr() {
		return siteBoldMmr;
	}

	public void setSiteBoldMmr(String siteBoldMmr) {
		this.siteBoldMmr = siteBoldMmr;
	}

	public Boolean getSiteFlag() {
		return siteFlag;
	}

	public void setSiteFlag(Boolean siteFlag) {
		this.siteFlag = siteFlag;
	}

	@Override
	public String toString() {
		return "NplQuotePdfBean{" +
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
				", totalARC=" + totalARC +
				", totalNRC=" + totalNRC +
				", totalTCV=" + totalTCV +
				", commercials=" + commercials +
				", solutions=" + solutions +
				", siteSolutions=" + siteSolutions +
				", presentDate='" + presentDate + '\'' +
				", publicIp='" + publicIp + '\'' +
				", isNat=" + isNat +
				", isApproved=" + isApproved +
				", isMSA=" + isMSA +
				", isSS=" + isSS +
				", isDocusign=" + isDocusign +
				", ikey='" + ikey + '\'' +
				", sfdcOpportunityId='" + sfdcOpportunityId + '\'' +
				", serviceId='" + serviceId + '\'' +
				", quoteCategory='" + quoteCategory + '\'' +
				", quoteType='" + quoteType + '\'' +
				", accessType='" + accessType + '\'' +
				", accessProvider='" + accessProvider + '\'' +
				", linkType='" + linkType + '\'' +
				", serviceCombinationType='" + serviceCombinationType + '\'' +
				", isMultiCircuit=" + isMultiCircuit +
				", totalARCFormatted='" + totalARCFormatted + '\'' +
				", totalNRCFormatted='" + totalNRCFormatted + '\'' +
				", totalTCVFormatted='" + totalTCVFormatted + '\'' +
				", poNumber='" + poNumber + '\'' +
				", poDate='" + poDate + '\'' +
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
				", offeringType='" + offeringType + '\'' +
				", demarcationApartment='" + demarcationApartment + '\'' +
				", demarcationFloor='" + demarcationFloor + '\'' +
				", demarcationRack='" + demarcationRack + '\'' +
				", demarcationRoom='" + demarcationRoom + '\'' +
				", isOrderEnrichmentAttributesProvided='" + isOrderEnrichmentAttributesProvided + '\'' +
				", baseUrl='" + baseUrl + '\'' +
				", enrichmentDetailsBean=" + enrichmentDetailsBean +
				", isPartner=" + isPartner +
				", isPartnerSellWith=" + isPartnerSellWith +
				", isPartnerSellThrough=" + isPartnerSellThrough +
				", tnc='" + tnc + '\'' +
				", isTnc=" + isTnc +
				", commercialChanges='" + commercialChanges + '\'' +
				", ownerEmailSfdc='" + ownerEmailSfdc + '\'' +
				", ownerNameSfdc='" + ownerNameSfdc + '\'' +
				", effectiveDate='" + effectiveDate + '\'' +
				", crossConnect='" + crossConnect + '\'' +
				", demarcTableNeeded=" + demarcTableNeeded +
				", siteBoldMmr='" + siteBoldMmr + '\'' +
				", siteFlag=" + siteFlag +
				", offeringName='" + offeringName + '\'' +
				'}';
	}
}
