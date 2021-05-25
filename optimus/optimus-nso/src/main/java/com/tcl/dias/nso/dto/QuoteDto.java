package com.tcl.dias.nso.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import com.tcl.dias.oms.entity.entities.Quote;

/**
 * This file contains the QuoteDto.java class. Dto Class
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class QuoteDto implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer createdBy;

	private Date createdTime;

	private Date effectiveDate;

	private Byte status;

	private Integer termInMonths;


	private Set<QuoteToLeDto> quoteToLes;
	
	private String quoteCode;
	
	private String stage;
	
	public QuoteDto() {
		
	}

	public QuoteDto(Quote quote) {
		if (quote != null) {
			this.id = quote.getId();
			this.createdBy = quote.getCreatedBy();
			this.createdTime = quote.getCreatedTime();
			this.status = quote.getStatus();
			this.termInMonths = quote.getTermInMonths();
			this.setQuoteToLes(quote.getQuoteToLes() != null
					? quote.getQuoteToLes().stream().map(QuoteToLeDto::new).collect(Collectors.toSet())
					: null);
			this.quoteCode = quote.getQuoteCode();
			quote.getQuoteToLes().forEach(quoteLe->this.stage = quoteLe.getStage());

		}
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getEffectiveDate() {
		return this.effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Integer getTermInMonths() {
		return this.termInMonths;
	}

	public void setTermInMonths(Integer termInMonths) {
		this.termInMonths = termInMonths;
	}


	/**
	 * @return the quoteToLes
	 */
	public Set<QuoteToLeDto> getQuoteToLes() {
		return quoteToLes;
	}

	/**
	 * @param quoteToLes
	 *            the quoteToLes to set
	 */
	public void setQuoteToLes(Set<QuoteToLeDto> quoteToLes) {
		this.quoteToLes = quoteToLes;
	}

	/**
	 * @return the quoteCode
	 */
	public String getQuoteCode() {
		return quoteCode;
	}

	/**
	 * @param quoteCode the quoteCode to set
	 */
	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	/**
	 * @return the stage
	 */
	public String getStage() {
		return stage;
	}

	/**
	 * @param stage the stage to set
	 */
	public void setStage(String stage) {
		this.stage = stage;
	}

	/**
	 * toString
	 * @return
	 */
	@Override
	public String toString() {
		return "QuoteDto [id=" + id + ", createdBy=" + createdBy + ", createdTime=" + createdTime + ", effectiveDate="
				+ effectiveDate + ", status=" + status + ", termInMonths=" + termInMonths + ", quoteToLes=" + quoteToLes
				+ "]";
	}
	
	

}