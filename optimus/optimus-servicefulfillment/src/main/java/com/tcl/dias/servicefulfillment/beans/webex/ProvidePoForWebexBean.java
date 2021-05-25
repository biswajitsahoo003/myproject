package com.tcl.dias.servicefulfillment.beans.webex;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

public class ProvidePoForWebexBean extends TaskDetailsBaseBean{

    private String webexLicencePoNumber;

    private String webexLicencePoVendorName;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

    private String webexLicencePoDate;

    private List<AttachmentIdBean> documentIds;

    public String getWebexLicencePoNumber() {
        return webexLicencePoNumber;
    }

    public void setWebexLicencePoNumber(String webexLicencePoNumber) {
        this.webexLicencePoNumber = webexLicencePoNumber;
    }

    public String getWebexLicencePoVendorName() {
        return webexLicencePoVendorName;
    }

    public void setWebexLicencePoVendorName(String webexLicencePoVendorName) {
        this.webexLicencePoVendorName = webexLicencePoVendorName;
    }

    public String getWebexLicencePoDate() {
        return webexLicencePoDate;
    }

    public void setWebexLicencePoDate(String webexLicencePoDate) {
        this.webexLicencePoDate = webexLicencePoDate;
    }

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }
}
