package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.format.annotation.NumberFormat;
/**
 * 
 * This binds the price of components 
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ComponentDetailsBean implements Serializable{
	private String name;
	@NumberFormat(pattern = "#,###,###,###.##")
	private BigDecimal values;
	private String actionType;
	private String orderType;
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	private SubComponentDetailsBean subComponentDetailsBean;
	public SubComponentDetailsBean getSubComponentDetailsBean() {
		return subComponentDetailsBean;
	}
	public void setSubComponentDetailsBean(SubComponentDetailsBean subComponentDetailsBean) {
		this.subComponentDetailsBean = subComponentDetailsBean;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getValues() {
		return values;
	}
	public void setValues(BigDecimal values) {
		this.values = values;
	}
	
}
