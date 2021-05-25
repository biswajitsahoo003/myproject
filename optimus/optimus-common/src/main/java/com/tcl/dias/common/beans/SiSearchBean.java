package com.tcl.dias.common.beans;

import java.util.List;
/**
 * 
 * This is the bean class used to filter data present in service inventory
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SiSearchBean {
	
	private String customerId;
	private List<Integer> leIds;
	private List<String> countries;
	private List<String> products;
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public List<Integer> getLeIds() {
		return leIds;
	}
	public void setLeIds(List<Integer> leIds) {
		this.leIds = leIds;
	}
	public List<String> getCountries() {
		return countries;
	}
	public void setCountries(List<String> countries) {
		this.countries = countries;
	}
	public List<String> getProducts() {
		return products;
	}
	public void setProducts(List<String> products) {
		this.products = products;
	}

}
