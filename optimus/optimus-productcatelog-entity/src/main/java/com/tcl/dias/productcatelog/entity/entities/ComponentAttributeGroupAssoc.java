package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.tcl.dias.productcatelog.entity.base.BaseEntity;

/**
 * Entity class to map associations between component and attribute groups
 * @author Dinahar Vivekanandan
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */
@Entity
@Table(name = "component_attr_group_assoc")
@NamedQuery(name = "ComponentAttributeGroupAssoc.findAll", query = "SELECT c FROM ComponentAttributeGroupAssoc c")
public class ComponentAttributeGroupAssoc extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -8891064040495517137L;

	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "component_id")
	private Component component;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attr_group_id")
	private AttributeGroupMaster attributeGroupMaster;
	
	


	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public AttributeGroupMaster getAttributeGroupMaster() {
		return attributeGroupMaster;
	}

	public void setAttributeGroupMaster(AttributeGroupMaster attributeGroupMaster) {
		this.attributeGroupMaster = attributeGroupMaster;
	}

	@Override
	public String toString() {
		return "ComponentAttributeGroupAssoc [component=" + component + ", attributeGroupMaster=" + attributeGroupMaster
				+ "]";
	}

}
