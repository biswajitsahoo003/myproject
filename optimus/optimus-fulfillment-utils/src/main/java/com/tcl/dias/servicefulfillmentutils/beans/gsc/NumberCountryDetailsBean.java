package com.tcl.dias.servicefulfillmentutils.beans.gsc;

public class NumberCountryDetailsBean {
	
	private Integer id;
	private String originCountry;         
	private String destinationCountry;   
    private String callType;   
	private String numbers;
	private String accessType;
	private String outpulse;
	private String billingStartDate;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOriginCountry() {
		return originCountry;
	}
	
	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}
	
	public String getDestinationCountry() {
		return destinationCountry;
	}
	
	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}
	
	public String getCallType() {
		return callType;
	}
	
	public void setCallType(String callType) {
		this.callType = callType;
	}
	
	public String getNumbers() {
		return numbers;
	}
	
	public void setNumbers(String numbers) {
		this.numbers = numbers;
	}
	
	public String getAccessType() {
		return accessType;
	}
	
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}
	
	public String getOutpulse() {
		return outpulse;
	} 
	
	public void setOutpulse(String outpulse) {
		this.outpulse = outpulse;
	}
	
	public String getBillingStartDate() {
		return billingStartDate;
	}
	
	public void setBillingStartDate(String billingStartDate) {
		this.billingStartDate = billingStartDate;
	}
	
	
	

}
