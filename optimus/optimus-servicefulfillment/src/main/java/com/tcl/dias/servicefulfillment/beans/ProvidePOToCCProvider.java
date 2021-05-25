package com.tcl.dias.servicefulfillment.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * This class is used for task - Provide PO details to Cross-Connect Provider.
 *
 *
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class ProvidePOToCCProvider extends TaskDetailsBaseBean {

    private String crossConnectPONumber;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String crossConnectPODate;
    private List<AttachmentIdBean> documentIds;
    private String supplierCAFNumber;
    private String crossConnectProvider;
    private String crossConnectRequired;

    public String getCrossConnectPONumber() {
        return crossConnectPONumber;
    }

    public void setCrossConnectPONumber(String crossConnectPONumber) {
        this.crossConnectPONumber = crossConnectPONumber;
    }

    public String getCrossConnectPODate() {
        return crossConnectPODate;
    }

    public void setCrossConnectPODate(String crossConnectPODate) {
        this.crossConnectPODate = crossConnectPODate;
    }

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }

    public String getSupplierCAFNumber() {
        return supplierCAFNumber;
    }

    public void setSupplierCAFNumber(String supplierCAFNumber) {
        this.supplierCAFNumber = supplierCAFNumber;
    }

    public String getCrossConnectProvider() {
        return crossConnectProvider;
    }

    public void setCrossConnectProvider(String crossConnectProvider) {
        this.crossConnectProvider = crossConnectProvider;
    }

    public String getCrossConnectRequired() {
        return crossConnectRequired;
    }

    public void setCrossConnectRequired(String crossConnectRequired) {
        this.crossConnectRequired = crossConnectRequired;
    }
}
