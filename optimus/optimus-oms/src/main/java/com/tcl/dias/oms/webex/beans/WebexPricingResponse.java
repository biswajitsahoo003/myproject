package com.tcl.dias.oms.webex.beans;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Response bean for Webex Pricing
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "error_flag", "error_msg", "totalTcv", "totalmrc", "totalnrc", "totalarc", "ucaasQuotes" })
public class WebexPricingResponse {

	@JsonProperty("error_flag")
	private String errorFlag;

	@JsonProperty("error_msg")
	private List<String> errorMessages;

	@JsonProperty("totalTcv")
	private BigDecimal totalTcv;

	@JsonProperty("totalmrc")
	private BigDecimal totalMrc;

	@JsonProperty("totalnrc")
	private BigDecimal totalNrc;

	@JsonProperty("totalarc")
	private BigDecimal totalArc;

	@JsonProperty("ucaasQuotes")
	private List<PricingUcaasQuotesBean> ucaasQuotes;

	public WebexPricingResponse() {
	}

	@JsonProperty("error_flag")
	public String getErrorFlag() {
		return errorFlag;
	}

	@JsonProperty("error_flag")
	public void setErrorFlag(String errorFlag) {
		this.errorFlag = errorFlag;
	}

	@JsonProperty("error_msg")
	public List<String> getErrorMessages() {
		return errorMessages;
	}

	@JsonProperty("error_msg")
	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}

	@JsonProperty("totalTcv")
	public BigDecimal getTotalTcv() {
		return totalTcv;
	}

	@JsonProperty("totalTcv")
	public void setTotalTcv(BigDecimal totalTcv) {
		this.totalTcv = totalTcv;
	}

	@JsonProperty("totalmrc")
	public BigDecimal getTotalMrc() {
		return totalMrc;
	}

	@JsonProperty("totalmrc")
	public void setTotalMrc(BigDecimal totalMrc) {
		this.totalMrc = totalMrc;
	}

	@JsonProperty("totalnrc")
	public BigDecimal getTotalNrc() {
		return totalNrc;
	}

	@JsonProperty("totalnrc")
	public void setTotalNrc(BigDecimal totalNrc) {
		this.totalNrc = totalNrc;
	}

	@JsonProperty("totalarc")
	public BigDecimal getTotalArc() {
		return totalArc;
	}

	@JsonProperty("totalarc")
	public void setTotalArc(BigDecimal totalArc) {
		this.totalArc = totalArc;
	}

	@JsonProperty("ucaasQuotes")
	public List<PricingUcaasQuotesBean> getUcaasQuotes() {
		return ucaasQuotes;
	}

	@JsonProperty("ucaasQuotes")
	public void setUcaasQuotes(List<PricingUcaasQuotesBean> ucaasQuotes) {
		this.ucaasQuotes = ucaasQuotes;
	}

	@Override
	public String toString() {
		return "WebexPricingResponse [errorFlag=" + errorFlag + ", errorMessages=" + errorMessages + ", totalTcv="
				+ totalTcv + ", totalMrc=" + totalMrc + ", totalNrc=" + totalNrc + ", totalArc=" + totalArc
				+ ", ucaasQuotes=" + ucaasQuotes + "]";
	}

}
