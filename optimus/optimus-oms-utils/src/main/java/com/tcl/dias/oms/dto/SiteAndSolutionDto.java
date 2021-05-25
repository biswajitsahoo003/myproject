package com.tcl.dias.oms.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * This file contains the BomDetailsDto.java class. Dto Class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SiteAndSolutionDto {

	List<QuoteProductComponentDto> quoteComponentDtos;

	List<QuoteIllSiteDto> illSiteDtos;

	/**
	 * @return the quoteComponentDtos
	 */
	public List<QuoteProductComponentDto> getQuoteComponentDtos() {
		return quoteComponentDtos;
	}

	/**
	 * @param quoteComponentDtos
	 *            the quoteComponentDtos to set
	 */
	public void setQuoteComponentDtos(List<QuoteProductComponentDto> quoteComponentDtos) {
		this.quoteComponentDtos = quoteComponentDtos;
	}

	/**
	 * @return the illSiteDtos
	 */
	public List<QuoteIllSiteDto> getIllSiteDtos() {
		if (illSiteDtos == null) {
			illSiteDtos = new ArrayList<>();
		}
		return illSiteDtos;
	}

	/**
	 * @param illSiteDtos
	 *            the illSiteDtos to set
	 */
	public void setIllSiteDtos(List<QuoteIllSiteDto> illSiteDtos) {
		this.illSiteDtos = illSiteDtos;
	}

}
