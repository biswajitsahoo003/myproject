package com.tcl.dias.oms.izosdwan.pdf.beans;
import com.tcl.dias.common.beans.CpeDetails;
import com.tcl.dias.common.beans.VProxyAddonsBean;
import com.tcl.dias.oms.beans.ProductSolutionBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.izosdwan.beans.ChargeableLineItemSummaryBean;
import com.tcl.dias.oms.izosdwan.beans.IzosdwanPdfSiteBean;
import com.tcl.dias.oms.izosdwan.beans.VproxySolutionLevelCharges;

import java.math.BigDecimal;
import java.util.List;

public class IzosdwanCofPdfBean {

    private Boolean isApproved;
    private Boolean isDocusign = false;

    private String baseUrl;
    private String orderType;
    private String orderRef;
    private String orderDate;

    private String supplierAddress;
    private String supplierGstnNumber;

    private String customerGstNumber;
    private String customerContractingEntity;
    private String customerContactName;
    private String billingPaymentsName;
    private String customerContactNumber;
    private String billingContactNumber;
    private String customerEmailId;
    private String billingEmailId;
    private String supplierAccountManager;
    private String supplierContactNumber;
    private String supplierEmailId;
    private String billingMethod;
    private String billingFreq;
    private String customerVatNumber;
	private String supplierVatNumber;
    
	private String billingCurrency;
    private String paymentCurrency;
    private String paymentTerm;
    private String supplierContactEntity;
    private String invoiceMethod;

    private String billingAddress;
    private String customerAddress;
    private String customerState;
    private String customerPincode;
    private String customerCity;
    private String customerCountry;
    private String productName;

    private Boolean isMSA = false;
    private Boolean isSS = false;
    private Boolean isSSStandard;
    private Boolean isSSPremium;
    private Boolean isArc = true;
    private String ikey;
    private Boolean isPureByon=false;
    private Boolean isGst = false;
	private Boolean isVat = true;

	private Boolean isGstSup = false;
	private Boolean isVatSup = true;
	
	private Integer isVutm;
	private String isVNF;
	
	/**
	* @return the isVutm
	*/

	public Integer getIsVutm() {
		return isVutm;
	}
	/**
	* @param isVutm the isVutm to set
	*/

	public void setIsVutm(Integer isVutm) {
		this.isVutm = isVutm;
	}

    public Boolean getIsPureByon() {
		return isPureByon;
	}

	public void setIsPureByon(Boolean isPureByon) {
		this.isPureByon = isPureByon;
	}

	public String getIkey() {
		return ikey;
	}

	public void setIkey(String ikey) {
		this.ikey = ikey;
	}

	private String poNumber;
    private String poDate;
    private String contractTerm;

    private String creditLimit;
    private String securityDepositAmount;
    

	private List<IzosdwanCofSiteBean> izosdwanCofSiteBeans;
    private List<ChargeableLineItemSummaryBean> chargeableLineItemSummaryBeans;
    private List<CommercialAttributesVproxy> commonComponentsVproxy;
    private List<VproxyCommercialDetailsBean> commercialDetailsVproxySolutions;

    public List<CommercialAttributesVproxy> getCommonComponentsVproxy() {
		return commonComponentsVproxy;
	}

	public void setCommonComponentsVproxy(List<CommercialAttributesVproxy> commonComponentsVproxy) {
		this.commonComponentsVproxy = commonComponentsVproxy;
	}

	public List<VproxyCommercialDetailsBean> getCommercialDetailsVproxySolutions() {
		return commercialDetailsVproxySolutions;
	}

	public void setCommercialDetailsVproxySolutions(List<VproxyCommercialDetailsBean> commercialDetailsVproxySolutions) {
		this.commercialDetailsVproxySolutions = commercialDetailsVproxySolutions;
	}

	private String arcTcv;
    private String nrcTcv;
    private String mrcTcv;
    private String tcv;

    private String tnc;
    private Boolean isTnc = false;
    private String primaryLocation;
    private String secondaryLocation;
    private String cgwGatewayBW;
    private String cgwServiceBW;
    private String migrationBandwidth;
    private Boolean isInternational;
    private String variant;
    private String pckg;
    
    private String addons;
	 private String effectiveArc;
	 private String effectiveNrc;
    private Integer noOfSites;
    private CpeDetails cpeDetails;
    private String optyId;
    private String billCurrencyArc;
    private String billCurrencyNrc;
    private String billCurrencyMrc;
   
    private String totalBillCurArc;
    private String totalBillCurNrc;
    private String totalBillCurMrc;
    private String totalBillCurRecurring;
    private String totalBillCurrCharges;
    private String totalBillCurOneTime;
    
   //os-68 docusign
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
	
	private Boolean isNat = true;
	
	//vproxy swg
	private String vProfile;
	private List<VProxyAddonsBean> vAddons;
	private String vProductOfferingName;
	private Boolean isVproxy=false;
	private Boolean isVproxyComm=false;
	private String swgTitle;
	private String totalnoofusers;
	private String totalnoofmiddleusers;
	private String totalnoofuserinothercountries;

	
	private Boolean isSwg=false;
	
	//vproxy spa
	private String svProfile;

	private List<VProxyAddonsBean> svAddons;
	private String svProductOfferingName;
	private String spaTitle;
	private String stotalnoofusers;
	private String stotalnoofmiddleusers;
	private Boolean isSpa=false;
	private String stotalnoofuserinothercountries;

	//terms and condition
	public Boolean isIndia=false;
	public Boolean isCustomerIndia=false;
	
	public Boolean getIsIndia() {
		return isIndia;
	}

	public void setIsIndia(Boolean isIndia) {
		this.isIndia = isIndia;
	}

	public String getTotalBillCurArc() {
		return totalBillCurArc;
	}

	public void setTotalBillCurArc(String totalBillCurArc) {
		this.totalBillCurArc = totalBillCurArc;
	}

	public String getTotalBillCurNrc() {
		return totalBillCurNrc;
	}

	public void setTotalBillCurNrc(String totalBillCurNrc) {
		this.totalBillCurNrc = totalBillCurNrc;
	}

	public String getTotalBillCurRecurring() {
		return totalBillCurRecurring;
	}

	public void setTotalBillCurRecurring(String totalBillCurRecurring) {
		this.totalBillCurRecurring = totalBillCurRecurring;
	}

	public String getTotalBillCurrCharges() {
		return totalBillCurrCharges;
	}

	public void setTotalBillCurrCharges(String totalBillCurrCharges) {
		this.totalBillCurrCharges = totalBillCurrCharges;
	}

	public String getTotalBillCurOneTime() {
		return totalBillCurOneTime;
	}

	public void setTotalBillCurOneTime(String totalBillCurOneTime) {
		this.totalBillCurOneTime = totalBillCurOneTime;
	}

	public String getBillCurrencyArc() {
		return billCurrencyArc;
	}

	public void setBillCurrencyArc(String billCurrencyArc) {
		this.billCurrencyArc = billCurrencyArc;
	}

	public String getBillCurrencyNrc() {
		return billCurrencyNrc;
	}

	public void setBillCurrencyNrc(String billCurrencyNrc) {
		this.billCurrencyNrc = billCurrencyNrc;
	}

	public String getAddons() {
		return addons;
	}

	public void setAddons(String addons) {
		this.addons = addons;
	}

	
	 
	 
	 
	 public String getEffectiveArc() {
		return effectiveArc;
	}

	public void setEffectiveArc(String effectiveArc) {
		this.effectiveArc = effectiveArc;
	}

	public String getEffectiveNrc() {
		return effectiveNrc;
	}

	public void setEffectiveNrc(String effectiveNrc) {
		this.effectiveNrc = effectiveNrc;
	}




    public String getPrimaryLocation() {
        return primaryLocation;
    }

    public void setPrimaryLocation(String primaryLocation) {
        this.primaryLocation = primaryLocation;
    }

    public String getSecondaryLocation() {
        return secondaryLocation;
    }

    public void setSecondaryLocation(String secondaryLocation) {
        this.secondaryLocation = secondaryLocation;
    }

    public String getCgwGatewayBW() {
        return cgwGatewayBW;
    }

    public void setCgwGatewayBW(String cgwGatewayBW) {
        this.cgwGatewayBW = cgwGatewayBW;
    }

    public String getCgwServiceBW() {
        return cgwServiceBW;
    }

    public void setCgwServiceBW(String cgwServiceBW) {
        this.cgwServiceBW = cgwServiceBW;
    }

    public String getMigrationBandwidth() {
        return migrationBandwidth;
    }

    public void setMigrationBandwidth(String migrationBandwidth) {
        this.migrationBandwidth = migrationBandwidth;
    }

    public Boolean getIsInternational() {
        return isInternational;
    }

    public void setIsInternational(Boolean isInternational) {
        this.isInternational = isInternational;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

   
    public String getPckg() {
		return pckg;
	}

	public void setPckg(String pckg) {
		this.pckg = pckg;
	}

	public Integer getNoOfSites() {
        return noOfSites;
    }

    public void setNoOfSites(Integer noOfSites) {
        this.noOfSites = noOfSites;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public Boolean getIsDocusign() {
        return isDocusign;
    }

    public void setIsDocusign(Boolean isDocusign) {
        this.isDocusign = isDocusign;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
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

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getSupplierGstnNumber() {
        return supplierGstnNumber;
    }

    public void setSupplierGstnNumber(String supplierGstnNumber) {
        this.supplierGstnNumber = supplierGstnNumber;
    }

    public String getCustomerGstNumber() {
        return customerGstNumber;
    }

    public void setCustomerGstNumber(String customerGstNumber) {
        this.customerGstNumber = customerGstNumber;
    }

    public String getCustomerContractingEntity() {
        return customerContractingEntity;
    }

    public void setCustomerContractingEntity(String customerContractingEntity) {
        this.customerContractingEntity = customerContractingEntity;
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

    public String getSupplierContactEntity() {
        return supplierContactEntity;
    }

    public void setSupplierContactEntity(String supplierContactEntity) {
        this.supplierContactEntity = supplierContactEntity;
    }

    public String getInvoiceMethod() {
        return invoiceMethod;
    }

    public void setInvoiceMethod(String invoiceMethod) {
        this.invoiceMethod = invoiceMethod;
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

    public Boolean getIsMSA() {
        return isMSA;
    }

    public void setIsMSA(Boolean isMSA) {
        isMSA = isMSA;
    }

    public Boolean getIsSS() {
        return isSS;
    }

    public void setIsSS(Boolean isSS) {
        isSS = isSS;
    }

    public Boolean getIsSSStandard() {
        return isSSStandard;
    }

    public void setIsSSStandard(Boolean isSSStandard) {
        isSSStandard = isSSStandard;
    }

    public Boolean getIsSSPremium() {
        return isSSPremium;
    }

    public void setIsSSPremium(Boolean isSSPremium) {
        isSSPremium = isSSPremium;
    }

    public Boolean getIsArc() {
        return isArc;
    }

    public void setIsArc(Boolean isArc) {
        isArc = isArc;
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

    public String getContractTerm() {
        return contractTerm;
    }

    public void setContractTerm(String contractTerm) {
        this.contractTerm = contractTerm;
    }

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getSecurityDepositAmount() {
        return securityDepositAmount;
    }

    public void setSecurityDepositAmount(String securityDepositAmount) {
        this.securityDepositAmount = securityDepositAmount;
    }

    public List<ChargeableLineItemSummaryBean> getChargeableLineItemSummaryBeans() {
        return chargeableLineItemSummaryBeans;
    }

    public void setChargeableLineItemSummaryBeans(List<ChargeableLineItemSummaryBean> chargeableLineItemSummaryBeans) {
        this.chargeableLineItemSummaryBeans = chargeableLineItemSummaryBeans;
    }

    public String getArcTcv() {
        return arcTcv;
    }

    public void setArcTcv(String arcTcv) {
        this.arcTcv = arcTcv;
    }

    public String getNrcTcv() {
        return nrcTcv;
    }

    public void setNrcTcv(String nrcTcv) {
        this.nrcTcv = nrcTcv;
    }

    public String getTcv() {
        return tcv;
    }

    public void setTcv(String tcv) {
        this.tcv = tcv;
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

    public List<IzosdwanCofSiteBean> getIzosdwanCofSiteBeans() {
        return izosdwanCofSiteBeans;
    }

    public void setIzosdwanCofSiteBeans(List<IzosdwanCofSiteBean> izosdwanCofSiteBeans) {
        this.izosdwanCofSiteBeans = izosdwanCofSiteBeans;
    }

    public CpeDetails getCpeDetails() {
        return cpeDetails;
    }

    public void setCpeDetails(CpeDetails cpeDetails) {
        this.cpeDetails = cpeDetails;
    }

    public String getOptyId() {
        return optyId;
    }

    public void setOptyId(String optyId) {
        this.optyId = optyId;
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

	public Boolean getIsNat() {
		return isNat;
	}

	public void setIsNat(Boolean isNat) {
		this.isNat = isNat;
	}

	public String getvProfile() {
		return vProfile;
	}

	public void setvProfile(String vProfile) {
		this.vProfile = vProfile;
	}

	
	public String getvProductOfferingName() {
		return vProductOfferingName;
	}

	public void setvProductOfferingName(String vProductOfferingName) {
		this.vProductOfferingName = vProductOfferingName;
	}

	public Boolean getIsVproxy() {
		return isVproxy;
	}

	public Boolean getIsVproxyComm() {
		return isVproxyComm;
	}

	public void setIsVproxyComm(Boolean isVproxyComm) {
		this.isVproxyComm = isVproxyComm;
	}

	public void setIsVproxy(Boolean isVproxy) {
		this.isVproxy = isVproxy;
	}

	public String getSwgTitle() {
		return swgTitle;
	}

	public void setSwgTitle(String swgTitle) {
		this.swgTitle = swgTitle;
	}

	public String getTotalnoofusers() {
		return totalnoofusers;
	}

	public void setTotalnoofusers(String totalnoofusers) {
		this.totalnoofusers = totalnoofusers;
	}

	public String getTotalnoofmiddleusers() {
		return totalnoofmiddleusers;
	}

	public void setTotalnoofmiddleusers(String totalnoofmiddleusers) {
		this.totalnoofmiddleusers = totalnoofmiddleusers;
	}

	public String getSvProfile() {
		return svProfile;
	}

	public void setSvProfile(String svProfile) {
		this.svProfile = svProfile;
	}

	
	
	public String getSvProductOfferingName() {
		return svProductOfferingName;
	}

	public void setSvProductOfferingName(String svProductOfferingName) {
		this.svProductOfferingName = svProductOfferingName;
	}

	public String getSpaTitle() {
		return spaTitle;
	}

	public void setSpaTitle(String spaTitle) {
		this.spaTitle = spaTitle;
	}

	public String getStotalnoofusers() {
		return stotalnoofusers;
	}

	public void setStotalnoofusers(String stotalnoofusers) {
		this.stotalnoofusers = stotalnoofusers;
	}

	public String getStotalnoofmiddleusers() {
		return stotalnoofmiddleusers;
	}

	public void setStotalnoofmiddleusers(String stotalnoofmiddleusers) {
		this.stotalnoofmiddleusers = stotalnoofmiddleusers;
	}

	public List<VProxyAddonsBean> getvAddons() {
		return vAddons;
	}

	public void setvAddons(List<VProxyAddonsBean> vAddons) {
		this.vAddons = vAddons;
	}

	public List<VProxyAddonsBean> getSvAddons() {
		return svAddons;
	}

	public void setSvAddons(List<VProxyAddonsBean> svAddons) {
		this.svAddons = svAddons;
	}

	public Boolean getIsSwg() {
		return isSwg;
	}

	public void setIsSwg(Boolean isSwg) {
		this.isSwg = isSwg;
	}

	public Boolean getIsSpa() {
		return isSpa;
	}

	public void setIsSpa(Boolean isSpa) {
		this.isSpa = isSpa;
	}

	public String getTotalnoofuserinothercountries() {
		return totalnoofuserinothercountries;
	}

	public void setTotalnoofuserinothercountries(String totalnoofuserinothercountries) {
		this.totalnoofuserinothercountries = totalnoofuserinothercountries;
	}

	public String getStotalnoofuserinothercountries() {
		return stotalnoofuserinothercountries;
	}

	public void setStotalnoofuserinothercountries(String stotalnoofuserinothercountries) {
		this.stotalnoofuserinothercountries = stotalnoofuserinothercountries;
	}

	public Boolean getIsCustomerIndia() {
		return isCustomerIndia;
	}

	public void setIsCustomerIndia(Boolean isCustomerIndia) {
		this.isCustomerIndia = isCustomerIndia;
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

    public String getIsVNF() {
        return isVNF;
    }
    
    public void setIsVNF(String isVNF) {
        this.isVNF = isVNF;
    }

    public String getBillCurrencyMrc() {
        return billCurrencyMrc;
    }

    public void setBillCurrencyMrc(String billCurrencyMrc) {
        this.billCurrencyMrc = billCurrencyMrc;
    }

    public String getTotalBillCurMrc() {
        return totalBillCurMrc;
    }

    public void setTotalBillCurMrc(String totalBillCurMrc) {
        this.totalBillCurMrc = totalBillCurMrc;
    }

    public String getMrcTcv() {
        return mrcTcv;
    }

    public void setMrcTcv(String mrcTcv) {
        this.mrcTcv = mrcTcv;
    }
}
