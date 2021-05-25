package com.tcl.dias.serviceassurance.entity.entities;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * 
 * Entity Class
 * 
 *The persistent class for the mst_service_catalog_variables database table.
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "mst_service_catalog_variables")
@NamedQuery(name = "MstServiceCatalogVariables.findAll", query = "SELECT n FROM MstServiceCatalogVariables n")

public class MstServiceCatalogVariables implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;

	@Column(name = "display_name")
	private String displayName;

	@Column(name = "snow_system_variable_id")
	private String snowSystemVariableId;

	private String code;

	private String type;
	
	@Column(name="category")
	private String category;
	
	public MstServiceCatalogVariables() {
	}

	@Lob
	@Column(name = "value_set")
	private String valueSet;

	@Lob
	@Column(name = "is_active")
	private String isActive;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getSnowSystemVariableId() {
		return snowSystemVariableId;
	}

	public void setSnowSystemVariableId(String snowSystemVariableId) {
		this.snowSystemVariableId = snowSystemVariableId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValueSet() {
		return valueSet;
	}

	public void setValueSet(String valueSet) {
		this.valueSet = valueSet;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	
	

}