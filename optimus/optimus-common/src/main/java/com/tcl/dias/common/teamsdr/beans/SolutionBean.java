package com.tcl.dias.common.teamsdr.beans;

/**
 * This file contains the SolutionBean.java
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class SolutionBean {
    private String country;
    private Integer solutionId;
    private String offeringName;
    private String productName;
    private Integer leId;
    private Integer quoteToLeId;

    public SolutionBean() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(Integer solutionId) {
        this.solutionId = solutionId;
    }

    public String getOfferingName() {
        return offeringName;
    }

    public void setOfferingName(String offeringName) {
        this.offeringName = offeringName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getLeId() {
        return leId;
    }

    public void setLeId(Integer leId) {
        this.leId = leId;
    }

    public Integer getQuoteToLeId() {
        return quoteToLeId;
    }

    public void setQuoteToLeId(Integer quoteToLeId) {
        this.quoteToLeId = quoteToLeId;
    }

    @Override
    public String toString() {
        return "SolutionBean{" +
                "country='" + country + '\'' +
                ", solutionId=" + solutionId +
                ", offeringName='" + offeringName + '\'' +
                ", productName='" + productName + '\'' +
                ", leId=" + leId +
                ", quoteToLeId=" + quoteToLeId +
                '}';
    }
}
