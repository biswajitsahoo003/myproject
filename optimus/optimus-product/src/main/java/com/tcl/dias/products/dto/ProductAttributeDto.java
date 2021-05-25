package com.tcl.dias.products.dto;

import java.util.Set;
import java.util.stream.Collectors;

import com.tcl.dias.productcatelog.entity.entities.AttributeMaster;
import com.tcl.dias.productcatelog.entity.entities.AttributeValue;
import com.tcl.dias.productcatelog.entity.entities.Component;

/**
 * This file contains the ProductAttributeDto.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ProductAttributeDto {
	
	
	private String customDisplayName;
	
	private String displayName;
	
	private String type;
	
	private String category;
	
	private String pagesNeeded;
	
	private Integer componentId;
	
	private Set<KeyValueDto> displayData;
	
	private String selectedValue;
	
	private Integer defaultValue;
	
	private Integer id;
	
	private String name;
	
	private String description;
	
	private String isActive;
	
	private Integer displayOrder;
	
	private Integer customDisplayOrder;
	
	private String customPageCategory;
	
	
	public String getCustomDisplayName() {
		return customDisplayName;
	}

	public void setCustomDisplayName(String customDisplayName) {
		this.customDisplayName = customDisplayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPagesNeeded() {
		return pagesNeeded;
	}

	public void setPagesNeeded(String pagesNeeded) {
		this.pagesNeeded = pagesNeeded;
	}

	
	public Integer getComponentId() {
		return componentId;
	}

	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}

	public Set<KeyValueDto> getDisplayData() {
		return displayData;
	}

	public void setDisplayData(Set<KeyValueDto> displayData) {
		this.displayData = displayData;
	}

	public String getSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}

	public Integer getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Integer defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Integer getCustomDisplayOrder() {
		return customDisplayOrder;
	}

	public void setCustomDisplayOrder(Integer customDisplayOrder) {
		this.customDisplayOrder = customDisplayOrder;
	}

	public String getCustomPageCategory() {
		return customPageCategory;
	}

	public void setCustomPageCategory(String customPageCategory) {
		this.customPageCategory = customPageCategory;
	}

	
	
	public  ProductAttributeDto(AttributeMaster attr,Set<AttributeValue> attrValues, Component component) {
		this.setCustomDisplayName(attr.getName());
		this.setDisplayName(attr.getName());
		
		String type = "";
		if (attrValues != null && attrValues.size()==2)
			type = "radio";
		if (attrValues != null && attrValues.size()==2)
			type = "select";
		
		this.setType(type);
		this.setCategory("basic");
		this.setPagesNeeded("[\"custom\", \"quote\", \"enrichment\"]");
		this.setComponentId(component.getId());
		if (attrValues!=null && !attrValues.isEmpty()) {
		this.setDisplayData(attrValues.stream().map(value-> constructKeyValuePair(value)).collect(Collectors.toSet()));
		}
		this.setSelectedValue("");
		this.setDefaultValue(0);
		this.setId(attr.getId());
		this.setName(attr.getName());
		this.setDescription(attr.getLongDesc());
		this.setIsActive(attr.getIsActive());
		this.setDisplayOrder(100);
		this.setCustomDisplayOrder(11);
		this.setCustomPageCategory("primary");
	}
	
	
	private KeyValueDto constructKeyValuePair(AttributeValue attributeValue) {

		String uomCd = "";
			if(attributeValue.getUomMaster()!=null && attributeValue.getUomMaster().getCd()!=null)
				uomCd = " "+attributeValue.getUomMaster().getCd();
			return new KeyValueDto(attributeValue.getAttrValues()+uomCd,attributeValue.getAttrValues()+uomCd);
	}
	

}
