package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;
import java.util.List;

import com.tcl.dias.oms.beans.VproxySolutionBean;
/**
 * 
 * This is the request bean to persist the vProxy Solutions
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class VproxySolutionRequestBean implements Serializable{
	

	private Integer quoteId;
	private Integer quoteLeId;
	private String productName;
	private List<VproxySolutionBean> vproxySolutionsBeans;
	
	/**
	 * @return the vproxySolutionsBeans
	 */
	public List<VproxySolutionBean> getVproxySolutionsBeans() {
		return vproxySolutionsBeans;
	}
	/**
	 * @param vproxySolutionsBeans the vproxySolutionsBeans to set
	 */
	public void setVproxySolutionsBeans(List<VproxySolutionBean> vproxySolutionsBeans) {
		this.vproxySolutionsBeans = vproxySolutionsBeans;
	}
	public Integer getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}
	public Integer getQuoteLeId() {
		return quoteLeId;
	}
	public void setQuoteLeId(Integer quoteLeId) {
		this.quoteLeId = quoteLeId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

}
