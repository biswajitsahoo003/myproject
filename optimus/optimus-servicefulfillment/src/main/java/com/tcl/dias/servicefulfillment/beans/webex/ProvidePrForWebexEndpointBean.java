package com.tcl.dias.servicefulfillment.beans.webex;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

public class ProvidePrForWebexEndpointBean extends TaskDetailsBaseBean {

    private String webexEndpointPrNumber;

    private String webexEndpointPrVendorName;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String webexEndpointPrDate;

    private List<AttachmentIdBean> documentIds;

    public String getWebexEndpointPrNumber() {
        return webexEndpointPrNumber;
    }

    public void setWebexEndpointPrNumber(String webexEndpointPrNumber) {
        this.webexEndpointPrNumber = webexEndpointPrNumber;
    }

    public String getWebexEndpointPrVendorName() {
        return webexEndpointPrVendorName;
    }

    public void setWebexEndpointPrVendorName(String webexEndpointPrVendorName) {
        this.webexEndpointPrVendorName = webexEndpointPrVendorName;
    }

    public String getWebexEndpointPrDate() {
        return webexEndpointPrDate;
    }

    public void setWebexEndpointPrDate(String webexEndpointPrDate) {
        this.webexEndpointPrDate = webexEndpointPrDate;
    }

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }
}
