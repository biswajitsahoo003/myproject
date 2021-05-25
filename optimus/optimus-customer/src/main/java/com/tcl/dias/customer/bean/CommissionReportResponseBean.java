package com.tcl.dias.customer.bean;

import java.util.List;
/**
 * This file contains the CommissionReportResponseBean.java class.
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CommissionReportResponseBean {
    private String reportDownloadDate;
    private List<PartnerCommissionReportBean> partnerCommissionReportBeans;

    public String getReportDownloadDate() {
        return reportDownloadDate;
    }

    public void setReportDownloadDate(String reportDownloadDate) {
        this.reportDownloadDate = reportDownloadDate;
    }

    public List<PartnerCommissionReportBean> getPartnerCommissionReportBeans() {
        return partnerCommissionReportBeans;
    }

    public void setPartnerCommissionReportBeans(List<PartnerCommissionReportBean> partnerCommissionReportBeans) {
        this.partnerCommissionReportBeans = partnerCommissionReportBeans;
    }
}
