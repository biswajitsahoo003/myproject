package com.tcl.dias.oms.teamsdr.beans;

import com.tcl.dias.oms.beans.QuoteProductComponentsAttributeValueBean;

import java.util.List;

/**
 * TeamsDR manual price bean for oms page
 *
 * @author Srinivasa Raghavan
 */
public class TeamsDRManualPriceBean {
	private List<QuoteTeamsDRManualPriceBean> quoteTeamsDRManualPrices;
	private Double subtotalMrc;
	private Double subtotalNrc;
	private Double quoteToLeMrc;
	private Double quoteToLeNrc;
	private Double quoteToLeArc;
	private Double quoteToLeTcv;
	private QuoteProductComponentsAttributeValueBean quoteLeAttribute;

	public List<QuoteTeamsDRManualPriceBean> getQuoteTeamsDRManualPrices() {
		return quoteTeamsDRManualPrices;
	}

	public void setQuoteTeamsDRManualPrices(List<QuoteTeamsDRManualPriceBean> quoteTeamsDRManualPrices) {
		this.quoteTeamsDRManualPrices = quoteTeamsDRManualPrices;
	}

	public Double getSubtotalMrc() {
		return subtotalMrc;
	}

	public void setSubtotalMrc(Double subtotalMrc) {
		this.subtotalMrc = subtotalMrc;
	}

	public Double getSubtotalNrc() {
		return subtotalNrc;
	}

	public void setSubtotalNrc(Double subtotalNrc) {
		this.subtotalNrc = subtotalNrc;
	}

	public Double getQuoteToLeMrc() {
		return quoteToLeMrc;
	}

	public void setQuoteToLeMrc(Double quoteToLeMrc) {
		this.quoteToLeMrc = quoteToLeMrc;
	}

	public Double getQuoteToLeNrc() {
		return quoteToLeNrc;
	}

	public void setQuoteToLeNrc(Double quoteToLeNrc) {
		this.quoteToLeNrc = quoteToLeNrc;
	}

	public Double getQuoteToLeArc() {
		return quoteToLeArc;
	}

	public void setQuoteToLeArc(Double quoteToLeArc) {
		this.quoteToLeArc = quoteToLeArc;
	}

	public Double getQuoteToLeTcv() {
		return quoteToLeTcv;
	}

	public void setQuoteToLeTcv(Double quoteToLeTcv) {
		this.quoteToLeTcv = quoteToLeTcv;
	}

	public QuoteProductComponentsAttributeValueBean getQuoteLeAttribute() {
		return quoteLeAttribute;
	}

	public void setQuoteLeAttribute(QuoteProductComponentsAttributeValueBean quoteLeAttribute) {
		this.quoteLeAttribute = quoteLeAttribute;
	}
}
