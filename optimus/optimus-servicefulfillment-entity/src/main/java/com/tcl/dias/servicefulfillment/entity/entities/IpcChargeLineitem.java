package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "ipc_charge_lineitems")
@NamedQuery(name = "IpcChargeLineitem.findAll", query = "SELECT ipc FROM IpcChargeLineitem ipc")
public class IpcChargeLineitem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "account_number")
	private String accountNumber;

	private String arc;

	@Column(name = "charge_lineitem")
	private String chargeLineitem;
	
	private String description;

	private String mrc;

	private String nrc;

	@Column(name = "ppu_rate")
	private String ppuRate;

	@Column(name = "service_id")
	private String serviceId;

	@Column(name = "service_type")
	private String serviceType;

	@Column(name = "billing_method")
	private String billingMethod;

	@Column(name = "pricing_model")
	private String pricingModel;

	@Column(name = "product_description")
	private String productDescription;

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
	
	@Column(name = "mig_parent_service_code")
	private String migParentServiceCode;
	
	@Column(name = "cloud_code")
	private String cloudCode;
	
	@Column(name = "parent_cloud_code")
	private String parentCloudCode;

	@Column(name = "site_type")
	private String siteType;
	
	@Column(name = "additional_param")
	private String additionalParam;
	
	@Column(name = "version")
	private Integer version;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "source_product_sequence")
	private Integer sourceProductSequence;
	
	@Column(name = "commissioned_flag")
	private String commissionedFlag;
	
	@Column(name = "terminated_flag")
	private String terminatedFlag;
	
	@Column(name = "action_type")
	private String actionType;
	
	@Column(name = "scenario_type")
	private String scenarioType;

	@Column(name = "old_nrc")
	private String oldNrc;
	
	public IpcChargeLineitem() {
	}

	public IpcChargeLineitem(String accountNumber, String arc, String chargeLineitem, String description, String mrc, String nrc,
			String ppuRate, String pricingModel, String productDescription,
			String serviceId, String serviceType, String billingMethod, String unitOfMeasurement, String quantity,
			String isProrated, String component, String cpeModel, String hsnCode, String serviceCode, String migParentServiceCode,
			String cloudCode, String parentCloudCode, String additionalParam, Integer version, String actionType, String status,
			String commissionedFlag, String terminatedFlag, String scenarioType) {
		super();
		this.accountNumber = accountNumber;
		this.arc = arc;
		this.chargeLineitem = chargeLineitem;
		this.description = description;
		this.mrc = mrc;
		this.nrc = nrc;
		this.ppuRate = ppuRate;
		this.pricingModel = pricingModel;
		this.productDescription = productDescription;
		this.serviceId = serviceId;
		this.serviceType = serviceType;
		this.billingMethod = billingMethod;
		this.unitOfMeasurement = unitOfMeasurement;
		this.quantity = quantity;
		this.isProrated = isProrated;
		this.component = component;
		this.cpeModel = cpeModel;
		this.hsnCode = hsnCode;
		this.serviceCode = serviceCode;
		this.migParentServiceCode = migParentServiceCode;
		this.cloudCode = cloudCode;
		this.parentCloudCode = parentCloudCode;
		this.additionalParam = additionalParam;
		this.version = version;
		this.actionType = actionType;
		this.status = status;
		this.commissionedFlag = commissionedFlag;
		this.terminatedFlag = terminatedFlag;
		this.scenarioType = scenarioType;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	public String getMigParentServiceCode() {
		return migParentServiceCode;
	}

	public void setMigParentServiceCode(String migParentServiceCode) {
		this.migParentServiceCode = migParentServiceCode;
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

	public String getAdditionalParam() {
		return additionalParam;
	}

	public void setAdditionalParam(String additionalParam) {
		this.additionalParam = additionalParam;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCommissionedFlag() {
		return commissionedFlag;
	}

	public void setCommissionedFlag(String commissionedFlag) {
		this.commissionedFlag = commissionedFlag;
	}

	public String getTerminatedFlag() {
		return terminatedFlag;
	}

	public void setTerminatedFlag(String terminatedFlag) {
		this.terminatedFlag = terminatedFlag;
	}

	public Integer getSourceProductSequence() {
		return sourceProductSequence;
	}

	public void setSourceProductSequence(Integer sourceProductSequence) {
		this.sourceProductSequence = sourceProductSequence;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	
	public String getPpuRate() {
		return ppuRate;
	}

	public void setPpuRate(String ppuRate) {
		this.ppuRate = ppuRate;
	}

	public String getPricingModel() {
		return pricingModel;
	}

	public void setPricingModel(String pricingModel) {
		this.pricingModel = pricingModel;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getScenarioType() {
		return scenarioType;
	}

	public void setScenarioType(String scenarioType) {
		this.scenarioType = scenarioType;
	}

	public String getOldNrc() {
		return oldNrc;
	}

	public void setOldNrc(String oldNrc) {
		this.oldNrc = oldNrc;
	}

	@Override
	public String toString() {
		return "IpcChargeLineitem [id=" + id + ", accountNumber=" + accountNumber + ", arc=" + arc + ", chargeLineitem="
				+ chargeLineitem + ", description=" + description + ", mrc=" + mrc + ", nrc=" + nrc + ", ppuRate="
				+ ppuRate + ", serviceId=" + serviceId + ", serviceType=" + serviceType + ", billingMethod="
				+ billingMethod + ", pricingModel=" + pricingModel + ", productDescription=" + productDescription
				+ ", unitOfMeasurement=" + unitOfMeasurement + ", quantity=" + quantity + ", isProrated=" + isProrated
				+ ", component=" + component + ", cpeModel=" + cpeModel + ", hsnCode=" + hsnCode + ", serviceCode="
				+ serviceCode + ", migParentServiceCode=" + migParentServiceCode + ", cloudCode=" + cloudCode
				+ ", parentCloudCode=" + parentCloudCode + ", siteType=" + siteType + ", additionalParam="
				+ additionalParam + ", version=" + version + ", status=" + status + ", sourceProductSequence="
				+ sourceProductSequence + ", commissionedFlag=" + commissionedFlag + ", terminatedFlag="
				+ terminatedFlag + ", actionType=" + actionType + ", scenarioType=" + scenarioType + "]";
	}
}