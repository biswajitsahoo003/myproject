package com.tcl.dias.oms.gsc.beans;

import com.tcl.dias.oms.entity.entities.GscAttachments;

import java.util.Date;

public class GscAttachmentBean {
    private Integer documentId;
    private String documentCategory;
    private String documentType;
    private String documentUid;
    private Integer omsAttachmentId;
    private String status;
    private String templateName;
    private String uploadedBy;
    private Date uploadedTime;
    private String documentName;
    private Integer templateAttachmentId;
    private Boolean swiftEnabled;

    public static GscAttachmentBean fromGscAttachment(GscAttachments gscAttachment) {
        GscAttachmentBean bean = new GscAttachmentBean();
        bean.setDocumentId(gscAttachment.getId());
        bean.setDocumentCategory(gscAttachment.getDocumentCategory());
        bean.setDocumentName(gscAttachment.getDocumentName());
        bean.setDocumentType(gscAttachment.getDocumentType());
        bean.setDocumentUid(gscAttachment.getDocumentUId());
        bean.setOmsAttachmentId(gscAttachment.getOmsAttachment().getId());
        bean.setStatus(gscAttachment.getStatus());
        bean.setUploadedTime(gscAttachment.getUploadedTime());
        bean.setUploadedBy(gscAttachment.getUploadedBy());
        return bean;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public String getDocumentCategory() {
        return documentCategory;
    }

    public void setDocumentCategory(String documentCategory) {
        this.documentCategory = documentCategory;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentUid() {
        return documentUid;
    }

    public void setDocumentUid(String documentUid) {
        this.documentUid = documentUid;
    }

    public Integer getOmsAttachmentId() {
        return omsAttachmentId;
    }

    public void setOmsAttachmentId(Integer omsAttachmentId) {
        this.omsAttachmentId = omsAttachmentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public Date getUploadedTime() {
        return uploadedTime;
    }

    public void setUploadedTime(Date uploadedTime) {
        this.uploadedTime = uploadedTime;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public Integer getTemplateAttachmentId() {
        return templateAttachmentId;
    }

    public void setTemplateAttachmentId(Integer templateAttachmentId) {
        this.templateAttachmentId = templateAttachmentId;
    }

    public Boolean getSwiftEnabled() {
        return swiftEnabled;
    }

    public void setSwiftEnabled(Boolean swiftEnabled) {
        this.swiftEnabled = swiftEnabled;
    }
}
