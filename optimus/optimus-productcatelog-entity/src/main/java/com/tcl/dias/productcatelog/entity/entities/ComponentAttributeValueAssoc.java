package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.tcl.dias.productcatelog.entity.base.BaseEntity;

/**
 * Entity class to map associations between component and attribute values
 * @author Dinahar Vivekanandan
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */
/*
@Entity
@Table(name = "component_attribute_value_assoc")
@NamedQuery(name = "ComponentAttributeValueAssoc.findAll", query = "SELECT v FROM ComponentAttributeValueAssoc v")*/
public class ComponentAttributeValueAssoc extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 7990888454184061745L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="attribute_value_id")
	private AttributeValue attributeValue;
	

	@Column(name ="reason_txt")
	private String reasonTxt;

}
