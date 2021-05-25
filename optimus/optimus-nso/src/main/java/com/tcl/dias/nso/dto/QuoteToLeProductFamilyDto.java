package com.tcl.dias.nso.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Dto Class for legal entity
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class QuoteToLeProductFamilyDto implements Serializable {

	private static final long serialVersionUID = -3837186287576524648L;

	private Integer id;

	private Set<ProductSolutionDto> productSolutions;

	private MstProductFamilyDto mstProductFamily;

	private QuoteToLeDto quoteToLe;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the productSolutions
	 */
	public Set<ProductSolutionDto> getProductSolutions() {
		if (productSolutions == null) {
			productSolutions = new HashSet<>();
		}
		return productSolutions;
	}

	/**
	 * @param productSolutions
	 *            the productSolutions to set
	 */
	public void setProductSolutions(Set<ProductSolutionDto> productSolutions) {
		this.productSolutions = productSolutions;
	}

	/**
	 * @return the mstProductFamily
	 */
	public MstProductFamilyDto getMstProductFamily() {
		return mstProductFamily;
	}

	/**
	 * @param mstProductFamily
	 *            the mstProductFamily to set
	 */
	public void setMstProductFamily(MstProductFamilyDto mstProductFamily) {
		this.mstProductFamily = mstProductFamily;
	}

	/**
	 * @return the quoteToLe
	 */
	public QuoteToLeDto getQuoteToLe() {
		return quoteToLe;
	}

	/**
	 * @param quoteToLe
	 *            the quoteToLe to set
	 */
	public void setQuoteToLe(QuoteToLeDto quoteToLe) {
		this.quoteToLe = quoteToLe;
	}

}