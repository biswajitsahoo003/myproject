package com.tcl.dias.oms.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.dto.QuoteIllSiteDto;
import com.tcl.dias.oms.dto.QuoteDto;
import com.tcl.dias.oms.dto.SiteAndSolutionDto;

/**
 * This file contains the QuoteResponse.java class.
 * Bean class
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class QuoteResponse {

	private Integer quoteId;
	private String quoteCode;
	private Integer quoteleId;
	private SiteAndSolutionDto solution;
	private QuoteDto quoteDto;
	private List<QuoteIllSiteDto> illSiteDtos;
	private String classification;

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public String getQuoteCode() {
		return quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	public Integer getQuoteleId() {
		return quoteleId;
	}

	public void setQuoteleId(Integer quoteleId) {
		this.quoteleId = quoteleId;
	}

	/**
	 * @return the solution
	 */
	public SiteAndSolutionDto getSolution() {
		return solution;
	}

	/**
	 * @param solution
	 *            the solution to set
	 */
	public void setSolution(SiteAndSolutionDto solution) {
		this.solution = solution;
	}

	/**
	 * @return the quoteDto
	 */
	public QuoteDto getQuoteDto() {
		return quoteDto;
	}

	/**
	 * @param quoteDto
	 *            the quoteDto to set
	 */
	public void setQuoteDto(QuoteDto quoteDto) {
		this.quoteDto = quoteDto;
	}

	/**
	 * @return the illSiteDtos
	 */
	public List<QuoteIllSiteDto> getIllSiteDtos() {
		return illSiteDtos;
	}

	/**
	 * @param illSiteDtos
	 *            the illSiteDtos to set
	 */
	public void setIllSiteDtos(List<QuoteIllSiteDto> illSiteDtos) {
		this.illSiteDtos = illSiteDtos;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	@Override
	public String toString() {
		return "QuoteResponse [quoteId=" + quoteId + ", quoteCode=" + quoteCode + ", quoteleId=" + quoteleId
				+ ", solution=" + solution + ", quoteDto=" + quoteDto + ", illSiteDtos=" + illSiteDtos
				+ ", classification=" + classification + "]";
	}
}
