package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

public class ServiceAttributes {
	private Integer id;
	private List<String> terminationNumbers;
	private List<String> portNumbers;
	private Integer portedNumbersCount;
	private String terminationName;
	private Integer quantityOfNumbers;
	private Double fixedRatePerMin;
	private Double specialRatePerMin;
	private Double mobileRatePerMin;
	private Double surchargeRate;
	private String portingSericeNeeded;
	private String terminationNumberISDCode;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<String> getTerminationNumbers() {
		return terminationNumbers;
	}

	public void setTerminationNumbers(List<String> terminationNumbers) {
		this.terminationNumbers = terminationNumbers;
	}

	public List<String> getPortNumbers() {
		return portNumbers;
	}

	public void setPortNumbers(List<String> portNumbers) {
		this.portNumbers = portNumbers;
	}

	public Integer getPortedNumbersCount() {
		return portedNumbersCount;
	}

	public void setPortedNumbersCount(Integer portedNumbersCount) {
		this.portedNumbersCount = portedNumbersCount;
	}

	public String getTerminationName() {
		return terminationName;
	}

	public void setTerminationName(String terminationName) {
		this.terminationName = terminationName;
	}

	public Integer getQuantityOfNumbers() {
		return quantityOfNumbers;
	}

	public void setQuantityOfNumbers(Integer quantityOfNumbers) {
		this.quantityOfNumbers = quantityOfNumbers;
	}

	public Double getFixedRatePerMin() {
		return fixedRatePerMin;
	}

	public void setFixedRatePerMin(Double fixedRatePerMin) {
		this.fixedRatePerMin = fixedRatePerMin;
	}

	public Double getSpecialRatePerMin() {
		return specialRatePerMin;
	}

	public void setSpecialRatePerMin(Double specialRatePerMin) {
		this.specialRatePerMin = specialRatePerMin;
	}

	public Double getMobileRatePerMin() {
		return mobileRatePerMin;
	}

	public void setMobileRatePerMin(Double mobileRatePerMin) {
		this.mobileRatePerMin = mobileRatePerMin;
	}

	public Double getSurchargeRate() {
		return surchargeRate;
	}

	public void setSurchargeRate(Double surchargeRate) {
		this.surchargeRate = surchargeRate;
	}

	public String getPortingSericeNeeded() {
		return portingSericeNeeded;
	}

	public void setPortingSericeNeeded(String portingSericeNeeded) {
		this.portingSericeNeeded = portingSericeNeeded;
	}

	public String getTerminationNumberISDCode() {
		return terminationNumberISDCode;
	}

	public void setTerminationNumberISDCode(String terminationNumberISDCode) {
		this.terminationNumberISDCode = terminationNumberISDCode;
	}

}
