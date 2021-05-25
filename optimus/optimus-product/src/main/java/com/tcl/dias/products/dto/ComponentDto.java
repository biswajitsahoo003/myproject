package com.tcl.dias.products.dto;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.productcatelog.entity.entities.AttributeGroupAttrAssoc;
import com.tcl.dias.productcatelog.entity.entities.AttributeGroupMaster;
import com.tcl.dias.productcatelog.entity.entities.AttributeMaster;
import com.tcl.dias.productcatelog.entity.entities.Component;
import com.tcl.dias.productcatelog.entity.entities.ComponentAttributeGroupAssoc;


/**
 * Data transfer object for component
 * 
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)
public class ComponentDto {

	@NotNull(message = Constants.ID_NULL)
	private int id;

	private String description;

	private String name;

	private Integer parentId;
	
	private String isBackUp;

	@Size(min = 1, max = 1, message = Constants.ISACTIVE_SIZE)
	@Pattern(regexp = Constants.ISACTIVE_PATTERN_REGEX, message = Constants.ISACTIVE_PATTERN)
	private String isActive;

	@JsonProperty("attributeMasters")
	private Set<AttributeMasterDto> attributeMasterDtoList;
	
	@JsonIgnore
	@JsonProperty("attributeGroupMasters")
	private Set<AttributeGroupMasterDto> attributeGroupMasterList;

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public ComponentDto(Component component) {
		
		/*if (component != null && !(component.getIsActive()!=null && component.getIsActive().equals("N"))) {
			this.setDescription(component.getDescription());
			this.setName(component.getName());
			this.setParentId(component.getParentComponent() != null ? component.getParentComponent().getId() : null);
			this.setId(component.getId());
			this.setIsActive(component.getIsActive());
			if (component.getComponentAttributeGroupAssocs() != null) {
				this.setAttributeMasterDtoList(component.getComponentAttributeGroupAssocs().stream()
						.filter(compAttGrpAss -> !(compAttGrpAss.getIsActive() != null
						&& compAttGrpAss.getIsActive().equals("N")))
						.map(ComponentAttributeGroupAssoc::getAttributeGroupMaster)
						.filter(attributeGroupMaster -> !(attributeGroupMaster.getIsActive() != null
								&& attributeGroupMaster.getIsActive().equals("N")))
						.flatMap(attributeGroupMaster -> attributeGroupMaster.getAttributeGroupAssocList().stream())
						.filter(attrGroupAss -> !(attrGroupAss.getIsActive() != null
						&& attrGroupAss.getIsActive().equals("N")))
						.map(AttributeGroupAttrAssoc::getAttributeMaster)
						.filter(attributeMaster -> !(attributeMaster.getIsActive() != null
								&& attributeMaster.getIsActive().equals("N")))
						.map(AttributeMasterDto::new).collect(Collectors.toSet()));

			}

		}*/
		
		if (component != null && !(component.getIsActive()!=null && component.getIsActive().equals("N"))) {
			this.setDescription(component.getDescription());
			this.setName(component.getName());
			this.setParentId(component.getParentComponent() != null ? component.getParentComponent().getId() : null);
			this.setId(component.getId());
			this.setIsActive(component.getIsActive());
			
			if (component.getComponentAttributeGroupAssocs() != null) {
				this.setAttributeGroupMasterList(component.getComponentAttributeGroupAssocs().stream()
						.filter(compAttGrpAss -> !(compAttGrpAss.getIsActive() != null
						&& compAttGrpAss.getIsActive().equals("N")))
						.map(ComponentAttributeGroupAssoc::getAttributeGroupMaster)
						.filter(attributeGroupMaster -> !(attributeGroupMaster.getIsActive() != null
								&& attributeGroupMaster.getIsActive().equals("N")))
						.map(AttributeGroupMasterDto::new)
						.collect(Collectors.toSet()));

			}

			
			if (component.getComponentAttributeGroupAssocs() != null) {
				this.setAttributeMasterDtoList(component.getComponentAttributeGroupAssocs().stream()
						.filter(compAttGrpAss -> !(compAttGrpAss.getIsActive() != null
						&& compAttGrpAss.getIsActive().equals("N")))
						.map(ComponentAttributeGroupAssoc::getAttributeGroupMaster)
						.filter(attributeGroupMaster -> !(attributeGroupMaster.getIsActive() != null
								&& attributeGroupMaster.getIsActive().equals("N")))
						.flatMap(attributeGroupMaster -> attributeGroupMaster.getAttributeGroupAssocList().stream())
						.filter(attrGroupAss -> !(attrGroupAss.getIsActive() != null
						&& attrGroupAss.getIsActive().equals("N")))
						.map(AttributeGroupAttrAssoc::getAttributeMaster)
						.filter(attributeMaster -> !(attributeMaster.getIsActive() != null
								&& attributeMaster.getIsActive().equals("N")))
						.map(attributeMaster->new AttributeMasterDto(attributeMaster,this.getAttributeGroupMasterList())).collect(Collectors.toSet()));

			}

		}
	}
	
	

	/*private void process(AttributeGroupMaster attributeGroupMaster) {
		
		new AttributeMasterDto(attributeGroupMaster,attributeGroupMaster.getAttributeGroupAssocList().stream().filter(attrGroupAss -> !(attrGroupAss.getIsActive() != null
				&& attrGroupAss.getIsActive().equals("N")))
				.map(AttributeGroupAttrAssoc::getAttributeMaster).collect(Collectors.toList()));
		}*/

	
	public Set<AttributeGroupMasterDto> getAttributeGroupMasterList() {
		return attributeGroupMasterList;
	}

	public void setAttributeGroupMasterList(Set<AttributeGroupMasterDto> attributeGroupMasterList) {
		this.attributeGroupMasterList = attributeGroupMasterList;
	}

	public ComponentDto() {
	}

	@Override
	public String toString() {
		return "ComponentDto [id=" + id + ", description=" + description + ", name=" + name + ", parentId=" + parentId
				+ ", isActive=" + isActive + ", attributeMasterDtoList=" + attributeMasterDtoList + "]";
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getIsBackUp() {
		return isBackUp;
	}

	public void setIsBackUp(String isBackUp) {
		this.isBackUp = isBackUp;
	}

	public Set<AttributeMasterDto> getAttributeMasterDtoList() {
		return attributeMasterDtoList;
	}

	public void setAttributeMasterDtoList(Set<AttributeMasterDto> attributeMasterDtoList) {
		this.attributeMasterDtoList = attributeMasterDtoList;
	}

}
