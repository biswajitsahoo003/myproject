package com.tcl.dias.servicefulfillment.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * CreateInventoryRecord - bean for Create Inventory Record api
 *
 * @author Vishesh Awasthi
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CreateInventoryRecord extends TaskDetailsBaseBean {

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String dateOfRecord;
    private String eorDetails;
    private String iorDetails;
    private String endMuxNodeIp;
    private String endMuxNodeName;
    private String gisId;
    private String endMuxNodePort;
    private List<MuxCardDetails> muxCardDetails;
    private String peEndPhysicalPort;
    private String isPEInternalCablingRequired;

    public List<MuxCardDetails> getMuxCardDetails() {
		return muxCardDetails;
	}

	public void setMuxCardDetails(List<MuxCardDetails> muxCardDetails) {
		this.muxCardDetails = muxCardDetails;
	}

	public String getDateOfRecord() {
        return dateOfRecord;
    }

    public void setDateOfRecord(String dateOfRecord) {
        this.dateOfRecord = dateOfRecord;
    }

    public String getEorDetails() {
        return eorDetails;
    }

    public void setEorDetails(String eorDetails) {
        this.eorDetails = eorDetails;
    }

    public String getIorDetails() {
        return iorDetails;
    }

    public void setIorDetails(String iorDetails) {
        this.iorDetails = iorDetails;
    }

    public String getEndMuxNodeIp() {
        return endMuxNodeIp;
    }

    public void setEndMuxNodeIp(String endMuxNodeIp) {
        this.endMuxNodeIp = endMuxNodeIp;
    }

    public String getEndMuxNodeName() {
        return endMuxNodeName;
    }

    public void setEndMuxNodeName(String endMuxNodeName) {
        this.endMuxNodeName = endMuxNodeName;
    }

    public String getEndMuxNodePort() {
        return endMuxNodePort;
    }

    public void setEndMuxNodePort(String endMuxNodePort) {
        this.endMuxNodePort = endMuxNodePort;
    }

	public String getGisId() {
		return gisId;
	}

	public void setGisId(String gisId) {
		this.gisId = gisId;
	}

    public String getPeEndPhysicalPort() {
        return peEndPhysicalPort;
    }

    public void setPeEndPhysicalPort(String peEndPhysicalPort) {
        this.peEndPhysicalPort = peEndPhysicalPort;
    }

    public String getIsPEInternalCablingRequired() {
        return isPEInternalCablingRequired;
    }

    public void setIsPEInternalCablingRequired(String isPEInternalCablingRequired) {
        this.isPEInternalCablingRequired = isPEInternalCablingRequired;
    }
}
