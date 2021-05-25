package com.tcl.dias.oms.gsc.beans;

import com.tcl.dias.oms.entity.entities.QuoteGsc;

import java.util.List;

/**
 * Quote Bean for GSC Products which is contains quote details and configuration
 * details
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscQuoteBean {

    private Integer id;
    private String accessType;
    private String serviceName;
    private Double arc;
    private Double mrc;
    private Double nrc;
    private Double tcv;
    private String productName;

    private List<GscQuoteConfigurationBean> configurations;

    public static GscOrderBean fromGscQuoteBean(GscQuoteBean gscQuoteBean) {
        GscOrderBean gscOrderBean = new GscOrderBean();
        gscOrderBean.setAccessType(gscQuoteBean.getAccessType());
        gscOrderBean.setServiceName(gscQuoteBean.getServiceName());
        gscOrderBean.setArc(gscQuoteBean.getArc());
        gscOrderBean.setMrc(gscQuoteBean.getMrc());
        gscOrderBean.setNrc(gscQuoteBean.getNrc());
        gscOrderBean.setTcv(gscQuoteBean.getTcv());
        return gscOrderBean;
    }

    public static GscQuoteBean fromQuoteGsc(QuoteGsc quoteGsc) {
        GscQuoteBean gscQuoteBean = new GscQuoteBean();
        gscQuoteBean.setId(quoteGsc.getId());
        gscQuoteBean.setAccessType(quoteGsc.getAccessType());
        gscQuoteBean.setServiceName(quoteGsc.getProductName());
        gscQuoteBean.setArc(quoteGsc.getArc());
        gscQuoteBean.setMrc(quoteGsc.getMrc());
        gscQuoteBean.setNrc(quoteGsc.getNrc());
        gscQuoteBean.setTcv(quoteGsc.getTcv());
        gscQuoteBean.setProductName(quoteGsc.getProductName());
        return gscQuoteBean;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Double getArc() {
        return arc;
    }

    public void setArc(Double arc) {
        this.arc = arc;
    }

    public Double getMrc() {
        return mrc;
    }

    public void setMrc(Double mrc) {
        this.mrc = mrc;
    }

    public Double getNrc() {
        return nrc;
    }

    public void setNrc(Double nrc) {
        this.nrc = nrc;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<GscQuoteConfigurationBean> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<GscQuoteConfigurationBean> configurations) {
        this.configurations = configurations;
    }

    public Double getTcv() {
        return tcv;
    }

    public void setTcv(Double tcv) {
        this.tcv = tcv;
    }
}
