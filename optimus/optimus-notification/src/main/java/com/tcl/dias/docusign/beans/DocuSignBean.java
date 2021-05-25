package com.tcl.dias.docusign.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DocuSignBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3774797885789431204L;
	private List<String> anchorList;
	private String documentId;
	private String envelopeId;
	private String signerName;
	private String signerEmail;
	private Map<String, String> ccMailer;
	private String documentName;
	private String status;
	private String signerFile;

	public List<String> getAnchorList() {
		return anchorList;
	}

	public void setAnchorList(List<String> anchorList) {
		this.anchorList = anchorList;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getEnvelopeId() {
		return envelopeId;
	}

	public void setEnvelopeId(String envelopeId) {
		this.envelopeId = envelopeId;
	}

	public String getSignerName() {
		return signerName;
	}

	public void setSignerName(String signerName) {
		this.signerName = signerName;
	}

	public String getSignerEmail() {
		return signerEmail;
	}

	public void setSignerEmail(String signerEmail) {
		this.signerEmail = signerEmail;
	}

	public Map<String, String> getCcMailer() {
		return ccMailer;
	}

	public void setCcMailer(Map<String, String> ccMailer) {
		this.ccMailer = ccMailer;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSignerFile() {
		return signerFile;
	}

	public void setSignerFile(String signerFile) {
		this.signerFile = signerFile;
	}

}
