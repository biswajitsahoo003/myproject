package com.tcl.dias.oms.teamsdr.beans;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the TeamsDRCityPricingBean.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "id", "no_of_named_users", "no_of_common_area_devices", "mediagateway", "mediagateway_type",
		"city", "site", "mrc", "nrc", "arc", "tcv" })
public class TeamsDRCityPricingBean {
	@JsonProperty("id")
	private Integer id;
	@JsonProperty("no_of_named_users")
	private Integer noOfNamedUsers;
	@JsonProperty("no_of_common_area_devices")
	private Integer noOfCommonAreaDevices;
	@JsonProperty("total_users")
	private Integer totalUsers;
	@JsonProperty("mediagateway")
	private List<TeamDRMediaGatewayPricingBean> mediaGateway;
	@JsonProperty("mediagateway_type")
	private String mediaGatewayType;
	@JsonProperty("city")
	private String city;
	@JsonProperty("site")
	private String site;
	@JsonProperty("mrc")
	private BigDecimal mrc;
	@JsonProperty("nrc")
	private BigDecimal nrc;
	@JsonProperty("arc")
	private BigDecimal arc;
	@JsonProperty("tcv")
	private BigDecimal tcv;

	public TeamsDRCityPricingBean() {
	}

	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	@JsonProperty("no_of_named_users")
	public Integer getNoOfNamedUsers() {
		return noOfNamedUsers;
	}

	@JsonProperty("no_of_named_users")
	public void setNoOfNamedUsers(Integer noOfNamedUsers) {
		this.noOfNamedUsers = noOfNamedUsers;
	}

	@JsonProperty("no_of_common_area_devices")
	public Integer getNoOfCommonAreaDevices() {
		return noOfCommonAreaDevices;
	}

	@JsonProperty("no_of_common_area_devices")
	public void setNoOfCommonAreaDevices(Integer noOfCommonAreaDevices) {
		this.noOfCommonAreaDevices = noOfCommonAreaDevices;
	}

	@JsonProperty("total_users")
	public Integer getTotalUsers() {
		return totalUsers;
	}

	@JsonProperty("total_users")
	public void setTotalUsers(Integer totalUsers) {
		this.totalUsers = totalUsers;
	}

	@JsonProperty("mediagateway")
	public List<TeamDRMediaGatewayPricingBean> getMediaGateway() {
		return mediaGateway;
	}

	@JsonProperty("mediagateway")
	public void setMediaGateway(List<TeamDRMediaGatewayPricingBean> mediaGateway) {
		this.mediaGateway = mediaGateway;
	}

	@JsonProperty("mediagateway_type")
	public String getMediaGatewayType() {
		return mediaGatewayType;
	}

	@JsonProperty("mediagateway_type")
	public void setMediaGatewayType(String mediaGatewayType) {
		this.mediaGatewayType = mediaGatewayType;
	}

	@JsonProperty("city")
	public String getCity() {
		return city;
	}

	@JsonProperty("city")
	public void setCity(String city) {
		this.city = city;
	}

	@JsonProperty("site")
	public String getSite() {
		return site;
	}

	@JsonProperty("site")
	public void setSite(String site) {
		this.site = site;
	}

	@JsonProperty("mrc")
	public BigDecimal getMrc() {
		return mrc;
	}

	@JsonProperty("mrc")
	public void setMrc(BigDecimal mrc) {
		this.mrc = mrc;
	}

	@JsonProperty("nrc")
	public BigDecimal getNrc() {
		return nrc;
	}

	@JsonProperty("nrc")
	public void setNrc(BigDecimal nrc) {
		this.nrc = nrc;
	}

	@JsonProperty("arc")
	public BigDecimal getArc() {
		return arc;
	}

	@JsonProperty("arc")
	public void setArc(BigDecimal arc) {
		this.arc = arc;
	}

	@JsonProperty("tcv")
	public BigDecimal getTcv() {
		return tcv;
	}

	@JsonProperty("tcv")
	public void setTcv(BigDecimal tcv) {
		this.tcv = tcv;
	}

	/**
	 *
	 * @param teamsDRCityBean
	 * @return
	 */
	public static TeamsDRCityPricingBean toTeamsdrCityDetailsPricingBean(TeamsDRCityBean teamsDRCityBean) {
		TeamsDRCityPricingBean teamsDRCityPricingBean = new TeamsDRCityPricingBean();
		teamsDRCityPricingBean.setId(teamsDRCityBean.getId());
		teamsDRCityPricingBean.setCity(teamsDRCityBean.getCity());
		if (Objects.nonNull(teamsDRCityBean.getMediaGateway())) {
			if (Objects.nonNull(teamsDRCityBean.getMediaGateway()) && !teamsDRCityBean.getMediaGateway().isEmpty()) {
				teamsDRCityPricingBean.setMediaGateway(teamsDRCityBean.getMediaGateway().stream()
						.map(TeamDRMediaGatewayPricingBean::toTeamsDrMediaGatewayPricingBean)
						.collect(Collectors.toList()));
			}
		} else {
			teamsDRCityPricingBean.setMediaGateway(null);
		}
		teamsDRCityPricingBean.setMediaGatewayType(teamsDRCityBean.getMediaGatewayType());
		teamsDRCityPricingBean.setArc(teamsDRCityBean.getArc());
		teamsDRCityPricingBean.setMrc(teamsDRCityBean.getArc());
		teamsDRCityPricingBean.setNrc(teamsDRCityBean.getNrc());
		return teamsDRCityPricingBean;
	}

	@Override
	public String toString() {
		return "TeamsDRCityPricingBean{" + "id=" + id + ", noOfNamedUsers=" + noOfNamedUsers
				+ ", noOfCommonAreaDevices=" + noOfCommonAreaDevices + ", totalUsers=" + totalUsers + ", mediaGateway="
				+ mediaGateway + ", mediaGatewayType='" + mediaGatewayType + '\'' + ", city='" + city + '\''
				+ ", site='" + site + '\'' + ", mrc=" + mrc + ", nrc=" + nrc + ", arc=" + arc + ", tcv=" + tcv + '}';
	}
}
