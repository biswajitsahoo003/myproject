package com.tcl.dias.common.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Master Classification Product Family Bean
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class MstClassificationProductBean implements Serializable {

    private String classification;

    private Set<MstProductFamilyBean> products;

    public MstClassificationProductBean() {
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public Set<MstProductFamilyBean> getProducts() {
        return products;
    }

    public void setProducts(Set<MstProductFamilyBean> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "MstClassificationProductBean{" +
                "classification='" + classification + '\'' +
                ", products=" + products +
                '}';
    }
}