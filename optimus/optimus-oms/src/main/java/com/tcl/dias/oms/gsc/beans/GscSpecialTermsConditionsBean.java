package com.tcl.dias.oms.gsc.beans;

/**
 * Gsc COF special terms and conditions bean
 *
 * @author sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class GscSpecialTermsConditionsBean {
    private Integer quoteId;
    private String termsAndConditions;

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    @Override
    public String toString() {
        return "GscSpecialTermsConditionsBean{" +
                "quoteId=" + quoteId +
                ", termsAndConditions='" + termsAndConditions + '\'' +
                '}';
    }
}
