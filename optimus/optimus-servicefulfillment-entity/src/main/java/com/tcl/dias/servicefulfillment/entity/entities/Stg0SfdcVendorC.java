package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * 
 * This file contains the Stg0SfdcVendorC.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="STG0_SFDC_VENDOR__C")
@NamedQuery(name="Stg0SfdcVendorC.findAll", query="SELECT s FROM Stg0SfdcVendorC s")
public class Stg0SfdcVendorC implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="CITY__C")
	private String cityC;

	@Column(name="COMPANY_CODE__C")
	private String companyCodeC;

	@Column(name="COUNTRY_KEY__C")
	private String countryKeyC;

	private String createdbyid;

	private Timestamp createddate;

	private String currencyisocode;

	@Column(name="etl_load_dt")
	private Timestamp etlLoadDt;
	
	@Id
	private String id;

	@Column(name="IS_INACTIVE__C")
	private String isInactiveC;

	@Column(name="IS_XCONNECT_PROVIDER__C")
	private String isXconnectProviderC;

	@Column(name="ISBACKHAUL__C")
	private String isbackhaulC;

	@Column(name="ISBLOCKED__C")
	private String isblockedC;

	private String isdeleted;

	@Column(name="ISMODIFIED__C")
	private String ismodifiedC;

	private Timestamp lastactivitydate;

	private String lastmodifiedbyid;

	private Timestamp lastmodifieddate;

	private Timestamp lastreferenceddate;

	private Timestamp lastvieweddate;

	@Column(name="NAME")
	private String name;

	@Column(name="NAME_OF_COMPANY_CODE__C")
	private String nameOfCompanyCodeC;

	private String ownerid;

	@Column(name="REGION__C")
	private String regionC;

	@Column(name="SFDC_PROVIDER_NAME__C")
	private String sfdcProviderNameC;

	@Column(name="src_system")
	private String srcSystem;

	private Timestamp systemmodstamp;

	@Column(name="VENDOR_ID__C")
	private String vendorIdC;

	public Stg0SfdcVendorC() {
	}

	public String getCityC() {
		return this.cityC;
	}

	public void setCityC(String cityC) {
		this.cityC = cityC;
	}

	public String getCompanyCodeC() {
		return this.companyCodeC;
	}

	public void setCompanyCodeC(String companyCodeC) {
		this.companyCodeC = companyCodeC;
	}

	public String getCountryKeyC() {
		return this.countryKeyC;
	}

	public void setCountryKeyC(String countryKeyC) {
		this.countryKeyC = countryKeyC;
	}

	public String getCreatedbyid() {
		return this.createdbyid;
	}

	public void setCreatedbyid(String createdbyid) {
		this.createdbyid = createdbyid;
	}

	public Timestamp getCreateddate() {
		return this.createddate;
	}

	public void setCreateddate(Timestamp createddate) {
		this.createddate = createddate;
	}

	public String getCurrencyisocode() {
		return this.currencyisocode;
	}

	public void setCurrencyisocode(String currencyisocode) {
		this.currencyisocode = currencyisocode;
	}

	public Timestamp getEtlLoadDt() {
		return this.etlLoadDt;
	}

	public void setEtlLoadDt(Timestamp etlLoadDt) {
		this.etlLoadDt = etlLoadDt;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsInactiveC() {
		return this.isInactiveC;
	}

	public void setIsInactiveC(String isInactiveC) {
		this.isInactiveC = isInactiveC;
	}

	public String getIsXconnectProviderC() {
		return this.isXconnectProviderC;
	}

	public void setIsXconnectProviderC(String isXconnectProviderC) {
		this.isXconnectProviderC = isXconnectProviderC;
	}

	public String getIsbackhaulC() {
		return this.isbackhaulC;
	}

	public void setIsbackhaulC(String isbackhaulC) {
		this.isbackhaulC = isbackhaulC;
	}

	public String getIsblockedC() {
		return this.isblockedC;
	}

	public void setIsblockedC(String isblockedC) {
		this.isblockedC = isblockedC;
	}

	public String getIsdeleted() {
		return this.isdeleted;
	}

	public void setIsdeleted(String isdeleted) {
		this.isdeleted = isdeleted;
	}

	public String getIsmodifiedC() {
		return this.ismodifiedC;
	}

	public void setIsmodifiedC(String ismodifiedC) {
		this.ismodifiedC = ismodifiedC;
	}

	public Timestamp getLastactivitydate() {
		return this.lastactivitydate;
	}

	public void setLastactivitydate(Timestamp lastactivitydate) {
		this.lastactivitydate = lastactivitydate;
	}

	public String getLastmodifiedbyid() {
		return this.lastmodifiedbyid;
	}

	public void setLastmodifiedbyid(String lastmodifiedbyid) {
		this.lastmodifiedbyid = lastmodifiedbyid;
	}

	public Timestamp getLastmodifieddate() {
		return this.lastmodifieddate;
	}

	public void setLastmodifieddate(Timestamp lastmodifieddate) {
		this.lastmodifieddate = lastmodifieddate;
	}

	public Timestamp getLastreferenceddate() {
		return this.lastreferenceddate;
	}

	public void setLastreferenceddate(Timestamp lastreferenceddate) {
		this.lastreferenceddate = lastreferenceddate;
	}

	public Timestamp getLastvieweddate() {
		return this.lastvieweddate;
	}

	public void setLastvieweddate(Timestamp lastvieweddate) {
		this.lastvieweddate = lastvieweddate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameOfCompanyCodeC() {
		return this.nameOfCompanyCodeC;
	}

	public void setNameOfCompanyCodeC(String nameOfCompanyCodeC) {
		this.nameOfCompanyCodeC = nameOfCompanyCodeC;
	}

	public String getOwnerid() {
		return this.ownerid;
	}

	public void setOwnerid(String ownerid) {
		this.ownerid = ownerid;
	}

	public String getRegionC() {
		return this.regionC;
	}

	public void setRegionC(String regionC) {
		this.regionC = regionC;
	}

	public String getSfdcProviderNameC() {
		return this.sfdcProviderNameC;
	}

	public void setSfdcProviderNameC(String sfdcProviderNameC) {
		this.sfdcProviderNameC = sfdcProviderNameC;
	}

	public String getSrcSystem() {
		return this.srcSystem;
	}

	public void setSrcSystem(String srcSystem) {
		this.srcSystem = srcSystem;
	}

	public Timestamp getSystemmodstamp() {
		return this.systemmodstamp;
	}

	public void setSystemmodstamp(Timestamp systemmodstamp) {
		this.systemmodstamp = systemmodstamp;
	}

	public String getVendorIdC() {
		return this.vendorIdC;
	}

	public void setVendorIdC(String vendorIdC) {
		this.vendorIdC = vendorIdC;
	}

}