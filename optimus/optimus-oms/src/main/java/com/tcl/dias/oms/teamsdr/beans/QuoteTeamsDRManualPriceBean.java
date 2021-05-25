package com.tcl.dias.oms.teamsdr.beans;

import com.tcl.dias.oms.beans.QuoteProductComponentsAttributeValueBean;

import java.util.List;

/**
 * Quote Teams dr manual price bean for oms page
 *
 * @author srraghav
 */
public class QuoteTeamsDRManualPriceBean {
	private Integer quoteTeamsDRId;
	private String plan;
	private String serviceName;
	private List<QuoteProductComponentsAttributeValueBean> attributes;

	public Integer getQuoteTeamsDRId() {
		return quoteTeamsDRId;
	}

	public void setQuoteTeamsDRId(Integer quoteTeamsDRId) {
		this.quoteTeamsDRId = quoteTeamsDRId;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public List<QuoteProductComponentsAttributeValueBean> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<QuoteProductComponentsAttributeValueBean> attributeValueBeans) {
		this.attributes = attributeValueBeans;
	}
}
