package com.tcl.dias.servicefulfillment.beans.webex;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import java.util.List;

public class PreparePrForEndpointBean extends TaskDetailsBaseBean {
    private String endpointInstallationPrNumber;
    private String endpointInstallationPrVendorName;
    private String endpointInstallationPrDate;
    private String endpointSupportPrNumber;
    private String endpointSupportPrVendorName;
    private String endpointSupportPrDate;
    private List<AttachmentIdBean> documentIds;

    public String getEndpointInstallationPrNumber() {
        return endpointInstallationPrNumber;
    }

    public void setEndpointInstallationPrNumber(String endpointInstallationPrNumber) {
        this.endpointInstallationPrNumber = endpointInstallationPrNumber;

    }

    public String getEndpointInstallationPrVendorName() {
        return endpointInstallationPrVendorName;
    }

    public void setEndpointInstallationPrVendorName(String endpointInstallationPrVendorName) {
        this.endpointInstallationPrVendorName = endpointInstallationPrVendorName;
    }

    public String getEndpointInstallationPrDate() {
        return endpointInstallationPrDate;
    }

    public void setEndpointInstallationPrDate(String endpointInstallationPrDate) {
        this.endpointInstallationPrDate = endpointInstallationPrDate;
    }

    public String getEndpointSupportPrNumber() {
        return endpointSupportPrNumber;
    }

    public void setEndpointSupportPrNumber(String endpointSupportPrNumber) {
        this.endpointSupportPrNumber = endpointSupportPrNumber;
    }

    public String getEndpointSupportPrVendorName() {
        return endpointSupportPrVendorName;
    }

    public void setEndpointSupportPrVendorName(String endpointSupportPrVendorName) {
        this.endpointSupportPrVendorName = endpointSupportPrVendorName;
    }

    public String getEndpointSupportPrDate() {
        return endpointSupportPrDate;
    }

    public void setEndpointSupportPrDate(String endpointSupportPrDate) {
        this.endpointSupportPrDate = endpointSupportPrDate;
    }

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }
}
