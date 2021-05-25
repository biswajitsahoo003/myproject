package com.tcl.dias.oms.teamsdr.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcl.dias.common.teamsdr.beans.MediaGatewayListPricesBean;

/**
 * Mg location wise detail
 * 
 * @author srraghav
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MgLocationWiseDetailBean {

	@JsonProperty("mediagateway_type")
	private String mediaGatewayType;

	@JsonProperty("address")
	private MgSiteAddressBean mgSiteAddress;

	@JsonProperty("mediagateway_list")
	private List<MediaGatewayListPricesBean> mediaGatewayListPrices;

	@JsonProperty("city_wise_mrc")
	private Double cityWiseMrc;

	@JsonProperty("city_wise_nrc")
	private Double cityWiseNrc;

	@JsonProperty("city_wise_arc")
	private Double cityWiseArc;

	@JsonProperty("city_wise_tcv")
	private Double cityWiseTcv;

	public String getMediaGatewayType() {
		return mediaGatewayType;
	}

	public void setMediaGatewayType(String mediaGatewayType) {
		this.mediaGatewayType = mediaGatewayType;
	}

	public MgSiteAddressBean getMgSiteAddress() {
		return mgSiteAddress;
	}

	public void setMgSiteAddress(MgSiteAddressBean mgSiteAddress) {
		this.mgSiteAddress = mgSiteAddress;
	}

	public List<MediaGatewayListPricesBean> getMediaGatewayListPrices() {
		return mediaGatewayListPrices;
	}

	public void setMediaGatewayListPrices(List<MediaGatewayListPricesBean> mediaGatewayListPrices) {
		this.mediaGatewayListPrices = mediaGatewayListPrices;
	}

	public Double getCityWiseMrc() {
		return cityWiseMrc;
	}

	public void setCityWiseMrc(Double cityWiseMrc) {
		this.cityWiseMrc = cityWiseMrc;
	}

	public Double getCityWiseNrc() {
		return cityWiseNrc;
	}

	public void setCityWiseNrc(Double cityWiseNrc) {
		this.cityWiseNrc = cityWiseNrc;
	}

	public Double getCityWiseArc() {
		return cityWiseArc;
	}

	public void setCityWiseArc(Double cityWiseArc) {
		this.cityWiseArc = cityWiseArc;
	}

	public Double getCityWiseTcv() {
		return cityWiseTcv;
	}

	public void setCityWiseTcv(Double cityWiseTcv) {
		this.cityWiseTcv = cityWiseTcv;
	}
}
