package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * 
 * This file contains the QuoteGscSla.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "quote_gsc_sla")
@NamedQuery(name = "QuoteGscSla.findAll", query = "SELECT q FROM QuoteGscSla q")
public class QuoteGscSla implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name = "attribute_name", length = 200)
	private String attributeName;

	@Column(name = "attribute_value", length = 200)
	private String attributeValue;

	// bi-directional many-to-one association to QuoteGsc
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quote_gsc_id")
	private QuoteGsc quoteGsc;

	// bi-directional many-to-one association to SlaMaster
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sla_master_id")
	private SlaMaster slaMaster;

	public QuoteGscSla() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAttributeName() {
		return this.attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeValue() {
		return this.attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public QuoteGsc getQuoteGsc() {
		return this.quoteGsc;
	}

	public void setQuoteGsc(QuoteGsc quoteGsc) {
		this.quoteGsc = quoteGsc;
	}

	public SlaMaster getSlaMaster() {
		return this.slaMaster;
	}

	public void setSlaMaster(SlaMaster slaMaster) {
		this.slaMaster = slaMaster;
	}

}