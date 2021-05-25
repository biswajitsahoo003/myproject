package com.tcl.dias.oms.gsc.pdf.beans;

/**
 * Bean for globaloutbound display rate bean
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class GlobalOutboundDisplayRatesBean {

    private String destinationCountry;
    private String destinationName;
    private String displayCurrency;
    private String phoneType;
    private String price;
    private String status;
    private String comments;
    private String destId;
    private String remarks= "NA";

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getDisplayCurrency() {
        return displayCurrency;
    }

    public void setDisplayCurrency(String displayCurrency) {
        this.displayCurrency = displayCurrency;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "GlobalOutboundDisplayRatesBean{" +
                "destinationCountry='" + destinationCountry + '\'' +
                ", destinationName='" + destinationName + '\'' +
                ", displayCurrency='" + displayCurrency + '\'' +
                ", phoneType='" + phoneType + '\'' +
                ", price='" + price + '\'' +
                ", status='" + status + '\'' +
                ", comments='" + comments + '\'' +
                ", destId='" + destId + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
