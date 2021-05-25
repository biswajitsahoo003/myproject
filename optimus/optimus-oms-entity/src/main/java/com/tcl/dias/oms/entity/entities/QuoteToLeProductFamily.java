package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
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
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "quote_to_le_product_family")
@NamedQuery(name = "QuoteToLeProductFamily.findAll", query = "SELECT q FROM QuoteToLeProductFamily q")
public class QuoteToLeProductFamily implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;


	// bi-directional many-to-one association to ProductSolution
	@OneToMany(mappedBy = "quoteToLeProductFamily")
	private Set<ProductSolution> productSolutions;

	// bi-directional many-to-one association to MstProductFamily
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_family_id")
	private MstProductFamily mstProductFamily;

	// bi-directional many-to-one association to QuoteToLe
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quote_to_le_id")
	private QuoteToLe quoteToLe;

	public QuoteToLeProductFamily() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Set<ProductSolution> getProductSolutions() {
		return this.productSolutions;
	}

	public void setProductSolutions(Set<ProductSolution> productSolutions) {
		this.productSolutions = productSolutions;
	}

	public ProductSolution addProductSolution(ProductSolution productSolution) {
		getProductSolutions().add(productSolution);
		productSolution.setQuoteToLeProductFamily(this);

		return productSolution;
	}

	public ProductSolution removeProductSolution(ProductSolution productSolution) {
		getProductSolutions().remove(productSolution);
		productSolution.setQuoteToLeProductFamily(null);

		return productSolution;
	}

	public MstProductFamily getMstProductFamily() {
		return this.mstProductFamily;
	}

	public void setMstProductFamily(MstProductFamily mstProductFamily) {
		this.mstProductFamily = mstProductFamily;
	}

	public QuoteToLe getQuoteToLe() {
		return this.quoteToLe;
	}

	public void setQuoteToLe(QuoteToLe quoteToLe) {
		this.quoteToLe = quoteToLe;
	}

}