package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * This file contains the QuoteGscDetail.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "quote_gsc_details")
@NamedQuery(name = "QuoteGscDetail.findAll", query = "SELECT q FROM QuoteGscDetail q")
public class QuoteGscDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	private Double arc;

	@Column(name = "created_by", length = 45)
	private String createdBy;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@Column(length = 100)
	private String dest;

	@Column(name = "dest_type", length = 45)
	private String destType;

	private Double mrc;

	private Double nrc;

	@Column(length = 100)
	private String src;

	@Column(name = "src_type", length = 45)
	private String srcType;

	@Column(name = "type")
	private String type;

	// bi-directional many-to-one association to QuoteGsc
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quote_gsc_id")
	private QuoteGsc quoteGsc;

	// bi-directional many-to-one association to QuoteGscTfn
	@OneToMany(mappedBy = "quoteGscDetail")
	private Set<QuoteGscTfn> quoteGscTfns;

	public QuoteGscDetail() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getArc() {
		return this.arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
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

	public String getDest() {
		return this.dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public String getDestType() {
		return this.destType;
	}

	public void setDestType(String destType) {
		this.destType = destType;
	}

	public Double getMrc() {
		return this.mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return this.nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public String getSrc() {
		return this.src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getSrcType() {
		return this.srcType;
	}

	public void setSrcType(String srcType) {
		this.srcType = srcType;
	}

	public QuoteGsc getQuoteGsc() {
		return this.quoteGsc;
	}

	public void setQuoteGsc(QuoteGsc quoteGsc) {
		this.quoteGsc = quoteGsc;
	}

	public Set<QuoteGscTfn> getQuoteGscTfns() {
		return this.quoteGscTfns;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setQuoteGscTfns(Set<QuoteGscTfn> quoteGscTfns) {
		this.quoteGscTfns = quoteGscTfns;
	}

	public QuoteGscTfn addQuoteGscTfn(QuoteGscTfn quoteGscTfn) {
		getQuoteGscTfns().add(quoteGscTfn);
		quoteGscTfn.setQuoteGscDetail(this);

		return quoteGscTfn;
	}

	public QuoteGscTfn removeQuoteGscTfn(QuoteGscTfn quoteGscTfn) {
		getQuoteGscTfns().remove(quoteGscTfn);
		quoteGscTfn.setQuoteGscDetail(null);

		return quoteGscTfn;
	}

}