package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * @author Dinahar V
 *
 */
@Entity
@Table(name = "attribute_value_group_assoc")
@NamedQuery(name = "AttributeValueGroupAssoc.findAll", query = "SELECT c FROM AttributeValueGroupAssoc c")

public class AttributeValueGroupAssoc implements Serializable {

	private static final long serialVersionUID = 7374203681599697013L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "created_dt")
	private Date createdTime;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "updated_dt")
	private Date updatedTime;

	@Column(name = "set_id")
	private Integer setId;

	@ManyToOne
	@JoinColumn(name = "attr_group_id")
	private AttributeGroupAttrAssoc attributeGroupAssoc;
	//private AttributeGroupMaster attributeGroupMaster;
	


	@ManyToOne
	@JoinColumn(name = "attr_value_id")
	private AttributeValue attributeValue;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Integer getSetId() {
		return setId;
	}

	public void setSetId(Integer setId) {
		this.setId = setId;
	}

	/*public AttributeGroupAttrAssoc getAttributeGroupAssoc() {
		return attributeGroupAssoc;
	}

	public void setAttributeGroupAssoc(AttributeGroupAttrAssoc attributeGroupAssoc) {
		this.attributeGroupAssoc = attributeGroupAssoc;
	}*/

	public AttributeValue getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(AttributeValue attributeValue) {
		this.attributeValue = attributeValue;
	}

	
/*public AttributeGroupMaster getAttributeGroupMaster() {

		return attributeGroupMaster;
	}

	public void setAttributeGroupMaster(AttributeGroupMaster attributeGroupMaster) {
		this.attributeGroupMaster = attributeGroupMaster;
	}*/

	public AttributeGroupAttrAssoc getAttributeGroupAssoc() {
		return attributeGroupAssoc;
	}

	public void setAttributeGroupAssoc(AttributeGroupAttrAssoc attributeGroupAssoc) {
		this.attributeGroupAssoc = attributeGroupAssoc;
	}


}
