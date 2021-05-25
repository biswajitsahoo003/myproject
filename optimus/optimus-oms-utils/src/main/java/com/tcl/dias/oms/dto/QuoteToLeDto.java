package com.tcl.dias.oms.dto;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;

/**
 * This file contains the QuoteToLeDto.java class. Dto Class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class QuoteToLeDto implements Serializable {

	private static final long serialVersionUID = 3018895448808159511L;

	private Integer id;

	private Integer currencyId;

	private Integer erfCusCustomerLegalEntityId;

	private Integer erfCusSpLegalEntityId;

	private Double finalMrc;

	private Double finalNrc;

	private Double proposedMrc;

	private Double proposedNrc;

	private String tpsSfdcOptyId;

	private Set<QuoteToLeProductFamilyDto> quoteToLeProductFamilies;

	private Set<QuoteToLeAttributeValueDto> quoteToLeAttributeValueDtos;
	
	private String stages;

	private String quoteCodes;
	
	public QuoteToLeDto(QuoteToLe quoteToLe) {
		if (quoteToLe != null) {
			this.id = quoteToLe.getId();
			this.finalMrc = quoteToLe.getFinalMrc();
			this.finalNrc = quoteToLe.getFinalNrc();
			this.proposedMrc = quoteToLe.getProposedMrc();
			this.proposedNrc = quoteToLe.getProposedNrc();
			this.currencyId = quoteToLe.getCurrencyId();
			this.erfCusCustomerLegalEntityId = quoteToLe.getErfCusCustomerLegalEntityId();
			this.erfCusSpLegalEntityId = quoteToLe.getErfCusSpLegalEntityId();
			this.tpsSfdcOptyId = quoteToLe.getTpsSfdcOptyId();

			if (quoteToLe.getQuoteLeAttributeValues() != null && !quoteToLe.getQuoteLeAttributeValues().isEmpty())
				this.setQuoteToLeAttributeValueDtos(quoteToLe.getQuoteLeAttributeValues().stream()
						.map(QuoteToLeAttributeValueDto::new).collect(Collectors.toSet()));
			this.stages = quoteToLe.getStage();
			this.quoteCodes = quoteToLe.getQuote().getQuoteCode();

		}
	}

	/**
	 * @return the quoteToLeProductFamilies
	 */
	public Set<QuoteToLeProductFamilyDto> getQuoteToLeProductFamilies() {
		return quoteToLeProductFamilies;
	}

	/**
	 * @param quoteToLeProductFamilies
	 *            the quoteToLeProductFamilies to set
	 */
	public void setQuoteToLeProductFamilies(Set<QuoteToLeProductFamilyDto> quoteToLeProductFamilies) {
		this.quoteToLeProductFamilies = quoteToLeProductFamilies;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCurrencyId() {
		return this.currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Integer getErfCusCustomerLegalEntityId() {
		return this.erfCusCustomerLegalEntityId;
	}

	public void setErfCusCustomerLegalEntityId(Integer erfCusCustomerLegalEntityId) {
		this.erfCusCustomerLegalEntityId = erfCusCustomerLegalEntityId;
	}

	public Integer getErfCusSpLegalEntityId() {
		return this.erfCusSpLegalEntityId;
	}

	public void setErfCusSpLegalEntityId(Integer erfCusSpLegalEntityId) {
		this.erfCusSpLegalEntityId = erfCusSpLegalEntityId;
	}

	public Double getFinalMrc() {
		return this.finalMrc;
	}

	public void setFinalMrc(Double finalMrc) {
		this.finalMrc = finalMrc;
	}

	public Double getFinalNrc() {
		return this.finalNrc;
	}

	public void setFinalNrc(Double finalNrc) {
		this.finalNrc = finalNrc;
	}

	public Double getProposedMrc() {
		return this.proposedMrc;
	}

	public void setProposedMrc(Double proposedMrc) {
		this.proposedMrc = proposedMrc;
	}

	public Double getProposedNrc() {
		return this.proposedNrc;
	}

	public void setProposedNrc(Double proposedNrc) {
		this.proposedNrc = proposedNrc;
	}

	public String getTpsSfdcOptyId() {
		return this.tpsSfdcOptyId;
	}

	public void setTpsSfdcOptyId(String tpsSfdcOptyId) {
		this.tpsSfdcOptyId = tpsSfdcOptyId;
	}

	public Set<QuoteToLeAttributeValueDto> getQuoteToLeAttributeValueDtos() {
		return quoteToLeAttributeValueDtos;
	}

	public void setQuoteToLeAttributeValueDtos(Set<QuoteToLeAttributeValueDto> quoteToLeAttributeValueDtos) {
		this.quoteToLeAttributeValueDtos = quoteToLeAttributeValueDtos;
	}

	/**
	 * @return the stage
	 */
	public String getStages() {
		return stages;
	}

	/**
	 * @param stage the stage to set
	 */
	public void setStages(String stages) {
		this.stages = stages;
	}
	

	/**
	 * @return the quoteCode
	 */
	public String getQuoteCodes() {
		return quoteCodes;
	}

	/**
	 * @param quoteCode the quoteCode to set
	 */
	public void setQuoteCodes(String quoteCodes) {
		this.quoteCodes = quoteCodes;
	}

	/**
	 * toString
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return "QuoteToLeDto [id=" + id + ", currencyId=" + currencyId + ", erfCusCustomerLegalEntityId="
				+ erfCusCustomerLegalEntityId + ", erfCusSpLegalEntityId=" + erfCusSpLegalEntityId + ", finalMrc="
				+ finalMrc + ", finalNrc=" + finalNrc + ", proposedMrc=" + proposedMrc + ", proposedNrc=" + proposedNrc
				+ ", tpsSfdcOptyId=" + tpsSfdcOptyId + ", quoteToLeProductFamilies=" + quoteToLeProductFamilies 
				+ ", quoteCodes=" + quoteCodes + ", stages=" + stages +"]";
	}

}