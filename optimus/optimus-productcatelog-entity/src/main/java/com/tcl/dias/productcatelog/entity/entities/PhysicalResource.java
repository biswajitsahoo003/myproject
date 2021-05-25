package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the physical_resource database table.
 * 
 */
@Entity
@Table(name="physical_resource")
public class PhysicalResource implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer id;
	
	@Column(name = "product_code")
	public String productCode;
	
	@Column(name = "long_desc")
	public String longDesc;
	
	@Column(name = "hsn_code")
	public String hsnCode;
	
	@Column(name = "list_price")
	public Double listPrice;
	
	@Column(name = "list_price_currency_id")
	public Integer listPriceCurrencyId;
	
	@Column(name = "provider_id")
	public Integer providerId;
	
	@Column(name = "product_category")
	public String productCategory;
	
	/*
	 * @Column(name="is_applicable_ntw_product") public String
	 * isApplicableNtwProduct;
	 * 
	 * @Column(name="is_applicable_gsc_product") public String
	 * isApplicableGscProduct;
	 */


    public PhysicalResource() {
    	// TO DO
    }


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getProductCode() {
		return productCode;
	}


	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}


	public String getLongDesc() {
		return longDesc;
	}


	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}


	public String getHsnCode() {
		return hsnCode;
	}


	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
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


	public Integer getProviderId() {
		return providerId;
	}


	public void setProviderId(Integer providerId) {
		this.providerId = providerId;
	}


	public String getProductCategory() {
		return productCategory;
	}


	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}


	/*
	 * public String getIsApplicableNtwProduct() { return isApplicableNtwProduct; }
	 * 
	 * 
	 * public void setIsApplicableNtwProduct(String isApplicableNtwProduct) {
	 * this.isApplicableNtwProduct = isApplicableNtwProduct; }
	 * 
	 * 
	 * public String getIsApplicableGscProduct() { return isApplicableGscProduct; }
	 * 
	 * 
	 * public void setIsApplicableGscProduct(String isApplicableGscProduct) {
	 * this.isApplicableGscProduct = isApplicableGscProduct; }
	 */
	

}