package com.tcl.dias.oms.gsc.pdf.beans;

import com.tcl.dias.oms.gsc.beans.GscQuoteConfigurationBean;

import java.util.List;

/**
 * PDF bean for global outbound template
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class GscCofOutboundPricesPdfBean {

    private List<GlobalOutboundDisplayRatesBean> globalOutboundDisplayRatesBeans;
    private String cofRefernceNumber;
    private String orderType;
    private String orderDate;
    private Boolean isApproved;
    private String isObjectStorage;
    private String ikey;
    private List<GscQuoteConfigurationBean> gscQuoteConfigurationBeans;
    private String paymentCurrency;

    public List<GlobalOutboundDisplayRatesBean> getGlobalOutboundDisplayRatesBeans() {
        return globalOutboundDisplayRatesBeans;
    }

    public void setGlobalOutboundDisplayRatesBeans(List<GlobalOutboundDisplayRatesBean> globalOutboundDisplayRatesBeans) {
        this.globalOutboundDisplayRatesBeans = globalOutboundDisplayRatesBeans;
    }

    public String getCofRefernceNumber() {
        return cofRefernceNumber;
    }

    public void setCofRefernceNumber(String cofRefernceNumber) {
        this.cofRefernceNumber = cofRefernceNumber;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public String getIsObjectStorage() {
        return isObjectStorage;
    }

    public void setIsObjectStorage(String isObjectStorage) {
        this.isObjectStorage = isObjectStorage;
    }

    public String getIkey() {
        return ikey;
    }

    public void setIkey(String ikey) {
        this.ikey = ikey;
    }

    public List<GscQuoteConfigurationBean> getGscQuoteConfigurationBeans() {
        return gscQuoteConfigurationBeans;
    }

    public void setGscQuoteConfigurationBeans(List<GscQuoteConfigurationBean> gscQuoteConfigurationBeans) {
        this.gscQuoteConfigurationBeans = gscQuoteConfigurationBeans;
    }

    public String getPaymentCurrency() {
        return paymentCurrency;
    }

    public void setPaymentCurrency(String paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }
}
