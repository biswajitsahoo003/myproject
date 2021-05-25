package com.tcl.dias.response.beans;

import java.io.Serializable;

import com.tcl.dias.serviceassurance.entity.entities.MstServiceCatalogVariables;
/**
 * This file contains the ServiceCatalogVariablesBean.java class.
 * 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ServiceCatalogVariablesBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String displayName;
	private String snowSystemVariableId;
	private String code;
	private String type;
	private String category;
	
	private String valueSet;
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
	public ServiceCatalogVariablesBean(MstServiceCatalogVariables mstServiceCatalogVariables) {
		this.id=mstServiceCatalogVariables.getId();
		this.name=mstServiceCatalogVariables.getName();
		this.displayName=mstServiceCatalogVariables.getDisplayName();
		this.snowSystemVariableId=mstServiceCatalogVariables.getSnowSystemVariableId();
		this.code=mstServiceCatalogVariables.getCode();
		this.type=mstServiceCatalogVariables.getType();
		this.category=mstServiceCatalogVariables.getCategory();
		this.valueSet=mstServiceCatalogVariables.getValueSet();
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getValueSet() {
		return valueSet;
	}
	public void setValueSet(String valueSet) {
		this.valueSet = valueSet;
	}
	
	
	
}
