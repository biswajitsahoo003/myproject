package com.tcl.dias.oms.webex.beans;

import java.util.Objects;

/**
 * This class contains UCAAS pricing related field.
 *
 * @author ssyed ali.
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class WebexUcaasQuotePriceBean {
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
	private String hsnCode;
	private String uom;
	private Boolean isLicenseComponent;
	private Boolean isEndpoint;
	private Boolean isEndpointIntl;
	private Boolean isSkuID;

	public WebexUcaasQuotePriceBean() {
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

	public Boolean getIsLicenseComponent() {
		return isLicenseComponent;
	}

	public void setIsLicenseComponent(Boolean isLicenseComponent) {
		this.isLicenseComponent = isLicenseComponent;
	}

	public Boolean getIsEndpoint() {
		return isEndpoint;
	}

	public void setIsEndpoint(Boolean isEndpoint) {
		this.isEndpoint = isEndpoint;
	}

	public Boolean getIsEndpointIntl() {
		return isEndpointIntl;
	}

	public void setIsEndpointIntl(Boolean isEndpointIntl) {
		this.isEndpointIntl = isEndpointIntl;
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

	public Boolean getIsSkuID() {
		return isSkuID;
	}

	public void setIsSkuID(Boolean isSkuID) {
		this.isSkuID = isSkuID;
	}

	public static WebexUcaasQuotePriceBean toWebexUcaasQuotePriceBean(QuoteUcaasBean quoteUcaas) {
		WebexUcaasQuotePriceBean webexUcaasQuotePriceBean = new WebexUcaasQuotePriceBean();
		webexUcaasQuotePriceBean.setId(quoteUcaas.getId());
		webexUcaasQuotePriceBean.setSkuId(quoteUcaas.getSkuId());
		webexUcaasQuotePriceBean.setSkuName(quoteUcaas.getSkuName());
		webexUcaasQuotePriceBean.setQuantity(quoteUcaas.getQuantity());
		webexUcaasQuotePriceBean.setArc(Objects.nonNull(quoteUcaas.getArc()) ? quoteUcaas.getArc() : 0.0);
		webexUcaasQuotePriceBean.setUnitMrc(Objects.nonNull(quoteUcaas.getUnitMrc()) ? quoteUcaas.getUnitMrc() : 0.0);
		webexUcaasQuotePriceBean.setMrc(Objects.nonNull(quoteUcaas.getMrc()) ? quoteUcaas.getMrc() : 0.0);
		webexUcaasQuotePriceBean.setUnitNrc(Objects.nonNull(quoteUcaas.getUnitNrc()) ? quoteUcaas.getUnitNrc() : 0.0);
		webexUcaasQuotePriceBean.setNrc(Objects.nonNull(quoteUcaas.getNrc()) ? quoteUcaas.getNrc() : 0.0);
		webexUcaasQuotePriceBean.setTcv(Objects.nonNull(quoteUcaas.getTcv()) ? quoteUcaas.getTcv() : 0.0);
		webexUcaasQuotePriceBean.setUom(quoteUcaas.getUom());
		return webexUcaasQuotePriceBean;
	}

	@Override
	public String toString() {
		return "WebexUcaasQuotePriceBean [id=" + id + ", skuId=" + skuId + ", skuName=" + skuName + ", quantity="
				+ quantity + ", arc=" + arc + ", unitMrc=" + unitMrc + ", mrc=" + mrc + ", unitNrc=" + unitNrc
				+ ", nrc=" + nrc + ", tcv=" + tcv + ", hsnCode=" + hsnCode + ", uom=" + uom + ", isLicenseComponent="
				+ isLicenseComponent + ", isEndpoint=" + isEndpoint + ", isEndpointIntl=" + isEndpointIntl
				+ ", isSkuID=" + isSkuID + "]";
	}

}
