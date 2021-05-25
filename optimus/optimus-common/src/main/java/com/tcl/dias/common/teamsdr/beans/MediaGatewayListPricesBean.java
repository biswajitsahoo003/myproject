package com.tcl.dias.common.teamsdr.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Media gateway list prices
 * 
 * @author srraghav
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MediaGatewayListPricesBean {

	@JsonProperty("mg_id")
	private Integer mgId;

	@JsonProperty("media_gateway_nm")
	private String mediaGatewayName;

	@JsonProperty("qty")
	private Integer qty;

	@JsonProperty("mrc")
	Double mrc;

	@JsonProperty("nrc")
	Double nrc;

	@JsonProperty("arc")
	Double arc;

	@JsonProperty("tcv")
	Double tcv;

	@JsonProperty("unit_nrc_with_delivery")
	private Double unitNrcWithDelivery;

	@JsonProperty("nrc_with_delivery")
	private Double nrcWithDelivery;

	@JsonProperty("cpe_outright_rental")
	private CpeChargesBean cpeOutrightRentalCharges;

	@JsonProperty("cpe_amc")
	private CpeChargesBean cpeAmcCharges;

	@JsonProperty("cpe_management")
	private CpeChargesBean cpeManagement;

	public Integer getMgId() {
		return mgId;
	}

	public void setMgId(Integer mgId) {
		this.mgId = mgId;
	}

	public String getMediaGatewayName() {
		return mediaGatewayName;
	}

	public void setMediaGatewayName(String mediaGatewayName) {
		this.mediaGatewayName = mediaGatewayName;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public Double getUnitNrcWithDelivery() {
		return unitNrcWithDelivery;
	}

	public void setUnitNrcWithDelivery(Double unitNrcWithDelivery) {
		this.unitNrcWithDelivery = unitNrcWithDelivery;
	}

	public Double getNrcWithDelivery() {
		return nrcWithDelivery;
	}

	public void setNrcWithDelivery(Double nrcWithDelivery) {
		this.nrcWithDelivery = nrcWithDelivery;
	}

	public CpeChargesBean getCpeOutrightRentalCharges() {
		return cpeOutrightRentalCharges;
	}

	public void setCpeOutrightRentalCharges(CpeChargesBean cpeOutrightRentalCharges) {
		this.cpeOutrightRentalCharges = cpeOutrightRentalCharges;
	}

	public CpeChargesBean getCpeAmcCharges() {
		return cpeAmcCharges;
	}

	public void setCpeAmcCharges(CpeChargesBean cpeAmcCharges) {
		this.cpeAmcCharges = cpeAmcCharges;
	}

	public CpeChargesBean getCpeManagement() {
		return cpeManagement;
	}

	public void setCpeManagement(CpeChargesBean cpeManagement) {
		this.cpeManagement = cpeManagement;
	}
}
