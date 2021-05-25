package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "odr_gst_address")
@NamedQuery(name = "OdrGstAddress.findAll", query = "SELECT o FROM OdrGstAddress o")
public class OdrGstAddress implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "building_name")
	private String buildingName;

	@Column(name = "building_number")
	private String buildingNumber;

	@Column(name = "created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_time")
	private Date createdTime;

	private String district;

	@Column(name = "flat_number")
	private String flatNumber;

	private String latitude;

	private String locality;

	private String longitude;

	private String pincode;

	private String state;

	private String street;

	// bi-directional many-to-one association to OdrServiceDetail
	@OneToMany(mappedBy = "odrGstAddress")
	private List<OdrServiceDetail> odrServiceDetails;

	public OdrGstAddress() {
		//DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBuildingName() {
		return this.buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getBuildingNumber() {
		return this.buildingNumber;
	}

	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getDistrict() {
		return this.district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getFlatNumber() {
		return this.flatNumber;
	}

	public void setFlatNumber(String flatNumber) {
		this.flatNumber = flatNumber;
	}

	public String getLatitude() {
		return this.latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLocality() {
		return this.locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getLongitude() {
		return this.longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getPincode() {
		return this.pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStreet() {
		return this.street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public List<OdrServiceDetail> getOdrServiceDetails() {
		return this.odrServiceDetails;
	}

	public void setOdrServiceDetails(List<OdrServiceDetail> odrServiceDetails) {
		this.odrServiceDetails = odrServiceDetails;
	}

	public OdrServiceDetail addOdrServiceDetail(OdrServiceDetail odrServiceDetail) {
		getOdrServiceDetails().add(odrServiceDetail);
		odrServiceDetail.setOdrGstAddress(this);

		return odrServiceDetail;
	}

	public OdrServiceDetail removeOdrServiceDetail(OdrServiceDetail odrServiceDetail) {
		getOdrServiceDetails().remove(odrServiceDetail);
		odrServiceDetail.setOdrGstAddress(null);

		return odrServiceDetail;
	}

}