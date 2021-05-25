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
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "quote_ill_site_sla")
@NamedQuery(name = "QuoteIllSiteSla.findAll", query = "SELECT q FROM QuoteIllSiteSla q")
public class QuoteIllSiteSla implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "sla_end_date")
	private Date slaEndDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "sla_start_date")
	private Date slaStartDate;

	// bi-directional many-to-one association to QuoteIllSite
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ill_site_id")
	private QuoteIllSite quoteIllSite;

	// bi-directional many-to-one association to SlaMaster
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sla_master_id")
	private SlaMaster slaMaster;

	@Column(name = "sla_value")
	private String slaValue;

	public QuoteIllSiteSla() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getSlaEndDate() {
		return this.slaEndDate;
	}

	public void setSlaEndDate(Date slaEndDate) {
		this.slaEndDate = slaEndDate;
	}

	public Date getSlaStartDate() {
		return this.slaStartDate;
	}

	public void setSlaStartDate(Date slaStartDate) {
		this.slaStartDate = slaStartDate;
	}

	public QuoteIllSite getQuoteIllSite() {
		return this.quoteIllSite;
	}

	public void setQuoteIllSite(QuoteIllSite quoteIllSite) {
		this.quoteIllSite = quoteIllSite;
	}

	public SlaMaster getSlaMaster() {
		return this.slaMaster;
	}

	public void setSlaMaster(SlaMaster slaMaster) {
		this.slaMaster = slaMaster;
	}

	/**
	 * @return the slaValue
	 */
	public String getSlaValue() {
		return slaValue;
	}

	/**
	 * @param slaValue
	 *            the slaValue to set
	 */
	public void setSlaValue(String slaValue) {
		this.slaValue = slaValue;
	}

}