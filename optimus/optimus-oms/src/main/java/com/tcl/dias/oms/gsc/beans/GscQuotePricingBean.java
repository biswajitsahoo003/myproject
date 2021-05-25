package com.tcl.dias.oms.gsc.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Total price for quote which is related to Gsc Products
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscQuotePricingBean {

	private Integer quoteLeId;
	private Double totalMRC;
	private Double totalNRC;
	private Double totalTCV;
	private Double uifnRegistrationCharge;

	private List<GscQuotePriceBean> gscQuotePrices = new ArrayList<>();

	public GscQuotePricingBean(Integer quoteLeId) {
		this.quoteLeId = quoteLeId;
	}

	public Integer getQuoteLeId() {
		return quoteLeId;
	}

	public void setQuoteLeId(Integer quoteLeId) {
		this.quoteLeId = quoteLeId;
	}

	public Double getTotalMRC() {
		return totalMRC;
	}

	public void setTotalMRC(Double totalMRC) {
		this.totalMRC = totalMRC;
	}

	public Double getTotalNRC() {
		return totalNRC;
	}

	public void setTotalNRC(Double totalNRC) {
		this.totalNRC = totalNRC;
	}

	public Double getTotalTCV() {
		return totalTCV;
	}

	public void setTotalTCV(Double totalTCV) {
		this.totalTCV = totalTCV;
	}

	public Double getUifnRegistrationCharge() {
		return uifnRegistrationCharge;
	}

	public void setUifnRegistrationCharge(Double uifnRegistrationCharge) {
		this.uifnRegistrationCharge = uifnRegistrationCharge;
	}

	public List<GscQuotePriceBean> getGscQuotePrices() {
		return gscQuotePrices;
	}

	public void setGscQuotePrices(List<GscQuotePriceBean> gscQuotePrices) {
		this.gscQuotePrices = gscQuotePrices;
	}

}
