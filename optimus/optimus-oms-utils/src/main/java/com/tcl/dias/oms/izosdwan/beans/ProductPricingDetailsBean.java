package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.format.annotation.NumberFormat;

/**
 * 
 * This bean binds all the prive values product wise
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ProductPricingDetailsBean implements Serializable{
	private ArcDetailsBean arcDetailsBean;
	private NrcDetailsBean nrcDetailsBean;
	private MrcDetailsBean mrcDetailsBean;
	@NumberFormat(pattern = "#############.##")
	private BigDecimal tcv;
	private String productName;
	private Integer siteId;
	private Integer locationId;
	private String serviceId;
	private String pri_sec;
	private String bandwidth;
	private BigDecimal tcvMrc;
	private String orderType;
	private BigDecimal previousMrcTotal;
	private BigDecimal previousArcTotal;
	private BigDecimal previousNrcTotal;
	
	
	public BigDecimal getPreviousNrcTotal() {
		return previousNrcTotal;
	}
	public void setPreviousNrcTotal(BigDecimal previousNrcTotal) {
		this.previousNrcTotal = previousNrcTotal;
	}
	public BigDecimal getPreviousArcTotal() {
		return previousArcTotal;
	}
	public void setPreviousArcTotal(BigDecimal previousArcTotal) {
		this.previousArcTotal = previousArcTotal;
	}
	public BigDecimal getPreviousMrcTotal() {
		return previousMrcTotal;
	}
	public void setPreviousMrcTotal(BigDecimal previousMrcTotal) {
		this.previousMrcTotal = previousMrcTotal;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getPri_sec() {
		return pri_sec;
	}
	public void setPri_sec(String pri_sec) {
		this.pri_sec = pri_sec;
	}
	public ArcDetailsBean getArcDetailsBean() {
		return arcDetailsBean;
	}
	public void setArcDetailsBean(ArcDetailsBean arcDetailsBean) {
		this.arcDetailsBean = arcDetailsBean;
	}
	public NrcDetailsBean getNrcDetailsBean() {
		return nrcDetailsBean;
	}
	public void setNrcDetailsBean(NrcDetailsBean nrcDetailsBean) {
		this.nrcDetailsBean = nrcDetailsBean;
	}
	public MrcDetailsBean getMrcDetailsBean() {
		return mrcDetailsBean;
	}
	public void setMrcDetailsBean(MrcDetailsBean mrcDetailsBean) {
		this.mrcDetailsBean = mrcDetailsBean;
	}
	public BigDecimal getTcv() {
		return tcv;
	}
	public void setTcv(BigDecimal tcv) {
		this.tcv = tcv;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public Integer getLocationId() {
		return locationId;
	}
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public BigDecimal getTcvMrc() {
		return tcvMrc;
	}

	public void setTcvMrc(BigDecimal tcvMrc) {
		this.tcvMrc = tcvMrc;
	}
}
