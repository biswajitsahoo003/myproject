package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the sc_charge_lineitems database table.
 * 
 * @author yomagesh
 */
@Entity
@Table(name = "sc_charge_lineitems")
@NamedQuery(name = "ScChargeLineitem.findAll", query = "SELECT s FROM ScChargeLineitem s")
public class ScChargeLineitem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "account_number")
	private String accountNumber;

	private String arc;

	@Column(name = "charge_lineitem")
	private String chargeLineitem;

	private String mrc;

	private String nrc;

	@Column(name = "service_id")
	private String serviceId;

	@Column(name = "service_type")
	private String serviceType;

	@Column(name = "billing_method")
	private String billingMethod;

	@Column(name = "uom")
	private String unitOfMeasurement;

	@Column(name = "quantity")
	private String quantity;

	@Column(name = "is_prorated")
	private String isProrated;

	private String component;

	@Column(name = "cpe_model")
	private String cpeModel;

	@Column(name = "hsn_code")
	private String hsnCode;
	
	@Column(name = "service_code")
	private String serviceCode;
	
	@Column(name = "cloud_code")
	private String cloudCode;
	
	@Column(name = "parent_cloud_code")
	private String parentCloudCode;
	
	@Column(name = "site_type")
	private String siteType;
	
	@Column(name = "commissioning_flag")
	private String commissioningFlag;
	
	@Column(name = "source_product_sequence")
	private String sourceProdSequence;
	
	// For IAS & GVPN Products
	@Column(name = "usage_arc")
	private String usageArc; 
	
	// For Teams DR Products
	@Column(name = "effective_usage")
	private String effectiveUsage; 
	
	@Column(name = "effective_overage")
	private String effectiveOverage; 
	
	@Column(name = "action_type")
	private String actionType;
	
	@Column(name = "status")
	private String status;
		
	@Column(name ="billing_type")
	private String billingType;
	
	@Column(name ="service_termination_flag")
	private String serviceTerminationFlag;
	
	@Column(name ="etc_charge")
	private String etcCharge;
	
	@Column(name ="etc_waiver")
	private String etcWaiver;
	
	@Column(name ="service_termination_date")
	private String termDate;
	
	@Column(name ="component_desc")
	private String componentDesc;
	
	@Column(name ="input_group_id")
	private String inputGroupId;
	
	@Column(name ="sku_id")
	private String skuId;
	
	@Column(name ="customer_ref")
	private String customerRef;
	
	public ScChargeLineitem() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccountNumber() {
		return this.accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getArc() {
		return this.arc;
	}

	public void setArc(String arc) {
		this.arc = arc;
	}

	public String getChargeLineitem() {
		return this.chargeLineitem;
	}

	public void setChargeLineitem(String chargeLineitem) {
		this.chargeLineitem = chargeLineitem;
	}

	public String getMrc() {
		return this.mrc;
	}

	public void setMrc(String mrc) {
		this.mrc = mrc;
	}

	public String getNrc() {
		return this.nrc;
	}

	public void setNrc(String nrc) {
		this.nrc = nrc;
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

	public String getBillingMethod() {
		return billingMethod;
	}

	public void setBillingMethod(String billingMethod) {
		this.billingMethod = billingMethod;
	}

	public String getUnitOfMeasurement() {
		return unitOfMeasurement;
	}

	public void setUnitOfMeasurement(String unitOfMeasurement) {
		this.unitOfMeasurement = unitOfMeasurement;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getIsProrated() {
		return isProrated;
	}

	public void setIsProrated(String isProrated) {
		this.isProrated = isProrated;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getCpeModel() {
		return cpeModel;
	}

	public void setCpeModel(String cpeModel) {
		this.cpeModel = cpeModel;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}
	
	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	
	public String getCloudCode() {
		return cloudCode;
	}

	public void setCloudCode(String cloudCode) {
		this.cloudCode = cloudCode;
	}

	public String getParentCloudCode() {
		return parentCloudCode;
	}

	public void setParentCloudCode(String parentCloudCode) {
		this.parentCloudCode = parentCloudCode;
	}
	
	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getCommissioningFlag() {
		return commissioningFlag;
	}

	public void setCommissioningFlag(String commissioningFlag) {
		this.commissioningFlag = commissioningFlag;
	}

	public String getSourceProdSequence() {
		return sourceProdSequence;
	}

	public void setSourceProdSequence(String sourceProdSequence) {
		this.sourceProdSequence = sourceProdSequence;
	}
	
	public String getUsageArc() {
		return usageArc;
	}

	public void setUsageArc(String usageArc) {
		this.usageArc = usageArc;
	}
	
	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}

	public String getServiceTerminationFlag() {
		return serviceTerminationFlag;
	}

	public void setServiceTerminationFlag(String serviceTerminationFlag) {
		this.serviceTerminationFlag = serviceTerminationFlag;
	}
	
	public String getEtcCharge() {
		return etcCharge;
	}

	public void setEtcCharge(String etcCharge) {
		this.etcCharge = etcCharge;
	}

	public String getEtcWaiver() {
		return etcWaiver;
	}

	public void setEtcWaiver(String etcWaiver) {
		this.etcWaiver = etcWaiver;
	}
	
	public String getTermDate() {
		return termDate;
	}

	public void setTermDate(String termDate) {
		this.termDate = termDate;
	}
	
	public String getComponentDesc() {
		return componentDesc;
	}

	public void setComponentDesc(String componentDesc) {
		this.componentDesc = componentDesc;
	}
	
	public String getInputGroupId() {
		return inputGroupId;
	}

	public void setInputGroupId(String inputGroupId) {
		this.inputGroupId = inputGroupId;
	}
	
	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getCustomerRef() {
		return customerRef;
	}

	public void setCustomerRef(String customerRef) {
		this.customerRef = customerRef;
	}

	public String getEffectiveUsage() {
		return effectiveUsage;
	}

	public void setEffectiveUsage(String effectiveUsage) {
		this.effectiveUsage = effectiveUsage;
	}

	public String getEffectiveOverage() {
		return effectiveOverage;
	}

	public void setEffectiveOverage(String effectiveOverage) {
		this.effectiveOverage = effectiveOverage;
	}

	@Override
	public String toString() {
		return "ScChargeLineitem [id=" + id + ", accountNumber=" + accountNumber + ", arc=" + arc + ", chargeLineitem="
				+ chargeLineitem + ", mrc=" + mrc + ", nrc=" + nrc + ", serviceId=" + serviceId + ", serviceType="
				+ serviceType + ", billingMethod=" + billingMethod + ", unitOfMeasurement=" + unitOfMeasurement
				+ ", quantity=" + quantity + ", isProrated=" + isProrated + ", component=" + component + ", cpeModel="
				+ cpeModel + ", hsnCode=" + hsnCode + ", serviceCode=" + serviceCode + ", cloudCode=" + cloudCode
				+ ", parentCloudCode=" + parentCloudCode + ", siteType=" + siteType + ", commissioningFlag="
				+ commissioningFlag + ", sourceProdSequence=" + sourceProdSequence + ", usageArc=" + usageArc
				+ ", actionType=" + actionType + ", status=" + status + ", billingType=" + billingType
				+ ", serviceTerminationFlag=" + serviceTerminationFlag + ", etcCharge=" + etcCharge + ", etcWaiver="
				+ etcWaiver + ", termDate=" + termDate + ", componentDesc=" + componentDesc + ", inputGroupId="
				+ inputGroupId + ", skuId=" + skuId + ", customerRef=" + customerRef + "]";
	}
	
	

}