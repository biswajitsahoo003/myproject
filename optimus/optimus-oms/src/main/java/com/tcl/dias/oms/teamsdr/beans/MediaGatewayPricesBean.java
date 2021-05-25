package com.tcl.dias.oms.teamsdr.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Media gateway prices request
 * 
 * @author srraghav
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MediaGatewayPricesBean {

	@JsonProperty("contract_term")
	private Integer contractTerm;

	@JsonProperty("mediagateway_details")
	private List<MediaGatewayPriceDetailsBean> mediaGatewayPriceDetails;

	@JsonProperty("total_arc")
	private Double totalArc;

	@JsonProperty("total_nrc")
	private Double totalNrc;

	@JsonProperty("total_mrc")
	private Double totalMrc;

	@JsonProperty("total_tcv")
	private Double totalTcv;

	public Integer getContractTerm() {
		return contractTerm;
	}

	public void setContractTerm(Integer contractTerm) {
		this.contractTerm = contractTerm;
	}

	public List<MediaGatewayPriceDetailsBean> getMediaGatewayPriceDetails() {
		return mediaGatewayPriceDetails;
	}

	public void setMediaGatewayPriceDetails(List<MediaGatewayPriceDetailsBean> mediaGatewayPriceDetails) {
		this.mediaGatewayPriceDetails = mediaGatewayPriceDetails;
	}

	public Double getTotalArc() {
		return totalArc;
	}

	public void setTotalArc(Double totalArc) {
		this.totalArc = totalArc;
	}

	public Double getTotalNrc() {
		return totalNrc;
	}

	public void setTotalNrc(Double totalNrc) {
		this.totalNrc = totalNrc;
	}

	public Double getTotalMrc() {
		return totalMrc;
	}

	public void setTotalMrc(Double totalMrc) {
		this.totalMrc = totalMrc;
	}

	public Double getTotalTcv() {
		return totalTcv;
	}

	public void setTotalTcv(Double totalTcv) {
		this.totalTcv = totalTcv;
	}
}
