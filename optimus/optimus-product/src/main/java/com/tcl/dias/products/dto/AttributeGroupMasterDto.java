package com.tcl.dias.products.dto;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.productcatelog.entity.entities.AttributeGroupAttrAssoc;
import com.tcl.dias.productcatelog.entity.entities.AttributeGroupMaster;
import com.tcl.dias.productcatelog.entity.entities.AttributeValueGroupAssoc;
import com.tcl.dias.productcatelog.entity.entities.ComponentAttributeGroupAssoc;

/**
 * This file contains the AttributeGroupMasterDto.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)
public class AttributeGroupMasterDto {
	
	private Integer id;

	private String name;

	private String description;

	private String reasonText;

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

	public String getReasonText() {
		return reasonText;
	}

	public void setReasonText(String reasonText) {
		this.reasonText = reasonText;
	}
	
	public AttributeGroupMasterDto (AttributeGroupMaster attributeGroupMaster) {
		if (attributeGroupMaster!=null && attributeGroupMaster.getIsActive()!=null && attributeGroupMaster.getIsActive()!="N") {
		this.setDescription(attributeGroupMaster.getDescription());
		this.setId(attributeGroupMaster.getId());
		this.setName(attributeGroupMaster.getName());
		this.setReasonText(attributeGroupMaster.getReasonText());
		}
	}
	
}
