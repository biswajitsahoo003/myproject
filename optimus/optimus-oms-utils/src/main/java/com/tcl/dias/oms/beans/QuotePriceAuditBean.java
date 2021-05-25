package com.tcl.dias.oms.beans;

import java.io.Serializable;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.dto.MstProductComponentDto;
import com.tcl.dias.oms.entity.entities.QuotePriceAudit;

/**
 * 
 * This is the bean class for the quote price audit entity
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuotePriceAuditBean implements Serializable {

	private static final long serialVersionUID = 1248430969347064870L;
	
	private Integer id;

	private String createdBy;

	private Timestamp createdTime;

	private Double fromArcPrice;

	private Double fromMrcPrice;

	private Double fromNrcPrice;

	private Byte idDeletedOrRefreshed;

	private String quoteRefId;

	private Double toMrcPrice;

	private Double toNrcPrice;

	private Double toArcPrice;
	
	private Double fromEffectiveUsagePrice;
	
	private Double toEffectiveUsagePrice;

	private QuotePriceBean quotePrice;
	
	private MstProductComponentDto  mstProductComponent;
	
	private ProductAttributeMasterBean productAttributeMaster;
	
	private Integer locationId;
	

	public QuotePriceAuditBean() {
		super();
	}
	

	/**
	 * @param id
	 * @param createdBy
	 * @param createdTime
	 * @param fromArcPrice
	 * @param fromMrcPrice
	 * @param fromNrcPrice
	 * @param idDeletedOrRefreshed
	 * @param quoteRefId
	 * @param toMrcPrice
	 * @param toNrcPrice
	 * @param toArcPrice
	 * @param fromEffectiveUsagePrice
	 * @param toEffectiveUsagePrice
	 * @param quotePrice
	 */
	public QuotePriceAuditBean(QuotePriceAudit quotePriceAudit) {
		super();
		this.id = quotePriceAudit.getId();
		this.createdBy = quotePriceAudit.getCreatedBy();
		this.createdTime = quotePriceAudit.getCreatedTime();
		this.fromArcPrice = quotePriceAudit.getFromArcPrice();
		this.fromMrcPrice = quotePriceAudit.getFromMrcPrice();
		this.fromNrcPrice = quotePriceAudit.getFromNrcPrice();
		this.idDeletedOrRefreshed = quotePriceAudit.getIdDeletedOrRefreshed();
		this.quoteRefId = quotePriceAudit.getQuoteRefId();
		this.toMrcPrice = quotePriceAudit.getToMrcPrice();
		this.toNrcPrice = quotePriceAudit.getToNrcPrice();
		this.toArcPrice = quotePriceAudit.getToArcPrice();
		this.fromEffectiveUsagePrice = quotePriceAudit.getFromEffectiveUsagePrice();
		this.toEffectiveUsagePrice = quotePriceAudit.getToEffectiveUsagePrice();
		this.quotePrice = new QuotePriceBean(quotePriceAudit.getQuotePrice());
	}



	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdTime
	 */
	public Timestamp getCreatedTime() {
		return createdTime;
	}

	/**
	 * @param createdTime the createdTime to set
	 */
	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * @return the fromArcPrice
	 */
	public Double getFromArcPrice() {
		return fromArcPrice;
	}

	/**
	 * @param fromArcPrice the fromArcPrice to set
	 */
	public void setFromArcPrice(Double fromArcPrice) {
		this.fromArcPrice = fromArcPrice;
	}

	/**
	 * @return the fromMrcPrice
	 */
	public Double getFromMrcPrice() {
		return fromMrcPrice;
	}

	/**
	 * @param fromMrcPrice the fromMrcPrice to set
	 */
	public void setFromMrcPrice(Double fromMrcPrice) {
		this.fromMrcPrice = fromMrcPrice;
	}

	/**
	 * @return the fromNrcPrice
	 */
	public Double getFromNrcPrice() {
		return fromNrcPrice;
	}

	/**
	 * @param fromNrcPrice the fromNrcPrice to set
	 */
	public void setFromNrcPrice(Double fromNrcPrice) {
		this.fromNrcPrice = fromNrcPrice;
	}

	/**
	 * @return the idDeletedOrRefreshed
	 */
	public Byte getIdDeletedOrRefreshed() {
		return idDeletedOrRefreshed;
	}

	/**
	 * @param idDeletedOrRefreshed the idDeletedOrRefreshed to set
	 */
	public void setIdDeletedOrRefreshed(Byte idDeletedOrRefreshed) {
		this.idDeletedOrRefreshed = idDeletedOrRefreshed;
	}

	/**
	 * @return the quoteRefId
	 */
	public String getQuoteRefId() {
		return quoteRefId;
	}

	/**
	 * @param quoteRefId the quoteRefId to set
	 */
	public void setQuoteRefId(String quoteRefId) {
		this.quoteRefId = quoteRefId;
	}

	/**
	 * @return the toMrcPrice
	 */
	public Double getToMrcPrice() {
		return toMrcPrice;
	}

	/**
	 * @param toMrcPrice the toMrcPrice to set
	 */
	public void setToMrcPrice(Double toMrcPrice) {
		this.toMrcPrice = toMrcPrice;
	}

	/**
	 * @return the toNrcPrice
	 */
	public Double getToNrcPrice() {
		return toNrcPrice;
	}

	/**
	 * @param toNrcPrice the toNrcPrice to set
	 */
	public void setToNrcPrice(Double toNrcPrice) {
		this.toNrcPrice = toNrcPrice;
	}

	/**
	 * @return the toArcPrice
	 */
	public Double getToArcPrice() {
		return toArcPrice;
	}

	/**
	 * @param toArcPrice the toArcPrice to set
	 */
	public void setToArcPrice(Double toArcPrice) {
		this.toArcPrice = toArcPrice;
	}

	/**
	 * @return the fromEffectiveUsagePrice
	 */
	public Double getFromEffectiveUsagePrice() {
		return fromEffectiveUsagePrice;
	}

	/**
	 * @param fromEffectiveUsagePrice the fromEffectiveUsagePrice to set
	 */
	public void setFromEffectiveUsagePrice(Double fromEffectiveUsagePrice) {
		this.fromEffectiveUsagePrice = fromEffectiveUsagePrice;
	}

	/**
	 * @return the toEffectiveUsagePrice
	 */
	public Double getToEffectiveUsagePrice() {
		return toEffectiveUsagePrice;
	}

	/**
	 * @param toEffectiveUsagePrice the toEffectiveUsagePrice to set
	 */
	public void setToEffectiveUsagePrice(Double toEffectiveUsagePrice) {
		this.toEffectiveUsagePrice = toEffectiveUsagePrice;
	}


	/**
	 * @return the quotePrice
	 */
	public QuotePriceBean getQuotePrice() {
		return quotePrice;
	}


	/**
	 * @param quotePrice the quotePrice to set
	 */
	public void setQuotePrice(QuotePriceBean quotePrice) {
		this.quotePrice = quotePrice;
	}


	/**
	 * @return the mstProductComponent
	 */
	public MstProductComponentDto getMstProductComponent() {
		return mstProductComponent;
	}


	/**
	 * @param mstProductComponent the mstProductComponent to set
	 */
	public void setMstProductComponent(MstProductComponentDto mstProductComponent) {
		this.mstProductComponent = mstProductComponent;
	}


	/**
	 * @return the productAttriuteMaster
	 */
	public ProductAttributeMasterBean getProductAttributeMaster() {
		return productAttributeMaster;
	}


	/**
	 * @param productAttriuteMaster the productAttriuteMaster to set
	 */
	public void setProductAttributeMaster(ProductAttributeMasterBean productAttributeMaster) {
		this.productAttributeMaster = productAttributeMaster;
	}


	/**
	 * @return the locationId
	 */
	public Integer getLocationId() {
		return locationId;
	}


	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}


	


	
	
	

	

}
