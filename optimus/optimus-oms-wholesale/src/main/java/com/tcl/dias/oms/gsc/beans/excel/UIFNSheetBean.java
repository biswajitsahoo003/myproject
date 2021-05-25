package com.tcl.dias.oms.gsc.beans.excel;

/**
 * Bean for Order Enrichment Bulk upload excel
 *
 * @author Gnana prakash
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class UIFNSheetBean extends OrderEnrichmentExcel {

    private String originCountry;
    private String destinationCountry;
    private String quantityRequired;
    private String portedStatus;
    private String outPulseNumber;
    private String portedNumber;

    public UIFNSheetBean() {
    }

    public UIFNSheetBean(String originCountry, String quantityRequired, String portedStatus, String outPulseNumber, String portedNumber) {
        this.originCountry = originCountry;
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
        return "UIFNSheetBean{" +
                "originCountry='" + originCountry + '\'' +
                ", destinationCountry='" + destinationCountry + '\'' +
                ", quantityRequired='" + quantityRequired + '\'' +
                ", portedStatus='" + portedStatus + '\'' +
                ", outPulseNumber='" + outPulseNumber + '\'' +
                ", portedNumber='" + portedNumber + '\'' +
                '}';
    }
}
