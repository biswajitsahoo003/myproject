package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * 
 * This file contains the Stg0SapPoDtlOptimus.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="STG0_SAP_PO_DTL_OPTIMUS")
@NamedQuery(name="Stg0SapPoDtlOptimus.findAll", query="SELECT s FROM Stg0SapPoDtlOptimus s")
public class Stg0SapPoDtlOptimus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CHILD_PO_NUMBER")
	private String childPoNumber;

	@Column(name="etl_load_dt")
	private Timestamp etlLoadDt;

	@Column(name="PARENT_PO_NUMBER")
	private String parentPoNumber;

	@Column(name="PO_CREATION_DATE")
	private String poCreationDate;

	@Column(name="PO_LINE_NO")
	private String poLineNo;
	
	@Id
	@Column(name="PO_NUMBER")
	private String poNumber;

	@Column(name="PRODUCT_COMPONENT")
	private String productComponent;

	@Column(name="src_system")
	private String srcSystem;

	@Column(name="TCL_SERVICE_ID")
	private String tclServiceId;

	@Column(name="TERMINATION_TYPE")
	private String terminationType;

	@Column(name="VENDOR_NO")
	private String vendorNo;

	@Column(name="VENDOR_REF_ID_ORDER_ID")
	private String vendorRefIdOrderId;

	public Stg0SapPoDtlOptimus() {
	}

	public String getChildPoNumber() {
		return this.childPoNumber;
	}

	public void setChildPoNumber(String childPoNumber) {
		this.childPoNumber = childPoNumber;
	}

	public Timestamp getEtlLoadDt() {
		return this.etlLoadDt;
	}

	public void setEtlLoadDt(Timestamp etlLoadDt) {
		this.etlLoadDt = etlLoadDt;
	}

	public String getParentPoNumber() {
		return this.parentPoNumber;
	}

	public void setParentPoNumber(String parentPoNumber) {
		this.parentPoNumber = parentPoNumber;
	}

	public String getPoCreationDate() {
		return this.poCreationDate;
	}

	public void setPoCreationDate(String poCreationDate) {
		this.poCreationDate = poCreationDate;
	}

	public String getPoLineNo() {
		return this.poLineNo;
	}

	public void setPoLineNo(String poLineNo) {
		this.poLineNo = poLineNo;
	}

	public String getPoNumber() {
		return this.poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getProductComponent() {
		return this.productComponent;
	}

	public void setProductComponent(String productComponent) {
		this.productComponent = productComponent;
	}

	public String getSrcSystem() {
		return this.srcSystem;
	}

	public void setSrcSystem(String srcSystem) {
		this.srcSystem = srcSystem;
	}

	public String getTclServiceId() {
		return this.tclServiceId;
	}

	public void setTclServiceId(String tclServiceId) {
		this.tclServiceId = tclServiceId;
	}

	public String getTerminationType() {
		return this.terminationType;
	}

	public void setTerminationType(String terminationType) {
		this.terminationType = terminationType;
	}

	public String getVendorNo() {
		return this.vendorNo;
	}

	public void setVendorNo(String vendorNo) {
		this.vendorNo = vendorNo;
	}

	public String getVendorRefIdOrderId() {
		return this.vendorRefIdOrderId;
	}

	public void setVendorRefIdOrderId(String vendorRefIdOrderId) {
		this.vendorRefIdOrderId = vendorRefIdOrderId;
	}

}