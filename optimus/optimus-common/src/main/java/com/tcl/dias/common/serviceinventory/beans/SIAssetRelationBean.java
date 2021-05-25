package com.tcl.dias.common.serviceinventory.beans;

import java.util.Date;

/**
 * Bean class to contain Service Inventory asset relation data
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */
public class SIAssetRelationBean {
	private Integer assetId;
	private Integer siRelatedAssetId;
	private String relationType;
	private Date startDate;
	private Date endDate;

	public Integer getAssetId() {
		return assetId;
	}

	public void setAssetId(Integer assetId) {
		this.assetId = assetId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getSiRelatedAssetId() {
		return siRelatedAssetId;
	}

	public void setSiRelatedAssetId(Integer siRelatedAssetId) {
		this.siRelatedAssetId = siRelatedAssetId;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	@Override
	public String toString() {
		return "SIAssetRelationBean{" +
				"assetId=" + assetId +
				", siRelatedAssetId=" + siRelatedAssetId +
				", relationType='" + relationType + '\'' +
				", startDate=" + startDate +
				", endDate=" + endDate +
				'}';
	}
}
