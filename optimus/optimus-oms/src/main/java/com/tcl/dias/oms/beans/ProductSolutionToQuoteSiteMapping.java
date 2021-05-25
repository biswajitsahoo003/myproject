package com.tcl.dias.oms.beans;

import java.util.List;

import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;

public class ProductSolutionToQuoteSiteMapping {
	ProductSolution solution;
    List<QuoteIllSite> sites;
	public ProductSolution getSolution() {
		return solution;
	}
	public void setSolution(ProductSolution solution) {
		this.solution = solution;
	}
	public List<QuoteIllSite> getSites() {
		return sites;
	}
	public void setSites(List<QuoteIllSite> sites) {
		this.sites = sites;
	}
	@Override
	public String toString() {
		return "ProductSolutionToQuoteSiteMapping [solution=" + solution + ", sites=" + sites + "]";
	}
	
    
}
