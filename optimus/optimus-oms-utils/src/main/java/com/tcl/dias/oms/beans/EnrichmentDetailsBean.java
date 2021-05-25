package com.tcl.dias.oms.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Contains enrichment details site level
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class EnrichmentDetailsBean {

    private String quoteCode;
    private Map<String, List<QuoteProductComponentsAttributeValueBean>> siteEnrichmentDetails;
    private Map<String, List<QuoteProductComponentsAttributeValueBean>> linkEnrichmentDetails;
    private Map<String, CrossConnectEnrichmentBean> crossConnectEnrichmentBean;

    public String getQuoteCode() {
        return quoteCode;
    }

    public void setQuoteCode(String quoteCode) {
        this.quoteCode = quoteCode;
    }

    public Map<String, List<QuoteProductComponentsAttributeValueBean>> getSiteEnrichmentDetails() {
        return siteEnrichmentDetails;
    }

    public void setSiteEnrichmentDetails(Map<String, List<QuoteProductComponentsAttributeValueBean>> siteEnrichmentDetails) {
        this.siteEnrichmentDetails = siteEnrichmentDetails;
    }

    public Map<String, List<QuoteProductComponentsAttributeValueBean>> getLinkEnrichmentDetails() {
        return linkEnrichmentDetails;
    }

    public void setLinkEnrichmentDetails(Map<String, List<QuoteProductComponentsAttributeValueBean>> linkEnrichmentDetails) {
        this.linkEnrichmentDetails = linkEnrichmentDetails;
    }

    public Map<String, CrossConnectEnrichmentBean> getCrossConnectEnrichmentBean() {
        return crossConnectEnrichmentBean;
    }

    public void setCrossConnectEnrichmentBean(Map<String, CrossConnectEnrichmentBean> crossConnectEnrichmentBean) {
        this.crossConnectEnrichmentBean = crossConnectEnrichmentBean;
    }

    @Override
    public String toString() {
        return "EnrichmentDetailsBean{" +
                "quoteCode='" + quoteCode + '\'' +
                ", siteEnrichmentDetails=" + siteEnrichmentDetails +
                ", linkEnrichmentDetails=" + linkEnrichmentDetails +
                ", crossConnectEnrichmentBean=" + crossConnectEnrichmentBean +
                '}';
    }
}
