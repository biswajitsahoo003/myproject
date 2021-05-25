package com.tcl.dias.oms.gsc.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Joiner;

import java.util.List;

/**
 * Quote Bean for GSC Products which is contains quote details and configuration
 * details
 *
 * @author Vishesh Awasthi
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GscQuoteProductComponentsAttributeArrayValueBean extends GscQuoteProductComponentsAttributeValueBean {
    private List<String> attributeValue;

    @Override
    @JsonIgnore
    public String getValueString() {
        return Joiner.on(",").join(this.getAttributeValue());
    }

    public List<String> getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(List<String> attributeValue) {
        this.attributeValue = attributeValue;
    }
}
