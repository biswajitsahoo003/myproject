package com.tcl.dias.oms.webex.beans;

import java.math.BigDecimal;
import java.util.List;

/**
 * WebexLicensePriceUpdateBean for updating the prices of license line items
 *
 * @author Srinivasa Raghavan
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class WebexLicenseManualPricingBean {

	private Integer quoteLeId;
	private BigDecimal totalTcv;
	private BigDecimal totalMrc;
	private BigDecimal totalNrc;
	private BigDecimal totalArc;
	private List<QuoteUcaasBean> quoteUcaasBeans;

	public WebexLicenseManualPricingBean() {
	}

	public Integer getQuoteLeId() {
		return quoteLeId;
	}

	public void setQuoteLeId(Integer quoteLeId) {
		this.quoteLeId = quoteLeId;
	}

	public BigDecimal getTotalTcv() {
		return totalTcv;
	}

	public void setTotalTcv(BigDecimal totalTcv) {
		this.totalTcv = totalTcv;
	}

	public BigDecimal getTotalMrc() {
		return totalMrc;
	}

	public void setTotalMrc(BigDecimal totalMrc) {
		this.totalMrc = totalMrc;
	}

	public BigDecimal getTotalNrc() {
		return totalNrc;
	}

	public void setTotalNrc(BigDecimal totalNrc) {
		this.totalNrc = totalNrc;
	}

	public BigDecimal getTotalArc() {
		return totalArc;
	}

	public void setTotalArc(BigDecimal totalArc) {
		this.totalArc = totalArc;
	}

	public List<QuoteUcaasBean> getQuoteUcaasBeans() {
		return quoteUcaasBeans;
	}

	public void setQuoteUcaasBeans(List<QuoteUcaasBean> quoteUcaasBeans) {
		this.quoteUcaasBeans = quoteUcaasBeans;
	}

	@Override
	public String toString() {
		return "WebexLicenseManualPricingBean{" + "quoteLeId=" + quoteLeId + ", totalTcv=" + totalTcv + ", totalMrc="
				+ totalMrc + ", totalNrc=" + totalNrc + ", totalArc=" + totalArc + ", quoteUcaasBeans="
				+ quoteUcaasBeans + '}';
	}
}
