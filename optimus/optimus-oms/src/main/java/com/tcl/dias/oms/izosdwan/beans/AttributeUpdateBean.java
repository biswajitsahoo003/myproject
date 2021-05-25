
package com.tcl.dias.oms.izosdwan.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the AttributeUpdateBean.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "attributeComponentId",
    "attributeName",
    "quotePriceId",
    "arc",
    "nrc",
    "cost"
})
public class AttributeUpdateBean {

    @JsonProperty("attributeComponentId")
    private Integer attributeComponentId;
    @JsonProperty("attributeName")
    private String attributeName;
    @JsonProperty("quotePriceId")
    private Integer quotePriceId;
    @JsonProperty("arc")
    private Double arc;
    @JsonProperty("nrc")
    private Double nrc;
    @JsonProperty("cost")
    private Double cost;
  
    @JsonProperty("cost")
    public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	@JsonProperty("attributeComponentId")
    public Integer getAttributeComponentId() {
        return attributeComponentId;
    }

    @JsonProperty("attributeComponentId")
    public void setAttributeComponentId(Integer attributeComponentId) {
        this.attributeComponentId = attributeComponentId;
    }

    @JsonProperty("quotePriceId")
    public Integer getQuotePriceId() {
        return quotePriceId;
    }

    @JsonProperty("quotePriceId")
    public void setQuotePriceId(Integer quotePriceId) {
        this.quotePriceId = quotePriceId;
    }

    @JsonProperty("arc")
    public Double getArc() {
        return arc;
    }

    @JsonProperty("arc")
    public void setArc(Double arc) {
        this.arc = arc;
    }

    @JsonProperty("nrc")
    public Double getNrc() {
        return nrc;
    }

    @JsonProperty("nrc")
    public void setNrc(Double nrc) {
        this.nrc = nrc;
    }

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

}
