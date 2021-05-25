package com.tcl.servicehandover.entity;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the GNV_ORDER_ENTRY_TAB database table.
 * 
 */
@Entity
@Table(name="GNV_ORDER_ENTRY_TAB")
@NamedQuery(name="GnvOrderEntryTab.findAll", query="SELECT g FROM GnvOrderEntryTab g")
public class GnvOrderEntryTab implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	//@GeneratedValue(generator = "sequence", strategy=GenerationType.IDENTITY)
	@Column(name="INPUT_ROW_ID")
	private BigDecimal inputRowId;
	
//    @GenericGenerator(name = "sequence", strategy = "sequence", parameters = {
//            @org.hibernate.annotations.Parameter(name = "sequenceName", value = "sequence"),
//            @org.hibernate.annotations.Parameter(name = "allocationSize", value = "1"),
//            @org.hibernate.annotations.Parameter(name = "sequencePrefix", value = "ACC_")
//    })
    //@GeneratedValue(generator = "sequence", strategy=GenerationType.SEQUENCE)
	@Id
	@Column(name="INPUT_GROUP_ID")
	private String inputGroupId;

	@Column(name="ACTION_TYPE")
	private String actionType;

	@Column(name="ATTR_5")
	private String attr5;

	@Column(name="ATTR_6")
	private String attr6;

	@Column(name="ATTR_7")
	private String attr7;

	@Column(name="ATTR_8")
	private String attr8;

	@Column(name="ATTR_9")
	private String attr9;

	@Column(name="CHANGE_ORDER_TYPE")
	private String changeOrderType;

	@Column(name="COMPONENT_ID")
	private String componentId;

	@Column(name="DUMMY_ACCOUNT_NUM")
	private String dummyAccountNum;

	@Column(name="ERROR_MESSAGE")
	private String errorMessage;

	@Column(name="I_ACCADDR1")
	private String iAccaddr1;

	@Column(name="I_ACCADDR2")
	private String iAccaddr2;

	@Column(name="I_ACCADDR3")
	private String iAccaddr3;

	@Column(name="I_ACCADDR4")
	private String iAccaddr4;

	@Column(name="I_ACCADDR5")
	private String iAccaddr5;

	@Column(name="I_ACCCOUNTRY_NAME")
	private String iAcccountryName;

	@Column(name="I_ACCOUNT_ATTRIBUTES")
	private String iAccountAttributes;

	@Column(name="I_ACCOUNT_NAME")
	private String iAccountName;

	@Column(name="I_ACCOUNT_NUMBER")
	private String iAccountNumber;

	@Column(name="I_ACCOUNTING_CUR")
	private String iAccountingCur;

	@Column(name="I_ACCZIPCODE")
	private String iAcczipcode;

	@Column(name="I_ADD_EVENT_SRC")
	private String iAddEventSrc;

	@Column(name="I_ADVANCE_BOO")
	private String iAdvanceBoo;

	@Lob
	@Column(name="I_ATTRIBUTE_STRING")
	private String iAttributeString;

	@Column(name="I_BANDWIDTH_TYPE")
	private String iBandwidthType;

	@Column(name="I_BILL_ACTIVATION_DATE")
	private String iBillActivationDate;

	@Column(name="I_BILL_GENERATION_DATE")
	private String iBillGenerationDate;

	@Column(name="I_BILL_HANDLING_CODE")
	private String iBillHandlingCode;

	@Column(name="I_BILLING_PERIOD")
	private String iBillingPeriod;

	@Column(name="I_BILLINGSEQ")
	private String iBillingseq;

	@Column(name="I_BPMID")
	private BigDecimal iBpmid;

	@Column(name="I_BUSINESS_UNIT")
	private String iBusinessUnit;

	@Column(name="I_C_FORM")
	private String iCForm;

	@Column(name="I_CHARGE_PERIOD")
	private String iChargePeriod;

	@Column(name="I_CHARGINGRATE_VALUE")
	private String iChargingrateValue;

	@Column(name="I_COMMISSION_DATE")
	private String iCommissionDate;

	@Column(name="I_COMPONENT_NAME")
	private String iComponentName;

	@Column(name="I_CONTRACT_DURATION")
	private BigDecimal iContractDuration;

	@Column(name="I_CONTRACT_GSTIN_ADDRESS")
	private String iContractGstinAddress;

	@Column(name="I_CONTRACTING_ADDRRESS")
	private String iContractingAddrress;

	@Column(name="I_CONTRACTING_GSTIN_NO")
	private String iContractingGstinNo;

	@Column(name="I_COPF_ID")
	private String iCopfId;

	@Column(name="I_CREDIT_CLASS")
	private String iCreditClass;

	@Column(name="I_CURRENCY_CODE_PRD")
	private String iCurrencyCodePrd;

	@Column(name="I_CUSTOMER_REF")
	private String iCustomerRef;

	@Column(name="I_DAY_EXTENSION")
	private String iDayExtension;

	@Column(name="I_DAY_TELEPHONE")
	private String iDayTelephone;

	@Column(name="I_DELIVERY_ADDRESS")
	private String iDeliveryAddress;

	@Column(name="I_DEPARTMENT")
	private String iDepartment;

	@Column(name="I_DEPOSIT_REFUND_BOO")
	private String iDepositRefundBoo;

	@Column(name="I_EMAIL")
	private String iEmail;

	@Column(name="I_EQUIPMENT_TYPE")
	private String iEquipmentType;

	@Column(name="I_EVE_EXTENSION")
	private String iEveExtension;

	@Column(name="I_EVE_TELEPHONE")
	private String iEveTelephone;

	@Column(name="I_EVENT_SOURCE")
	private String iEventSource;

	@Column(name="I_EVENT_SOURCE_LABEL")
	private String iEventSourceLabel;

	@Column(name="I_EVENT_SOURCE_TEXT")
	private String iEventSourceText;

	@Column(name="I_FAX")
	private String iFax;

	@Column(name="I_FIRST_NAME")
	private String iFirstName;

	@Column(name="I_INFLIGHT")
	private String iInflight;

	@Column(name="I_INFO_CURRENCY")
	private String iInfoCurrency;

	@Column(name="I_INITIALS")
	private String iInitials;

	@Column(name="I_INVOICING_CO_NAME")
	private String iInvoicingCoName;

	@Column(name="I_LAST_NAME")
	private String iLastName;

	@Column(name="I_MIGRATION")
	private String iMigration;

	@Column(name="I_MOBILE")
	private String iMobile;

	@Column(name="I_NRC_CHARGES")
	private String iNrcCharges;

	@Column(name="I_PORTAL_ID")
	private String iPortalId;

	@Column(name="I_POS_OLD_PRODUCT_SEQ")
	private BigDecimal iPosOldProductSeq;

	@Column(name="I_POSITION")
	private String iPosition;

	@Column(name="I_PRODADDR1")
	private String iProdaddr1;

	@Column(name="I_PRODADDR2")
	private String iProdaddr2;

	@Column(name="I_PRODADDR3")
	private String iProdaddr3;

	@Column(name="I_PRODADDR4")
	private String iProdaddr4;

	@Column(name="I_PRODADDR5")
	private String iProdaddr5;

	@Column(name="I_PRODCOUNTRY_NAME")
	private String iProdcountryName;

	@Column(name="I_PRODUCT_NAME")
	private String iProductName;

	@Column(name="I_PRODUCT_QUANTITY")
	private BigDecimal iProductQuantity;

	@Column(name="I_PRODZIP_CODE")
	private String iProdzipCode;

	@Column(name="I_PRORATE_BOO")
	private String iProrateBoo;

	@Column(name="I_RATE_END_DTM")
	private Date iRateEndDtm;

	@Column(name="I_RC_CHARGES")
	private String iRcCharges;

	@Column(name="I_REFUND_BOO")
	private String iRefundBoo;

	@Column(name="I_RHS_HRS")
	private BigDecimal iRhsHrs;

	@Column(name="I_SCENARIO_TYPE")
	private String iScenarioType;

	@Column(name="I_SERVICE_ID")
	private String iServiceId;

	@Column(name="I_SERVICE_TYPE")
	private String iServiceType;

	@Column(name="I_SITE_A_ADDRESS")
	private String iSiteAAddress;

	@Column(name="I_SITE_B_ADDRESS")
	private String iSiteBAddress;

	@Column(name="I_SITE_END")
	private String iSiteEnd;

	@Column(name="I_SITEA_GSTIN_ADDRESS")
	private String iSiteaGstinAddress;

	@Column(name="I_SITEA_GSTIN_NO")
	private String iSiteaGstinNo;

	@Column(name="I_SITEB_GSTIN_ADDRESS")
	private String iSitebGstinAddress;

	@Column(name="I_SITEB_GSTIN_NO")
	private String iSitebGstinNo;

	@Column(name="I_SIZE")
	private BigDecimal iSize;

	@Column(name="I_SOURCE_PARENT_PRODUCT_SEQ")
	private BigDecimal iSourceParentProductSeq;

	@Column(name="I_SOURCE_PRODUCT_SEQ")
	private BigDecimal iSourceProductSeq;

	@Column(name="I_SYSTEM_INDICATOR")
	private String iSystemIndicator;

	@Column(name="I_TAX_EXEMPT_REF")
	private String iTaxExemptRef;

	@Column(name="I_TAX_EXEMPT_TXT")
	private String iTaxExemptTxt;

	@Column(name="I_TERM_CHARGES")
	private BigDecimal iTermCharges;

	@Column(name="I_TERM_ENDDATE")
	private String iTermEnddate;

	@Column(name="I_TERM_EVENT_SRC")
	private String iTermEventSrc;

	@Column(name="I_TERM_PROPOSED_DATE")
	private String iTermProposedDate;

	@Column(name="I_TERM_REASON_ID")
	private String iTermReasonId;

	@Column(name="I_TITLE")
	private String iTitle;

	@Column(name="I_USER_NAME")
	private String iUserName;

	@Column(name="I_VAT_IDENTIFIER")
	private String iVatIdentifier;

	@Column(name="I_WAREHOUSE_ADDRESS")
	private String iWarehouseAddress;

	@Column(name="ORDER_TYPE")
	private String orderType;

	@Column(name="REQUEST_TYPE")
	private String requestType;

	@Column(name="ROW_CREATED_DTM")
	private Date rowCreatedDtm;

	@Column(name="ROW_LAST_UPDATED_DTM")
	private Date rowLastUpdatedDtm;

	@Column(name="SOURCE_SYSTEM")
	private String sourceSystem;

	private String status;

	public GnvOrderEntryTab() {
	}

	public String getActionType() {
		return this.actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getAttr5() {
		return this.attr5;
	}

	public void setAttr5(String attr5) {
		this.attr5 = attr5;
	}

	public String getAttr6() {
		return this.attr6;
	}

	public void setAttr6(String attr6) {
		this.attr6 = attr6;
	}

	public String getAttr7() {
		return this.attr7;
	}

	public void setAttr7(String attr7) {
		this.attr7 = attr7;
	}

	public String getAttr8() {
		return this.attr8;
	}

	public void setAttr8(String attr8) {
		this.attr8 = attr8;
	}

	public String getAttr9() {
		return this.attr9;
	}

	public void setAttr9(String attr9) {
		this.attr9 = attr9;
	}

	public String getChangeOrderType() {
		return this.changeOrderType;
	}

	public void setChangeOrderType(String changeOrderType) {
		this.changeOrderType = changeOrderType;
	}

	public String getComponentId() {
		return this.componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public String getDummyAccountNum() {
		return this.dummyAccountNum;
	}

	public void setDummyAccountNum(String dummyAccountNum) {
		this.dummyAccountNum = dummyAccountNum;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getIAccaddr1() {
		return this.iAccaddr1;
	}

	public void setIAccaddr1(String iAccaddr1) {
		this.iAccaddr1 = iAccaddr1;
	}

	public String getIAccaddr2() {
		return this.iAccaddr2;
	}

	public void setIAccaddr2(String iAccaddr2) {
		this.iAccaddr2 = iAccaddr2;
	}

	public String getIAccaddr3() {
		return this.iAccaddr3;
	}

	public void setIAccaddr3(String iAccaddr3) {
		this.iAccaddr3 = iAccaddr3;
	}

	public String getIAccaddr4() {
		return this.iAccaddr4;
	}

	public void setIAccaddr4(String iAccaddr4) {
		this.iAccaddr4 = iAccaddr4;
	}

	public String getIAccaddr5() {
		return this.iAccaddr5;
	}

	public void setIAccaddr5(String iAccaddr5) {
		this.iAccaddr5 = iAccaddr5;
	}

	public String getIAcccountryName() {
		return this.iAcccountryName;
	}

	public void setIAcccountryName(String iAcccountryName) {
		this.iAcccountryName = iAcccountryName;
	}

	public String getIAccountAttributes() {
		return this.iAccountAttributes;
	}

	public void setIAccountAttributes(String iAccountAttributes) {
		this.iAccountAttributes = iAccountAttributes;
	}

	public String getIAccountName() {
		return this.iAccountName;
	}

	public void setIAccountName(String iAccountName) {
		this.iAccountName = iAccountName;
	}

	public String getIAccountNumber() {
		return this.iAccountNumber;
	}

	public void setIAccountNumber(String iAccountNumber) {
		this.iAccountNumber = iAccountNumber;
	}

	public String getIAccountingCur() {
		return this.iAccountingCur;
	}

	public void setIAccountingCur(String iAccountingCur) {
		this.iAccountingCur = iAccountingCur;
	}

	public String getIAcczipcode() {
		return this.iAcczipcode;
	}

	public void setIAcczipcode(String iAcczipcode) {
		this.iAcczipcode = iAcczipcode;
	}

	public String getIAddEventSrc() {
		return this.iAddEventSrc;
	}

	public void setIAddEventSrc(String iAddEventSrc) {
		this.iAddEventSrc = iAddEventSrc;
	}

	public String getIAdvanceBoo() {
		return this.iAdvanceBoo;
	}

	public void setIAdvanceBoo(String iAdvanceBoo) {
		this.iAdvanceBoo = iAdvanceBoo;
	}

	public String getIAttributeString() {
		return this.iAttributeString;
	}

	public void setIAttributeString(String iAttributeString) {
		this.iAttributeString = iAttributeString;
	}

	public String getIBandwidthType() {
		return this.iBandwidthType;
	}

	public void setIBandwidthType(String iBandwidthType) {
		this.iBandwidthType = iBandwidthType;
	}

	public String getIBillActivationDate() {
		return this.iBillActivationDate;
	}

	public void setIBillActivationDate(String iBillActivationDate) {
		this.iBillActivationDate = iBillActivationDate;
	}

	public String getIBillGenerationDate() {
		return this.iBillGenerationDate;
	}

	public void setIBillGenerationDate(String iBillGenerationDate) {
		this.iBillGenerationDate = iBillGenerationDate;
	}

	public String getIBillHandlingCode() {
		return this.iBillHandlingCode;
	}

	public void setIBillHandlingCode(String iBillHandlingCode) {
		this.iBillHandlingCode = iBillHandlingCode;
	}

	public String getIBillingPeriod() {
		return this.iBillingPeriod;
	}

	public void setIBillingPeriod(String iBillingPeriod) {
		this.iBillingPeriod = iBillingPeriod;
	}

	public String getIBillingseq() {
		return this.iBillingseq;
	}

	public void setIBillingseq(String iBillingseq) {
		this.iBillingseq = iBillingseq;
	}

	public BigDecimal getIBpmid() {
		return this.iBpmid;
	}

	public void setIBpmid(BigDecimal iBpmid) {
		this.iBpmid = iBpmid;
	}

	public String getIBusinessUnit() {
		return this.iBusinessUnit;
	}

	public void setIBusinessUnit(String iBusinessUnit) {
		this.iBusinessUnit = iBusinessUnit;
	}

	public String getICForm() {
		return this.iCForm;
	}

	public void setICForm(String iCForm) {
		this.iCForm = iCForm;
	}

	public String getIChargePeriod() {
		return this.iChargePeriod;
	}

	public void setIChargePeriod(String iChargePeriod) {
		this.iChargePeriod = iChargePeriod;
	}

	public String getIChargingrateValue() {
		return this.iChargingrateValue;
	}

	public void setIChargingrateValue(String iChargingrateValue) {
		this.iChargingrateValue = iChargingrateValue;
	}

	public String getICommissionDate() {
		return this.iCommissionDate;
	}

	public void setICommissionDate(String iCommissionDate) {
		this.iCommissionDate = iCommissionDate;
	}

	public String getIComponentName() {
		return this.iComponentName;
	}

	public void setIComponentName(String iComponentName) {
		this.iComponentName = iComponentName;
	}

	public BigDecimal getIContractDuration() {
		return this.iContractDuration;
	}

	public void setIContractDuration(BigDecimal iContractDuration) {
		this.iContractDuration = iContractDuration;
	}

	public String getIContractGstinAddress() {
		return this.iContractGstinAddress;
	}

	public void setIContractGstinAddress(String iContractGstinAddress) {
		this.iContractGstinAddress = iContractGstinAddress;
	}

	public String getIContractingAddrress() {
		return this.iContractingAddrress;
	}

	public void setIContractingAddrress(String iContractingAddrress) {
		this.iContractingAddrress = iContractingAddrress;
	}

	public String getIContractingGstinNo() {
		return this.iContractingGstinNo;
	}

	public void setIContractingGstinNo(String iContractingGstinNo) {
		this.iContractingGstinNo = iContractingGstinNo;
	}

	public String getICopfId() {
		return this.iCopfId;
	}

	public void setICopfId(String iCopfId) {
		this.iCopfId = iCopfId;
	}

	public String getICreditClass() {
		return this.iCreditClass;
	}

	public void setICreditClass(String iCreditClass) {
		this.iCreditClass = iCreditClass;
	}

	public String getICurrencyCodePrd() {
		return this.iCurrencyCodePrd;
	}

	public void setICurrencyCodePrd(String iCurrencyCodePrd) {
		this.iCurrencyCodePrd = iCurrencyCodePrd;
	}

	public String getICustomerRef() {
		return this.iCustomerRef;
	}

	public void setICustomerRef(String iCustomerRef) {
		this.iCustomerRef = iCustomerRef;
	}

	public String getIDayExtension() {
		return this.iDayExtension;
	}

	public void setIDayExtension(String iDayExtension) {
		this.iDayExtension = iDayExtension;
	}

	public String getIDayTelephone() {
		return this.iDayTelephone;
	}

	public void setIDayTelephone(String iDayTelephone) {
		this.iDayTelephone = iDayTelephone;
	}

	public String getIDeliveryAddress() {
		return this.iDeliveryAddress;
	}

	public void setIDeliveryAddress(String iDeliveryAddress) {
		this.iDeliveryAddress = iDeliveryAddress;
	}

	public String getIDepartment() {
		return this.iDepartment;
	}

	public void setIDepartment(String iDepartment) {
		this.iDepartment = iDepartment;
	}

	public String getIDepositRefundBoo() {
		return this.iDepositRefundBoo;
	}

	public void setIDepositRefundBoo(String iDepositRefundBoo) {
		this.iDepositRefundBoo = iDepositRefundBoo;
	}

	public String getIEmail() {
		return this.iEmail;
	}

	public void setIEmail(String iEmail) {
		this.iEmail = iEmail;
	}

	public String getIEquipmentType() {
		return this.iEquipmentType;
	}

	public void setIEquipmentType(String iEquipmentType) {
		this.iEquipmentType = iEquipmentType;
	}

	public String getIEveExtension() {
		return this.iEveExtension;
	}

	public void setIEveExtension(String iEveExtension) {
		this.iEveExtension = iEveExtension;
	}

	public String getIEveTelephone() {
		return this.iEveTelephone;
	}

	public void setIEveTelephone(String iEveTelephone) {
		this.iEveTelephone = iEveTelephone;
	}

	public String getIEventSource() {
		return this.iEventSource;
	}

	public void setIEventSource(String iEventSource) {
		this.iEventSource = iEventSource;
	}

	public String getIEventSourceLabel() {
		return this.iEventSourceLabel;
	}

	public void setIEventSourceLabel(String iEventSourceLabel) {
		this.iEventSourceLabel = iEventSourceLabel;
	}

	public String getIEventSourceText() {
		return this.iEventSourceText;
	}

	public void setIEventSourceText(String iEventSourceText) {
		this.iEventSourceText = iEventSourceText;
	}

	public String getIFax() {
		return this.iFax;
	}

	public void setIFax(String iFax) {
		this.iFax = iFax;
	}

	public String getIFirstName() {
		return this.iFirstName;
	}

	public void setIFirstName(String iFirstName) {
		this.iFirstName = iFirstName;
	}

	public String getIInflight() {
		return this.iInflight;
	}

	public void setIInflight(String iInflight) {
		this.iInflight = iInflight;
	}

	public String getIInfoCurrency() {
		return this.iInfoCurrency;
	}

	public void setIInfoCurrency(String iInfoCurrency) {
		this.iInfoCurrency = iInfoCurrency;
	}

	public String getIInitials() {
		return this.iInitials;
	}

	public void setIInitials(String iInitials) {
		this.iInitials = iInitials;
	}

	public String getIInvoicingCoName() {
		return this.iInvoicingCoName;
	}

	public void setIInvoicingCoName(String iInvoicingCoName) {
		this.iInvoicingCoName = iInvoicingCoName;
	}

	public String getILastName() {
		return this.iLastName;
	}

	public void setILastName(String iLastName) {
		this.iLastName = iLastName;
	}

	public String getIMigration() {
		return this.iMigration;
	}

	public void setIMigration(String iMigration) {
		this.iMigration = iMigration;
	}

	public String getIMobile() {
		return this.iMobile;
	}

	public void setIMobile(String iMobile) {
		this.iMobile = iMobile;
	}

	public String getINrcCharges() {
		return this.iNrcCharges;
	}

	public void setINrcCharges(String iNrcCharges) {
		this.iNrcCharges = iNrcCharges;
	}

	public String getIPortalId() {
		return this.iPortalId;
	}

	public void setIPortalId(String iPortalId) {
		this.iPortalId = iPortalId;
	}

	public BigDecimal getIPosOldProductSeq() {
		return this.iPosOldProductSeq;
	}

	public void setIPosOldProductSeq(BigDecimal iPosOldProductSeq) {
		this.iPosOldProductSeq = iPosOldProductSeq;
	}

	public String getIPosition() {
		return this.iPosition;
	}

	public void setIPosition(String iPosition) {
		this.iPosition = iPosition;
	}

	public String getIProdaddr1() {
		return this.iProdaddr1;
	}

	public void setIProdaddr1(String iProdaddr1) {
		this.iProdaddr1 = iProdaddr1;
	}

	public String getIProdaddr2() {
		return this.iProdaddr2;
	}

	public void setIProdaddr2(String iProdaddr2) {
		this.iProdaddr2 = iProdaddr2;
	}

	public String getIProdaddr3() {
		return this.iProdaddr3;
	}

	public void setIProdaddr3(String iProdaddr3) {
		this.iProdaddr3 = iProdaddr3;
	}

	public String getIProdaddr4() {
		return this.iProdaddr4;
	}

	public void setIProdaddr4(String iProdaddr4) {
		this.iProdaddr4 = iProdaddr4;
	}

	public String getIProdaddr5() {
		return this.iProdaddr5;
	}

	public void setIProdaddr5(String iProdaddr5) {
		this.iProdaddr5 = iProdaddr5;
	}

	public String getIProdcountryName() {
		return this.iProdcountryName;
	}

	public void setIProdcountryName(String iProdcountryName) {
		this.iProdcountryName = iProdcountryName;
	}

	public String getIProductName() {
		return this.iProductName;
	}

	public void setIProductName(String iProductName) {
		this.iProductName = iProductName;
	}

	public BigDecimal getIProductQuantity() {
		return this.iProductQuantity;
	}

	public void setIProductQuantity(BigDecimal iProductQuantity) {
		this.iProductQuantity = iProductQuantity;
	}

	public String getIProdzipCode() {
		return this.iProdzipCode;
	}

	public void setIProdzipCode(String iProdzipCode) {
		this.iProdzipCode = iProdzipCode;
	}

	public String getIProrateBoo() {
		return this.iProrateBoo;
	}

	public void setIProrateBoo(String iProrateBoo) {
		this.iProrateBoo = iProrateBoo;
	}

	public Date getIRateEndDtm() {
		return this.iRateEndDtm;
	}

	public void setIRateEndDtm(Date iRateEndDtm) {
		this.iRateEndDtm = iRateEndDtm;
	}

	public String getIRcCharges() {
		return this.iRcCharges;
	}

	public void setIRcCharges(String iRcCharges) {
		this.iRcCharges = iRcCharges;
	}

	public String getIRefundBoo() {
		return this.iRefundBoo;
	}

	public void setIRefundBoo(String iRefundBoo) {
		this.iRefundBoo = iRefundBoo;
	}

	public BigDecimal getIRhsHrs() {
		return this.iRhsHrs;
	}

	public void setIRhsHrs(BigDecimal iRhsHrs) {
		this.iRhsHrs = iRhsHrs;
	}

	public String getIScenarioType() {
		return this.iScenarioType;
	}

	public void setIScenarioType(String iScenarioType) {
		this.iScenarioType = iScenarioType;
	}

	public String getIServiceId() {
		return this.iServiceId;
	}

	public void setIServiceId(String iServiceId) {
		this.iServiceId = iServiceId;
	}

	public String getIServiceType() {
		return this.iServiceType;
	}

	public void setIServiceType(String iServiceType) {
		this.iServiceType = iServiceType;
	}

	public String getISiteAAddress() {
		return this.iSiteAAddress;
	}

	public void setISiteAAddress(String iSiteAAddress) {
		this.iSiteAAddress = iSiteAAddress;
	}

	public String getISiteBAddress() {
		return this.iSiteBAddress;
	}

	public void setISiteBAddress(String iSiteBAddress) {
		this.iSiteBAddress = iSiteBAddress;
	}

	public String getISiteEnd() {
		return this.iSiteEnd;
	}

	public void setISiteEnd(String iSiteEnd) {
		this.iSiteEnd = iSiteEnd;
	}

	public String getISiteaGstinAddress() {
		return this.iSiteaGstinAddress;
	}

	public void setISiteaGstinAddress(String iSiteaGstinAddress) {
		this.iSiteaGstinAddress = iSiteaGstinAddress;
	}

	public String getISiteaGstinNo() {
		return this.iSiteaGstinNo;
	}

	public void setISiteaGstinNo(String iSiteaGstinNo) {
		this.iSiteaGstinNo = iSiteaGstinNo;
	}

	public String getISitebGstinAddress() {
		return this.iSitebGstinAddress;
	}

	public void setISitebGstinAddress(String iSitebGstinAddress) {
		this.iSitebGstinAddress = iSitebGstinAddress;
	}

	public String getISitebGstinNo() {
		return this.iSitebGstinNo;
	}

	public void setISitebGstinNo(String iSitebGstinNo) {
		this.iSitebGstinNo = iSitebGstinNo;
	}

	public BigDecimal getISize() {
		return this.iSize;
	}

	public void setISize(BigDecimal iSize) {
		this.iSize = iSize;
	}

	public BigDecimal getISourceParentProductSeq() {
		return this.iSourceParentProductSeq;
	}

	public void setISourceParentProductSeq(BigDecimal iSourceParentProductSeq) {
		this.iSourceParentProductSeq = iSourceParentProductSeq;
	}

	public BigDecimal getISourceProductSeq() {
		return this.iSourceProductSeq;
	}

	public void setISourceProductSeq(BigDecimal iSourceProductSeq) {
		this.iSourceProductSeq = iSourceProductSeq;
	}

	public String getISystemIndicator() {
		return this.iSystemIndicator;
	}

	public void setISystemIndicator(String iSystemIndicator) {
		this.iSystemIndicator = iSystemIndicator;
	}

	public String getITaxExemptRef() {
		return this.iTaxExemptRef;
	}

	public void setITaxExemptRef(String iTaxExemptRef) {
		this.iTaxExemptRef = iTaxExemptRef;
	}

	public String getITaxExemptTxt() {
		return this.iTaxExemptTxt;
	}

	public void setITaxExemptTxt(String iTaxExemptTxt) {
		this.iTaxExemptTxt = iTaxExemptTxt;
	}

	public BigDecimal getITermCharges() {
		return this.iTermCharges;
	}

	public void setITermCharges(BigDecimal iTermCharges) {
		this.iTermCharges = iTermCharges;
	}

	public String getITermEnddate() {
		return this.iTermEnddate;
	}

	public void setITermEnddate(String iTermEnddate) {
		this.iTermEnddate = iTermEnddate;
	}

	public String getITermEventSrc() {
		return this.iTermEventSrc;
	}

	public void setITermEventSrc(String iTermEventSrc) {
		this.iTermEventSrc = iTermEventSrc;
	}

	public String getITermProposedDate() {
		return this.iTermProposedDate;
	}

	public void setITermProposedDate(String iTermProposedDate) {
		this.iTermProposedDate = iTermProposedDate;
	}

	public String getITermReasonId() {
		return this.iTermReasonId;
	}

	public void setITermReasonId(String iTermReasonId) {
		this.iTermReasonId = iTermReasonId;
	}

	public String getITitle() {
		return this.iTitle;
	}

	public void setITitle(String iTitle) {
		this.iTitle = iTitle;
	}

	public String getIUserName() {
		return this.iUserName;
	}

	public void setIUserName(String iUserName) {
		this.iUserName = iUserName;
	}

	public String getIVatIdentifier() {
		return this.iVatIdentifier;
	}

	public void setIVatIdentifier(String iVatIdentifier) {
		this.iVatIdentifier = iVatIdentifier;
	}

	public String getIWarehouseAddress() {
		return this.iWarehouseAddress;
	}

	public void setIWarehouseAddress(String iWarehouseAddress) {
		this.iWarehouseAddress = iWarehouseAddress;
	}

	public String getInputGroupId() {
		return this.inputGroupId;
	}

	public void setInputGroupId(String inputGroupId) {
		this.inputGroupId = inputGroupId;
	}

	public BigDecimal getInputRowId() {
		return this.inputRowId;
	}

	public void setInputRowId(BigDecimal inputRowId) {
		this.inputRowId = inputRowId;
	}

	public String getOrderType() {
		return this.orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getRequestType() {
		return this.requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public Date getRowCreatedDtm() {
		return this.rowCreatedDtm;
	}

	public void setRowCreatedDtm(Date rowCreatedDtm) {
		this.rowCreatedDtm = rowCreatedDtm;
	}

	public Date getRowLastUpdatedDtm() {
		return this.rowLastUpdatedDtm;
	}

	public void setRowLastUpdatedDtm(Date rowLastUpdatedDtm) {
		this.rowLastUpdatedDtm = rowLastUpdatedDtm;
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

	@Override
	public String toString() {
		return "GnvOrderEntryTab [actionType=" + actionType + ", iCustomerRef=" + iCustomerRef + ", iDepositRefundBoo="
				+ iDepositRefundBoo + ", iInflight=" + iInflight + ", inputGroupId=" + inputGroupId + ", inputRowId="
				+ inputRowId + ", iRefundBoo=" + iRefundBoo + ", iTermProposedDate=" + iTermProposedDate
				+ ", iTermReasonId=" + iTermReasonId + ", iUserName=" + iUserName + ", orderType=" + orderType
				+ ", requestType=" + requestType + ", sourceSystem=" + sourceSystem + "]";
	}
	

}