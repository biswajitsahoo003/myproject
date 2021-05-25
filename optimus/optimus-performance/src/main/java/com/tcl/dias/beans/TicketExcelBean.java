package com.tcl.dias.beans;

/**
 * Bean to hold all ticket details which will be generated as Excel
 * 
 * @author KRUTSRIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 **/
public class TicketExcelBean {
	
	
	private String legalEntity;
	private String ticketNumber;
	private String informationSource;
	private String customerServiceId;
	private String impact;
	private String status;
	private String serviceDownTime;
	private String rfoResponsible;
	private String rfoCause;
	private String rfoComments;
	private String rfoSpecification;
	private String created;
	private String resolved;
	private String sumOfTotalOutageDuration;
	private String closed;
	public String getLegalEntity() {
		return legalEntity;
	}
	public void setLegalEntity(String legalEntity) {
		this.legalEntity = legalEntity;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public String getInformationSource() {
		return informationSource;
	}
	public void setInformationSource(String informationSource) {
		this.informationSource = informationSource;
	}
	public String getCustomerServiceId() {
		return customerServiceId;
	}
	public void setCustomerServiceId(String customerServiceId) {
		this.customerServiceId = customerServiceId;
	}
	public String getImpact() {
		return impact;
	}
	public void setImpact(String impact) {
		this.impact = impact;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getServiceDownTime() {
		return serviceDownTime;
	}
	public void setServiceDownTime(String serviceDownTime) {
		this.serviceDownTime = serviceDownTime;
	}
	public String getRfoResponsible() {
		return rfoResponsible;
	}
	public void setRfoResponsible(String rfoResponsible) {
		this.rfoResponsible = rfoResponsible;
	}
	public String getRfoCause() {
		return rfoCause;
	}
	public void setRfoCause(String rfoCause) {
		this.rfoCause = rfoCause;
	}
	public String getRfoComments() {
		return rfoComments;
	}
	public void setRfoComments(String rfoComments) {
		this.rfoComments = rfoComments;
	}
	
	public String getRfoSpecification() {
		return rfoSpecification;
	}
	public void setRfoSpecification(String rfoSpecification) {
		this.rfoSpecification = rfoSpecification;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getResolved() {
		return resolved;
	}
	public void setResolved(String resolved) {
		this.resolved = resolved;
	}
	public String getSumOfTotalOutageDuration() {
		return sumOfTotalOutageDuration;
	}
	public void setSumOfTotalOutageDuration(String sumOfTotalOutageDuration) {
		this.sumOfTotalOutageDuration = sumOfTotalOutageDuration;
	}
	public String getClosed() {
		return closed;
	}
	public void setClosed(String closed) {
		this.closed = closed;
	}

	
	
}
