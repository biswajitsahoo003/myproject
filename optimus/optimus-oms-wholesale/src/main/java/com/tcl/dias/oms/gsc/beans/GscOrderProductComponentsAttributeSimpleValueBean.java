package com.tcl.dias.oms.gsc.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tcl.dias.oms.entity.entities.OrderProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.OrdersLeAttributeValue;

/**
 * Product components and attribute value mapping for simple value
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GscOrderProductComponentsAttributeSimpleValueBean extends GscOrderProductComponentsAttributeValueBean {
    private String attributeValue;

    /**
     * Convert from OrderProductComponentsAttributeValue to
     * GscOrderProductComponentsAttributeSimpleValueBean
     *
     * @param orderProductComponentsAttributeValue
     * @return GscOrderProductComponentsAttributeSimpleValueBean
     */
    public static GscOrderProductComponentsAttributeSimpleValueBean fromAttribute(
            OrderProductComponentsAttributeValue orderProductComponentsAttributeValue) {
        GscOrderProductComponentsAttributeSimpleValueBean gscOrderProductComponentsAttributeSimpleValueBean = new GscOrderProductComponentsAttributeSimpleValueBean();
        gscOrderProductComponentsAttributeSimpleValueBean.setAttributeId(orderProductComponentsAttributeValue.getId());
        gscOrderProductComponentsAttributeSimpleValueBean
                .setAttributeName(orderProductComponentsAttributeValue.getProductAttributeMaster().getName());
        gscOrderProductComponentsAttributeSimpleValueBean
                .setDescription(orderProductComponentsAttributeValue.getProductAttributeMaster().getDescription());
        gscOrderProductComponentsAttributeSimpleValueBean
                .setDisplayValue(orderProductComponentsAttributeValue.getDisplayValue());
        gscOrderProductComponentsAttributeSimpleValueBean
                .setAttributeValue(orderProductComponentsAttributeValue.getAttributeValues());
        return gscOrderProductComponentsAttributeSimpleValueBean;
    }

    public static GscOrderProductComponentsAttributeSimpleValueBean formOrderLeAttributeValue(
            OrdersLeAttributeValue attributeValue) {
        GscOrderProductComponentsAttributeSimpleValueBean valueBean = new GscOrderProductComponentsAttributeSimpleValueBean();
        valueBean.setAttributeId(attributeValue.getId());
        valueBean.setAttributeName(attributeValue.getMstOmsAttribute().getName());
        valueBean.setDescription(attributeValue.getMstOmsAttribute().getDescription());
        valueBean.setAttributeValue(attributeValue.getAttributeValue());
        valueBean.setDisplayValue(attributeValue.getDisplayValue());
        return valueBean;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    @Override
    @JsonIgnore
    public String getValueString() {
        return this.getAttributeValue();
    }
}
