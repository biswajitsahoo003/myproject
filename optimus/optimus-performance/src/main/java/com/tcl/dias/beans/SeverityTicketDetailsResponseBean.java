package com.tcl.dias.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This file contains the severity wise ticket details.
 * 
 *
 * @author DSIVALIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class SeverityTicketDetailsResponseBean {

	private String serviceId;
	
	private String ticketNo;

	private String impact;

	private String categoryDetail;
	
	private String rfoResponsible;

	private String siteAddress;
	
	private String cktAlias;
	
	private String rfoSpecification;
	
	private String ticketStatus;

	private Integer ticketOutageInMts;
	
	private String serviceType;


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
	 * @return the ticketNo
	 */
	public String getTicketNo() {
		return ticketNo;
	}

	/**
	 * @param ticketNo the ticketNo to set
	 */
	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	/**
	 * @return the impact
	 */
	public String getImpact() {
		return impact;
	}

	/**
	 * @param impact the impact to set
	 */
	public void setImpact(String impact) {
		this.impact = impact;
	}

	/**
	 * @return the categoryDetail
	 */
	public String getCategoryDetail() {
		return categoryDetail;
	}

	/**
	 * @param categoryDetail the categoryDetail to set
	 */
	public void setCategoryDetail(String categoryDetail) {
		this.categoryDetail = categoryDetail;
	}

	/**
	 * @return the rfoResponsible
	 */
	public String getRfoResponsible() {
		return rfoResponsible;
	}

	/**
	 * @param rfoResponsible the rfoResponsible to set
	 */
	public void setRfoResponsible(String rfoResponsible) {
		this.rfoResponsible = rfoResponsible;
	}

	/**
	 * @return the siteAddress
	 */
	public String getSiteAddress() {
		return siteAddress;
	}

	/**
	 * @param siteAddress the siteAddress to set
	 */
	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	/**
	 * @return the cktAlias
	 */
	public String getCktAlias() {
		return cktAlias;
	}

	/**
	 * @param cktAlias the cktAlias to set
	 */
	public void setCktAlias(String cktAlias) {
		this.cktAlias = cktAlias;
	}

	/**
	 * @return the rfoSpecification
	 */
	public String getRfoSpecification() {
		return rfoSpecification;
	}

	/**
	 * @param rfoSpecification the rfoSpecification to set
	 */
	public void setRfoSpecification(String rfoSpecification) {
		this.rfoSpecification = rfoSpecification;
	}

	/**
	 * @return the ticketStatus
	 */
	public String getTicketStatus() {
		return ticketStatus;
	}

	/**
	 * @param ticketStatus the ticketStatus to set
	 */
	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	/**
	 * @return the ticketOutageInMts
	 */
	public Integer getTicketOutageInMts() {
		return ticketOutageInMts;
	}

	/**
	 * @param ticketOutageInMts the ticketOutageInMts to set
	 */
	public void setTicketOutageInMts(Integer ticketOutageInMts) {
		this.ticketOutageInMts = ticketOutageInMts;
	}

	/**
	 * @return the serviceType
	 */
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType the serviceType to set
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	
}
