package com.tcl.dias.oms.webex.beans;

import java.math.BigDecimal;
import java.util.List;

/**
 * This class contains UCAAS pricing fields.
 *
 * @author ssyed ali.
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class WebexQuotePricingBean {
	public Boolean errorFlag;
	public List<String> errorMsg;
	public BigDecimal totalTcv;
	public BigDecimal totalMrc;
	public BigDecimal totalNrc;
	public BigDecimal totalArc;
	public List<WebexUcaasQuotePriceBean> ucaasQuotes;

	public WebexQuotePricingBean() {
	}

	public List<WebexUcaasQuotePriceBean> getUcaasQuotes() {
		return ucaasQuotes;
	}

	public void setUcaasQuotes(List<WebexUcaasQuotePriceBean> ucaasQuotes) {
		this.ucaasQuotes = ucaasQuotes;
	}

	public Boolean getErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(Boolean errorFlag) {
		this.errorFlag = errorFlag;
	}

	public List<String> getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(List<String> errorMsg) {
		this.errorMsg = errorMsg;
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

	@Override
	public String toString() {
		return "WebexQuotePricingBean [errorFlag=" + errorFlag + ", errorMsg=" + errorMsg + ", totalTcv=" + totalTcv
				+ ", totalMrc=" + totalMrc + ", totalNrc=" + totalNrc + ", totalArc=" + totalArc + ", ucaasQuotes="
				+ ucaasQuotes + "]";
	}

}
