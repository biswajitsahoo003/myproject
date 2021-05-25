package com.tcl.dias.products.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.productcatelog.entity.entities.CpeBomGvpnIntlDetailView;
import com.tcl.dias.productcatelog.entity.entities.PhysicalResource;

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

	
	public ResourceDto () {
		
	}
	
	public  ResourceDto(PhysicalResource resource) {
		if (resource !=null) {
			this.setId(resource.getId());
			this.setLongDesc(resource.getLongDesc());
			this.setListPrice(resource.getListPrice());
			this.setListPriceCurrencyId(resource.getListPriceCurrencyId());
			this.setProductCode(resource.getProductCode());
			this.setHsnCode(resource.getHsnCode());
			this.setProviderId(resource.getProviderId());
			this.setProductCategory(resource.getProductCategory());
		}
		
	}
	
	
		
	
	
	

	public String getBomName() {
		return bomName;
	}

	public void setBomName(String bomName) {
		this.bomName = bomName;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "ResourceDto [id=" + id + ", productCode=" + productCode + ", longDesc=" + longDesc + ", hsnCode="
				+ hsnCode + ", listPrice=" + listPrice + ", listPriceCurrencyId=" + listPriceCurrencyId
				+ ", providerId=" + providerId + ", productCategory=" + productCategory + ",bomName=" +bomName+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hsnCode == null) ? 0 : hsnCode.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((listPrice == null) ? 0 : listPrice.hashCode());
		result = prime * result + ((listPriceCurrencyId == null) ? 0 : listPriceCurrencyId.hashCode());
		result = prime * result + ((longDesc == null) ? 0 : longDesc.hashCode());
		result = prime * result + ((productCategory == null) ? 0 : productCategory.hashCode());
		result = prime * result + ((productCode == null) ? 0 : productCode.hashCode());
		result = prime * result + ((providerId == null) ? 0 : providerId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceDto other = (ResourceDto) obj;
		if (hsnCode == null) {
			if (other.hsnCode != null)
				return false;
		} else if (!hsnCode.equals(other.hsnCode))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (listPrice == null) {
			if (other.listPrice != null)
				return false;
		} else if (!listPrice.equals(other.listPrice))
			return false;
		if (listPriceCurrencyId == null) {
			if (other.listPriceCurrencyId != null)
				return false;
		} else if (!listPriceCurrencyId.equals(other.listPriceCurrencyId))
			return false;
		if (longDesc == null) {
			if (other.longDesc != null)
				return false;
		} else if (!longDesc.equals(other.longDesc))
			return false;
		if (productCategory == null) {
			if (other.productCategory != null)
				return false;
		} else if (!productCategory.equals(other.productCategory))
			return false;
		if (productCode == null) {
			if (other.productCode != null)
				return false;
		} else if (!productCode.equals(other.productCode))
			return false;
		if (providerId == null) {
			if (other.providerId != null)
				return false;
		} else if (!providerId.equals(other.providerId))
			return false;
		return true;
	}



	
}
