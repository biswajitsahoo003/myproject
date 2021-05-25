package com.tcl.dias.oms.gsc.beans.excel;

/**
 * Bean for Order Enrichment Bulk upload excel
 *
 * @author Gnana prakash
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class LNSSheetBean extends OrderEnrichmentExcel {

    private String originCountry;
    private String destinationCountry;
    private String city;
    private String npaAreaCode;
    private String quantityRequired;
    private String portedStatus;
    private String outPulseNumber;
    private String portedNumber;

    public LNSSheetBean() {
    }

    public LNSSheetBean(String originCountry, String city, String npaAreaCode, String quantityRequired, String portedStatus, String outPulseNumber, String portedNumber) {
        this.originCountry = originCountry;
        this.city = city;
        this.npaAreaCode = npaAreaCode;
        this.quantityRequired = quantityRequired;
        this.portedStatus = portedStatus;
        this.outPulseNumber = outPulseNumber;
        this.portedNumber = portedNumber;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNpaAreaCode() {
        return npaAreaCode;
    }

    public void setNpaAreaCode(String npaAreaCode) {
        this.npaAreaCode = npaAreaCode;
    }

    public String getQuantityRequired() {
        return quantityRequired;
    }

    public void setQuantityRequired(String quantityRequired) {
        this.quantityRequired = quantityRequired;
    }

    public String getPortedStatus() {
        return portedStatus;
    }

    public void setPortedStatus(String portedStatus) {
        this.portedStatus = portedStatus;
    }

    public String getOutPulseNumber() {
        return outPulseNumber;
    }

    public void setOutPulseNumber(String outPulseNumber) {
        this.outPulseNumber = outPulseNumber;
    }

    public String getPortedNumber() {
        return portedNumber;
    }

    public void setPortedNumber(String portedNumber) {
        this.portedNumber = portedNumber;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    @Override
    public String toString() {
        return "LNSSheetBean{" +
                "originCountry='" + originCountry + '\'' +
                ", destinationCountry='" + destinationCountry + '\'' +
                ", city='" + city + '\'' +
                ", npaAreaCode='" + npaAreaCode + '\'' +
                ", quantityRequired='" + quantityRequired + '\'' +
                ", portedStatus='" + portedStatus + '\'' +
                ", outPulseNumber='" + outPulseNumber + '\'' +
                ", portedNumber='" + portedNumber + '\'' +
                '}';
    }
}
