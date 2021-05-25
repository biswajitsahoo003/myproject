package com.tcl.dias.servicefulfillment.beans.webex;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

public class ProvidePrForWebexBean extends TaskDetailsBaseBean {

    private String webexLicencePrNumber;

    private String webexLicencePrVendorName;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String webexLicencePrDate;

    private List<AttachmentIdBean> documentIds;

    public String getWebexLicencePrNumber() {
        return webexLicencePrNumber;
    }

    public void setWebexLicencePrNumber(String webexLicencePrNumber) {
        this.webexLicencePrNumber = webexLicencePrNumber;
    }

    public String getWebexLicencePrVendorName() {
        return webexLicencePrVendorName;
    }

    public void setWebexLicencePrVendorName(String webexLicencePrVendorName) {
        this.webexLicencePrVendorName = webexLicencePrVendorName;
    }

    public String getWebexLicencePrDate() {
        return webexLicencePrDate;
    }

    public void setWebexLicencePrDate(String webexLicencePrDate) {
        this.webexLicencePrDate = webexLicencePrDate;
    }

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }
}
