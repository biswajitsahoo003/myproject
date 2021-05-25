package com.tcl.dias.servicefulfillment.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import org.springframework.format.annotation.DateTimeFormat;

public class ProvideMinBean extends TaskDetailsBaseBean {
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String cpeDispatchDate;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String cpeDeliveryDate;
    private String courierDispatchVendorName;
    private String courierDispatchNumber;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String cpeModelEndofsale;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String cpeModelEndoflife ;
    private String cpeSerialNumber;
    //private String cpeMrnNumber;
    private String cpeMinNumber;
    private String courierTrackNumber;
    private String vehicleDocketTrackNumber;

    public String getCpeDispatchDate() {
        return cpeDispatchDate;
    }

    public void setCpeDispatchDate(String cpeDispatchDate) {
        this.cpeDispatchDate = cpeDispatchDate;
    }

    public String getCpeDeliveryDate() {
        return cpeDeliveryDate;
    }

    public void setCpeDeliveryDate(String cpeDeliveryDate) {
        this.cpeDeliveryDate = cpeDeliveryDate;
    }

    public String getCourierDispatchVendorName() {
        return courierDispatchVendorName;
    }

    public void setCourierDispatchVendorName(String courierDispatchVendorName) {
        this.courierDispatchVendorName = courierDispatchVendorName;
    }

    public String getCourierDispatchNumber() {
        return courierDispatchNumber;
    }

    public void setCourierDispatchNumber(String courierDispatchNumber) {
        this.courierDispatchNumber = courierDispatchNumber;
    }

    public String getCpeModelEndofsale() {
        return cpeModelEndofsale;
    }

    public void setCpeModelEndofsale(String cpeModelEndofsale) {
        this.cpeModelEndofsale = cpeModelEndofsale;
    }

    public String getCpeModelEndoflife() {
        return cpeModelEndoflife;
    }

    public void setCpeModelEndoflife(String cpeModelEndoflife) {
        this.cpeModelEndoflife = cpeModelEndoflife;
    }

    public String getCpeSerialNumber() {
        return cpeSerialNumber;
    }

    public void setCpeSerialNumber(String cpeSerialNumber) {
        this.cpeSerialNumber = cpeSerialNumber;
    }

    public String getCpeMinNumber() {
        return cpeMinNumber;
    }

    public void setCpeMinNumber(String cpeMinNumber) {
        this.cpeMinNumber = cpeMinNumber;
    }

    public String getCourierTrackNumber() {
        return courierTrackNumber;
    }

    public void setCourierTrackNumber(String courierTrackNumber) {
        this.courierTrackNumber = courierTrackNumber;
    }

    public String getVehicleDocketTrackNumber() {
        return vehicleDocketTrackNumber;
    }

    public void setVehicleDocketTrackNumber(String vehicleDocketTrackNumber) {
        this.vehicleDocketTrackNumber = vehicleDocketTrackNumber;
    }

    @Override
    public String toString() {
        return "ProvideMinBean{" +
                "cpeDispatchDate='" + cpeDispatchDate + '\'' +
                ", cpeDeliveryDate='" + cpeDeliveryDate + '\'' +
                ", courierDispatchVendorName='" + courierDispatchVendorName + '\'' +
                ", courierDispatchNumber='" + courierDispatchNumber + '\'' +
                ", cpeModelEndofsale='" + cpeModelEndofsale + '\'' +
                ", cpeModelEndoflife='" + cpeModelEndoflife + '\'' +
                ", cpeSerialNumber='" + cpeSerialNumber + '\'' +
                ", cpeMinNumber='" + cpeMinNumber + '\'' +
                ", courierTrackNumber='" + courierTrackNumber + '\'' +
                ", vehicleDocketTrackNumber='" + vehicleDocketTrackNumber + '\'' +
                '}';
    }
}
