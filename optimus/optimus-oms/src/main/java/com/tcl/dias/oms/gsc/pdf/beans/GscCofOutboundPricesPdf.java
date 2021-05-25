package com.tcl.dias.oms.gsc.pdf.beans;

import java.util.List;

import com.tcl.dias.oms.gsc.pricing.beans.GscOutboundPriceBean;

/**
 * Outbound bean for dynamic pdf generation
 * 
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscCofOutboundPricesPdf {

	private List<GscOutboundPriceBean> gscOutboundPriceBean;

	private String dynamicColoumnName;

	private String orderType;

	private  String cofRefernceNumber;

	private String orderDate;

	public String getOrderType() {
		return orderType;
	}

	public String getCofRefernceNumber() {
		return cofRefernceNumber;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public void setCofRefernceNumber(String cofRefernceNumber) {
		this.cofRefernceNumber = cofRefernceNumber;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}



	public List<GscOutboundPriceBean> getGscOutboundPriceBean() {
		return gscOutboundPriceBean;
	}

	public void setGscOutboundPriceBean(List<GscOutboundPriceBean> gscOutboundPriceBean) {
		this.gscOutboundPriceBean = gscOutboundPriceBean;
	}

	public String getDynamicColoumnName() {
		return dynamicColoumnName;
	}

	public void setDynamicColoumnName(String dynamicColoumnName) {
		this.dynamicColoumnName = dynamicColoumnName;
	}
}
