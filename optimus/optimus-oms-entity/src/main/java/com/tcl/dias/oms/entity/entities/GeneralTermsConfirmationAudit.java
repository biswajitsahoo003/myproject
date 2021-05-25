package com.tcl.dias.oms.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * The entity class for the general_terms_confirmation_audit database table.
 * 
 * @author ANUSHA UNNI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "general_terms_confirmation_audit")
@NamedQuery(name = "GeneralTermsConfirmationAudit.findAll", query = "SELECT g FROM GeneralTermsConfirmationAudit g")
public class GeneralTermsConfirmationAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "public_ip")
	private String publicIp;

	@Column(name = "audit_type")
	private String auditType;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	@Column(name = "user_agent")
	private String userAgent;

	@Column(name = "created_time_unix")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTimeUnix;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublicIp() {
		return publicIp;
	}

	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}

	public String getAuditType() {
		return auditType;
	}

	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public Date getCreatedTimeUnix() {
		return createdTimeUnix;
	}

	public void setCreatedTimeUnix(Date createdTimeUnix) {
		this.createdTimeUnix = createdTimeUnix;
	}
}
