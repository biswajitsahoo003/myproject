package com.tcl.dias.customer.dto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.customer.bean.BillingAddress;
import com.tcl.dias.customer.entity.entities.CustomerLeCountry;
import com.tcl.dias.customer.entity.entities.CustomerLeCurrency;
import com.tcl.dias.customer.entity.entities.CustomerLegalEntity;
import com.tcl.dias.customer.entity.entities.PartnerLegalEntity;
import org.springframework.util.CollectionUtils;

/**
 * This file Contains customerLegal Entity information
 * 
 *
 * @author NITHYA V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class CustomerLegalEntityDto {

	private String legalEntityName;

	private List<String> country;

	private String agreementId;

	private Integer legalEntityId;

	private String sfdcId;

	private AttributesDto attributes;

	private List<BillingAddress> billingAddresses;

	private String currency;

	private String poMandatory;

	/**
	 * the customerId to get
	 * 
	 * @return the customerId
	 */

	/**
	 * the agreementId to get
	 * 
	 * @return the agreementId
	 */
	public String getAgreementId() {
		return agreementId;
	}

	/**
	 * the agreementId to set
	 * 
	 * @param agreementId
	 */
	public void setAgreementId(String agreementId) {
		this.agreementId = agreementId;
	}

	/**
	 * @return the country
	 */
	public List<String> getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(List<String> country) {
		this.country = country;
	}

	public CustomerLegalEntityDto() {

	}

	public CustomerLegalEntityDto(CustomerLegalEntity customerLegalEntity) {

		if (customerLegalEntity != null) {
			this.legalEntityId = customerLegalEntity.getId();
			this.legalEntityName = customerLegalEntity.getEntityName();
			this.agreementId = customerLegalEntity.getAgreementId();
			this.sfdcId = customerLegalEntity.getTpsSfdcCuid();
			Set<CustomerLeCountry> custLeEn = customerLegalEntity.getCustomerLeCountries();
			if (custLeEn != null && !custLeEn.isEmpty()) {
				this.setCountry(custLeEn.stream().map(countryList -> countryList.getMstCountry().getName())
						.collect(Collectors.toList()));
			}
			if (!customerLegalEntity.getCustomerLeAttributeValues().isEmpty()) {
				this.setAttributes(new AttributesDto(customerLegalEntity.getCustomerLeAttributeValues()));
			}
			Set<CustomerLeCurrency> customerLeCurrencies = customerLegalEntity.getCustomerLeCurrencies();
			if(!CollectionUtils.isEmpty(customerLeCurrencies)) {
				this.currency = customerLeCurrencies.stream().findFirst().get().getCurrencyMaster().getShortName();
			}
		}

	}

	public CustomerLegalEntityDto(PartnerLegalEntity partnerLegalEntity) {
		if (partnerLegalEntity != null) {
			this.legalEntityId = partnerLegalEntity.getId();
			this.legalEntityName = partnerLegalEntity.getEntityName();
			this.agreementId = partnerLegalEntity.getAgreementId();
			this.sfdcId = partnerLegalEntity.getTpsSfdcCuid();
			if (!partnerLegalEntity.getPartnerLeAttributeValues().isEmpty()) {
				this.setAttributes(new AttributesDto(partnerLegalEntity.getPartnerLeAttributeValues(),"partner"));
			}
		}
	}

	public CustomerLegalEntityDto(CustomerLegalEntity customerLegalEntity, List<AttachmentDto> attachmentDto) {

		if (customerLegalEntity != null) {
			this.legalEntityId = customerLegalEntity.getId();
			this.legalEntityName = customerLegalEntity.getEntityName();
			this.agreementId = customerLegalEntity.getAgreementId();
			this.sfdcId = customerLegalEntity.getTpsSfdcCuid();
			Set<CustomerLeCountry> custLeEn = customerLegalEntity.getCustomerLeCountries();
			if (custLeEn != null && !custLeEn.isEmpty()) {
				this.setCountry(custLeEn.stream().map(countryList -> countryList.getMstCountry().getName())
						.collect(Collectors.toList()));
			}

			if (!customerLegalEntity.getCustomerLeAttributeValues().isEmpty()) {
				this.setAttributes(
						new AttributesDto(customerLegalEntity.getCustomerLeAttributeValues(), attachmentDto));
			}
		}
	}

	public AttributesDto getAttributes() {
		return attributes;
	}

	public void setAttributes(AttributesDto attributes) {
		this.attributes = attributes;
	}

	public String getLegalEntityName() {
		return legalEntityName;
	}

	public void setLegalEntityName(String legalEntityName) {
		this.legalEntityName = legalEntityName;
	}

	public Integer getLegalEntityId() {
		return legalEntityId;
	}

	public void setLegalEntityId(Integer legalEntityId) {
		this.legalEntityId = legalEntityId;
	}

	public String getSfdcId() {
		return sfdcId;
	}

	public void setSfdcId(String sfdcId) {
		this.sfdcId = sfdcId;
	}

	public List<BillingAddress> getBillingAddresses() {
		return billingAddresses;
	}

	public void setBillingAddresses(List<BillingAddress> billingAddresses) {
		this.billingAddresses = billingAddresses;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPoMandatory() {
		return poMandatory;
	}

	public void setPoMandatory(String poMandatory) {
		this.poMandatory = poMandatory;
	}
}
