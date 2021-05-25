package com.tcl.dias.common.beans;

import java.util.List;

/**
 * 
 * @author vpachava
 *
 */

public class VproxyProductOfferingBean {

	private String productOfferingName;

	private String solutionName;
	
	private List<String> productOfferingDescription;

	private List<VProxyAddonsBean> vProxyAddonsBeans;



	public String getProductOfferingName() {
		return productOfferingName;
	}

	public void setProductOfferingName(String productOfferingName) {
		this.productOfferingName = productOfferingName;
	}


	public List<String> getProductOfferingDescription() {
		return productOfferingDescription;
	}

	public void setProductOfferingDescription(List<String> productOfferingDescription) {
		this.productOfferingDescription = productOfferingDescription;
	}

	public List<VProxyAddonsBean> getvProxyAddonsBeans() {
		return vProxyAddonsBeans;
	}

	public void setvProxyAddonsBeans(List<VProxyAddonsBean> vProxyAddonsBeans) {
		this.vProxyAddonsBeans = vProxyAddonsBeans;
	}

	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	

	
}
