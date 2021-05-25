package com.tcl.dias.products.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.productcatelog.entity.entities.ProductCatalog;

/**
 * 
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * 
 * Data transfer object for Product Family entity
 */
@JsonInclude(Include.NON_NULL)
public class ProductFamilyDto {

	@NotNull(message = Constants.ID_NULL)
	private int id;
	private Date effectiveFrom;
	private Date effectiveTo;
	private String description;
	private String name;
	private String url;
	private String isMacdEnabledFlag;
	private String productShortName;


	public String getProductShortName() {
		return productShortName;
	}

	public void setProductShortName(String productShortName) {
		this.productShortName = productShortName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public Date getEffectiveTo() {
		return effectiveTo;
	}

	public void setEffectiveTo(Date effectiveTo) {
		this.effectiveTo = effectiveTo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ProductFamilyDto() {
	}

	public ProductFamilyDto(ProductCatalog productFamily) {
		if (productFamily != null) {
			this.setDescription(productFamily.getDescription());
			this.setEffectiveFrom(productFamily.getEffectiveFrom());
			this.setEffectiveTo(productFamily.getEffectiveTo());
			this.setId(productFamily.getId());
			this.setName(productFamily.getName());
			this.setUrl(productFamily.getUrl());
			this.setIsMacdEnabledFlag(productFamily.getIsMacdEnabledFlag());
			this.setProductShortName(productFamily.getProductShortName());
		}
	}

	public String getIsMacdEnabledFlag() {
		return isMacdEnabledFlag;
	}

	public void setIsMacdEnabledFlag(String isMacdEnabledFlag) {
		this.isMacdEnabledFlag = isMacdEnabledFlag;
	}

	@Override
	public String toString() {
		return "ProductFamilyDto [id=" + id + ", effectiveFrom=" + effectiveFrom + ", effectiveTo=" + effectiveTo
				+ ", description=" + description + ", name=" + name + ", url=" + url + ", isMacdEnabledFlag="
				+ isMacdEnabledFlag + ", productShortName=" + productShortName + "]";
	}

}
