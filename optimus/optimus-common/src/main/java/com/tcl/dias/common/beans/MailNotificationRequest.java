package com.tcl.dias.common.beans;

import java.util.List;
import java.util.Map;

import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * @author Manojkumar R
 *
 */
public class MailNotificationRequest {

	private List<String> to;
	private List<String> cc;
	private List<String> bcc;
	private String from;
	private String subject;
	private Map<String, Object> variable;
	private String templateId;
	private Boolean isAttachment = false;
	private String attachmentName;
	private String attachementHtml;
	private Map<String, String> cofObjectMapper;
	private String attachmentPath;
	private String notificationType;
	private String userEmailId;
	private String productName;
	private String notificationAction;
	private String referenceName;
	private String referenceValue;

	public String getNotificationAction() {
		return notificationAction;
	}

	public void setNotificationAction(String notificationAction) {
		this.notificationAction = notificationAction;
	}

	public String getUserEmailId() {
		return userEmailId;
	}

	public void setUserEmailId(String userEmailId) {
		this.userEmailId = userEmailId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public List<String> getCc() {
		return cc;
	}

	public void setCc(List<String> cc) {
		this.cc = cc;
	}

	public List<String> getBcc() {
		return bcc;
	}

	public void setBcc(List<String> bcc) {
		this.bcc = bcc;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Map<String, Object> getVariable() {
		return variable;
	}

	public void setVariable(Map<String, Object> variable) {
		this.variable = variable;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public Boolean getIsAttachment() {
		return isAttachment;
	}

	public void setIsAttachment(Boolean isAttachment) {
		this.isAttachment = isAttachment;
	}

	public String getAttachementHtml() {
		return attachementHtml;
	}

	public void setAttachementHtml(String attachementHtml) {
		this.attachementHtml = attachementHtml;
	}

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public String getAttachmentPath() {
		return attachmentPath;
	}

	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}

	public Map<String, String> getCofObjectMapper() {
		return cofObjectMapper;
	}

	public void setCofObjectMapper(Map<String, String> cofObjectMapper) {
		this.cofObjectMapper = cofObjectMapper;
	}

	public String getReferenceName() {
		return referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	public String getReferenceValue() {
		return referenceValue;
	}

	public void setReferenceValue(String referenceValue) {
		this.referenceValue = referenceValue;
	}

	public String toJsonString() throws TclCommonException {
		return Utils.convertObjectToJson(this);
	}

	@Override
	public String toString() {
		return "MailNotificationRequest [to=" + to + ", cc=" + cc + ", bcc=" + bcc + ", from=" + from + ", subject="
				+ subject + ", variable=" + variable + ", templateId=" + templateId + ", isAttachment=" + isAttachment
				+ ", attachmentName=" + attachmentName + ", attachementHtml=" + "******" + ", cofObjectMapper="
				+ cofObjectMapper + ", attachmentPath=" + attachmentPath + ", notificationType=" + notificationType
				+ ", userEmailId=" + userEmailId + ", productName=" + productName + ", notificationAction="
				+ notificationAction + "]";
	}

}
