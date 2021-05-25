package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.tcl.dias.productcatelog.entity.base.BaseEntity;

/**
 * 
 * @author Dinahar V
 *
 */
@Entity
@Table(name = "attribute_group_master")
@NamedQuery(name = "AttributeGroupMaster.findAll", query = "SELECT c FROM AttributeGroupMaster c")

public class AttributeGroupMaster extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 9131768269063825734L;

	@Column(name = "nm")
	private String name;

	@Column(name = "long_desc")
	private String description;

	@Column(name = "reason_txt")
	private String reasonText;

	@OneToMany(mappedBy = "attributeGroupMaster")
	private List<ComponentAttributeGroupAssoc> componentAttributeGroupAssocList;

	@OneToMany(mappedBy = "attributeGroupMaster")
	private List<AttributeGroupAttrAssoc> attributeGroupAssocList;
	
	/*@OneToMany(mappedBy = "attributeGroupMaster")
	private List<AttributeValueGroupAssoc> attributeValueGroupAssoc;
*/
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

	public List<ComponentAttributeGroupAssoc> getComponentAttributeGroupAssocList() {
		return componentAttributeGroupAssocList;
	}

	public void setComponentAttributeGroupAssocList(
			List<ComponentAttributeGroupAssoc> componentAttributeGroupAssocList) {
		this.componentAttributeGroupAssocList = componentAttributeGroupAssocList;
	}

	public List<AttributeGroupAttrAssoc> getAttributeGroupAssocList() {
		return attributeGroupAssocList;
	}

	public void setAttributeGroupAssocList(List<AttributeGroupAttrAssoc> attributeGroupAssocList) {
		this.attributeGroupAssocList = attributeGroupAssocList;
	}

	@Override
	public String toString() {
		return "AttributeGroupMaster [name=" + name + ", description=" + description + ", reasonText=" + reasonText
				+ ", componentAttributeGroupAssocList=" + componentAttributeGroupAssocList
				+ ", attributeGroupAssocList=" + attributeGroupAssocList + "]";
	}


	/*public List<AttributeValueGroupAssoc> getAttributeValueGroupAssoc() {

		return attributeValueGroupAssoc;
	}

	public void setAttributeValueGroupAssoc(List<AttributeValueGroupAssoc> attributeValueGroupAssoc) {
		this.attributeValueGroupAssoc = attributeValueGroupAssoc;
	}*/
}
