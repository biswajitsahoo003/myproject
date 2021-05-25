package com.tcl.dias.beans;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This file contains the SeverityTicketsBean.java class.
 * 
 *
 * @author DSIVALIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class TicketInfoBean {
	
	private String ticketNo;
	
	private String cuid;
	
	private String ticketStatus;
	
	private String ticketImpact;
	
	private Timestamp ticketCreationDate;
	
	private Integer ticketDownTimeInMts;
	
	private String rfoSpecification;
	
	private String serviceId;
	
	private String serviceType;
	
	private String rfoComments;
	
	private String circuitSpeed;


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
	 * @return the cuid
	 */
	public String getCuid() {
		return cuid;
	}

	/**
	 * @param cuid the cuid to set
	 */
	public void setCuid(String cuid) {
		this.cuid = cuid;
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
	 * @return the ticketImpact
	 */
	public String getTicketImpact() {
		return ticketImpact;
	}

	/**
	 * @param ticketImpact the ticketImpact to set
	 */
	public void setTicketImpact(String ticketImpact) {
		this.ticketImpact = ticketImpact;
	}

	/**
	 * @return the ticketSeverity
	 */
	public String getTicketSeverity() {
		return rfoComments;
	}

	/**
	 * @param ticketSeverity the ticketSeverity to set
	 */
	public void setTicketSeverity(String ticketSeverity) {
		this.rfoComments = ticketSeverity;
	}

	/**
	 * @return the ticketCreationDate
	 */
	public Timestamp getTicketCreationDate() {
		return ticketCreationDate;
	}

	/**
	 * @param ticketCreationDate the ticketCreationDate to set
	 */
	public void setTicketCreationDate(Timestamp ticketCreationDate) {
		this.ticketCreationDate = ticketCreationDate;
	}

	/**
	 * @return the ticketDurationInMts
	 */
	public Integer getTicketDurationInMts() {
		return ticketDownTimeInMts;
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
	 * @return the serviceTYpe
	 */
	public String getServiceTYpe() {
		return serviceType;
	}

	/**
	 * @param serviceTYpe the serviceTYpe to set
	 */
	public void setServiceType(String serviceTYpe) {
		this.serviceType = serviceTYpe;
	}

	/**
	 * @return the rfoComments
	 */
	public String getRfoComments() {
		return rfoComments;
	}

	/**
	 * @param rfoComments the rfoComments to set
	 */
	public void setRfoComments(String rfoComments) {
		this.rfoComments = rfoComments;
	}

	/**
	 * @return the serviceType
	 */
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * @param ticketDurationInMts the ticketDurationInMts to set
	 */
	public void setTicketDownTimeInMts(Integer ticketDurationInMts) {
		this.ticketDownTimeInMts = ticketDurationInMts;
	}

	/**
	 * @return the circuitSpeed
	 */
	public String getCircuitSpeed() {
		return circuitSpeed;
	}

	/**
	 * @param circuitSpeed the circuitSpeed to set
	 */
	public void setCircuitSpeed(String circuitSpeed) {
		this.circuitSpeed = circuitSpeed;
	}

	/**
	 * @return the ticketDownTimeInMts
	 */
	public Integer getTicketDownTimeInMts() {
		return ticketDownTimeInMts;
	}
	
}
