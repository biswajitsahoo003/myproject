package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "attribute_group_attr_assoc")
@NamedQuery(name = "AttributeGroupAttrAssoc.findAll", query = "SELECT c FROM AttributeGroupAttrAssoc c")

public class AttributeGroupAttrAssoc extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = 6596146154235440499L;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="group_id")
	private AttributeGroupMaster attributeGroupMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="attribute_id")
	private AttributeMaster attributeMaster;
	
	@OneToMany(mappedBy = "attributeGroupAssoc")
	private List<AttributeValueGroupAssoc> attributeValueGroupAssoc;

	

	public AttributeGroupMaster getAttributeGroupMaster() {
		return attributeGroupMaster;
	}

	public void setAttributeGroupMaster(AttributeGroupMaster attributeGroupMaster) {
		this.attributeGroupMaster = attributeGroupMaster;
	}

	public AttributeMaster getAttributeMaster() {
		return attributeMaster;
	}
	
	public void setAttributeMaster(AttributeMaster attributeMaster) {
		this.attributeMaster = attributeMaster;
	}


	@Override
	public String toString() {
		return "AttributeGroupAttrAssoc [attributeGroupMaster=" + attributeGroupMaster + ", attributeMaster="
				+ attributeMaster + "]";
	}

	public List<AttributeValueGroupAssoc> getAttributeValueGroupAssoc() {
		return attributeValueGroupAssoc;
	}

	public void setAttributeValueGroupAssoc(List<AttributeValueGroupAssoc> attributeValueGroupAssoc) {
		this.attributeValueGroupAssoc = attributeValueGroupAssoc;
	}

	



}
