package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * Entity Class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "user")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "contact_no")
	private String contactNo;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "partner_id")
	private Integer partnerId;

	private Integer status;

	@Column(name = "user_type")
	private String userType;

	@Column(name = "designation")
	private String designation;

	private String username;

	// bi-directional many-to-one association to Customer
	@ManyToOne(fetch = FetchType.LAZY)
	private Customer customer;

	@Column(name = "force_change_password")
	private Byte forceChangePassword;

	@Column(name = "is_otp_enabled")
	private Byte isOtpEnabled;

	@Column(name = "is_gen_terms_approved")
	private Byte IsPartnerGeneralTermsApproved;

	@Column(name = "show_cos_message")
	private Byte showCosMessage;

	@Column(name = "imp_flag")
	private Byte impFlag=1;

	public User() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContactNo() {
		return this.contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getEmailId() {
		return this.emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getPartnerId() {
		return this.partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUserType() {
		return this.userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	/**
	 * @return the forceChangePassword
	 */
	public Byte getForceChangePassword() {
		return forceChangePassword;
	}

	/**
	 * @param forceChangePassword the forceChangePassword to set
	 */
	public void setForceChangePassword(Byte forceChangePassword) {
		this.forceChangePassword = forceChangePassword;
	}

	public Byte getIsOtpEnabled() {
		return isOtpEnabled;
	}

	public void setIsOtpEnabled(Byte isOtpEnabled) {
		this.isOtpEnabled = isOtpEnabled;
	}

	public Byte getIsPartnerGeneralTermsApproved() {
		return IsPartnerGeneralTermsApproved;
	}

	public void setIsPartnerGeneralTermsApproved(Byte isPartnerGeneralTermsApproved) {
		IsPartnerGeneralTermsApproved = isPartnerGeneralTermsApproved;
	}

	public Byte getShowCosMessage() {
		return showCosMessage;
	}

	public void setShowCosMessage(Byte showCosMessage) {
		this.showCosMessage = showCosMessage;
	}

	public Byte getImpFlag() {
		return impFlag;
	}

	public void setImpFlag(Byte impFlag) {
		this.impFlag = impFlag;
	}

}