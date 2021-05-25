
package com.tcl.dias.oms.izosdwan.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the QuoteProductComponentUpdateBean.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "componentId",
    "componentName",
    "attributeBeans",
    "componentArc",
    "componentNrc"
})
public class QuoteProductComponentUpdateBean {

    @JsonProperty("componentId")
    private Integer componentId;
    @JsonProperty("componentName")
    private String componentName;
    @JsonProperty("attributeBeans")
    private List<AttributeUpdateBean> attributeBeans = null;
    @JsonProperty("componentArc")
    private Double componentArc;
    @JsonProperty("componentNrc")
    private Double componentNrc;
   
    @JsonProperty("componentId")
    public Integer getComponentId() {
        return componentId;
    }

    @JsonProperty("componentId")
    public void setComponentId(Integer componentId) {
        this.componentId = componentId;
    }

    @JsonProperty("attributeBeans")
    public List<AttributeUpdateBean> getAttributeBeans() {
        return attributeBeans;
    }

    @JsonProperty("attributeBeans")
    public void setAttributeBeans(List<AttributeUpdateBean> attributeBeans) {
        this.attributeBeans = attributeBeans;
    }

    @JsonProperty("componentArc")
    public Double getComponentArc() {
        return componentArc;
    }

    @JsonProperty("componentArc")
    public void setComponentArc(Double componentArc) {
        this.componentArc = componentArc;
    }

    @JsonProperty("componentNrc")
    public Double getComponentNrc() {
        return componentNrc;
    }

    @JsonProperty("componentNrc")
    public void setComponentNrc(Double componentNrc) {
        this.componentNrc = componentNrc;
    }

	public String getComponentName() {
		return componentName;
	}
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

}
