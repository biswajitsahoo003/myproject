
package com.tcl.dias.oms.izosdwan.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the PriceUpdateBean.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "referenceId",
    "quoteProductComponentBeans","referenceName"
})
public class PriceUpdateBean {

    @JsonProperty("referenceId")
    private Integer referenceId;
    @JsonProperty("quoteProductComponentBeans")
    private List<QuoteProductComponentUpdateBean> quoteProductComponentBeans = null;
    @JsonProperty("referenceName")
    private String referenceName;

    public Integer getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	@JsonProperty("quoteProductComponentBeans")
    public List<QuoteProductComponentUpdateBean> getQuoteProductComponentBeans() {
        return quoteProductComponentBeans;
    }

    @JsonProperty("quoteProductComponentBeans")
    public void setQuoteProductComponentBeans(List<QuoteProductComponentUpdateBean> quoteProductComponentBeans) {
        this.quoteProductComponentBeans = quoteProductComponentBeans;
    }

	public String getReferenceName() {
		return referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}


}
