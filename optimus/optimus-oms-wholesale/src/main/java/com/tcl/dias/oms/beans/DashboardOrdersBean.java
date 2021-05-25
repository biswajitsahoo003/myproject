package com.tcl.dias.oms.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

/**
 * This file contains the order bean for Dashboard.
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class DashboardOrdersBean {

    private OrdersBean orders;

    private List<OrderIllSiteBean> orderIllSite;

    private List<LegalAttributeBean> legalAttributes;

    /**
     * getOrders to get the orders
     *
     * @return
     */

    public OrdersBean getOrders() {
        return orders;
    }

    /**
     * setOrders the orderbeans to set
     *
     * @param orders
     */

    public void setOrders(OrdersBean orders) {
        this.orders = orders;
    }

    /**
     * getOrderIllSite to get the order Ill site
     *
     * @return
     */

    public List<OrderIllSiteBean> getOrderIllSite() {
        return orderIllSite;
    }

    /**
     * setOrderIllSite the order Ill site beans to set
     *
     * @param orderIllSite
     */

    public void setOrderIllSite(List<OrderIllSiteBean> orderIllSite) {
        this.orderIllSite = orderIllSite;
    }

    /**
     * getLegalAttributes
     *
     * @return returns the legal attributes list
     */

    public List<LegalAttributeBean> getLegalAttributes() {
        return legalAttributes;
    }

    /**
     * setLegalAttributes the legal attributes to set
     *
     * @param legalAttributes
     */

    public void setLegalAttributes(List<LegalAttributeBean> legalAttributes) {
        this.legalAttributes = legalAttributes;
    }


}
