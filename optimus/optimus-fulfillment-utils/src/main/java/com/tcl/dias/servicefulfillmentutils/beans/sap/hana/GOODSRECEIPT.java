
package com.tcl.dias.servicefulfillmentutils.beans.sap.hana;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "MTN_NO",
     "OBD",
     "Tracking_Number",
     "Received_Date",
     "Invoice_no",
})
public class GOODSRECEIPT {

    @JsonProperty("MTN_NO")
    private String mtnNo;

    @JsonProperty("OBD")
    private String obd;

    @JsonProperty("Tracking_Number")
    private String trackingNumber;

    @JsonProperty("Received_Date")
    private String receivedDate;

    @JsonProperty("Invoice_no")
    private String invoiceNo;
    @JsonProperty("MTN_NO")
    public String getMtnNo() {
        return mtnNo;
    }
    @JsonProperty("MTN_NO")
    public void setMtnNo(String mtnNo) {
        this.mtnNo = mtnNo;
    }
    @JsonProperty("OBD")
    public String getObd() {
        return obd;
    }
    @JsonProperty("OBD")
    public void setObd(String obd) {
        this.obd = obd;
    }
    @JsonProperty("Tracking_Number")
    public String getTrackingNumber() {
        return trackingNumber;
    }
    @JsonProperty("Tracking_Number")
    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }
    @JsonProperty("Received_Date")
    public String getReceivedDate() {
        return receivedDate;
    }
    @JsonProperty("Received_Date")
    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }
    @JsonProperty("Invoice_no")
    public String getInvoiceNo() {
        return invoiceNo;
    }
    @JsonProperty("Invoice_no")
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }



}
