package com.tcl.dias.oms.gsc.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;

/**
 * Product components and attribute value mapping for simple value
 *
 * @author Vishesh Awasthi
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GscQuoteProductComponentsAttributeSimpleValueBean extends GscQuoteProductComponentsAttributeValueBean {
    private String attributeValue;

    public static GscQuoteProductComponentsAttributeSimpleValueBean fromProductComponentAttributeValue(
            QuoteProductComponentsAttributeValue attributeValue) {
        GscQuoteProductComponentsAttributeSimpleValueBean valueBean = new GscQuoteProductComponentsAttributeSimpleValueBean();
        valueBean.setAttributeName(attributeValue.getProductAttributeMaster().getName());
        valueBean.setAttributeValue(attributeValue.getAttributeValues());
        valueBean.setAttributeId(attributeValue.getId());
        valueBean.setDescription(attributeValue.getProductAttributeMaster().getDescription());
        valueBean.setDisplayValue(attributeValue.getDisplayValue());
        return valueBean;
    }

    public static GscQuoteProductComponentsAttributeSimpleValueBean fromQuoteLeAttributeValue(
            QuoteLeAttributeValue attributeValue) {
        GscQuoteProductComponentsAttributeSimpleValueBean valueBean = new GscQuoteProductComponentsAttributeSimpleValueBean();
        valueBean.setAttributeName(attributeValue.getMstOmsAttribute().getName());
        valueBean.setAttributeValue(attributeValue.getAttributeValue());
        valueBean.setAttributeId(attributeValue.getId());
        valueBean.setDescription(attributeValue.getMstOmsAttribute().getDescription());
        valueBean.setDisplayValue(attributeValue.getDisplayValue());
        return valueBean;
    }

    @Override
    @JsonIgnore
    public String getValueString() {
        return this.getAttributeValue();
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }
}
