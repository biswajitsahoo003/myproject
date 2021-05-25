package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * 
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "quote_tnc")
@NamedQuery(name = "QuoteTnc.findAll", query = "SELECT q FROM QuoteTnc q")
public class QuoteTnc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_time")
	private Date createdTime;

	@Lob
	private String tnc;

	@Column(name = "updated_by")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_time")
	private Date updatedTime;

	// bi-directional many-to-one association to Quote
	@ManyToOne(fetch = FetchType.LAZY)
	private Quote quote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quote_le_id")
	private QuoteToLe quoteToLe;

	public QuoteTnc() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getTnc() {
		return this.tnc;
	}

	public void setTnc(String tnc) {
		this.tnc = tnc;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Quote getQuote() {
		return this.quote;
	}

	public void setQuote(Quote quote) {
		this.quote = quote;
	}

	public QuoteToLe getQuoteToLe() {
		return quoteToLe;
	}

	public void setQuoteToLe(QuoteToLe quoteToLe) {
		this.quoteToLe = quoteToLe;
	}
}