package com.tcl.dias.oms.renewals.bean;

import java.util.List;
import java.util.Map;

import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.ProductSolutionSiLink;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.UpdateGstRequest;

public class RenewalsProductsolutionLinkMap {
	
	Map<String, ProductSolution> productsolutionLinkMap ;
	
	private QuoteToLe quoteTole;

	private List<UpdateGstRequest> updateList;
	
	public Map<String, ProductSolution> getProductsolutionLinkMap() {
		return productsolutionLinkMap;
	}

	public void setProductsolutionLinkMap(Map<String, ProductSolution> productsolutionLinkMap) {
		this.productsolutionLinkMap = productsolutionLinkMap;
	}

	public QuoteToLe getQuoteTole() {
		return quoteTole;
	}

	public void setQuoteTole(QuoteToLe quoteTole) {
		this.quoteTole = quoteTole;
	}

	public List<UpdateGstRequest> getUpdateList() {
		return updateList;
	}

	public void setUpdateList(List<UpdateGstRequest> updateList) {
		this.updateList = updateList;
	}	

}
