package com.tcl.dias.products.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.productcatelog.entity.entities.Product;


/**
 * 
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * 
 * 
 *            Data transfer object for Product entity
 */

@JsonInclude(Include.NON_NULL)
public class ProductDto {

	@NotNull(message = Constants.ID_NULL)
	private Integer id;

	@Size(min = 1, max = 1, message = Constants.ISACTIVE_SIZE)
	private String isAddonService;

	@Size(min = 1, max = 1, message = Constants.ISACTIVE_SIZE)
	private String isTemplate;

	private String description;

	private String name;

	@NotNull(message = Constants.FOREIGN_KEY_NULL)
	private Integer parentId;

	@Size(min = 1, max = 1, message = Constants.ISACTIVE_SIZE)
	private String isActive;

	private String url;

	@JsonProperty("prodCompAssocs")
	private Set<ProductComponentAssocDto> productComponentAssocsDto;

	@JsonIgnore
	@JsonProperty("components")
	private List<ComponentDto> componentDtoList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIsAddonService() {
		return isAddonService;
	}

	public void setIsAddonService(String isAddonService) {
		this.isAddonService = isAddonService;
	}

	public String getIsTemplate() {
		return isTemplate;
	}

	public void setIsTemplate(String isTemplate) {
		this.isTemplate = isTemplate;
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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ProductDto() {
	}

	public ProductDto(Product product) {
		if (product != null && !(product.getIsActive() != null && product.getIsActive().equals("N"))) {
			this.setDescription(product.getDescription());
			this.setId(product.getId());
			this.setIsAddonService(product.getIsAddonService());
			this.setIsTemplate(product.getIsTemplate());
			this.setName(product.getName());
			this.setParentId(product.getParentId());
			this.setUrl(product.getUrl());
			this.setIsActive(product.getIsActive());

			this.setProductComponentAssocsDto(
					product.getProductComponentAssocs() != null
							? product.getProductComponentAssocs().stream()
									.filter(prodCompAssocs -> (!(prodCompAssocs.getIsActive() != null
											&& prodCompAssocs.getIsActive().equals("N"))))
									.map(ProductComponentAssocDto::new).collect(Collectors.toSet())
							: null);

		}
	}

	@Override
	public String toString() {
		return "ProductDto [id=" + id + ", isAddonService=" + isAddonService + ", isTemplate=" + isTemplate
				+ ", description=" + description + ", name=" + name + ", parentId=" + parentId + ", isActive="
				+ isActive + ", url=" + url + ", productComponentAssocsDto=" + productComponentAssocsDto
				+ ", componentDtoList=" + componentDtoList + "]";
	}

	public List<ComponentDto> getComponentDtoList() {
		return componentDtoList;
	}

	public void setComponentDtoList(List<ComponentDto> componentDtoList) {
		this.componentDtoList = componentDtoList;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Set<ProductComponentAssocDto> getProductComponentAssocsDto() {
		return productComponentAssocsDto;
	}

	public void setProductComponentAssocsDto(Set<ProductComponentAssocDto> productComponentAssocsDto) {
		this.productComponentAssocsDto = productComponentAssocsDto;
	}

}
