package com.tcl.dias.oms.gsc.beans;

import java.util.List;

/**
 * Attributes for Gsc Quotes
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscQuoteAttributesBean {

    private Integer quoteId;
    private List<GscQuoteProductComponentsAttributeValueBean> attributes;

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    public List<GscQuoteProductComponentsAttributeValueBean> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<GscQuoteProductComponentsAttributeValueBean> attributes) {
        this.attributes = attributes;
    }
}
