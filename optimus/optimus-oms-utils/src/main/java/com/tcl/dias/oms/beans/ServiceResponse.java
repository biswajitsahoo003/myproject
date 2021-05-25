package com.tcl.dias.oms.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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

	/**
	 * This Returns the file name
	 * 
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * This Sets the file name
	 * 
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * This Returns the status
	 * 
	 * @return
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * This sets the status
	 * 
	 * @param success
	 */
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

	@Override
	public String toString() {
		return "ServiceResponse{" +
				"fileName='" + fileName + '\'' +
				", status=" + status +
				", attachmentId=" + attachmentId +
				", urlPath='" + urlPath + '\'' +
				'}';
	}
}
