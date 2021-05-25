package com.tcl.dias.serviceinventory.beans;

import java.util.List;

public class ServiceCatalogRequest {
	
    private Integer productId;
    
    private String product;
    
    // Populate this for all products other than GSC
    private List<String> serviceIds;

    // Populate this in case of GSC
    private List<GSCServiceCatalogRequest> gscServiceRequest;
    
    
    public Integer getProductId() {
           return productId;
    }

    public void setProductId(Integer productId) {
           this.productId = productId;
    }

    public List<String> getServiceIds() {
           return serviceIds;
    }

    public void setServiceIds(List<String> serviceIds) {
           this.serviceIds = serviceIds;
    }

    public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public List<GSCServiceCatalogRequest> getGscServiceRequest() {
		return gscServiceRequest;
	}

	public void setGscServiceRequest(List<GSCServiceCatalogRequest> gscServiceRequest) {
		this.gscServiceRequest = gscServiceRequest;
	}
	
}
