
package com.tcl.dias.nso.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "productName", "quoteId", "quoteToLe", "solutions" })
public class QuoteDetail {

	@JsonProperty("productName")
	private String productName;
	@JsonProperty("quoteId")
	private Integer quoteId;
	@JsonProperty("quoteToLe")
	private List<QuoteToLeDetail> quoteToLes;

	/**
	 * No args constructor for use in serialization
	 *
	 */
	public QuoteDetail() {
		// DO NOTHING
	}

	@JsonProperty("productName")
	public String getProductName() {
		return productName;
	}

	@JsonProperty("productName")
	public void setProductName(String productName) {
		this.productName = productName;
	}

	@JsonProperty("quoteId")
	public Object getQuoteId() {
		return quoteId;
	}

	@JsonProperty("quoteId")
	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public List<QuoteToLeDetail> getQuoteToLes() {
		return quoteToLes;
	}

	public void setQuoteToLes(List<QuoteToLeDetail> quoteToLes) {
		this.quoteToLes = quoteToLes;
	}

	

	@Override
	public String toString() {
		return "QuoteDetail [productName=" + productName + ", quoteId=" + quoteId + ", quoteToLes=" + quoteToLes
				+"]";
	}

}