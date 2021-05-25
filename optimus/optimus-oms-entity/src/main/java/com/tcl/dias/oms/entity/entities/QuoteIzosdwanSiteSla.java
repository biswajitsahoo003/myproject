package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the quote_izosdwan_site_sla database table.
 * 
 */
@Entity
@Table(name="quote_izosdwan_site_sla")
@NamedQuery(name="QuoteIzosdwanSiteSla.findAll", query="SELECT q FROM QuoteIzosdwanSiteSla q")
public class QuoteIzosdwanSiteSla implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="sla_end_date")
	private Date slaEndDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="sla_start_date")
	private Date slaStartDate;

	@Column(name="sla_value")
	private String slaValue;

	//bi-directional many-to-one association to QuoteIzosdwanSite
	@ManyToOne
	@JoinColumn(name="izosdwan_site_id")
	private QuoteIzosdwanSite quoteIzosdwanSite;

	//bi-directional many-to-one association to SlaMaster
	@ManyToOne
	@JoinColumn(name="sla_master_id")
	private SlaMaster slaMaster;

	public QuoteIzosdwanSiteSla() {
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

	public String getSlaValue() {
		return this.slaValue;
	}

	public void setSlaValue(String slaValue) {
		this.slaValue = slaValue;
	}

	public QuoteIzosdwanSite getQuoteIzosdwanSite() {
		return this.quoteIzosdwanSite;
	}

	public void setQuoteIzosdwanSite(QuoteIzosdwanSite quoteIzosdwanSite) {
		this.quoteIzosdwanSite = quoteIzosdwanSite;
	}

	public SlaMaster getSlaMaster() {
		return this.slaMaster;
	}

	public void setSlaMaster(SlaMaster slaMaster) {
		this.slaMaster = slaMaster;
	}

}