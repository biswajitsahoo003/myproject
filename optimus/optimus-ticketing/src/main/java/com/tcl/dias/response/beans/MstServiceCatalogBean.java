package com.tcl.dias.response.beans;

import java.io.Serializable;

import com.tcl.dias.serviceassurance.entity.entities.MstServiceCatalog;
/**
 * This file contains the MstServiceCatalogBean.java class.
 * 
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MstServiceCatalogBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer catalogId;
	
	private Integer parentId;
	
	private String  name;

	private String  displayName;
	
	private String  code;
	
	private String  isactive;
	
	public Integer getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Integer catalogId) {
		this.catalogId = catalogId;
	}
	
	public MstServiceCatalogBean(MstServiceCatalog mstServiceCatalog) {
		this.catalogId=mstServiceCatalog.getId();
		this.parentId=mstServiceCatalog.getParentId();
		this.name=mstServiceCatalog.getName();
		this.displayName=mstServiceCatalog.getDisplayName();
		this.code=mstServiceCatalog.getCode();
		this.isactive=mstServiceCatalog.getIsActive();
	}

	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getIsActive() {
		return isactive;
	}
	public void setIsActive(String isActive) {
		this.isactive = isActive;
	}

}
