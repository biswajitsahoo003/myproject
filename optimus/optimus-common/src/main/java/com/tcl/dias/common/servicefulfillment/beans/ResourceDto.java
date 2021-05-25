package com.tcl.dias.common.servicefulfillment.beans;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.utils.Constants;

/**
 * Data transfer object for physical resources under CPE BOM
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)
public class ResourceDto {
	
	@NotNull(message = Constants.ID_NULL)
	private Integer id;
	
	private String productCode;
	
	private String longDesc;
	
	private String hsnCode;
	
	private Double listPrice;
	
	private Integer listPriceCurrencyId;
	
	private Integer providerId;
	
	private String productCategory;
	
	private String bomName;

	private Integer quantity;

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

	public String getBomName() {
		return bomName;
	}

	public void setBomName(String bomName) {
		this.bomName = bomName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	
}
