
package com.tcl.dias.products.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.productcatelog.entity.entities.Component;
import com.tcl.dias.productcatelog.entity.entities.ProductComponentAssoc;

/**
 * 
 * * @author Dinahar Vivekanandan
 * 
 * Data transfer object for ProductComponentAssoc entity
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)
public class ProductComponentAssocDto {

	@JsonIgnore
	@NotNull(message = Constants.ID_NULL)
	private int id;

	@JsonIgnore
	@Size(min = 1, max = 1, message = Constants.ISACTIVE_SIZE)
	private String isAdvanced;

	@Size(min = 1, max = 1, message = Constants.ISACTIVE_SIZE)
	private String isBackup;

	@JsonIgnore
	@NotNull(message = Constants.FOREIGN_KEY_NULL)
	private Integer componentId;

	private ComponentDto component;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIsAdvanced() {
		return isAdvanced;
	}

	public void setIsAdvanced(String isAdvanced) {
		this.isAdvanced = isAdvanced;
	}

	public String getIsBackup() {
		return isBackup;
	}

	public void setIsBackup(String isBackup) {
		this.isBackup = isBackup;
	}

	public Integer getComponentId() {
		return componentId;
	}

	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}

	public ProductComponentAssocDto(ProductComponentAssoc productComponentAssoc) {
		if (productComponentAssoc != null && !(productComponentAssoc.getIsActive()!=null && productComponentAssoc.getIsActive().equals("N"))) {
			this.setComponent(
					productComponentAssoc.getComponent() != null ? new ComponentDto(productComponentAssoc.getComponent()) : null);
			this.setComponentId(
					productComponentAssoc.getComponent() != null ? productComponentAssoc.getComponent().getId() : null);
			this.setId(productComponentAssoc.getId());
			this.setIsAdvanced(productComponentAssoc.getIsAdvanced());
			this.setIsBackup(productComponentAssoc.getIsBackup());
		}
	}

	public ProductComponentAssocDto() {
	}

	@Override
	public String toString() {
		return "ProductComponentAssocDto [id=" + id + ", isAdvanced=" + isAdvanced + ", isBackup=" + isBackup
				+ ", componentId=" + componentId + ", component=" + component + "]";
	}

	public ComponentDto getComponent() {
		return component;
	}

	public void setComponent(ComponentDto component) {
		this.component = component;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((component == null) ? 0 : component.hashCode());
		result = prime * result + ((componentId == null) ? 0 : componentId.hashCode());
		result = prime * result + id;
		result = prime * result + ((isAdvanced == null) ? 0 : isAdvanced.hashCode());
		result = prime * result + ((isBackup == null) ? 0 : isBackup.hashCode());
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
		ProductComponentAssocDto other = (ProductComponentAssocDto) obj;
		if (component == null) {
			if (other.component != null)
				return false;
		} else if (!component.equals(other.component))
			return false;
		if (componentId == null) {
			if (other.componentId != null)
				return false;
		} else if (!componentId.equals(other.componentId))
			return false;
		if (id != other.id)
			return false;
		if (isAdvanced == null) {
			if (other.isAdvanced != null)
				return false;
		} else if (!isAdvanced.equals(other.isAdvanced))
			return false;
		if (isBackup == null) {
			if (other.isBackup != null)
				return false;
		} else if (!isBackup.equals(other.isBackup))
			return false;
		return true;
	}

}
