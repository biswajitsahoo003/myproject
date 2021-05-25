package com.tcl.dias.productcatelog.entity.entities;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Immutable
@Table(name="vw_gsc_additional_cpe_bom_attributes")
@IdClass(CpeBomGscBomAttributesViewId.class)
public class CpeBomGscBomAttributesView implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="country_id")
	private Integer countryId;

	@Column(name="country_code")
	private String countryCode;

	@Id
	@Column(name="country_name")
	private String countryName;

	@Column(name="product_code")
	private String productCode;

	@Column(name="product_category")
	private String productCategory;

	@Column(name="discount_percent")
	private Double discountPercent;

	@Column(name="quantity")
	private int quantity;

	@Column(name="long_desc")
	private String longDesc;

	@Column(name="list_price")
	private Double listPrice;

	@Column(name="list_price_currency_id")
	private Integer listPriceCurrencyId;


	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public Double getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(Double discountPercent) {
		this.discountPercent = discountPercent;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getLongDesc() {
		return longDesc;
	}

	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}

	public Double getListPrice() {
		return listPrice;
	}

	public void setListPrice(Double listPrice) {
		this.listPrice = listPrice;
	}

	public Integer getListPriceCurrencyId() {
		return listPriceCurrencyId;
	}

	public void setListPriceCurrencyId(Integer listPriceCurrencyId) {
		this.listPriceCurrencyId = listPriceCurrencyId;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
}
