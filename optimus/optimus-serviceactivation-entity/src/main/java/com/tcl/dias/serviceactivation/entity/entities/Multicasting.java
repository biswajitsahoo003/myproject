package com.tcl.dias.serviceactivation.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * 
 * Multicasting Entity Class
 * 
 *
 * @author Naveen
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="multicasting")
@NamedQuery(name="Multicasting.findAll", query="SELECT m FROM Multicasting m")
public class Multicasting implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="multicating_id")
	private Integer multicatingId;

	@Column(name="auto_discovery_option")
	private String autoDiscoveryOption;

	@Column(name="data_mdt")
	private String dataMdt;

	@Column(name="data_mdt_threshold")
	private String dataMdtThreshold;

	@Column(name="default_mdt")
	private String defaultMdt;

	@Column(name="end_date")
	private Timestamp endDate;

	@Column(name="last_modified_date")
	private Timestamp lastModifiedDate;

	@Column(name="modified_by")
	private String modifiedBy;

	@Column(name="rp_address")
	private String rpAddress;

	@Column(name="rp_location")
	private String rpLocation;

	@Column(name="start_date")
	private Timestamp startDate;

	private String type;

	@Column(name="wan_pim_mode")
	private String wanPimMode;

	//bi-directional many-to-one association to ServiceDetail
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="service_details_id")
	private ServiceDetail serviceDetail;

	public Multicasting() {
	}

	public Integer getMulticatingId() {
		return this.multicatingId;
	}

	public void setMulticatingId(Integer multicatingId) {
		this.multicatingId = multicatingId;
	}

	public String getAutoDiscoveryOption() {
		return this.autoDiscoveryOption;
	}

	public void setAutoDiscoveryOption(String autoDiscoveryOption) {
		this.autoDiscoveryOption = autoDiscoveryOption;
	}

	public String getDataMdt() {
		return this.dataMdt;
	}

	public void setDataMdt(String dataMdt) {
		this.dataMdt = dataMdt;
	}

	public String getDataMdtThreshold() {
		return this.dataMdtThreshold;
	}

	public void setDataMdtThreshold(String dataMdtThreshold) {
		this.dataMdtThreshold = dataMdtThreshold;
	}

	public String getDefaultMdt() {
		return this.defaultMdt;
	}

	public void setDefaultMdt(String defaultMdt) {
		this.defaultMdt = defaultMdt;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
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

	public String getRpAddress() {
		return this.rpAddress;
	}

	public void setRpAddress(String rpAddress) {
		this.rpAddress = rpAddress;
	}

	public String getRpLocation() {
		return this.rpLocation;
	}

	public void setRpLocation(String rpLocation) {
		this.rpLocation = rpLocation;
	}

	public Timestamp getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWanPimMode() {
		return this.wanPimMode;
	}

	public void setWanPimMode(String wanPimMode) {
		this.wanPimMode = wanPimMode;
	}

	public ServiceDetail getServiceDetail() {
		return this.serviceDetail;
	}

	public void setServiceDetail(ServiceDetail serviceDetail) {
		this.serviceDetail = serviceDetail;
	}

}