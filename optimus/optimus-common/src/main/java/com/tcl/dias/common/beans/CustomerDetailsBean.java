package com.tcl.dias.common.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This file contains the CustomerDetailsBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CustomerDetailsBean implements  Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1440659712489125141L;
	private List<CustomerAttributeBean> customerAttributes;

	/**
	 * @return the customerAttributes
	 */
	public List<CustomerAttributeBean> getCustomerAttributes() {
		
		if(customerAttributes==null) {
			customerAttributes=new ArrayList<>();
		}
		return customerAttributes;
	}

	/**
	 * @param customerAttributes the customerAttributes to set
	 */
	public void setCustomerAttributes(List<CustomerAttributeBean> customerAttributes) {
		this.customerAttributes = customerAttributes;
	}
	
	
	

}
