package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * 
 * Entity Class for credit check audit
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "quote_le_credit_check_audit")
@NamedQuery(name = "QuoteLeCreditCheckAudit.findAll", query = "SELECT q FROM QuoteLeCreditCheckAudit q")
public class QuoteLeCreditCheckAudit implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// bi-directional many-to-one association to QuoteToLe
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quote_le_id")
	private QuoteToLe quoteToLe;

	@Column(name = "tps_sfdc_approved_mrc")
	private Double tpsSfdcApprovedMrc;

	@Column(name = "tps_sfdc_approved_nrc")
	private Double tpsSfdcApprovedNrc;

	@Column(name = "tps_sfdc_approved_by")
	private String tpsSfdcApprovedBy;

	@Column(name = "tps_sfdc_differential_mrc")
	private Double tpsSfdcDifferentialMrc;
	
	@Column(name = "tps_sfdc_credit_check_status")
	private String tpsSfdcCreditCheckStatus;
	
	@Column(name="tps_sfdc_customer_name")
	private String tpsSfdcCustomerName;
	
	@Column(name="tps_sfdc_cuid")
	private String tpsSfdcCuId;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the quoteToLe
	 */
	public QuoteToLe getQuoteToLe() {
		return quoteToLe;
	}

	/**
	 * @param quoteToLe the quoteToLe to set
	 */
	public void setQuoteToLe(QuoteToLe quoteToLe) {
		this.quoteToLe = quoteToLe;
	}

	/**
	 * @return the tpsSfdcApprovedMrc
	 */
	public Double getTpsSfdcApprovedMrc() {
		return tpsSfdcApprovedMrc;
	}

	/**
	 * @param tpsSfdcApprovedMrc the tpsSfdcApprovedMrc to set
	 */
	public void setTpsSfdcApprovedMrc(Double tpsSfdcApprovedMrc) {
		this.tpsSfdcApprovedMrc = tpsSfdcApprovedMrc;
	}

	/**
	 * @return the tpsSfdcApprovedNrc
	 */
	public Double getTpsSfdcApprovedNrc() {
		return tpsSfdcApprovedNrc;
	}

	/**
	 * @param tpsSfdcApprovedNrc the tpsSfdcApprovedNrc to set
	 */
	public void setTpsSfdcApprovedNrc(Double tpsSfdcApprovedNrc) {
		this.tpsSfdcApprovedNrc = tpsSfdcApprovedNrc;
	}

	/**
	 * @return the tpsSfdcApprovedBy
	 */
	public String getTpsSfdcApprovedBy() {
		return tpsSfdcApprovedBy;
	}

	/**
	 * @param tpsSfdcApprovedBy the tpsSfdcApprovedBy to set
	 */
	public void setTpsSfdcApprovedBy(String tpsSfdcApprovedBy) {
		this.tpsSfdcApprovedBy = tpsSfdcApprovedBy;
	}

	/**
	 * @return the tpsSfdcDifferentialMrc
	 */
	public Double getTpsSfdcDifferentialMrc() {
		return tpsSfdcDifferentialMrc;
	}

	/**
	 * @param tpsSfdcDifferentialMrc the tpsSfdcDifferentialMrc to set
	 */
	public void setTpsSfdcDifferentialMrc(Double tpsSfdcDifferentialMrc) {
		this.tpsSfdcDifferentialMrc = tpsSfdcDifferentialMrc;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdTime
	 */
	public Date getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the tpsSfdcCreditCheckStatus
	 */
	public String getTpsSfdcCreditCheckStatus() {
		return tpsSfdcCreditCheckStatus;
	}

	/**
	 * @param tpsSfdcCreditCheckStatus the tpsSfdcCreditCheckStatus to set
	 */
	public void setTpsSfdcCreditCheckStatus(String tpsSfdcCreditCheckStatus) {
		this.tpsSfdcCreditCheckStatus = tpsSfdcCreditCheckStatus;
	}

	public String getTpsSfdcCustomerName() {
		return tpsSfdcCustomerName;
	}

	public void setTpsSfdcCustomerName(String tpsSfdcCustomerName) {
		this.tpsSfdcCustomerName = tpsSfdcCustomerName;
	}

	public String getTpsSfdcCuId() {
		return tpsSfdcCuId;
	}

	public void setTpsSfdcCuId(String tpsSfdcCuId) {
		this.tpsSfdcCuId = tpsSfdcCuId;
	}
	
	
	
	

}
