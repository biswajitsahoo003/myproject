package com.tcl.dias.nso.dto;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

import com.tcl.dias.oms.entity.entities.QuoteProductComponent;

/**
 * This file contains the QuoteProductComponentDto.java class. Dto Class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class QuoteProductComponentDto implements Serializable {

	private static final long serialVersionUID = -7189354404059483164L;

	private Integer id;

	private Integer referenceId;

	private MstProductComponentDto mstProductComponent;

	private MstProductFamilyDto mstProductFamily;

	private Set<QuoteProductComponentsAttributeValueDto> quoteProductComponentsAttributeValues;

	private QuotePriceDto quotePriceDto;

	public QuoteProductComponentDto() {
		// DO NOTHING
	}

	public QuoteProductComponentDto(QuoteProductComponent quoteProductComponent) {
		if (quoteProductComponent != null) {

			this.id = quoteProductComponent.getId();
			this.mstProductFamily = new MstProductFamilyDto(quoteProductComponent.getMstProductFamily());
			this.referenceId = quoteProductComponent.getReferenceId();
			this.mstProductComponent = quoteProductComponent.getMstProductComponent() != null
					? new MstProductComponentDto(quoteProductComponent.getMstProductComponent())
					: null;
			if (quoteProductComponent.getQuoteProductComponentsAttributeValues() != null) {
				this.quoteProductComponentsAttributeValues = quoteProductComponent
						.getQuoteProductComponentsAttributeValues().stream()
						.map(QuoteProductComponentsAttributeValueDto::new).collect(Collectors.toSet());
			}
		}

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

	/**
	 * @return the mstProductComponent
	 */
	public MstProductComponentDto getMstProductComponent() {
		return mstProductComponent;
	}

	/**
	 * @param mstProductComponent
	 *            the mstProductComponent to set
	 */
	public void setMstProductComponent(MstProductComponentDto mstProductComponent) {
		this.mstProductComponent = mstProductComponent;
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
	 * @return the quoteProductComponentsAttributeValues
	 */
	public Set<QuoteProductComponentsAttributeValueDto> getQuoteProductComponentsAttributeValues() {
		return quoteProductComponentsAttributeValues;
	}

	/**
	 * @param quoteProductComponentsAttributeValues
	 *            the quoteProductComponentsAttributeValues to set
	 */
	public void setQuoteProductComponentsAttributeValues(
			Set<QuoteProductComponentsAttributeValueDto> quoteProductComponentsAttributeValues) {
		this.quoteProductComponentsAttributeValues = quoteProductComponentsAttributeValues;
	}

	/**
	 * @return the quotePriceDto
	 */
	public QuotePriceDto getQuotePriceDto() {
		return quotePriceDto;
	}

	/**
	 * @param quotePriceDto
	 *            the quotePriceDto to set
	 */
	public void setQuotePriceDto(QuotePriceDto quotePriceDto) {
		this.quotePriceDto = quotePriceDto;
	}

	/**
	 * toString
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return "QuoteProductComponentDto [id=" + id + ", referenceId=" + referenceId + ", mstProductComponent="
				+ mstProductComponent + ", mstProductFamily=" + mstProductFamily
				+ ", quoteProductComponentsAttributeValues=" + quoteProductComponentsAttributeValues
				+ ", quotePriceDto=" + quotePriceDto + "]";
	}

}