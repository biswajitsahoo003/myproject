package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ViewPriceRevisionDetail class
 * 
 *
 * @author Veera B
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "vw_service_pricerevsn_dtl")
public class ViewPriceRevisionDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SERVICE_ID")
	private String serviceId;

	@Column(name = "MAX_PRICE_REV_DATE")
	private String maxPriceRevDate;

	@Column(name = "EFF_DATE_OF_PRICE_REVISION")
	private String effDateOfPriceRevision;

	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * @return the maxPriceRevDate
	 */
	public String getMaxPriceRevDate() {
		return maxPriceRevDate;
	}

	/**
	 * @param maxPriceRevDate the maxPriceRevDate to set
	 */
	public void setMaxPriceRevDate(String maxPriceRevDate) {
		this.maxPriceRevDate = maxPriceRevDate;
	}

	/**
	 * @return the effDateOfPriceRevision
	 */
	public String getEffDateOfPriceRevision() {
		return effDateOfPriceRevision;
	}

	/**
	 * @param effDateOfPriceRevision the effDateOfPriceRevision to set
	 */
	public void setEffDateOfPriceRevision(String effDateOfPriceRevision) {
		this.effDateOfPriceRevision = effDateOfPriceRevision;
	}
	
}
