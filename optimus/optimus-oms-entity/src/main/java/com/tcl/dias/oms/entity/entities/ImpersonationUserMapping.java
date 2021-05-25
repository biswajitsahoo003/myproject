package com.tcl.dias.oms.entity.entities;

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
 * 
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "impersonation_user_mapping")
@NamedQuery(name = "ImpersonationUserMapping.findAll", query = "SELECT c FROM ImpersonationUserMapping c")
public class ImpersonationUserMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "source_user_id")
	private User sourceUserId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "imp_user_id")
	private User impUserId;

	@Column(name = "is_active")
	private Byte status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getSourceUserId() {
		return sourceUserId;
	}

	public void setSourceUserId(User sourceUserId) {
		this.sourceUserId = sourceUserId;
	}

	public User getImpUserId() {
		return impUserId;
	}

	public void setImpUserId(User impUserId) {
		this.impUserId = impUserId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

}