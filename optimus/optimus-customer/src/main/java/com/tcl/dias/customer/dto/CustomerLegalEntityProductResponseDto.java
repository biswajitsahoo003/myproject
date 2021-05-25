package com.tcl.dias.customer.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This file Contains customerLegal Entity information
 * 
 *
 * @author RSriramo
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)
public class CustomerLegalEntityProductResponseDto {

	@NotNull
	private String sple;

	@NotNull
	private String currency;

	@NotNull
	private boolean louRequired;

	@NotNull
	private Integer serviceProviderId;
	
	@NotNull
	private boolean isException;
	
	@NotNull
	private boolean isWithHoldTaxApplicable;

	public String getSple() {
		return sple;
	}

	public void setSple(String sple) {
		this.sple = sple;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public boolean isLouRequired() {
		return louRequired;
	}

	public void setLouRequired(boolean louRequired) {
		this.louRequired = louRequired;
	}

	public Integer getServiceProviderId() {
		return serviceProviderId;
	}

	public void setServiceProviderId(Integer serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}

	@Override
	public String toString() {
		return "CustomerLegalEntityProductResponseDto [sple=" + sple + ", currency=" + currency + ", louRequired="
				+ louRequired + ", serviceProviderId=" + serviceProviderId + ", isException=" + isException
				+ ", isWithHoldTaxApplicable=" + isWithHoldTaxApplicable + "]";
	}
	
	public boolean isException() {
		return isException;
	}

	public void setException(boolean isException) {
		this.isException = isException;
	}

	public boolean isWithHoldTaxApplicable() {
		return isWithHoldTaxApplicable;
	}

	public void setWithHoldTaxApplicable(boolean isWithHoldTaxApplicable) {
		this.isWithHoldTaxApplicable = isWithHoldTaxApplicable;
	}
}
