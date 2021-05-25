package com.tcl.dias.oms.ipc.beans;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class QuotePdfBean {

	private String orderRef;
	private String orderDate;
	private String orderType;
	private String baseUrl;
	private String productName;
	private String contractTerm;
	private String presentDate;
	private Double totalMRC;
	private Double totalNRC;
	private Double totalTCV;
	private Double totalARC;

	private Boolean isNat = true;
	private Boolean isApproved = false;
	private Boolean isMSA = false;
	private Boolean isArc = true;
	private Boolean isSS = false;
	private Boolean isDocusign = false;
	private Boolean hasPayPerUseVM = false;
	private Boolean isDataTransferSelected = false;
	private Boolean isVMsManaged = false;
	private String tnc;
	private Boolean isTnc = false;
	private Boolean isInternational = false;
	
	private Boolean isPartner=false;
	private Boolean isPartnerSellWith=false;
	
	private String paymentCurrency;
	private String paymentTerm;
	private String invoiceMethod;

	private String customerContractingEntity;
	private String customerGstNumber;
	private String customerVatNumber;
	private String customerContactName;
	private String customerContactNumber;
	private String customerEmailId;
	private String customerAddress;
	private String customerState;
	private String customerPincode;
	private String customerCity;
	private String customerCountry;

	private String billingPaymentsName;
	private String billingContactNumber;
	private String billingEmailId;
	private String billingMethod;
	private String billingFreq;
	private String billingCurrency;
	private String billingAddress;

	private String supplierContactEntity;
	private String supplierAddress;
	private String supplierAccountManager;
	private String supplierContactNumber;
	private String supplierEmailId;
	private String supplierGstnNumber;
	private String supplierVatNumber;

	private Boolean isGst = false;
	private Boolean isVat = true;

	private Boolean isGstSup = false;
	private Boolean isVatSup = true;

	private Double withHoldingTax = 0D;
	private String poNumber;
	private String poDate;
	
	private Set<SolutionPdfBean> solutions = new LinkedHashSet<>();

	private String dcCloudType;
	private String hostingLocation;
	private String hostingCountry;
	private Map<String, String> dataTransferPricingMap;
	
	//MACD change
	private String sfdcOpportunityId;
	private String serviceId;
	private String quoteCategory;
	private String oldServiceId;
	
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
	
	private String vmDeletionDate;
	private Set<DeletedVmsBean> deletedVms = new LinkedHashSet<>();
	
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

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getContractTerm() {
		return contractTerm;
	}

	public void setContractTerm(String contractTerm) {
		this.contractTerm = contractTerm;
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

	public Boolean getHasPayPerUseVM() {
		return hasPayPerUseVM;
	}

	public void setHasPayPerUseVM(Boolean hasPayPerUseVM) {
		this.hasPayPerUseVM = hasPayPerUseVM;
	}

	public Boolean getIsDataTransferSelected() {
		return isDataTransferSelected;
	}

	public void setIsDataTransferSelected(Boolean isDataTransferSelected) {
		this.isDataTransferSelected = isDataTransferSelected;
	}

	public Boolean getIsVMsManaged() {
		return isVMsManaged;
	}

	public void setIsVMsManaged(Boolean isVMsManaged) {
		this.isVMsManaged = isVMsManaged;
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

	public Boolean getIsInternational() {
		return isInternational;
	}

	public void setIsInternational(Boolean isInternational) {
		this.isInternational = isInternational;
	}

	public Boolean getIsArc() {
		return isArc;
	}

	public void setIsArc(Boolean isArc) {
		this.isArc = isArc;
	}

	public Boolean getIsSS() {
		return isSS;
	}

	public void setIsSS(Boolean isSS) {
		this.isSS = isSS;
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

	public String getCustomerContactNumber() {
		return customerContactNumber;
	}

	public void setCustomerContactNumber(String customerContactNumber) {
		this.customerContactNumber = customerContactNumber;
	}

	public String getCustomerEmailId() {
		return customerEmailId;
	}

	public void setCustomerEmailId(String customerEmailId) {
		this.customerEmailId = customerEmailId;
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

	public String getBillingPaymentsName() {
		return billingPaymentsName;
	}

	public void setBillingPaymentsName(String billingPaymentsName) {
		this.billingPaymentsName = billingPaymentsName;
	}

	public String getBillingContactNumber() {
		return billingContactNumber;
	}

	public void setBillingContactNumber(String billingContactNumber) {
		this.billingContactNumber = billingContactNumber;
	}

	public String getBillingEmailId() {
		return billingEmailId;
	}

	public void setBillingEmailId(String billingEmailId) {
		this.billingEmailId = billingEmailId;
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

	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
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

	public String getSupplierVatNumber() {
		return supplierVatNumber;
	}

	public void setSupplierVatNumber(String supplierVatNumber) {
		this.supplierVatNumber = supplierVatNumber;
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

	public Set<SolutionPdfBean> getSolutions() {
		return solutions;
	}

	public void setSolutions(Set<SolutionPdfBean> solutions) {
		this.solutions = solutions;
	}

	public Boolean getIsDocusign() {
		return isDocusign;
	}

	public void setIsDocusign(Boolean isDocusign) {
		this.isDocusign = isDocusign;
	}

	public String getDcCloudType() {
		return dcCloudType;
	}

	public void setDcCloudType(String dcCloudType) {
		this.dcCloudType = dcCloudType;
	}

	public String getHostingLocation() {
		return hostingLocation;
	}

	public void setHostingLocation(String hostingLocation) {
		this.hostingLocation = hostingLocation;
	}

	public String getHostingCountry() {
		return hostingCountry;
	}

	public void setHostingCountry(String hostingCountry) {
		this.hostingCountry = hostingCountry;
	}

	public Map<String, String> getDataTransferPricingMap() {
		return dataTransferPricingMap;
	}

	public void setDataTransferPricingMap(Map<String, String> dataTransferPricingMap) {
		this.dataTransferPricingMap = dataTransferPricingMap;
	}

	public Double getWithHoldingTax() {
		return withHoldingTax;
	}

	public void setWithHoldingTax(Double withHoldingTax) {
		this.withHoldingTax = withHoldingTax;
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

	public String getPresentDate() {
		return presentDate;
	}

	public void setPresentDate(String presentDate) {
		this.presentDate = presentDate;
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

	public String getOldServiceId() {
		return oldServiceId;
	}

	public void setOldServiceId(String oldServiceId) {
		this.oldServiceId = oldServiceId;
	}

	public Boolean getIsPartner() {
		return isPartner;
	}

	public void setIsPartner(Boolean isPartner) {
		this.isPartner = isPartner;
	}

	public Boolean getIsPartnerSellWith() {
		return isPartnerSellWith;
	}

	public void setIsPartnerSellWith(Boolean isPartnerSellWith) {
		this.isPartnerSellWith = isPartnerSellWith;
	}

	public String getVmDeletionDate() {
		return vmDeletionDate;
	}

	public void setVmDeletionDate(String vmDeletionDate) {
		this.vmDeletionDate = vmDeletionDate;
	}

	public Set<DeletedVmsBean> getDeletedVms() {
		return deletedVms;
	}

	public void setDeletedVms(Set<DeletedVmsBean> deletedVms) {
		this.deletedVms = deletedVms;
	}
}
