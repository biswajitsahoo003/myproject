package com.tcl.dias.oms.renewals.bean;

import java.util.List;
import java.util.Map;

import com.tcl.dias.oms.entity.entities.QuotePrice;

public class RenevalsIllSiteBean {

	private Map<Integer, RenewalsPriceBean> illSitePrice;
	
	private List<QuotePrice> quotePriceList;
	
	Double totalMrc = 0D;
	Double totalNrc = 0D;
	Double totalArc = 0D;
	
	public Map<Integer, RenewalsPriceBean> getIllSitePrice() {
		return illSitePrice;
	}
	
	public void setIllSitePrice(Map<Integer, RenewalsPriceBean> illSitePrice) {
		this.illSitePrice = illSitePrice;
	}
	public List<QuotePrice> getQuotePriceList() {
		return quotePriceList;
	}
	public void setQuotePriceList(List<QuotePrice> quotePriceList) {
		this.quotePriceList = quotePriceList;
	}
	public Double getTotalMrc() {
		return totalMrc;
	}
	public void setTotalMrc(Double totalMrc) {
		this.totalMrc = totalMrc;
	}
	public Double getTotalNrc() {
		return totalNrc;
	}
	public void setTotalNrc(Double totalNrc) {
		this.totalNrc = totalNrc;
	}
	public Double getTotalArc() {
		return totalArc;
	}
	public void setTotalArc(Double totalArc) {
		this.totalArc = totalArc;
	}
	
	
	
	
	
}
