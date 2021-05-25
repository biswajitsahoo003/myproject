package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 
 * Multicasting Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class MulticastingBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer multicatingId;
	private String autoDiscoveryOption;
	private String dataMdt;
	private String dataMdtThreshold;
	private String defaultMdt;
	private Timestamp endDate;
	private Timestamp lastModifiedDate;
	private String modifiedBy;
	private String rpAddress;
	private String rpLocation;
	private Timestamp startDate;
	private String type;
	private String wanPimMode;
	private boolean isEdited;
	public Integer getMulticatingId() {
		return multicatingId;
	}
	public void setMulticatingId(Integer multicatingId) {
		this.multicatingId = multicatingId;
	}
	public String getAutoDiscoveryOption() {
		return autoDiscoveryOption;
	}
	public void setAutoDiscoveryOption(String autoDiscoveryOption) {
		this.autoDiscoveryOption = autoDiscoveryOption;
	}
	public String getDataMdt() {
		return dataMdt;
	}
	public void setDataMdt(String dataMdt) {
		this.dataMdt = dataMdt;
	}
	public String getDataMdtThreshold() {
		return dataMdtThreshold;
	}
	public void setDataMdtThreshold(String dataMdtThreshold) {
		this.dataMdtThreshold = dataMdtThreshold;
	}
	public String getDefaultMdt() {
		return defaultMdt;
	}
	public void setDefaultMdt(String defaultMdt) {
		this.defaultMdt = defaultMdt;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
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
	public String getRpAddress() {
		return rpAddress;
	}
	public void setRpAddress(String rpAddress) {
		this.rpAddress = rpAddress;
	}
	public String getRpLocation() {
		return rpLocation;
	}
	public void setRpLocation(String rpLocation) {
		this.rpLocation = rpLocation;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getWanPimMode() {
		return wanPimMode;
	}
	public void setWanPimMode(String wanPimMode) {
		this.wanPimMode = wanPimMode;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		isEdited = edited;
	}
}