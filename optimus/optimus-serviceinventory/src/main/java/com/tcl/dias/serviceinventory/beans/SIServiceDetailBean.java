package com.tcl.dias.serviceinventory.beans;

import com.tcl.dias.serviceinventory.entity.entities.SIServiceDetail;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Bean class to hold SI service detail
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SIServiceDetailBean {

	private Integer id;

	private String tpsServiceId;

	private String vpnId;

	private Integer siOrderId;

	private String serviceTopology;

	private String siteTopology;

	private String siteType;

	private String siteAddress;

	private String productName;

	private String parentProductName;

	private String parentProductOfferingName;
	
	private String bandwidthPortSpeed;

	private String bandwidthUnit;

	private String vpnName;

	private String callType;

	private String custOrgNo;

	private String supplOrgNo;

	private Set<SIServiceAttributeBean> attributes;
	
	private String legalEnityName;
	
	private String lastMileProvider;

	public String getLegalEnityName() {
		return legalEnityName;
	}

	public void setLegalEnityName(String legalEnityName) {
		this.legalEnityName = legalEnityName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSiOrderId() {
		return siOrderId;
	}

	public void setSiOrderId(Integer siOrderId) {
		this.siOrderId = siOrderId;
	}

	public SIServiceDetailBean() {

	}

	public SIServiceDetailBean(SIServiceDetail entity) {
		if (Objects.nonNull(entity)) {
			this.setId(entity.getId());
			this.setTpsServiceId(entity.getTpsServiceId());
			this.setServiceTopology(entity.getServiceTopology());
			this.setSiteTopology(entity.getSiteTopology());
			this.setVpnId(entity.getBillingAccountId());
			this.setSiteType(entity.getSiteType());
			this.setSiteAddress(entity.getSiteAddress());
			this.setProductName(entity.getErfPrdCatalogProductName());
			this.setParentProductName(entity.getErfPrdCatalogParentProductName());
			this.setParentProductOfferingName(entity.getErfPrdCatalogParentProductOfferingName());
			this.setBandwidthPortSpeed(entity.getBwPortspeed());
			this.setBandwidthUnit(entity.getBwUnit());
			this.setVpnName(entity.getVpnName());
			this.setCallType(entity.getCallType());
			this.setCustOrgNo(entity.getCustOrgNo());
			this.setSupplOrgNo(entity.getSupplOrgNo());
			this.setLastMileProvider(entity.getLastmileProvider());
			if (Objects.nonNull(entity.getSiServiceAttributes())) {
				this.setAttributes(entity.getSiServiceAttributes().stream().map(SIServiceAttributeBean::new)
						.collect(Collectors.toSet()));
			}
			if (Objects.nonNull(entity.getSiOrder().getErfCustLeName())) {
				this.setLegalEnityName(entity.getSiOrder().getErfCustLeName());
			}
		}
	}

	public Set<SIServiceAttributeBean> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<SIServiceAttributeBean> attributes) {
		this.attributes = attributes;
	}

	public String getTpsServiceId() {
		return tpsServiceId;
	}

	public void setTpsServiceId(String tpsServiceId) {
		this.tpsServiceId = tpsServiceId;
	}

	public String getVpnId() {
		return vpnId;
	}

	public void setVpnId(String vpnId) {
		this.vpnId = vpnId;
	}

	public String getServiceTopology() {
		return serviceTopology;
	}

	public void setServiceTopology(String serviceTopology) {
		this.serviceTopology = serviceTopology;
	}

	public String getSiteTopology() {
		return siteTopology;
	}

	public void setSiteTopology(String siteTopology) {
		this.siteTopology = siteTopology;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getParentProductName() {
		return parentProductName;
	}

	public void setParentProductName(String parentProductName) {
		this.parentProductName = parentProductName;
	}

	public String getParentProductOfferingName() {
		return parentProductOfferingName;
	}

	public void setParentProductOfferingName(String parentProductOfferingName) {
		this.parentProductOfferingName = parentProductOfferingName;
	}


	public String getBandwidthUnit() {
		return bandwidthUnit;
	}

	public void setBandwidthUnit(String bandwidthUnit) {
		this.bandwidthUnit = bandwidthUnit;
	}

	public String getVpnName() {
		return vpnName;
	}

	public void setVpnName(String vpnName) {
		this.vpnName = vpnName;
	}

	public String getBandwidthPortSpeed() {
		return bandwidthPortSpeed;
	}

	public void setBandwidthPortSpeed(String bandwidthPortSpeed) {
		this.bandwidthPortSpeed = bandwidthPortSpeed;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public String getCustOrgNo() {
		return custOrgNo;
	}

	public void setCustOrgNo(String custOrgNo) {
		this.custOrgNo = custOrgNo;
	}

	public String getSupplOrgNo() {
		return supplOrgNo;
	}

	public void setSupplOrgNo(String supplOrgNo) {
		this.supplOrgNo = supplOrgNo;
	}

	public String getLastMileProvider() {
		return lastMileProvider;
	}

	public void setLastMileProvider(String lastMileProvider) {
		this.lastMileProvider = lastMileProvider;
	}
	
}
