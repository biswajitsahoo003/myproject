package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * 
 * This file contains the CpeDeviceNameDetail.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="cpe_device_name_detail")
@NamedQuery(name="CpeDeviceNameDetail.findAll", query="SELECT c FROM CpeDeviceNameDetail c")
public class CpeDeviceNameDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String city;

	@Column(name="country_code")
	private String countryCode;

	@Column(name="cpe_device_name")
	private String cpeDeviceName;

	@Column(name="erf_cust_customer_id")
	private Integer erfCustCustomerId;

	@Column(name="site_identifier")
	private Integer siteIdentifier;

	//bi-directional many-to-one association to ScComponent
	@ManyToOne
	@JoinColumn(name="sc_component_id")
	private ScComponent scComponent;

	//bi-directional many-to-one association to ScServiceDetail
	@ManyToOne
	@JoinColumn(name="sc_service_detail_id")
	private ScServiceDetail scServiceDetail;

	public CpeDeviceNameDetail() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCpeDeviceName() {
		return this.cpeDeviceName;
	}

	public void setCpeDeviceName(String cpeDeviceName) {
		this.cpeDeviceName = cpeDeviceName;
	}

	public Integer getErfCustCustomerId() {
		return this.erfCustCustomerId;
	}

	public void setErfCustCustomerId(Integer erfCustCustomerId) {
		this.erfCustCustomerId = erfCustCustomerId;
	}

	public Integer getSiteIdentifier() {
		return this.siteIdentifier;
	}

	public void setSiteIdentifier(Integer siteIdentifier) {
		this.siteIdentifier = siteIdentifier;
	}

	public ScComponent getScComponent() {
		return this.scComponent;
	}

	public void setScComponent(ScComponent scComponent) {
		this.scComponent = scComponent;
	}

	public ScServiceDetail getScServiceDetail() {
		return this.scServiceDetail;
	}

	public void setScServiceDetail(ScServiceDetail scServiceDetail) {
		this.scServiceDetail = scServiceDetail;
	}

}