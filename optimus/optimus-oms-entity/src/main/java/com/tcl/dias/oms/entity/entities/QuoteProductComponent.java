package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
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
 * Entity class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "quote_product_component")
@NamedQuery(name = "QuoteProductComponent.findAll", query = "SELECT q FROM QuoteProductComponent q")
public class QuoteProductComponent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "reference_id")
	private Integer referenceId;

	@Column(name = "type")
	private String type;

	// bi-directional many-to-one association to MstProductComponent
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_component_id")
	private MstProductComponent mstProductComponent;

	// bi-directional many-to-one association to MstProductFamily
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_family_id")
	private MstProductFamily mstProductFamily;

	// bi-directional many-to-one association to
	// QuoteProductComponentsAttributeValue
	@OneToMany(mappedBy = "quoteProductComponent")
	private Set<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues;
	
	@Column(name = "reference_name")
	private String referenceName;

	public String getReferenceName() {
		return referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	public QuoteProductComponent() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReferenceId() {
		return this.referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	public MstProductComponent getMstProductComponent() {
		return this.mstProductComponent;
	}

	public void setMstProductComponent(MstProductComponent mstProductComponent) {
		this.mstProductComponent = mstProductComponent;
	}

	public MstProductFamily getMstProductFamily() {
		return this.mstProductFamily;
	}

	public void setMstProductFamily(MstProductFamily mstProductFamily) {
		this.mstProductFamily = mstProductFamily;
	}

	public Set<QuoteProductComponentsAttributeValue> getQuoteProductComponentsAttributeValues() {
		return this.quoteProductComponentsAttributeValues;
	}

	public void setQuoteProductComponentsAttributeValues(
			Set<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues) {
		this.quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValues;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public QuoteProductComponentsAttributeValue addQuoteProductComponentsAttributeValue(
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue) {
		getQuoteProductComponentsAttributeValues().add(quoteProductComponentsAttributeValue);
		quoteProductComponentsAttributeValue.setQuoteProductComponent(this);

		return quoteProductComponentsAttributeValue;
	}

	public QuoteProductComponentsAttributeValue removeQuoteProductComponentsAttributeValue(
			QuoteProductComponentsAttributeValue quoteProductComponentsAttributeValue) {
		getQuoteProductComponentsAttributeValues().remove(quoteProductComponentsAttributeValue);
		quoteProductComponentsAttributeValue.setQuoteProductComponent(null);

		return quoteProductComponentsAttributeValue;
	}

}