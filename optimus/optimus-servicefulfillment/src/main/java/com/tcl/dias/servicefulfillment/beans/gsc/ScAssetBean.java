package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

public class ScAssetBean {
    private Integer tollfreeId;
    private String billStartDate;
    private String cdrLog;
    List<RoutingNumberBean> routingNumberBeans;

    public Integer getTollfreeId() {
        return tollfreeId;
    }

    public void setTollfreeId(Integer tollfreeId) {
        this.tollfreeId = tollfreeId;
    }

    public String getBillStartDate() {
        return billStartDate;
    }

    public void setBillStartDate(String billStartDate) {
        this.billStartDate = billStartDate;
    }

    public String getCdrLog() {
        return cdrLog;
    }

    public void setCdrLog(String cdrLog) {
        this.cdrLog = cdrLog;
    }

    public List<RoutingNumberBean> getRoutingNumberBeans() {
        return routingNumberBeans;
    }

    public void setRoutingNumberBeans(List<RoutingNumberBean> routingNumberBeans) {
        this.routingNumberBeans = routingNumberBeans;
    }
}
