package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 
 * This file contains the QuoteGscTfn.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "quote_gsc_tfn")
@NamedQuery(name = "QuoteGscTfn.findAll", query = "SELECT q FROM QuoteGscTfn q")
public class QuoteGscTfn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "country_code", length = 100)
	private String countryCode;

	@Column(name = "created_by", length = 45)
	private String createdBy;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@Column(name = "is_ported")
	private Byte isPorted;

	@Column(name = "ported_from", length = 150)
	private String portedFrom;

	private Byte status;

	@Column(name = "tfn_number", length = 200)
	private String tfnNumber;

	@Column(name = "updated_by", length = 45)
	private String updatedBy;

	@Column(name = "updated_time")
	private Timestamp updatedTime;

	// bi-directional many-to-one association to QuoteGscDetail
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quote_gsc_details_id")
	private QuoteGscDetail quoteGscDetail;

	public QuoteGscTfn() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Byte getIsPorted() {
		return this.isPorted;
	}

	public void setIsPorted(Byte isPorted) {
		this.isPorted = isPorted;
	}

	public String getPortedFrom() {
		return this.portedFrom;
	}

	public void setPortedFrom(String portedFrom) {
		this.portedFrom = portedFrom;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public String getTfnNumber() {
		return this.tfnNumber;
	}

	public void setTfnNumber(String tfnNumber) {
		this.tfnNumber = tfnNumber;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	public QuoteGscDetail getQuoteGscDetail() {
		return this.quoteGscDetail;
	}

	public void setQuoteGscDetail(QuoteGscDetail quoteGscDetail) {
		this.quoteGscDetail = quoteGscDetail;
	}

}