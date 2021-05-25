package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the bom_physical_resource_assoc database table.
  *
 * @author Dinahar Vivekanandan
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited 
 */
@Entity
@Table(name="bom_physical_resource_assoc")
public class BomPhysicalResourceAssoc implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bom_id")
	public BomMaster bomMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "physical_resource_id")
	public PhysicalResource physicalResource;
	
	@Column(name = "relation")
	public String relation;	

    public BomPhysicalResourceAssoc() {
    	//TO DO
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public PhysicalResource getPhysicalResource() {
		return physicalResource;
	}

	public void setPhysicalResource(PhysicalResource physicalResource) {
		this.physicalResource = physicalResource;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public BomMaster getBomMaster() {
		return bomMaster;
	}

	public void setBomMaster(BomMaster bomMaster) {
		this.bomMaster = bomMaster;
	}

}