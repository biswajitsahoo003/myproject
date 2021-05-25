package com.tcl.dias.oms.entity.entities;

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
 * @author vpachava This table consists of questionnaire information of izosdwan
 *         product
 *
 */

@Entity
@Table(name = "quote_izosdwan_attribute_values")
@NamedQuery(name = "QuoteIzoSdwanAttributeValues.findAll", query = "SELECT q FROM QuoteIzoSdwanAttributeValues q")
public class QuoteIzoSdwanAttributeValues {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String quoteVersion;

	@Column(name = "attribute_value")
	private String attributeValue;

	@Column(name = "display_value")
	private String displayValue;

	// bi-directional many-to-one association to QuoteToLe
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quote_id")
	private Quote quote;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getQuoteVersion() {
		return quoteVersion;
	}

	public void setQuoteVersion(String quoteVersion) {
		this.quoteVersion = quoteVersion;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public Quote getQuote() {
		return quote;
	}

	public void setQuote(Quote quote) {
		this.quote = quote;
	}

	@Override
	public String toString() {
		return "QuoteIzoSdwanAttributeValues [id=" + id + ", quoteVersion=" + quoteVersion + ", attributeValue="
				+ attributeValue + ", displayValue=" + displayValue + ", quote=" + quote + "]";
	}

	
}
