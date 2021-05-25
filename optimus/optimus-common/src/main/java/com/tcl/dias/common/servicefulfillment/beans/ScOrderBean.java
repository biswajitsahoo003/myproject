package com.tcl.dias.common.servicefulfillment.beans;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * This file contains the ScOrderBean.java class.
 * 
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class ScOrderBean {

	private Integer id;
	private String createdBy;
	private Date createdDate;
	private String customerGroupName;
	private String customerSegment;
	private String demoFlag;
	private Integer erfCustCustomerId;
	private String erfCustCustomerName;
	private Integer erfCustLeId;
	private String erfCustLeName;
	private Integer erfCustPartnerId;
	private String erfCustPartnerName;
	private Integer erfCustPartnerLeId;
	private String partnerCuid;
	private Integer erfCustSpLeId;
	private String erfCustSpLeName;
	private Integer erfUserCustomerUserId;
	private Integer erfUserInitiatorId;
	private String isActive;
	private String isBundleOrder;
	private String isMultipleLe;
	private Date lastMacdDate;
	private Date macdCreatedDate;
	private String opOrderCode;
	private String opportunityClassification;
	private String orderCategory;
	private Date orderEndDate;
	private String orderSource;
	private Date orderStartDate;
	private String orderStatus;
	private String orderType;
	private Integer parentId;
	private String parentOpOrderCode;
	private String sfdcAccountId;
	private String tpsCrmCofId;
	private String tpsCrmOptyId;
	private String tpsCrmSystem;
	private String tpsSapCrnId;
	private String tpsSecsId;
	private String tpsSfdcCuid;
	private String updatedBy;
	private Date updatedDate;
	private String uuid;
	private Integer erfOrderId;
	private String sfdcOptyId;

	private Integer erfOrderLeId;
	private Set<ScContractInfoBean> scContractInfos = new HashSet<>();
	private Set<ScOrderAttributeBean> scOrderAttributes = new HashSet<>();
	private Set<ScServiceDetailBean> scServiceDetails = new HashSet<>();
	private Set<ScCommercialBean> scCommercialBean = new HashSet<>();

	public Integer getErfOrderLeId() {
		return erfOrderLeId;
	}

	public void setErfOrderLeId(Integer erfOrderLeId) {
		this.erfOrderLeId = erfOrderLeId;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCustomerGroupName() {
		return this.customerGroupName;
	}

	public void setCustomerGroupName(String customerGroupName) {
		this.customerGroupName = customerGroupName;
	}

	public String getCustomerSegment() {
		return this.customerSegment;
	}

	public void setCustomerSegment(String customerSegment) {
		this.customerSegment = customerSegment;
	}

	public String getDemoFlag() {
		return this.demoFlag;
	}

	public void setDemoFlag(String demoFlag) {
		this.demoFlag = demoFlag;
	}

	public Integer getErfCustCustomerId() {
		return this.erfCustCustomerId;
	}

	public void setErfCustCustomerId(Integer erfCustCustomerId) {
		this.erfCustCustomerId = erfCustCustomerId;
	}

	public String getErfCustCustomerName() {
		return this.erfCustCustomerName;
	}

	public void setErfCustCustomerName(String erfCustCustomerName) {
		this.erfCustCustomerName = erfCustCustomerName;
	}

	public Integer getErfCustLeId() {
		return this.erfCustLeId;
	}

	public void setErfCustLeId(Integer erfCustLeId) {
		this.erfCustLeId = erfCustLeId;
	}

	public String getErfCustLeName() {
		return this.erfCustLeName;
	}

	public void setErfCustLeName(String erfCustLeName) {
		this.erfCustLeName = erfCustLeName;
	}

	public Integer getErfCustPartnerId() {
		return this.erfCustPartnerId;
	}

	public void setErfCustPartnerId(Integer erfCustPartnerId) {
		this.erfCustPartnerId = erfCustPartnerId;
	}

	public String getErfCustPartnerName() {
		return this.erfCustPartnerName;
	}

	public void setErfCustPartnerName(String erfCustPartnerName) {
		this.erfCustPartnerName = erfCustPartnerName;
	}

	public Integer getErfCustSpLeId() {
		return this.erfCustSpLeId;
	}

	public void setErfCustSpLeId(Integer erfCustSpLeId) {
		this.erfCustSpLeId = erfCustSpLeId;
	}

	public String getErfCustSpLeName() {
		return this.erfCustSpLeName;
	}

	public void setErfCustSpLeName(String erfCustSpLeName) {
		this.erfCustSpLeName = erfCustSpLeName;
	}

	public Integer getErfUserCustomerUserId() {
		return this.erfUserCustomerUserId;
	}

	public void setErfUserCustomerUserId(Integer erfUserCustomerUserId) {
		this.erfUserCustomerUserId = erfUserCustomerUserId;
	}

	public Integer getErfUserInitiatorId() {
		return this.erfUserInitiatorId;
	}

	public void setErfUserInitiatorId(Integer erfUserInitiatorId) {
		this.erfUserInitiatorId = erfUserInitiatorId;
	}

	public String getIsActive() {
		return this.isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getIsBundleOrder() {
		return this.isBundleOrder;
	}

	public void setIsBundleOrder(String isBundleOrder) {
		this.isBundleOrder = isBundleOrder;
	}

	public String getIsMultipleLe() {
		return this.isMultipleLe;
	}

	public void setIsMultipleLe(String isMultipleLe) {
		this.isMultipleLe = isMultipleLe;
	}

	public Date getLastMacdDate() {
		return this.lastMacdDate;
	}

	public void setLastMacdDate(Date lastMacdDate) {
		this.lastMacdDate = lastMacdDate;
	}

	public Date getMacdCreatedDate() {
		return this.macdCreatedDate;
	}

	public void setMacdCreatedDate(Date macdCreatedDate) {
		this.macdCreatedDate = macdCreatedDate;
	}

	public String getOpOrderCode() {
		return this.opOrderCode;
	}

	public void setOpOrderCode(String opOrderCode) {
		this.opOrderCode = opOrderCode;
	}

	public String getOpportunityClassification() {
		return this.opportunityClassification;
	}

	public void setOpportunityClassification(String opportunityClassification) {
		this.opportunityClassification = opportunityClassification;
	}

	public String getOrderCategory() {
		return this.orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public Date getOrderEndDate() {
		return this.orderEndDate;
	}

	public void setOrderEndDate(Date orderEndDate) {
		this.orderEndDate = orderEndDate;
	}

	public String getOrderSource() {
		return this.orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public Date getOrderStartDate() {
		return this.orderStartDate;
	}

	public void setOrderStartDate(Date orderStartDate) {
		this.orderStartDate = orderStartDate;
	}

	public String getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderType() {
		return this.orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getParentOpOrderCode() {
		return this.parentOpOrderCode;
	}

	public void setParentOpOrderCode(String parentOpOrderCode) {
		this.parentOpOrderCode = parentOpOrderCode;
	}

	public String getSfdcAccountId() {
		return this.sfdcAccountId;
	}

	public void setSfdcAccountId(String sfdcAccountId) {
		this.sfdcAccountId = sfdcAccountId;
	}

	public String getTpsCrmCofId() {
		return this.tpsCrmCofId;
	}

	public void setTpsCrmCofId(String tpsCrmCofId) {
		this.tpsCrmCofId = tpsCrmCofId;
	}

	public String getTpsCrmOptyId() {
		return this.tpsCrmOptyId;
	}

	public void setTpsCrmOptyId(String tpsCrmOptyId) {
		this.tpsCrmOptyId = tpsCrmOptyId;
	}

	public String getTpsCrmSystem() {
		return this.tpsCrmSystem;
	}

	public void setTpsCrmSystem(String tpsCrmSystem) {
		this.tpsCrmSystem = tpsCrmSystem;
	}

	public String getTpsSapCrnId() {
		return this.tpsSapCrnId;
	}

	public void setTpsSapCrnId(String tpsSapCrnId) {
		this.tpsSapCrnId = tpsSapCrnId;
	}

	public String getTpsSecsId() {
		return this.tpsSecsId;
	}

	public void setTpsSecsId(String tpsSecsId) {
		this.tpsSecsId = tpsSecsId;
	}

	public String getTpsSfdcCuid() {
		return this.tpsSfdcCuid;
	}

	public void setTpsSfdcCuid(String tpsSfdcCuid) {
		this.tpsSfdcCuid = tpsSfdcCuid;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Set<ScContractInfoBean> getScContractInfos() {
		return this.scContractInfos;
	}

	public void setscContractInfos(Set<ScContractInfoBean> scContractInfos) {
		this.scContractInfos = scContractInfos;
	}

	public ScContractInfoBean addScContractInfo(ScContractInfoBean scContractInfo) {
		getScContractInfos().add(scContractInfo);
		scContractInfo.setScOrder(this);
		return scContractInfo;
	}

	public ScContractInfoBean removeScContractInfo(ScContractInfoBean scContractInfo) {
		getScContractInfos().remove(scContractInfo);
		scContractInfo.setScOrder(null);
		return scContractInfo;
	}

	public Set<ScOrderAttributeBean> getScOrderAttributes() {
		return this.scOrderAttributes;
	}

	public void setScOrderAttributes(Set<ScOrderAttributeBean> scOrderAttributes) {
		this.scOrderAttributes = scOrderAttributes;
	}

	public ScOrderAttributeBean addScOrderAttribute(ScOrderAttributeBean scOrderAttribute) {
		getScOrderAttributes().add(scOrderAttribute);
		scOrderAttribute.setScOrder(this);
		return scOrderAttribute;
	}

	public ScOrderAttributeBean removeScOrderAttribute(ScOrderAttributeBean scOrderAttribute) {
		getScOrderAttributes().remove(scOrderAttribute);
		scOrderAttribute.setScOrder(null);
		return scOrderAttribute;
	}

	public Set<ScServiceDetailBean> getScServiceDetails() {
		return this.scServiceDetails;
	}

	public void setScServiceDetails(Set<ScServiceDetailBean> scServiceDetails) {
		this.scServiceDetails = scServiceDetails;
	}

	public Integer getErfOrderId() {
		return erfOrderId;
	}

	public void setErfOrderId(Integer erfOrderId) {
		this.erfOrderId = erfOrderId;
	}

	public String getSfdcOptyId() {
		return sfdcOptyId;
	}

	public void setSfdcOptyId(String sfdcOptyId) {
		this.sfdcOptyId = sfdcOptyId;
	}

	public ScServiceDetailBean addScServiceDetail(ScServiceDetailBean scServiceDetail) {
		getScServiceDetails().add(scServiceDetail);
		scServiceDetail.setScOrder(this);
		return scServiceDetail;
	}

	public ScServiceDetailBean removeScServiceDetail(ScServiceDetailBean scServiceDetail) {
		getScServiceDetails().remove(scServiceDetail);
		scServiceDetail.setScOrder(null);
		return scServiceDetail;
	}

	public Set<ScCommercialBean> getScCommercialBean() {
		return scCommercialBean;
	}

	public void setScCommercialBean(Set<ScCommercialBean> scCommercialBean) {
		this.scCommercialBean = scCommercialBean;
	}

	public Integer getErfCustPartnerLeId() {
		return erfCustPartnerLeId;
	}

	public void setErfCustPartnerLeId(Integer erfCustPartnerLeId) {
		this.erfCustPartnerLeId = erfCustPartnerLeId;
	}

	public String getPartnerCuid() {
		return partnerCuid;
	}

	public void setPartnerCuid(String partnerCuid) {
		this.partnerCuid = partnerCuid;
	}
	
	/*
	 * public String getOrderSubCategory() { 
	 * 	return orderSubCategory; 
	 * }
	 * 
	 * public void setOrderSubCategory(String orderSubCategory) {
	 * 	this.orderSubCategory = orderSubCategory; 
	 * }
	 */
}