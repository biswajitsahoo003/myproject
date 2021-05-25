package com.tcl.dias.productcatelog.entity.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



/**
 * Entity class for bom inventory catalog assoc table
 * 
 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "bom_inventory_catalog_assoc")
public class BomInventoryCatalogAssoc {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "product_catalog_bom_name")
	private String productCatalogBomName;
	
	@Column(name = "inventory_bom_name")
	private String inventoryBomName;
	
	@Column(name="created")
	private Date created;
	
	@Column(name="created_by")
	private String  createdBy;
	
	@Column(name="updated")
	private Date updated;
	
	@Column(name="updated_by")
	private String updatedBy;

	public String getProductCatalogBomName() {
		return productCatalogBomName;
	}

	public void setProductCatalogBomName(String productCatalogBomName) {
		this.productCatalogBomName = productCatalogBomName;
	}

	public String getInventoryBomName() {
		return inventoryBomName;
	}

	public void setInventoryBomName(String inventoryBomName) {
		this.inventoryBomName = inventoryBomName;
	}

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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
	

}
