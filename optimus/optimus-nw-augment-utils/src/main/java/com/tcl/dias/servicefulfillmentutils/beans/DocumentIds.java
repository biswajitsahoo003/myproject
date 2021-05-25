package com.tcl.dias.servicefulfillmentutils.beans;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class DocumentIds extends TaskDetailsBaseBean {

    List<AttachmentIdBean> documentIds;

    private String customerPoNumber;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String customerPoDate;

	private String taxExemptionReason;

    public String getCustomerPoNumber() {
        return customerPoNumber;
    }

    public void setCustomerPoNumber(String customerPoNumber) {
        this.customerPoNumber = customerPoNumber;
    }

    public String getCustomerPoDate() {
        return customerPoDate;
    }

    public void setCustomerPoDate(String customerPoDate) {
        this.customerPoDate = customerPoDate;
    }

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }

    public String getTaxExemptionReason() {
        return taxExemptionReason;
    }

    public void setTaxExemptionReason(String taxExemptionReason) {
        this.taxExemptionReason = taxExemptionReason;
    }

    @Override
    public String toString() {
        return "DocumentIds{" +
                "documentIds=" + documentIds +
                ", customerPoNumber='" + customerPoNumber + '\'' +
                ", customerPoDate='" + customerPoDate + '\'' +
                ", taxExemptionReason='" + taxExemptionReason + '\'' +
                '}';
    }
}
