package com.tcl.dias.oms.beans;

import java.util.List;

/**
 * Bean file
 *
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OpportunityConfigurations {

    private List<OpportunityConfiguration> activeOptys;
    private Long totalElements;
    private Integer totalPages;

    public List<OpportunityConfiguration> getActiveOptys() {
        return activeOptys;
    }

    public void setActiveOptys(List<OpportunityConfiguration> activeQuotes) {
        this.activeOptys = activeQuotes;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public String toString() {
        return "QuoteConfigurations [activeOptys=" + activeOptys + "totalElements=" + totalElements + "totalPages="
                + totalPages + "]";
    }

}
