package com.tcl.dias.servicehandover.util;

import com.tcl.dias.servicefulfillment.entity.entities.GenevaIpcOrderEntry;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;

public class UniqueProductCreation {
	
	private String productName;
	private String mrc;
	private String nrc;
	private String arc;
	private String quantity;
	private String mhs_remarks;
	private String accountId;
	private ScOrder scOrder;
	private ScServiceDetail scServiceDetail;
	private String processInstanceId;
	private Integer count;
	private String orderType;
	private String actionType;
	private String changeOrderType;
	private Boolean isMacd;
	private String cloudCode;
	private String parentCloudCode;
	private String groupIDModifyVM;
	private GenevaIpcOrderEntry genevaIpcOrderEntry;
	private String copfId;
	private String macdOrderId;
	private String contractGstn;
	private String inputGroupId;
	private String serviceType;
	private String component;
	private String poDate;
	private String poNumber;
	private String hsnCode;
	private String sourceProdSeq;
	private String baseBandwidth;
	private String commissioningDate;
	private String scenarioType;
	private ServicehandoverAudit servicehandoverAudit;
	private String migParentServiceCode;
	private String contractingAddress;
	private String sapCode;
	private String productDescription;
	private String pricingModel;
	private String ppuRate;
	private String overageChargesDiscountTiers;
	private boolean isBillingInternational;
	private String billingEntity;
	
	public String getPricingModel() {
		return pricingModel;
	}
	public void setPricingModel(String pricingModel) {
		this.pricingModel = pricingModel;
	}
	public String getPpuRate() {
		return ppuRate;
	}
	public void setPpuRate(String ppuRate) {
		this.ppuRate = ppuRate;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getMrc() {
		return mrc;
	}
	public void setMrc(String mrc) {
		this.mrc = mrc;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getMhs_remarks() {
		return mhs_remarks;
	}
	public void setMhs_remarks(String mhs_remarks) {
		this.mhs_remarks = mhs_remarks;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public ScOrder getScOrder() {
		return scOrder;
	}
	public void setScOrder(ScOrder scOrder) {
		this.scOrder = scOrder;
	}
	public ScServiceDetail getScServiceDetail() {
		return scServiceDetail;
	}
	public void setScServiceDetail(ScServiceDetail scServiceDetail) {
		this.scServiceDetail = scServiceDetail;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getChangeOrderType() {
		return changeOrderType;
	}
	public void setChangeOrderType(String changeOrderType) {
		this.changeOrderType = changeOrderType;
	}
	public GenevaIpcOrderEntry getGenevaIpcOrderEntry() {
		return genevaIpcOrderEntry;
	}
	public void setGenevaIpcOrderEntry(GenevaIpcOrderEntry genevaIpcOrderEntry) {
		this.genevaIpcOrderEntry = genevaIpcOrderEntry;
	}
	public Boolean getIsMacd() {
		return isMacd;
	}
	public void setIsMacd(Boolean isMacd) {
		this.isMacd = isMacd;
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
	public String getGroupIDModifyVM() {
		return groupIDModifyVM;
	}
	public void setGroupIDModifyVM(String groupIDModifyVM) {
		this.groupIDModifyVM = groupIDModifyVM;
	}
	public String getCopfId() {
		return copfId;
	}
	public void setCopfId(String copfId) {
		this.copfId = copfId;
	}
	public String getNrc() {
		return nrc;
	}
	public void setNrc(String nrc) {
		this.nrc = nrc;
	}
	public String getMacdOrderId() {
		return macdOrderId;
	}
	public void setMacdOrderId(String macdOrderId) {
		this.macdOrderId = macdOrderId;
	}
	public String getContractGstn() {
		return contractGstn;
	}
	public void setContractGstn(String contractGstn) {
		this.contractGstn = contractGstn;
	}
	public String getArc() {
		return arc;
	}
	public void setArc(String arc) {
		this.arc = arc;
	}
	public String getInputGroupId() {
		return inputGroupId;
	}
	public void setInputGroupId(String inputGroupId) {
		this.inputGroupId = inputGroupId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
	}
	public String getPoDate() {
		return poDate;
	}
	public void setPoDate(String poDate) {
		this.poDate = poDate;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	public String getHsnCode() {
		return hsnCode;
	}
	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}
	public String getSourceProdSeq() {
		return sourceProdSeq;
	}
	public void setSourceProdSeq(String sourceProdSeq) {
		this.sourceProdSeq = sourceProdSeq;
	}
	public String getBaseBandwidth() {
		return baseBandwidth;
	}
	public void setBaseBandwidth(String baseBandwidth) {
		this.baseBandwidth = baseBandwidth;
	}
	public String getCommissioningDate() {
		return commissioningDate;
	}
	public void setCommissioningDate(String commissioningDate) {
		this.commissioningDate = commissioningDate;
	}
	public String getScenarioType() {
		return scenarioType;
	}
	public void setScenarioType(String scenarioType) {
		this.scenarioType = scenarioType;
	}
	public ServicehandoverAudit getServicehandoverAudit() {
		return servicehandoverAudit;
	}
	public void setServicehandoverAudit(ServicehandoverAudit servicehandoverAudit) {
		this.servicehandoverAudit = servicehandoverAudit;
	}
	public String getMigParentServiceCode() {
		return migParentServiceCode;
	}
	public void setMigParentServiceCode(String migParentServiceCode) {
		this.migParentServiceCode = migParentServiceCode;
	}
	public String getContractingAddress() {
		return contractingAddress;
	}
	public void setContractingAddress(String contractingAddress) {
		this.contractingAddress = contractingAddress;
	}
	public String getSapCode() {
		return sapCode;
	}
	public void setSapCode(String sapCode) {
		this.sapCode = sapCode;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public String getOverageChargesDiscountTiers() {
		return overageChargesDiscountTiers;
	}
	public void setOverageChargesDiscountTiers(String overageChargesDiscountTiers) {
		this.overageChargesDiscountTiers = overageChargesDiscountTiers;
	}
	public boolean isBillingInternational() {
		return isBillingInternational;
	}
	public void setBillingInternational(boolean isBillingInternational) {
		this.isBillingInternational = isBillingInternational;
	}
	public String getBillingEntity() {
		return billingEntity;
	}
	public void setBillingEntity(String billingEntity) {
		this.billingEntity = billingEntity;
	}
	
	
	

}
