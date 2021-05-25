package com.tcl.dias.oms.izosdwan.beans;

import java.math.BigDecimal;

import org.springframework.format.annotation.NumberFormat;

public class QuotePricingDetailsBean {
	private SolutionPricingDetailsBean izosdwan;
	private String termsInMonths;
	private String currency;
	@NumberFormat(pattern = "#,###,###,###.##")
	private BigDecimal arc;
	@NumberFormat(pattern = "#,###,###,###.##")
	private BigDecimal nrc;
	@NumberFormat(pattern = "#,###,###,###.##")
	private BigDecimal mrc;
	@NumberFormat(pattern = "#,###,###,###.##")
	private BigDecimal tcv;
	@NumberFormat(pattern = "#,###,###,###.##")
	private BigDecimal tcvMrc;
	public BigDecimal getArc() {
		return arc;
	}
	public void setArc(BigDecimal arc) {
		this.arc = arc;
	}
	public BigDecimal getNrc() {
		return nrc;
	}
	public void setNrc(BigDecimal nrc) {
		this.nrc = nrc;
	}
	public BigDecimal getMrc() {
		return mrc;
	}
	public void setMrc(BigDecimal mrc) {
		this.mrc = mrc;
	}
	public BigDecimal getTcv() {
		return tcv;
	}
	public void setTcv(BigDecimal tcv) {
		this.tcv = tcv;
	}
	public SolutionPricingDetailsBean getIzosdwan() {
		return izosdwan;
	}
	public void setIzosdwan(SolutionPricingDetailsBean izosdwan) {
		this.izosdwan = izosdwan;
	}
	public String getTermsInMonths() {
		return termsInMonths;
	}
	public void setTermsInMonths(String termsInMonths) {
		this.termsInMonths = termsInMonths;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getTcvMrc() {
		return tcvMrc;
	}

	public void setTcvMrc(BigDecimal tcvMrc) {
		this.tcvMrc = tcvMrc;
	}
}
