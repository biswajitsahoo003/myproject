package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Entity class for bom master table
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "bom_master")
public class BomMaster implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "bom_name")
	private String bomName;
	
	@Column(name = "bm_bom_name")
	private String bmBomName;
	
	@Column(name = "bom_type")
	private String bomType;
	
	@OneToMany(mappedBy = "bomMaster")
	private List<BomPhysicalResourceAssoc> bomPhysicalResourceAssocList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBomName() {
		return bomName;
	}

	public void setBomName(String bomName) {
		this.bomName = bomName;
	}

	public String getBmBomName() {
		return bmBomName;
	}

	public void setBmBomName(String bmBomName) {
		this.bmBomName = bmBomName;
	}

	public String getBomType() {
		return bomType;
	}

	public void setBomType(String bomType) {
		this.bomType = bomType;
	}

	public List<BomPhysicalResourceAssoc> getBomPhysicalResourceAssocList() {
		return bomPhysicalResourceAssocList;
	}

	public void setBomPhysicalResourceAssocList(List<BomPhysicalResourceAssoc> bomPhysicalResourceAssocList) {
		this.bomPhysicalResourceAssocList = bomPhysicalResourceAssocList;
	}

}
