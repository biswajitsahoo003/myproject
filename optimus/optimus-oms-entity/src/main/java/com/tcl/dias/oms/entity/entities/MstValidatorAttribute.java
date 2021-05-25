package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "mst_validator_attributes")
@NamedQuery(name = "MstValidatorAttribute.findAll", query = "SELECT m FROM MstValidatorAttribute m")
public class MstValidatorAttribute implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "node_name")
	private String nodeName;

	private String type;

	@Column(name = "x_path")
	private String xPath;

	// bi-directional many-to-one association to MstProductFamily
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private MstProductFamily mstProductFamily;

	public MstValidatorAttribute() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNodeName() {
		return this.nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getXPath() {
		return this.xPath;
	}

	public void setXPath(String xPath) {
		this.xPath = xPath;
	}

	public MstProductFamily getMstProductFamily() {
		return this.mstProductFamily;
	}

	public void setMstProductFamily(MstProductFamily mstProductFamily) {
		this.mstProductFamily = mstProductFamily;
	}

}