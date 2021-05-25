package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


/**
 * 
 * WimaxLastmile Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="wimax_lastmile")
@NamedQuery(name="WimaxLastmile.findAll", query="SELECT w FROM WimaxLastmile w")
public class WimaxLastmile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="wimax_lastmile_id")
	private Integer wimaxLastmileId;

	@Column(name="bts_ip")
	private String btsIp;

	@Column(name="bts_name")
	private String btsName;

	@Column(name="custstatic_wanip")
	private String custstaticWanip;

	@Column(name="custstatic_wanip_custprovided")
	private Byte custstaticWanipCustprovided;

	@Column(name="custstatic_wanip_gateway")
	private String custstaticWanipGateway;

	@Column(name="custstatic_wanip_gateway_custprovided")
	private Byte custstaticWanipGatewayCustprovided;

	@Column(name="custstatic_wanip_mask")
	private String custstaticWanipMask;

	@Column(name="custstatic_wanip_mask_custprovided")
	private Byte custstaticWanipMaskCustprovided;

	private String description1;

	private String description2;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="gatemgmtip_custprovided")
	private Byte gatemgmtipCustprovided;

	@Column(name="gateway_mgmt_ip")
	private String gatewayMgmtIp;

	@Column(name="home_region")
	private String homeRegion;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="port_speed")
	private String portSpeed;

	@Column(name="port_speed_unit")
	private String portSpeedUnit;

	@Column(name="provisioning_mode")
	private String provisioningMode;

	@Column(name="ssvlan_tagging")
	private Byte ssvlanTagging;

	@Column(name="start_date")
	private Timestamp startDate;

	@Column(name="su_mgmt_ip")
	private String suMgmtIp;

	@Column(name="su_mgmt_subnet")
	private String suMgmtSubnet;

	@Column(name="sumac_address")
	private String sumacAddress;

	@Column(name="sumgmtip_custprovided")
	private Byte sumgmtipCustprovided;

	@Column(name="sumgmtsubnet_custprovided")
	private Byte sumgmtsubnetCustprovided;

	@Column(name="unique_name")
	private String uniqueName;

	//bi-directional many-to-one association to VlanQosProfile
	@OneToMany(mappedBy="wimaxLastmile")
	private Set<VlanQosProfile> vlanQosProfiles;

	//bi-directional many-to-one association to LmComponent
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="lm_component_lm_component_id")
	private LmComponent lmComponent;

	public WimaxLastmile() {
	}

	public Integer getWimaxLastmileId() {
		return this.wimaxLastmileId;
	}

	public void setWimaxLastmileId(Integer wimaxLastmileId) {
		this.wimaxLastmileId = wimaxLastmileId;
	}

	public String getBtsIp() {
		return this.btsIp;
	}

	public void setBtsIp(String btsIp) {
		this.btsIp = btsIp;
	}

	public String getBtsName() {
		return this.btsName;
	}

	public void setBtsName(String btsName) {
		this.btsName = btsName;
	}

	public String getCuststaticWanip() {
		return this.custstaticWanip;
	}

	public void setCuststaticWanip(String custstaticWanip) {
		this.custstaticWanip = custstaticWanip;
	}

	public Byte getCuststaticWanipCustprovided() {
		return this.custstaticWanipCustprovided;
	}

	public void setCuststaticWanipCustprovided(Byte custstaticWanipCustprovided) {
		this.custstaticWanipCustprovided = custstaticWanipCustprovided;
	}

	public String getCuststaticWanipGateway() {
		return this.custstaticWanipGateway;
	}

	public void setCuststaticWanipGateway(String custstaticWanipGateway) {
		this.custstaticWanipGateway = custstaticWanipGateway;
	}

	public Byte getCuststaticWanipGatewayCustprovided() {
		return this.custstaticWanipGatewayCustprovided;
	}

	public void setCuststaticWanipGatewayCustprovided(Byte custstaticWanipGatewayCustprovided) {
		this.custstaticWanipGatewayCustprovided = custstaticWanipGatewayCustprovided;
	}

	public String getCuststaticWanipMask() {
		return this.custstaticWanipMask;
	}

	public void setCuststaticWanipMask(String custstaticWanipMask) {
		this.custstaticWanipMask = custstaticWanipMask;
	}

	public Byte getCuststaticWanipMaskCustprovided() {
		return this.custstaticWanipMaskCustprovided;
	}

	public void setCuststaticWanipMaskCustprovided(Byte custstaticWanipMaskCustprovided) {
		this.custstaticWanipMaskCustprovided = custstaticWanipMaskCustprovided;
	}

	public String getDescription1() {
		return this.description1;
	}

	public void setDescription1(String description1) {
		this.description1 = description1;
	}

	public String getDescription2() {
		return this.description2;
	}

	public void setDescription2(String description2) {
		this.description2 = description2;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Byte getGatemgmtipCustprovided() {
		return this.gatemgmtipCustprovided;
	}

	public void setGatemgmtipCustprovided(Byte gatemgmtipCustprovided) {
		this.gatemgmtipCustprovided = gatemgmtipCustprovided;
	}

	public String getGatewayMgmtIp() {
		return this.gatewayMgmtIp;
	}

	public void setGatewayMgmtIp(String gatewayMgmtIp) {
		this.gatewayMgmtIp = gatewayMgmtIp;
	}

	public String getHomeRegion() {
		return this.homeRegion;
	}

	public void setHomeRegion(String homeRegion) {
		this.homeRegion = homeRegion;
	}

	public Timestamp getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getModifiedBy() {
		return this.modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getPortSpeed() {
		return this.portSpeed;
	}

	public void setPortSpeed(String portSpeed) {
		this.portSpeed = portSpeed;
	}

	public String getPortSpeedUnit() {
		return this.portSpeedUnit;
	}

	public void setPortSpeedUnit(String portSpeedUnit) {
		this.portSpeedUnit = portSpeedUnit;
	}

	public String getProvisioningMode() {
		return this.provisioningMode;
	}

	public void setProvisioningMode(String provisioningMode) {
		this.provisioningMode = provisioningMode;
	}

	public Byte getSsvlanTagging() {
		return this.ssvlanTagging;
	}

	public void setSsvlanTagging(Byte ssvlanTagging) {
		this.ssvlanTagging = ssvlanTagging;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getSuMgmtIp() {
		return this.suMgmtIp;
	}

	public void setSuMgmtIp(String suMgmtIp) {
		this.suMgmtIp = suMgmtIp;
	}

	public String getSuMgmtSubnet() {
		return this.suMgmtSubnet;
	}

	public void setSuMgmtSubnet(String suMgmtSubnet) {
		this.suMgmtSubnet = suMgmtSubnet;
	}

	public String getSumacAddress() {
		return this.sumacAddress;
	}

	public void setSumacAddress(String sumacAddress) {
		this.sumacAddress = sumacAddress;
	}

	public Byte getSumgmtipCustprovided() {
		return this.sumgmtipCustprovided;
	}

	public void setSumgmtipCustprovided(Byte sumgmtipCustprovided) {
		this.sumgmtipCustprovided = sumgmtipCustprovided;
	}

	public Byte getSumgmtsubnetCustprovided() {
		return this.sumgmtsubnetCustprovided;
	}

	public void setSumgmtsubnetCustprovided(Byte sumgmtsubnetCustprovided) {
		this.sumgmtsubnetCustprovided = sumgmtsubnetCustprovided;
	}

	public String getUniqueName() {
		return this.uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	public Set<VlanQosProfile> getVlanQosProfiles() {
		return this.vlanQosProfiles;
	}

	public void setVlanQosProfiles(Set<VlanQosProfile> vlanQosProfiles) {
		this.vlanQosProfiles = vlanQosProfiles;
	}

	public VlanQosProfile addVlanQosProfile(VlanQosProfile vlanQosProfile) {
		getVlanQosProfiles().add(vlanQosProfile);
		vlanQosProfile.setWimaxLastmile(this);

		return vlanQosProfile;
	}

	public VlanQosProfile removeVlanQosProfile(VlanQosProfile vlanQosProfile) {
		getVlanQosProfiles().remove(vlanQosProfile);
		vlanQosProfile.setWimaxLastmile(null);

		return vlanQosProfile;
	}

	public LmComponent getLmComponent() {
		return this.lmComponent;
	}

	public void setLmComponent(LmComponent lmComponent) {
		this.lmComponent = lmComponent;
	}

}