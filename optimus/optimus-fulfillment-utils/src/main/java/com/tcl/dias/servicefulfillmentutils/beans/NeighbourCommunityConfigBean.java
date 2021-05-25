package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 
 * NeighbourCommunityConfig Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class NeighbourCommunityConfigBean implements Serializable {
	private static final long serialVersionUID = 1L;


	private Integer neighbourCommunityId;
	private String action;
	private String community;
	private Timestamp endDate;
	private Boolean iscommunityExtended;
	private Boolean ispreprovisioned;
	private Timestamp lastModifiedDate;
	private String modifiedBy;
	private String name;
	private String option;
	private Timestamp startDate;
	private String type;
	private boolean isEdited;
	public Integer getNeighbourCommunityId() {
		return neighbourCommunityId;
	}
	public void setNeighbourCommunityId(Integer neighbourCommunityId) {
		this.neighbourCommunityId = neighbourCommunityId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getCommunity() {
		return community;
	}
	public void setCommunity(String community) {
		this.community = community;
	}
	public Timestamp getEndDate() {
		return endDate;
	}
	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}
	public Boolean getIscommunityExtended() {
		return iscommunityExtended;
	}
	public void setIscommunityExtended(Boolean iscommunityExtended) {
		this.iscommunityExtended = iscommunityExtended;
	}
	public Boolean getIspreprovisioned() {
		return ispreprovisioned;
	}
	public void setIspreprovisioned(Boolean ispreprovisioned) {
		this.ispreprovisioned = ispreprovisioned;
	}
	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public Timestamp getStartDate() {
		return startDate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean edited) {
		isEdited = edited;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}