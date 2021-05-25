package com.tcl.dias.oms.partner.beans;

/**
 * Sales Funnel Data from SFDC
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class SfdcSalesFunnelData {

    Long totalAcv;
    Integer orderCount;

    public Long getTotalAcv() {
        return totalAcv;
    }

    public void setTotalAcv(Long totalAcv) {
        this.totalAcv = totalAcv;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }
}
