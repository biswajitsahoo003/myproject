package com.tcl.dias.customer.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * The persistent class for the attachments database table.
 * 
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "attachments")
@NamedQuery(name = "Attachment.findAll", query = "SELECT a FROM Attachment a")
public class Attachment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@Column(name = "display_name")
	private String displayName;

	@Column(name = "is_deleted")
	private Byte isDeleted;

	private String name;

	@Column(name = "uri_path_or_url")
	private String uriPathOrUrl;
	
	@Column(name="expiry_window")
	private Long expiryWindow;
	
	public Attachment() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCreatedBy() {

		return this.createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Byte getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(Byte isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the uriPathOrUrl
	 */
	public String getUriPathOrUrl() {
		return uriPathOrUrl;
	}

	/**
	 * @param uriPathOrUrl the uriPathOrUrl to set
	 */
	public void setUriPathOrUrl(String uriPathOrUrl) {
		this.uriPathOrUrl = uriPathOrUrl;
	}

	/**
	 * @return the expiryWindow
	 */
	public Long getExpiryWindow() {
		return expiryWindow;
	}

	/**
	 * @param expiryWindow the expiryWindow to set
	 */
	public void setExpiryWindow(Long expiryWindow) {
		this.expiryWindow = expiryWindow;
	}

	


	

}