package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Bean for Classification
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassificationBean {

    private String sapCode;
    private String customerLegalName;
    private String classification;
    private String cuId;
    private Integer customerLeId;
    private Integer partnerLeId;
    private String partnerLeName;
    private Integer productFamilyId;

    public String getSapCode() {
        return sapCode;
    }

    public void setSapCode(String sapCode) {
        this.sapCode = sapCode;
    }

    public String getCustomerLegalName() {
        return customerLegalName;
    }

    public void setCustomerLegalName(String customerLegalName) {
        this.customerLegalName = customerLegalName;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getCuId() {
        return cuId;
    }

    public void setCuId(String cuId) {
        this.cuId = cuId;
    }

    public Integer getCustomerLeId() {
        return customerLeId;
    }

    public void setCustomerLeId(Integer customerLeId) {
        this.customerLeId = customerLeId;
    }

    public Integer getPartnerLeId() {
        return partnerLeId;
    }

    public void setPartnerLeId(Integer partnerLeId) {
        this.partnerLeId = partnerLeId;
    }

    public String getPartnerLeName() {
        return partnerLeName;
    }

    public void setPartnerLeName(String partnerLeName) {
        this.partnerLeName = partnerLeName;
    }

    public Integer getProductFamilyId() {
        return productFamilyId;
    }

    public void setProductFamilyId(Integer productFamilyId) {
        this.productFamilyId = productFamilyId;
    }

    @Override
    public String toString() {
        return "ClassificationBean{" +
                "sapCode='" + sapCode + '\'' +
                ", customerLegalName='" + customerLegalName + '\'' +
                ", classification='" + classification + '\'' +
                ", cuId='" + cuId + '\'' +
                ", customerLeId=" + customerLeId +
                ", partnerLeId=" + partnerLeId +
                ", partnerLeName='" + partnerLeName + '\'' +
                ", productFamilyId=" + productFamilyId +
                '}';
    }
}
