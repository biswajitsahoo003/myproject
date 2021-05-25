package com.tcl.dias.common.beans;

import java.util.List;

public class BomInventoryCatalogAssocResponse {
	
	private List<String> productCatalogCpeBoms;

	public List<String> getProductCatalogCpeBoms() {
		return productCatalogCpeBoms;
	}

	public void setProductCatalogCpeBoms(List<String> productCatalogCpeBoms) {
		this.productCatalogCpeBoms = productCatalogCpeBoms;
	}

	@Override
	public String toString() {
		return "BomInventoryCatalogAssocResponse [productCatalogCpeBoms=" + productCatalogCpeBoms + "]";
	}
	
	
	

}
