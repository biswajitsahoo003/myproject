package com.tcl.dias.oms.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for the dashboard related beans
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class DashboardCustomerbean {

    private Integer activeQuotesCount;

    private String customerName;

    private Integer customerId;

    private Integer activeOrderCount;

    private Integer totalOrderCount;

    private List<DashboardLegalEntityBean> legalEntityBeans;

    /**
     * @return the activeQuotesCount
     */
    public Integer getActiveQuotesCount() {
        return activeQuotesCount;
    }

    /**
     * @param activeQuotesCount the activeQuotesCount to set
     */
    public void setActiveQuotesCount(Integer activeQuotesCount) {
        this.activeQuotesCount = activeQuotesCount;
    }

    /**
     * @return the activeOrderCount
     */
    public Integer getActiveOrderCount() {
        return activeOrderCount;
    }

    /**
     * @param activeOrderCount the activeOrderCount to set
     */
    public void setActiveOrderCount(Integer activeOrderCount) {
        this.activeOrderCount = activeOrderCount;
    }

    /**
     * @return the legalEntityBeans
     */
    public List<DashboardLegalEntityBean> getLegalEntityBeans() {

        if (legalEntityBeans == null) {
            legalEntityBeans = new ArrayList<>();
        }
        return legalEntityBeans;
    }

    /**
     * @param legalEntityBeans the legalEntityBeans to set
     */
    public void setLegalEntityBeans(List<DashboardLegalEntityBean> legalEntityBeans) {
        this.legalEntityBeans = legalEntityBeans;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return the customerId
     */
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * @return the total order count
     */
    public Integer getTotalOrderCount() {
        return totalOrderCount;
    }

    /**
     * @param totalOrderCount the totalOrderCount to set
     */
    public void setTotalOrderCount(Integer totalOrderCount) {
        this.totalOrderCount = totalOrderCount;
    }


}
