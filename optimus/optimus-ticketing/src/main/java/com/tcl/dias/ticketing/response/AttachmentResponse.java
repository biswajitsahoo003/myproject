package com.tcl.dias.ticketing.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.response.beans.AttachmentInfoBean;

/**
 * This file contains the AttachmentResponse.java class.
 * used to get the attachment response
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttachmentResponse {

	private String status;

	private String message;

	private String ticketId;

	private List<AttachmentInfoBean> attachmentInfo;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTicketId() {
		return ticketId;
	}

	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}

	public List<AttachmentInfoBean> getAttachmentInfo() {
		return attachmentInfo;
	}

	public void setAttachmentInfo(List<AttachmentInfoBean> attachmentInfo) {
		this.attachmentInfo = attachmentInfo;
	}

}
