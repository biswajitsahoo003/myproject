package com.tcl.dias.networkaugment.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "mst_vendors")
@NamedQuery(name = "MstVendor.findAll", query = "SELECT v FROM MstVendor v")
public class MstVendor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2433439164672782704L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;

	@Column(name = "circle")
	private String circle;
	
	@Column(name = "vendor_id")
	private String vendorId;

	@Column(name = "contact_name")
	private String contactName;

	@Column(name = "name")
	private String name;

	@Column(name = "type")
	private String type;

	@Column(name = "country")
	private String country;
	
	@Column(name="phone_number")
	private String phoneNumber;
	
	@Column(name="email")
	private String email;
	

	//bi-directional many-to-one association to MstStatus
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "status",referencedColumnName="id")
	private MstStatus mstStatus;

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public MstStatus getMstStatus() {
		return mstStatus;
	}

	public void setMstStatus(MstStatus mstStatus) {
		this.mstStatus = mstStatus;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	
}
