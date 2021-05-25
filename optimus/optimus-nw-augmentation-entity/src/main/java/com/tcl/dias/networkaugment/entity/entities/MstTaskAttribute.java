package com.tcl.dias.networkaugment.entity.entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * 
 * This file contains the MstTaskAttribute.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "mst_task_attributes")
@NamedQuery(name = "MstTaskAttribute.findAll", query = "SELECT m FROM MstTaskAttribute m")
public class MstTaskAttribute implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "attribute_name")
	private String attributeName;

	private String category;

	@Column(name = "node_name")
	private String nodeName;

	@Column(name = "sub_category")
	private String subCategory;

	// bi-directional many-to-one association to MstTaskDef
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mst_task_def_key")
	private MstTaskDef mstTaskDef;

	public MstTaskAttribute() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAttributeName() {
		return this.attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public MstTaskDef getMstTaskDef() {
		return this.mstTaskDef;
	}

	public void setMstTaskDef(MstTaskDef mstTaskDef) {
		this.mstTaskDef = mstTaskDef;
	}

	/**
	 * @return the subCategory
	 */
	public String getSubCategory() {
		return subCategory;
	}

	/**
	 * @param subCategory the subCategory to set
	 */
	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

}