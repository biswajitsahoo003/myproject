package com.tcl.dias.oms.beans;


/**
 * GeneralTermsAndConditionsBean class to get Terms And Conditions
 *
 *
 * @author Diksha Sharma
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GeneralTermsAndConditionsBean {
    private Integer id;
    private String termsAndConditions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    @Override
    public String toString() {
        return "GeneralTermsAndConditionBean{" +
                "id=" + id +
                ", termsAndConditions='" + termsAndConditions + '\'' +
                '}';
    }
}
