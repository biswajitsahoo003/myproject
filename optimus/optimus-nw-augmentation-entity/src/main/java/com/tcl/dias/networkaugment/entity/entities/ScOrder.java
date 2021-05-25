package com.tcl.dias.networkaugment.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.*;

/**
 *
 * This file contains the ScOrder.java class.
 *
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "sc_order")
@NamedQuery(name = "ScOrder.findAll", query = "SELECT s FROM ScOrder s")
public class ScOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_date")
	private Timestamp createdDate;

	@Column(name = "customer_group_name")
	private String customerGroupName;

	@Column(name = "customer_segment")
	private String customerSegment;

	@Column(name = "demo_flag")
	private String demoFlag;

	@Column(name = "erf_cust_customer_id")
	private Integer erfCustCustomerId;

	@Column(name = "erf_cust_customer_name")
	private String erfCustCustomerName;

	@Column(name = "erf_cust_le_id")
	private Integer erfCustLeId;

	@Column(name = "erf_cust_le_name")
	private String erfCustLeName;

	@Column(name = "erf_cust_partner_id")
	private Integer erfCustPartnerId;

	@Column(name = "erf_cust_partner_name")
	private String erfCustPartnerName;

	@Column(name = "erf_cust_sp_le_id")
	private Integer erfCustSpLeId;

	@Column(name = "erf_cust_sp_le_name")
	private String erfCustSpLeName;

	@Column(name = "erf_user_customer_user_id")
	private Integer erfUserCustomerUserId;

	@Column(name = "erf_user_initiator_id")
	private Integer erfUserInitiatorId;

	@Column(name = "is_active")
	private String isActive = "Y";

	@Column(name = "is_migrated_order")
	private String isMigratedOrder;

	@Column(name = "is_bundle_order")
	private String isBundleOrder;

	@Column(name = "is_multiple_le")
	private String isMultipleLe;

	@Column(name = "last_macd_date")
	private Timestamp lastMacdDate;

	@Column(name = "macd_created_date")
	private Timestamp macdCreatedDate;

	@Column(name = "op_order_code")
	private String opOrderCode;

	@Column(name = "opportunity_classification")
	private String opportunityClassification;

	@Column(name = "order_category")
	private String orderCategory;

	/*@Column(name = "order_sub_category")
	private String orderSubCategory;*/

	@Column(name = "order_end_date")
	private Timestamp orderEndDate;

	@Column(name = "order_source")
	private String orderSource;

	@Column(name = "order_start_date")
	private Timestamp orderStartDate;

	@Column(name = "order_status")
	private String orderStatus;

	@Column(name = "order_type")
	private String orderType;

	@Column(name = "parent_id")
	private Integer parentId;

	@Column(name = "parent_op_order_code")
	private String parentOpOrderCode;

	@Column(name = "sfdc_account_id")
	private String sfdcAccountId;

	@Column(name = "tps_crm_cof_id")
	private String tpsCrmCofId;

	@Column(name = "tps_crm_opty_id")
	private String tpsCrmOptyId;

	@Column(name = "tps_crm_system")
	private String tpsCrmSystem;

	@Column(name = "tps_sap_crn_id")
	private String tpsSapCrnId;

	@Column(name = "tps_secs_id")
	private String tpsSecsId;

	@Column(name = "tps_sfdc_cuid")
	private String tpsSfdcCuid;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_date")
	private Timestamp updatedDate;

	@Column(name = "erf_order_id")
	private Integer erfOrderId;

	@Column(name = "tps_sfdc_opty_id")
	private String tpsSfdcOptyId;

	private String uuid;

	@Column(name = "erf_order_le_id")
	private Integer erfOrderLeId;

	//Columns required for Network Augmentation
	@Column(name = "bts_order_id")
	private String btsOrderId;

	@Column(name = "is_iprm_required")
	private String isIprmRequired;

	@Column(name = "is_pe_required")
	private String isPeRequired;

	@Column(name = "m_and_l_required")
	private String mAndLRequired;

	@Column(name = "order_creation_date")
	private Timestamp orderCreationDate;

	@Column(name = "originator_contact_number")
	private String originatorContactNumber;

	@Column(name = "originator_group_id")
	private String originatorGroupId;

	@Column(name = "originator_name")
	private String originatorName;

	@Column(name = "pm_contact_email")
	private String pmContactEmail;

	@Column(name = "pm_contact_phone_no")
	private String pmContactPhoneNo;

	@Column(name = "pm_name")
	private String pmName;

	@Column(name = "priority")
	private String priority;

	@Column(name = "project_type")
	private String projectType;

	@Column(name = "process_type")
	private String processType;

	@Column(name = "technology_type")
	private  String technologyType;

	@Column(name = "scenario_type")
	private String scenarioType;

	@Column(name = "subject")
	private String subject;

	@Column(name = "project_name")
	private String projectName;

	@Column(name = "eor_confirmed_by")
	private String eorConfirmedBy;

	@Column(name = "city_tier")
	private String cityTier;

	@Column(name = "rfm_rfs")
	private String rfmRfs;

	@Column(name = "rfm_rfs_assign_to")
	private String rfmRfsAssignTo;

	// bi-directional many-to-one association to ScContractInfo
	@OneToMany(mappedBy = "scOrder")
	private Set<ScContractInfo> scContractInfos1;

	// bi-directional many-to-one association to ScOrderAttribute
	@OneToMany(mappedBy = "scOrder")
	private Set<ScOrderAttribute> scOrderAttributes;

	// bi-directional many-to-one association to ScServiceDetail
	@OneToMany(mappedBy = "scOrder", cascade = CascadeType.ALL)
	private Set<ScServiceDetail> scServiceDetails;

	// bi-directional many-to-one association to Task
	@OneToMany(mappedBy = "scOrder", cascade = CascadeType.ALL)
	private Set<Task> task;

	// bi-directional many-to-one association to ScServiceDetail
	@OneToMany(mappedBy = "scOrder", cascade = CascadeType.ALL)
	private Set<NwaAccessEorDetails> nwaAccessEorDetails;

	// bi-directional many-to-one association to ScServiceDetail
	@OneToMany(mappedBy = "scOrder", cascade = CascadeType.ALL)
	private Set<NwaEorEquipDetails> nwaEorEquipDetails;

	// bi-directional many-to-one association to ScServiceDetail
	@OneToMany(mappedBy = "scOrder", cascade = CascadeType.ALL)
	private Set<NwaOrderDetailsExtnd> nwaOrderDetailsExtnds;

	// bi-directional many-to-one association to ScServiceDetail
	@OneToMany(mappedBy = "scOrder", cascade = CascadeType.ALL)
	private Set<NwaMaterialRequestDetails> nwaMaterialRequestDetails;

	// bi-directional many-to-one association to ScServiceDetail
	@OneToMany(mappedBy = "scOrder", cascade = CascadeType.ALL)
	private  Set<NwaLinkedOrderDetails> nwaLinkedOrderDetails;

	// bi-directional many-to-one association to ScServiceDetail
	@OneToMany(mappedBy = "scOrder", cascade = CascadeType.ALL)
	private Set<NwaRemarksDetails> nwaRemarksDetails;


	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "sc_gst_address_id")
	private ScGstAddress scGstAddress;

	// bi-directional many-to-one association to ScServiceDetail
	@OneToMany(mappedBy = "scOrder", cascade = CascadeType.ALL)
	private  Set<NwaSuspensionReason> nwaSuspensionReasons;

	/*// bi-directional many-to-one association to ScServiceDetail
	@OneToMany(mappedBy = "scOrder", cascade = CascadeType.ALL)
	private  Set<NwaRejectionDetails> nwaRejectionDetails;*/

	// bi-directional many-to-one association to ScServiceDetail
	@OneToMany(mappedBy = "scOrder", cascade = CascadeType.ALL)
	@OrderBy("seqNo asc" )
	private  Set<NwaCardDetails> nwaCardDetails;

	// bi-directional many-to-one association to ScServiceDetail
	@OneToMany(mappedBy = "scOrder", cascade = CascadeType.ALL)
	private  Set<NwaBtsDetails> nwaBtsDetails;

	public ScOrder() {
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

	public Timestamp getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
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

	public Timestamp getLastMacdDate() {
		return this.lastMacdDate;
	}

	public void setLastMacdDate(Timestamp lastMacdDate) {
		this.lastMacdDate = lastMacdDate;
	}

	public Timestamp getMacdCreatedDate() {
		return this.macdCreatedDate;
	}

	public void setMacdCreatedDate(Timestamp macdCreatedDate) {
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

	public Timestamp getOrderEndDate() {
		return this.orderEndDate;
	}

	public void setOrderEndDate(Timestamp orderEndDate) {
		this.orderEndDate = orderEndDate;
	}

	public String getOrderSource() {
		return this.orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public Timestamp getOrderStartDate() {
		return this.orderStartDate;
	}

	public void setOrderStartDate(Timestamp orderStartDate) {
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

	public Timestamp getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Set<ScContractInfo> getScContractInfos1() {
		return this.scContractInfos1;
	}

	public void setScContractInfos1(Set<ScContractInfo> scContractInfos1) {
		this.scContractInfos1 = scContractInfos1;
	}

	public Set<ScOrderAttribute> getScOrderAttributes() {
		return this.scOrderAttributes;
	}

	public void setScOrderAttributes(Set<ScOrderAttribute> scOrderAttributes) {
		this.scOrderAttributes = scOrderAttributes;
	}

	public String getBtsOrderId() {
		return btsOrderId;
	}

	public void setBtsOrderId(String btsOrderId) {
		this.btsOrderId = btsOrderId;
	}

	public String getIsIprmRequired() {
		return isIprmRequired;
	}

	public void setIsIprmRequired(String isIprmRequired) {
		this.isIprmRequired = isIprmRequired;
	}

	public String getIsPeRequired() {
		return isPeRequired;
	}

	public void setIsPeRequired(String isPeRequired) {
		this.isPeRequired = isPeRequired;
	}

	public String getmAndLRequired() {
		return mAndLRequired;
	}

	public void setmAndLRequired(String mAndLRequired) {
		this.mAndLRequired = mAndLRequired;
	}

	public Timestamp getOrderCreationDate() {
		return orderCreationDate;
	}

	public void setOrderCreationDate(Timestamp orderCreationDate) {
		this.orderCreationDate = orderCreationDate;
	}

	public String getOriginatorContactNumber() {
		return originatorContactNumber;
	}

	public void setOriginatorContactNumber(String originatorContactNumber) {
		this.originatorContactNumber = originatorContactNumber;
	}

	public String getOriginatorGroupId() {
		return originatorGroupId;
	}

	public void setOriginatorGroupId(String originatorGroupId) {
		this.originatorGroupId = originatorGroupId;
	}

	public String getOriginatorName() {
		return originatorName;
	}

	public void setOriginatorName(String originatorName) {
		this.originatorName = originatorName;
	}

	public String getPmContactEmail() {
		return pmContactEmail;
	}

	public void setPmContactEmail(String pmContactEmail) {
		this.pmContactEmail = pmContactEmail;
	}

	public String getPmContactPhoneNo() {
		return pmContactPhoneNo;
	}

	public void setPmContactPhoneNo(String pmContactPhoneNo) {
		this.pmContactPhoneNo = pmContactPhoneNo;
	}

	public String getPmName() {
		return pmName;
	}

	public void setPmName(String pmName) {
		this.pmName = pmName;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getScenarioType() {
		return scenarioType;
	}

	public void setScenarioType(String scenarioType) {
		this.scenarioType = scenarioType;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public ScOrderAttribute addScOrderAttribute(ScOrderAttribute scOrderAttribute) {
		getScOrderAttributes().add(scOrderAttribute);
		scOrderAttribute.setScOrder(this);

		return scOrderAttribute;
	}

	public ScOrderAttribute removeScOrderAttribute(ScOrderAttribute scOrderAttribute) {
		getScOrderAttributes().remove(scOrderAttribute);
		scOrderAttribute.setScOrder(null);

		return scOrderAttribute;
	}

	public Set<ScServiceDetail> getScServiceDetails() {
		return this.scServiceDetails;
	}

	public void setScServiceDetails(Set<ScServiceDetail> scServiceDetails) {
		this.scServiceDetails = scServiceDetails;
	}

	public ScServiceDetail addScServiceDetail(ScServiceDetail scServiceDetail) {
		getScServiceDetails().add(scServiceDetail);
		scServiceDetail.setScOrder(this);

		return scServiceDetail;
	}

	public ScServiceDetail removeScServiceDetail(ScServiceDetail scServiceDetail) {
		getScServiceDetails().remove(scServiceDetail);
		scServiceDetail.setScOrder(null);

		return scServiceDetail;
	}

	public Integer getErfOrderId() {
		return erfOrderId;
	}

	public void setErfOrderId(Integer erfOrderId) {
		this.erfOrderId = erfOrderId;
	}

	public String getTpsSfdcOptyId() {
		return tpsSfdcOptyId;
	}

	public void setTpsSfdcOptyId(String tpsSfdcOptyId) {
		this.tpsSfdcOptyId = tpsSfdcOptyId;
	}

	public Integer getErfOrderLeId() {
		return erfOrderLeId;
	}

	public void setErfOrderLeId(Integer erfOrderLeId) {
		this.erfOrderLeId = erfOrderLeId;
	}

	public String getIsMigratedOrder() {
		return isMigratedOrder;
	}

	public void setIsMigratedOrder(String isMigratedOrder) {
		this.isMigratedOrder = isMigratedOrder;
	}

	public ScGstAddress getScGstAddress() {
		return scGstAddress;
	}

	public void setScGstAddress(ScGstAddress scGstAddress) {
		this.scGstAddress = scGstAddress;
	}

	public Set<NwaAccessEorDetails> getNwaAccessEorDetails() {
		return nwaAccessEorDetails;
	}

	public void setNwaAccessEorDetails(Set<NwaAccessEorDetails> nwaAccessEorDetails) {
		this.nwaAccessEorDetails = nwaAccessEorDetails;
	}

	public Set<NwaEorEquipDetails> getNwaEorEquipDetails() {
		return nwaEorEquipDetails;
	}

	public void setNwaEorEquipDetails(Set<NwaEorEquipDetails> nwaEorEquipDetails) {
		this.nwaEorEquipDetails = nwaEorEquipDetails;
	}

	public Set<NwaOrderDetailsExtnd> getNwaOrderDetailsExtnds() {
		return nwaOrderDetailsExtnds;
	}

	public void setNwaOrderDetailsExtnds(Set<NwaOrderDetailsExtnd> nwaOrderDetailsExtnds) {
		this.nwaOrderDetailsExtnds = nwaOrderDetailsExtnds;
	}

	public Set<NwaMaterialRequestDetails> getNwaMaterialRequestDetails() {
		return nwaMaterialRequestDetails;
	}

	public void setNwaMaterialRequestDetails(Set<NwaMaterialRequestDetails> nwaMaterialRequestDetails) {
		this.nwaMaterialRequestDetails = nwaMaterialRequestDetails;
	}

	public Set<NwaLinkedOrderDetails> getNwaLinkedOrderDetails() {
		return nwaLinkedOrderDetails;
	}

	public void setNwaLinkedOrderDetails(Set<NwaLinkedOrderDetails> nwaLinkedOrderDetails) {
		this.nwaLinkedOrderDetails = nwaLinkedOrderDetails;
	}

	public Set<NwaRemarksDetails> getNwaRemarksDetails() {
		return nwaRemarksDetails;
	}

	public void setNwaRemarksDetails(Set<NwaRemarksDetails> nwaRemarksDetails) {
		this.nwaRemarksDetails = nwaRemarksDetails;
	}

	public Set<NwaSuspensionReason> getNwaSuspensionReasons() {
		return nwaSuspensionReasons;
	}

	public void setNwaSuspensionReasons(Set<NwaSuspensionReason> nwaSuspensionReasons) {
		this.nwaSuspensionReasons = nwaSuspensionReasons;
	}

	public String getTechnologyType() {
		return technologyType;
	}

	public void setTechnologyType(String technologyType) {
		this.technologyType = technologyType;
	}

	/*public String getOrderSubCategory() {
		return orderSubCategory;
	}

	public void setOrderSubCategory(String orderSubCategory) {
		this.orderSubCategory = orderSubCategory;
	}*/

	public Set<Task> getTask() {
		return task;
	}

	public void setTask(Set<Task> task) {
		this.task = task;
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}

	public Set<NwaCardDetails> getNwaCardDetails() {
		return nwaCardDetails;
	}

	public void setNwaCardDetails(Set<NwaCardDetails> nwaCardDetails) {
		this.nwaCardDetails = nwaCardDetails;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getEorConfirmedBy() {
		return eorConfirmedBy;
	}

	public void setEorConfirmedBy(String eorConfirmedBy) {
		this.eorConfirmedBy = eorConfirmedBy;
	}

	public String getCityTier() {
		return cityTier;
	}

	public void setCityTier(String cityTier) {
		this.cityTier = cityTier;
	}

	public String getRfmRfs() {
		return rfmRfs;
	}

	public void setRfmRfs(String rfmRfs) {
		this.rfmRfs = rfmRfs;
	}

	public String getRfmRfsAssignTo() {
		return rfmRfsAssignTo;
	}

	public void setRfmRfsAssignTo(String rfmRfsAssignTo) {
		this.rfmRfsAssignTo = rfmRfsAssignTo;
	}

	public Set<NwaBtsDetails> getNwaBtsDetails() {
		return nwaBtsDetails;
	}

	public void setNwaBtsDetails(Set<NwaBtsDetails> nwaBtsDetails) {
		this.nwaBtsDetails = nwaBtsDetails;
	}
}