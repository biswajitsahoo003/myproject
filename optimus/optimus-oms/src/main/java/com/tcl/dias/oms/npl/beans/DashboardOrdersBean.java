package com.tcl.dias.oms.npl.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.beans.LegalAttributeBean;

/**
 * 
 * This file contains the order bean for Dashboard.
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class DashboardOrdersBean {

	private NplOrdersBean orders;
	
	private List<OrderNplSiteBean> orderNplSite;
	
	private List<LegalAttributeBean> legalAttributes;
	
	/**
	 * 
	 * getOrders to get the orders
	 * @return
	 */

	public NplOrdersBean getOrders() {
		return orders;
	}
	
	/**
	 * 
	 * setOrders the orderbeans to set
	 * @param orders
	 */

	public void setOrders(NplOrdersBean orders) {
		this.orders = orders;
	}
	
	/**
	 * 
	 * getOrderIllSite to get the order NPL site
	 * @return
	 */

	public List<OrderNplSiteBean> getOrderNplSite() {
		return orderNplSite;
	}
	
	/**
	 * 
	 * setOrderIllSite the order NPL site beans to set
	 * @param orderIllSite
	 */

	public void setOrderIllSite(List<OrderNplSiteBean> orderNplSite) {
		this.orderNplSite = orderNplSite;
	}
	
	/**
	 * 
	 * getLegalAttributes
	 * @return returns the legal attributes list
	 */

	public List<LegalAttributeBean> getLegalAttributes() {
		return legalAttributes;
	}
	
	/**
	 * 
	 * setLegalAttributes the legal attributes to set
	 * @param legalAttributes
	 */

	public void setLegalAttributes(List<LegalAttributeBean> legalAttributes) {
		this.legalAttributes = legalAttributes;
	}
	
	
	
}
