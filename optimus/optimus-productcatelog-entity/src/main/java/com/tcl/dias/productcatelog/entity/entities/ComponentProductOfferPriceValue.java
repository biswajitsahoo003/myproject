package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the component_product_offer_price_value database table.
 * 
 */
@Entity
@Table(name="component_product_offer_price_value")
public class ComponentProductOfferPriceValue implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Column(name="created_by")
	private String createdBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="created_dt")
	private Date createdDt;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="effective_from_dt")
	private Date effectiveFromDt;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="effective_to_dt")
	private Date effectiveToDt;

	@Column(name="is_active_ind")
	private String isActiveInd;

	private double price;

	@Column(name="reason_txt")
	private String reasonTxt;

	private String remarks;

	@Column(name="updated_by")
	private String updatedBy;

    @Temporal( TemporalType.TIMESTAMP)
	@Column(name="updated_dt")
	private Date updatedDt;

	//bi-directional many-to-one association to AttributeMaster
    @ManyToOne
	@JoinColumn(name="price_attr_id")
	private AttributeMaster attributeMaster;

	//bi-directional many-to-one association to ComponentProductOfferPrice
    @ManyToOne
	@JoinColumn(name="comp_pdt_offr_pr_id")
	private ComponentProductOfferPrice componentProductOfferPrice;

	//bi-directional many-to-one association to CurrencyMaster
    @ManyToOne
	@JoinColumn(name="currency_id")
	private CurrencyMaster currencyMaster;

    public ComponentProductOfferPriceValue() {
    	// TO DO
    }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public Date getEffectiveFromDt() {
		return this.effectiveFromDt;
	}

	public void setEffectiveFromDt(Date effectiveFromDt) {
		this.effectiveFromDt = effectiveFromDt;
	}

	public Date getEffectiveToDt() {
		return this.effectiveToDt;
	}

	public void setEffectiveToDt(Date effectiveToDt) {
		this.effectiveToDt = effectiveToDt;
	}

	public String getIsActiveInd() {
		return this.isActiveInd;
	}

	public void setIsActiveInd(String isActiveInd) {
		this.isActiveInd = isActiveInd;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getReasonTxt() {
		return this.reasonTxt;
	}

	public void setReasonTxt(String reasonTxt) {
		this.reasonTxt = reasonTxt;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public AttributeMaster getAttributeMaster() {
		return this.attributeMaster;
	}

	public void setAttributeMaster(AttributeMaster attributeMaster) {
		this.attributeMaster = attributeMaster;
	}
	
	public ComponentProductOfferPrice getComponentProductOfferPrice() {
		return this.componentProductOfferPrice;
	}

	public void setComponentProductOfferPrice(ComponentProductOfferPrice componentProductOfferPrice) {
		this.componentProductOfferPrice = componentProductOfferPrice;
	}
	
	public CurrencyMaster getCurrencyMaster() {
		return this.currencyMaster;
	}

	public void setCurrencyMaster(CurrencyMaster currencyMaster) {
		this.currencyMaster = currencyMaster;
	}
	
}