package com.tcl.dias.oms.webex.beans;

import com.tcl.dias.oms.entity.entities.OrderUcaas;
import com.tcl.dias.oms.webex.util.WebexConstants;

import java.util.Objects;

/**
 * OrderUcaasBean holds the license line items details of webex product.
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class OrderUcaasBean {

	private Integer id;
	private String skuId;
	private String skuName;
	private Integer quantity;
	private Double arc;
	private Double unitMrc;
	private Double mrc;
	private Double unitNrc;
	private Double nrc;
	private Double tcv;
	private Double ciscoUnitListPrice;
	private Double ciscoDiscountPercent;
	private Double ciscoUnitNetPrice;
	private String hsnCode;
	private String uom;
	private Boolean isEndpoint;
	private Boolean isUpdated;
	private EndpointDetails endpointDetails;

	public OrderUcaasBean() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
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

	public Double getTcv() {
		return tcv;
	}

	public void setTcv(Double tcv) {
		this.tcv = tcv;
	}

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	public String getUom() {
		return uom;
	}

	public void setUom(String uom) {
		this.uom = uom;
	}

	public Double getUnitMrc() {
		return unitMrc;
	}

	public void setUnitMrc(Double unitMrc) {
		this.unitMrc = unitMrc;
	}

	public Double getUnitNrc() {
		return unitNrc;
	}

	public void setUnitNrc(Double unitNrc) {
		this.unitNrc = unitNrc;
	}

	public Double getCiscoUnitListPrice() {
		return ciscoUnitListPrice;
	}

	public void setCiscoUnitListPrice(Double ciscoUnitListPrice) {
		this.ciscoUnitListPrice = ciscoUnitListPrice;
	}

	public Double getCiscoDiscountPercent() {
		return ciscoDiscountPercent;
	}

	public void setCiscoDiscountPercent(Double ciscoDiscountPercent) {
		this.ciscoDiscountPercent = ciscoDiscountPercent;
	}

	public Double getCiscoUnitNetPrice() {
		return ciscoUnitNetPrice;
	}

	public void setCiscoUnitNetPrice(Double ciscoUnitNetPrice) {
		this.ciscoUnitNetPrice = ciscoUnitNetPrice;
	}

	public Boolean getIsEndpoint() {
		return isEndpoint;
	}

	public void setIsEndpoint(Boolean endpoint) {
		isEndpoint = endpoint;
	}

	public EndpointDetails getEndpointDetails() {
		return endpointDetails;
	}

	public void setEndpointDetails(EndpointDetails endpointDetails) {
		this.endpointDetails = endpointDetails;
	}

	public Boolean getIsUpdated() {
		return isUpdated;
	}

	public void setIsUpdated(Boolean isUpdated) {
		this.isUpdated = isUpdated;
	}

	public static OrderUcaasBean toOrderUcaasBean(OrderUcaas orderUcaas) {
		OrderUcaasBean orderUcaasBean = new OrderUcaasBean();
		orderUcaasBean.setId(orderUcaas.getId());
		orderUcaasBean.setSkuId(orderUcaas.getName());
		orderUcaasBean.setSkuName(orderUcaas.getDescription());
		orderUcaasBean.setQuantity(orderUcaas.getQuantity());
		orderUcaasBean.setUnitMrc(orderUcaas.getUnitMrc());
		orderUcaasBean.setArc(orderUcaas.getArc());
		orderUcaasBean.setMrc(orderUcaas.getMrc());
		orderUcaasBean.setUnitNrc(orderUcaas.getUnitNrc());
		orderUcaasBean.setNrc(orderUcaas.getNrc());
		orderUcaasBean.setTcv(orderUcaas.getTcv());
		orderUcaasBean.setCiscoUnitListPrice(orderUcaas.getCiscoUnitListPrice());
		orderUcaasBean.setCiscoDiscountPercent(orderUcaas.getCiscoDiscountPercent());
		orderUcaasBean.setCiscoUnitNetPrice(orderUcaas.getCiscoUnitNetPrice());
		orderUcaasBean.setUom(orderUcaas.getUom());
		orderUcaasBean.setHsnCode(Objects.nonNull(orderUcaas.getHsnCode()) ? orderUcaas.getHsnCode() : null);
		orderUcaasBean.setIsEndpoint(orderUcaas.getUom().equals(WebexConstants.ENDPOINT));
		if (orderUcaasBean.getIsEndpoint()) {
			EndpointDetails endpointDetails = new EndpointDetails();
			endpointDetails.setLocationId(orderUcaas.getEndpointLocationId());
			endpointDetails.setContractType(orderUcaas.getContractType());
			endpointDetails.setEndpointManagementType(orderUcaas.getEndpointManagementType());
			orderUcaasBean.setIsUpdated(orderUcaas.getIsUpdated() == 1);
			orderUcaasBean.setEndpointDetails(endpointDetails);
		}
		return orderUcaasBean;
	}

	@Override
	public String toString() {
		return "OrderUcaasBean{" + "id=" + id + ", skuId='" + skuId + '\'' + ", skuName='" + skuName + '\''
				+ ", quantity=" + quantity + ", arc=" + arc + ", unitMrc=" + unitMrc + ", mrc=" + mrc + ", unitNrc="
				+ unitNrc + ", nrc=" + nrc + ", tcv=" + tcv + ", ciscoUnitListPrice=" + ciscoUnitListPrice
				+ ", ciscoDiscountPercent=" + ciscoDiscountPercent + ", ciscoUnitNetPrice=" + ciscoUnitNetPrice
				+ ", hsnCode='" + hsnCode + '\'' + ", uom='" + uom + '\'' + ", isEndpoint=" + isEndpoint
				+ ", endpointDetails=" + endpointDetails + '}';
	}
}
