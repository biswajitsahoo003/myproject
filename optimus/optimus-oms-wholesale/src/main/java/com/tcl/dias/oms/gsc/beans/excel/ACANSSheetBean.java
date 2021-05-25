package com.tcl.dias.oms.gsc.beans.excel;

/**
 * Bean for Order Enrichment Bulk upload excel
 *
 * @author Gnana prakash
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class ACANSSheetBean extends OrderEnrichmentExcel {

    private String originCountry;
    private String destinationCountry;
    private String city;
    private String quantityRequired;
    private String outPulseNumber;

    public ACANSSheetBean() {
    }

    public ACANSSheetBean(String originCountry, String city, String quantityRequired, String outPulseNumber) {
        this.originCountry = originCountry;
        this.city = city;
        this.quantityRequired = quantityRequired;
        this.outPulseNumber = outPulseNumber;
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

    public String getQuantityRequired() {
        return quantityRequired;
    }

    public void setQuantityRequired(String quantityRequired) {
        this.quantityRequired = quantityRequired;
    }

    public String getOutPulseNumber() {
        return outPulseNumber;
    }

    public void setOutPulseNumber(String outPulseNumber) {
        this.outPulseNumber = outPulseNumber;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    @Override
    public String toString() {
        return "ACANSSheetBean{" +
                "originCountry='" + originCountry + '\'' +
                ", destinationCountry='" + destinationCountry + '\'' +
                ", city='" + city + '\'' +
                ", quantityRequired='" + quantityRequired + '\'' +
                ", outPulseNumber='" + outPulseNumber + '\'' +
                '}';
    }
}
