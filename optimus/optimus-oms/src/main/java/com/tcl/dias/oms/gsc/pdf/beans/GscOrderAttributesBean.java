package com.tcl.dias.oms.gsc.pdf.beans;

import java.util.List;

import com.tcl.dias.oms.gsc.beans.GscOrderProductComponentsAttributeValueBean;

/**
 * Attributes for GSC Orders
 * 
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscOrderAttributesBean {
	private Integer orderToLeId;
	private List<GscOrderProductComponentsAttributeValueBean> attributes;

	public Integer getOrderToLeId() {
		return orderToLeId;
	}

	public void setOrderToLeId(Integer orderToLeId) {
		this.orderToLeId = orderToLeId;
	}

	public List<GscOrderProductComponentsAttributeValueBean> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<GscOrderProductComponentsAttributeValueBean> attributes) {
		this.attributes = attributes;
	}

}
