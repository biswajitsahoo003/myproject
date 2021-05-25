package com.tcl.dias.oms.partner.beans.dnb;

/**
 * DnB Request Bean
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class DnBRequestBean {

    private String customerLeName;
    private String countryName;
    private String countryCode;
    private String classification;
    private String productName;
    private String partnerId;

    public String getCustomerLeName() {
        return customerLeName;
    }

    public void setCustomerLeName(String customerLeName) {
        this.customerLeName = customerLeName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }
}
