package com.tcl.dias.customer.bean;

import java.util.Set;

/**
 * Class for Partner Product Classification Bean
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class PartnerProductClassificationBean {

    private Set<String> classification;

    public Set<String> getClassification() {
        return classification;
    }

    public void setClassification(Set<String> classification) {
        this.classification = classification;
    }

    @Override
    public String toString() {
        return "PartnerProductClassificationBean{" +
                "classification=" + classification +
                '}';
    }
}
