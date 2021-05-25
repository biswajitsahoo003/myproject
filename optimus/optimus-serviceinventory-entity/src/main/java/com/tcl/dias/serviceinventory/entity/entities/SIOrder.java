package com.tcl.dias.serviceinventory.entity.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;


/**
 * 
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * The persistent class for the si_order database table.
 * 
 */
@Entity
@Table(name="si_order")
public class SIOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="created_date")
	private Timestamp createdDate;

	@Column(name="customer_group_name")
	private String customerGroupName;

	@Column(name="customer_segment")
	private String customerSegment;

	@Column(name="demo_flag")
	private String demoFlag;

	@Column(name="erf_cust_customer_id")
	private String erfCustCustomerId;

	@Column(name="erf_cust_customer_name")
	private String erfCustCustomerName;

	@Column(name="erf_cust_le_id")
	private Integer erfCustLeId;

	@Column(name="erf_cust_le_name")
	private String erfCustLeName;

	@Column(name="erf_cust_partner_id")
	private Integer erfCustPartnerId;

	@Column(name="erf_cust_partner_le_id")
	private Integer erfCustPartnerLeId;

	@Column(name="erf_cust_partner_name")
	private String erfCustPartnerName;

	@Column(name="erf_cust_sp_le_id")
	private Integer erfCustSpLeId;

	@Column(name="erf_cust_sp_le_name")
	private String erfCustSpLeName;

	@Column(name="erf_user_customer_user_id")
	private Integer erfUserCustomerUserId;

	@Column(name="erf_user_initiator_id")
	private Integer erfUserInitiatorId;

	@Column(name="is_active")
	private String isActive;

	@Column(name="is_bundle_order")
	private String isBundleOrder;

	@Column(name="is_multiple_le")
	private String isMultipleLe;

	@Column(name="last_macd_date")
	private Timestamp lastMacdDate;

	@Column(name="macd_created_date")
	private Timestamp macdCreatedDate;

	@Column(name="op_order_code")
	private String opOrderCode;

	@Column(name="opportunity_classification")
	private String opportunityClassification;

	@Column(name="order_category")
	private String orderCategory;

	/*@Column(name = "order_sub_category")
	private String orderSubCategory;*/

	@Column(name="order_end_date")
	private Timestamp orderEndDate;

	@Column(name="order_source")
	private String orderSource;

	@Column(name="order_start_date")
	private Timestamp orderStartDate;

	@Column(name="order_status")
	private String orderStatus;

	@Column(name="order_type")
	private String orderType;

	@Column(name="parent_id")
	private Integer parentId;

	@Column(name="parent_op_order_code")
	private String parentOpOrderCode;

	@Column(name="sfdc_account_id")
	private String sfdcAccountId;

	@Column(name="tps_crm_cof_id")
	private String tpsCrmCofId;

	@Column(name="tps_crm_opty_id")
	private String tpsCrmOptyId;

	@Column(name="tps_crm_system")
	private String tpsCrmSystem;

	@Column(name="tps_sap_crn_id")
	private String tpsSapCrnId;

	@Column(name="tps_secs_id")
	private String tpsSecsId;

	@Column(name="tps_sfdc_cuid")
	private String tpsSfdcCuid;

	@Column(name="updated_by")
	private String updatedBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;

	@Column(name="partner_cuid")
	private String partnerCuid;

	private String uuid;

	@Column(name = "erf_order_le_id")
	private Integer erfOrderLeId;

	@Column(name = "erf_order_id")
	private Integer erfOrderId;

	//bi-directional many-to-one association to SiContractInfo
	@OneToMany(mappedBy="siOrder")
	private Set<SIContractInfo> siContractInfos;

	//bi-directional many-to-one association to SiOrderAttribute
	@OneToMany(mappedBy="siOrder")
	private Set<SIOrderAttribute> siOrderAttributes;

	//bi-directional many-to-one association to SiServiceDetail
	@OneToMany(mappedBy="siOrder")
	private Set<SIServiceDetail> siServiceDetails;
	
	public Integer getErfOrderId() {
		return erfOrderId;
	}

	public void setErfOrderId(Integer erfOrderId) {
		this.erfOrderId = erfOrderId;
	}

	public SIOrder() {
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

	public String getErfCustCustomerId() {
		return this.erfCustCustomerId;
	}

	public void setErfCustCustomerId(String erfCustCustomerId) {
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

	public Set<SIContractInfo> getSiContractInfos() {
		return this.siContractInfos;
	}

	public void setSiContractInfos(Set<SIContractInfo> siContractInfos) {
		this.siContractInfos = siContractInfos;
	}
	
	
	public Set<SIOrderAttribute> getSiOrderAttributes() {
		return this.siOrderAttributes;
	}

	public void setSiOrderAttributes(Set<SIOrderAttribute> siOrderAttributes) {
		this.siOrderAttributes = siOrderAttributes;
	}
	
	public Set<SIServiceDetail> getSiServiceDetails() {
		return this.siServiceDetails;
	}

	public void setSiServiceDetails(Set<SIServiceDetail> siServiceDetails) {
		this.siServiceDetails = siServiceDetails;
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

	public Integer getErfOrderLeId() {
		return erfOrderLeId;
	}

	public void setErfOrderLeId(Integer erfOrderLeId) {
		this.erfOrderLeId = erfOrderLeId;
	}

	/*public String getOrderSubCategory() {
		return orderSubCategory;
	}

	public void setOrderSubCategory(String orderSubCategory) {
		this.orderSubCategory = orderSubCategory;
	}*/
	
	
}