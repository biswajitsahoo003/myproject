package com.tcl.dias.serviceinventory.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This file contains the gsc service circuit link details
 * 
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "vw_gsc_srvc_circt_link_dtl")
public class ViewGscServiceCircuitLinkDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id")
	private String id;

	@Column(name = "sfdc_cuid")
	private String sfdcCuid;

	@Column(name = "erf_cust_customer_id")
	private String erfCusCustomerId;

	@Column(name = "cust_ip_addr")
	private String customerIpAddress;

	@Column(name = "circt_gr_cd")
	private String circuitGrCd;

	@Column(name = "swtch_unit_cd")
	private String switchingUnitCd;

	@Column(name = "circuit_id")
	private String circuitId;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSfdcCuid() {
		return sfdcCuid;
	}

	public void setSfdcCuid(String sfdcCuid) {
		this.sfdcCuid = sfdcCuid;
	}

	public String getErfCusCustomerId() {
		return erfCusCustomerId;
	}

	public void setErfCusCustomerId(String erfCusCustomerId) {
		this.erfCusCustomerId = erfCusCustomerId;
	}

	public String getCustomerIpAddress() {
		return customerIpAddress;
	}

	public void setCustomerIpAddress(String customerIpAddress) {
		this.customerIpAddress = customerIpAddress;
	}

	public String getCircuitGrCd() {
		return circuitGrCd;
	}

	public void setCircuitGrCd(String circuitGrCd) {
		this.circuitGrCd = circuitGrCd;
	}

	public String getSwitchingUnitCd() {
		return switchingUnitCd;
	}

	public void setSwitchingUnitCd(String switchingUnitCd) {
		this.switchingUnitCd = switchingUnitCd;
	}

	public String getCircuitId() {
		return circuitId;
	}

	public void setCircuitId(String circuitId) {
		this.circuitId = circuitId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
}
