package com.tcl.dias.oms.beans;

public class OrderIllSitetoServiceBean
{
	private Integer id;
	private Integer orderToLeId;
	private String erfServiceInventoryTpsServiceId;
	private Integer orderIllSiteId;
	private String type;
	private Integer orderNplLinkId;
	private String erfSfdcOrderType;
	private String erfSfdcSubType;



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderToLeId() {
		return orderToLeId;
	}

	public void setOrderToLeId(Integer orderToLeId) {
		this.orderToLeId = orderToLeId;
	}

	public String getErfServiceInventoryTpsServiceId() {
		return erfServiceInventoryTpsServiceId;
	}

	public void setErfServiceInventoryTpsServiceId(String erfServiceInventoryTpsServiceId) {
		this.erfServiceInventoryTpsServiceId = erfServiceInventoryTpsServiceId;
	}

	public Integer getOrderIllSiteId() {
		return orderIllSiteId;
	}

	public void setOrderIllSiteId(Integer orderIllSiteId) {
		this.orderIllSiteId = orderIllSiteId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getOrderNplLinkId() {
		return orderNplLinkId;
	}

	public void setOrderNplLinkId(Integer orderNplLinkId) {
		this.orderNplLinkId = orderNplLinkId;
	}

	public String getErfSfdcOrderType() {
		return erfSfdcOrderType;
	}

	public void setErfSfdcOrderType(String erfSfdcOrderType) {
		this.erfSfdcOrderType = erfSfdcOrderType;
	}

	public String getErfSfdcSubType() {
		return erfSfdcSubType;
	}

	public void setErfSfdcSubType(String erfSfdcSubType) {
		this.erfSfdcSubType = erfSfdcSubType;
	}

	@Override
	public String toString() {
		return "OrderIllSitetoServiceBean [id=" + id + ", orderToLeId=" + orderToLeId
				+ ", erfServiceInventoryTpsServiceId=" + erfServiceInventoryTpsServiceId + ", orderIllSiteId="
				+ orderIllSiteId + ", type=" + type + ", orderNplLinkId=" + orderNplLinkId + ", erfSfdcOrderType="
				+ erfSfdcOrderType + ", erfSfdcSubType=" + erfSfdcSubType + "]";
	}
	
}
