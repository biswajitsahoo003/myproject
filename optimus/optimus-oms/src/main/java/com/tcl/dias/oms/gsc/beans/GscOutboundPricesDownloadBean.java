package com.tcl.dias.oms.gsc.beans;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

/**
 * Bean for download outbound prices
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscOutboundPricesDownloadBean {

	private Integer attachmentId;

	private String tempUploadUrlInfo;

	private String documentId;

	private File file;

	private Integer quoteToLeId;

	private HttpServletResponse response;

	private Integer domesticVoiceOutboundAttachmentId;

	public Integer getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getTempUploadUrlInfo() {
		return tempUploadUrlInfo;
	}

	public void setTempUploadUrlInfo(String tempUploadUrlInfo) {
		this.tempUploadUrlInfo = tempUploadUrlInfo;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}

	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public Integer getDomesticVoiceOutboundAttachmentId() {
		return domesticVoiceOutboundAttachmentId;
	}

	public void setDomesticVoiceOutboundAttachmentId(Integer domesticVoiceOutboundAttachmentId) {
		this.domesticVoiceOutboundAttachmentId = domesticVoiceOutboundAttachmentId;
	}
}
