package com.tcl.dias.oms.entity.entities;

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
 * Bean class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "impersonation_user_audit")
@NamedQuery(name = "ImpersonationUserAudit.findAll", query = "SELECT s FROM ImpersonationUserAudit s")
public class ImpersonationUserAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "impersonating_user")
	private String impersonatingUser;

	@Column(name = "impersonated_user")
	private String impersonatedUser;

	@Column(name = "session_id")
	private String sessionId;

	@Column(name = "public_ip")
	private String publicIp;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	private Timestamp createdTime;
	
	@Column(name = "is_imp_readonly")
	private Byte isImpReadOnly;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImpersonatingUser() {
		return impersonatingUser;
	}

	public void setImpersonatingUser(String impersonatingUser) {
		this.impersonatingUser = impersonatingUser;
	}

	public String getImpersonatedUser() {
		return impersonatedUser;
	}

	public void setImpersonatedUser(String impersonatedUser) {
		this.impersonatedUser = impersonatedUser;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Byte getIsImpReadOnly() {
		return isImpReadOnly;
	}

	public void setIsImpReadOnly(Byte isImpReadOnly) {
		this.isImpReadOnly = isImpReadOnly;
	}
	
	

}