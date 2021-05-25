package com.tcl.dias.servicefulfillment.beans.webex;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class DispatchEndpointBean extends TaskDetailsBaseBean {

    private String endpointMrnNumber;
    private String endpointMinNumber;
    private String courierDispatchVendorName;
    private String courierTrackNumber;
    private String endpointDispatchDate;
    private String distributionCenterName;
    private String distributionCenterAddress;
    private String endpointSerialNumber;
    private List<AttachmentIdBean> documentIds;
    
    private List<SerialNumberBean> serialNumber;

    public String getEndpointMrnNumber() {
        return endpointMrnNumber;
    }

    public void setEndpointMrnNumber(String endpointMrnNumber) {
        this.endpointMrnNumber = endpointMrnNumber;
    }

    public String getEndpointMinNumber() {
        return endpointMinNumber;
    }

    public void setEndpointMinNumber(String endpointMinNumber) {
        this.endpointMinNumber = endpointMinNumber;
    }

    public String getCourierDispatchVendorName() {
        return courierDispatchVendorName;
    }

    public void setCourierDispatchVendorName(String courierDispatchVendorName) {
        this.courierDispatchVendorName = courierDispatchVendorName;
    }

    public String getCourierTrackNumber() {
        return courierTrackNumber;
    }

    public void setCourierTrackNumber(String courierTrackNumber) {
        this.courierTrackNumber = courierTrackNumber;
    }

    public String getEndpointDispatchDate() {
        return endpointDispatchDate;
    }

    public void setEndpointDispatchDate(String endpointDispatchDate) {
        this.endpointDispatchDate = endpointDispatchDate;
    }

    public String getDistributionCenterName() {
        return distributionCenterName;
    }

    public void setDistributionCenterName(String distributionCenterName) {
        this.distributionCenterName = distributionCenterName;
    }

    public String getDistributionCenterAddress() {
        return distributionCenterAddress;
    }

    public void setDistributionCenterAddress(String distributionCenterAddress) {
        this.distributionCenterAddress = distributionCenterAddress;
    }

    public String getEndpointSerialNumber() {
        return endpointSerialNumber;
    }

    public void setEndpointSerialNumber(String endpointSerialNumber) {
        this.endpointSerialNumber = endpointSerialNumber;
    }

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }
    
	public List<SerialNumberBean> getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(List<SerialNumberBean> serialNumber) {
		this.serialNumber = serialNumber;
	}
}
