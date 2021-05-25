package com.tcl.dias.common.serviceinventory.beans;

import java.sql.Timestamp;

/**
 * Bean class to contain Service Inventory attribute data
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */
public class SIAttributeBean {

	private String name;
	private String value;
	private String category;
	private String createdBy;
	private String updatedBy;
	private Timestamp updatedDate;
	private Timestamp createdDate;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public Timestamp getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "SIAttributeBean{" +
				"name='" + name + '\'' +
				", value='" + value + '\'' +
				", category='" + category + '\'' +
				", createdBy='" + createdBy + '\'' +
				", updatedBy='" + updatedBy + '\'' +
				", updatedDate=" + updatedDate +
				", createdDate=" + createdDate +
				'}';
	}
}
