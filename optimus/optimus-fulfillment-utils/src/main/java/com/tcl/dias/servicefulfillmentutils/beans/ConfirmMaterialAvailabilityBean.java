package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ConfirmMaterialAvailabilityBean extends TaskDetailsBaseBean {
    private String grnNumber;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String grnCreationDate;
    private String materialReceived;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String materialReceivedDate;
    private List<SerialNumberBean> serialNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String expectedCpeETADate;
    private String action;
    private String cpeSerialNumber;
    
    

    public String getMaterialReceivedDate() {
		return materialReceivedDate;
	}

	public void setMaterialReceivedDate(String materialReceivedDate) {
		this.materialReceivedDate = materialReceivedDate;
	}

	public String getMaterialReceived() {
        return materialReceived;
    }

    public void setMaterialReceived(String materialReceived) {
        this.materialReceived = materialReceived;
    }

    public List<SerialNumberBean> getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(List<SerialNumberBean> serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getGrnNumber() {
        return grnNumber;
    }

    public void setGrnNumber(String grnNumber) {
        this.grnNumber = grnNumber;
    }

    public String getGrnCreationDate() {
        return grnCreationDate;
    }

    public void setGrnCreationDate(String grnCreationDate) {
        this.grnCreationDate = grnCreationDate;
    }

    public String getExpectedCpeETADate() {
        return expectedCpeETADate;
    }

    public void setExpectedCpeETADate(String expectedCpeETADate) {
        this.expectedCpeETADate = expectedCpeETADate;
    }

    public String getCpeSerialNumber() {
		return cpeSerialNumber;
	}

	public void setCpeSerialNumber(String cpeSerialNumber) {
		this.cpeSerialNumber = cpeSerialNumber;
	}

	public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
    
    
    
    @Override
    public String toString() {
        return "ConfirmMaterialAvailabilityBean{" +
                "grnNumber='" + grnNumber + '\'' +
                ", grnCreationDate='" + grnCreationDate + '\'' +
                '}';
    }
}
