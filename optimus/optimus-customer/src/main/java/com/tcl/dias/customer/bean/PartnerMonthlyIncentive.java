package com.tcl.dias.customer.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Bean including  Partner Monthly Incentive
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartnerMonthlyIncentive {

    private Double sellWithIncentive;
    private Double oneTimeIncentive;
    private Double incentive;
    private String currency;

    public Double getSellWithIncentive() {
        return sellWithIncentive;
    }

    public void setSellWithIncentive(Double sellWithIncentive) {
        this.sellWithIncentive = sellWithIncentive;
    }

    public Double getOneTimeIncentive() {
        return oneTimeIncentive;
    }

    public void setOneTimeIncentive(Double oneTimeIncentive) {
        this.oneTimeIncentive = oneTimeIncentive;
    }

    public Double getIncentive() {
        return incentive;
    }

    public void setIncentive(Double incentive) {
        this.incentive = incentive;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
