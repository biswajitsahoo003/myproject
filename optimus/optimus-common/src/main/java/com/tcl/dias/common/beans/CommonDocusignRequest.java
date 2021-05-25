package com.tcl.dias.common.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This file contains the CommonDocusignRequest.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CommonDocusignRequest {

	private String pdfHtml;
	private String toEmail;
	private String toName;
	private String type;
	private List<Approver> approvers;
	private Map<String, String> ccEmails = new HashMap<>();
	private Map<String, String> bccEmails = new HashMap<>();
	private Integer quoteId;
	private Integer quoteLeId;
	private String subject;
	private String fileName;
	private String documentId;
	private List<String> anchorStrings;
	private List<String> dateSignedAnchorStrings;
	private List<String> customerNameAnchorStrings;
	private List<String> approverDateAnchorStrings;
	
	private List<String> commercialAnchorStrings;
	private List<String> commercialDateSignedAnchorStrings;
	private List<String> commercialNameAnchorStrings;
	private List<String> commercialApproverDateAnchorStrings;
	
	private List<Approver> customerSigner = new ArrayList<>();

	public List<String> getDateSignedAnchorStrings() {
		return dateSignedAnchorStrings;
	}

	public void setDateSignedAnchorStrings(List<String> dateSignedAnchorStrings) {
		this.dateSignedAnchorStrings = dateSignedAnchorStrings;
	}

	public List<String> getCustomerNameAnchorStrings() {
		return customerNameAnchorStrings;
	}

	public void setCustomerNameAnchorStrings(List<String> customerNameAnchorStrings) {
		this.customerNameAnchorStrings = customerNameAnchorStrings;
	}

	public String getPdfHtml() {
		return pdfHtml;
	}

	public void setPdfHtml(String pdfHtml) {
		this.pdfHtml = pdfHtml;
	}

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	public Map<String, String> getCcEmails() {
		return ccEmails;
	}

	public void setCcEmails(Map<String, String> ccEmails) {
		this.ccEmails = ccEmails;
	}

	public Map<String, String> getBccEmails() {
		return bccEmails;
	}

	public void setBccEmails(Map<String, String> bccEmails) {
		this.bccEmails = bccEmails;
	}

	public Integer getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}

	public Integer getQuoteLeId() {
		return quoteLeId;
	}

	public void setQuoteLeId(Integer quoteLeId) {
		this.quoteLeId = quoteLeId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public List<String> getAnchorStrings() {
		return anchorStrings;
	}

	public void setAnchorStrings(List<String> anchorStrings) {
		this.anchorStrings = anchorStrings;
	}

	public List<Approver> getApprovers() {
		return approvers;
	}

	public void setApprovers(List<Approver> approvers) {
		this.approvers = approvers;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getApproverDateAnchorStrings() {
		return approverDateAnchorStrings;
	}

	public void setApproverDateAnchorStrings(List<String> approverDateAnchorStrings) {
		this.approverDateAnchorStrings = approverDateAnchorStrings;
	}

	public List<Approver> getCustomerSigner() {
		return customerSigner;
	}

	public void setCustomerSigner(List<Approver> customerSigner) {
		this.customerSigner = customerSigner;
	}

	public List<String> getCommercialAnchorStrings() {
		return commercialAnchorStrings;
	}

	public void setCommercialAnchorStrings(List<String> commercialAnchorStrings) {
		this.commercialAnchorStrings = commercialAnchorStrings;
	}


	public List<String> getCommercialDateSignedAnchorStrings() {
		return commercialDateSignedAnchorStrings;
	}

	public void setCommercialDateSignedAnchorStrings(List<String> commercialDateSignedAnchorStrings) {
		this.commercialDateSignedAnchorStrings = commercialDateSignedAnchorStrings;
	}

	public List<String> getCommercialNameAnchorStrings() {
		return commercialNameAnchorStrings;
	}

	public void setCommercialNameAnchorStrings(List<String> commercialNameAnchorStrings) {
		this.commercialNameAnchorStrings = commercialNameAnchorStrings;
	}

	public List<String> getCommercialApproverDateAnchorStrings() {
		return commercialApproverDateAnchorStrings;
	}

	public void setCommercialApproverDateAnchorStrings(List<String> commercialApproverDateAnchorStrings) {
		this.commercialApproverDateAnchorStrings = commercialApproverDateAnchorStrings;
	}

	@Override
	public String toString() {
		return "CommonDocusignRequest{" +
				"pdfHtml='" + pdfHtml + '\'' +
				", toEmail='" + toEmail + '\'' +
				", toName='" + toName + '\'' +
				", type='" + type + '\'' +
				", approvers=" + approvers +
				", ccEmails=" + ccEmails +
				", bccEmails=" + bccEmails +
				", quoteId=" + quoteId +
				", quoteLeId=" + quoteLeId +
				", subject='" + subject + '\'' +
				", fileName='" + fileName + '\'' +
				", documentId='" + documentId + '\'' +
				", anchorStrings=" + anchorStrings +
				", dateSignedAnchorStrings=" + dateSignedAnchorStrings +
				", customerNameAnchorStrings=" + customerNameAnchorStrings +
				", approverDateAnchorStrings=" + approverDateAnchorStrings +
				", customerSigner=" + customerSigner +
				'}';
	}
}
