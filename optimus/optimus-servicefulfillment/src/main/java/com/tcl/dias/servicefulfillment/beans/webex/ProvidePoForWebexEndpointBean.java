package com.tcl.dias.servicefulfillment.beans.webex;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

public class ProvidePoForWebexEndpointBean extends TaskDetailsBaseBean{

    private String webexEndpointPoNumber;

    private String webexEndpointPoVendorName;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

    private String webexEndpointPoDate;

    private List<AttachmentIdBean> documentIds;

    public String getWebexEndpointPoNumber() {
        return webexEndpointPoNumber;
    }

    public void setWebexEndpointPoNumber(String webexEndpointPoNumber) {
        this.webexEndpointPoNumber = webexEndpointPoNumber;
    }

    public String getWebexEndpointPoVendorName() {
        return webexEndpointPoVendorName;
    }

    public void setWebexEndpointPoVendorName(String webexEndpointPoVendorName) {
        this.webexEndpointPoVendorName = webexEndpointPoVendorName;
    }

    public String getWebexEndpointPoDate() {
        return webexEndpointPoDate;
    }

    public void setWebexEndpointPoDate(String webexEndpointPoDate) {
        this.webexEndpointPoDate = webexEndpointPoDate;
    }

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }
}
