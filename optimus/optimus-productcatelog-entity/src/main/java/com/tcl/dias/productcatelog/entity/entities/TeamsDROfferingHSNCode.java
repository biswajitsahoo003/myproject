package com.tcl.dias.productcatelog.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The persistent class for the vw_mstmdr_offering_hsn_code database table.
 *
 * @author Syed Ali.
 * @createdAt 23/12/2020, Wednesday, 11:29
 */
@Entity
@Table(name = "vw_mstmdr_offering_hsn_code")
@NamedQuery(name = "TeamsDROfferingHSNCode.findAll", query = "SELECT v FROM TeamsDROfferingHSNCode v")
public class TeamsDROfferingHSNCode implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "bundled_offering_nm")
	private String bundledOfferningNm;

	@Column(name = "atomic_offering_nm")
	private String atomicOfferingNm;

	@Column(name = "charge_nm")
	private String chargeNm;

	@Column(name = "vendor_nm")
	private String vendorNm;

	@Column(name = "charge_line_item")
	private String chargeLineItem;

	@Column(name = "hsn_code")
	private String hsnCode;

	public TeamsDROfferingHSNCode() {
	}

	public String getBundledOfferningNm() {
		return bundledOfferningNm;
	}

	public void setBundledOfferningNm(String bundledOfferningNm) {
		this.bundledOfferningNm = bundledOfferningNm;
	}

	public String getAtomicOfferingNm() {
		return atomicOfferingNm;
	}

	public void setAtomicOfferingNm(String atomicOfferingNm) {
		this.atomicOfferingNm = atomicOfferingNm;
	}

	public String getChargeNm() {
		return chargeNm;
	}

	public void setChargeNm(String chargeNm) {
		this.chargeNm = chargeNm;
	}

	public String getChargeLineItem() {
		return chargeLineItem;
	}

	public void setChargeLineItem(String chargeLineItem) {
		this.chargeLineItem = chargeLineItem;
	}

	public String getVendorNm() {
		return vendorNm;
	}

	public void setVendorNm(String vendorNm) {
		this.vendorNm = vendorNm;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}
}
