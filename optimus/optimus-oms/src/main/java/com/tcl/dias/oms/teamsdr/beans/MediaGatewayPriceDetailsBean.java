package com.tcl.dias.oms.teamsdr.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Media gateway price details
 * 
 * @author srraghav
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MediaGatewayPriceDetailsBean {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("mediagateway_type")
	private String mediaGatewayType;

	@JsonProperty("mediagateway_provider")
	private String mediaGatewayProvider;

	@JsonProperty("no_of_pri")
	private String noOfPri;

	@JsonProperty("no_of_session")
	private String noOfSession;

	@JsonProperty("redundancy")
	private String redundancy;

	@JsonProperty("purchase_type")
	private String purchaseType;

	@JsonProperty("amc")
	private String amc;

	@JsonProperty("ahr")
	private String ahr;

	@JsonProperty("country")
	private String country;

	@JsonProperty("uom")
	private String uom;

	@JsonProperty("country_wise_total_mrc")
	private Double countryWiseMrc;

	@JsonProperty("country_wise_total_nrc")
	private Double countryWiseNrc;

	@JsonProperty("country_wise_total_arc")
	private Double countryWiseArc;

	@JsonProperty("country_wise_total_tcv")
	private Double countryWiseTcv;

	@JsonProperty("location_wise_detail")
	private List<MgLocationWiseDetailBean> locationWiseDetails;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMediaGatewayType() {
		return mediaGatewayType;
	}

	public void setMediaGatewayType(String mediaGatewayType) {
		this.mediaGatewayType = mediaGatewayType;
	}

	public String getMediaGatewayProvider() {
		return mediaGatewayProvider;
	}

	public void setMediaGatewayProvider(String mediaGatewayProvider) {
		this.mediaGatewayProvider = mediaGatewayProvider;
	}

	public String getNoOfPri() {
		return noOfPri;
	}

	public void setNoOfPri(String noOfPri) {
		this.noOfPri = noOfPri;
	}

	public String getNoOfSession() {
		return noOfSession;
	}

	public void setNoOfSession(String noOfSession) {
		this.noOfSession = noOfSession;
	}

	public String getRedundancy() {
		return redundancy;
	}

	public void setRedundancy(String redundancy) {
		this.redundancy = redundancy;
	}

	public String getPurchaseType() {
		return purchaseType;
	}

	public void setPurchaseType(String purchaseType) {
		this.purchaseType = purchaseType;
	}

	public String getAmc() {
		return amc;
	}

	public void setAmc(String amc) {
		this.amc = amc;
	}

	public String getAhr() {
		return ahr;
	}

	public void setAhr(String ahr) {
		this.ahr = ahr;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public Double getCountryWiseMrc() {
		return countryWiseMrc;
	}

	public void setCountryWiseMrc(Double countryWiseMrc) {
		this.countryWiseMrc = countryWiseMrc;
	}

	public Double getCountryWiseNrc() {
		return countryWiseNrc;
	}

	public void setCountryWiseNrc(Double countryWiseNrc) {
		this.countryWiseNrc = countryWiseNrc;
	}

	public Double getCountryWiseArc() {
		return countryWiseArc;
	}

	public void setCountryWiseArc(Double countryWiseArc) {
		this.countryWiseArc = countryWiseArc;
	}

	public Double getCountryWiseTcv() {
		return countryWiseTcv;
	}

	public void setCountryWiseTcv(Double countryWiseTcv) {
		this.countryWiseTcv = countryWiseTcv;
	}

	public List<MgLocationWiseDetailBean> getLocationWiseDetails() {
		return locationWiseDetails;
	}

	public void setLocationWiseDetails(List<MgLocationWiseDetailBean> locationWiseDetails) {
		this.locationWiseDetails = locationWiseDetails;
	}
}
