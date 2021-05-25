package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * utility_audit table for utility portal
 * @author archchan
 *
 */
@Entity
@Table(name = "utility_audit")
@NamedQuery(name = "UtilityAudit.findAll", query = "SELECT c FROM UtilityAudit c")
public class UtilityAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "mdc_token")
	private String mdcToken;
	
	@Column(name = "quote_code")
	private String quoteCode;
	
	@Column(name = "from_value")
	private String fromValue;
	
	@Column(name = "to_value")
	private String toValue;
	
	@Column(name = "utility_type")
	private String utilityType;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
	@Column(name = "updated_time")
	private Date updatedTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMdcToken() {
		return mdcToken;
	}

	public void setMdcToken(String mdcToken) {
		this.mdcToken = mdcToken;
	}

	public String getQuoteCode() {
		return quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	public String getFromValue() {
		return fromValue;
	}

	public void setFromValue(String fromValue) {
		this.fromValue = fromValue;
	}

	public String getToValue() {
		return toValue;
	}

	public void setToValue(String toValue) {
		this.toValue = toValue;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getUtilityType() {
		return utilityType;
	}

	public void setUtilityType(String utilityType) {
		this.utilityType = utilityType;
	}

	@Override
	public String toString() {
		return "UtilityAudit [id=" + id + ", mdcToken=" + mdcToken + ", quoteCode=" + quoteCode + ", fromValue="
				+ fromValue + ", toValue=" + toValue + ", utilityType=" + utilityType + ", updatedBy=" + updatedBy
				+ ", updatedTime=" + updatedTime + "]";
	}
	
}
