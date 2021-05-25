package com.tcl.dias.oms.lr.beans;

/**
 * This file contains the LrResponse.java class.
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class LrResponse {

    private String orderId;
    private String lrDownloadUrl;
    private String product;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLrDownloadUrl() {
        return lrDownloadUrl;
    }

    public void setLrDownloadUrl(String lrDownloadUrl) {
        this.lrDownloadUrl = lrDownloadUrl;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "LrResponse [orderId=" + orderId + ", lrDownloadUrl=" + lrDownloadUrl + "]";
    }

}
