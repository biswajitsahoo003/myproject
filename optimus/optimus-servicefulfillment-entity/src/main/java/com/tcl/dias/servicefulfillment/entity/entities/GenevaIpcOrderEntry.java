package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the geneva_ipc_order_entry database table.
 * 
 */
@Entity
@Table(name="geneva_ipc_order_entry")
@NamedQuery(name="GenevaIpcOrderEntry.findAll", query="SELECT g FROM GenevaIpcOrderEntry g")
public class GenevaIpcOrderEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="acc_addr1")
	private String accAddr1;

	@Column(name="acc_addr2")
	private String accAddr2;

	@Column(name="acc_addr3")
	private String accAddr3;

	@Column(name="acc_attributes")
	private String accAttributes;

	@Column(name="acc_city")
	private String accCity;

	@Column(name="acc_country")
	private String accCountry;

	@Column(name="acc_state")
	private String accState;

	@Column(name="acc_zipcode")
	private String accZipcode;

	@Column(name="account_name")
	private String accountName;

	@Column(name="account_num")
	private String accountNum;

	@Column(name="accounting_currency")
	private String accountingCurrency;

	@Column(name="action_type")
	private String actionType;

	@Column(name="advance_boo")
	private String advanceBoo;

	@Column(name="attribute_string")
	private String attributeString;

	@Column(name="bill_handlingCode") 
	private String billHandlingCode;

	private String billActivation_date;

	private String billGeneration_date;

	@Column(name="billing_entity")
	private String billingEntity;

	@Column(name="billing_period")
	private String billingPeriod;

	@Column(name="business_unit")
	private String businessUnit;

	private String change_orderType;

	@Column(name="charge_period")
	private String chargePeriod;

	@Column(name="circuit_count")
	private String circuitCount;

	@Column(name="commission_date")
	private String commissionDate;

	@Column(name="component_name")
	private String componentName;

	@Column(name="contact_email")
	private String email;

	@Column(name="contact_name")
	private String contactName;

	@Column(name="contact_number")
	private String dayTelephone;

	@Column(name="contract_duration")
	private Integer contractDuration;

	@Column(name="contract_gstin_address")
	private String contractGstinAddress;

	@Column(name="contract_gstin_no")
	private String contractGstinNo;

	@Column(name="contracting_address")
	private String contractingAddress;

	@Column(name="copf_id")
	private String copfId;

	@Column(name="cps_name")
	private String cpsName;

	@Column(name="created_date")
	private String createdDate;

	@Column(name="creation_date")
	private String creationDate;

	@Column(name="creation_date_prd")
	private String creationDatePrd;

	@Column(name="credit_class")
	private String creditClass;

	@Column(name="currency_code")
	private String currencyCode;

	@Column(name="currency_code_prd")
	private String currencyCodePrd;

	@Column(name="customer_name")
	private String customerName;

	private String customer_orderNum;

	@Column(name="customer_ref")
	private String customerRef;

	@Column(name="customer_type")
	private String customerType;

	@Column(name="deposit_refund_boo")
	private String depositRefundBoo;

	@Column(name="equipement_type")
	private String equipementType;

	@Column(name="eventsource_label")
	private String eventsourceLabel;

	@Column(name="eventsource_string")
	private String eventsourceString;

	@Column(name="eventsource_txt")
	private String eventsourceTxt;

	@Column(name="first_name")
	private String firstName;

	@Column(name="group_id")
	private String groupId;

	@Column(name="info_currency")
	private String infoCurrency;

	private String invoicing_coName;

	private String invoicing_coName_prd;

	@Column(name="last_name")
	private String lastName;

	@Column(name="order_type")
	private String orderType;

	private String ovrdn_initPrice;

	private String ovrdn_periodicPrice;

	private String payment_dueDate;

	@Column(name="prod_addr1")
	private String prodAddr1;

	@Column(name="prod_addr2")
	private String prodAddr2;

	@Column(name="prod_addr3")
	private String prodAddr3;

	@Column(name="prod_city")
	private String prodCity;

	@Column(name="prod_country")
	private String prodCountry;

	@Column(name="prod_quantity")
	private Integer prodQuantity;

	@Column(name="prod_state")
	private String prodState;

	@Column(name="prod_zipcode")
	private String prodZipcode;

	@Column(name="product_name")
	private String productName;

	@Column(name="profile_id")
	private String profileId;

	@Column(name="prorate_boo")
	private String prorateBoo;

	@Column(name="provider_segment")
	private String providerSegment;

	@Column(name="rate_override_boo")
	private String rateOverrideBoo;

	@Column(name="refund_boo")
	private String refundBoo;

	@Column(name="request_type")
	private String requestType;

	@Column(name="scenario_type")
	private String scenarioType;

	@Column(name="secs_id")
	private String secsId;

	@Column(name="service_id")
	private String serviceId;

	@Column(name="service_type")
	private String serviceType;

	@Column(name="site_a_address")
	private String siteAAddress;

	@Column(name="site_b_address")
	private String siteBAddress;

	@Column(name="site_end")
	private String siteEnd;

	@Column(name="site_gstin_address")
	private String siteGstinAddress;

	@Column(name="site_gstin_no")
	private String siteGstinNo;

	private String source_parentProductSeq;
	
	@Column(name="source_productSeq")
	private String sourceProductSeq;

	@Column(name="source_system")
	private String sourceSystem;

	private String status;

	@Column(name="status_msg")
	private String statusMsg;

	private String taxExempt_ref;

	private String taxExempt_txt;

	private String title;

	@Column(name="total_arc")
	private String totalArc;

	@Column(name="total_nrc")
	private String totalNrc;

	@Column(name="updated_date")
	private String updatedDate;
	
	@Column(name="source_old_product_seq")
	private String sourceOldProdSeq;

	public GenevaIpcOrderEntry() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccAddr1() {
		return this.accAddr1;
	}

	public void setAccAddr1(String accAddr1) {
		this.accAddr1 = accAddr1;
	}

	public String getAccAddr2() {
		return this.accAddr2;
	}

	public void setAccAddr2(String accAddr2) {
		this.accAddr2 = accAddr2;
	}

	public String getAccAddr3() {
		return this.accAddr3;
	}

	public void setAccAddr3(String accAddr3) {
		this.accAddr3 = accAddr3;
	}

	public String getAccAttributes() {
		return this.accAttributes;
	}

	public void setAccAttributes(String accAttributes) {
		this.accAttributes = accAttributes;
	}

	public String getAccCity() {
		return this.accCity;
	}

	public void setAccCity(String accCity) {
		this.accCity = accCity;
	}

	public String getAccCountry() {
		return this.accCountry;
	}

	public void setAccCountry(String accCountry) {
		this.accCountry = accCountry;
	}

	public String getAccState() {
		return this.accState;
	}

	public void setAccState(String accState) {
		this.accState = accState;
	}

	public String getAccZipcode() {
		return this.accZipcode;
	}

	public void setAccZipcode(String accZipcode) {
		this.accZipcode = accZipcode;
	}

	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNum() {
		return this.accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getAccountingCurrency() {
		return this.accountingCurrency;
	}

	public void setAccountingCurrency(String accountingCurrency) {
		this.accountingCurrency = accountingCurrency;
	}

	public String getActionType() {
		return this.actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getAdvanceBoo() {
		return this.advanceBoo;
	}

	public void setAdvanceBoo(String advanceBoo) {
		this.advanceBoo = advanceBoo;
	}

	public String getAttributeString() {
		return this.attributeString;
	}

	public void setAttributeString(String attributeString) {
		this.attributeString = attributeString;
	}

	public String getBillHandlingCode() {
		return billHandlingCode;
	}

	public void setBillHandlingCode(String billHandlingCode) {
		this.billHandlingCode = billHandlingCode;
	}

	public String getBillActivation_date() {
		return this.billActivation_date;
	}

	public void setBillActivation_date(String billActivation_date) {
		this.billActivation_date = billActivation_date;
	}

	public String getBillGeneration_date() {
		return this.billGeneration_date;
	}

	public void setBillGeneration_date(String billGeneration_date) {
		this.billGeneration_date = billGeneration_date;
	}

	public String getBillingEntity() {
		return this.billingEntity;
	}

	public void setBillingEntity(String billingEntity) {
		this.billingEntity = billingEntity;
	}

	public String getBillingPeriod() {
		return this.billingPeriod;
	}

	public void setBillingPeriod(String billingPeriod) {
		this.billingPeriod = billingPeriod;
	}

	public String getBusinessUnit() {
		return this.businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public String getChange_orderType() {
		return this.change_orderType;
	}

	public void setChange_orderType(String change_orderType) {
		this.change_orderType = change_orderType;
	}

	public String getChargePeriod() {
		return this.chargePeriod;
	}

	public void setChargePeriod(String chargePeriod) {
		this.chargePeriod = chargePeriod;
	}

	public String getCircuitCount() {
		return this.circuitCount;
	}

	public void setCircuitCount(String circuitCount) {
		this.circuitCount = circuitCount;
	}

	public String getCommissionDate() {
		return this.commissionDate;
	}

	public void setCommissionDate(String commissionDate) {
		this.commissionDate = commissionDate;
	}

	public String getComponentName() {
		return this.componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactName() {
		return this.contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getDayTelephone() {
		return dayTelephone;
	}

	public void setDayTelephone(String dayTelephone) {
		this.dayTelephone = dayTelephone;
	}

	public Integer getContractDuration() {
		return this.contractDuration;
	}

	public void setContractDuration(Integer contractDuration) {
		this.contractDuration = contractDuration;
	}

	public String getContractGstinAddress() {
		return this.contractGstinAddress;
	}

	public void setContractGstinAddress(String contractGstinAddress) {
		this.contractGstinAddress = contractGstinAddress;
	}

	public String getContractGstinNo() {
		return this.contractGstinNo;
	}

	public void setContractGstinNo(String contractGstinNo) {
		this.contractGstinNo = contractGstinNo;
	}

	public String getContractingAddress() {
		return this.contractingAddress;
	}

	public void setContractingAddress(String contractingAddress) {
		this.contractingAddress = contractingAddress;
	}

	public String getCopfId() {
		return this.copfId;
	}

	public void setCopfId(String copfId) {
		this.copfId = copfId;
	}

	public String getCpsName() {
		return this.cpsName;
	}

	public void setCpsName(String cpsName) {
		this.cpsName = cpsName;
	}

	public String getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreationDatePrd() {
		return this.creationDatePrd;
	}

	public void setCreationDatePrd(String creationDatePrd) {
		this.creationDatePrd = creationDatePrd;
	}

	public String getCreditClass() {
		return this.creditClass;
	}

	public void setCreditClass(String creditClass) {
		this.creditClass = creditClass;
	}

	public String getCurrencyCode() {
		return this.currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencyCodePrd() {
		return this.currencyCodePrd;
	}

	public void setCurrencyCodePrd(String currencyCodePrd) {
		this.currencyCodePrd = currencyCodePrd;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomer_orderNum() {
		return this.customer_orderNum;
	}

	public void setCustomer_orderNum(String customer_orderNum) {
		this.customer_orderNum = customer_orderNum;
	}

	public String getCustomerRef() {
		return this.customerRef;
	}

	public void setCustomerRef(String customerRef) {
		this.customerRef = customerRef;
	}

	public String getCustomerType() {
		return this.customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getDepositRefundBoo() {
		return this.depositRefundBoo;
	}

	public void setDepositRefundBoo(String depositRefundBoo) {
		this.depositRefundBoo = depositRefundBoo;
	}

	public String getEquipementType() {
		return this.equipementType;
	}

	public void setEquipementType(String equipementType) {
		this.equipementType = equipementType;
	}

	public String getEventsourceLabel() {
		return this.eventsourceLabel;
	}

	public void setEventsourceLabel(String eventsourceLabel) {
		this.eventsourceLabel = eventsourceLabel;
	}

	public String getEventsourceString() {
		return this.eventsourceString;
	}

	public void setEventsourceString(String eventsourceString) {
		this.eventsourceString = eventsourceString;
	}

	public String getEventsourceTxt() {
		return this.eventsourceTxt;
	}

	public void setEventsourceTxt(String eventsourceTxt) {
		this.eventsourceTxt = eventsourceTxt;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getInfoCurrency() {
		return this.infoCurrency;
	}

	public void setInfoCurrency(String infoCurrency) {
		this.infoCurrency = infoCurrency;
	}

	public String getInvoicing_coName() {
		return this.invoicing_coName;
	}

	public void setInvoicing_coName(String invoicing_coName) {
		this.invoicing_coName = invoicing_coName;
	}

	public String getInvoicing_coName_prd() {
		return this.invoicing_coName_prd;
	}

	public void setInvoicing_coName_prd(String invoicing_coName_prd) {
		this.invoicing_coName_prd = invoicing_coName_prd;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getOrderType() {
		return this.orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOvrdn_initPrice() {
		return this.ovrdn_initPrice;
	}

	public void setOvrdn_initPrice(String ovrdn_initPrice) {
		this.ovrdn_initPrice = ovrdn_initPrice;
	}

	public String getOvrdn_periodicPrice() {
		return this.ovrdn_periodicPrice;
	}

	public void setOvrdn_periodicPrice(String ovrdn_periodicPrice) {
		this.ovrdn_periodicPrice = ovrdn_periodicPrice;
	}

	public String getPayment_dueDate() {
		return this.payment_dueDate;
	}

	public void setPayment_dueDate(String payment_dueDate) {
		this.payment_dueDate = payment_dueDate;
	}

	public String getProdAddr1() {
		return this.prodAddr1;
	}

	public void setProdAddr1(String prodAddr1) {
		this.prodAddr1 = prodAddr1;
	}

	public String getProdAddr2() {
		return this.prodAddr2;
	}

	public void setProdAddr2(String prodAddr2) {
		this.prodAddr2 = prodAddr2;
	}

	public String getProdAddr3() {
		return this.prodAddr3;
	}

	public void setProdAddr3(String prodAddr3) {
		this.prodAddr3 = prodAddr3;
	}

	public String getProdCity() {
		return this.prodCity;
	}

	public void setProdCity(String prodCity) {
		this.prodCity = prodCity;
	}

	public String getProdCountry() {
		return this.prodCountry;
	}

	public void setProdCountry(String prodCountry) {
		this.prodCountry = prodCountry;
	}

	public Integer getProdQuantity() {
		return this.prodQuantity;
	}

	public void setProdQuantity(Integer prodQuantity) {
		this.prodQuantity = prodQuantity;
	}

	public String getProdState() {
		return this.prodState;
	}

	public void setProdState(String prodState) {
		this.prodState = prodState;
	}

	public String getProdZipcode() {
		return this.prodZipcode;
	}

	public void setProdZipcode(String prodZipcode) {
		this.prodZipcode = prodZipcode;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProfileId() {
		return this.profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public String getProrateBoo() {
		return this.prorateBoo;
	}

	public void setProrateBoo(String prorateBoo) {
		this.prorateBoo = prorateBoo;
	}

	public String getProviderSegment() {
		return this.providerSegment;
	}

	public void setProviderSegment(String providerSegment) {
		this.providerSegment = providerSegment;
	}

	public String getRateOverrideBoo() {
		return this.rateOverrideBoo;
	}

	public void setRateOverrideBoo(String rateOverrideBoo) {
		this.rateOverrideBoo = rateOverrideBoo;
	}

	public String getRefundBoo() {
		return this.refundBoo;
	}

	public void setRefundBoo(String refundBoo) {
		this.refundBoo = refundBoo;
	}

	public String getRequestType() {
		return this.requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getScenarioType() {
		return this.scenarioType;
	}

	public void setScenarioType(String scenarioType) {
		this.scenarioType = scenarioType;
	}

	public String getSecsId() {
		return this.secsId;
	}

	public void setSecsId(String secsId) {
		this.secsId = secsId;
	}

	public String getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getSiteAAddress() {
		return this.siteAAddress;
	}

	public void setSiteAAddress(String siteAAddress) {
		this.siteAAddress = siteAAddress;
	}

	public String getSiteBAddress() {
		return this.siteBAddress;
	}

	public void setSiteBAddress(String siteBAddress) {
		this.siteBAddress = siteBAddress;
	}

	public String getSiteEnd() {
		return this.siteEnd;
	}

	public void setSiteEnd(String siteEnd) {
		this.siteEnd = siteEnd;
	}

	public String getSiteGstinAddress() {
		return this.siteGstinAddress;
	}

	public void setSiteGstinAddress(String siteGstinAddress) {
		this.siteGstinAddress = siteGstinAddress;
	}

	public String getSiteGstinNo() {
		return this.siteGstinNo;
	}

	public void setSiteGstinNo(String siteGstinNo) {
		this.siteGstinNo = siteGstinNo;
	}

	public String getSource_parentProductSeq() {
		return this.source_parentProductSeq;
	}

	public void setSource_parentProductSeq(String source_parentProductSeq) {
		this.source_parentProductSeq = source_parentProductSeq;
	}

	public String getSourceProductSeq() {
		return this.sourceProductSeq;
	}

	public void setSourceProductSeq(String sourceProductSeq) {
		this.sourceProductSeq = sourceProductSeq;
	}

	public String getSourceSystem() {
		return this.sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusMsg() {
		return this.statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}

	public String getTaxExempt_ref() {
		return this.taxExempt_ref;
	}

	public void setTaxExempt_ref(String taxExempt_ref) {
		this.taxExempt_ref = taxExempt_ref;
	}

	public String getTaxExempt_txt() {
		return this.taxExempt_txt;
	}

	public void setTaxExempt_txt(String taxExempt_txt) {
		this.taxExempt_txt = taxExempt_txt;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTotalArc() {
		return this.totalArc;
	}

	public void setTotalArc(String totalArc) {
		this.totalArc = totalArc;
	}

	public String getTotalNrc() {
		return this.totalNrc;
	}

	public void setTotalNrc(String totalNrc) {
		this.totalNrc = totalNrc;
	}

	public String getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getSourceOldProdSeq() {
		return sourceOldProdSeq;
	}

	public void setSourceOldProdSeq(String sourceOldProdSeq) {
		this.sourceOldProdSeq = sourceOldProdSeq;
	}
	

}