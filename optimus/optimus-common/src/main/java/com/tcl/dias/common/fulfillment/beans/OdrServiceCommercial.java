package com.tcl.dias.common.fulfillment.beans;

import java.sql.Timestamp;

/**
 * 
 * This file contains the bean for holding the IPC order service commercial values.
 * 
 *
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class OdrServiceCommercial {

	private Integer id;
	private Integer parentItem;
	private String itemType;
	private String item;
	private Double mrc;
	private Double nrc;
	private Double arc;
	private String createdBy;
	private Timestamp createdDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentItem() {
		return parentItem;
	}

	public void setParentItem(Integer parentItem) {
		this.parentItem = parentItem;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Double getMrc() {
		return mrc;
	}

	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	public Double getNrc() {
		return nrc;
	}

	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	public Double getArc() {
		return arc;
	}

	public void setArc(Double arc) {
		this.arc = arc;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "OdrServiceCommercialComponent [id=" + id + ", parentItem=" + parentItem + ", itemType=" + itemType
				+ ", item=" + item + ", mrc=" + mrc + ", nrc=" + nrc + ", arc=" + arc + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + "]";
	}

}
