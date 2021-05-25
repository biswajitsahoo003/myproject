package com.tcl.dias.oms.beans;

import java.util.List;

import com.tcl.dias.oms.beans.CompareQuotePrices;

/**
 * This class is the bean class for Compare Quotes API
 * @author SURUCHIA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

public class CompareQuotes {

	private List<CompareQuotePrices> prices;
	
	private TotalSolutionQuote solutionQuote;
	
	
	

	public TotalSolutionQuote getSolutionQuote() {
		return solutionQuote;
	}

	public void setSolutionQuote(TotalSolutionQuote solutionQuote) {
		this.solutionQuote = solutionQuote;
	}

	public List<CompareQuotePrices> getPrices() {
		return prices;
	}

	public void setPrices(List<CompareQuotePrices> prices) {
		this.prices = prices;
	}

	@Override
	public String toString() {
		return "CompareQuotes [prices=" + prices + "]";
	}
}
