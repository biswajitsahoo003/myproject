package com.tcl.dias.servicefulfillment.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

public class PrCpeInstallationBean extends TaskDetailsBaseBean {
    private String cpeInstallationPrNumber;
    private String cpeInstallationPrVendorName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String cpeInstallationPrDate;
    private String cpeSupportPrNumber;
    private String cpeSupportPrVendorName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String cpeSupportPrDate;
    private List<AttachmentIdBean> documentIds;
    private String cpeInstallationPrVendorEmailId;
    private String cpeSupportPrVendorEmailId;


    public String getCpeInstallationPrNumber() {
        return cpeInstallationPrNumber;
    }

    public void setCpeInstallationPrNumber(String cpeInstallationPrNumber) {
        this.cpeInstallationPrNumber = cpeInstallationPrNumber;
    }

    public String getCpeInstallationPrVendorName() {
        return cpeInstallationPrVendorName;
    }

    public void setCpeInstallationPrVendorName(String cpeInstallationPrVendorName) {
        this.cpeInstallationPrVendorName = cpeInstallationPrVendorName;
    }

    public String getCpeInstallationPrDate() {
        return cpeInstallationPrDate;
    }

    public void setCpeInstallationPrDate(String cpeInstallationPrDate) {
        this.cpeInstallationPrDate = cpeInstallationPrDate;
    }

    public String getCpeSupportPrNumber() {
        return cpeSupportPrNumber;
    }

    public void setCpeSupportPrNumber(String cpeSupportPrNumber) {
        this.cpeSupportPrNumber = cpeSupportPrNumber;
    }

    public String getCpeSupportPrVendorName() {
        return cpeSupportPrVendorName;
    }

    public void setCpeSupportPrVendorName(String cpeSupportPrVendorName) {
        this.cpeSupportPrVendorName = cpeSupportPrVendorName;
    }

    public String getCpeSupportPrDate() {
        return cpeSupportPrDate;
    }

    public void setCpeSupportPrDate(String cpeSupportPrDate) {
        this.cpeSupportPrDate = cpeSupportPrDate;
    }

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }

	public String getCpeInstallationPrVendorEmailId() {
		return cpeInstallationPrVendorEmailId;
	}

	public void setCpeInstallationPrVendorEmailId(String cpeInstallationPrVendorEmailId) {
		this.cpeInstallationPrVendorEmailId = cpeInstallationPrVendorEmailId;
	}

	public String getCpeSupportPrVendorEmailId() {
		return cpeSupportPrVendorEmailId;
	}

	public void setCpeSupportPrVendorEmailId(String cpeSupportPrVendorEmailId) {
		this.cpeSupportPrVendorEmailId = cpeSupportPrVendorEmailId;
	}

	@Override
    public String toString() {
        return "PrCpeInstallationBean{" +
                "cpeInstallationPrNumber='" + cpeInstallationPrNumber + '\'' +
                ", cpeInstallationPrVendorName='" + cpeInstallationPrVendorName + '\'' +
                ", cpeInstallationPrDate='" + cpeInstallationPrDate + '\'' +
                ", cpeSupportPrNumber='" + cpeSupportPrNumber + '\'' +
                ", cpeSupportPrVendorName='" + cpeSupportPrVendorName + '\'' +
                ", cpeSupportPrDate='" + cpeSupportPrDate + '\'' +
                ", cpeInstallationPrVendorEmailId='" + cpeInstallationPrVendorEmailId + '\'' +
                ", cpeSupportPrVendorEmailId='" + cpeSupportPrVendorEmailId + '\'' +
                ", documentIds=" + documentIds +
                '}';
    }
}
