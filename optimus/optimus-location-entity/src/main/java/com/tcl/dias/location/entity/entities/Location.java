package com.tcl.dias.location.entity.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "location")
@NamedQuery(name = "Location.findAll", query = "SELECT l FROM Location l")
public class Location implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "api_address_id")
	private Integer apiAddressId;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "end_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@Column(name = "is_active")
	private Byte isActive;

	@Column(name = "is_verified")
	private Integer isVerified;

	@Column(name = "lat_long")
	private String latLong;

	@Column(name = "verified_type")
	private String verifiedType;

	@Column(name = "pop_location_id")
	private String popLocationId;

	@Column(name = "tier")
	private String tier;

	// bi-directional many-to-one association to CustomerLocation
	@OneToMany(mappedBy = "location")
	private Set<CustomerLocation> customerLocations;

	// bi-directional many-to-one association to Demarcation
	@ManyToOne(fetch = FetchType.LAZY)
	private Demarcation demarcation;

	@Column(name = "address_id")
	private Integer addressId;
	
	@Column(name = "remarks")
	private String remarks;

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public Location() {
		// Do Nothing
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getApiAddressId() {
		return this.apiAddressId;
	}

	public void setApiAddressId(Integer apiAddressId) {
		this.apiAddressId = apiAddressId;
	}

	public Integer getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public byte getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Byte isActive) {
		this.isActive = isActive;
	}

	public Integer getIsVerified() {
		return this.isVerified;
	}

	public void setIsVerified(Integer isVerified) {
		this.isVerified = isVerified;
	}

	public String getLatLong() {
		return this.latLong;
	}

	public void setLatLong(String latLong) {
		this.latLong = latLong;
	}

	public String getVerifiedType() {
		return this.verifiedType;
	}

	public void setVerifiedType(String verifiedType) {
		this.verifiedType = verifiedType;
	}

	public Set<CustomerLocation> getCustomerLocations() {
		return this.customerLocations;
	}

	public void setCustomerLocations(Set<CustomerLocation> customerLocations) {
		this.customerLocations = customerLocations;
	}

	public CustomerLocation addCustomerLocation(CustomerLocation customerLocation) {
		getCustomerLocations().add(customerLocation);
		customerLocation.setLocation(this);

		return customerLocation;
	}

	public CustomerLocation removeCustomerLocation(CustomerLocation customerLocation) {
		getCustomerLocations().remove(customerLocation);
		customerLocation.setLocation(null);

		return customerLocation;
	}

	public Demarcation getDemarcation() {
		return this.demarcation;
	}

	public void setDemarcation(Demarcation demarcation) {
		this.demarcation = demarcation;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getPopLocationId() {
		return popLocationId;
	}

	public void setPopLocationId(String popLocationId) {
		this.popLocationId = popLocationId;
	}

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

}