package com.tcl.dias.oms.teamsdr.beans;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.entity.entities.OrderDirectRoutingCity;
import com.tcl.dias.oms.entity.entities.OrderDirectRoutingMediaGateways;

/**
 * Teams DR Order Media gateway bean
 * 
 * @author Srinivasa Raghavan
 */
public class TeamsDROrderMediaGatewayBean {
	private Integer id;
	private String name;
	private Integer quantity;
	private List<OrderProductComponentBean> mediaGatewayComponents;
	private Double mrc;
	private Double nrc;
	private Double arc;
	private Double tcv;

	public TeamsDROrderMediaGatewayBean() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getMrc() {
		return mrc;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public List<OrderProductComponentBean> getMediaGatewayComponents() {
		return mediaGatewayComponents;
	}

	public void setMediaGatewayComponents(List<OrderProductComponentBean> mediaGatewayComponents) {
		this.mediaGatewayComponents = mediaGatewayComponents;
	}

	/**
	 *
	 * @param mediaGateWay
	 * @param orderDirectRoutingCity
	 * @return
	 */
	public static OrderDirectRoutingMediaGateways toTeamsDrMediaGatewayBean(TeamsDROrderMediaGatewayBean mediaGateWay,
			OrderDirectRoutingCity orderDirectRoutingCity) {
		OrderDirectRoutingMediaGateways teamsDrMediagateway = new OrderDirectRoutingMediaGateways();
		teamsDrMediagateway.setOrderDirectRoutingCity(orderDirectRoutingCity);
		teamsDrMediagateway.setName(mediaGateWay.getName());
		if (Objects.nonNull(mediaGateWay.getMrc())) {
			teamsDrMediagateway.setMrc(mediaGateWay.getMrc());
		}
		if (Objects.nonNull(mediaGateWay.getNrc())) {
			teamsDrMediagateway.setNrc(mediaGateWay.getNrc());
		}
		if (Objects.nonNull(mediaGateWay.getArc())) {
			teamsDrMediagateway.setArc(mediaGateWay.getArc());
		}
		if (Objects.nonNull(mediaGateWay.getTcv())) {
			teamsDrMediagateway.setTcv(mediaGateWay.getTcv());
		}
		teamsDrMediagateway.setQuantity(mediaGateWay.getQuantity());
		teamsDrMediagateway.setCreatedTime(new Date());
		teamsDrMediagateway.setOrderVersion(orderDirectRoutingCity.getOrderVersion());
		return teamsDrMediagateway;
	}

	@Override
	public String toString() {
		return "TeamsDRMediaGatewayBean{" + "id=" + id + ", name='" + name + '\'' + ", quantity=" + quantity
				+ ", mediaGatewayComponents=" + mediaGatewayComponents + ", mrc=" + mrc + ", nrc=" + nrc + ", arc="
				+ arc + ", tcv=" + tcv + '}';
	}
}
