package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.OneToMany;


/**
 * 
 * WimaxLastmile Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class WimaxLastmileBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer wimaxLastmileId;
	private String btsIp;
	private String btsName;
	private String custstaticWanip;
	private Byte custstaticWanipCustprovided;
	private String custstaticWanipGateway;
	private Byte custstaticWanipGatewayCustprovided;
	private String custstaticWanipMask;
	private Byte custstaticWanipMaskCustprovided;
	private String description1;
	private String description2;
	private Timestamp endDate;
	private Byte gatemgmtipCustprovided;
	private String gatewayMgmtIp;
	private String homeRegion;
	private Timestamp lastModifiedDate;
	private String modifiedBy;
	private String portSpeed;
	private String portSpeedUnit;
	private String provisioningMode;
	private Byte ssvlanTagging;
	private Timestamp startDate;
	private String suMgmtIp;
	private String suMgmtSubnet;
	private String sumacAddress;
	private Byte sumgmtipCustprovided;
	private Byte sumgmtsubnetCustprovided;
	private String uniqueName;
	private boolean isEdited;

	
	@OneToMany
	private Set<VlanQosProfileBean> vlanQosProfiles;


	public Integer getWimaxLastmileId() {
		return wimaxLastmileId;
	}


	public void setWimaxLastmileId(Integer wimaxLastmileId) {
		this.wimaxLastmileId = wimaxLastmileId;
	}


	public String getBtsIp() {
		return btsIp;
	}


	public void setBtsIp(String btsIp) {
		this.btsIp = btsIp;
	}


	public String getBtsName() {
		return btsName;
	}


	public void setBtsName(String btsName) {
		this.btsName = btsName;
	}


	public String getCuststaticWanip() {
		return custstaticWanip;
	}


	public void setCuststaticWanip(String custstaticWanip) {
		this.custstaticWanip = custstaticWanip;
	}


	public Byte getCuststaticWanipCustprovided() {
		return custstaticWanipCustprovided;
	}


	public void setCuststaticWanipCustprovided(Byte custstaticWanipCustprovided) {
		this.custstaticWanipCustprovided = custstaticWanipCustprovided;
	}


	public String getCuststaticWanipGateway() {
		return custstaticWanipGateway;
	}


	public void setCuststaticWanipGateway(String custstaticWanipGateway) {
		this.custstaticWanipGateway = custstaticWanipGateway;
	}


	public Byte getCuststaticWanipGatewayCustprovided() {
		return custstaticWanipGatewayCustprovided;
	}


	public void setCuststaticWanipGatewayCustprovided(Byte custstaticWanipGatewayCustprovided) {
		this.custstaticWanipGatewayCustprovided = custstaticWanipGatewayCustprovided;
	}


	public String getCuststaticWanipMask() {
		return custstaticWanipMask;
	}


	public void setCuststaticWanipMask(String custstaticWanipMask) {
		this.custstaticWanipMask = custstaticWanipMask;
	}


	public Byte getCuststaticWanipMaskCustprovided() {
		return custstaticWanipMaskCustprovided;
	}


	public void setCuststaticWanipMaskCustprovided(Byte custstaticWanipMaskCustprovided) {
		this.custstaticWanipMaskCustprovided = custstaticWanipMaskCustprovided;
	}


	public String getDescription1() {
		return description1;
	}


	public void setDescription1(String description1) {
		this.description1 = description1;
	}


	public String getDescription2() {
		return description2;
	}


	public void setDescription2(String description2) {
		this.description2 = description2;
	}


	public Timestamp getEndDate() {
		return endDate;
	}


	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}


	public Byte getGatemgmtipCustprovided() {
		return gatemgmtipCustprovided;
	}


	public void setGatemgmtipCustprovided(Byte gatemgmtipCustprovided) {
		this.gatemgmtipCustprovided = gatemgmtipCustprovided;
	}


	public String getGatewayMgmtIp() {
		return gatewayMgmtIp;
	}


	public void setGatewayMgmtIp(String gatewayMgmtIp) {
		this.gatewayMgmtIp = gatewayMgmtIp;
	}


	public String getHomeRegion() {
		return homeRegion;
	}


	public void setHomeRegion(String homeRegion) {
		this.homeRegion = homeRegion;
	}


	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}


	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}


	public String getModifiedBy() {
		return modifiedBy;
	}


	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}


	public String getPortSpeed() {
		return portSpeed;
	}


	public void setPortSpeed(String portSpeed) {
		this.portSpeed = portSpeed;
	}


	public String getPortSpeedUnit() {
		return portSpeedUnit;
	}


	public void setPortSpeedUnit(String portSpeedUnit) {
		this.portSpeedUnit = portSpeedUnit;
	}


	public String getProvisioningMode() {
		return provisioningMode;
	}


	public void setProvisioningMode(String provisioningMode) {
		this.provisioningMode = provisioningMode;
	}


	public Byte getSsvlanTagging() {
		return ssvlanTagging;
	}


	public void setSsvlanTagging(Byte ssvlanTagging) {
		this.ssvlanTagging = ssvlanTagging;
	}


	public Timestamp getStartDate() {
		return startDate;
	}


	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}


	public String getSuMgmtIp() {
		return suMgmtIp;
	}


	public void setSuMgmtIp(String suMgmtIp) {
		this.suMgmtIp = suMgmtIp;
	}


	public String getSuMgmtSubnet() {
		return suMgmtSubnet;
	}


	public void setSuMgmtSubnet(String suMgmtSubnet) {
		this.suMgmtSubnet = suMgmtSubnet;
	}


	public String getSumacAddress() {
		return sumacAddress;
	}


	public void setSumacAddress(String sumacAddress) {
		this.sumacAddress = sumacAddress;
	}


	public Byte getSumgmtipCustprovided() {
		return sumgmtipCustprovided;
	}


	public void setSumgmtipCustprovided(Byte sumgmtipCustprovided) {
		this.sumgmtipCustprovided = sumgmtipCustprovided;
	}


	public Byte getSumgmtsubnetCustprovided() {
		return sumgmtsubnetCustprovided;
	}


	public void setSumgmtsubnetCustprovided(Byte sumgmtsubnetCustprovided) {
		this.sumgmtsubnetCustprovided = sumgmtsubnetCustprovided;
	}


	public String getUniqueName() {
		return uniqueName;
	}


	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}


	public Set<VlanQosProfileBean> getVlanQosProfiles() {
		if(vlanQosProfiles==null) {
			vlanQosProfiles=new HashSet<>();
		}
		return vlanQosProfiles;
	}


	public void setVlanQosProfiles(Set<VlanQosProfileBean> vlanQosProfiles) {
		this.vlanQosProfiles = vlanQosProfiles;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		isEdited = edited;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}