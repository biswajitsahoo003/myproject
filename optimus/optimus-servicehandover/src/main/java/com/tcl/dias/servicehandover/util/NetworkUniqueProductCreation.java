package com.tcl.dias.servicehandover.util;

import com.tcl.dias.servicefulfillment.entity.entities.GenevaIpcOrderEntry;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;

public class NetworkUniqueProductCreation {
	
	private String productName;
	private Float mrc;
	private Float nrc;
	private Float arc;
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
	private String groupIdForMacd;
	private GenevaIpcOrderEntry genevaIpcOrderEntry;
	private String copfId;
	private String macdOrderId;
	private String serviceType;
	private String component;
	private String cpeModel;
	private String poDate;
	private String poNumber;
	private String wareHouseState;
	private String cpeSerialNo;
	private String cpeDispatchDate;
	private String hsnCode;
	private String attributeString;
	private String sourceProductSeq;
	private String termEndDate;
	private String inputGroupId;
	private String siteType;
	private String billingType;
	private boolean isParallelMacd;
	private Integer parallelRunDays;
	private String baseBandwidth;
	private String maxBandwidth;
	private String usageModel;
	private ServicehandoverAudit servicehandoverAudit;
	private String optimusOrderType;
	private String usageArc;
	private String macdServiceId;
	private String commissioningDate;
	private String description;
	private String skuId;
	private String customerRef;
	private boolean isBillingInternational;
	private String billingMethod;
	private String customerCuId;
	private String delegateAdminAccess;
	private String effectiveUsage;
	private String effectiveOverage;
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Float getMrc() {
		return mrc;
	}
	public void setMrc(Float mrc) {
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
	public String getGroupIdForMacd() {
		return groupIdForMacd;
	}
	public void setGroupIdForMacd(String groupIdForMacd) {
		this.groupIdForMacd = groupIdForMacd;
	}
	public String getCopfId() {
		return copfId;
	}
	public void setCopfId(String copfId) {
		this.copfId = copfId;
	}
	public Float getNrc() {
		return nrc;
	}
	public void setNrc(Float nrc) {
		this.nrc = nrc;
	}
	public String getMacdOrderId() {
		return macdOrderId;
	}
	public void setMacdOrderId(String macdOrderId) {
		this.macdOrderId = macdOrderId;
	}
	public Float getArc() {
		return arc;
	}
	public void setArc(Float arc) {
		this.arc = arc;
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
	public String getCpeModel() {
		return cpeModel;
	}
	public void setCpeModel(String cpeModel) {
		this.cpeModel = cpeModel;
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
	public String getWareHouseState() {
		return wareHouseState;
	}
	public void setWareHouseState(String wareHouseState) {
		this.wareHouseState = wareHouseState;
	}
	public String getCpeSerialNo() {
		return cpeSerialNo;
	}
	public void setCpeSerialNo(String cpeSerialNo) {
		this.cpeSerialNo = cpeSerialNo;
	}
	public String getCpeDispatchDate() {
		return cpeDispatchDate;
	}
	public void setCpeDispatchDate(String cpeDispatchDate) {
		this.cpeDispatchDate = cpeDispatchDate;
	}
	public String getHsnCode() {
		return hsnCode;
	}
	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}
	public String getAttributeString() {
		return attributeString;
	}
	public void setAttributeString(String attributeString) {
		this.attributeString = attributeString;
	}
	
	public String getSourceProductSeq() {
		return sourceProductSeq;
	}
	public void setSourceProductSeq(String sourceProductSeq) {
		this.sourceProductSeq = sourceProductSeq;
	}
	public String getInputGroupId() {
		return inputGroupId;
	}
	public void setInputGroupId(String inputGroupId) {
		this.inputGroupId = inputGroupId;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	public String getBillingType() {
		return billingType;
	}
	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}
	public boolean isParallelMacd() {
		return isParallelMacd;
	}
	public void setParallelMacd(boolean isParallelMacd) {
		this.isParallelMacd = isParallelMacd;
	}
	public Integer getParallelRunDays() {
		return parallelRunDays;
	}
	public void setParallelRunDays(Integer parallelRunDays) {
		this.parallelRunDays = parallelRunDays;
	}
	public String getTermEndDate() {
		return termEndDate;
	}
	public void setTermEndDate(String termEndDate) {
		this.termEndDate = termEndDate;
	}
	public String getBaseBandwidth() {
		return baseBandwidth;
	}
	public void setBaseBandwidth(String baseBandwidth) {
		this.baseBandwidth = baseBandwidth;
	}
	public String getMaxBandwidth() {
		return maxBandwidth;
	}
	public void setMaxBandwidth(String maxBandwidth) {
		this.maxBandwidth = maxBandwidth;
	}
	public String getUsageModel() {
		return usageModel;
	}
	public void setUsageModel(String usageModel) {
		this.usageModel = usageModel;
	}
	public ServicehandoverAudit getServicehandoverAudit() {
		return servicehandoverAudit;
	}
	public void setServicehandoverAudit(ServicehandoverAudit servicehandoverAudit) {
		this.servicehandoverAudit = servicehandoverAudit;
	}
	public String getOptimusOrderType() {
		return optimusOrderType;
	}
	public void setOptimusOrderType(String optimusOrderType) {
		this.optimusOrderType = optimusOrderType;
	}
	
	public String getUsageArc() {
		return usageArc;
	}
	public void setUsageArc(String usageArc) {
		this.usageArc = usageArc; 
	}
	public String getMacdServiceId() {
		return macdServiceId;
	}
	public void setMacdServiceId(String macdServiceId) {
		this.macdServiceId = macdServiceId;
	}
	public String getCommissioningDate() {
		return commissioningDate;
	}
	public void setCommissioningDate(String commissioningDate) {
		this.commissioningDate = commissioningDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public boolean isBillingInternational() {
		return isBillingInternational;
	}
	public void setBillingInternational(boolean isBillingInternational) {
		this.isBillingInternational = isBillingInternational;
	}
	public String getBillingMethod() {
		return billingMethod;
	}
	public void setBillingMethod(String billingMethod) {
		this.billingMethod = billingMethod;
	}
	public String getCustomerCuId() {
		return customerCuId;
	}
	public void setCustomerCuId(String customerCuId) {
		this.customerCuId = customerCuId;
	}
	public String getDelegateAdminAccess() {
		return delegateAdminAccess;
	}
	public void setDelegateAdminAccess(String delegateAdminAccess) {
		this.delegateAdminAccess = delegateAdminAccess;
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
	
}
