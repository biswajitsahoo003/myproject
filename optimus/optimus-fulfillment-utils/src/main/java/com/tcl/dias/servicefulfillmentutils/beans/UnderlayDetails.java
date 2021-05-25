package com.tcl.dias.servicefulfillmentutils.beans;

import java.sql.Timestamp;
import java.util.List;

public class UnderlayDetails {

    private Integer serviceId;
    private String serviceCode;
    private String productName;
    private String linkType;
    private String commissioningDate;
    private List<AttachmentIdBean> documentIds;

    public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

	public String getCommissioningDate() {
		return commissioningDate;
	}

	public void setCommissioningDate(String commissioningDate) {
		this.commissioningDate = commissioningDate;
	}

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }
}
