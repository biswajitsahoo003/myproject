/**
 * 
 */
package com.tcl.dias.nso.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.nso.dto.QuoteDto;
import com.tcl.dias.nso.dto.QuoteIllSiteDto;
import com.tcl.dias.nso.dto.SiteAndSolutionDto;

/**
 * @author KarMani
 *
 */


@JsonInclude(Include.NON_NULL)
public class QuoteResponse {

	private Integer quoteId;
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
		return "QuoteResponse{" +
				"quoteId=" + quoteId +
				", quoteleId=" + quoteleId +
				", solution=" + solution +
				", quoteDto=" + quoteDto +
				", illSiteDtos=" + illSiteDtos +
				", classification='" + classification + '\'' +
				'}';
	}
}

