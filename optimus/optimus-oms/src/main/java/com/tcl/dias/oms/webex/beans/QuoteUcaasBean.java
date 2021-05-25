package com.tcl.dias.oms.webex.beans;

import com.tcl.dias.oms.entity.entities.QuoteUcaas;
import com.tcl.dias.oms.webex.util.WebexConstants;

import java.util.Objects;

/**
 * QuoteUcaasBean holds the license line items details of webex product.
 *
 * @author arjayapa
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class QuoteUcaasBean {

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

	public static QuoteUcaas toQuoteUcaas(QuoteUcaasBean ucaasQuoteBean) {
		QuoteUcaas quoteUcaas = new QuoteUcaas();
		quoteUcaas.setName(ucaasQuoteBean.getSkuId());
		quoteUcaas.setDescription(ucaasQuoteBean.getSkuName());
		quoteUcaas.setQuantity(ucaasQuoteBean.getQuantity());
		quoteUcaas.setArc(ucaasQuoteBean.getArc());
		quoteUcaas.setUnitMrc(ucaasQuoteBean.getUnitMrc());
		quoteUcaas.setMrc(ucaasQuoteBean.getMrc());
		quoteUcaas.setUnitNrc(ucaasQuoteBean.getUnitNrc());
		quoteUcaas.setNrc(ucaasQuoteBean.getNrc());
		quoteUcaas.setTcv(ucaasQuoteBean.getTcv());
		quoteUcaas.setCiscoUnitListPrice(ucaasQuoteBean.getCiscoUnitListPrice());
		quoteUcaas.setCiscoDiscountPercent(ucaasQuoteBean.getCiscoDiscountPercent());
		quoteUcaas.setCiscoUnitNetPrice(ucaasQuoteBean.getCiscoUnitNetPrice());
		quoteUcaas.setUom(ucaasQuoteBean.getUom());
		return quoteUcaas;
	}

	public static QuoteUcaasBean toQuoteUcaasBean(QuoteUcaas quoteUcaas) {
		QuoteUcaasBean ucaasQuoteBean = new QuoteUcaasBean();
		ucaasQuoteBean.setId(quoteUcaas.getId());
		ucaasQuoteBean.setSkuId(quoteUcaas.getName());
		ucaasQuoteBean.setSkuName(quoteUcaas.getDescription());
		ucaasQuoteBean.setQuantity(quoteUcaas.getQuantity());
		ucaasQuoteBean.setArc(quoteUcaas.getArc());
		ucaasQuoteBean.setUnitMrc(quoteUcaas.getUnitMrc());
		ucaasQuoteBean.setMrc(quoteUcaas.getMrc());
		ucaasQuoteBean.setUnitNrc(quoteUcaas.getUnitNrc());
		ucaasQuoteBean.setNrc(quoteUcaas.getNrc());
		ucaasQuoteBean.setTcv(quoteUcaas.getTcv());
		ucaasQuoteBean.setCiscoUnitListPrice(quoteUcaas.getCiscoUnitListPrice());
		ucaasQuoteBean.setCiscoDiscountPercent(quoteUcaas.getCiscoDiscountPercent());
		ucaasQuoteBean.setCiscoUnitNetPrice(quoteUcaas.getCiscoUnitNetPrice());
		ucaasQuoteBean.setUom(quoteUcaas.getUom());
		ucaasQuoteBean.setHsnCode(Objects.nonNull(quoteUcaas.getHsnCode()) ? quoteUcaas.getHsnCode() : null);
		ucaasQuoteBean.setIsEndpoint(quoteUcaas.getUom().equals(WebexConstants.ENDPOINT));
		if (ucaasQuoteBean.getIsEndpoint()) {
			EndpointDetails endpointDetails = new EndpointDetails();
			endpointDetails.setLocationId(quoteUcaas.getEndpointLocationId());
			endpointDetails.setEndpointManagementType(quoteUcaas.getEndpointManagementType());
			endpointDetails.setContractType(quoteUcaas.getContractType());
			ucaasQuoteBean.setIsUpdated(quoteUcaas.getIsUpdated() == 1);
			ucaasQuoteBean.setEndpointDetails(endpointDetails);
		}
		return ucaasQuoteBean;
	}

	@Override
	public String toString() {
		return "QuoteUcaasBean{" + "id=" + id + ", skuId='" + skuId + '\'' + ", skuName='" + skuName + '\''
				+ ", quantity=" + quantity + ", arc=" + arc + ", unitMrc=" + unitMrc + ", mrc=" + mrc + ", unitNrc="
				+ unitNrc + ", nrc=" + nrc + ", tcv=" + tcv + ", ciscoUnitListPrice=" + ciscoUnitListPrice
				+ ", ciscoDiscountPercent=" + ciscoDiscountPercent + ", ciscoUnitNetPrice=" + ciscoUnitNetPrice
				+ ", hsnCode='" + hsnCode + '\'' + ", uom='" + uom + '\'' + ", isEndpoint=" + isEndpoint
				+ ", isUpdated=" + isUpdated + ", endpointDetails=" + endpointDetails + '}';
	}
}
