package com.tcl.dias.serviceassurance.entity.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * 
 * Entity Class
 * 
 *The persistent class for the service_catalog_to_variables_map database table.
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="service_catalog_to_variables_map")
@NamedQuery(name="ServiceCatalogToVariablesMap.findAll", query="SELECT n FROM ServiceCatalogToVariablesMap n")
public class ServiceCatalogToVariablesMap implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="catalog_id")
	private Integer catalogId;
	
	@Column(name="catalog_variable_id")
	private Integer catalogVariableId;
	
	@Column(name="display_order")
	private Integer displayOrder;
	
	@Column(name="is_active")
	private String  isActive;
	
	public ServiceCatalogToVariablesMap() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Integer catalogId) {
		this.catalogId = catalogId;
	}

	public Integer getCatalogVariableId() {
		return catalogVariableId;
	}

	public void setCatalogVariableId(Integer catalogVariableId) {
		this.catalogVariableId = catalogVariableId;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getIsAtive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	
}