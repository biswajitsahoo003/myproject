package com.tcl.dias.customer.bean;

import java.util.List;

/**
 * This file contains the CommissionReportBean.java class.
 *
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CommissionReportBean {

    private String reportDownloadDate;
    private String reportPeriod;
    private String baseCurrency;
    private String commissionType;
    private String partnerName;
    private String totalBilledValueSellWith;
    private String totalBilledValueSellThru;
    private String totalBilledValueLeadReferral;
    private Double target;
    private String totalCommissionSellWith;
    private String totalCommissionSellThru;
    private String totalCommissionLeadReferral;
    private String totalCommissionAmmountSellWith;
    private String totalCommissionAmmountSellThru;
    private String totalCommissionAmmountLeadReferral;
    private List<CallidusPartnerCommisions> leadReferral;
    private List<CallidusPartnerCommisions> commisionsSellWith;
    private List<CallidusPartnerCommisions> commisionsSellThru;
    /** These below four properties used for Compensation Statement Summary report*/
    private Double overachievementValueSellWith;
    private Double overachievementValuetSellThru;
    private Double overachievementValueLeadReferral;
    private String totalEarnings;
    /** These below properties used for Yearly Summary report*/
    private List<YearlyCommissionReportBean> yearlyCommissionSellWith;
    private List<YearlyCommissionReportBean> yearlyCommissionSellThru;
    private List<YearlyCommissionReportBean> yearlyCommissionLeadReferral;
    private String businessTotal;
    private String commissionsTotal;
    private  String incentivesPaid;

    public String getBusinessTotal() {
        return businessTotal;
    }

    public void setBusinessTotal(String businessTotal) {
        this.businessTotal = businessTotal;
    }

    public String getCommissionsTotal() {
        return commissionsTotal;
    }

    public void setCommissionsTotal(String commissionsTotal) {
        this.commissionsTotal = commissionsTotal;
    }

    public String getIncentivesPaid() {
        return incentivesPaid;
    }

    public void setIncentivesPaid(String incentivesPaid) {
        this.incentivesPaid = incentivesPaid;
    }

    public List<YearlyCommissionReportBean> getYearlyCommissionSellWith() {
        return yearlyCommissionSellWith;
    }

    public void setYearlyCommissionSellWith(List<YearlyCommissionReportBean> yearlyCommissionSellWith) {
        this.yearlyCommissionSellWith = yearlyCommissionSellWith;
    }

    public List<YearlyCommissionReportBean> getYearlyCommissionSellThru() {
        return yearlyCommissionSellThru;
    }

    public void setYearlyCommissionSellThru(List<YearlyCommissionReportBean> yearlyCommissionSellThru) {
        this.yearlyCommissionSellThru = yearlyCommissionSellThru;
    }

    public List<YearlyCommissionReportBean> getYearlyCommissionLeadReferral() {
        return yearlyCommissionLeadReferral;
    }

    public void setYearlyCommissionLeadReferral(List<YearlyCommissionReportBean> yearlyCommissionLeadReferral) {
        this.yearlyCommissionLeadReferral = yearlyCommissionLeadReferral;
    }

    public String getTotalEarnings() {
        return totalEarnings;
    }
    public void setTotalEarnings(String totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public Double getOverachievementValueSellWith() {
        return overachievementValueSellWith;
    }

    public void setOverachievementValueSellWith(Double overachievementValueSellWith) {
        this.overachievementValueSellWith = overachievementValueSellWith;
    }

    public Double getOverachievementValuetSellThru() {
        return overachievementValuetSellThru;
    }

    public void setOverachievementValuetSellThru(Double overachievementValuetSellThru) {
        this.overachievementValuetSellThru = overachievementValuetSellThru;
    }

    public Double getOverachievementValueLeadReferral() {
        return overachievementValueLeadReferral;
    }

    public void setOverachievementValueLeadReferral(Double overachievementValueLeadReferral) {
        this.overachievementValueLeadReferral = overachievementValueLeadReferral;
    }

    public String getTotalBilledValueSellWith() {
        return totalBilledValueSellWith;
    }

    public void setTotalBilledValueSellWith(String totalBilledValueSellWith) {
        this.totalBilledValueSellWith = totalBilledValueSellWith;
    }

    public String getTotalBilledValueSellThru() {
        return totalBilledValueSellThru;
    }

    public void setTotalBilledValueSellThru(String totalBilledValueSellThru) {
        this.totalBilledValueSellThru = totalBilledValueSellThru;
    }

    public String getTotalBilledValueLeadReferral() {
        return totalBilledValueLeadReferral;
    }

    public void setTotalBilledValueLeadReferral(String totalBilledValueLeadReferral) {
        this.totalBilledValueLeadReferral = totalBilledValueLeadReferral;
    }

    public String getTotalCommissionSellWith() {
        return totalCommissionSellWith;
    }

    public void setTotalCommissionSellWith(String totalCommissionSellWith) {
        this.totalCommissionSellWith = totalCommissionSellWith;
    }

    public String getTotalCommissionSellThru() {
        return totalCommissionSellThru;
    }

    public void setTotalCommissionSellThru(String totalCommissionSellThru) {
        this.totalCommissionSellThru = totalCommissionSellThru;
    }

    public String getTotalCommissionLeadReferral() {
        return totalCommissionLeadReferral;
    }

    public void setTotalCommissionLeadReferral(String totalCommissionLeadReferral) {
        this.totalCommissionLeadReferral = totalCommissionLeadReferral;
    }

    public List<CallidusPartnerCommisions> getLeadReferral() {
        return leadReferral;
    }

    public void setLeadReferral(List<CallidusPartnerCommisions> leadReferral) {
        this.leadReferral = leadReferral;
    }

    public String getTotalCommissionAmmountSellWith() {
        return totalCommissionAmmountSellWith;
    }

    public void setTotalCommissionAmmountSellWith(String totalCommissionAmmountSellWith) {
        this.totalCommissionAmmountSellWith = totalCommissionAmmountSellWith;
    }

    public String getTotalCommissionAmmountSellThru() {
        return totalCommissionAmmountSellThru;
    }

    public void setTotalCommissionAmmountSellThru(String totalCommissionAmmountSellThru) {
        this.totalCommissionAmmountSellThru = totalCommissionAmmountSellThru;
    }

    public String getTotalCommissionAmmountLeadReferral() {
        return totalCommissionAmmountLeadReferral;
    }

    public void setTotalCommissionAmmountLeadReferral(String totalCommissionAmmountLeadReferral) {
        this.totalCommissionAmmountLeadReferral = totalCommissionAmmountLeadReferral;
    }

    public List<CallidusPartnerCommisions> getCommisionsSellWith() {
        return commisionsSellWith;
    }

    public void setCommisionsSellWith(List<CallidusPartnerCommisions> commisionsSellWith) {
        this.commisionsSellWith = commisionsSellWith;
    }

    public List<CallidusPartnerCommisions> getCommisionsSellThru() {
        return commisionsSellThru;
    }

    public void setCommisionsSellThru(List<CallidusPartnerCommisions> commisionsSellThru) {
        this.commisionsSellThru = commisionsSellThru;
    }



    public Double getTarget() {
        return target;
    }

    public void setTarget(Double target) {
        this.target = target;
    }



    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }


    public String getReportDownloadDate() {
        return reportDownloadDate;
    }

    public void setReportDownloadDate(String reportDownloadDate) {
        this.reportDownloadDate = reportDownloadDate;
    }

    public String getReportPeriod() {
        return reportPeriod;
    }

    public void setReportPeriod(String reportPeriod) {
        this.reportPeriod = reportPeriod;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getCommissionType() {
        return commissionType;
    }

    public void setCommissionType(String uniqueNumber) {
        this.commissionType = commissionType;
    }



}
