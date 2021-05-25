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
 * Entity Class for quote_npl_link_sla table
 * 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "quote_npl_link_sla")
@NamedQuery(name = "QuoteNplLinkSla.findAll", query = "SELECT q FROM QuoteNplLinkSla q")
public class QuoteNplLinkSla implements Serializable {

	/**
	 * 
	 */
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quote_npl_link_id")
	private QuoteNplLink quoteNplLink;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sla_master_id")
	private SlaMaster slaMaster;
	
	@Column(name = "sla_value")
	private String slaValue;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getSlaEndDate() {
		return slaEndDate;
	}

	public void setSlaEndDate(Date slaEndDate) {
		this.slaEndDate = slaEndDate;
	}

	public Date getSlaStartDate() {
		return slaStartDate;
	}

	public void setSlaStartDate(Date slaStartDate) {
		this.slaStartDate = slaStartDate;
	}

	public QuoteNplLink getQuoteNplLink() {
		return quoteNplLink;
	}

	public void setQuoteNplLink(QuoteNplLink quoteNplLink) {
		this.quoteNplLink = quoteNplLink;
	}

	public SlaMaster getSlaMaster() {
		return slaMaster;
	}

	public void setSlaMaster(SlaMaster slaMaster) {
		this.slaMaster = slaMaster;
	}

	public String getSlaValue() {
		return slaValue;
	}

	public void setSlaValue(String slaValue) {
		this.slaValue = slaValue;
	}

	
	
}