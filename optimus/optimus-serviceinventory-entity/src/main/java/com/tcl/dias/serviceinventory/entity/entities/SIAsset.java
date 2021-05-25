package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;


/**
 * 
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * The persistent class for the si_asset database table.
 * 
 */
@Entity
@Table(name="si_asset")
public class SIAsset implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(name="asset_group_id")
	private String assetGroupId;

	@Column(name="asset_group_type")
	private String assetGroupType;

	@Column(name="asset_status")
	private String assetStatus;

	@Column(name="asset_tag")
	private String assetTag;

	@Column(name="circuit_id")
	private String circuitId;

	@Column(name="commissioned_date")
	private Timestamp commissionedDate;

	@Column(name="contract_end_date")
	private Timestamp contractEndDate;

	@Column(name="contract_start_date")
	private Timestamp contractStartDate;

	@Column(name="created_by")
	private String createdBy;

	@Column(name="created_date")
	private Timestamp createdDate;

	private String description;

	@Column(name="erf_customer_id")
	private Integer erfCustomerId;

	@Column(name="erf_loc_location_id")
	private Integer erfLocLocationId;

	@Column(name="erf_partner_id")
	private Integer erfPartnerId;

	private String fqdn;

	@Column(name="gateway_ip")
	private String gatewayIp;

	@Column(name="mac_id")
	private String macId;

	@Column(name="managed_by")
	private String managedBy;

	@Column(name="management_ip")
	private String managementIp;

	private String model;

	@Column(name="monitoring_tool")
	private String monitoringTool;

	private String name;

	@Column(name="oem_vendor")
	private String oemVendor;

	private String owner;

	@Column(name="parent_asset_tag")
	private String parentAssetTag;

	@Column(name="parent_id")
	private Integer parentId;

	@Column(name="public_ip")
	private String publicIp;

	@Column(name="serial_no")
	private String serialNo;

	@Column(name="support_end_date")
	private Timestamp supportEndDate;

	@Column(name="support_start_date")
	private Timestamp supportStartDate;

	@Column(name="support_status")
	private String supportStatus;
	
	@Column(name="support_type")
	private String supportType;

	@Column(name="support_vendor")
	private String supportVendor;

	@Column(name="termination_date")
	private Timestamp terminationDate;

	private String type;

	@Column(name="updated_by")
	private String updatedBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;
	
	@Column(name = "is_active")
	private String isActive;

	@Column(name = "cpe_managed")
	private String cpeManaged;

	@Column(name = "cpe_provider")
	private String cpeProvider;

	@Column(name = "date_of_installation")
	private Timestamp dateOfInstallation;

	//bi-directional many-to-one association to SiAssetAttribute
	@OneToMany(mappedBy="siAsset", cascade = CascadeType.ALL)
	private Set<SIAssetAttribute> siAssetAttributes;

	//bi-directional many-to-one association to SiAssetToService
	@OneToMany(mappedBy="siAsset", cascade = CascadeType.ALL)
	private Set<SIAssetToService> siAssetToServices;
	
	//bi-directional many-to-one association to SIAssetCommercial
	@OneToMany(mappedBy="siAsset", cascade = CascadeType.ALL)
	private List<SIAssetCommercial> siAssetCommercials;
	
	//bi-directional many-to-one association to SIServiceDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SI_service_detail_id")
	private SIServiceDetail siServiceDetail;
	
	@Column(name="business_unit")
	private String businessUnit;
	
	@Column(name = "cloud_code")
	private String cloudCode;

	@Column(name = "parent_cloud_code")
	private String parentCloudCode;
	
	private String zone;
	
	private String environment;

	@Column(name= "scope_of_management")
	private String scopeOfManagement;

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

    public SIAsset() {
    }

	public Integer getId() {
		return this.id;
	}

	public String getAssetGroupId() {
		return this.assetGroupId;
	}

	public void setAssetGroupId(String assetGroupId) {
		this.assetGroupId = assetGroupId;
	}

	public String getAssetGroupType() {
		return this.assetGroupType;
	}

	public void setAssetGroupType(String assetGroupType) {
		this.assetGroupType = assetGroupType;
	}

	public String getAssetStatus() {
		return this.assetStatus;
	}

	public void setAssetStatus(String assetStatus) {
		this.assetStatus = assetStatus;
	}

	public String getAssetTag() {
		return this.assetTag;
	}

	public void setAssetTag(String assetTag) {
		this.assetTag = assetTag;
	}

	public String getCircuitId() {
		return this.circuitId;
	}

	public void setCircuitId(String circuitId) {
		this.circuitId = circuitId;
	}

	public Timestamp getCommissionedDate() {
		return this.commissionedDate;
	}

	public void setCommissionedDate(Timestamp commissionedDate) {
		this.commissionedDate = commissionedDate;
	}

	public Timestamp getContractEndDate() {
		return this.contractEndDate;
	}

	public void setContractEndDate(Timestamp contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public Timestamp getContractStartDate() {
		return this.contractStartDate;
	}

	public void setContractStartDate(Timestamp contractStartDate) {
		this.contractStartDate = contractStartDate;
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getErfCustomerId() {
		return this.erfCustomerId;
	}

	public void setErfCustomerId(Integer erfCustomerId) {
		this.erfCustomerId = erfCustomerId;
	}

	public Integer getErfLocLocationId() {
		return this.erfLocLocationId;
	}

	public void setErfLocLocationId(Integer erfLocLocationId) {
		this.erfLocLocationId = erfLocLocationId;
	}

	public Integer getErfPartnerId() {
		return this.erfPartnerId;
	}

	public void setErfPartnerId(Integer erfPartnerId) {
		this.erfPartnerId = erfPartnerId;
	}

	public String getFqdn() {
		return this.fqdn;
	}

	public void setFqdn(String fqdn) {
		this.fqdn = fqdn;
	}

	public String getGatewayIp() {
		return this.gatewayIp;
	}

	public void setGatewayIp(String gatewayIp) {
		this.gatewayIp = gatewayIp;
	}

	public String getMacId() {
		return this.macId;
	}

	public void setMacId(String macId) {
		this.macId = macId;
	}

	public String getManagedBy() {
		return this.managedBy;
	}

	public void setManagedBy(String managedBy) {
		this.managedBy = managedBy;
	}

	public String getManagementIp() {
		return this.managementIp;
	}

	public void setManagementIp(String managementIp) {
		this.managementIp = managementIp;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getMonitoringTool() {
		return this.monitoringTool;
	}

	public void setMonitoringTool(String monitoringTool) {
		this.monitoringTool = monitoringTool;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOemVendor() {
		return this.oemVendor;
	}

	public void setOemVendor(String oemVendor) {
		this.oemVendor = oemVendor;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getParentAssetTag() {
		return this.parentAssetTag;
	}

	public void setParentAssetTag(String parentAssetTag) {
		this.parentAssetTag = parentAssetTag;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getPublicIp() {
		return this.publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public String getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Timestamp getSupportEndDate() {
		return this.supportEndDate;
	}

	public void setSupportEndDate(Timestamp supportEndDate) {
		this.supportEndDate = supportEndDate;
	}

	public Timestamp getSupportStartDate() {
		return this.supportStartDate;
	}

	public void setSupportStartDate(Timestamp supportStartDate) {
		this.supportStartDate = supportStartDate;
	}

	public String getSupportStatus() {
		return this.supportStatus;
	}

	public void setSupportStatus(String supportStatus) {
		this.supportStatus = supportStatus;
	}

	public String getSupportVendor() {
		return this.supportVendor;
	}

	public void setSupportVendor(String supportVendor) {
		this.supportVendor = supportVendor;
	}

	public Timestamp getTerminationDate() {
		return this.terminationDate;
	}

	public void setTerminationDate(Timestamp terminationDate) {
		this.terminationDate = terminationDate;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Set<SIAssetAttribute> getSiAssetAttributes() {
		return this.siAssetAttributes;
	}

	public void setSiAssetAttributes(Set<SIAssetAttribute> siAssetAttributes) {
		this.siAssetAttributes = siAssetAttributes;
	}
	
	public Set<SIAssetToService> getSiAssetToServices() {
		return this.siAssetToServices;
	}

	public void setSiAssetToServices(Set<SIAssetToService> siAssetToServices) {
		this.siAssetToServices = siAssetToServices;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<SIAssetCommercial> getSiAssetCommercials() {
		return siAssetCommercials;
	}

	public void setSiAssetCommercials(List<SIAssetCommercial> siAssetCommercials) {
		this.siAssetCommercials = siAssetCommercials;
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

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public SIServiceDetail getSiServiceDetail() {
		return siServiceDetail;
	}

	public void setSiServiceDetail(SIServiceDetail siServiceDetail) {
		this.siServiceDetail = siServiceDetail;
	}

	public String getCpeManaged() {
		return cpeManaged;
	}

	public void setCpeManaged(String cpeManaged) {
		this.cpeManaged = cpeManaged;
	}

	public String getScopeOfManagement() {
		return scopeOfManagement;
	}

	public void setScopeOfManagement(String scopeOfManagement) {
		this.scopeOfManagement = scopeOfManagement;
	}

	public String getCpeProvider() {
		return cpeProvider;
	}

	public void setCpeProvider(String cpeProvider) {
		this.cpeProvider = cpeProvider;
	}

	public Timestamp getDateOfInstallation() {
		return dateOfInstallation;
	}

	public void setDateOfInstallation(Timestamp dateOfInstallation) {
		this.dateOfInstallation = dateOfInstallation;
	}

	public String getSupportType() {
		return supportType;
	}

	public void setSupportType(String supportType) {
		this.supportType = supportType;
	}
	
}