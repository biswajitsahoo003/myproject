package com.tcl.dias.servicefulfillmentutils.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.utils.Status;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceResponse {

    private String fileName;
    private Status status;
    private Integer attachmentId;
    private String urlPath;
    private TempUploadUrlInfo tempUploadUrlInfo;
    private String documentId;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public TempUploadUrlInfo getTempUploadUrlInfo() {
        return tempUploadUrlInfo;
    }

    public void setTempUploadUrlInfo(TempUploadUrlInfo tempUploadUrlInfo) {
        this.tempUploadUrlInfo = tempUploadUrlInfo;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    @Override
    public String toString() {
        return "ServiceResponse{" +
                "fileName='" + fileName + '\'' +
                ", status=" + status +
                ", attachmentId=" + attachmentId +
                ", urlPath='" + urlPath + '\'' +
                ", tempUploadUrlInfo=" + tempUploadUrlInfo +
                ", documentId='" + documentId + '\'' +
                '}';
    }
}
