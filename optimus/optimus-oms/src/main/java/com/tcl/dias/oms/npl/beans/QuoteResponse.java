package com.tcl.dias.oms.npl.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
	private Integer quoteleId;
	private SiteAndSolutionDto solution;
	private QuoteDto quoteDto;
	private List<NplLinkBean> nplLinks;
	private List<QuoteNplSiteDto> nplSiteDtos;
	
	public List<NplLinkBean> getNplLinks() {
		return nplLinks;
	}

	public void setNplLinks(List<NplLinkBean> nplLinks) {
		this.nplLinks = nplLinks;
	}

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

	

	@Override
	public String toString() {
		return "QuoteResponse [quoteId=" + quoteId + ", quoteleId=" + quoteleId + "]";
	}

	public List<QuoteNplSiteDto> getNplSiteDtos() {
		return nplSiteDtos;
	}

	public void setNplSiteDtos(List<QuoteNplSiteDto> nplSiteDtos) {
		this.nplSiteDtos = nplSiteDtos;
	}

}
