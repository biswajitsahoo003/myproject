package com.tcl.dias.customer.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.utils.Status;

/**
 * contains the response information about the file upload
 * 
 * 
 * @author SEKHAR ER
 *
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
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

	/**
	 * @return the urlPath
	 */
	public String getUrlPath() {
		return urlPath;
	}

	/**
	 * @param urlPath the urlPath to set
	 */
	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	@Override
	public String toString() {
		return "ServiceResponse [fileName=" + fileName + ", status=" + status + ", attachmentId=" + attachmentId + ", urlPath=" + urlPath + "]";
	}

}
