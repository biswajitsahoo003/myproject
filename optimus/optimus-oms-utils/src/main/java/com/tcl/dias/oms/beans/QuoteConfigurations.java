package com.tcl.dias.oms.beans;

import java.util.List;

/**
 * Bean file
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class QuoteConfigurations {

	private List<QuoteConfiguration> activeQuotes;
	private Long totalElements;
	private Integer totalPages;

	public List<QuoteConfiguration> getActiveQuotes() {
		return activeQuotes;
	}

	public void setActiveQuotes(List<QuoteConfiguration> activeQuotes) {
		this.activeQuotes = activeQuotes;
	}

	public Long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	@Override
	public String toString() {
		return "QuoteConfigurations [activeQuotes=" + activeQuotes + "totalElements=" + totalElements + "totalPages="
				+ totalPages + "]";
	}

}
