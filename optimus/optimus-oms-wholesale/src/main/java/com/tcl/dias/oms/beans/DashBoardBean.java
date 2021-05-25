package com.tcl.dias.oms.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.ArrayList;
import java.util.List;

/**
 * This file contains the DashBoardBean.java class.
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class DashBoardBean {

    private List<DashboardCustomerbean> dashboardCustomerbeans;
    private Long totalOrders;
    private Long activeOrders;
    private Long activeSites;
    private Long totalSites;

    /**
     * @return the dashboardCustomerbeans
     */
    public List<DashboardCustomerbean> getDashboardCustomerbeans() {
        if (dashboardCustomerbeans == null) {
            dashboardCustomerbeans = new ArrayList<>();
        }
        return dashboardCustomerbeans;
    }

    /**
     * @param dashboardCustomerbeans the dashboardCustomerbeans to set
     */
    public void setDashboardCustomerbeans(List<DashboardCustomerbean> dashboardCustomerbeans) {
        this.dashboardCustomerbeans = dashboardCustomerbeans;
    }

    /**
     * @return the totalOrders
     */
    public Long getTotalOrders() {
        return totalOrders;
    }

    /**
     * @param totalOrders the totalOrders to set
     */
    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    /**
     * @return the activeOrders
     */
    public Long getActiveOrders() {
        return activeOrders;
    }

    /**
     * @param activeOrders the activeOrders to set
     */
    public void setActiveOrders(Long activeOrders) {
        this.activeOrders = activeOrders;
    }

    /**
     * @return the activeSites
     */
    public Long getActiveSites() {
        return activeSites;
    }

    /**
     * @param activeSites the activeSites to set
     */
    public void setActiveSites(Long activeSites) {
        this.activeSites = activeSites;
    }

    /**
     * @return the totalSites
     */
    public Long getTotalSites() {
        return totalSites;
    }

    /**
     * @param totalSites the totalSites to set
     */
    public void setTotalSites(Long totalSites) {
        this.totalSites = totalSites;
    }


}
