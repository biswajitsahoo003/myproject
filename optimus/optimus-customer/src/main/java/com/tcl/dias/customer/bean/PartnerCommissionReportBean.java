package com.tcl.dias.customer.bean;

import java.util.List;

/**
 * This file contains the PartnerCommissionReportBean.java class.
 *
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class PartnerCommissionReportBean {
    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }


    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    private String baseCurrency;
    private String partnerName;

    public String getReportPeriod() {
        return reportPeriod;
    }

    public void setReportPeriod(String reportPeriod) {
        this.reportPeriod = reportPeriod;
    }

    private String reportPeriod;

    public List<CommissionReportBean> getCommissionReportBean() {
        return commissionReportBean;
    }

    public void setCommissionReportBean(List<CommissionReportBean> commissionReportBean) {
        this.commissionReportBean = commissionReportBean;
    }

    private List<CommissionReportBean> commissionReportBean;
}
