package com.tcl.dias.products.dto;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.productcatelog.entity.entities.AttributeGroupAttrAssoc;
import com.tcl.dias.productcatelog.entity.entities.AttributeMaster;
import com.tcl.dias.productcatelog.entity.entities.AttributeValueGroupAssoc;

/**
 * 
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * 
 * Data transfer object for Attribute master
 * 
 */
@JsonInclude(Include.NON_NULL)

public class AttributeMasterDto {

	@NotNull(message = Constants.ID_NULL)
	private Integer id;

	@Size(min = 1, max = 1, message = Constants.ISACTIVE_SIZE)
	@Pattern(regexp = Constants.ISACTIVE_PATTERN_REGEX, message = Constants.ISACTIVE_PATTERN)
	private String isActive;

	@Size(min = 1, max = 1, message = Constants.ISACTIVE_SIZE)
	private String isChargeable;

	private String description;

	private String name;

	@JsonProperty("attributeValues")
	private Set<AttributeValueDto> attributeValueDtoList;

	public AttributeMasterDto() {

	}

	public AttributeMasterDto(AttributeMaster attributeMaster) {

		
		if (attributeMaster != null && !(attributeMaster.getIsActive()!=null && attributeMaster.getIsActive().equals("N"))) {
			this.setId(attributeMaster.getId());
			this.setIsActive(attributeMaster.getIsActive());
			this.setIsChargeable(attributeMaster.getIsChargeableInd());
			this.setDescription(attributeMaster.getLongDesc());
			this.setName(attributeMaster.getName());
		/**
		 * 	TODO :Commented lines to be restored in case of malfunction
		  if (attributeMaster.getAttributeGroupAssocs() != null
					&& !attributeMaster.getAttributeGroupAssocs().isEmpty()) {
				this.setAttributeValueDtoList(attributeMaster.getAttributeGroupAssocs().stream()
						.filter(attrGrpAss -> !(attrGrpAss.getIsActive() != null
						&& attrGrpAss.getIsActive().equals("N")))
						.flatMap(attributeGroupAssoc -> attributeGroupAssoc.getAttributeValueGroupAssocList().stream())
						.map(AttributeValueGroupAssoc::getAttributeValue)
						.filter(attributeValue -> !(attributeValue.getIsActive() != null
								&& attributeValue.getIsActive().equals("N")))
						.map(AttributeValueDto::new).collect(Collectors.toSet()));
			}*/
		
			if (attributeMaster.getAttributeGroupAttrAssocs() != null
					&& !attributeMaster.getAttributeGroupAttrAssocs().isEmpty()) {
				this.setAttributeValueDtoList(attributeMaster.getAttributeGroupAttrAssocs().stream()
						.filter(attrGrpAss -> !(attrGrpAss.getIsActive() != null
						&& attrGrpAss.getIsActive().equals("N")))
						.flatMap(attGrpAttAs->attGrpAttAs.getAttributeValueGroupAssoc().stream())
						.map(AttributeValueGroupAssoc::getAttributeValue)
						.filter(attributeValue -> !(attributeValue.getIsActive() != null
								&& attributeValue.getIsActive().equals("N")))
						.map(AttributeValueDto::new).collect(Collectors.toSet()));		
			}
		}


 }
 
 public AttributeMasterDto(AttributeMaster attributeMaster,Set<AttributeGroupMasterDto> attributeGroupMasterList) {
        
 /*
 *      TODO :LInes which will help while debugging
 *      List<Integer> attrGrpIdsFromAttrMaster =null;
        if (attributeMaster.getAttributeGroupAttrAssocs() != null
                     && !attributeMaster.getAttributeGroupAttrAssocs().isEmpty()) {
        attrGrpIdsFromAttrMaster = attributeMaster.getAttributeGroupAttrAssocs().stream()
        .filter(attrGrpAss -> !(attrGrpAss.getIsActive() != null
        && attrGrpAss.getIsActive().equals("N")))
        .map(grpAttAss->grpAttAss.getAttributeGroupMaster().getId()).collect(Collectors.toList());
        }
*/
		List<Integer> attrGrpIds = attributeGroupMasterList.stream().map(AttributeGroupMasterDto::getId).collect(Collectors.toList());
		
	
		
				if (attributeMaster != null && !(attributeMaster.getIsActive()!=null && attributeMaster.getIsActive().equals("N"))) {
			this.setId(attributeMaster.getId());
			this.setIsActive(attributeMaster.getIsActive());
			this.setIsChargeable(attributeMaster.getIsChargeableInd());
			this.setDescription(attributeMaster.getLongDesc());
			this.setName(attributeMaster.getName());
	
			if (attributeMaster.getAttributeGroupAttrAssocs() != null
					&& !attributeMaster.getAttributeGroupAttrAssocs().isEmpty()) {
				this.setAttributeValueDtoList(attributeMaster.getAttributeGroupAttrAssocs().stream()
						.filter(attrGrpAss -> !(attrGrpAss.getIsActive() != null
						&& attrGrpAss.getIsActive().equals("N")))
						.map(AttributeGroupAttrAssoc::getAttributeGroupMaster)
						.filter(attrGrp->attrGrpIds.contains(attrGrp.getId()))
						.flatMap(grpMaster->grpMaster.getAttributeGroupAssocList().stream())
						.flatMap(attGrpAttAs->attGrpAttAs.getAttributeValueGroupAssoc().stream())
						.map(AttributeValueGroupAssoc::getAttributeValue)
						.filter(attributeValue -> !(attributeValue.getIsActive() != null
								&& attributeValue.getIsActive().equals("N")))
						.map(AttributeValueDto::new).collect(Collectors.toSet()));
							
			}
		}

 }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getIsChargeable() {
		return isChargeable;
	}

	public void setIsChargeable(String isChargeable) {
		this.isChargeable = isChargeable;
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

	

	@Override
	public String toString() {
		return "AttributeMasterDto [id=" + id + ", isActive=" + isActive + ", isChargeable=" + isChargeable
				+ ", description=" + description + ", name=" + name + ", attributeValueDtoList=" + attributeValueDtoList
				+ "]";
	}

	public Set<AttributeValueDto> getAttributeValueDtoList() {
		return attributeValueDtoList;
	}

	public void setAttributeValueDtoList(Set<AttributeValueDto> attributeValueDtoList) {
		this.attributeValueDtoList = attributeValueDtoList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attributeValueDtoList == null) ? 0 : attributeValueDtoList.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isActive == null) ? 0 : isActive.hashCode());
		result = prime * result + ((isChargeable == null) ? 0 : isChargeable.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		AttributeMasterDto other = (AttributeMasterDto) obj;
		if (attributeValueDtoList == null) {
			if (other.attributeValueDtoList != null)
				return false;
		} else if (!attributeValueDtoList.equals(other.attributeValueDtoList))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isActive == null) {
			if (other.isActive != null)
				return false;
		} else if (!isActive.equals(other.isActive))
			return false;
		if (isChargeable == null) {
			if (other.isChargeable != null)
				return false;
		} else if (!isChargeable.equals(other.isChargeable))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
