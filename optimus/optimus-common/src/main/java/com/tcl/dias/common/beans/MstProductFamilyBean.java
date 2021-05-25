package com.tcl.dias.common.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Master Product Family Bean
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MstProductFamilyBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private Byte status;

    private  Integer productCatalogFamilyId;

    private List<String> classification;

    public MstProductFamilyBean() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getStatus() {
        return this.status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getProductCatalogFamilyId() {
        return productCatalogFamilyId;
    }

    public void setProductCatalogFamilyId(Integer productCatalogFamilyId) {
        this.productCatalogFamilyId = productCatalogFamilyId;
    }


    public List<String> getClassification() {
        return classification;
    }

    public void setClassification(List<String> classification) {
        this.classification = classification;
    }

    @Override
    public String toString() {
        return "MstProductFamilyBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", productCatalogFamilyId=" + productCatalogFamilyId +
                ", classification=" + classification +
                '}';
    }
}