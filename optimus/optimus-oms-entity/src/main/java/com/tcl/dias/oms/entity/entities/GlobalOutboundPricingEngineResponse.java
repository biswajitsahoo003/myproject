package com.tcl.dias.oms.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Entity for global out bound pricing engine response
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "global_outbound_pricing_engine_response")
@NamedQuery(name = "GlobalOutboundPricingEngineResponse.findAll", query = "SELECT p FROM GlobalOutboundPricingEngineResponse p")
public class GlobalOutboundPricingEngineResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "date_time")
	private Timestamp dateTime;

	@Column(name = "price_mode")
	private String priceMode;

	@Column(name = "pricing_type")
	private String pricingType;

	@Lob
	@Column(name = "request_data")
	private String requestData;

	@Lob
	@Column(name = "response_data")
	private String responseData;

	@Column(name = "quote_code")
	private String quoteCode;

	@Column(name = "quote_le_code")
	private String quoteLeCode;

	@Column(name = "is_negotiated_prices")
	private Byte isNegotiatedPrices;

	public GlobalOutboundPricingEngineResponse() {
		// DO NOTHING
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getDateTime() {
		return dateTime;
	}

	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}

	public String getPriceMode() {
		return priceMode;
	}

	public void setPriceMode(String priceMode) {
		this.priceMode = priceMode;
	}

	public String getPricingType() {
		return pricingType;
	}

	public void setPricingType(String pricingType) {
		this.pricingType = pricingType;
	}

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	public String getQuoteCode() {
		return quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	public String getQuoteLeCode() {
		return quoteLeCode;
	}

	public void setQuoteLeCode(String quoteLeCode) {
		this.quoteLeCode = quoteLeCode;
	}

	public Byte getIsNegotiatedPrices() {
		return isNegotiatedPrices;
	}

	public void setIsNegotiatedPrices(Byte isNegotiatedPrices) {
		this.isNegotiatedPrices = isNegotiatedPrices;
	}
}