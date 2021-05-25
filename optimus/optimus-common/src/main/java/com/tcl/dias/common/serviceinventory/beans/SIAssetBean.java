package com.tcl.dias.common.serviceinventory.beans;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Bean class to contain Service Inventory asset data
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */
@JsonInclude(NON_NULL)
public class SIAssetBean {
	private Integer id;
	private String name;
	private String fqdn;
	private String assetGroupId;
	private String assetGroupType;
	private String assetStatus;
	private String assetTag;
	private Integer erfCustomerId;
	private String publicIp;
	private String serialNo;
	private Date terminationDate;
	private String type;
	private List<SIAttributeBean> attributes;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFqdn() {
		return fqdn;
	}

	public void setFqdn(String fqdn) {
		this.fqdn = fqdn;
	}

	public String getAssetGroupId() {
		return assetGroupId;
	}

	public void setAssetGroupId(String assetGroupId) {
		this.assetGroupId = assetGroupId;
	}

	public String getAssetGroupType() {
		return assetGroupType;
	}

	public void setAssetGroupType(String assetGroupType) {
		this.assetGroupType = assetGroupType;
	}

	public String getAssetStatus() {
		return assetStatus;
	}

	public void setAssetStatus(String assetStatus) {
		this.assetStatus = assetStatus;
	}

	public String getAssetTag() {
		return assetTag;
	}

	public void setAssetTag(String assetTag) {
		this.assetTag = assetTag;
	}

	public Integer getErfCustomerId() {
		return erfCustomerId;
	}

	public void setErfCustomerId(Integer erfCustomerId) {
		this.erfCustomerId = erfCustomerId;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Date getTerminationDate() {
		return terminationDate;
	}

	public void setTerminationDate(Date terminationDate) {
		this.terminationDate = terminationDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<SIAttributeBean> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<SIAttributeBean> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return "SIAssetBean{" +
				"id=" + id +
				", name='" + name + '\'' +
				", fqdn='" + fqdn + '\'' +
				", assetGroupId='" + assetGroupId + '\'' +
				", assetGroupType='" + assetGroupType + '\'' +
				", assetStatus='" + assetStatus + '\'' +
				", assetTag='" + assetTag + '\'' +
				", erfCustomerId=" + erfCustomerId +
				", publicIp='" + publicIp + '\'' +
				", serialNo='" + serialNo + '\'' +
				", terminationDate=" + terminationDate +
				", type='" + type + '\'' +
				", attributes=" + attributes +
				'}';
	}
}
