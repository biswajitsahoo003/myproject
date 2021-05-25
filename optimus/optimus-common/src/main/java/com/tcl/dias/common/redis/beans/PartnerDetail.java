package com.tcl.dias.common.redis.beans;

import java.io.Serializable;

/**
 * Bean class for Partner Details
 * 
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class PartnerDetail implements Serializable {

	private static final long serialVersionUID = 421163718445776624L;

	private Integer partnerId;

	private Integer partnerLeId;

	private String partnerAcId;

	private String partnerCode;

	private String partnerEmailId;

	private String partnerName;

	private Integer erfPartnerId;

	private Byte status;

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	public Integer getPartnerLeId() {
		return partnerLeId;
	}

	public void setPartnerLeId(Integer partnerLeId) {
		this.partnerLeId = partnerLeId;
	}

	public String getPartnerAcId() {
		return partnerAcId;
	}

	public void setPartnerAcId(String partnerAcId) {
		this.partnerAcId = partnerAcId;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getPartnerEmailId() {
		return partnerEmailId;
	}

	public void setPartnerEmailId(String partnerEmailId) {
		this.partnerEmailId = partnerEmailId;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public Integer getErfPartnerId() {
		return erfPartnerId;
	}

	public void setErfPartnerId(Integer erfPartnerId) {
		this.erfPartnerId = erfPartnerId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "PartnerDetail{" +
				"partnerId=" + partnerId +
				", partnerLeId=" + partnerLeId +
				", partnerAcId='" + partnerAcId + '\'' +
				", partnerCode='" + partnerCode + '\'' +
				", partnerEmailId='" + partnerEmailId + '\'' +
				", partnerName='" + partnerName + '\'' +
				", erfPartnerId=" + erfPartnerId +
				", status=" + status +
				'}';
	}
}
